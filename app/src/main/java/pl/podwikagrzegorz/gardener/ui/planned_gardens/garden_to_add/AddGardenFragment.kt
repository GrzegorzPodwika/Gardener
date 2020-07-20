package pl.podwikagrzegorz.gardener.ui.planned_gardens.garden_to_add

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.util.Pair
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import coil.api.load
import com.google.android.material.datepicker.MaterialDatePicker
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.pojo.Period
import pl.podwikagrzegorz.gardener.databinding.FragmentAddGardenBinding
import pl.podwikagrzegorz.gardener.extensions.deleteCaptionedImage
import pl.podwikagrzegorz.gardener.extensions.getAbsoluteFilePath
import pl.podwikagrzegorz.gardener.extensions.getNavigationResult
import pl.podwikagrzegorz.gardener.extensions.isDefault
import java.io.File
import java.util.*


class AddGardenFragment : Fragment() {

    private lateinit var gardenBinding: FragmentAddGardenBinding
    private val datePickerBuilder = MaterialDatePicker.Builder.dateRangePicker()
    private lateinit var materialDatePicker: MaterialDatePicker<Pair<Long, Long>>

    private var snapshotUniqueName: String = ""
    private var snapshotLatitude: Double = 0.0
    private var snapshotLongitude: Double = 0.0
    private val period = Period()
    private var isAddedGarden = false
    private var isTakenSnapshot = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gardenBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_garden, container, false)
        presetDatePicker()
        return gardenBinding.root
    }

    private fun presetDatePicker() {
        datePickerBuilder.setTitleText("SELECT A DATE")
        materialDatePicker = datePickerBuilder.build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeArgsFromMapFragment()
        setOnPhoneNumberTextWatcher()
        setOnDatePickerListener()
        setOnPickDatesButtonListener()
        setLocationButtonListener()
        setGardenOrServiceSwitchListener()
        setConfirmAddingGardenButtonListener()
    }

    private fun observeArgsFromMapFragment() {
        getNavigationResult<Double>(KEY_LATITUDE)?.observe(viewLifecycleOwner) { latitude ->
            snapshotLatitude = latitude
        }
        getNavigationResult<Double>(KEY_LONGITUDE)?.observe(viewLifecycleOwner) { longitude ->
            snapshotLongitude = longitude
        }
        getNavigationResult<String>(KEY_TAKEN_SNAPSHOT_NAME)?.observe(viewLifecycleOwner) { takenSnapshotName ->
            isTakenSnapshot = true
            snapshotUniqueName = takenSnapshotName
            setTakenSnapshotIntoImageView()
        }
    }

    private fun setTakenSnapshotIntoImageView() {
        val absolutePath = requireContext().getAbsoluteFilePath(snapshotUniqueName)

        gardenBinding.shapeableImageViewPickedLocalization.load(File(absolutePath))
    }

    private fun setOnPhoneNumberTextWatcher() {
        gardenBinding.textInputEditTextPhoneNumber.addTextChangedListener {
            if (it?.length != gardenBinding.textInputLayoutPhoneNumber.counterMaxLength) {
                gardenBinding.textInputLayoutPhoneNumber.error = getString(R.string.error)
            } else {
                gardenBinding.textInputLayoutPhoneNumber.error = null
            }
        }
    }

    private fun setOnDatePickerListener() {
        materialDatePicker.addOnPositiveButtonClickListener {
            val startDayInMilliseconds = it.first
            val endDayInMilliseconds = it.second

            if (startDayInMilliseconds != null && endDayInMilliseconds != null) {
                loadDatesIntoPeriodVariable(startDayInMilliseconds, endDayInMilliseconds)
            }
        }
    }

    private fun loadDatesIntoPeriodVariable(
        startDayInMilliseconds: Long,
        endDayInMilliseconds: Long
    ) {
        val calendar = Calendar.getInstance()

        calendar.timeInMillis = startDayInMilliseconds
        period.startDay = calendar.get(Calendar.DAY_OF_MONTH)
        period.startMonth =
            calendar.get(Calendar.MONTH) + 1    // Calendar receives month from range 0-11
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
            val navController = findNavController()

            val action = AddGardenFragmentDirections.actionNavAddGardenToNavMapFragment()
            navController.navigate(action)
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
        gardenBinding.materialButtonConfirmAddingGarden.setOnClickListener {
            if (checkViewsAreFilledByUser()) {
                setBundleAndNavigateToPreviousFragment()
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
        if (snapshotUniqueName.isEmpty()) {
            val toastMessage = getString(R.string.none_taken_snapshot)
            Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun setBundleAndNavigateToPreviousFragment() {
        isAddedGarden = true

        val bundle = prepareBundle()
        setFragmentResult(KEY_DATA_FROM_ADD_GARDEN_FRAGMENT, bundle)
        findNavController().navigateUp()
    }

    private fun prepareBundle(): Bundle {
        val bundle = Bundle()
        bundle.putString(
            REQUEST_GARDEN_TITLE,
            gardenBinding.textInputEditTextGardenTitle.text.toString()
        )
        bundle.putInt(
            REQUEST_PHONE_NUMBER,
            gardenBinding.textInputEditTextPhoneNumber.text.toString().toInt()
        )
        bundle.putSerializable(REQUEST_PERIOD, period)
        bundle.putBoolean(REQUEST_IS_GARDEN, gardenBinding.switchMaterialGardenOrService.isChecked)
        bundle.putString(REQUEST_UNIQUE_SNAPSHOT_NAME, snapshotUniqueName)
        bundle.putDouble(REQUEST_LATITUDE, snapshotLatitude)
        bundle.putDouble(REQUEST_LONGITUDE, snapshotLongitude)
        return bundle
    }


    override fun onDestroyView() {
        super.onDestroyView()

        if (!isAddedGarden && isTakenSnapshot) {
            requireContext().getAbsoluteFilePath(snapshotUniqueName).deleteCaptionedImage()
            Toast.makeText(
                requireContext(),
                "Image has been deleted! $isAddedGarden $isTakenSnapshot",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        const val TAG = "IMAGE"

        const val KEY_LATITUDE = "KEY_LATITUDE"
        const val KEY_LONGITUDE = "KEY_LONGITUDE"
        const val KEY_TAKEN_SNAPSHOT_NAME = "KEY_TAKEN_SNAPSHOT_NAME"
        const val KEY_DATA_FROM_ADD_GARDEN_FRAGMENT = "KEY_DATA_FROM_ADD_GARDEN_FRAGMENT"

        const val REQUEST_GARDEN_TITLE = "REQUEST_GARDEN_TITLE"
        const val REQUEST_PHONE_NUMBER = "REQUEST_PHONE_NUMBER"
        const val REQUEST_PERIOD = "REQUEST_PERIOD"
        const val REQUEST_IS_GARDEN = "REQUEST_IS_GARDEN"
        const val REQUEST_UNIQUE_SNAPSHOT_NAME = "REQUEST_UNIQUE_SNAPSHOT_NAME"
        const val REQUEST_LATITUDE = "REQUEST_LATITUDE"
        const val REQUEST_LONGITUDE = "REQUEST_LONGITUDE"

    }
}