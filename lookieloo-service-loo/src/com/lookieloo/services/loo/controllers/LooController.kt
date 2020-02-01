package com.lookieloo.services.loo.controllers

import com.lookieloo.services.core.controller.KodeinController
import com.lookieloo.services.core.logging.logger
import com.lookieloo.services.loo.data.LooRepository
import com.lookieloo.services.loo.models.request.LooByLocRequest
import com.lookieloo.services.loo.models.request.LooCreate
import com.lookieloo.services.loo.models.response.toInfo
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import org.kodein.di.Kodein
import org.kodein.di.generic.instance


class LooController(kodein: Kodein) : KodeinController(kodein) {
	private val looRepository: LooRepository by instance()

	override fun Routing.registerRoutes() {
		get("/test") {
			logger.info("This is a test endpoint!")
			call.respond("Test Success!")
		}

		get("/loo/{id}") {
			logger.info("Calling a loo!")
			val looId = call.parameters["id"]

			val loo = looId?.let { id -> looRepository.get(id) }
			loo?.let { a -> call.respond(a.toInfo()) }
		}

		post("/loo") {
			val toCreate = call.receive<LooCreate>()
			//looRepository.save(Loo())
			//call.respond(newLoo.toInfo())
		}

		post("/loo/findNearby") {
			val request = call.receive<LooByLocRequest>()

			val results = looRepository.findByLocation(
				request.latitude,
				request.longitude,
				request.minDistance,
				request.maxDistance
			)
			call.respond(results)
		}
	}
}
