package com.lookieloo.services.loo.models.response

import com.lookieloo.services.loo.models.LatLng
import com.lookieloo.services.loo.models.Loo
import java.util.*


class LooInfo private constructor(
	val id: String,
	val location: LatLng?,
	val description: String?
) {
	override fun equals(other: Any?) = other is LooInfo &&
			id == other.id &&
			location == other.location &&
			description == other.description

	override fun hashCode() = Objects.hash(id, location, description)

	override fun toString() = "Loo(key=$id, location=$location, name='$description')"

	class Builder {
		@set:JvmSynthetic // Hide 'void' setter from Java.
		var id: String? = null
		@set:JvmSynthetic // Hide 'void' setter from Java.
		var location: LatLng? = null
		@set:JvmSynthetic // Hide 'void' setter from Java.
		var description: String? = null

		fun setId(id: String?) = apply { this.id = id }
		fun setLocation(location: LatLng?) = apply { this.location = location }
		fun setDescription(name: String?) = apply { this.description = name }

		fun build() = LooInfo(
			id = checkNotNull(this.id) { "A loo 'id' is required" },
			location = this.location,
			description = this.description
		)
	}
}

@JvmSynthetic // Hide from Java callers who should use Builder.
fun LooInfo(initializer: LooInfo.Builder.() -> Unit): LooInfo {
	return LooInfo.Builder().apply(initializer).build()
}

fun Loo.toInfo() =
	LooInfo {
		id = this@toInfo.id.toString()
		location = this@toInfo.location?.let { loc -> loc.getLat()?.let { LatLng(it, loc.getLng()!!) } }
		description = this@toInfo.description
	}
