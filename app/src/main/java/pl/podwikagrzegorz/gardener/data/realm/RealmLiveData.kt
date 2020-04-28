package pl.podwikagrzegorz.gardener.data.realm

import androidx.lifecycle.MutableLiveData
import io.realm.RealmChangeListener
import io.realm.RealmModel
import io.realm.RealmResults

class RealmLiveData <T : RealmModel>(private val realmResults: RealmResults<T>) : MutableLiveData<RealmResults<T>>() {

    private val listener = RealmChangeListener<RealmResults<T>> { results -> value = results }

    override fun onActive() {
        realmResults.addChangeListener(listener)
    }

    override fun onInactive() {
        realmResults.removeChangeListener(listener)
    }
}