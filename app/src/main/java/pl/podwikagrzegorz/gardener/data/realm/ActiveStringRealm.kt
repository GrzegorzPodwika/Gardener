package pl.podwikagrzegorz.gardener.data.realm

import io.realm.RealmObject

open class ActiveStringRealm(
    var name: String = "",
    var isActive : Boolean = true
) : RealmObject()