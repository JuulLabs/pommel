package com.juul.pommel.sample

import com.juul.pommel.annotations.SoloModule
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Named

@SoloModule(NameProvider::class, SingletonComponent::class)
@Named("NameProvider")
class EnglishNameProvider @Inject constructor() : NameProvider {

    override fun name(): String {
        return "Pommel"
    }
}
