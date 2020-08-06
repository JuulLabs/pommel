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
    fun `install in Singleton scope `() {
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
    fun `install in ActivityRetainedScoped`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import dagger.hilt.android.scopes.ActivityRetainedScoped
          
          @SoloModule
          @ActivityRetainedScoped
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
         import dagger.hilt.android.components.ActivityRetainedComponent;
         import dagger.hilt.android.scopes.ActivityRetainedScoped;
         
         @Module
         @InstallIn(ActivityRetainedComponent.class)
         public class SampleClass_SoloModule {
           @Provides
           @ActivityRetainedScoped
           public SampleClass provides_test_SampleClass() {
             return new SampleClass(
                 );
           }
         }"""
        )
    }

    @Test
    fun `install in ActivityScoped`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import dagger.hilt.android.scopes.ActivityScoped
          
          @SoloModule
          @ActivityScoped
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
         import dagger.hilt.android.components.ActivityComponent;
         import dagger.hilt.android.scopes.ActivityScoped;
         
         @Module
         @InstallIn(ActivityComponent.class)
         public class SampleClass_SoloModule {
           @Provides
           @ActivityScoped
           public SampleClass provides_test_SampleClass() {
             return new SampleClass(
                 );
           }
         }"""
        )
    }

    @Test
    fun `install in FragmentScoped`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import dagger.hilt.android.scopes.FragmentScoped
          
          @SoloModule
          @FragmentScoped
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
         import dagger.hilt.android.components.FragmentComponent;
         import dagger.hilt.android.scopes.FragmentScoped;
         
         @Module
         @InstallIn(FragmentComponent.class)
         public class SampleClass_SoloModule {
           @Provides
           @FragmentScoped
           public SampleClass provides_test_SampleClass() {
             return new SampleClass(
                 );
           }
         }"""
        )
    }

    @Test
    fun `install in ServiceScoped`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import dagger.hilt.android.scopes.ServiceScoped
          
          @SoloModule
          @ServiceScoped
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
         import dagger.hilt.android.components.ServiceComponent;
         import dagger.hilt.android.scopes.ServiceScoped;
         
         @Module
         @InstallIn(ServiceComponent.class)
         public class SampleClass_SoloModule {
           @Provides
           @ServiceScoped
           public SampleClass provides_test_SampleClass() {
             return new SampleClass(
                 );
           }
         }"""
        )
    }

    @Test
    fun `install in ViewScoped`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import dagger.hilt.android.scopes.ViewScoped
          
          @SoloModule
          @ViewScoped
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
         import dagger.hilt.android.components.ViewComponent;
         import dagger.hilt.android.scopes.ViewScoped;
         
         @Module
         @InstallIn(ViewComponent.class)
         public class SampleClass_SoloModule {
           @Provides
           @ViewScoped
           public SampleClass provides_test_SampleClass() {
             return new SampleClass(
                 );
           }
         }"""
        )
    }

    @Test
    fun `custom scope fails`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
        
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import javax.inject.Scope
          
          @Scope
          annotation class CustomScope
          
          @SoloModule
          @CustomScope
          class SampleClass @Inject constructor()

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.messages).contains("error: @SoloModule does not support custom scopes--use Dagger-Hilt defined scopes or set install to false")
    }

    @Test
    fun `custom scope with install false`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
        
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import javax.inject.Scope
          
          @Scope
          annotation class CustomScope
          
          @SoloModule(install = false)
          @CustomScope
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
           @CustomScope
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
          
          @SoloModule(AbstractClass::class)
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
          
          @SoloModule(TestInterface::class)
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
          
          @SoloModule(SecondTestInterface::class)
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
          
          @SoloModule
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
    fun `use abstract class when extending interfaces and abstract class`() {
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
          
          @SoloModule(AbstractClass::class)
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
    fun `private class fails with compilation error`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject 
          
          @SoloModule
          private class SampleClass @Inject constructor()

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.messages).contains("error: Types marked with @SoloModule must be public")
    }

    @Test
    fun `nested non-static class fails with compilation error`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject 
          
          class SampleClass {
              
              @SoloModule
              inner class InnerClass @Inject constructor()
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.messages).contains("error: Nested types marked with @SoloModule must be static")
    }

    @Test
    fun `nested static class`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject 
          
          class SampleClass {
              
              @SoloModule
              class InnerClass @Inject constructor()
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("SampleClass\$InnerClass_SoloModule.java")
        assertThat(file).isEqualToJava(
            """
         package test;

         import dagger.Module;
         import dagger.Provides;
         import dagger.hilt.InstallIn;
         import dagger.hilt.android.components.ApplicationComponent;
         
         @Module
         @InstallIn(ApplicationComponent.class)
         public class SampleClass${'$'}InnerClass_SoloModule {
           @Provides
           public SampleClass.InnerClass provides_test_SampleClass${'$'}InnerClass() {
             return new SampleClass.InnerClass(
                 );
           }
         }"""
        )
    }

    @Test
    fun `multiple inject constructor fails`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
        
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import javax.inject.Scope
          
          @Scope
          annotation class CustomScope
          
          @SoloModule
          @CustomScope
          class SampleClass @Inject constructor(private val a: Int) {
              
              @Inject constructor(): this(10) 
          }
          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.messages).contains("error: Multiple constructors marked with @Inject annotated found")
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
