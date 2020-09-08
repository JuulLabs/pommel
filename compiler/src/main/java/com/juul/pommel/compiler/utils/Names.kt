package com.juul.pommel.compiler.utils

import com.squareup.javapoet.ClassName

internal val module = ClassName.get("dagger", "Module")
internal val provides = ClassName.get("dagger", "Provides")
internal val binds = ClassName.get("dagger", "Binds")
internal val installIn = ClassName.get("dagger.hilt", "InstallIn")
internal val generated = ClassName.get("javax.annotation", "Generated")
