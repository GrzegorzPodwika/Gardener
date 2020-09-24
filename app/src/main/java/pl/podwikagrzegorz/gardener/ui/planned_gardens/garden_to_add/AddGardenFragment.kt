package pl.podwikagrzegorz.gardener.ui.planned_gardens.garden_to_add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.FragmentAddGardenBinding
import pl.podwikagrzegorz.gardener.extensions.*
import timber.log.Timber

class AddGardenFragment : Fragment() {

    private lateinit var binding: FragmentAddGardenBinding
    private val viewModel : AddGardenViewModel by lazy {
        ViewModelProvider(this).get(AddGardenViewModel::class.java)
    }

    private val datePickerBuilder =
        MaterialDatePicker.Builder.dateRangePicker().setTitleText("SELECT A DATE")
    private val materialDatePicker: MaterialDatePicker<Pair<Long, Long>> = datePickerBuilder.build()

    private var isAddedGarden = false
    private var isTakenSnapshot = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddGardenBinding.inflate(inflater, container, false)

        setUpBindingWithViewModel()
        observeArgsFromMapFragment()
        setOnPhoneNumberTextWatcher()
        setOnPickDatesButtonListener()
        setOnChosenRangeComplete()
        setLocationButtonListener()
        setConfirmAddingGardenButtonListener()

        return binding.root
    }

    private fun setUpBindingWithViewModel() {
        binding.apply {
            lifecycleOwner = this@AddGardenFragment
            addGardenViewModel = viewModel
        }
    }

    private fun observeArgsFromMapFragment() {
        getNavigationResult<Double>(KEY_LATITUDE)?.observe(viewLifecycleOwner) { latitude ->
            viewModel.setLatitude(latitude)
        }
        getNavigationResult<Double>(KEY_LONGITUDE)?.observe(viewLifecycleOwner) { longitude ->
            viewModel.setLongitude(longitude)
        }
        getNavigationResult<String>(KEY_TAKEN_SNAPSHOT_NAME)?.observe(viewLifecycleOwner) { takenSnapshotName ->
            isTakenSnapshot = true
            viewModel.setTakenSnapshotName(takenSnapshotName)
        }
    }


    private fun setOnPhoneNumberTextWatcher() {
        binding.textInputEditTextPhoneNumber.addTextChangedListener {
            if (it?.length != binding.textInputLayoutPhoneNumber.counterMaxLength) {
                binding.textInputLayoutPhoneNumber.error = getString(R.string.error)
            } else {
                binding.textInputLayoutPhoneNumber.error = null
            }
        }
    }

    private fun setOnPickDatesButtonListener() {
        binding.materialButtonPickGardenDateRange.setOnClickListener {
            materialDatePicker.show(childFragmentManager, null)
        }
    }

    private fun setOnChosenRangeComplete() {
        materialDatePicker.addOnPositiveButtonClickListener {
            val startDayInMilliseconds = it.first
            val endDayInMilliseconds = it.second

            if (startDayInMilliseconds != null && endDayInMilliseconds != null) {
                viewModel.setPeriod(startDayInMilliseconds, endDayInMilliseconds)
            }
        }
    }

    private fun setLocationButtonListener() {
        binding.materialButtonLocateGarden.setOnClickListener {
            val navController = findNavController()
            val action = AddGardenFragmentDirections.actionNavAddGardenToNavMapFragment()
            navController.navigate(action)
        }
    }

    private fun setConfirmAddingGardenButtonListener() {
        binding.materialButtonConfirmAddingGarden.setOnClickListener {
            if (checkViewsAreFilledByUser()) {
                setBundleAndNavigateToPreviousFragment()
            }
        }
    }

    private fun checkViewsAreFilledByUser(): Boolean {

        if (viewModel.isGivenGardenTitleEmpty()) {
            val toastMessage = getString(R.string.empty_garden_title)
            toast(toastMessage)
            return false
        }
        if (viewModel.isGivenPhoneNumberIncorrect()) {
            val toastMessage = getString(R.string.empty_phone_number)
            toast(toastMessage)
            return false
        }
        if (viewModel.isPeriodDefault()) {
            val toastMessage = getString(R.string.default_period)
            toast(toastMessage)
            return false
        }
        if (viewModel.isNoneTakenSnapshot()) {
            val toastMessage = getString(R.string.none_taken_snapshot)
            toast(toastMessage)
            return false
        }

        return true
    }

    private fun setBundleAndNavigateToPreviousFragment() {
        isAddedGarden = true

        val bundle = viewModel.prepareBundle()
        setFragmentResult(KEY_DATA_FROM_ADD_GARDEN_FRAGMENT, bundle)
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        if (!isAddedGarden && isTakenSnapshot) {
            val uniqueSnapshotName = viewModel.uniqueSnapshotName
            requireContext().getAbsoluteFilePath(uniqueSnapshotName).deleteCaptionedImage()
            Timber.i("Image has been deleted! $isAddedGarden $isTakenSnapshot")
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
