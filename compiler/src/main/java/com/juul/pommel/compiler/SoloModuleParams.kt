package com.juul.pommel.compiler

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName

/** parameters that are retrieved from @SoloModule annotation **/
internal data class SoloModuleParams(
    val component: ClassName?,
    val scope: AnnotationSpec?,
    val qualifier: AnnotationSpec?,
    val install: Boolean,
    val bindingType: TypeName
)
