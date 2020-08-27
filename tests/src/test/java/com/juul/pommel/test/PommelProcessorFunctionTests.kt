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
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Scope
          
          
          @SoloModule(installIn = SingletonComponent::class)
          fun baseUrl(): String {
             return "baseUrl"
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("SourceKt_baseUrl_SoloModule.java")
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
         public abstract class SourceKt_baseUrl_SoloModule {
           @Provides
           public static String provides_test_SourceKt_baseUrl() {
             return SourceKt.baseUrl(
                 );
           }
         }"""
        )
    }

    @Test
    fun `function install in SingletonComponent`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
        
          
          import com.juul.pommel.annotations.SoloModule
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Scope
          import javax.inject.Singleton

          @SoloModule(installIn = SingletonComponent::class)
          @Singleton
          fun baseUrl(): String {
             return "baseUrl"
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("SourceKt_baseUrl_SoloModule.java")
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
         public abstract class SourceKt_baseUrl_SoloModule {
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
    fun `function install in ActivityRetainedComponent`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
        
          
          import com.juul.pommel.annotations.SoloModule
          import dagger.hilt.android.components.ActivityRetainedComponent
          import javax.inject.Inject
          import dagger.hilt.android.scopes.ActivityRetainedScoped

          @SoloModule(installIn = ActivityRetainedComponent::class)
          @ActivityRetainedScoped
          fun baseUrl(): String {
             return "baseUrl"
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("SourceKt_baseUrl_SoloModule.java")
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
         public abstract class SourceKt_baseUrl_SoloModule {
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
    fun `function install in ActivityComponent`() {
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
          fun baseUrl(): String {
             return "baseUrl"
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("SourceKt_baseUrl_SoloModule.java")
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
         public abstract class SourceKt_baseUrl_SoloModule {
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
    fun `function install in FragmentComponent`() {
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
          fun baseUrl(): String {
             return "baseUrl"
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("SourceKt_baseUrl_SoloModule.java")
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
         public abstract class SourceKt_baseUrl_SoloModule {
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
    fun `function install in ServiceComponent`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
        
          
          import com.juul.pommel.annotations.SoloModule
          import dagger.hilt.android.components.ServiceComponent
          import javax.inject.Inject
          import dagger.hilt.android.scopes.ServiceScoped

          @SoloModule(installIn = ServiceComponent::class)
          @ServiceScoped
          fun baseUrl(): String {
             return "baseUrl"
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("SourceKt_baseUrl_SoloModule.java")
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
         public abstract class SourceKt_baseUrl_SoloModule {
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
    fun `function install in ViewComponent`() {
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
          fun baseUrl(): String {
             return "baseUrl"
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("SourceKt_baseUrl_SoloModule.java")
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
         public abstract class SourceKt_baseUrl_SoloModule {
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
    fun `function install in ViewWithFragmentComponent`() {
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
          fun baseUrl(): String {
             return "baseUrl"
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("SourceKt_baseUrl_SoloModule.java")
        assertThat(file).isEqualToJava(
            """
         package test;

         import dagger.Module;
         import dagger.Provides;
         import dagger.hilt.InstallIn;
         import dagger.hilt.android.components.ViewWithFragmentComponent;
         import dagger.hilt.android.scopes.ViewScoped;
         import java.lang.String;
         import javax.annotation.Generated;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(ViewWithFragmentComponent.class)
         public abstract class SourceKt_baseUrl_SoloModule {
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
    fun `function install into custom component`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 
          
        
          
          import com.juul.pommel.annotations.SoloModule  
          import dagger.hilt.DefineComponent
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Scope
          
          @Scope
          annotation class CustomScope
          
          @CustomScope
          @DefineComponent(parent = SingletonComponent::class) 
          interface MyCustomComponent

          @SoloModule(installIn = MyCustomComponent::class)
          @CustomScope
          fun baseUrl(): String {
             return "baseUrl"
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("SourceKt_baseUrl_SoloModule.java")
        assertThat(file).isEqualToJava(
            """
         package test;

         import dagger.Module;
         import dagger.Provides;
         import dagger.hilt.InstallIn;
         import java.lang.String;
         import javax.annotation.Generated;
         
         $GENERATED_ANNOTATION
         @Module
         @InstallIn(MyCustomComponent.class)
         public abstract class SourceKt_baseUrl_SoloModule {
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
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Scope

          @SoloModule(installIn = SingletonComponent::class)
          fun baseUrl(a: Int, @Named("b") b: Byte): String {
             return "baseUrl" + a.toString() + b.toString()
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("SourceKt_baseUrl_SoloModule.java")
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
         public abstract class SourceKt_baseUrl_SoloModule {
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
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Scope

          @SoloModule(installIn = SingletonComponent::class)
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
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Scope

          class SampleClass {
              @SoloModule(installIn = SingletonComponent::class)
              fun baseUrl(a: Int, @Named("b") b: Byte): String {
                  return "baseUrl" + a.toString() + b.toString()
              }
          }
          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.COMPILATION_ERROR)
        assertThat(result.messages).contains("error: Functions marked with @SoloModule must be top level or enclosed in an object or companion class")
    }

    @Test
    fun `function in top level object with JvmStatic`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 

          import com.juul.pommel.annotations.SoloModule
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Scope

          object Sample {
              @SoloModule(installIn = SingletonComponent::class)
              @JvmStatic
              fun baseUrl(a: Int, @Named("b") b: Byte): String {
                  return "baseUrl" + a.toString() + b.toString()
              }
          }
          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("Sample_baseUrl_SoloModule.java")
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
         public abstract class Sample_baseUrl_SoloModule {
           @Provides
           public static String provides_test_Sample_baseUrl(int a, @Named("b") byte b) {
             return Sample.INSTANCE.baseUrl(
                 a,
                 b);
           }
         }"""
        )
    }

    @Test
    fun `function in top level object no JvmStatic`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 

          import com.juul.pommel.annotations.SoloModule
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Scope

          object Sample {
              @SoloModule(installIn = SingletonComponent::class)
              fun baseUrl(a: Int, @Named("b") b: Byte): String {
                  return "baseUrl" + a.toString() + b.toString()
              }
          }
          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("Sample_baseUrl_SoloModule.java")
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
         public abstract class Sample_baseUrl_SoloModule {
           @Provides
           public static String provides_test_Sample_baseUrl(int a, @Named("b") byte b) {
             return Sample.INSTANCE.baseUrl(
                 a,
                 b);
           }
         }"""
        )
    }

    @Test
    fun `function in a companion object `() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 

          import com.juul.pommel.annotations.SoloModule
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Scope

          class Sample { 
              companion object {
                  @SoloModule(installIn = SingletonComponent::class)
                  fun baseUrl(a: Int, @Named("b") b: Byte): String {
                      return "baseUrl" + a.toString() + b.toString()
                  } 
              }
          }
          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("Sample_Companion_baseUrl_SoloModule.java")
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
         public abstract class Sample_Companion_baseUrl_SoloModule {
           @Provides
           public static String provides_test_Sample${'$'}Companion_baseUrl(int a, @Named("b") byte b) {
             return Sample.Companion.baseUrl(
                 a,
                 b);
           }
         }"""
        )
    }

    @Test
    fun `function in a named companion object `() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 

          import com.juul.pommel.annotations.SoloModule
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Scope

          class Sample { 
              companion object Module{
                  @SoloModule(installIn = SingletonComponent::class)
                  fun baseUrl(a: Int, @Named("b") b: Byte): String {
                      return "baseUrl" + a.toString() + b.toString()
                  } 
              }
          }
          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("Sample_Module_baseUrl_SoloModule.java")
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
         public abstract class Sample_Module_baseUrl_SoloModule {
           @Provides
           public static String provides_test_Sample${'$'}Module_baseUrl(int a, @Named("b") byte b) {
             return Sample.Module.baseUrl(
                 a,
                 b);
           }
         }"""
        )
    }

    @Test
    fun `function in a nested class companion object `() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 

          import com.juul.pommel.annotations.SoloModule
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Scope

          class Sample { 
              class Module{
                  companion object Nested {
                      @SoloModule(installIn = SingletonComponent::class)
                      fun baseUrl(a: Int, @Named("b") b: Byte): String {
                          return "baseUrl" + a.toString() + b.toString()
                      }
                  }
              }
          }
          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("Sample_Module_Nested_baseUrl_SoloModule.java")
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
         public abstract class Sample_Module_Nested_baseUrl_SoloModule {
           @Provides
           public static String provides_test_Sample${'$'}Module${'$'}Nested_baseUrl(int a, @Named("b") byte b) {
             return Sample.Module.Nested.baseUrl(
                 a,
                 b);
           }
         }"""
        )
    }

    @Test
    fun `function in a nested object class`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 

          import com.juul.pommel.annotations.SoloModule
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Scope

          object Sample { 
              object A {
                  object B {
                      @SoloModule(installIn = SingletonComponent::class)
                      fun baseUrl(a: Int, @Named("b") b: Byte): String {
                          return "baseUrl" + a.toString() + b.toString()
                      }
                  }
              }
          }
          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("Sample_A_B_baseUrl_SoloModule.java")
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
         public abstract class Sample_A_B_baseUrl_SoloModule {
           @Provides
           public static String provides_test_Sample${'$'}A${'$'}B_baseUrl(int a, @Named("b") byte b) {
             return Sample.A.B.INSTANCE.baseUrl(
                 a,
                 b);
           }
         }"""
        )
    }

    @Test
    fun `getter function annotated`() {
        val result = compile(
            SourceFile.kotlin(
                "source.kt",
                """
          package test 

          import com.juul.pommel.annotations.SoloModule
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Scope

          @get:SoloModule(installIn = SingletonComponent::class)
          val baseUrl = "baseUrl"
          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("SourceKt_getBaseUrl_SoloModule.java")
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
         public abstract class SourceKt_getBaseUrl_SoloModule {
           @Provides
           public static String provides_test_SourceKt_getBaseUrl() {
             return SourceKt.getBaseUrl(
                 );
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
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Scope

          @SoloModule(installIn = SingletonComponent::class)
          fun baseUrl(a: Int, @Named("b") b: Byte): String {
             return "baseUrl" + a.toString() + b.toString()
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("HiltFunctions_baseUrl_SoloModule.java")
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
         public abstract class HiltFunctions_baseUrl_SoloModule {
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
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Scope
          
          interface TestInterface

          class SampleClass : TestInterface
          
          @SoloModule(TestInterface::class, SingletonComponent::class)
          fun sampleClass() = SampleClass()

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("SourceKt_sampleClass_SoloModule.java")
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
         public abstract class SourceKt_sampleClass_SoloModule {
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
          import dagger.hilt.components.SingletonComponent
          import javax.inject.Inject
          import javax.inject.Named
          import javax.inject.Scope
          
          interface TestInterface

          class SampleClass : TestInterface
          
          @SoloModule(installIn = SingletonComponent::class)
          @Named("sample")
          fun sampleClass(): TestInterface {
             return SampleClass()
          }

          """
            )
        )

        assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
        val file = result.getGeneratedFile("SourceKt_sampleClass_SoloModule.java")
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
         public abstract class SourceKt_sampleClass_SoloModule {
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
