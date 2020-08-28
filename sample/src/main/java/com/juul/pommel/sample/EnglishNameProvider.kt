package com.juul.pommel.sample

import com.juul.pommel.annotations.SoloModule
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Named

@SoloModule(installIn = SingletonComponent::class, bindingClass = NameProvider::class)
@Named("NameProvider")
class EnglishNameProvider @Inject constructor() : NameProvider {

    override fun name(): String {
        return "Pommel"
    }
}
