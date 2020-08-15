package pl.podwikagrzegorz.gardener.data.daos

import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import pl.podwikagrzegorz.gardener.data.domain.*
import pl.podwikagrzegorz.gardener.data.realm.*
import java.util.*
import kotlin.NoSuchElementException

class GardenComponentsDAO(gardenID: Long) {

    private val realm: Realm
    private val gardenRealm: GardenRealm
    var listener: OnExecuteTransactionListener? = null


    // Basic Garden
/*
    fun getBasicGarden(): MutableLiveData<BasicGardenRealm> =
        MutableLiveData(gardenRealm.basicGarden!!)
*/
    fun getBasicGarden(): BasicGardenRealm =
        realm.copyFromRealm(gardenRealm.basicGarden!!)


    // Description
    fun addDescriptionToList(description: String) {
        realm.executeTransaction {
            gardenRealm.listOfDescriptions.add(ActiveStringRealm(description))
        }
        notifyListenerAboutSuccess()
    }

    private fun notifyListenerAboutSuccess() {
        listener?.onTransactionSuccess()
    }

    fun reverseFlagOnDescription(position: Int) {
        realm.executeTransaction {
            gardenRealm.listOfDescriptions[position]?.apply {
                isActive = !isActive
            }
        }
        notifyListenerAboutSuccess()
    }

    fun deleteDescriptionFromList(position: Long) {
        realm.executeTransaction {
            gardenRealm.listOfDescriptions.removeAt(position.toInt())
        }
        notifyListenerAboutSuccess()
    }

    fun getListOfDescriptions() : List<ActiveString> =
        gardenRealm.listOfDescriptions.asListOfActiveStrings()

    fun getLiveListOfDescriptions(): MutableLiveData<List<ActiveString>> =
        gardenRealm.listOfDescriptions.asListOfActiveStrings().asLiveList()

    //Notes
    fun addNoteToList(note: String) {
        realm.executeTransaction {
            gardenRealm.listOfNotes.add(ActiveStringRealm(note))
        }
        notifyListenerAboutSuccess()
    }

    fun reverseFlagOnNote(position: Int) {
        realm.executeTransaction {
            gardenRealm.listOfNotes[position]?.apply {
                isActive = !isActive
            }
        }
        notifyListenerAboutSuccess()
    }

    fun deleteNoteFromList(position: Long) {
        realm.executeTransaction {
            gardenRealm.listOfNotes.removeAt(position.toInt())
        }
        notifyListenerAboutSuccess()
    }
    fun getListOfNotes(): List<ActiveString> =
        gardenRealm.listOfNotes.asListOfActiveStrings()

    fun getLiveListOfNotes(): MutableLiveData<List<ActiveString>> =
        gardenRealm.listOfNotes.asListOfActiveStrings().asLiveList()

    //Tools
    fun addListOfPickedTools(listOfPickedTools: List<ItemRealm>) {
        realm.executeTransaction {
            gardenRealm.listOfTools.addAll(listOfPickedTools)
        }
        notifyListenerAboutSuccess()
    }

    fun updateNumberOfProperTool(noItems: Int, position: Int) {
        realm.executeTransaction {
            gardenRealm.listOfTools[position]?.numberOfItems = noItems
        }
        notifyListenerAboutSuccess()
    }

/*    fun updateListOfActiveTools(listOfActiveTools: List<Boolean>) {
        realm.executeTransaction {
            for (i in listOfActiveTools.indices) {
                gardenRealm.listOfTools[i]?.isActive = listOfActiveTools[i]
            }
        }
        notifyListenerAboutSuccess()
    }*/

    fun reverseFlagOnTool(position: Int) {
        realm.executeTransaction {
            gardenRealm.listOfTools[position]?.apply {
                isActive = !isActive
            }
        }
        notifyListenerAboutSuccess()
    }

    fun deleteToolFromList(id: Long) {
        realm.executeTransaction {
            gardenRealm.listOfTools.removeAt(id.toInt())
        }
        notifyListenerAboutSuccess()
    }

    fun getListOfTools(): List<Item> =
        gardenRealm.listOfTools.asListOfItems()

    fun getLiveListOfTools(): MutableLiveData<List<Item>> =
        gardenRealm.listOfTools.asListOfItems().asLiveList()


    // Machines
    fun addListOfPickedMachines(listOfPickedMachines: List<ItemRealm>) {
        realm.executeTransaction {
            gardenRealm.listOfMachines.addAll(listOfPickedMachines)
        }
        notifyListenerAboutSuccess()
    }

    fun updateNumberOfProperMachine(noItems: Int, position: Int) {
        realm.executeTransaction {
            gardenRealm.listOfMachines[position]?.numberOfItems = noItems
        }
        notifyListenerAboutSuccess()
    }

    fun reverseFlagOnMachine(position: Int) {
        realm.executeTransaction {
            gardenRealm.listOfMachines[position]?.apply {
                isActive = !isActive
            }
        }
        notifyListenerAboutSuccess()
    }

    fun deleteMachineFromList(position: Long) {
        realm.executeTransaction {
            gardenRealm.listOfMachines.removeAt(position.toInt())
        }
        notifyListenerAboutSuccess()
    }

    fun getListOfMachines(): List<Item> =
        gardenRealm.listOfMachines.asListOfItems()

    fun getLiveListOfMachines(): MutableLiveData<List<Item>> =
        gardenRealm.listOfMachines.asListOfItems().asLiveList()

    // Properties
    fun addListOfPickedProperties(listOfItemRealm: List<ItemRealm>) {
        realm.executeTransaction {
            gardenRealm.listOfProperties.addAll(listOfItemRealm)
        }
        notifyListenerAboutSuccess()
    }

    fun updateNumberOfProperty(noItems: Int, position: Int) {
        realm.executeTransaction {
            gardenRealm.listOfProperties[position]?.numberOfItems = noItems
        }
        notifyListenerAboutSuccess()
    }

    fun reverseFlagOnProperty(position: Int) {
        realm.executeTransaction {
            gardenRealm.listOfProperties[position]?.apply {
                isActive = !isActive
            }
        }
        notifyListenerAboutSuccess()
    }


    fun deletePropertyFromList(position: Long) {
        realm.executeTransaction {
            gardenRealm.listOfProperties.removeAt(position.toInt())
        }
        notifyListenerAboutSuccess()
    }

    fun getListOfProperties(): List<Item> =
        gardenRealm.listOfProperties.asListOfItems()

    fun getLiveListOfProperties(): MutableLiveData<List<Item>> =
        gardenRealm.listOfProperties.asListOfItems().asLiveList()

    // Shopping
    fun addShoppingNoteToList(shoppingNote: String) {
        realm.executeTransaction {
            gardenRealm.listOfShopping.add(ActiveStringRealm(shoppingNote))
        }
        notifyListenerAboutSuccess()
    }

    fun reverseFlagOnShoppingNote(position: Int) {
        realm.executeTransaction {
            gardenRealm.listOfShopping[position]?.apply {
                isActive = !isActive
            }
        }
        notifyListenerAboutSuccess()
    }

    fun deleteShoppingNoteFromList(position: Long) {
        realm.executeTransaction {
            gardenRealm.listOfShopping.removeAt(position.toInt())
        }
        notifyListenerAboutSuccess()
    }

    fun getListOfShopping(): List<ActiveString> =
        gardenRealm.listOfShopping.asListOfActiveStrings()

    fun getLiveListOfShopping(): MutableLiveData<List<ActiveString>> =
        gardenRealm.listOfShopping.asListOfActiveStrings().asLiveList()


    // Man hours
    fun addListOfWorkersFullNames(listOfWorkersFullNames: List<String>) {
        realm.executeTransaction {
            for (name in listOfWorkersFullNames) {

                gardenRealm.mapOfWorkedHours.also { realmList ->
                    val isCurrentlyInList = realmList.find { it.workerFullName == name }

                    if (isCurrentlyInList == null)
                        realmList.add(ManHoursMapRealm(name))
                }
            }

        }
        notifyListenerAboutSuccess()
    }

    fun addListOfWorkedHoursWithPickedDate(listOfWorkedHours: List<Double>, date: Date) {
        realm.executeTransaction {
            for (index in listOfWorkedHours.indices) {
                if (listOfWorkedHours[index] != 0.0) {
                    gardenRealm.mapOfWorkedHours[index]!!.listOfManHours.add(
                        ManHoursRealm(
                            date,
                            listOfWorkedHours[index]
                        )
                    )
                }
            }
        }
        notifyListenerAboutSuccess()
    }

    fun getMapOfManHours(): List<ManHoursMap> =
        gardenRealm.mapOfWorkedHours.asListOfManHoursMap()

    fun getLiveMapOfManHours(): MutableLiveData<List<ManHoursMap>> =
        gardenRealm.mapOfWorkedHours.asListOfManHoursMap().asLiveList()

    // Photos
    fun addPictureToList(path: String) {
        realm.executeTransaction {
            gardenRealm.listOfPicturesPaths.add(path)
        }
        notifyListenerAboutSuccess()
    }

    fun getListOfPicturesPaths(): List<String> =
        gardenRealm.listOfPicturesPaths.toList()

    fun getLiveListOfPicturesPaths(): MutableLiveData<List<String>> =
        gardenRealm.listOfPicturesPaths.toList().asLiveList()

    // End components methods
    fun closeRealm() {
        realm.close()
    }

    init {
        val realmConfig = RealmConfiguration.Builder()
            .name(REALM_GARDEN_NAME)
            .modules(GardenModule())
            .build()

        realm = Realm.getInstance(realmConfig)
        gardenRealm = realm.where<GardenRealm>().equalTo(ID, gardenID).findFirst()
            ?: throw NoSuchElementException("Garden with this Id not found!")
    }

    companion object {
        private const val REALM_GARDEN_NAME = "garden.realm"
        private const val ID = "id"
    }
}