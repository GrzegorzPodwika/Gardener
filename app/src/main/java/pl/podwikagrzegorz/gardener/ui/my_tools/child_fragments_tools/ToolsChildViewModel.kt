package pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.data.daos.ToolDAO
import pl.podwikagrzegorz.gardener.data.pojo.Tool
import pl.podwikagrzegorz.gardener.data.realm.ToolModule
import pl.podwikagrzegorz.gardener.data.realm.ToolRealm
import pl.podwikagrzegorz.gardener.data.realm.toolDAO

class ToolsChildViewModel : ViewModel() {
    private val realm: Realm
    private val toolDAO : ToolDAO

    fun getToolData() : MutableLiveData<RealmResults<ToolRealm>>
            = toolDAO.getLiveRealmResults()

    fun addTool(tool : Tool){
        toolDAO.insertItem(tool)
    }

    fun deleteTool(id: Long?){
        id?.let { toolDAO.deleteItem(id) }
    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }

    init {
        val realmConfig = RealmConfiguration.Builder()
            .name(REALM_TOOL_NAME)
            .modules(ToolModule())
            .build()
        realm = Realm.getInstance(realmConfig)
        toolDAO = realm.toolDAO()
    }

    companion object{
        const val REALM_TOOL_NAME = "tool.realm"
    }
}
