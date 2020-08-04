package com.juul.pommel.sample

import com.juul.pommel.annotations.SoloModule
import javax.inject.Inject

@SoloModule(NameProvider::class)
class EnglishNameProvider @Inject constructor() : NameProvider {

    override fun name(): String {
        return "Pommel"
    }
}
