package com.juul.pommel.sample

import com.juul.pommel.annotations.SoloModule
import javax.inject.Inject

@SoloModule(WelcomeProvider::class)
class EnglishWelcomeProvider @Inject constructor() : WelcomeProvider {

    override fun greeting(): String {
        return "Hello"
    }
}
