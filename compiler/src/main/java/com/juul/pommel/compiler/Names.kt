package com.juul.pommel.compiler

import com.squareup.javapoet.ClassName

internal val applicationComponent: ClassName = ClassName.get("dagger.hilt.android.components", "ApplicationComponent")
internal val activityComponent: ClassName = ClassName.get("dagger.hilt.android.components", "ActivityComponent")
internal val activityRetainedComponent: ClassName = ClassName.get("dagger.hilt.android.components", "ActivityRetainedComponent")
internal val fragmentComponent: ClassName = ClassName.get("dagger.hilt.android.components", "FragmentComponent")
internal val serviceComponent: ClassName = ClassName.get("dagger.hilt.android.components", "ServiceComponent")
internal val viewComponent: ClassName = ClassName.get("dagger.hilt.android.components", "ViewComponent")

internal val module = ClassName.get("dagger", "Module")
internal val provides = ClassName.get("dagger", "Provides")
internal val binds = ClassName.get("dagger", "Binds")
internal val installIn = ClassName.get("dagger.hilt", "InstallIn")
internal val generated = ClassName.get("javax.annotation", "Generated")
