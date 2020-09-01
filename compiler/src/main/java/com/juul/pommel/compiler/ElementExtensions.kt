package com.juul.pommel.compiler

import com.juul.pommel.annotations.SoloModule
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

internal fun TypeElement.toClassName(): ClassName = ClassName.get(this)

internal val VariableElement.qualifiedType: TypeName
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
        it.annotationType.asElement().hasAnnotation(QUALIFIER_ANNOTATION)
    }?.let { AnnotationSpec.get(it) }
}

internal fun Element.toSoloModuleParams(): SoloModuleParams {
    val soloModule = checkNotNull(getAnnotation(SoloModule::class.java))
    val component = checkNotNull(getTypeMirror { soloModule.installIn }).toTypeName()
    val typeName = checkNotNull(getTypeMirror { soloModule.bindingClass }).toTypeName()
    // Check if the current element is either a Class or Function
    // in order to get the appropriate return value of the element
    val bindingType = when {
        typeName.toString() == JAVA_VOID && this.kind == ElementKind.CLASS -> this.asType().toTypeName()
        typeName.toString() == JAVA_VOID && this.kind == ElementKind.METHOD -> (this as ExecutableElement).returnType.toTypeName()
        else -> typeName
    }

    val scope = annotationMirrors.find {
        it.annotationType.asElement().hasAnnotation(SCOPE_ANNOTATION)
    }?.let {
        AnnotationSpec.get(it)
    }

    val qualifier = annotationMirrors.find {
        it.annotationType.asElement().hasAnnotation(QUALIFIER_ANNOTATION)
    }?.let {
        AnnotationSpec.get(it)
    }

    return SoloModuleParams(
        component = component,
        scope = scope,
        qualifier = qualifier,
        bindingType = bindingType
    )
}
