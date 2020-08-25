package com.juul.pommel.sample

import com.juul.pommel.annotations.SoloModule
import javax.inject.Named

interface Question {
    fun question(): String

    private class EnglishQuestion : Question {

        override fun question(): String {
            return "How are you?"
        }
    }

    companion object Factory {
        @SoloModule
        @Named("QuestionProvider")
        fun create(): Question = EnglishQuestion()
    }
}
