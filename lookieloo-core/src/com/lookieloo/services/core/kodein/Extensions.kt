package com.lookieloo.services.core.kodein

import com.lookieloo.services.core.controller.KodeinController
import io.ktor.application.Application
import io.ktor.routing.routing
import org.kodein.di.Instance
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import org.kodein.di.jvmType

/**
 * Registers a [kodeinApplication] that that will call [kodeinMapper] for mapping stuff.
 * The [kodeinMapper] is a lambda that is in charge of mapping all the required.
 *
 * After calling [kodeinMapper], this function will search
 * for registered subclasses of [KodeinController], and will call their [KodeinController.registerRoutes] methods.
 */
fun Application.kodeinApplication(
	kodeinMapper: Kodein.MainBuilder.(Application) -> Unit = {}
) {
	val application = this

	/**
	 * Creates a [Kodein] instance, binding the [Application] instance.
	 * Also calls the [kodeInMapper] to map the Controller dependencies.
	 */
	val kodein = Kodein {
		bind<Application>() with instance(application)
		kodeinMapper(this, application)
	}

	/**
	 * Detects all the registered [KodeinController] and registers its routes.
	 */
	routing {
		for (bind in kodein.container.tree.bindings) {
			val bindClass = bind.key.type.jvmType as? Class<*>?
			if (bindClass != null && KodeinController::class.java.isAssignableFrom(bindClass)) {
				val res by kodein.Instance(bind.key.type)
				println("Registering '$res' routes...")
				(res as KodeinController).apply { registerRoutes() }
			}
		}
	}
}

/**
 * Shortcut for binding singletons to the same type.
 */
inline fun <reified T : Any> Kodein.MainBuilder.bindSingleton(crossinline callback: (Kodein) -> T) {
	bind<T>() with singleton { callback(this@singleton.kodein) }
}
