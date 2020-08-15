package pl.podwikagrzegorz.gardener.data.realm

import io.realm.RealmObject
import pl.podwikagrzegorz.gardener.data.domain.ActiveString

open class ActiveStringRealm(
    var name: String = "",
    var isActive : Boolean = true
) : RealmObject() {
    fun asActiveString() : ActiveString =
        ActiveString(name, isActive)
}