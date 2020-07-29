package com.juul.pommel.compiler

import com.juul.pommel.compiler.internal.applyEach
import com.juul.pommel.compiler.internal.hasAnnotation
import com.juul.pommel.compiler.internal.toTypeName
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

private val MODULE = ClassName.get("dagger", "Module")
private val PROVIDES = ClassName.get("dagger", "Provides")
private val INSTALLIN = ClassName.get("dagger.hilt", "InstallIn")

class PommelWriter(
    val moduleType: TypeElement,
    val targetType: TypeName,
    val scope: AnnotationSpec?,
    val component: ClassName,
    val parameters: List<VariableElement>,
    val install: Boolean,
    val binds: TypeName
) {

    val generatedType = moduleType.toClassName().soloModuleName()

    fun writeModule(): TypeSpec {
        return TypeSpec.classBuilder(generatedType)
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(MODULE)
            .apply {
                if (install) {
                    addAnnotation(
                        AnnotationSpec.builder(INSTALLIN)
                            .addMember("value", "\$T.\$L", component, "class")
                            .build()
                    )
                }
            }
            .addMethod(
                MethodSpec.methodBuilder(targetType.rawClassName().provideFunctionName())
                    .addAnnotation(PROVIDES)
                    .apply { scope?.let { addAnnotation(it) } }
                    .addModifiers(Modifier.PUBLIC)
                    .returns(binds)
                    .applyEach(parameters) {
                        addParameter(it.qualifiedType, it.simpleName.toString())
                    }
                    .addStatement(
                        "return new \$T(\n\$L)", targetType,
                        parameters.map { CodeBlock.of("\$N", it.simpleName) }.joinToCode(",\n")
                    )
                    .build()
            )
            .build()
    }
}

private fun TypeElement.toClassName(): ClassName = ClassName.get(this)

private fun ClassName.provideFunctionName() = "provides_" + reflectionName().replace('.', '_')

private fun ClassName.soloModuleName(): ClassName {
    return peerClassWithReflectionNesting(simpleName() + "_SoloModule")
}

private fun ClassName.peerClassWithReflectionNesting(name: String): ClassName {
    var prefix = ""
    var peek = this
    while (true) {
        peek = peek.enclosingClassName() ?: break
        prefix = peek.simpleName() + "$" + prefix
    }
    return ClassName.get(packageName(), prefix + name)
}

private fun Iterable<CodeBlock>.joinToCode(separator: String = ", ") =
    CodeBlock.join(this, separator)

private val VariableElement.qualifiedType: TypeName
    get() {
        val type = asType().toTypeName()
        val qualifier = qualifier()
        return if (qualifier != null) {
            type.annotated(qualifier)
        } else {
            type
        }
    }

private fun VariableElement.qualifier(): AnnotationSpec? {
    return annotationMirrors.find {
        it.annotationType.asElement().hasAnnotation("javax.inject.Qualifier")
    }?.let { AnnotationSpec.get(it) }
}

fun TypeName.rawClassName(): ClassName = when (this) {
    is ClassName -> this
    is ParameterizedTypeName -> rawType
    else -> throw IllegalStateException("Cannot extract raw class name from $this")
}
