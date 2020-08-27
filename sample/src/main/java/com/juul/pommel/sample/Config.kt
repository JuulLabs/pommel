package com.juul.pommel.sample

import com.juul.pommel.annotations.SoloModule
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@SoloModule(installIn = SingletonComponent::class)
@Named("subGreeting")
fun subGreeting(): String {
    return "Good Morning!"
}
