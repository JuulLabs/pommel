package com.juul.pommel.compiler.kotlin

import com.juul.pommel.compiler.METADATA_ANNOTATION
import com.juul.pommel.compiler.hasAnnotation
import javax.annotation.processing.Messager
import javax.lang.model.element.Element

object KotlinMetadataFactory {

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
