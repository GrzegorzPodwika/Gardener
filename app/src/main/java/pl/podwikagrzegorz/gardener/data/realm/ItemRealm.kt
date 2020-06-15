package pl.podwikagrzegorz.gardener.data.realm

import io.realm.RealmObject

open class ItemRealm(
    var itemName: String = "",
    var numberOfItems: Int = 0
): RealmObject()