package pl.podwikagrzegorz.gardener.data.realm

import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.data.daos.MachineDAO
import pl.podwikagrzegorz.gardener.data.daos.NoteDAO
import pl.podwikagrzegorz.gardener.data.daos.PropertyDAO
import pl.podwikagrzegorz.gardener.data.daos.ToolDAO

fun <T: RealmModel> RealmResults<T>.asLiveData() = RealmLiveData<T>(this)
fun Realm.noteDAO() : NoteDAO = NoteDAO(this)
fun Realm.machineDAO() : MachineDAO = MachineDAO(this)
fun Realm.toolDAO() : ToolDAO = ToolDAO(this)
fun Realm.propertyDAO() : PropertyDAO = PropertyDAO(this)
