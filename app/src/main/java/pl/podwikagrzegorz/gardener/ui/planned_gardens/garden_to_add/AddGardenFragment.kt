package pl.podwikagrzegorz.gardener.ui.planned_gardens.garden_to_add

import android.app.Activity.RESULT_OK
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.pojo.Period
import pl.podwikagrzegorz.gardener.databinding.FragmentAddGardenBinding
import pl.podwikagrzegorz.gardener.extensions.deleteCaptionedImage
import pl.podwikagrzegorz.gardener.maps.MapsActivity
import pl.podwikagrzegorz.gardener.ui.planned_gardens.PlannedGardensFragmentDirections
import java.io.File

//TODO poruszanie sie miedzy fragmentami
class AddGardenFragment : Fragment(), DatePickerFragment.OnDateSelectedListener {

    private lateinit var gardenBinding: FragmentAddGardenBinding
    private var snapshotPath: String? = null
    private var snapshotLatitude: Double? = null
    private var snapshotLongitude: Double? = null
    private val period = Period()
    private var isAddedGarden = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gardenBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_garden, container, false)
        return gardenBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //dates
        gardenBinding.materialButtonStartGardenDate.setOnClickListener {
            val datePickerFragment = DatePickerFragment(true, this)
            datePickerFragment.show(childFragmentManager, TAG_START_DATE_PICKER)
        }

        gardenBinding.materialButtonEndGardenDate.setOnClickListener {
            val datePickerFragment = DatePickerFragment(false, this)
            datePickerFragment.show(childFragmentManager, TAG_END_DATE_PICKER)
        }

        //localization
        gardenBinding.materialButtonLocateGarden.setOnClickListener {
            val intent = Intent(context, MapsActivity::class.java)
            startActivityForResult(intent, REQUEST_TAKEN_SNAPSHOT_OK)
        }

        gardenBinding.switchMaterialGardenOrService.setOnCheckedChangeListener { button, isChecked ->
            when (isChecked){
                true -> {
                    gardenBinding.shapeableImageViewGardenOrService.setImageResource(R.drawable.ic_farm)
                    button.setText(R.string.garden)
                }
                false ->{
                    gardenBinding.shapeableImageViewGardenOrService.setImageResource(R.drawable.ic_lawn_mower)
                    button.setText(R.string.service)
                }
            }
        }

        //adding Bundles and confirm adding new Garden
        gardenBinding.fabConfirmAddingGarden.setOnClickListener {
            isAddedGarden = true
            val navController = findNavController()
            val action = prepareAction()
            navController.navigate(action)
        }
    }

    private fun prepareAction(): NavDirections =
        AddGardenFragmentDirections.actionAddGardenToPlannedGardens(
            gardenBinding.textInputEditTextGardenTitle.text.toString(),
            gardenBinding.textInputEditTextPhoneNumber.text.toString().toInt(),
            period,
            gardenBinding.switchMaterialGardenOrService.isChecked,
            snapshotPath.toString(),
            snapshotLatitude?.toFloat() ?: 0.0F,
            snapshotLongitude?.toFloat() ?: 0.0F
        )


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //TODO zajac sie resezta pobrania snapshota
        if (requestCode == REQUEST_TAKEN_SNAPSHOT_OK) {
            if (resultCode == RESULT_OK) {
                val tmpSnapshotPath = data?.getStringExtra(MapsActivity.CAPTIONED_SNAPSHOT_PATH)
                snapshotLatitude = data?.getDoubleExtra(
                    MapsActivity.CAPTIONED_SNAPSHOT_LATITUDE,
                    MapsActivity.defaultCoordinates.latitude
                )
                snapshotLongitude = data?.getDoubleExtra(
                    MapsActivity.CAPTIONED_SNAPSHOT_LONGITUDE,
                    MapsActivity.defaultCoordinates.longitude
                )

                val file: File = ContextWrapper(context).getFileStreamPath(tmpSnapshotPath)
                snapshotPath = file.absolutePath

                gardenBinding.shapeableImageViewPickedLocalization.setImageDrawable(
                    Drawable.createFromPath(
                        snapshotPath
                    )
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (!isAddedGarden)
            snapshotPath?.deleteCaptionedImage()
    }

    override fun onDateSelected(
        year: Int,
        month: Int,
        dayOfMonth: Int,
        isRequireStartDate: Boolean
    ) {
        if (isRequireStartDate) {
            period.startDay = dayOfMonth
            period.startMonth = month + 1
            period.startYear = year
        } else {
            period.endDay = dayOfMonth
            period.endMonth = month + 1
            period.endYear = year
        }

        updateDateTextView()
    }

    private fun updateDateTextView() {
        val currentPeriod = String.format(
            "%02d.%02d.%s  -  %02d.%02d.%s",
            period.startDay,
            period.startMonth,
            period.startYear,
            period.endDay,
            period.endMonth,
            period.endYear
        )

        gardenBinding.materialTextViewPickedPeriod.text = currentPeriod
    }

    companion object {
        const val REQUEST_TAKEN_SNAPSHOT_OK = 1

        const val BUNDLE_GARDEN_TITLE = "BUNDLE_GARDEN_TITLE"
        const val BUNDLE_PHONE_NUMBER = "BUNDLE_PHONE_NUMBER"
        const val BUNDLE_PERIOD = "BUNDLE_PERIOD"
        const val BUNDLE_IS_GARDEN = "BUNDLE_IS_GARDEN"
        const val BUNDLE_SNAPSHOT_PATH = "REQUEST_SNAPSHOT_PATH"
        const val BUNDLE_LATITUDE = "BUNDLE_LATITUDE"
        const val BUNDLE_LONGITUDE = "BUNDLE_LONGITUDE"

        const val TAG_START_DATE_PICKER = "TAG_START_DATE_PICKER"
        const val TAG_END_DATE_PICKER = "TAG_END_DATE_PICKER"
    }
}
