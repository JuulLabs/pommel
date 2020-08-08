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

```kotlin
@SoloModule
@Singleton
class SampleClass @Inject constructor(
    @Named("a") val a: Int,
    val b: String
)
```

Will generate the equivalent of:

```java
@Module
@InstallIn(ApplicationComponent.class)
public abstract class SampleClass_SoloModule {
  @Provides
  @Singleton
  public static SampleClass provides_SampleClass(@Named("a") int a, String b) {
    return new SampleClass(a, b);
  }
}
```

If you are using Dagger-Hilt Pommel will automatically install the module into the correct component. Pommel currently does not support custom
components but you can disable installing into a component with the annotation parameter `install`:

```kotlin
@SoloModule(install = false)
@Singleton
class SampleClass @Inject constructor(
    @Named("a") val a: Int,
    val b: String
)
```

Will generate the equivalent of:

```java
@Module
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

@SoloModule
@Singleton
class SampleClass @Inject constructor(
    @Named("a") val a: Int,
    val b: String
) : MyInterface
```

Will generate the equivalent of:

```java
@Module
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

@SoloModule(MyInterface::class)
@Singleton
class SampleClass @Inject constructor(
    @Named("a") val a: Int,
    val b: String
) : MyInterface
```

Will generate the equivalent of:

```java
@Module
public abstract class SampleClass_SoloModule {
  @Binds
  @Singleton
  public abstract MyInterface binds_SampleClass(SampleClass sampleClass);
}
```

You can annotate your class with a qualifier:

```kotlin
interface MyInterface

@SoloModule(MyInterface::class)
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
public class SampleClass_SoloModule {
  @Binds
  @Singleton
  @Named("sample")
  public abstract MyInterface provides_SampleClass(SampleClass sampleClass);
}
```

# Testing

Pommel was intended to be used for testing with Dagger-Hilt. Simply mark the element you'd like to replace in test with `@SoloModule`, then uninstall the resulting generated module in your test to provide your own test implementation:

```kotlin
@SoloModule
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

Pommel currently only supports being annotated on a `class`, as a result it assumes that the `class` has at least one constructor that is annotated with `@Inject` and
that all parameters passed into the constructor are also on the Dagger graph.

# Download

```
implementation "com.juul.pommel:annotations:$version"

kaptAndroidTest "com.juul.pommel:compiler:$version" // for android tests
kaptTest "com.juul.pommel:compiler:$version" // for unit tests
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

