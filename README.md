# Pommel

Pommel is an annotation processor for Dagger projects to generate a module with a single binding

# Motivation

The new [Dagger-Hilt](https://dagger.dev/hilt/) package from Google has simplified the Dagger setup for
Android and introduced a [new testing package](https://dagger.dev/hilt/testing). You can now choose modules
to exclude in your tests using [@UninstallModules](https://dagger.dev/api/latest/dagger/hilt/android/testing/UninstallModules.html)
and provide your own testing modules.

```kotlin
@UninstallModules(ProductionServiceModule::class)
@HiltAndroidTest
class ServiceTest {

  @Rule
  @JvmField
  val hiltRule = HiltAndroidRule(this)
  
  @BindValue
  @JvmField
  val fakeService: Service = FakeService()
}
```
The problem with `@UninstallModules` is it will exclude all the bindings that are provided by that module. Dagger does
not provide a way to uninstall a single binding from the graph. The Dagger documentation suggest [to create a module with a single
binding if you want such behavior](https://dagger.dev/hilt/testing)


>Hilt does not directly support uninstalling individual bindings, but it’s effectively possible by only including a single binding in a given module.


It would be a tedious task to create a module with a single binding for every dependency that a developer may want to replace in testing. It would also be creating
boilerplate since dependencies using constructor injection do not need to be declared in a module.

# Usage

## Class

Pommel expects you to install the generated module into a component:

```kotlin
@SoloModule(installIn = SingletonComponent::class)
@Singleton
class SampleClass @Inject constructor(
    @Named("a") val a: Int,
    val b: String
)
```

Will generate the equivalent of:

```java
@Module
@InstallIn(SingletonComponent.class)
public abstract class SampleClass_SoloModule {
  @Provides
  @Singleton
  public static SampleClass provides_SampleClass(@Named("a") int a, String b) {
    return new SampleClass(a, b);
  }
}
```

Pommel will always bind to the concrete implementation:

```kotlin
interface MyInterface

@SoloModule(installIn = SingletonComponent::class)
@Singleton
class SampleClass @Inject constructor(
    @Named("a") val a: Int,
    val b: String
) : MyInterface
```

Will generate the equivalent of:

```java
@Module
@InstallIn(SingletonComponent.class)
public abstract class SampleClass_SoloModule {
  @Provides
  @Singleton
  public static SampleClass provides_SampleClass(@Named("a") int a, String b) {
    return new SampleClass(a, b);
  }
}
```

If your class extends an `abstract class` or `interface`, you can specify to bind to the extending type:

```kotlin
interface MyInterface

@SoloModule(MyInterface::class, SingletonComponent::class)
@Singleton
class SampleClass @Inject constructor(
    @Named("a") val a: Int,
    val b: String
) : MyInterface
```

Will generate the equivalent of:

```java
@Module
@InstallIn(SingletonComponent.class)
public abstract class SampleClass_SoloModule {
  @Binds
  @Singleton
  public abstract MyInterface binds_SampleClass(SampleClass sampleClass);
}
```

You can annotate your class with a qualifier:

```kotlin
interface MyInterface

@SoloModule(MyInterface::class, SingletonComponent::class)
@Singleton
@Named("sample")
class SampleClass @Inject constructor(
    @Named("a") val a: Int,
    val b: String
) : MyInterface
```

Will generate the equivalent of:

```java
@Module
@InstallIn(SingletonComponent.class)
public abstract class SampleClass_SoloModule {
  @Binds
  @Singleton
  @Named("sample")
  public abstract MyInterface provides_SampleClass(SampleClass sampleClass);
}
```

## Functions

Pommel expects you to install the generated module into a component:

```kotlin
@Singleton
@SoloModule(installIn = SingletonComponent::class)
@Named("name")
fun name(): String = "Pommel"
```

Will generate the equivalent of:

```java
@Module
@InstallIn(SingletonComponent.class)
public abstract class FileName_name_SoloModule {
  @Provides
  @Singleton
  @Named("name")
  public static String provides_FileName_name() {
      return FileName.name(); 
  }
}
```

Where `FileName` is the name of the file where the function is in.

You can use `@file:JvmName` to change the name of the file.

```kotlin
@file:JvmName("PommelFunctions")

@Singleton
@SoloModule(installIn = SingletonComponent::class)
@Named("name")
fun name(): String = "Pommel"
```

Will generate the equivalent of:

```java
@Module
@InstallIn(SingletonComponent.class)
public abstract class PommelFunctions_name_SoloModule {
  @Provides
  @Singleton
  @Named("name")
  public static String provides_PommelFunctions_name() {
      return PommelFunctions.name(); 
  }
}
```

You can also use a top level `object` to hold your pommel functions.

```kotlin

object Module {
    @Singleton
    @SoloModule(installIn = SingletonComponent::class)
    @Named("name")
    fun name(): String = "Pommel"
}
```

Will generate the equivalent of:

```java
@Module
@InstallIn(SingletonComponent.class)
public abstract class Module_name_SoloModule {
  @Provides
  @Singleton
  @Named("name")
  public static String provides_Module_name() {
      return Module.INSTANCE.name(); 
  }
}
```

You can enclose your functions in a `companion` object

```kotlin
class MyClass private constructor () {
    companion object Factory {
        @SoloModule(installIn = SingletonComponent::class)
        fun create() = MyClass()
    }
}
```

Will generate the equivalent of:

```java
@Module
@InstallIn(SingletonComponent.class)
public abstract class MyClass_Factory_create_SoloModule {
  @Provides
  @Singleton
  @Named("name")
  public static MyClass provides_MyClass$Factory_create() {
      return MyClass.Factory.create(); 
  }
}
```

You annotate the getter of a backing field:

```kotlin
@get:SoloModule(installIn = SingletonComponent::class)
@Named("baseUrl")
val baseUrl = "baseUrl"
```

Will generate the equivalent of:


```java
@Module
@InstallIn(SingletonComponent.class)
public abstract class FileName_getBaseUrl_SoloModule {
    @Provides
    @Named("baseUrl")
    public static String provides_getBaseUrl() {
        return FileName.getBaseUrl();
    }
}
```

# Testing

Pommel was intended to be used for testing with Dagger-Hilt. Simply mark the element you'd like to replace in test with `@SoloModule`, then uninstall the resulting generated module in your test to provide your own test implementation:

```kotlin
@SoloModule(installIn = SingletonComponent::class)
@Singleton
class MyService @Inject constructor(
    @Named("a") val a: Int,
    val b: String
)
```

Then in your test `class`:

```kotlin
@UninstallModules(MyService_SoloModule::class)
@HiltAndroidTest
class MyServiceTest {

  @Rule
  @JvmField
  val hiltRule = HiltAndroidRule(this)
  
  @BindValue
  @JvmField
  val testSerivce: MyService = FakeService()
}
```

Reference the [Dagger-Hilt testing package](https://dagger.dev/hilt/testing) for best practices on testing.

# Limitations

Pommel expects `class`es annotated with `@SoloModule` to have a constructor that is is annotated with `@Inject`. Multiple constructors annotated with `@Inject` is not supported as Dagger itself only supports a single
constructor annotated with `@Inject`. All parameters passed into the constructor must also be on the Dagger graph, as this is another expectation of Dagger constructor injection

Pommel expects all functions annotated with `@SoloModule` to be `static` due to the fact that the generated module needs to call the annotated function.
As a result Pommel only supports top level functions and functions that are enclosed in an `object` or `companion` class

Pommel was designed to be used with Dagger-Hilt as result it expects a component to be passed into the `installIn` parameter. The component passed into `installIn`  follows the same limitation of Dagger-Hilt.
The component must be one of the predefined Dagger-Hilt components or a custom component annotated with `DefineComponent`. If there is a need to not install a generated `SoloModule` please file a GitHub issue.

# Download

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.juul.pommel/compiler/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.juul.pommel/compiler)

```groovy
repositories {
    jcenter() // or mavenCentral()
}

dependencies {
    implementation "com.juul.pommel:annotations:$version"

    kaptAndroidTest "com.juul.pommel:compiler:$version" // for android tests
    kaptTest "com.juul.pommel:compiler:$version" // for unit tests
}
```

# License

```
Copyright 2020 JUUL Labs, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

