package com.lookieloo.services.core.data

import org.bson.types.ObjectId
import org.litote.kmongo.Id
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.id.toId
import org.litote.kmongo.util.idValue

open class BaseDaoAsync<T : Any>(private val collection: CoroutineCollection<T>) :
	IBaseDaoAsync<T> {

	override suspend fun get(id: String): T? {
		return get(ObjectId(id).toId())
	}

	override suspend fun get(id: ObjectId): T? {
		return get(id.toId())
	}

	override suspend fun get(id: Id<T>): T? {
		return collection.findOneById(id)
	}

	override suspend fun save(obj: T): Any? {
		return collection.save(obj).idValue
	}

	override suspend fun delete(id: Id<T>): Boolean {
		return collection.deleteOneById(id).wasAcknowledged()
	}

}
