package com.juul.pommel.compiler

import javax.lang.model.AnnotatedConstruct
import javax.lang.model.element.TypeElement

/** Return true if this [AnnotatedConstruct] is annotated with `qualifiedName`. */
internal fun AnnotatedConstruct.hasAnnotation(qualifiedName: String) = getAnnotation(qualifiedName) != null

/** Return the first annotation matching [qualifiedName] or null. */
internal fun AnnotatedConstruct.getAnnotation(qualifiedName: String) = annotationMirrors
    .firstOrNull {
        it.annotationType.asElement().cast<TypeElement>().qualifiedName.contentEquals(qualifiedName)
    }
