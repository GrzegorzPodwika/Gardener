package pl.podwikagrzegorz.gardener.data.realm

import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmModel
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.data.daos.*

fun <T: RealmModel> RealmResults<T>.asLiveData() = RealmLiveData<T>(this)
fun <T> RealmList<T>.asLiveList() = MutableLiveData(this)
