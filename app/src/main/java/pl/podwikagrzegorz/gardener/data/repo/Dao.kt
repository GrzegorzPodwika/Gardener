package pl.podwikagrzegorz.gardener.data.repo

import com.google.firebase.firestore.Query

interface Dao<T> {
    suspend fun insert(item : T)
    suspend fun update(documentId: String, newItem: T)
    suspend fun delete(documentId: String)

    fun getQuery(): Query
    fun getQuerySortedByName(): Query
    fun getQuerySortedByTimestamp(): Query
}