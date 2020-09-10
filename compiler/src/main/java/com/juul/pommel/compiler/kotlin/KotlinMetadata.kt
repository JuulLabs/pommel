package com.juul.pommel.compiler.kotlin

import com.juul.pommel.compiler.extensions.note
import kotlinx.metadata.ClassName
import kotlinx.metadata.Flag
import kotlinx.metadata.Flags
import kotlinx.metadata.KmClassVisitor
import kotlinx.metadata.jvm.KotlinClassHeader
import kotlinx.metadata.jvm.KotlinClassMetadata
import javax.annotation.processing.Messager
import javax.lang.model.element.Element

internal class KotlinMetadata private constructor(private val flags: Flags) {

    fun isObjectClass(): Boolean {
        return Flag.Class.IS_OBJECT.invoke(flags)
    }

    fun isCompanionObjectClass(): Boolean {
        return Flag.Class.IS_COMPANION_OBJECT.invoke(flags)
    }

    companion object {
        fun fromElement(element: Element, messager: Messager?): KotlinMetadata {
            val visitor = MetadataVisitor()
            when (val classMetadata = metadataOf(element)) {
                is KotlinClassMetadata.Class -> classMetadata.accept(visitor)
                else -> messager?.note("Unsupported metadata type: + $classMetadata", element)
            }
            return KotlinMetadata(visitor.classFlags)
        }
    }
}

private class MetadataVisitor : KmClassVisitor() {

    var classFlags: Int = 0

    override fun visit(flags: Flags, name: ClassName) {
        classFlags = flags
    }
}

private fun metadataOf(element: Element): KotlinClassMetadata {
    val annotation = element.getAnnotation(Metadata::class.java)
    checkNotNull(annotation)
    val header = KotlinClassHeader(
        annotation.kind,
        annotation.metadataVersion,
        annotation.metadataVersion,
        annotation.data1,
        annotation.data2,
        annotation.extraString,
        annotation.packageName,
        annotation.extraInt
    )
    return when (val metadata = KotlinClassMetadata.read(header)) {
        null -> {
            // Should only happen on Kotlin < 1.0 (i.e. metadata version < 1.1)
            error("Unsupported metadata version. Check that your Kotlin version is >= 1.0")
        }
        else -> metadata
    }
}
