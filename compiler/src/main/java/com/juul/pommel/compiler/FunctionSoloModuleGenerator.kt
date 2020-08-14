package com.juul.pommel.compiler

import com.squareup.javapoet.AnnotationSpec
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

internal class FunctionSoloModuleGenerator : SoloModuleGenerator {

    override fun isGeneratorFor(element: Element): Boolean {
        return element.kind == ElementKind.METHOD
    }

    override fun generate(pommelModule: PommelModule, element: Element): JavaFile {
        val funcName = (element.enclosingElement.asType().toTypeName().rawClassName().reflectionName().replace('.', '_'))
        val spec = TypeSpec.classBuilder(element.simpleName.toString() + "_SoloModule")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .addAnnotation(
                AnnotationSpec.builder(generated)
                    .addMember("value", "\$S", PommelProcessor::class.qualifiedName)
                    .addMember("comments", "\$S", "https://github.com/JuulLabs/pommel")
                    .build()
            )
            .addAnnotation(module)
            .apply {
                if (pommelModule.install && pommelModule.component != null) {
                    addAnnotation(
                        AnnotationSpec.builder(installIn)
                            .addMember("value", "\$T.\$L", pommelModule.component, "class")
                            .build()
                    )
                }
            }
            .addMethod(
                MethodSpec.methodBuilder("provides_$funcName" + "_" + element.simpleName.toString())
                    .addAnnotation(provides)
                    .apply { if (pommelModule.scope != null) addAnnotation(pommelModule.scope) }
                    .apply { if (pommelModule.qualifier != null) addAnnotation(pommelModule.qualifier) }
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(pommelModule.returnType)
                    .applyEach(pommelModule.parameters) { param ->
                        addParameter(param.qualifiedType, param.simpleName.toString())
                    }
                    .addStatement(
                        "return \$L.\$L(\n\$L)", pommelModule.moduleType.simpleName, element.simpleName,
                        pommelModule.parameters.map { parameter -> CodeBlock.of("\$N", parameter.simpleName) }.joinToCode(",\n")
                    )
                    .build()
            )
            .build()
        return JavaFile.builder(pommelModule.moduleType.toClassName().packageName(), spec)
            .build()
    }

    override fun validate(element: Element, messager: Messager): PommelModule? {
        require(element is ExecutableElement)
        var valid = true

        if (Modifier.PUBLIC !in element.modifiers) {
            messager.error("Functions marked with @SoloModule must be public", element)
            valid = false
        }

        if (Modifier.STATIC !in element.modifiers) {
            messager.error("Functions marked with @SoloModule must be static", element)
            valid = false
        }

        val soloModuleParams = element.toSoloModuleParams()
        if (soloModuleParams.component == null && soloModuleParams.install) {
            messager.error("@SoloModule does not support custom scopes--use Dagger-Hilt defined scopes or set install to false", element)
            valid = false
        }

        if (!valid) return null

        return PommelModule(
            moduleType = element.enclosingElement as TypeElement,
            targetType = (element.enclosingElement as TypeElement).asType().toTypeName(),
            scope = soloModuleParams.scope,
            qualifier = soloModuleParams.qualifier,
            component = soloModuleParams.component,
            parameters = element.parameters,
            install = soloModuleParams.install,
            returnType = soloModuleParams.bindingType
        )
    }
}
