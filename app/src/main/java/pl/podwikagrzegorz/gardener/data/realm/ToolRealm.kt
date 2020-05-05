package pl.podwikagrzegorz.gardener.data.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ToolRealm (
    @PrimaryKey var id: Long = 0,
    var toolName: String = "",
    var numberOfTools: Int = 0) : RealmObject()