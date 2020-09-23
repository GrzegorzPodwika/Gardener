package pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.podwikagrzegorz.gardener.data.domain.Property
import pl.podwikagrzegorz.gardener.data.repo.PropertyRepository

class PropertiesChildViewModel @ViewModelInject constructor(
    private val propertyRepository: PropertyRepository
) : ViewModel() {
    private var _listOfProperties: List<Property> = listOf()
    val listOfProperties: List<Property>
        get() = _listOfProperties

    private val _listOfPropertyNames = mutableListOf<String>()
    val listOfPropertyNames: List<String>
        get() = _listOfPropertyNames

    private val _eventAddProperty = MutableLiveData<Boolean>()
    val eventAddProperty: LiveData<Boolean>
        get() = _eventAddProperty

    private val _errorEditTextEmpty = MutableLiveData<Boolean>()
    val errorEditTextEmpty: LiveData<Boolean>
        get() = _errorEditTextEmpty

    fun onAddProperty(propertyName: String, amountOfProperties: String) {
        if (propertyName.isNotEmpty() && amountOfProperties.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                val property = Property(propertyName, amountOfProperties)
                propertyRepository.insert(property)
                _eventAddProperty.postValue(true)
            }
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

    fun updateProperty(newProperty: Property) =
        viewModelScope.launch(Dispatchers.IO) {
            propertyRepository.update(newProperty.documentId,  newProperty)
        }

    fun deleteProperty(propertyName: String) =
        viewModelScope.launch(Dispatchers.IO) {
            propertyRepository.delete(propertyName)
        }

    fun getQuery() =
        propertyRepository.getQuery()

    fun getQuerySortedByName() =
        propertyRepository.getQuerySortedByName()

    fun getQuerySortedByNumberOfProperties() =
        propertyRepository.getQuerySortedByNumberOfProperties()

    fun getQuerySortedByTimestamp() =
        propertyRepository.getQuerySortedByTimestamp()

    fun preInitialize() {}

    init {
        fetchListOfProperties()
    }

    private fun fetchListOfProperties() {
        viewModelScope.launch(Dispatchers.Main) {
            _listOfProperties = propertyRepository.getAllProperties()
            for (property in _listOfProperties) {
                _listOfPropertyNames.add(property.propertyName)
            }
        }
    }
}


/*
    fun findMaxValueOf(itemName: String): Int =
        propertyDAO.findMaxValueOf(itemName) ?: GardenerApp.MAX_NUMBER_OF_MACHINES

    fun getListOfProperties(): List<Property> =
        listOfProperties.value ?: emptyList()*/
