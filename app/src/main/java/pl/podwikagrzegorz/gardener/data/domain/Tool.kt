package pl.podwikagrzegorz.gardener.data.domain

import pl.podwikagrzegorz.gardener.data.realm.ToolRealm

data class Tool(
    var id: Long = 0,
    var toolName: String = "",
    var numberOfTools: Int = 0
) {
    fun asToolRealm(): ToolRealm =
        ToolRealm(id, toolName, numberOfTools)

}