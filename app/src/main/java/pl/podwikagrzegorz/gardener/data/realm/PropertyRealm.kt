package pl.podwikagrzegorz.gardener.data.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import pl.podwikagrzegorz.gardener.data.domain.Property

open class PropertyRealm(
    @PrimaryKey var id: Long = 0,
    var propertyName: String = "",
    var numberOfProperties: Int = 0
) : RealmObject() {
    fun asProperty() : Property =
        Property(id, propertyName, numberOfProperties)
}