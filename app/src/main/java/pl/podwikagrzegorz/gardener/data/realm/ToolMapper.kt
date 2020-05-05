package pl.podwikagrzegorz.gardener.data.realm

import pl.podwikagrzegorz.gardener.data.pojo.Tool

class ToolMapper : ItemMapper<Tool, ToolRealm> {
    override fun fromRealm(itemRealm: ToolRealm): Tool {
        val tool = Tool()
        tool.id = itemRealm.id
        tool.toolName = itemRealm.toolName
        tool.numberOfTools = itemRealm.numberOfTools

        return tool
    }
}