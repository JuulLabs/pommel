package com.juul.pommel.compiler

import com.google.auto.service.AutoService
import com.juul.pommel.annotations.SoloModule
import com.juul.pommel.compiler.internal.activityComponent
import com.juul.pommel.compiler.internal.activityRetainedComponent
import com.juul.pommel.compiler.internal.activityRetainedScoped
import com.juul.pommel.compiler.internal.activityScoped
import com.juul.pommel.compiler.internal.applicationComponent
import com.juul.pommel.compiler.internal.castEach
import com.juul.pommel.compiler.internal.findElementsAnnotatedWith
import com.juul.pommel.compiler.internal.fragmentComponent
import com.juul.pommel.compiler.internal.fragmentScoped
import com.juul.pommel.compiler.internal.getTypeMirror
import com.juul.pommel.compiler.internal.hasAnnotation
import com.juul.pommel.compiler.internal.injectAnnotation
import com.juul.pommel.compiler.internal.javaObject
import com.juul.pommel.compiler.internal.scopeAnnotaion
import com.juul.pommel.compiler.internal.serviceComponent
import com.juul.pommel.compiler.internal.serviceScoped
import com.juul.pommel.compiler.internal.singletionScoped
import com.juul.pommel.compiler.internal.toTypeName
import com.juul.pommel.compiler.internal.viewComponent
import com.juul.pommel.compiler.internal.viewScoped
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
            error("@SoloModule-using types must not be private", this)
            valid = false
        }

        if (enclosingElement.kind == ElementKind.CLASS && Modifier.STATIC !in modifiers) {
            error("Nested @SoloModule--using types must be static", this)
            valid = false
        }

        val scope = annotationMirrors.find {
            it.annotationType.asElement().hasAnnotation(scopeAnnotaion)
        }?.let {
            AnnotationSpec.get(it)
        }

        val component = if (scope != null) {
            when (scope.type.toString()) {
                singletionScoped -> applicationComponent
                activityRetainedScoped -> activityRetainedComponent
                activityScoped -> activityComponent
                fragmentScoped -> fragmentComponent
                serviceScoped -> serviceComponent
                viewScoped -> viewComponent
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
            .filter { it.hasAnnotation(injectAnnotation) }
            .castEach<ExecutableElement>()

        if (constructors.size > 1) {
            error("Multiple @Inject-annotated constructors found.", this)
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
        val bindSuperType = soloModule.bindSuperType
        val typeName = checkNotNull(getTypeMirror { soloModule.bindingClass }).toTypeName()
        val binds = when {
            typeName.toString() == javaObject -> null
            else -> typeName
        }

        if (interfaces.size > 1 && binds == null && bindSuperType) {
            error("Multiple interfaces found. Binding type must be specified", this)
            valid = false
        }

        if (!valid) return null

        if (interfaces.size >= 1 && superclass.toString() != "java.lang.Object" && bindSuperType) {
            error("Multiple super classes found. Binding type must be specified", this)
            valid = false
        }

        if (!valid) return null

        val interfaceType = interfaces.singleOrNull()

        val bindingType = when {
            interfaceType != null && bindSuperType -> interfaceType.toTypeName()
            superclass.toString() != javaObject && bindSuperType -> superclass.toTypeName()
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
