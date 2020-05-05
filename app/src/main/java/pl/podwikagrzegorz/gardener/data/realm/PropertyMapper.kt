package pl.podwikagrzegorz.gardener.data.realm

import pl.podwikagrzegorz.gardener.data.pojo.Property

class PropertyMapper : ItemMapper<Property, PropertyRealm> {
    override fun fromRealm(itemRealm: PropertyRealm): Property {
        val property = Property()
        property.id = itemRealm.id
        property.propertyName = itemRealm.propertyName
        property.numberOfProperties = itemRealm.numberOfProperties

        return property
    }
}