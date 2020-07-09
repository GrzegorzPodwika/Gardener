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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.pojo.Period
import pl.podwikagrzegorz.gardener.databinding.FragmentAddGardenBinding
import pl.podwikagrzegorz.gardener.extensions.deleteCaptionedImage
import pl.podwikagrzegorz.gardener.extensions.isDefault
import pl.podwikagrzegorz.gardener.maps.MapsActivity
import java.io.File

class AddGardenFragment : Fragment(), DatePickerFragment.OnDateSelectedListener {

    private lateinit var gardenBinding: FragmentAddGardenBinding

    private var absoluteSnapshotPath: String? = null
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

        setDatesButtonListeners()
        setLocationButtonListener()
        setGardenOrServiceSwitchListener()
        setConfirmAddingGardenButtonListener()
    }

    private fun setDatesButtonListeners() {
        gardenBinding.materialButtonStartGardenDate.setOnClickListener {
            val datePickerFragment = DatePickerFragment(true, this)
            datePickerFragment.show(childFragmentManager, TAG_START_DATE_PICKER)
        }

        gardenBinding.materialButtonEndGardenDate.setOnClickListener {
            val datePickerFragment = DatePickerFragment(false, this)
            datePickerFragment.show(childFragmentManager, TAG_END_DATE_PICKER)
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
            if (checkViewsAreFilledByUser()){
                prepareActionAndNavigateToPreviousFragment()
            }
        }
    }

    private fun checkViewsAreFilledByUser() : Boolean{

        if (gardenBinding.textInputEditTextGardenTitle.text.toString().isEmpty()){
            val toastMessage = getString(R.string.empty_garden_title)
            Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
            return false
        }
        if (gardenBinding.textInputEditTextPhoneNumber.text.toString().isEmpty()){
            val toastMessage = getString(R.string.empty_phone_number)
            Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
            return false
        }
        if (period.isDefault()){
            val toastMessage = getString(R.string.default_period)
            Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
            return false
        }
        if (absoluteSnapshotPath.isNullOrEmpty()){
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
            if (data != null){
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
        gardenBinding.materialTextViewPickedPeriod.text = period.getPeriodAsString()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        if (!isAddedGarden)
            absoluteSnapshotPath?.deleteCaptionedImage()
    }

    companion object {
        const val REQUEST_TAKEN_SNAPSHOT_OK = 1

        const val TAG_START_DATE_PICKER = "TAG_START_DATE_PICKER"
        const val TAG_END_DATE_PICKER = "TAG_END_DATE_PICKER"
    }
}
