package pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.data.daos.OnExecuteTransactionListener
import pl.podwikagrzegorz.gardener.data.daos.PropertyDAO
import pl.podwikagrzegorz.gardener.data.domain.Property

class PropertiesChildViewModel : ViewModel(), OnExecuteTransactionListener {
    private val propertyDAO = PropertyDAO()

    private val _listOfProperties: MutableLiveData<List<Property>> =
        propertyDAO.getLiveDomainData()
    val listOfProperties: LiveData<List<Property>>
        get() = _listOfProperties

    private val _eventAddProperty = MutableLiveData<Boolean>()
    val eventAddProperty : LiveData<Boolean>
        get() = _eventAddProperty

    private val _errorEditTextEmpty = MutableLiveData<Boolean>()
    val errorEditTextEmpty : LiveData<Boolean>
        get() = _errorEditTextEmpty

    fun onAddProperty(propertyName: String, numbOfPropertiesAsString: String) {
        if (numbOfPropertiesAsString.isNotEmpty()) {
            val property = Property(0, propertyName, numbOfPropertiesAsString.toInt())
            propertyDAO.insertItem(property)
            _eventAddProperty.value = true
        } else {
            onErrorShow()
        }
    }

    private fun onErrorShow() {
        _errorEditTextEmpty.value = true
    }

    fun onAddPropertyComplete() {
        _eventAddProperty.value = false
    }

    fun onErrorShowComplete() {
        _errorEditTextEmpty.value = false
    }

    fun findMaxValueOf(itemName: String): Int =
        propertyDAO.findMaxValueOf(itemName) ?: GardenerApp.MAX_NUMBER_OF_MACHINES


    fun deleteProperty(id: Long) {
        propertyDAO.deleteItem(id)
    }

    override fun onAsyncTransactionSuccess() {
        fetchFreshData()
    }

    private fun fetchFreshData() {
        _listOfProperties.value = propertyDAO.getDomainData()
    }

    override fun onCleared() {
        propertyDAO.closeRealm()
        super.onCleared()
    }

    init {
        propertyDAO.listener = this
    }
}

/*
    fun getListOfProperties(): List<Property> =
        listOfProperties.value ?: emptyList()*/
