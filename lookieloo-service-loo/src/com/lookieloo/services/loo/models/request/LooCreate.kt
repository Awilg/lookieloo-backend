package com.lookieloo.services.loo.models.request

import com.lookieloo.services.loo.models.LatLng
import com.lookieloo.services.loo.models.Location
import com.lookieloo.services.loo.models.Loo

data class LooCreate(
	val title: String,
	val description: String,
	val location: LatLng,
	val attributes: Set<String>
)

fun LooCreate.toLoo(): Loo {
	return Loo(
		description = this@toLoo.description,
		title = this@toLoo.title,
		location = Location(
			coordinates = doubleArrayOf(
				this@toLoo.location.longitude,
				this@toLoo.location.latitude
			)
		),
		attributes = this@toLoo.attributes
	)
}

