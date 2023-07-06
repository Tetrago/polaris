package tetrago.polaris.app.data

import io.objectbox.Property
import org.koin.core.context.GlobalContext.get
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

abstract class Registry<P : DataStoreProvider, E : Any>(
    provider: KClass<P>,
    entity: KClass<E>,
    private val column: Property<E>? = null
) : RegistryProvider {
    constructor(provider: KClass<P>, column: Property<E>) : this(provider, column.entity.entityClass.kotlin, column)

    abstract class Holder<E : Any> {
        abstract operator fun getValue(thisRef: Any?, property: KProperty<*>): E
        abstract operator fun setValue(thisRef: Any?, property: KProperty<*>, value: E)
    }

    private val field by lazy { entity.java.getDeclaredField(column!!.name).also { it.isAccessible = true } }

    val store by lazy { get().get<P>(provider).get().boxFor(entity.java) }

    override fun create() = Unit

    protected fun hold(parameter: String) = object : Holder<E>() {
        lateinit var elem: E

        override operator fun getValue(thisRef: Any?, property: KProperty<*>): E {
            if(!this::elem.isInitialized) {
                elem = store.query(column!!.equal(parameter)).build().use { it.findFirst() }!!
            }

            return elem
        }

        override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: E) {
            field.set(value, parameter)
            elem = value
            store.put(elem)
        }
    }
}
