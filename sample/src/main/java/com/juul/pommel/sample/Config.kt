package com.juul.pommel.sample

import com.juul.pommel.annotations.SoloModule
import javax.inject.Named

@SoloModule
@Named("subGreeting")
fun subGreeting(): String {
    return "Good Morning!"
}
