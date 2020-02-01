package com.lookieloo.services.loo

import com.fasterxml.jackson.databind.SerializationFeature
import com.lookieloo.services.core.kodein.bindSingleton
import com.lookieloo.services.core.kodein.kodeinApplication
import com.lookieloo.services.loo.controllers.LooController
import com.lookieloo.services.loo.data.LooRepository
import com.lookieloo.services.loo.models.Loo
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import io.ktor.request.path
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.id.jackson.IdJacksonModule
import org.litote.kmongo.reactivestreams.KMongo
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
fun Application.module() {

	install(CallLogging) {
		level = Level.INFO
		filter { call -> call.request.path().startsWith("/") }
	}

	install(DefaultHeaders) {
		header("X-Engine", "Ktor") // will send this header with each response
	}

	install(Authentication) {
	}

	install(ContentNegotiation) {
		jackson {
			enable(SerializationFeature.INDENT_OUTPUT)
			registerModule(IdJacksonModule())
		}
	}

	val client = KMongo.createClient("mongodb://mongo:27017").coroutine
	val database = client.getDatabase("Lookieloo-Loo")
	val looCollection = database.getCollection<Loo>()

	kodeinApplication {
		bind<LooRepository>() with singleton { LooRepository(looCollection) }
		bindSingleton { LooController(it) }
	}
}

