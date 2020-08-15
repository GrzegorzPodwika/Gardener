package pl.podwikagrzegorz.gardener.data.domain

import pl.podwikagrzegorz.gardener.data.realm.PropertyRealm

data class Property(
    var id: Long = 0,
    var propertyName: String = "",
    var numberOfProperties: Int = 0
) {
    fun asPropertyRealm(): PropertyRealm =
        PropertyRealm(id, propertyName, numberOfProperties)
}