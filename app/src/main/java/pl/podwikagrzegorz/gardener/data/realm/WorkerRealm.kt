package pl.podwikagrzegorz.gardener.data.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import pl.podwikagrzegorz.gardener.data.domain.Worker
import java.lang.StringBuilder

open class WorkerRealm(
    @PrimaryKey
    var id: Long = 0,
    var name: String = "",
    var surname: String = ""
) : RealmObject() {
    val fullName : String
        get() = StringBuilder().append(name).append(" ").append(surname).toString()

    fun asWorker() : Worker =
        Worker(id, name, surname)
}