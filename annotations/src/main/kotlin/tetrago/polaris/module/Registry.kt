package tetrago.polaris.module

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class Registry(val entity: KClass<*>)
