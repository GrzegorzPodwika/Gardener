package pl.podwikagrzegorz.gardener.data.realm

import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmModel
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.data.daos.*

fun <T: RealmModel> RealmResults<T>.asLiveData() = RealmLiveData<T>(this)
fun <T> RealmList<T>.asLiveList() = MutableLiveData(this)
fun <T> List<T>.asLiveList() = MutableLiveData(this)
fun Realm.noteDAO() : NoteDAO = NoteDAO(this)
fun Realm.machineDAO() : MachineDAO = MachineDAO(this)
fun Realm.toolDAO() : ToolDAO = ToolDAO(this)
fun Realm.propertyDAO() : PropertyDAO = PropertyDAO(this)