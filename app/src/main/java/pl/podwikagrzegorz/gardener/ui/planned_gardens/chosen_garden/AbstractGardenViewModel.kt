package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import io.realm.kotlin.where
import pl.podwikagrzegorz.gardener.data.realm.GardenModule
import pl.podwikagrzegorz.gardener.data.realm.GardenRealm

abstract class AbstractGardenViewModel(private val gardenID: Long) : ViewModel() {
    protected val realm: Realm
    protected val gardenRealm: GardenRealm?

    open fun getItemsList(): LiveData<RealmList<String>>? = null
    open fun addItemToList(item: String) {}
    open fun deleteItemFromList(id: Long?) {}

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }

    init {
        val realmConfig = RealmConfiguration.Builder()
            .name(REALM_GARDEN_NAME)
            .modules(GardenModule())
            .build()

        realm = Realm.getInstance(realmConfig)
        gardenRealm = realm.where<GardenRealm>().equalTo(ID, gardenID).findFirst()
    }

    companion object {
        private const val REALM_GARDEN_NAME = "garden.realm"
        private const val ID = "id"
    }
}

