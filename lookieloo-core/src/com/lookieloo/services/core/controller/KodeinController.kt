package com.lookieloo.services.core.controller

import io.ktor.application.Application
import io.ktor.routing.Routing
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

/**
 * A [KodeinAware] base class for Controllers handling routes.
 */
abstract class KodeinController(override val kodein: Kodein) : KodeinAware {
	/**
	 * Injected dependency with the current [Application].
	 */
	val application: Application by instance()

	/**
	 * Method that subtypes must override to register the handled [Routing] routes.
	 */
	abstract fun Routing.registerRoutes()
}
