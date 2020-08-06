package com.juul.pommel.sample

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Named

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(
    value = [
        EnglishWelcomeProvider_SoloModule::class,
        EnglishNameProvider_SoloModule::class
    ]
)
class PommelSampleTest {

    @Rule(order = 0)
    @JvmField
    val hiltRule: HiltAndroidRule = HiltAndroidRule(this)

    @Rule(order = 1)
    @JvmField
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @BindValue
    @JvmField
    @field:Named("NameProvider")
    val testNameProvider: NameProvider = SpanishNameProvider()

    @BindValue
    @JvmField
    val spanishWelcomeProvider: WelcomeProvider = SpanishWelcomeProvider()

    @Test
    fun text_is_displayed() {
        Espresso.onView(ViewMatchers.withText("Hola, Pomo!"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}
