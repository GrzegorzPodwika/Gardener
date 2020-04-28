package pl.podwikagrzegorz.gardener.data.realm

import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.data.daos.NoteDAO

fun <T: RealmModel> RealmResults<T>.asLiveData() =
    RealmLiveData<T>(this)
fun Realm.noteDAO() : NoteDAO =
    NoteDAO(this)
