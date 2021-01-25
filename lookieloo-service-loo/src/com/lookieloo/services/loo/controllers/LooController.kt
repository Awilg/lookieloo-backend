package com.lookieloo.services.loo.controllers

import com.lookieloo.services.core.controller.KodeinController
import com.lookieloo.services.core.logging.logger
import com.lookieloo.services.loo.data.LooRepository
import com.lookieloo.services.loo.models.request.LooByLocRequest
import com.lookieloo.services.loo.models.request.LooCreate
import com.lookieloo.services.loo.models.request.toLoo
import com.lookieloo.services.loo.models.response.toInfo
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import org.kodein.di.Kodein
import org.kodein.di.generic.instance


class LooController(kodein: Kodein) : KodeinController(kodein) {
	private val looRepository: LooRepository by instance()

	override fun Routing.registerRoutes() {
		healthCheck()
		getLoo()
		deleteLoo()
		createLoo()
		findNearbyLoos()
	}

	private fun Routing.healthCheck() {
		get("/test") {
			call.respond("Test Success!")
		}
	}

	private fun Routing.getLoo() {
		get("/loo/{id}") {
			val looId = call.parameters["id"]

			val loo = looId?.let { id -> looRepository.get(id) }
			loo?.let { a -> call.respond(a.toInfo()) }
		}
	}

	private fun Routing.deleteLoo() {
		delete("/loo/{id}") {
			val looId = call.parameters["id"]

			looId?.let { id -> looRepository.delete(id) }
			call.respond("Deleted!")
		}
	}

	private fun Routing.createLoo() {
		post("/loo") {
			val toCreate = call.receive<LooCreate>()
			val loo = toCreate.toLoo()
			looRepository.save(loo)
			call.respond(loo.toInfo())
		}
	}

	private fun Routing.findNearbyLoos() {
		post("/loo/findNearby") {
			val request = call.receive<LooByLocRequest>()
			val filterParams: List<String>? = call.request.queryParameters.getAll("filter")

			val results = looRepository.findByLocation(
				request.latitude,
				request.longitude,
				request.minDistance,
				request.maxDistance,
				filterParams
			).map { loo -> loo.toInfo() }

			call.respond(results)
		}
	}
}
