package pl.podwikagrzegorz.gardener.data.daos

import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import io.realm.kotlin.where
import pl.podwikagrzegorz.gardener.data.realm.GardenModule
import pl.podwikagrzegorz.gardener.data.realm.GardenRealm
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

    fun deleteDescriptionFromList(id: Long) {
        realm.executeTransaction {
            gardenRealm.listOfDescriptions.removeAt(id.toInt())
        }
    }

    fun getLiveListOfDescriptions(): MutableLiveData<RealmList<String>> =
        gardenRealm.listOfDescriptions.asLiveList()


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