package pl.podwikagrzegorz.gardener.data.daos

import androidx.lifecycle.MutableLiveData
import io.realm.RealmResults

interface DAO<T> {
    fun insertItem(item: T)
    fun updateItem(item: T)
    fun deleteItem(id: Long)

    fun getItemById(id: Long): T?
    fun getRealmResults() : RealmResults<T>
    fun getLiveRealmResults() : MutableLiveData<RealmResults<T>>

    fun closeRealm()
}