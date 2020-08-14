package com.juul.pommel.test

import com.juul.pommel.compiler.PommelProcessor
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.junit.Rule
import org.junit.rules.TemporaryFolder

abstract class PommelProcessorTests {

    @Rule
    @JvmField val temporaryFolder: TemporaryFolder = TemporaryFolder()

    protected fun compile(vararg sourceFiles: SourceFile): KotlinCompilation.Result {
        return KotlinCompilation().apply {
            workingDir = temporaryFolder.root
            inheritClassPath = true
            sources = sourceFiles.toList()
            // Dirty hack to get around the fact that I can't seem to get
            // Gradle to see the `Processor` class in Android projects
            this::class.java.declaredMethods
                .single { it.name == "setAnnotationProcessors" }
                .invoke(this, listOf(PommelProcessor()))
        }.compile()
    }
}
