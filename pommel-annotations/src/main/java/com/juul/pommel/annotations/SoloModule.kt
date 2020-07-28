package com.juul.pommel.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class SoloModule(
    val install: Boolean = true,
    val bindSuperType: Boolean = true,
    val bindingClass: KClass<*> = Any::class
)
