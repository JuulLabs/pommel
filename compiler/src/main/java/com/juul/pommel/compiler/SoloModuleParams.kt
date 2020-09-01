package com.juul.pommel.compiler

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.TypeName

/** parameters that are retrieved from @SoloModule annotation **/
internal data class SoloModuleParams(
    val component: TypeName,
    val scope: AnnotationSpec?,
    val qualifier: AnnotationSpec?,
    val bindingType: TypeName
)
