package pl.podwikagrzegorz.gardener.data.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.lang.StringBuilder

open class WorkerRealm(
    @PrimaryKey
    var id: Long = 0,
    var name: String = "",
    var surname: String = ""
) : RealmObject() {
    fun getFullName(): String =
        StringBuilder().append(name).append(" ").append(surname).toString()
}