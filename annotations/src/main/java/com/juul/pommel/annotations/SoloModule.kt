package com.juul.pommel.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.BINARY)
annotation class SoloModule(
    val installIn: KClass<*>,
    val bindingClass: KClass<*> = Nothing::class
)
