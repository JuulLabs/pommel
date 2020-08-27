package com.juul.pommel.sample

import com.juul.pommel.annotations.SoloModule
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

interface Question {
    fun question(): String

    private class EnglishQuestion : Question {

        override fun question(): String {
            return "How are you?"
        }
    }

    companion object Factory {
        @SoloModule(installIn = SingletonComponent::class)
        @Named("QuestionProvider")
        fun create(): Question = EnglishQuestion()
    }
}
