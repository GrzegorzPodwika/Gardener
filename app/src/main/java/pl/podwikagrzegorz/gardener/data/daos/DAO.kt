package pl.podwikagrzegorz.gardener.data.daos

import androidx.lifecycle.MutableLiveData
import io.realm.RealmModel
import io.realm.RealmResults

interface DAO<T> {
    fun insertItem(item: T)
    fun updateItem(item: T)
    fun deleteItem(id: Long)

    fun getItemById(id: Long): T?
    fun getDomainData() : List<T>
    fun getLiveDomainData() : MutableLiveData<List<T>>

    fun closeRealm()
}