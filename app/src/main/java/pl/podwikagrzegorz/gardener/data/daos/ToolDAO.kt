package pl.podwikagrzegorz.gardener.data.daos

import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.kotlin.where
import pl.podwikagrzegorz.gardener.data.realm.*

class ToolDAO : DAO<ToolRealm> {
    private val realm: Realm

    override fun insertItem(item: ToolRealm) {
        val generatedNewId = generateId()

        realm.executeTransactionAsync { bgRealm ->
            item.id = generatedNewId
            bgRealm.insert(item)
        }
    }

    override fun updateItem(item: ToolRealm) {
        realm.executeTransactionAsync { bgRealm ->
            val toolRealm = bgRealm.where<ToolRealm>().equalTo(ID, item.id).findFirst()
            toolRealm?.toolName = item.toolName
            toolRealm?.numberOfTools = item.numberOfTools
        }
    }

    override fun deleteItem(id: Long) {
        realm.executeTransactionAsync { bgRealm ->
            val toolToDelete = bgRealm.where<ToolRealm>().equalTo(ID, id).findFirst()
            toolToDelete?.deleteFromRealm()
        }
    }


    override fun getItemById(id: Long): ToolRealm? =
        realm.where<ToolRealm>().equalTo(ID, id).findFirst()

    override fun getRealmResults(): RealmResults<ToolRealm>
            = realm.where<ToolRealm>().findAllAsync()


    override fun getLiveRealmResults(): MutableLiveData<RealmResults<ToolRealm>> =
        realm.where<ToolRealm>().findAllAsync().asLiveData()


    override fun closeRealm() {
        realm.close()
    }

    private fun generateId(): Long {
        val maxValue = realm.where<ToolRealm>().max(ID)
        var nextId: Long = 0

        if (maxValue != null) {
            nextId = maxValue.toLong() + 1
        }

        return nextId
    }

    fun findMaxValueOf(itemName: String): Int?
        = realm.where<ToolRealm>().equalTo("toolName", itemName).findFirst()?.numberOfTools

    init {
        val realmConfig = RealmConfiguration.Builder()
            .name(REALM_TOOL_NAME)
            .modules(ToolModule())
            .build()
        realm = Realm.getInstance(realmConfig)
    }

    companion object {
        private const val ID = "id"
        private const val REALM_TOOL_NAME = "tool.realm"
    }
}