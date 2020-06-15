package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.realm.RealmList
import pl.podwikagrzegorz.gardener.data.realm.asLiveList

class PicturesPathsViewModel(gardenID: Long) : AbstractGardenViewModel(gardenID) {
    private val listOfPicturesPaths: MutableLiveData<RealmList<String>>? =
        gardenRealm?.listOfPicturesPaths?.asLiveList()

    override fun getItemsList(): LiveData<RealmList<String>>? =
        listOfPicturesPaths


    override fun addItemToList(item: String) {
        realm.executeTransaction {
            gardenRealm?.listOfPicturesPaths?.add(item)
            refreshLiveDataList()
        }
    }

    override fun deleteItemFromList(id: Long?) {
        if (id != null) {
            realm.executeTransaction {
                gardenRealm?.listOfPicturesPaths?.removeAt(id.toInt())
                refreshLiveDataList()
            }
        }
    }

    private fun refreshLiveDataList() {
        listOfPicturesPaths?.postValue(listOfPicturesPaths.value)
    }

    companion object {
        private const val GARDEN_ID = "GARDEN_ID"

        fun toBundle(gardenID: Long): Bundle {
            val bundle = Bundle(1)
            bundle.putLong(GARDEN_ID, gardenID)
            return bundle
        }

        fun fromBundle(bundle: Bundle): Long = bundle.getLong(GARDEN_ID)
    }
}