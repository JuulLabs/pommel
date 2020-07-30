package com.juul.pommel.compiler.internal

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.AnnotatedConstruct
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror

internal const val SCOPE_ANNOTATION: String = "javax.inject.Scope"
internal const val INJECT_ANNOTATION: String = "javax.inject.Inject"
internal const val JAVA_VOID: String = "java.lang.Void"

internal const val SINGLETON_SCOPED: String = "javax.inject.Singleton"
internal const val ACTIVITY_RETAINED_SCOPED: String = "dagger.hilt.android.scopes.ActivityRetainedScoped"
internal const val ACTIVITY_SCOPED: String = "dagger.hilt.android.scopes.ActivityScoped"
internal const val FRAGMENT_SCOPED: String = "dagger.hilt.android.scopes.FragmentScoped"
internal const val SERVICE_SCOPED: String = "dagger.hilt.android.scopes.ServiceScoped"
internal const val VIEW_SCOPED: String = "dagger.hilt.android.scopes.ViewScoped"

internal val applicationComponent: ClassName = ClassName.get("dagger.hilt.android.components", "ApplicationComponent")
internal val activityComponent: ClassName = ClassName.get("dagger.hilt.android.components", "ActivityComponent")
internal val activityRetainedComponent: ClassName = ClassName.get("dagger.hilt.android.components", "ActivityRetainedComponent")
internal val fragmentComponent: ClassName = ClassName.get("dagger.hilt.android.components", "FragmentComponent")
internal val serviceComponent: ClassName = ClassName.get("dagger.hilt.android.components", "ServiceComponent")
internal val viewComponent: ClassName = ClassName.get("dagger.hilt.android.components", "ViewComponent")

/** Return a list of elements annotated with `T`. */
internal inline fun <reified T : Annotation> RoundEnvironment.findElementsAnnotatedWith(): Set<Element> =
    getElementsAnnotatedWith(T::class.java)

/** Return true if this [AnnotatedConstruct] is annotated with `qualifiedName`. */
internal fun AnnotatedConstruct.hasAnnotation(qualifiedName: String) = getAnnotation(qualifiedName) != null

/** Return the first annotation matching [qualifiedName] or null. */
internal fun AnnotatedConstruct.getAnnotation(qualifiedName: String) = annotationMirrors
    .firstOrNull {
        it.annotationType.asElement().cast<TypeElement>().qualifiedName.contentEquals(qualifiedName)
    }

/** Equivalent to `this as T` for use in function chains. */
@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
internal inline fun <T> Any.cast(): T = this as T

@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
internal inline fun <T> Iterable<*>.castEach() = map { it as T }

internal inline fun <T : Any, I> T.applyEach(items: Iterable<I>, func: T.(I) -> Unit): T {
    items.forEach { item -> func(item) }
    return this
}

internal fun TypeMirror.toTypeName(): TypeName = TypeName.get(this)

internal fun getTypeMirror(block: () -> Unit): TypeMirror? {
    try {
        block()
    } catch (e: MirroredTypeException) {
        return e.typeMirror
    }
    return null
}
