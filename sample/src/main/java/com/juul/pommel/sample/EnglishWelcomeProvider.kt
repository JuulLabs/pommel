package com.juul.pommel.sample

import com.juul.pommel.annotations.SoloModule
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@SoloModule(SingletonComponent::class, WelcomeProvider::class)
class EnglishWelcomeProvider @Inject constructor() : WelcomeProvider {

    override fun greeting(): String {
        return "Hello"
    }
}
