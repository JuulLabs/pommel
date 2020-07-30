package com.juul.pommel.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class SoloModule(
    val bindingClass: KClass<*> = Nothing::class,
    val install: Boolean = true
)
