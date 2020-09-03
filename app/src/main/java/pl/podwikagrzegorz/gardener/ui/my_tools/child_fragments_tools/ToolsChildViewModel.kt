package pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.podwikagrzegorz.gardener.data.domain.Tool
import pl.podwikagrzegorz.gardener.data.repo.ToolRepository
import timber.log.Timber

class ToolsChildViewModel @ViewModelInject constructor(
    private val toolRepository: ToolRepository
) : ViewModel() {

    private var _listOfTools: List<Tool> = listOf()
    val listOfTools: List<Tool>
        get() = _listOfTools

    private val _eventAddTool = MutableLiveData<Boolean>()
    val eventAddTool: LiveData<Boolean>
        get() = _eventAddTool

    private val _errorEditTextEmpty = MutableLiveData<Boolean>()
    val errorEditTextEmpty: LiveData<Boolean>
        get() = _errorEditTextEmpty

    fun onAddTool(toolName: String, numbOfToolsAsString: String) {
        if (toolName.isNotEmpty() && numbOfToolsAsString.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                val tool = Tool(toolName, numbOfToolsAsString.toInt())
                toolRepository.insert(tool)
                _eventAddTool.postValue(true)
            }
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

    fun deleteTool(toolName: String) =
        viewModelScope.launch(Dispatchers.IO) {
            toolRepository.delete(toolName)
        }

    fun getQuery() =
        toolRepository.getQuery()

    fun getQuerySortedByName() =
        toolRepository.getQuerySortedByName()

    fun getQuerySortedByNumberOfTools() =
        toolRepository.getQuerySortedByNumberOfTools()

    fun getQuerySortedByTimestamp() =
        toolRepository.getQuerySortedByTimestamp()

    fun preInitialize() {}

    init {
        initializeListOfTools()
        Timber.i("Initialize ${javaClass.name}")
    }

    private fun initializeListOfTools() {
        viewModelScope.launch {
            _listOfTools = toolRepository.getAllTools()
        }
    }
}



/*    private val _listOfToolNames = mutableListOf<String>()
    val listOfToolNames : List<String>
        get() = _listOfToolNames
    fun findMaxValueOf(itemName: String): Int =
        toolDAO.findMaxValueOf(itemName) ?: GardenerApp.MAX_NUMBER_OF_MACHINES

fun getListOfTools(): List<Tool> =
    listOfTools.value ?: emptyList()
*/
