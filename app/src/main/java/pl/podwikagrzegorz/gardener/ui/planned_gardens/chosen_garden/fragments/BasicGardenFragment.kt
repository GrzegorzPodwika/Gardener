package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.fragments

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.api.load
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.realm.BasicGardenRealm
import pl.podwikagrzegorz.gardener.databinding.FragmentAddedGardenBinding
import pl.podwikagrzegorz.gardener.extensions.getAbsoluteFilePath
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets.FullImageDialogFragment
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.BasicGardenViewModel
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.GardenViewModelFactory
import java.io.File

class BasicGardenFragment : Fragment() {

    private lateinit var gardenBinding: FragmentAddedGardenBinding
    private val gardenID: Long by lazy {
        BasicGardenViewModel.fromBundle(requireArguments())
    }
    private val viewModelGarden: BasicGardenViewModel by viewModels {
        GardenViewModelFactory(
            gardenID
        )
    }
    private lateinit var receivedBasicGarden: BasicGardenRealm

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gardenBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_added_garden, container, false)

        return gardenBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadViewsWithBasicInfoAboutGarden()
        setOnCallToClientButtonListener()
        setOnNavigateToClientButtonListener()
    }

    private fun loadViewsWithBasicInfoAboutGarden() {
        receivedBasicGarden = viewModelGarden.getBasicGarden()

        gardenBinding.textViewGardenTitle.text = receivedBasicGarden.gardenTitle
        gardenBinding.textViewPhoneNumber.text = receivedBasicGarden.phoneNumber.toString()
        gardenBinding.textViewPlannedPeriod.text = receivedBasicGarden.period!!.getPeriodAsString()

        val absolutePath = requireContext().getAbsoluteFilePath(receivedBasicGarden.uniqueSnapshotName)
        gardenBinding.imageViewPickedLocalization.load(File(absolutePath))

    }

    private fun setOnCallToClientButtonListener() {
        gardenBinding.materialButtonCallToClient.setOnClickListener {
            val phoneNumber = gardenBinding.textViewPhoneNumber.text.toString()
            val implicitIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phoneNumber}"))
            startActivity(implicitIntent)
        }
    }

    private fun setOnNavigateToClientButtonListener() {
        gardenBinding.materialButtonNavigate.setOnClickListener {
            val latitude = receivedBasicGarden.latitude
            val longitude = receivedBasicGarden.longitude
            val packageManager = requireContext().packageManager

            val gmmIntentUri =
                Uri.parse("google.navigation:q=$latitude,$longitude")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            mapIntent.resolveActivity(packageManager)?.let {
                startActivity(mapIntent)
            }
        }
    }

    companion object {
        fun create(gardenID: Long): BasicGardenFragment {
            val fragment = BasicGardenFragment()
            fragment.arguments = BasicGardenViewModel.toBundle(gardenID)
            return fragment
        }
    }
}