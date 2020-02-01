package com.lookieloo.services.loo.data

import com.lookieloo.services.core.data.BaseDaoAsync
import com.lookieloo.services.loo.models.Loo
import org.litote.kmongo.MongoOperator
import org.litote.kmongo.coroutine.CoroutineCollection

class LooRepository(private val collection: CoroutineCollection<Loo>) : BaseDaoAsync<Loo>(collection) {

	//47.6062° N, 122.3321° W
	suspend fun findByLocation(latitude : Double, longitude: Double, minDistance : Double, maxDistance : Double): List<Loo> {

		val results = collection.find(
			"{ " +
					"location: {" +
					"${MongoOperator.nearSphere}:{" +
					"${MongoOperator.geometry}:{" +
					"type: \"Point\"," +
					"coordinates: [${longitude}, ${latitude}], }," +
					"\$maxDistance: $maxDistance," +
					"\$minDistance: $minDistance," +
					"}," +
					"}," +
					"}"
		)

		return results.toList()
	}
}
