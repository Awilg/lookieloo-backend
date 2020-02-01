package com.lookieloo.services.loo.models.request

import com.lookieloo.services.loo.models.LatLng

data class LooCreate(
	val description: String,
	val latLng: LatLng
)
