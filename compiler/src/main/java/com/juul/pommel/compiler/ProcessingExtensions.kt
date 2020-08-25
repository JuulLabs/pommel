package com.juul.pommel.compiler

import javax.annotation.processing.Messager
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.tools.Diagnostic

/** Return a list of elements annotated with `T`. */
internal inline fun <reified T : Annotation> RoundEnvironment.getElementsAnnotatedWith(): Set<Element> =
    getElementsAnnotatedWith(T::class.java)

internal fun Messager.error(message: String, element: Element? = null) {
    printMessage(Diagnostic.Kind.ERROR, message, element)
}

internal fun Messager.warn(message: String, element: Element? = null) {
    printMessage(Diagnostic.Kind.WARNING, message, element)
}
