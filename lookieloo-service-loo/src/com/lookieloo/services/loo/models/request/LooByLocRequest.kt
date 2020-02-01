package com.lookieloo.services.loo.models.request

import com.lookieloo.services.loo.models.Latitude
import com.lookieloo.services.loo.models.Longitude

data class LooByLocRequest(
	val latitude: Latitude,
	val longitude: Longitude,
	val minDistance: Double = 0.0,
	val maxDistance: Double = Double.MAX_VALUE
)
