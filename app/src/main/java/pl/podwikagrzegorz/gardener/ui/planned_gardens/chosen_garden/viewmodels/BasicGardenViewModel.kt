package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import android.os.Bundle
import pl.podwikagrzegorz.gardener.data.realm.BasicGardenRealm

class BasicGardenViewModel(gardenID: Long) : AbstractGardenViewModel(gardenID) {
    fun getBasicGarden(): BasicGardenRealm? =
        gardenRealm?.basicGarden

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