package com.juul.pommel.sample

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Named

/**
 * Roboeletric has limitation with Dagger-Hilt. Running this test from the IDE will fail.
 * To run this test run from the command line with the gradle task ./gradlew :sample:test
 *
 * You can read more about it here
 *
 * https://dagger.dev/hilt/gradle-setup.html#running-with-android-studio
 */
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@HiltAndroidTest
@UninstallModules(
    value = [
        EnglishWelcomeProvider_SoloModule::class,
        EnglishNameProvider_SoloModule::class,
        subGreeting_SoloModule::class,
        create_SoloModule::class
    ]
)
class PommelSampleUnitTest {

    @Rule(order = 0)
    @JvmField
    val hiltRule: HiltAndroidRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    val frenchWelcomeProvider: WelcomeProvider = FrenchWelcomeProvider()

    @BindValue
    @JvmField
    @field:Named("NameProvider")
    val frenchNameProvider: NameProvider = FrenchNameProvider()

    @BindValue
    @JvmField
    @field:Named("subGreeting")
    val subGreeting: String = "Bonjour!"

    @BindValue
    @JvmField
    @field:Named("QuestionProvider")
    val frenchQuestion: Question = object : Question {
        override fun question(): String {
            return "Comment ca va?"
        }
    }

    @Test
    fun text_is_displayed() {
        val scenario = launchActivity<MainActivity>()
        onView(withId(R.id.hello)).check(matches(withText("Bonjour, Pommeau!")))
        onView(withId(R.id.sub_greeting)).check(matches(withText("Bonjour!")))
        onView(withId(R.id.question)).check(matches(withText("Comment ca va?")))
        scenario.close()
    }
}
