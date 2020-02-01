package com.lookieloo.services.loo.models

import java.util.*

data class Location(
	val type: String = "Point",
	val coordinates: DoubleArray
) {
	fun getLat() = coordinates.getOrNull(1)
	fun getLng() = coordinates.getOrNull(0)

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as Location

		if (type != other.type) return false
		if (!coordinates.contentEquals(other.coordinates)) return false

		return true
	}

	override fun hashCode() = Objects.hash(type, coordinates)
}
