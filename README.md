# Pommel

Pommel is an annotation processor for Dagger projects to generate a module with single binding

# Motivation

Thew new [Dagger-Hilt](https://dagger.dev/hilt/) package from Google has simplified the Dagger setup from
Android and has also introduced a [new testing package as well](https://dagger.dev/hilt/testing). You can now choose modules
to not include to your test using the [@UninstallModules](https://dagger.dev/api/latest/dagger/hilt/android/testing/UninstallModules.html)  
and provide your own testing modules.

```kotlin
@UninstallModules(ProdFooModule::class)
@HiltAndroidTest
class FooTest {

  ...
  @BindValue
  @JvmField
  val fakeFoo: Foo = FakeFoo()
}
```
The problem with @UninstallModules is that will not include all the bindings that are provided by that module. Dagger does
not provided a way to uninstall a single binding from graph. The Dagger documentation suggest [to own included a single
binding in a module if you want such behavior](https://dagger.dev/hilt/testing)

```
Hilt does not directly support uninstalling individual bindings, but it’s effectively possible by only including a single binding in a given module.
```

It would be a tedious task to create a module with a single binding for every dependency that a developer may want to replace in testing. It would also be creating
boilerplate since dependency using constructor injection do not need to be declared in a module.

# Usage

```kotlin
package test 

@SoloModule
@Singleton
class SampleClass @Inject constructor(
    @Named("a" ) val a: Int,
    val b: String
)
```

This will generate the following:

```java
@Module
@InstallIn(ApplicationComponent.class)
public class SampleClass_SoloModule {
  @Provides
  @Singleton
  public SampleClass provides_test_SampleClass(@Named("a") int a, String b) {
    return new SampleClass(
        a,
        b);
  }
}
```

If you are using Dagger-Hilt Pommel will automatically install the module into the correct component. Pommel currently does not support custom
components but you can disable installing into a component with the annotation parameter `install`

```kotlin
package test 

@SoloModule(install = false)
@Singleton
class SampleClass @Inject constructor(
    @Named("a" ) val a: Int,
    val b: String
)
```

This will generate the following:

```java
@Module
public class SampleClass_SoloModule {
  @Provides
  @Singleton
  public SampleClass provides_test_SampleClass(@Named("a") int a, String b) {
    return new SampleClass(
        a,
        b);
  }
}
```

If your class extends an abstract class or interface Pommel will automatically bind to the extending type

```kotlin
package test 

interface MyInterface

@SoloModule
@Singleton
class SampleClass @Inject constructor(
    @Named("a" ) val a: Int,
    val b: String
) : MyInterface
```

This will generate the following:

```java
@Module
public class SampleClass_SoloModule {
  @Provides
  @Singleton
  public MyInterface provides_test_SampleClass(@Named("a") int a, String b) {
    return new SampleClass(
        a,
        b);
  }
}
```

If you extend multiple interfaces and an abstract class you need to specify the binding type with `bindingClass` parameter


```kotlin
package test 

interface MyInterface

interface OtherInterface

@SoloModule(bindingClass = OtherInterface::class)
@Singleton
class SampleClass @Inject constructor(
    @Named("a" ) val a: Int,
    val b: String
) : MyInterface, OtherInterface
```

This will generate the following:

```java
@Module
public class SampleClass_SoloModule {
  @Provides
  @Singleton
  public OtherInterface provides_test_SampleClass(@Named("a") int a, String b) {
    return new SampleClass(
        a,
        b);
  }
}
```

If you wish just to bind to the concrete implementation you can just set the `bindSuperType` parameter to false

```kotlin
package test 

interface MyInterface

interface OtherInterface

@SoloModule(bindSuperType = false)
@Singleton
class SampleClass @Inject constructor(
    @Named("a" ) val a: Int,
    val b: String
) : MyInterface, OtherInterface
```

This will generate the following:

```java
@Module
public class SampleClass_SoloModule {
  @Provides
  @Singleton
  public SampleClass provides_test_SampleClass(@Named("a") int a, String b) {
    return new SampleClass(
        a,
        b);
  }
}
```

# Testing

Pommel's main usage is for testing with Dagger-Hilt. You can uninstall the generated module in your tests.

```kotlin
package test 

@SoloModule
@Singleton
class MyService @Inject constructor(
    @Named("a" ) val a: Int,
    val b: String
)
```

And then in your test:

```kotlin
@UninstallModules(MyService_SoloModule::class)
@HiltAndroidTest
class MyServiceTest {

  @BindValue
  @JvmField
  val testSerivce: MyService = FakeService()
}
```

Reference the [Dagger-Hilt testing package](https://dagger.dev/hilt/testing) for best practices on testing.

# Assumptions

Pommel can currently only supports being annotated on a class as a result it assumes that the class has at least one constructor that is annotated with `@Inject` and
that all parameters pass into the constructor are also on the Dagger graph.

# Download

```
kapt "com.juul.pommel:compiler:$version" // only include if you want to generate module in main source set 
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

