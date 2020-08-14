package com.juul.pommel.compiler

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

/** Structure of the generated module class **/
internal data class PommelModule(
    val moduleType: TypeElement,
    val targetType: TypeName,
    val component: ClassName?,
    val parameters: List<VariableElement>,
    val scope: AnnotationSpec?,
    val qualifier: AnnotationSpec?,
    val install: Boolean,
    val returnType: TypeName
)
