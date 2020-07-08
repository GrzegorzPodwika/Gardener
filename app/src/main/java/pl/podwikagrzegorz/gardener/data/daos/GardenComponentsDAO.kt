package pl.podwikagrzegorz.gardener.data.daos

import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import io.realm.kotlin.where
import pl.podwikagrzegorz.gardener.data.realm.GardenModule
import pl.podwikagrzegorz.gardener.data.realm.GardenRealm
import pl.podwikagrzegorz.gardener.data.realm.ItemRealm
import pl.podwikagrzegorz.gardener.data.realm.asLiveList

class GardenComponentsDAO(private val gardenID: Long) {
    private val realm: Realm
    private val gardenRealm: GardenRealm

    // Description
    fun addDescriptionToList(description: String) {
        realm.executeTransaction {
            gardenRealm.listOfDescriptions.add(description)
        }
    }

    fun deleteDescriptionFromList(position: Long) {
        realm.executeTransaction {
            gardenRealm.listOfDescriptions.removeAt(position.toInt())
        }
    }

    fun getLiveListOfDescriptions(): MutableLiveData<RealmList<String>> =
        gardenRealm.listOfDescriptions.asLiveList()


    //Notes
    fun addNoteToList(note: String) {
        realm.executeTransaction {
            gardenRealm.listOfNotes.add(note)
        }
    }

    fun deleteNoteFromList(position: Long) {
        realm.executeTransaction {
            gardenRealm.listOfNotes.removeAt(position.toInt())
        }
    }

    fun getLiveListOfNotes(): MutableLiveData<RealmList<String>> =
        gardenRealm.listOfNotes.asLiveList()

    //Tools
    fun addListOfPickedTools(listOfPickedTools: List<ItemRealm>){
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
            for (i in listOfActiveTools.indices){
                gardenRealm.listOfTools[i]?.isActive = listOfActiveTools[i]
            }
        }
    }

    fun changeFlagToOpposite(position: Int) {
        realm.executeTransaction {
            gardenRealm.listOfTools[position]?.apply {
                isActive = !isActive
            }
        }
    }


    fun getLiveListOfTools(): MutableLiveData<RealmList<ItemRealm>> =
        gardenRealm.listOfTools.asLiveList()

    fun deleteToolFromList(id: Long){
        realm.executeTransaction {
            gardenRealm.listOfTools.removeAt(id.toInt())
        }
    }



    //Shopping
    fun addShoppingNoteToList(shoppingNote: String) {
        realm.executeTransaction {
            gardenRealm.listOfShopping.add(shoppingNote)
        }
    }

    fun deleteShoppingNoteFromList(position: Long) {
        realm.executeTransaction {
            gardenRealm.listOfShopping.removeAt(position.toInt())
        }
    }

    fun getLiveListOfShopping() : MutableLiveData<RealmList<String>> =
        gardenRealm.listOfShopping.asLiveList()




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