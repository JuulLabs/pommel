package com.juul.pommel.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.BINARY)
annotation class SoloModule(
    val bindingClass: KClass<*> = Nothing::class,
    val installIn: KClass<*>
)
