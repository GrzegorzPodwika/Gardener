package pl.podwikagrzegorz.gardener.data.domain

import pl.podwikagrzegorz.gardener.data.realm.ActiveStringRealm

data class ActiveString(
    var name: String = "",
    var isActive : Boolean = true
) {
    fun asActiveStringRealm() : ActiveStringRealm =
        ActiveStringRealm(name, isActive)
}