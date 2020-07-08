package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.ClassCastException

class GardenViewModelFactory(private val gardenID: Long) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(BasicGardenViewModel::class.java) ->
                BasicGardenViewModel(
                    gardenID
                ) as T

            modelClass.isAssignableFrom(DescriptionViewModel::class.java) ->
                DescriptionViewModel(
                    gardenID
                ) as T

            modelClass.isAssignableFrom(NoteViewModel::class.java) ->
                NoteViewModel(
                    gardenID
                ) as T

            modelClass.isAssignableFrom(ToolViewModel::class.java) ->
                ToolViewModel(
                    gardenID
                ) as T

            modelClass.isAssignableFrom(MachineViewModel::class.java) ->
                MachineViewModel(
                    gardenID
                ) as T

            modelClass.isAssignableFrom(PropertyViewModel::class.java) ->
                PropertyViewModel(
                    gardenID
                ) as T

            modelClass.isAssignableFrom(ShoppingViewModel::class.java) ->
                ShoppingViewModel(
                    gardenID
                ) as T

            modelClass.isAssignableFrom(PicturesPathsViewModel::class.java) ->
                PicturesPathsViewModel(
                    gardenID
                ) as T

            modelClass.isAssignableFrom(ManHoursViewModel::class.java) ->
                ManHoursViewModel(
                    gardenID
                ) as T

            modelClass.isAssignableFrom(PhotosViewModel::class.java) ->
                PhotosViewModel(
                    gardenID
                ) as T

            else -> throw ClassCastException("ViewModel class not found.")

        }

    }
}