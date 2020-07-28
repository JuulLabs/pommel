package com.juul.pommel.test

import com.juul.pommel.compiler.PommelProcessor
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import kotlin.test.Test

class PommelProcessorTests {

    @Rule
    @JvmField val temporaryFolder: TemporaryFolder = TemporaryFolder()

    @Test
    fun `unscoped constructor injection`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject 
          
          @SoloModule
          class SampleClass @Inject constructor()

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("SampleClass_SoloModule.java")
        assertThat(file).isEqualToJava(
            """
         package test;
         
         import dagger.Module;
         import dagger.Provides;
         import dagger.hilt.InstallIn;
         import dagger.hilt.android.components.ApplicationComponent;
         
         @Module
         @InstallIn(ApplicationComponent.class)
         public class SampleClass_SoloModule {
           @Provides
           public SampleClass provides_test_SampleClass() {
             return new SampleClass(
                 );
           }
         }"""
        )
    }

    @Test
    fun `install is false `() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject 
          
          @SoloModule(install = false)
          class SampleClass @Inject constructor()

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("SampleClass_SoloModule.java")
        assertThat(file).isEqualToJava(
            """
         package test;
         
         import dagger.Module;
         import dagger.Provides;
         
         @Module
         public class SampleClass_SoloModule {
           @Provides
           public SampleClass provides_test_SampleClass() {
             return new SampleClass(
                 );
           }
         }"""
        )
    }

    @Test
    fun `Singleton scope `() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import javax.inject.Singleton 
          
          @SoloModule
          @Singleton
          class SampleClass @Inject constructor()

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("SampleClass_SoloModule.java")
        assertThat(file).isEqualToJava(
            """
         package test;

         import dagger.Module;
         import dagger.Provides;
         import dagger.hilt.InstallIn;
         import dagger.hilt.android.components.ApplicationComponent;
         import javax.inject.Singleton;
         
         @Module
         @InstallIn(ApplicationComponent.class)
         public class SampleClass_SoloModule {
           @Provides
           @Singleton
           public SampleClass provides_test_SampleClass() {
             return new SampleClass(
                 );
           }
         }"""
        )
    }

    @Test
    fun `with parameters`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import javax.inject.Singleton 
          
          @SoloModule
          @Singleton
          class SampleClass @Inject constructor(
              val a: Int,
              val b: String,
              val c: Double
          )

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("SampleClass_SoloModule.java")
        assertThat(file).isEqualToJava(
            """
         package test;

         import dagger.Module;
         import dagger.Provides;
         import dagger.hilt.InstallIn;
         import dagger.hilt.android.components.ApplicationComponent;
         import java.lang.String;
         import javax.inject.Singleton;
         
         @Module
         @InstallIn(ApplicationComponent.class)
         public class SampleClass_SoloModule {
           @Provides
           @Singleton
           public SampleClass provides_test_SampleClass(int a, String b, double c) {
             return new SampleClass(
                 a,
                 b,
                 c);
           }
         }"""
        )
    }

    @Test
    fun `with qualified parameters`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Singleton 
          
          @SoloModule
          @Singleton
          class SampleClass @Inject constructor(
              @Named("a" ) val a: Int,
              val b: String,
              val c: Double,
              @Named("b") val d: Byte
          )

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("SampleClass_SoloModule.java")
        assertThat(file).isEqualToJava(
            """
         package test;

         import dagger.Module;
         import dagger.Provides;
         import dagger.hilt.InstallIn;
         import dagger.hilt.android.components.ApplicationComponent;
         import java.lang.String;
         import javax.inject.Named;
         import javax.inject.Singleton;
         
         @Module
         @InstallIn(ApplicationComponent.class)
         public class SampleClass_SoloModule {
           @Provides
           @Singleton
           public SampleClass provides_test_SampleClass(@Named("a") int a, String b, double c,
               @Named("b") byte d) {
             return new SampleClass(
                 a,
                 b,
                 c,
                 d);
           }
         }"""
        )
    }

    @Test
    fun `bind abstract class`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Singleton 
          
          abstract class AbstractClass
          
          @SoloModule
          @Singleton
          class SampleClass @Inject constructor(
              @Named("a" ) val a: Int,
              val b: String,
              val c: Double,
              @Named("b") val d: Byte
          ) : AbstractClass()

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("SampleClass_SoloModule.java")
        assertThat(file).isEqualToJava(
            """
         package test;

         import dagger.Module;
         import dagger.Provides;
         import dagger.hilt.InstallIn;
         import dagger.hilt.android.components.ApplicationComponent;
         import java.lang.String;
         import javax.inject.Named;
         import javax.inject.Singleton;
         
         @Module
         @InstallIn(ApplicationComponent.class)
         public class SampleClass_SoloModule {
           @Provides
           @Singleton
           public AbstractClass provides_test_SampleClass(@Named("a") int a, String b, double c,
               @Named("b") byte d) {
             return new SampleClass(
                 a,
                 b,
                 c,
                 d);
           }
         }"""
        )
    }

    @Test
    fun `bind interface`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Singleton 
          
          interface TestInterface
          
          @SoloModule
          @Singleton
          class SampleClass @Inject constructor(
              @Named("a" ) val a: Int,
              val b: String,
              val c: Double,
              @Named("b") val d: Byte
          ) : TestInterface

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("SampleClass_SoloModule.java")
        assertThat(file).isEqualToJava(
            """
         package test;

         import dagger.Module;
         import dagger.Provides;
         import dagger.hilt.InstallIn;
         import dagger.hilt.android.components.ApplicationComponent;
         import java.lang.String;
         import javax.inject.Named;
         import javax.inject.Singleton;
         
         @Module
         @InstallIn(ApplicationComponent.class)
         public class SampleClass_SoloModule {
           @Provides
           @Singleton
           public TestInterface provides_test_SampleClass(@Named("a") int a, String b, double c,
               @Named("b") byte d) {
             return new SampleClass(
                 a,
                 b,
                 c,
                 d);
           }
         }"""
        )
    }

    @Test
    fun `specify binding interface`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Singleton 
          
          interface TestInterface
          
          interface SecondTestInterface
          
          @SoloModule(bindingClass = SecondTestInterface::class)
          @Singleton
          class SampleClass @Inject constructor(
              @Named("a" ) val a: Int,
              val b: String,
              val c: Double,
              @Named("b") val d: Byte
          ) : TestInterface, SecondTestInterface

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("SampleClass_SoloModule.java")
        assertThat(file).isEqualToJava(
            """
         package test;

         import dagger.Module;
         import dagger.Provides;
         import dagger.hilt.InstallIn;
         import dagger.hilt.android.components.ApplicationComponent;
         import java.lang.String;
         import javax.inject.Named;
         import javax.inject.Singleton;
         
         @Module
         @InstallIn(ApplicationComponent.class)
         public class SampleClass_SoloModule {
           @Provides
           @Singleton
           public SecondTestInterface provides_test_SampleClass(@Named("a") int a, String b, double c,
               @Named("b") byte d) {
             return new SampleClass(
                 a,
                 b,
                 c,
                 d);
           }
         }"""
        )
    }

    @Test
    fun `do not bind interface`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Singleton 
          
          interface TestInterface
          
          @SoloModule(bindSuperType = false)
          @Singleton
          class SampleClass @Inject constructor(
              @Named("a" ) val a: Int,
              val b: String,
              val c: Double,
              @Named("b") val d: Byte
          ) : TestInterface

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("SampleClass_SoloModule.java")
        assertThat(file).isEqualToJava(
            """
         package test;

         import dagger.Module;
         import dagger.Provides;
         import dagger.hilt.InstallIn;
         import dagger.hilt.android.components.ApplicationComponent;
         import java.lang.String;
         import javax.inject.Named;
         import javax.inject.Singleton;
         
         @Module
         @InstallIn(ApplicationComponent.class)
         public class SampleClass_SoloModule {
           @Provides
           @Singleton
           public SampleClass provides_test_SampleClass(@Named("a") int a, String b, double c,
               @Named("b") byte d) {
             return new SampleClass(
                 a,
                 b,
                 c,
                 d);
           }
         }"""
        )
    }

    @Test
    fun `compilation error from extending abstract class and interface `() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Singleton 
          
          interface TestInterface
          
          abstract class AbstractClass
          
          @SoloModule
          @Singleton
          class SampleClass @Inject constructor(
              @Named("a" ) val a: Int,
              val b: String,
              val c: Double,
              @Named("b") val d: Byte
          ) : AbstractClass(), TestInterface

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.messages).contains("error: Multiple super classes found. Binding type must be specified")
    }

    @Test
    fun `compilation error from extending multiple interfaces`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Singleton 
          
          interface TestInterface
          
          interface SecondInterface
          
          @SoloModule
          @Singleton
          class SampleClass @Inject constructor(
              @Named("a" ) val a: Int,
              val b: String,
              val c: Double,
              @Named("b") val d: Byte
          ) : SecondInterface, TestInterface

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.messages).contains("error: Multiple interfaces found. Binding type must be specified")
    }

    @Test
    fun `bind concrete implementation extending multiple interfaces`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Singleton 
          
          interface TestInterface
          
          interface OtherInterface
          
          @SoloModule(bindSuperType = false)
          @Singleton
          class SampleClass @Inject constructor(
              @Named("a" ) val a: Int,
              val b: String,
              val c: Double,
              @Named("b") val d: Byte
          ) : TestInterface, OtherInterface

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("SampleClass_SoloModule.java")
        assertThat(file).isEqualToJava(
            """
         package test;

         import dagger.Module;
         import dagger.Provides;
         import dagger.hilt.InstallIn;
         import dagger.hilt.android.components.ApplicationComponent;
         import java.lang.String;
         import javax.inject.Named;
         import javax.inject.Singleton;
         
         @Module
         @InstallIn(ApplicationComponent.class)
         public class SampleClass_SoloModule {
           @Provides
           @Singleton
           public SampleClass provides_test_SampleClass(@Named("a") int a, String b, double c,
               @Named("b") byte d) {
             return new SampleClass(
                 a,
                 b,
                 c,
                 d);
           }
         }"""
        )
    }

    @Test
    fun `bind concrete implementation extending interfaces and abstract class`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Singleton 
          
          interface TestInterface
          
          abstract class AbstractClass
          
          @SoloModule(bindSuperType = false)
          @Singleton
          class SampleClass @Inject constructor(
              @Named("a" ) val a: Int,
              val b: String,
              val c: Double,
              @Named("b") val d: Byte
          ) : AbstractClass(), TestInterface

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("SampleClass_SoloModule.java")
        assertThat(file).isEqualToJava(
            """
         package test;

         import dagger.Module;
         import dagger.Provides;
         import dagger.hilt.InstallIn;
         import dagger.hilt.android.components.ApplicationComponent;
         import java.lang.String;
         import javax.inject.Named;
         import javax.inject.Singleton;
         
         @Module
         @InstallIn(ApplicationComponent.class)
         public class SampleClass_SoloModule {
           @Provides
           @Singleton
           public SampleClass provides_test_SampleClass(@Named("a") int a, String b, double c,
               @Named("b") byte d) {
             return new SampleClass(
                 a,
                 b,
                 c,
                 d);
           }
         }"""
        )
    }

    private fun prepareCompilation(vararg sourceFiles: SourceFile): KotlinCompilation {
        return KotlinCompilation().apply {
            workingDir = temporaryFolder.root
            inheritClassPath = true
            sources = sourceFiles.toList()
            // Dirty hack to get around the fact that I can't seem to get
            // Gradle to see the `Processor` class in Android projects
            this::class.java.declaredMethods
                .single { it.name == "setAnnotationProcessors" }
                .invoke(this, listOf(PommelProcessor()))
        }
    }

    private fun compile(vararg sourceFiles: SourceFile): KotlinCompilation.Result {
        return prepareCompilation(*sourceFiles).compile()
    }
}
