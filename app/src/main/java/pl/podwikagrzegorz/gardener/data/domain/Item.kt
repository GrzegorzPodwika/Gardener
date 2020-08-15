package pl.podwikagrzegorz.gardener.data.domain

import pl.podwikagrzegorz.gardener.data.realm.ItemRealm

data class Item(
    var itemName: String = "",
    var numberOfItems: Int = 0,
    var isActive: Boolean = true
) {
    fun asItemRealm() : ItemRealm =
        ItemRealm(itemName, numberOfItems, isActive)
}