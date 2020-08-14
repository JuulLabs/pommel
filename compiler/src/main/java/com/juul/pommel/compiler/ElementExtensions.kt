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
    val install = soloModule.install
    val typeName = checkNotNull(getTypeMirror { soloModule.bindingClass }).toTypeName()
    // Calling toTypeName() on a function will throw an IllegalArgumentException since a developer can
    // directly specify the return type of a function directly, you can ignore this annotation parameter
    // better to call element.returnType to get the return value of a function
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

    val component = if (scope != null) {
        when (scope.type.toString()) {
            SINGLETON_SCOPED -> applicationComponent
            ACTIVITY_RETAINED_SCOPED -> activityRetainedComponent
            ACTIVITY_SCOPED -> activityComponent
            FRAGMENT_SCOPED -> fragmentComponent
            SERVICE_SCOPED -> serviceComponent
            VIEW_SCOPED -> viewComponent
            else -> null // custom scope
        }
    } else {
        applicationComponent
    }

    return SoloModuleParams(
        component = component,
        scope = scope,
        qualifier = qualifier,
        install = install,
        bindingType = bindingType
    )
}
