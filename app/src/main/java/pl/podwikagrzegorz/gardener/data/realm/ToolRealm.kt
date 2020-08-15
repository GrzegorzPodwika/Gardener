package pl.podwikagrzegorz.gardener.data.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import pl.podwikagrzegorz.gardener.data.domain.Tool

open class ToolRealm(
    @PrimaryKey var id: Long = 0,
    var toolName: String = "",
    var numberOfTools: Int = 0
) : RealmObject() {
    fun asTool(): Tool =
        Tool(id, toolName, numberOfTools)
}