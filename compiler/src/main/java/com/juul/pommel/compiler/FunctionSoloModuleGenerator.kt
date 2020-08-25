package com.juul.pommel.compiler

import com.juul.pommel.compiler.kotlin.KotlinMetadataFactory
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

    /**
     *  object and companion classes can be deeply nested.
     *  This function will retrieve the fully chained name.
     *
     *  e.g.
     *
     *  class Sample {
     *      class Module {
     *          companion object Pommel {
     *              fun baseUrl(): String = "baseUrl"
     *          }
     *      }
     *  }
     *
     *  will return the following function call:
     *
     *  Sample.Module.Pommel.baseUrl()
     */
    private fun Element.fullFunctionName(): String {
        var enclosingElement = this
        var name = ""
        var instanceAdded = false
        // stop once you reach the package as they are no more enclosing elements beyond package
        // we only need to add 'INSTANCE' for the last nested object class
        // we can chain the other object classes
        // e.g. ObjectA.ObjectB.ObjectC.INSTANCE.baseUrl()
        while (enclosingElement.kind != ElementKind.PACKAGE) {
            val metadata = if (enclosingElement.enclosingElement.kind != ElementKind.PACKAGE && !instanceAdded) {
                KotlinMetadataFactory.create(enclosingElement, null)
            } else {
                // Kotlin Metadata annotation does not exist on a package
                // simply ignore and move on
                null
            }
            // do not add chained dot if this is the first iteration of the loop
            // you will end up with an extra chained dot at the end of the string
            val dot = if (name.isNotBlank()) "." else ""
            val objectClass = if (metadata?.isObjectClass() == true) {
                instanceAdded = true
                "INSTANCE."
            } else {
                ""
            }
            name = objectClass + enclosingElement.simpleName.toString() + dot + name
            enclosingElement = enclosingElement.enclosingElement
        }
        return name
    }

    override fun generate(pommelModule: PommelModule, element: Element): JavaFile {
        val funcName = (element.enclosingElement.asType().toTypeName().rawClassName().reflectionName().replace('.', '_'))
        val fullFunctionName = element.fullFunctionName()
        val className = fullFunctionName.replace('.', '_').replace("INSTANCE_", "")
        val spec = TypeSpec.classBuilder(className + "_SoloModule")
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
                        "return \$L(\n\$L)", fullFunctionName,
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

        val metadata = KotlinMetadataFactory.create(element, messager)

        if (Modifier.PUBLIC !in element.modifiers) {
            messager.error("Functions marked with @SoloModule must be public", element)
            valid = false
        }

        if (Modifier.STATIC !in element.modifiers && !metadata.isCompanionObjectClass() && !metadata.isObjectClass()) {
            messager.error("Functions marked with @SoloModule must be top level or enclosed in an object or companion class", element)
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
