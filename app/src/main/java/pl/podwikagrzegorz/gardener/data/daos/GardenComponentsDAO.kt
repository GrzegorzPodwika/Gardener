package pl.podwikagrzegorz.gardener.data.daos

import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import io.realm.kotlin.where
import pl.podwikagrzegorz.gardener.data.realm.*
import java.util.*
import kotlin.NoSuchElementException

class GardenComponentsDAO(gardenID: Long) {
    private val realm: Realm
    private val gardenRealm: GardenRealm

    // Basic Garden
    fun getBasicGarden(): BasicGardenRealm =
        gardenRealm.basicGarden!!

    // Description
    fun addDescriptionToList(description: String) {
        realm.executeTransaction {
            gardenRealm.listOfDescriptions.add(ActiveStringRealm(description))
        }
    }

    fun reverseFlagOnDescription(position: Int) {
        realm.executeTransaction {
            gardenRealm.listOfDescriptions[position]?.apply {
                isActive = !isActive
            }
        }
    }

    fun deleteDescriptionFromList(position: Long) {
        realm.executeTransaction {
            gardenRealm.listOfDescriptions.removeAt(position.toInt())
        }
    }

    fun getLiveListOfDescriptions(): MutableLiveData<RealmList<ActiveStringRealm>> =
        gardenRealm.listOfDescriptions.asLiveList()

    //Notes
    fun addNoteToList(note: String) {
        realm.executeTransaction {
            gardenRealm.listOfNotes.add(ActiveStringRealm(note))
        }
    }

    fun reverseFlagOnNote(position: Int) {
        realm.executeTransaction {
            gardenRealm.listOfNotes[position]?.apply {
                isActive = !isActive
            }
        }
    }

    fun deleteNoteFromList(position: Long) {
        realm.executeTransaction {
            gardenRealm.listOfNotes.removeAt(position.toInt())
        }
    }

    fun getLiveListOfNotes(): MutableLiveData<RealmList<ActiveStringRealm>> =
        gardenRealm.listOfNotes.asLiveList()

    //Tools
    fun addListOfPickedTools(listOfPickedTools: List<ItemRealm>) {
        realm.executeTransaction {
            gardenRealm.listOfTools.addAll(listOfPickedTools)
        }
    }

    fun updateNumberOfProperTool(noItems: Int, position: Int) {
        realm.executeTransaction {
            gardenRealm.listOfTools[position]?.numberOfItems = noItems
        }
    }

    fun updateListOfActiveTools(listOfActiveTools: List<Boolean>) {
        realm.executeTransaction {
            for (i in listOfActiveTools.indices) {
                gardenRealm.listOfTools[i]?.isActive = listOfActiveTools[i]
            }
        }
    }

    fun reverseFlagOnTool(position: Int) {
        realm.executeTransaction {
            gardenRealm.listOfTools[position]?.apply {
                isActive = !isActive
            }
        }
    }

    fun getLiveListOfTools(): MutableLiveData<RealmList<ItemRealm>> =
        gardenRealm.listOfTools.asLiveList()

    fun deleteToolFromList(id: Long) {
        realm.executeTransaction {
            gardenRealm.listOfTools.removeAt(id.toInt())
        }
    }

    // Machines
    fun addListOfPickedMachines(listOfPickedMachines: List<ItemRealm>) {
        realm.executeTransaction {
            gardenRealm.listOfMachines.addAll(listOfPickedMachines)
        }
    }

    fun updateNumberOfProperMachine(noItems: Int, position: Int) {
        realm.executeTransaction {
            gardenRealm.listOfMachines[position]?.numberOfItems = noItems
        }
    }

    fun reverseFlagOnMachine(position: Int) {
        realm.executeTransaction {
            gardenRealm.listOfMachines[position]?.apply {
                isActive = !isActive
            }
        }
    }

    fun getLiveListOfMachines(): MutableLiveData<RealmList<ItemRealm>> =
        gardenRealm.listOfMachines.asLiveList()

    fun deleteMachineFromList(position: Long) {
        realm.executeTransaction {
            gardenRealm.listOfMachines.removeAt(position.toInt())
        }
    }

    // Properties
    fun addListOfPickedProperties(listOfItemRealm: List<ItemRealm>) {
        realm.executeTransaction {
            gardenRealm.listOfProperties.addAll(listOfItemRealm)
        }
    }

    fun updateNumberOfProperty(noItems: Int, position: Int) {
        realm.executeTransaction {
            gardenRealm.listOfProperties[position]?.numberOfItems = noItems
        }
    }

    fun reverseFlagOnProperty(position: Int) {
        realm.executeTransaction {
            gardenRealm.listOfProperties[position]?.apply {
                isActive = !isActive
            }
        }
    }


    fun getLiveListOfProperties(): MutableLiveData<RealmList<ItemRealm>> =
        gardenRealm.listOfProperties.asLiveList()

    fun deletePropertyFromList(position: Long) {
        realm.executeTransaction {
            gardenRealm.listOfProperties.removeAt(position.toInt())
        }
    }

    // Shopping
    fun addShoppingNoteToList(shoppingNote: String) {
        realm.executeTransaction {
            gardenRealm.listOfShopping.add(ActiveStringRealm(shoppingNote))
        }
    }

    fun reverseFlagOnShoppingNote(position: Int) {
        realm.executeTransaction {
            gardenRealm.listOfShopping[position]?.apply {
                isActive = !isActive
            }
        }
    }

    fun deleteShoppingNoteFromList(position: Long) {
        realm.executeTransaction {
            gardenRealm.listOfShopping.removeAt(position.toInt())
        }
    }

    fun getLiveListOfShopping(): MutableLiveData<RealmList<ActiveStringRealm>> =
        gardenRealm.listOfShopping.asLiveList()


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
    }

    fun getLiveMapOfManHours(): MutableLiveData<RealmList<ManHoursMapRealm>> =
        gardenRealm.mapOfWorkedHours.asLiveList()

    // Photos
    fun addPictureToList(path: String) {
        realm.executeTransaction {
            gardenRealm.listOfPicturesPaths.add(path)
        }
    }

    fun getLiveListOfPicturesPaths(): MutableLiveData<RealmList<String>> =
        gardenRealm.listOfPicturesPaths.asLiveList()

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