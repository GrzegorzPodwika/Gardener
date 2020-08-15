package pl.podwikagrzegorz.gardener.data.daos

import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import pl.podwikagrzegorz.gardener.data.domain.Tool
import pl.podwikagrzegorz.gardener.data.domain.asListOfTools
import pl.podwikagrzegorz.gardener.data.realm.*
import timber.log.Timber

class ToolDAO: DAO<Tool> {
    private val realm: Realm
    var listener: OnExecuteTransactionListener? = null

    override fun insertItem(item: Tool) {
        val generatedNewId = generateId()
        val toolRealm = item.asToolRealm()

        realm.executeTransactionAsync ({ bgRealm ->
            toolRealm.id = generatedNewId
            bgRealm.insert(toolRealm)
        }, {
            listener?.onAsyncTransactionSuccess()
        }, { error ->
            Timber.e(error)
        })
    }

    override fun updateItem(item: Tool) {
        realm.executeTransactionAsync ({ bgRealm ->
            val toolRealm = bgRealm.where<ToolRealm>().equalTo(ID, item.id).findFirst()
            toolRealm?.toolName = item.toolName
            toolRealm?.numberOfTools = item.numberOfTools
        }, {
            listener?.onAsyncTransactionSuccess()
        }, { error ->
            Timber.e(error)
        })
    }

    override fun deleteItem(id: Long) {
        realm.executeTransactionAsync ({ bgRealm ->
            val toolToDelete = bgRealm.where<ToolRealm>().equalTo(ID, id).findFirst()
            toolToDelete?.deleteFromRealm()
        }, {
            listener?.onAsyncTransactionSuccess()
        }, { error ->
            Timber.e(error)
        })
    }


    override fun getItemById(id: Long): Tool? =
        realm.where<ToolRealm>().equalTo(ID, id).findFirstAsync()?.asTool()

    override fun getDomainData(): List<Tool> =
        realm.where<ToolRealm>().findAllAsync().asListOfTools()

    override fun getLiveDomainData(): MutableLiveData<List<Tool>> =
        getDomainData().asLiveList()


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