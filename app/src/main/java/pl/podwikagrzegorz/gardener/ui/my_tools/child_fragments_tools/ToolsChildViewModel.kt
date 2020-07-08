package pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.data.daos.ToolDAO
import pl.podwikagrzegorz.gardener.data.pojo.Tool
import pl.podwikagrzegorz.gardener.data.realm.ToolRealm

class ToolsChildViewModel : ViewModel() {
    private val toolDAO = ToolDAO()

    private val _listOfTools: MutableLiveData<RealmResults<ToolRealm>> =
        toolDAO.getLiveRealmResults()

    val listOfTools: LiveData<RealmResults<ToolRealm>>
        get() = _listOfTools

    fun getListOfToolsAsRealmResults() =
        toolDAO.getRealmResults()

    fun getSingleTool(id: Long?): ToolRealm? = id?.let { toolDAO.getItemById(it) }

    fun addTool(tool: ToolRealm) {
        toolDAO.insertItem(tool)
    }

    fun deleteTool(id: Long?) {
        id?.let { toolDAO.deleteItem(id) }
    }

    override fun onCleared() {
        toolDAO.closeRealm()
        super.onCleared()
    }

    fun findMaxValueOf(itemName: String): Int {
        val searchedMachine = _listOfTools.value?.find {
            it.toolName == itemName
        }

        return searchedMachine?.numberOfTools ?: GardenerApp.MAX_NUMBER_OF_MACHINES
    }

}
