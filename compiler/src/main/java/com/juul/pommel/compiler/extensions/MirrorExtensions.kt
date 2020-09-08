package com.juul.pommel.compiler.extensions

import com.squareup.javapoet.TypeName
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror

internal fun TypeMirror.toTypeName(): TypeName = TypeName.get(this)

internal fun getTypeMirror(block: () -> Unit): TypeMirror? {
    try {
        block()
    } catch (e: MirroredTypeException) {
        return e.typeMirror
    }
    return null
}
