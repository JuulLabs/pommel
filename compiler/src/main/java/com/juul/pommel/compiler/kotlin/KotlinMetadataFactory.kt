package com.juul.pommel.compiler.kotlin

import com.juul.pommel.compiler.extensions.hasAnnotation
import com.juul.pommel.compiler.utils.METADATA_ANNOTATION
import javax.annotation.processing.Messager
import javax.lang.model.element.Element

internal object KotlinMetadataFactory {

    private val metadataCache = mutableMapOf<Element, KotlinMetadata>()

    fun create(element: Element, messager: Messager?): KotlinMetadata {
        val enclosingElement = element.enclosingElement
        if (!enclosingElement.hasAnnotation(METADATA_ANNOTATION)) {
            error("Missing @Metadata for: $enclosingElement")
        }
        return metadataCache.getOrPut(enclosingElement) {
            KotlinMetadata.fromElement(enclosingElement, messager)
        }
    }
}
