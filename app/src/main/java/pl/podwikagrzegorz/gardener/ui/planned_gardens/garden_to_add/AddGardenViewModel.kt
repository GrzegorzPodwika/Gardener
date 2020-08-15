package pl.podwikagrzegorz.gardener.ui.planned_gardens.garden_to_add

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import pl.podwikagrzegorz.gardener.BR
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.domain.BasicGarden
import pl.podwikagrzegorz.gardener.extensions.isDefault
import pl.podwikagrzegorz.gardener.extensions.loadRangeIntoPeriod

class AddGardenViewModel : ObservableViewModel() {

    private val basicGarden = BasicGarden()

    @Bindable
    var gardenTitle: String? = null
        set(value) {
            if (field != value) {
                field = value
                basicGarden.gardenTitle = value ?: ""
                notifyPropertyChanged(BR.gardenTitle)
            }
        }

    @Bindable
    var phoneNumberAsString: String? = null
        set(value) {
            if (field != value) {
                field = value
                value?.let { basicGarden.phoneNumber = it.toInt() }
                notifyPropertyChanged(BR.phoneNumberAsString)
            }
        }

    @get:Bindable
    var periodAsString: String = basicGarden.period.periodAsString
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.periodAsString)
            }
        }


    @Bindable
    var isGardenOrService: Boolean = true
        set(value) {
            if (field != value) {
                field = value
                basicGarden.isGarden = value
                notifyPropertyChanged(BR.gardenOrServiceAsString)
                notifyPropertyChanged(BR.gardenOrServiceAsDrawable)
            }
        }

    @Bindable
    fun getGardenOrServiceAsString(): String {
        return when (isGardenOrService) {
            true -> GardenerApp.res.getString(R.string.garden)
            false -> GardenerApp.res.getString(R.string.service)
        }
    }

    @Bindable
    fun getGardenOrServiceAsDrawable(): Drawable {
        return when (isGardenOrService) {
            true -> GardenerApp.res.getDrawable(R.drawable.ic_farm, null)
            false -> GardenerApp.res.getDrawable(R.drawable.ic_lawn_mower, null)
        }
    }

    @get:Bindable
    var uniqueSnapshotName: String = ""
        set(value) {
            if (field != value) {
                field = value
                basicGarden.uniqueSnapshotName = value
                notifyPropertyChanged(BR.uniqueSnapshotName)
            }
        }

    fun setLatitude(latitude: Double) {
        basicGarden.latitude = latitude
    }

    fun setLongitude(longitude: Double) {
        basicGarden.longitude = longitude
    }

    fun setTakenSnapshotName(takenSnapshotName: String) {
        uniqueSnapshotName = takenSnapshotName
    }

    fun setPeriod(startDayInMilliseconds: Long, endDayInMilliseconds: Long) {
        basicGarden.period.loadRangeIntoPeriod(
            startDayInMilliseconds,
            endDayInMilliseconds
        )

        periodAsString = basicGarden.period.periodAsString
    }


    fun isGivenGardenTitleEmpty(): Boolean {
        return gardenTitle.isNullOrEmpty()
    }

    fun isGivenPhoneNumberIncorrect(): Boolean {
        return phoneNumberAsString.isNullOrEmpty() || phoneNumberAsString?.length != 9
    }

    fun isPeriodDefault(): Boolean {
        return basicGarden.period.isDefault()
    }

    fun isNoneTakenSnapshot(): Boolean {
        return uniqueSnapshotName.isEmpty()
    }

    fun prepareBundle(): Bundle =
        bundleOf(
            AddGardenFragment.REQUEST_GARDEN_TITLE to basicGarden.gardenTitle,
            AddGardenFragment.REQUEST_PHONE_NUMBER to phoneNumberAsString!!.toInt(),
            AddGardenFragment.REQUEST_PERIOD to basicGarden.period,
            AddGardenFragment.REQUEST_IS_GARDEN to basicGarden.isGarden,
            AddGardenFragment.REQUEST_UNIQUE_SNAPSHOT_NAME to basicGarden.uniqueSnapshotName,
            AddGardenFragment.REQUEST_LATITUDE to basicGarden.latitude,
            AddGardenFragment.REQUEST_LONGITUDE to basicGarden.longitude
        )

}