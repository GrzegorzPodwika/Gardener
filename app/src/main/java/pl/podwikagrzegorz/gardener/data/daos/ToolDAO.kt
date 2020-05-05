package pl.podwikagrzegorz.gardener.data.daos

import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import pl.podwikagrzegorz.gardener.data.pojo.Tool
import pl.podwikagrzegorz.gardener.data.realm.NoteRealm
import pl.podwikagrzegorz.gardener.data.realm.ToolMapper
import pl.podwikagrzegorz.gardener.data.realm.ToolRealm
import pl.podwikagrzegorz.gardener.data.realm.asLiveData

class ToolDAO(override val realm: Realm) : AbstractRealmDAO<Tool, ToolRealm>(realm) {

    override fun insertItem(item: Tool) {
        realm.executeTransaction{
            val toolRealm = ToolRealm(generateId(), item.toolName, item.numberOfTools)
            it.insert(toolRealm)
        }
    }

    override fun getItemById(id: Long): Tool? {
        val toolRealm = realm.where<ToolRealm>().equalTo(ID, id).findFirst()

        return toolRealm?.let { ToolMapper().fromRealm(it) }
    }

    override fun updateItem(item: Tool) {
        realm.executeTransactionAsync { bgRealm ->
            val toolRealm = bgRealm.where<ToolRealm>().equalTo(ID, item.id).findFirst()
            toolRealm?.toolName = item.toolName
            toolRealm?.numberOfTools = item.numberOfTools
        }
    }

    override fun deleteItem(id: Long) {
        realm.executeTransactionAsync {bgRealm ->
            val toolToDelete = bgRealm.where<ToolRealm>().equalTo(ID, id).findFirst()
            toolToDelete?.deleteFromRealm()
        }
    }

    override fun getRealmResults(): RealmResults<ToolRealm>
            = realm.where<ToolRealm>().findAll()


    override fun getLiveRealmResults(): MutableLiveData<RealmResults<ToolRealm>>
            = realm.where<ToolRealm>().findAllAsync().asLiveData()

    override fun getItemsList(): List<Tool> {
        val tools : MutableList<Tool> = ArrayList()
        val toolMapper = ToolMapper()
        val realmResults = getRealmResults()

        for (tool in realmResults){
            tools.add(toolMapper.fromRealm(tool))
        }

        return tools
    }

    override fun deleteAllItems() {
        realm.executeTransactionAsync { bgRealm ->
            realm.where<ToolRealm>().findAll().deleteAllFromRealm()
        }
    }

    override fun generateId(): Long {
        val maxValue = realm.where<ToolRealm>().max(ID)
        var nextId: Long = 0

        if (maxValue != null) {
            nextId = maxValue.toLong() + 1
        }

        return nextId
    }
}