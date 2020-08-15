package pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.data.daos.OnExecuteTransactionListener
import pl.podwikagrzegorz.gardener.data.daos.ToolDAO
import pl.podwikagrzegorz.gardener.data.domain.Tool

class ToolsChildViewModel : ViewModel(), OnExecuteTransactionListener {
    private val toolDAO = ToolDAO()

    private val _listOfTools: MutableLiveData<List<Tool>> =
        toolDAO.getLiveDomainData()
    val listOfTools: LiveData<List<Tool>>
        get() = _listOfTools

    private val _eventAddTool = MutableLiveData<Boolean>()
    val eventAddTool: LiveData<Boolean>
        get() = _eventAddTool

    private val _errorEditTextEmpty = MutableLiveData<Boolean>()
    val errorEditTextEmpty : LiveData<Boolean>
        get() = _errorEditTextEmpty

    fun onAddTool(toolName: String, numbOfToolsAsString: String) {
        if (numbOfToolsAsString.isNotEmpty()) {
            val tool = Tool(0, toolName, numbOfToolsAsString.toInt())
            toolDAO.insertItem(tool)
            _eventAddTool.value = true
        } else {
            onErrorShow()
        }
    }

    private fun onErrorShow() {
        _errorEditTextEmpty.value = true
    }

    fun onAddToolComplete() {
        _eventAddTool.value = false
    }

    fun onErrorShowComplete() {
        _errorEditTextEmpty.value = false
    }

    fun findMaxValueOf(itemName: String): Int =
        toolDAO.findMaxValueOf(itemName) ?: GardenerApp.MAX_NUMBER_OF_MACHINES

    fun deleteTool(id: Long) {
        toolDAO.deleteItem(id)
    }

    override fun onAsyncTransactionSuccess() {
        fetchFreshData()
    }

    private fun fetchFreshData() {
        _listOfTools.value = toolDAO.getDomainData()
    }


    override fun onCleared() {
        toolDAO.closeRealm()
        super.onCleared()
    }

    init {
        toolDAO.listener = this
    }
}

/*
fun getListOfTools(): List<Tool> =
    listOfTools.value ?: emptyList()
*/
