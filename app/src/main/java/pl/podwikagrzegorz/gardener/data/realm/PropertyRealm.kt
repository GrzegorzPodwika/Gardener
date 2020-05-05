package pl.podwikagrzegorz.gardener.data.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class PropertyRealm (
    @PrimaryKey var id: Long = 0,
    var propertyName: String = "",
    var numberOfProperties: Int = 0) : RealmObject()