package com.lookieloo.services.core.data

import org.bson.types.ObjectId
import org.litote.kmongo.Id

interface IBaseDaoAsync<T : Any> {
    suspend fun get(id: String) : T?
    suspend fun get(id: ObjectId) : T?
    suspend fun get(id: Id<T>) : T?
    suspend fun save(obj : T) : Any?
    suspend fun delete(id: Id<T>) : Boolean
}
