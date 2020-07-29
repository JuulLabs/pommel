package com.juul.pommel.compiler

import com.google.auto.service.AutoService
import com.juul.pommel.annotations.SoloModule
import com.juul.pommel.compiler.internal.ACTIVITY_RETAINED_SCOPED
import com.juul.pommel.compiler.internal.ACTIVITY_SCOPED
import com.juul.pommel.compiler.internal.FRAGMENT_SCOPED
import com.juul.pommel.compiler.internal.INJECT_ANNOTATION
import com.juul.pommel.compiler.internal.JAVA_OBJECT
import com.juul.pommel.compiler.internal.SCOPE_ANNOTATION
import com.juul.pommel.compiler.internal.SERVICE_SCOPED
import com.juul.pommel.compiler.internal.SINGLETON_SCOPED
import com.juul.pommel.compiler.internal.VIEW_SCOPED
import com.juul.pommel.compiler.internal.activityComponent
import com.juul.pommel.compiler.internal.activityRetainedComponent
import com.juul.pommel.compiler.internal.applicationComponent
import com.juul.pommel.compiler.internal.castEach
import com.juul.pommel.compiler.internal.findElementsAnnotatedWith
import com.juul.pommel.compiler.internal.fragmentComponent
import com.juul.pommel.compiler.internal.getTypeMirror
import com.juul.pommel.compiler.internal.hasAnnotation
import com.juul.pommel.compiler.internal.serviceComponent
import com.juul.pommel.compiler.internal.toTypeName
import com.juul.pommel.compiler.internal.viewComponent
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.JavaFile
import net.ltgt.gradle.incap.IncrementalAnnotationProcessor
import net.ltgt.gradle.incap.IncrementalAnnotationProcessorType
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

@IncrementalAnnotationProcessor(IncrementalAnnotationProcessorType.ISOLATING)
@AutoService(Processor::class)
class PommelProcessor : AbstractProcessor() {

    override fun init(env: ProcessingEnvironment) {
        super.init(env)
        sourceVersion = env.sourceVersion
        messager = env.messager
        filer = env.filer
        elements = env.elementUtils
    }

    private lateinit var sourceVersion: SourceVersion
    private lateinit var messager: Messager
    private lateinit var filer: Filer
    private lateinit var elements: Elements

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()

    override fun getSupportedAnnotationTypes() = setOf(
        SoloModule::class.java.canonicalName
    )

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        roundEnv.findElementsAnnotatedWith<SoloModule>()
            .map { it as TypeElement }
            .mapNotNull { it.toPommelWriter() }
            .forEach(this::writeSoloModule)
        return false
    }

    private fun error(message: String, element: Element? = null) {
        messager.printMessage(Diagnostic.Kind.ERROR, message, element)
    }

    private fun writeSoloModule(writer: PommelWriter) {
        val generatedTypeSpec = writer.writeModule()
            .toBuilder()
            .addOriginatingElement(writer.moduleType)
            .build()

        JavaFile.builder(writer.generatedType.packageName(), generatedTypeSpec)
            .build()
            .writeTo(filer)
    }

    private fun TypeElement.toPommelWriter(): PommelWriter? {
        var valid = true

        if (Modifier.PRIVATE in modifiers) {
            error("Types marked with @SoloModule must not be private", this)
            valid = false
        }

        if (enclosingElement.kind == ElementKind.CLASS && Modifier.STATIC !in modifiers) {
            error("Nested types marked with @SoloModule must be static", this)
            valid = false
        }

        val scope = annotationMirrors.find {
            it.annotationType.asElement().hasAnnotation(SCOPE_ANNOTATION)
        }?.let {
            AnnotationSpec.get(it)
        }

        val component = if (scope != null) {
            when (scope.type.toString()) {
                SINGLETON_SCOPED -> applicationComponent
                ACTIVITY_RETAINED_SCOPED -> activityRetainedComponent
                ACTIVITY_SCOPED -> activityComponent
                FRAGMENT_SCOPED -> fragmentComponent
                SERVICE_SCOPED -> serviceComponent
                VIEW_SCOPED -> viewComponent
                else -> null
            }
        } else {
            applicationComponent
        }

        if (component == null) {
            error("@SoloModule does not support custom scopes--use Dagger-Hilt defined scopes or set install to false", this)
            valid = false
        }

        val constructors = enclosedElements
            .filter { it.kind == ElementKind.CONSTRUCTOR }
            .filter { it.hasAnnotation(INJECT_ANNOTATION) }
            .castEach<ExecutableElement>()

        if (constructors.size > 1) {
            error("Multiple constructors marked with @Inject annotated found.", this)
            valid = false
        }

        if (!valid) return null

        val constructor = constructors.single()
        if (Modifier.PRIVATE in constructor.modifiers) {
            error("@Inject constructor must not be private.", constructor)
            valid = false
        }

        if (!valid) return null

        val soloModule = checkNotNull(getAnnotation(SoloModule::class.java))
        val install = soloModule.install
        val typeName = checkNotNull(getTypeMirror { soloModule.bindingClass }).toTypeName()
        val binds = when {
            typeName.toString() == JAVA_OBJECT -> null
            else -> typeName
        }
        val bindSuperType = soloModule.bindSuperType && binds == null

        if (interfaces.size > 1 && bindSuperType) {
            error("Multiple interfaces found. Binding type must be specified", this)
            valid = false
        }

        if (interfaces.size >= 1 && superclass.toString() != JAVA_OBJECT && bindSuperType) {
            error("Multiple super classes found. Binding type must be specified", this)
            valid = false
        }

        if (!valid) return null

        val interfaceType = interfaces.singleOrNull()

        val bindingType = when {
            interfaceType != null && bindSuperType -> interfaceType.toTypeName()
            superclass.toString() != JAVA_OBJECT && bindSuperType -> superclass.toTypeName()
            binds != null -> binds
            else -> this.asType().toTypeName()
        }

        return PommelWriter(
            moduleType = this,
            targetType = this.asType().toTypeName(),
            scope = scope,
            component = checkNotNull(component),
            parameters = constructor.parameters,
            install = install,
            binds = bindingType
        )
    }
}
