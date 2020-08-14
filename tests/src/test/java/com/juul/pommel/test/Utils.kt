package com.juul.pommel.test

import com.tschuchort.compiletesting.KotlinCompilation
import org.assertj.core.api.AbstractFileAssert
import org.intellij.lang.annotations.Language
import java.io.File

internal const val GENERATED_ANNOTATION =
    """@Generated(
             value = "com.juul.pommel.compiler.PommelProcessor",
             comments = "https://github.com/JuulLabs/pommel"
         )"""

internal fun KotlinCompilation.Result.getGeneratedFile(name: String): File =
    checkNotNull(generatedFiles.firstOrNull { it.name == name }) {
        "Unable to find generated file: $name"
    }

/** Wrapper around [AbstractFileAssert.hasContent] for Java code literals to syntax highlight in Android Studio. */
internal fun AbstractFileAssert<*>.isEqualToJava(
    @Language("java") source: String
): AbstractFileAssert<*> = this.hasContent(source.trimIndent())
