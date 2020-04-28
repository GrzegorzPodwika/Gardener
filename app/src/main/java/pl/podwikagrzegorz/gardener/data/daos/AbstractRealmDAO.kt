package pl.podwikagrzegorz.gardener.data.daos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults

abstract class AbstractRealmDAO<T, K> (open val realm: Realm) {

    abstract fun insertItem(item: T)
    abstract fun getItemById(id: Long): T?

    abstract fun updateItem(item: T)
    abstract fun deleteItem(id: Long)
    abstract fun getRealmResults() : RealmResults<K>
    abstract fun getLiveRealmResults() : MutableLiveData<RealmResults<K>>
    abstract fun getItemsList() : List<T>
    abstract fun deleteAllItems()
    abstract fun generateId() : Long

    fun close() = realm.close()

    companion object{
        const val ID = "id"
    }
}