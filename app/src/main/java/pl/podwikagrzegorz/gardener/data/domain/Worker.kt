package pl.podwikagrzegorz.gardener.data.domain

import pl.podwikagrzegorz.gardener.data.realm.WorkerRealm
import java.lang.StringBuilder

data class Worker(
    var id: Long = 0,
    var name: String = "",
    var surname: String = ""
) {
    val fullName : String
        get() = StringBuilder().append(name).append(" ").append(surname).toString()

    fun asWorkerRealm() : WorkerRealm =
        WorkerRealm(id, name, surname)
}