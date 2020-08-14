package com.juul.pommel.compiler

import com.squareup.javapoet.JavaFile
import javax.annotation.processing.Messager
import javax.lang.model.element.Element

internal interface SoloModuleGenerator {
    fun isGeneratorFor(element: Element): Boolean
    fun generate(pommelModule: PommelModule, element: Element): JavaFile
    fun validate(element: Element, messager: Messager): PommelModule?
}

private val generators = listOf(
    ClassSoloModuleGenerator(),
    FunctionSoloModuleGenerator()
)

// @SoloModule is targeted to only be annotated on a class or function
// there shouldn't be a case where this does not return a valid generator
// if such a case does exist we should fail fast and crash
internal fun Element.getCodeGenerator(): SoloModuleGenerator =
    generators.first { it.isGeneratorFor(this) }
