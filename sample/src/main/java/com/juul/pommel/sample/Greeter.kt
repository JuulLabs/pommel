package com.juul.pommel.sample

import com.juul.pommel.annotations.SoloModule
import javax.inject.Inject
import javax.inject.Named

@SoloModule
class Greeter @Inject constructor(
    private val welcomeProvider: WelcomeProvider,
    @Named("NameProvider") private val nameProvider: NameProvider,
    @Named("subGreeting") private val subGreeting: String,
    @Named("QuestionProvider") private val question: Question
) {

    fun greet(): String {
        return "${welcomeProvider.greeting()}, ${nameProvider.name()}!"
    }

    fun subGreeting(): String {
        return subGreeting
    }

    fun question(): String {
        return question.question()
    }
}
