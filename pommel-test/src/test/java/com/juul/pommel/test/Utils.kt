package com.juul.pommel.test

import com.tschuchort.compiletesting.KotlinCompilation
import org.assertj.core.api.AbstractFileAssert
import org.assertj.core.api.AbstractStringAssert
import org.intellij.lang.annotations.Language
import java.io.File

fun KotlinCompilation.Result.getGeneratedFile(name: String): File =
    checkNotNull(generatedFiles.firstOrNull { it.name == name }) {
        "Unable to find generated file: $name"
    }

/** Wrapper around [AbstractStringAssert.isEqualTo] for Kotlin code literals to syntax highlight in Android Studio. */
fun AbstractFileAssert<*>.isEqualToJava(
    @Language("java") source: String
): AbstractFileAssert<*> = this.hasContent(source.trimIndent())
