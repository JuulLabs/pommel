package com.juul.pommel.compiler.generator

import com.juul.pommel.compiler.PommelModule
import com.juul.pommel.compiler.PommelProcessor
import com.juul.pommel.compiler.extensions.error
import com.juul.pommel.compiler.extensions.hasAnnotation
import com.juul.pommel.compiler.extensions.joinToCode
import com.juul.pommel.compiler.extensions.qualifiedType
import com.juul.pommel.compiler.extensions.rawClassName
import com.juul.pommel.compiler.extensions.soloModuleName
import com.juul.pommel.compiler.extensions.toClassName
import com.juul.pommel.compiler.extensions.toSoloModuleParams
import com.juul.pommel.compiler.extensions.toTypeName
import com.juul.pommel.compiler.utils.INJECT_ANNOTATION
import com.juul.pommel.compiler.utils.applyEach
import com.juul.pommel.compiler.utils.binds
import com.juul.pommel.compiler.utils.castEach
import com.juul.pommel.compiler.utils.generated
import com.juul.pommel.compiler.utils.installIn
import com.juul.pommel.compiler.utils.module
import com.juul.pommel.compiler.utils.provides
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

internal class ClassSoloModuleGenerator : SoloModuleGenerator {

    override fun isGeneratorFor(element: Element): Boolean {
        return element.kind == ElementKind.CLASS
    }

    override fun generate(pommelModule: PommelModule, element: Element): JavaFile {
        val generatedType = pommelModule.moduleType.toClassName().soloModuleName()
        val spec = TypeSpec.classBuilder(generatedType)
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .addAnnotation(
                AnnotationSpec.builder(generated)
                    .addMember("value", "\$S", PommelProcessor::class.qualifiedName)
                    .addMember("comments", "\$S", "https://github.com/JuulLabs/pommel")
                    .build()
            )
            .addAnnotation(module)
            .addAnnotation(
                AnnotationSpec.builder(installIn)
                    .addMember("value", "\$T.\$L", pommelModule.component, "class")
                    .build()
            )
            .addMethod(
                // if the value of returnType is equal to the target class annotated with @SoloModule
                // then we generate a provides method otherwise the value of the target class is a
                // implementation of returnType and we generate a binds method
                if (pommelModule.targetType == pommelModule.returnType) {
                    writeProvidesMethod(pommelModule)
                } else {
                    writeBindsMethod(pommelModule)
                }
            )
            .build()

        return JavaFile.builder(generatedType.packageName(), spec)
            .build()
    }

    override fun validate(element: Element, messager: Messager): PommelModule? {
        require(element is TypeElement)
        var valid = true

        if (Modifier.PUBLIC !in element.modifiers) {
            messager.error("Types marked with @SoloModule must be public", element)
            valid = false
        }

        if (element.enclosingElement.kind == ElementKind.CLASS && Modifier.STATIC !in element.modifiers) {
            messager.error("Nested types marked with @SoloModule must be static", element)
            valid = false
        }

        val constructors = element.enclosedElements
            .filter { it.kind == ElementKind.CONSTRUCTOR }
            .filter { it.hasAnnotation(INJECT_ANNOTATION) }
            .castEach<ExecutableElement>()

        if (constructors.size > 1) {
            messager.error("Multiple constructors marked with @Inject annotated found.", element)
            valid = false
        }

        if (!valid) return null

        val constructor = constructors.single()
        if (Modifier.PRIVATE in constructor.modifiers) {
            messager.error("@Inject constructor must not be private.", constructor)
            valid = false
        }

        val soloModuleParams = element.toSoloModuleParams()

        if (!valid) return null

        return PommelModule(
            moduleType = element,
            targetType = element.asType().toTypeName(),
            scope = soloModuleParams.scope,
            qualifier = soloModuleParams.qualifier,
            component = soloModuleParams.component,
            parameters = constructor.parameters,
            returnType = soloModuleParams.bindingType
        )
    }

    private fun writeProvidesMethod(pommelModule: PommelModule): MethodSpec {
        return MethodSpec.methodBuilder(pommelModule.targetType.rawClassName().provideFunctionName())
            .addAnnotation(provides)
            .apply { if (pommelModule.scope != null) addAnnotation(pommelModule.scope) }
            .apply { if (pommelModule.qualifier != null) addAnnotation(pommelModule.qualifier) }
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(pommelModule.returnType)
            .applyEach(pommelModule.parameters) {
                addParameter(it.qualifiedType, it.simpleName.toString())
            }
            .addStatement(
                "return new \$T(\n\$L)", pommelModule.targetType,
                pommelModule.parameters.map { CodeBlock.of("\$N", it.simpleName) }.joinToCode(",\n")
            )
            .build()
    }

    private fun writeBindsMethod(pommelModule: PommelModule): MethodSpec {
        return MethodSpec.methodBuilder(pommelModule.targetType.rawClassName().bindsFunctionName())
            .addAnnotation(binds)
            .apply { if (pommelModule.scope != null) addAnnotation(pommelModule.scope) }
            .apply { if (pommelModule.qualifier != null) addAnnotation(pommelModule.qualifier) }
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .returns(pommelModule.returnType)
            .addParameter(
                pommelModule.targetType,
                pommelModule.targetType.rawClassName().simpleName().decapitalize()
            )
            .build()
    }

    private fun ClassName.provideFunctionName() = "provides_" + reflectionName().replace('.', '_')

    private fun ClassName.bindsFunctionName() = "binds_" + reflectionName().replace('.', '_')
}
