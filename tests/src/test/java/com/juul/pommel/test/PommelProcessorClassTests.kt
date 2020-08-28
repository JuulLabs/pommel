package com.juul.pommel.test

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertEquals
import kotlin.test.Test

class PommelProcessorClassTests : PommelProcessorTests() {

    @Test
    fun `unscoped constructor injection`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject 
          
          @SoloModule(installIn = SingletonComponent::class)
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
         import dagger.hilt.components.SingletonComponent;
         import javax.annotation.Generated;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(SingletonComponent.class)
         public abstract class SampleClass_SoloModule {
           @Provides
           public static SampleClass provides_test_SampleClass() {
             return new SampleClass(
                 );
           }
         }"""
        )
    }

    @Test
    fun `install in SingletonComponent `() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule 
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Singleton 
          
          @SoloModule(installIn = SingletonComponent::class)
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
         import dagger.hilt.components.SingletonComponent;
         import javax.annotation.Generated;
         import javax.inject.Singleton;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(SingletonComponent.class)
         public abstract class SampleClass_SoloModule {
           @Provides
           @Singleton
           public static SampleClass provides_test_SampleClass() {
             return new SampleClass(
                 );
           }
         }"""
        )
    }

    @Test
    fun `install in ActivityRetainedComponent`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import dagger.hilt.android.components.ActivityRetainedComponent
          import dagger.hilt.android.scopes.ActivityRetainedScoped
          
          @SoloModule(installIn = ActivityRetainedComponent::class)
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
         import javax.annotation.Generated;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(ActivityRetainedComponent.class)
         public abstract class SampleClass_SoloModule {
           @Provides
           @ActivityRetainedScoped
           public static SampleClass provides_test_SampleClass() {
             return new SampleClass(
                 );
           }
         }"""
        )
    }

    @Test
    fun `install in ActivityComponent`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import dagger.hilt.android.components.ActivityComponent
          import javax.inject.Inject
          import dagger.hilt.android.scopes.ActivityScoped
          
          @SoloModule(installIn = ActivityComponent::class)
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
         import javax.annotation.Generated;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(ActivityComponent.class)
         public abstract class SampleClass_SoloModule {
           @Provides
           @ActivityScoped
           public static SampleClass provides_test_SampleClass() {
             return new SampleClass(
                 );
           }
         }"""
        )
    }

    @Test
    fun `install in FragmentComponent`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import dagger.hilt.android.components.FragmentComponent
          import javax.inject.Inject
          import dagger.hilt.android.scopes.FragmentScoped
          
          @SoloModule(installIn = FragmentComponent::class)
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
         import javax.annotation.Generated;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(FragmentComponent.class)
         public abstract class SampleClass_SoloModule {
           @Provides
           @FragmentScoped
           public static SampleClass provides_test_SampleClass() {
             return new SampleClass(
                 );
           }
         }"""
        )
    }

    @Test
    fun `install in ServiceComponent`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import dagger.hilt.android.components.ServiceComponent
          import dagger.hilt.android.scopes.ServiceScoped
          
          @SoloModule(installIn = ServiceComponent::class)
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
         import javax.annotation.Generated;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(ServiceComponent.class)
         public abstract class SampleClass_SoloModule {
           @Provides
           @ServiceScoped
           public static SampleClass provides_test_SampleClass() {
             return new SampleClass(
                 );
           }
         }"""
        )
    }

    @Test
    fun `install in ViewComponent`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import dagger.hilt.android.components.ViewComponent
          import javax.inject.Inject
          import dagger.hilt.android.scopes.ViewScoped
          
          @SoloModule(installIn = ViewComponent::class)
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
         import javax.annotation.Generated;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(ViewComponent.class)
         public abstract class SampleClass_SoloModule {
           @Provides
           @ViewScoped
           public static SampleClass provides_test_SampleClass() {
             return new SampleClass(
                 );
           }
         }"""
        )
    }

    @Test
    fun `install in ViewWithFragmentComponent`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import dagger.hilt.android.components.ViewWithFragmentComponent
          import javax.inject.Inject
          import dagger.hilt.android.scopes.ViewScoped
          
          @SoloModule(installIn = ViewWithFragmentComponent::class)
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
         import dagger.hilt.android.components.ViewWithFragmentComponent;
         import dagger.hilt.android.scopes.ViewScoped;
         import javax.annotation.Generated;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(ViewWithFragmentComponent.class)
         public abstract class SampleClass_SoloModule {
           @Provides
           @ViewScoped
           public static SampleClass provides_test_SampleClass() {
             return new SampleClass(
                 );
           }
         }"""
        )
    }

    @Test
    fun `Install in custom component`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
        
          
          import com.juul.pommel.annotations.SoloModule
          import dagger.hilt.components.SingletonComponent
          import dagger.hilt.DefineComponent
          import javax.inject.Inject
          import javax.inject.Scope
          
          @Scope
          annotation class CustomScope
          
          @CustomScope
          @DefineComponent(parent = SingletonComponent::class) 
          interface MyCustomComponent
          
          @SoloModule(installIn = MyCustomComponent::class)
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
         import dagger.hilt.InstallIn;
         import javax.annotation.Generated;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(MyCustomComponent.class)
         public abstract class SampleClass_SoloModule {
           @Provides
           @CustomScope
           public static SampleClass provides_test_SampleClass() {
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
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Singleton 
          
          @SoloModule(installIn = SingletonComponent::class)
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
         import dagger.hilt.components.SingletonComponent;
         import java.lang.String;
         import javax.annotation.Generated;
         import javax.inject.Singleton;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(SingletonComponent.class)
         public abstract class SampleClass_SoloModule {
           @Provides
           @Singleton
           public static SampleClass provides_test_SampleClass(int a, String b, double c) {
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
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Singleton 
          
          @SoloModule(installIn = SingletonComponent::class)
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
         import dagger.hilt.components.SingletonComponent;
         import java.lang.String;
         import javax.annotation.Generated;
         import javax.inject.Named;
         import javax.inject.Singleton;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(SingletonComponent.class)
         public abstract class SampleClass_SoloModule {
           @Provides
           @Singleton
           public static SampleClass provides_test_SampleClass(@Named("a") int a, String b, double c,
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
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Singleton 
          
          abstract class AbstractClass
          
          @SoloModule(installIn = SingletonComponent::class, bindingClass = AbstractClass::class)
          @Singleton
          class SampleClass @Inject constructor(
              @Named("a") val a: Int,
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

         import dagger.Binds;
         import dagger.Module;
         import dagger.hilt.InstallIn;
         import dagger.hilt.components.SingletonComponent;
         import javax.annotation.Generated;
         import javax.inject.Singleton;

         $GENERATED_ANNOTATION
         @Module
         @InstallIn(SingletonComponent.class)
         public abstract class SampleClass_SoloModule {
           @Binds
           @Singleton
           public abstract AbstractClass binds_test_SampleClass(SampleClass sampleClass);
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
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Singleton 
          
          interface TestInterface
          
          @SoloModule(installIn = SingletonComponent::class, bindingClass = TestInterface::class)
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

         import dagger.Binds;
         import dagger.Module;
         import dagger.hilt.InstallIn;
         import dagger.hilt.components.SingletonComponent;
         import javax.annotation.Generated;
         import javax.inject.Singleton;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(SingletonComponent.class)
         public abstract class SampleClass_SoloModule {
           @Binds
           @Singleton
           public abstract TestInterface binds_test_SampleClass(SampleClass sampleClass);
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
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Singleton 
          
          interface TestInterface
          
          interface SecondTestInterface
          
          @SoloModule(installIn = SingletonComponent::class, bindingClass = SecondTestInterface::class)
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

         import dagger.Binds;
         import dagger.Module;
         import dagger.hilt.InstallIn;
         import dagger.hilt.components.SingletonComponent;
         import javax.annotation.Generated;
         import javax.inject.Singleton;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(SingletonComponent.class)
         public abstract class SampleClass_SoloModule {
           @Binds
           @Singleton
           public abstract SecondTestInterface binds_test_SampleClass(SampleClass sampleClass);
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
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Singleton 
          
          interface TestInterface
          
          @SoloModule(installIn = SingletonComponent::class)
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
         import dagger.hilt.components.SingletonComponent;
         import java.lang.String;
         import javax.annotation.Generated;
         import javax.inject.Named;
         import javax.inject.Singleton;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(SingletonComponent.class)
         public abstract class SampleClass_SoloModule {
           @Provides
           @Singleton
           public static SampleClass provides_test_SampleClass(@Named("a") int a, String b, double c,
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
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Singleton 
          
          interface TestInterface
          
          interface OtherInterface
          
          @SoloModule(installIn = SingletonComponent::class)
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
         import dagger.hilt.components.SingletonComponent;
         import java.lang.String;
         import javax.annotation.Generated;
         import javax.inject.Named;
         import javax.inject.Singleton;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(SingletonComponent.class)
         public abstract class SampleClass_SoloModule {
           @Provides
           @Singleton
           public static SampleClass provides_test_SampleClass(@Named("a") int a, String b, double c,
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
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Singleton 
          
          interface TestInterface
          
          abstract class AbstractClass
          
          @SoloModule(installIn = SingletonComponent::class)
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
         import dagger.hilt.components.SingletonComponent;
         import java.lang.String;
         import javax.annotation.Generated;
         import javax.inject.Named;
         import javax.inject.Singleton;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(SingletonComponent.class)
         public abstract class SampleClass_SoloModule {
           @Provides
           @Singleton
           public static SampleClass provides_test_SampleClass(@Named("a") int a, String b, double c,
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
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Singleton 
          
          interface TestInterface
          
          abstract class AbstractClass
          
          @SoloModule(installIn = SingletonComponent::class, bindingClass = AbstractClass::class)
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

         import dagger.Binds;
         import dagger.Module;
         import dagger.hilt.InstallIn;
         import dagger.hilt.components.SingletonComponent;
         import javax.annotation.Generated;
         import javax.inject.Singleton;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(SingletonComponent.class)
         public abstract class SampleClass_SoloModule {
           @Binds
           @Singleton
           public abstract AbstractClass binds_test_SampleClass(SampleClass sampleClass);
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
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject 
          
          @SoloModule(installIn = SingletonComponent::class)
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
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject 
          
          class SampleClass {
              
              @SoloModule(installIn = SingletonComponent::class)
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
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject 
          
          class SampleClass {
              
              @SoloModule(installIn = SingletonComponent::class)
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
         import dagger.hilt.components.SingletonComponent;
         import javax.annotation.Generated;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(SingletonComponent.class)
         public abstract class SampleClass${'$'}InnerClass_SoloModule {
           @Provides
           public static SampleClass.InnerClass provides_test_SampleClass${'$'}InnerClass() {
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
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Scope

          @SoloModule(installIn = SingletonComponent::class)
          class SampleClass @Inject constructor(private val a: Int) {
              
              @Inject constructor(): this(10) 
          }
          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.messages).contains("error: Multiple constructors marked with @Inject annotated found")
        assertThat(result.messages).doesNotContain("An exception occurred: java.lang.IllegalArgumentException: List has more than one element")
    }

    @Test
    fun `bind interface with qualifier`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
          import com.juul.pommel.annotations.SoloModule
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Singleton 
          
          interface TestInterface
          
          @SoloModule(installIn = SingletonComponent::class, bindingClass = TestInterface::class)
          @Singleton
          @Named("test")
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

         import dagger.Binds;
         import dagger.Module;
         import dagger.hilt.InstallIn;
         import dagger.hilt.components.SingletonComponent;
         import javax.annotation.Generated;
         import javax.inject.Named;
         import javax.inject.Singleton;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(SingletonComponent.class)
         public abstract class SampleClass_SoloModule {
           @Binds
           @Singleton
           @Named("test")
           public abstract TestInterface binds_test_SampleClass(SampleClass sampleClass);
         }"""
        )
    }
}
