package pl.podwikagrzegorz.gardener.data.realm

import io.realm.RealmObject
import pl.podwikagrzegorz.gardener.data.domain.Item

open class ItemRealm(
    var itemName: String = "",
    var numberOfItems: Int = 0,
    var isActive: Boolean = true
) : RealmObject() {
    fun asItem(): Item =
        Item(itemName, numberOfItems, isActive)
}