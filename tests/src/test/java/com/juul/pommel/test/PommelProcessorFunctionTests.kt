package com.juul.pommel.test

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test
import kotlin.test.assertEquals

class PommelProcessorFunctionTests : PommelProcessorTests() {

    @Test
    fun `function annotation`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
        
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import javax.inject.Scope
          
          
          @SoloModule
          fun baseUrl(): String {
             return "baseUrl"
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("baseUrl_SoloModule.java")
        assertThat(file).isEqualToJava(
            """
         package test;

         import dagger.Module;
         import dagger.Provides;
         import dagger.hilt.InstallIn;
         import dagger.hilt.components.SingletonComponent;
         import java.lang.String;
         import javax.annotation.Generated;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(SingletonComponent.class)
         public abstract class baseUrl_SoloModule {
           @Provides
           public static String provides_test_SourceKt_baseUrl() {
             return SourceKt.baseUrl(
                 );
           }
         }"""
        )
    }

    @Test
    fun `function install is false`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
        
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import javax.inject.Scope
          
          
          @SoloModule(install = false)
          fun baseUrl(): String {
             return "baseUrl"
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("baseUrl_SoloModule.java")
        assertThat(file).isEqualToJava(
            """
         package test;

         import dagger.Module;
         import dagger.Provides;
         import java.lang.String;
         import javax.annotation.Generated;
         
         $GENERATED_ANNOTATION
         @Module
         public abstract class baseUrl_SoloModule {
           @Provides
           public static String provides_test_SourceKt_baseUrl() {
             return SourceKt.baseUrl(
                 );
           }
         }"""
        )
    }

    @Test
    fun `function install in Singleton scope`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
        
          
          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import javax.inject.Scope
          import javax.inject.Singleton

          @SoloModule
          @Singleton
          fun baseUrl(): String {
             return "baseUrl"
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("baseUrl_SoloModule.java")
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
         public abstract class baseUrl_SoloModule {
           @Provides
           @Singleton
           public static String provides_test_SourceKt_baseUrl() {
             return SourceKt.baseUrl(
                 );
           }
         }"""
        )
    }

    @Test
    fun `function install in ActivityRetainedScoped scope`() {
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
          fun baseUrl(): String {
             return "baseUrl"
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("baseUrl_SoloModule.java")
        assertThat(file).isEqualToJava(
            """
         package test;

         import dagger.Module;
         import dagger.Provides;
         import dagger.hilt.InstallIn;
         import dagger.hilt.android.components.ActivityRetainedComponent;
         import dagger.hilt.android.scopes.ActivityRetainedScoped;
         import java.lang.String;
         import javax.annotation.Generated;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(ActivityRetainedComponent.class)
         public abstract class baseUrl_SoloModule {
           @Provides
           @ActivityRetainedScoped
           public static String provides_test_SourceKt_baseUrl() {
             return SourceKt.baseUrl(
                 );
           }
         }"""
        )
    }

    @Test
    fun `function install in ActivityScoped scope`() {
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
          fun baseUrl(): String {
             return "baseUrl"
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("baseUrl_SoloModule.java")
        assertThat(file).isEqualToJava(
            """
         package test;

         import dagger.Module;
         import dagger.Provides;
         import dagger.hilt.InstallIn;
         import dagger.hilt.android.components.ActivityComponent;
         import dagger.hilt.android.scopes.ActivityScoped;
         import java.lang.String;
         import javax.annotation.Generated;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(ActivityComponent.class)
         public abstract class baseUrl_SoloModule {
           @Provides
           @ActivityScoped
           public static String provides_test_SourceKt_baseUrl() {
             return SourceKt.baseUrl(
                 );
           }
         }"""
        )
    }

    @Test
    fun `function install in FragmentScoped scope`() {
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
          fun baseUrl(): String {
             return "baseUrl"
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("baseUrl_SoloModule.java")
        assertThat(file).isEqualToJava(
            """
         package test;

         import dagger.Module;
         import dagger.Provides;
         import dagger.hilt.InstallIn;
         import dagger.hilt.android.components.FragmentComponent;
         import dagger.hilt.android.scopes.FragmentScoped;
         import java.lang.String;
         import javax.annotation.Generated;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(FragmentComponent.class)
         public abstract class baseUrl_SoloModule {
           @Provides
           @FragmentScoped
           public static String provides_test_SourceKt_baseUrl() {
             return SourceKt.baseUrl(
                 );
           }
         }"""
        )
    }

    @Test
    fun `function install in ServiceScoped scope`() {
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
          fun baseUrl(): String {
             return "baseUrl"
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("baseUrl_SoloModule.java")
        assertThat(file).isEqualToJava(
            """
         package test;

         import dagger.Module;
         import dagger.Provides;
         import dagger.hilt.InstallIn;
         import dagger.hilt.android.components.ServiceComponent;
         import dagger.hilt.android.scopes.ServiceScoped;
         import java.lang.String;
         import javax.annotation.Generated;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(ServiceComponent.class)
         public abstract class baseUrl_SoloModule {
           @Provides
           @ServiceScoped
           public static String provides_test_SourceKt_baseUrl() {
             return SourceKt.baseUrl(
                 );
           }
         }"""
        )
    }

    @Test
    fun `function install in ViewScoped scope`() {
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
          fun baseUrl(): String {
             return "baseUrl"
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("baseUrl_SoloModule.java")
        assertThat(file).isEqualToJava(
            """
         package test;

         import dagger.Module;
         import dagger.Provides;
         import dagger.hilt.InstallIn;
         import dagger.hilt.android.components.ViewComponent;
         import dagger.hilt.android.scopes.ViewScoped;
         import java.lang.String;
         import javax.annotation.Generated;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(ViewComponent.class)
         public abstract class baseUrl_SoloModule {
           @Provides
           @ViewScoped
           public static String provides_test_SourceKt_baseUrl() {
             return SourceKt.baseUrl(
                 );
           }
         }"""
        )
    }

    @Test
    fun `function custom scope fails`() {
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
          fun baseUrl(): String {
             return "baseUrl"
          }

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
          fun baseUrl(): String {
             return "baseUrl"
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("baseUrl_SoloModule.java")
        assertThat(file).isEqualToJava(
            """
         package test;

         import dagger.Module;
         import dagger.Provides;
         import java.lang.String;
         import javax.annotation.Generated;
         
         $GENERATED_ANNOTATION
         @Module
         public abstract class baseUrl_SoloModule {
           @Provides
           @CustomScope
           public static String provides_test_SourceKt_baseUrl() {
             return SourceKt.baseUrl(
                 );
           }
         }"""
        )
    }

    @Test
    fun `function with qualified parameters`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 

          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Scope

          @SoloModule
          fun baseUrl(a: Int, @Named("b") b: Byte): String {
             return "baseUrl" + a.toString() + b.toString()
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("baseUrl_SoloModule.java")
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
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(SingletonComponent.class)
         public abstract class baseUrl_SoloModule {
           @Provides
           public static String provides_test_SourceKt_baseUrl(int a, @Named("b") byte b) {
             return SourceKt.baseUrl(
                 a,
                 b);
           }
         }"""
        )
    }

    @Test
    fun `private function fails compilation`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 

          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Scope

          @SoloModule
          private fun baseUrl(a: Int, @Named("b") b: Byte): String {
             return "baseUrl" + a.toString() + b.toString()
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.messages).contains("error: Functions marked with @SoloModule must be public")
    }

    @Test
    fun `non static function fails compilation`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 

          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Scope

          class SampleClass {
              @SoloModule
              fun baseUrl(a: Int, @Named("b") b: Byte): String {
                  return "baseUrl" + a.toString() + b.toString()
              }
          }
          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.messages).contains("error: Functions marked with @SoloModule must be static")
    }

    @Test
    fun `function in top level companion object`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 

          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Scope

          object Sample {
              @SoloModule
              @JvmStatic
              fun baseUrl(a: Int, @Named("b") b: Byte): String {
                  return "baseUrl" + a.toString() + b.toString()
              }
          }
          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("baseUrl_SoloModule.java")
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
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(SingletonComponent.class)
         public abstract class baseUrl_SoloModule {
           @Provides
           public static String provides_test_Sample_baseUrl(int a, @Named("b") byte b) {
             return Sample.baseUrl(
                 a,
                 b);
           }
         }"""
        )
    }

    @Test
    fun `function using JVM file name`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          @file:JvmName("HiltFunctions")

          package test 

          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Scope

          @SoloModule
          fun baseUrl(a: Int, @Named("b") b: Byte): String {
             return "baseUrl" + a.toString() + b.toString()
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("baseUrl_SoloModule.java")
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
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(SingletonComponent.class)
         public abstract class baseUrl_SoloModule {
           @Provides
           public static String provides_test_HiltFunctions_baseUrl(int a, @Named("b") byte b) {
             return HiltFunctions.baseUrl(
                 a,
                 b);
           }
         }"""
        )
    }

    @Test
    fun `use binding type in function`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 

          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import javax.inject.Scope
          
          interface TestInterface

          class SampleClass : TestInterface
          
          @SoloModule(TestInterface::class)
          fun sampleClass() = SampleClass()

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("sampleClass_SoloModule.java")
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
         public abstract class sampleClass_SoloModule {
           @Provides
           public static TestInterface provides_test_SourceKt_sampleClass() {
             return SourceKt.sampleClass(
                 );
           }
         }"""
        )
    }

    @Test
    fun `bind function with qualifier`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 

          import com.juul.pommel.annotations.SoloModule
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Scope
          
          interface TestInterface

          class SampleClass : TestInterface
          
          @SoloModule
          @Named("sample")
          fun sampleClass(): TestInterface {
             return SampleClass()
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("sampleClass_SoloModule.java")
        assertThat(file).isEqualToJava(
            """
         package test;

         import dagger.Module;
         import dagger.Provides;
         import dagger.hilt.InstallIn;
         import dagger.hilt.components.SingletonComponent;
         import javax.annotation.Generated;
         import javax.inject.Named;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(SingletonComponent.class)
         public abstract class sampleClass_SoloModule {
           @Provides
           @Named("sample")
           public static TestInterface provides_test_SourceKt_sampleClass() {
             return SourceKt.sampleClass(
                 );
           }
         }"""
        )
    }
}
