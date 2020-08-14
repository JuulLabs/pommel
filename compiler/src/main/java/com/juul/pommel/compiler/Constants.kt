package com.juul.pommel.compiler

internal const val SCOPE_ANNOTATION: String = "javax.inject.Scope"
internal const val INJECT_ANNOTATION: String = "javax.inject.Inject"
internal const val QUALIFIER_ANNOTATION: String = "javax.inject.Qualifier"
internal const val JAVA_VOID: String = "java.lang.Void"

internal const val SINGLETON_SCOPED: String = "javax.inject.Singleton"
internal const val ACTIVITY_RETAINED_SCOPED: String = "dagger.hilt.android.scopes.ActivityRetainedScoped"
internal const val ACTIVITY_SCOPED: String = "dagger.hilt.android.scopes.ActivityScoped"
internal const val FRAGMENT_SCOPED: String = "dagger.hilt.android.scopes.FragmentScoped"
internal const val SERVICE_SCOPED: String = "dagger.hilt.android.scopes.ServiceScoped"
internal const val VIEW_SCOPED: String = "dagger.hilt.android.scopes.ViewScoped"
