package pl.podwikagrzegorz.gardener.ui.planned_gardens.garden_to_add

import android.app.Activity.RESULT_OK
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.util.Pair
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.pojo.Period
import pl.podwikagrzegorz.gardener.databinding.FragmentAddGardenBinding
import pl.podwikagrzegorz.gardener.extensions.deleteCaptionedImage
import pl.podwikagrzegorz.gardener.extensions.isDefault
import pl.podwikagrzegorz.gardener.maps.MapsActivity
import java.io.File
import java.time.Instant
import java.util.*

class AddGardenFragment : Fragment() {

    private lateinit var gardenBinding: FragmentAddGardenBinding

    private var absoluteSnapshotPath: String? = null
    private var snapshotLatitude: Double? = null
    private var snapshotLongitude: Double? = null
    private val period = Period()
    private var isAddedGarden = false
    private val datePickerBuilder = MaterialDatePicker.Builder.dateRangePicker()
    private lateinit var materialDatePicker : MaterialDatePicker<Pair<Long, Long>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gardenBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_garden, container, false)
        materialDatePicker = datePickerBuilder.build()
        return gardenBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setOnPhoneNumberTextWatcher()
        setUpDatePicker()
        setOnPickDatesButtonListener()
        setLocationButtonListener()
        setGardenOrServiceSwitchListener()
        setConfirmAddingGardenButtonListener()
    }

    private fun setOnPhoneNumberTextWatcher() {
        gardenBinding.textInputEditTextPhoneNumber.addTextChangedListener {
            if (it?.length != gardenBinding.textInputLayoutPhoneNumber.counterMaxLength){
                gardenBinding.textInputLayoutPhoneNumber.error = getString(R.string.error)
            } else {
                gardenBinding.textInputLayoutPhoneNumber.error = null
            }
        }
    }

    private fun setUpDatePicker() {
        materialDatePicker.addOnPositiveButtonClickListener {
            val startDayInMilliseconds = it.first
            val endDayInMilliseconds = it.second

            if (startDayInMilliseconds != null && endDayInMilliseconds != null){
                loadDatesIntoPeriodVariable(startDayInMilliseconds, endDayInMilliseconds)
            }
        }
    }

    private fun loadDatesIntoPeriodVariable(startDayInMilliseconds: Long, endDayInMilliseconds: Long) {
        val calendar = Calendar.getInstance()

        calendar.timeInMillis =  startDayInMilliseconds
        period.startDay = calendar.get(Calendar.DAY_OF_MONTH)
        period.startMonth = calendar.get(Calendar.MONTH) + 1    // Calendar receives month from range 0-11
        period.startYear = calendar.get(Calendar.YEAR)

        calendar.timeInMillis = endDayInMilliseconds
        period.endDay = calendar.get(Calendar.DAY_OF_MONTH)
        period.endMonth = calendar.get(Calendar.MONTH) + 1
        period.endYear = calendar.get(Calendar.YEAR)

        updateDateTextView()
    }

    private fun updateDateTextView() {
        gardenBinding.materialTextViewPickedPeriod.text = period.getPeriodAsString()
    }

    private fun setOnPickDatesButtonListener() {
        gardenBinding.materialButtonPickGardenDateRange.setOnClickListener {
            materialDatePicker.show(childFragmentManager, null)
        }

    }

    private fun setLocationButtonListener() {
        gardenBinding.materialButtonLocateGarden.setOnClickListener {
            val intent = Intent(context, MapsActivity::class.java)
            startActivityForResult(intent, REQUEST_TAKEN_SNAPSHOT_OK)
        }
    }

    private fun setGardenOrServiceSwitchListener() {
        gardenBinding.switchMaterialGardenOrService.setOnCheckedChangeListener { button, isChecked ->
            when (isChecked) {
                true -> {
                    gardenBinding.shapeableImageViewGardenOrService.setImageResource(R.drawable.ic_farm)
                    button.setText(R.string.garden)
                }
                false -> {
                    gardenBinding.shapeableImageViewGardenOrService.setImageResource(R.drawable.ic_lawn_mower)
                    button.setText(R.string.service)
                }
            }
        }
    }

    private fun setConfirmAddingGardenButtonListener() {
        gardenBinding.fabConfirmAddingGarden.setOnClickListener {
            if (checkViewsAreFilledByUser()) {
                prepareActionAndNavigateToPreviousFragment()
            }
        }
    }

    private fun checkViewsAreFilledByUser(): Boolean {

        if (gardenBinding.textInputEditTextGardenTitle.text.toString().isEmpty()) {
            val toastMessage = getString(R.string.empty_garden_title)
            Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
            return false
        }
        if (gardenBinding.textInputEditTextPhoneNumber.text.toString().isEmpty()
            || gardenBinding.textInputEditTextPhoneNumber.text.toString().length != 9
        ) {
            val toastMessage = getString(R.string.empty_phone_number)
            Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
            return false
        }
        if (period.isDefault()) {
            val toastMessage = getString(R.string.default_period)
            Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
            return false
        }
        if (absoluteSnapshotPath.isNullOrEmpty()) {
            val toastMessage = getString(R.string.none_taken_snapshot)
            Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun prepareActionAndNavigateToPreviousFragment() {
        isAddedGarden = true

        val navController = findNavController()
        val actionAddGardenToPlannedGardens = prepareAction()
        navController.navigate(actionAddGardenToPlannedGardens)
    }

    private fun prepareAction(): NavDirections =
        AddGardenFragmentDirections.actionAddGardenToPlannedGardens(
            gardenBinding.textInputEditTextGardenTitle.text.toString(),
            gardenBinding.textInputEditTextPhoneNumber.text.toString().toInt(),
            period,
            gardenBinding.switchMaterialGardenOrService.isChecked,
            absoluteSnapshotPath.toString(),
            snapshotLatitude?.toFloat() ?: 0.0F,
            snapshotLongitude?.toFloat() ?: 0.0F
        )


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_TAKEN_SNAPSHOT_OK && resultCode == RESULT_OK) {
            if (data != null) {
                loadVariablesFromReceivedIntent(data)
            }
        }
    }

    private fun loadVariablesFromReceivedIntent(receivedData: Intent) {
        snapshotLatitude = receivedData.getDoubleExtra(
            MapsActivity.CAPTIONED_SNAPSHOT_LATITUDE,
            MapsActivity.defaultCoordinates.latitude
        )
        snapshotLongitude = receivedData.getDoubleExtra(
            MapsActivity.CAPTIONED_SNAPSHOT_LONGITUDE,
            MapsActivity.defaultCoordinates.longitude
        )
        val snapshotName = receivedData.getStringExtra(MapsActivity.CAPTIONED_SNAPSHOT_PATH)

        setTakenSnapshotIntoImageView(snapshotName)
    }

    private fun setTakenSnapshotIntoImageView(snapshotName: String?) {
        snapshotName?.let {
            val file: File = ContextWrapper(context).getFileStreamPath(snapshotName)
            absoluteSnapshotPath = file.absolutePath

            gardenBinding.shapeableImageViewPickedLocalization.setImageDrawable(
                Drawable.createFromPath(
                    absoluteSnapshotPath
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        if (!isAddedGarden)
            absoluteSnapshotPath?.deleteCaptionedImage()
    }

    init {
        datePickerBuilder.setTitleText("SELECT A DATE")
    }

    companion object {
        const val REQUEST_TAKEN_SNAPSHOT_OK = 1

        const val TAG_START_DATE_PICKER = "TAG_START_DATE_PICKER"
        const val TAG_END_DATE_PICKER = "TAG_END_DATE_PICKER"
    }
}

/*    override fun onDateSelected(
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

    }*/