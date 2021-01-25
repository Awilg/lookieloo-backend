package com.lookieloo.services.loo.models

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId

data class Loo(
	@BsonId val id: Id<Loo> = newId(),
	val title: String,
	val description: String,
	val location: Location?,
	val attributes: Set<String>
)
