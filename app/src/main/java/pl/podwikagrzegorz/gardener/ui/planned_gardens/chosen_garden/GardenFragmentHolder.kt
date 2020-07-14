package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.RealmList
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.realm.*
import pl.podwikagrzegorz.gardener.databinding.*
import pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools.MachinesChildViewModel
import pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools.PropertiesChildViewModel
import pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools.ToolsChildViewModel
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.AddedItemAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.ExpandableListAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.PickNumberDialog
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.SingleItemAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets.SheetAssignWorkerFragment
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets.SheetManHoursFragment
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets.SheetToolsFragment
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.*
import pl.podwikagrzegorz.gardener.ui.price_list.OnDeleteItemListener
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.math.min

sealed class GardenFragmentHolder {

    //Class No1 - BasicGarden
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

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            loadViewsWithBasicInfoAboutGarden()
            setOnCallToClientButtonListener()
            setOnNavigateToClientButtonListener()
        }

        private fun loadViewsWithBasicInfoAboutGarden() {
            receivedBasicGarden = viewModelGarden.getBasicGarden()

            gardenBinding.textViewGardenTitle.text = receivedBasicGarden.gardenTitle
            gardenBinding.textViewPhoneNumber.text = receivedBasicGarden.phoneNumber.toString()
            gardenBinding.textViewPlannedPeriod.text = receivedBasicGarden.period!!.getPeriodAsString()

            gardenBinding.imageViewPickedLocalization.setImageDrawable(
                Drawable.createFromPath(
                    receivedBasicGarden.snapshotPath
                )
            )
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

    //Class No2 - DescriptionFragment
    class DescriptionFragment : Fragment(), OnDeleteItemListener {

        private lateinit var binding: FragmentRecViewWithBottomViewBinding
        private val gardenID: Long by lazy {
            DescriptionViewModel.fromBundle(requireArguments())
        }
        private val viewModelGarden: DescriptionViewModel by viewModels {
            GardenViewModelFactory(
                gardenID
            )
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_rec_view_with_bottom_view,
                container,
                false
            )

            return binding.root
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            presetRecyclerView()
            observeListOfDescriptions()
            setOnAddDescriptionButtonListener()
        }

        private fun presetRecyclerView() {
            binding.recyclerViewSingleItems.layoutManager =
                LinearLayoutManager(requireContext())
        }

        private fun observeListOfDescriptions() {
            viewModelGarden.listOfDescriptions
                .observe(viewLifecycleOwner, Observer {
                    binding.recyclerViewSingleItems.adapter =
                        SingleItemAdapter(
                            it
                        ).apply { setListener(this@DescriptionFragment) }

                })
        }

        private fun setOnAddDescriptionButtonListener() {
            binding.imageButtonAddItem.setOnClickListener {
                insertUserDescription()
            }
        }

        private fun insertUserDescription() {
            insertDescriptionToViewModel()
            clearView()
            setFocusToEditTextView()
        }

        private fun insertDescriptionToViewModel() {
            val userDescription: String = binding.editTextBotItemName.text.toString()
            viewModelGarden.addDescriptionToList(userDescription)
        }

        private fun clearView() {
            binding.editTextBotItemName.text.clear()
        }

        private fun setFocusToEditTextView() {
            binding.editTextBotItemName.requestFocus()
        }

        override fun onDeleteItemClick(id: Long?) {
            viewModelGarden.deleteDescriptionFromList(id)
        }

        companion object {
            fun create(gardenID: Long): DescriptionFragment {
                val fragment = DescriptionFragment()
                fragment.arguments = DescriptionViewModel.toBundle(gardenID)
                return fragment
            }

        }
    }

    //Class No3 - Notes
    class NoteFragment : Fragment(), OnDeleteItemListener {

        private lateinit var binding: FragmentRecViewWithBottomViewBinding
        private val gardenID: Long by lazy {
            NoteViewModel.fromBundle(requireArguments())
        }
        private val viewModelGarden: NoteViewModel by viewModels {
            GardenViewModelFactory(
                gardenID
            )
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_rec_view_with_bottom_view,
                container,
                false
            )

            return binding.root
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            presetRecyclerView()
            observeListOfNotes()
            setOnAddNoteButtonListener()
        }

        private fun presetRecyclerView() {
            binding.recyclerViewSingleItems.layoutManager =
                LinearLayoutManager(requireContext())
        }

        private fun observeListOfNotes() {
            viewModelGarden.listOfNotes
                .observe(viewLifecycleOwner, Observer {
                    binding.recyclerViewSingleItems.adapter =
                        SingleItemAdapter(
                            it
                        ).apply { setListener(this@NoteFragment) }
                })
        }

        private fun setOnAddNoteButtonListener() {
            binding.imageButtonAddItem.setOnClickListener {
                insertUserNote()
            }
        }

        private fun insertUserNote() {
            insertNoteToViewModel()
            clearView()
            setFocusToEditTextView()

        }

        private fun insertNoteToViewModel() {
            val userNote: String = binding.editTextBotItemName.text.toString()
            viewModelGarden.addNoteToList(userNote)
        }

        private fun clearView() {
            binding.editTextBotItemName.text.clear()
        }

        private fun setFocusToEditTextView() {
            binding.editTextBotItemName.requestFocus()
        }

        override fun onDeleteItemClick(id: Long?) {
            viewModelGarden.deleteItemFromList(id)
        }

        companion object {
            fun create(gardenID: Long): NoteFragment {
                val fragment = NoteFragment()
                fragment.arguments = NoteViewModel.toBundle(gardenID)
                return fragment
            }

        }
    }

    //Class No4 - Tools
    class ToolFragment : Fragment(), OnDeleteItemListener {
        private lateinit var binding: FragmentToolsInViewpagerBinding
        private val gardenID: Long by lazy {
            ToolViewModel.fromBundle(requireArguments())
        }
        private val viewModelMainTools: ToolViewModel by viewModels {
            GardenViewModelFactory(
                gardenID
            )
        }
        private val viewModelReceivedTools: ToolsChildViewModel by lazy {
            ViewModelProvider(this).get(ToolsChildViewModel::class.java)
        }

        private val receivedTools: RealmResults<ToolRealm> by lazy {
            viewModelReceivedTools.getListOfToolsAsRealmResults()
        }

        private val receivedToolNames = mutableListOf<String>()
        private lateinit var adapter: AddedItemAdapter

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_tools_in_viewpager,
                container,
                false
            )

            return binding.root
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            presetRecyclerView()
            mapReceivedToolsIntoNames()
            observeListOfAddedTools()
            setOnAddToolsButtonListener()
        }

        private fun presetRecyclerView() {
            binding.recyclerViewAddedTools.layoutManager =
                LinearLayoutManager(requireContext())
        }

        private fun mapReceivedToolsIntoNames() {
            receivedTools.forEach { receivedToolNames.add(it.toolName) }
        }

        private fun observeListOfAddedTools() {
            viewModelMainTools.listOfTools
                .observe(viewLifecycleOwner, Observer {
                    adapter = AddedItemAdapter(it, this)
                    binding.recyclerViewAddedTools.adapter = adapter
                })
        }

        private fun setOnAddToolsButtonListener() {
            binding.materialButtonAddTools.setOnClickListener {
                SheetToolsFragment(
                    receivedToolNames,
                    object : SheetToolsFragment.OnGetListOfPickedItemsListener {
                        override fun onGetListOfPickedItems(listOfPickedItems: List<Boolean>) {
                            addListOfPickedToolsToMainList(listOfPickedItems)
                        }
                    }
                ).show(childFragmentManager, null)
            }
        }

        private fun addListOfPickedToolsToMainList(listOfPickedItems: List<Boolean>) {
            val listOfItemRealm = mutableListOf<ItemRealm>()

            for (index in listOfPickedItems.indices) {
                if (listOfPickedItems[index]) {
                    receivedTools[index]?.let {
                        val toolToAdd = ItemRealm(it.toolName, it.numberOfTools)
                        listOfItemRealm.add(toolToAdd)
                    }
                }
            }

            viewModelMainTools.addListOfPickedTools(listOfItemRealm)
        }

        override fun onChangeNumberOfItems(currentValue: Int, position: Int, itemName: String) {
            val maxValue: Int = viewModelReceivedTools.findMaxValueOf(itemName)

            PickNumberDialog(
                currentValue,
                maxValue,
                object :
                    PickNumberDialog.OnChosenNumberListener {
                    override fun onChosenNumber(chosenNumber: Int) {
                        viewModelMainTools.updateNumberOfTools(chosenNumber, position)
                    }
                }).show(childFragmentManager, null)
        }

        override fun onDeleteItemClick(id: Long?) {
            viewModelMainTools.deleteItemFromList(id)
        }

        override fun onChangeFlagToOpposite(position: Int) {
            viewModelMainTools.reverseFlagOnTool(position)
        }

        companion object {
            fun create(gardenID: Long): ToolFragment {
                val fragment = ToolFragment()
                fragment.arguments = ToolViewModel.toBundle(gardenID)
                return fragment
            }

        }
    }

    //Class No5 - Machines
    class MachineFragment : Fragment(), OnDeleteItemListener {
        private lateinit var binding: FragmentToolsInViewpagerBinding
        private val gardenID: Long by lazy {
            MachineViewModel.fromBundle(requireArguments())
        }
        private val viewModelMainMachines: MachineViewModel by viewModels {
            GardenViewModelFactory(
                gardenID
            )
        }
        private val viewModelReceivedMachines: MachinesChildViewModel by lazy {
            ViewModelProvider(this).get(MachinesChildViewModel::class.java)
        }

        private val receivedMachines: RealmResults<MachineRealm> by lazy {
            viewModelReceivedMachines.getListOfMachinesAsRealmResults()
        }

        private val receivedMachineNames = mutableListOf<String>()
        private lateinit var adapter: AddedItemAdapter

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_tools_in_viewpager,
                container,
                false
            )

            return binding.root
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            presetRecyclerView()
            mapReceivedMachinesIntoNames()
            observeListOfAddedMachines()
            setOnAddMachinesButtonListener()
        }

        private fun presetRecyclerView() {
            binding.recyclerViewAddedTools.layoutManager =
                LinearLayoutManager(requireContext())
        }

        private fun mapReceivedMachinesIntoNames() {
            receivedMachines.forEach { receivedMachineNames.add(it.machineName) }
        }

        private fun observeListOfAddedMachines() {
            viewModelMainMachines.listOfMachines
                .observe(viewLifecycleOwner, Observer {
                    adapter = AddedItemAdapter(it, this)
                    binding.recyclerViewAddedTools.adapter = adapter
                })
        }

        private fun setOnAddMachinesButtonListener() {
            binding.materialButtonAddTools.text = getString(R.string.equipment)
            binding.materialButtonAddTools.icon = GardenerApp.res.getDrawable(R.drawable.ic_equipment, null)
            binding.materialButtonAddTools.setOnClickListener {
                SheetToolsFragment(
                    receivedMachineNames,
                    object : SheetToolsFragment.OnGetListOfPickedItemsListener {
                        override fun onGetListOfPickedItems(listOfPickedItems: List<Boolean>) {
                            addListOfPickedMachinesToMainList(listOfPickedItems)
                        }
                    }
                ).show(childFragmentManager, null)
            }
        }

        private fun addListOfPickedMachinesToMainList(listOfPickedItems: List<Boolean>) {
            val listOfItemRealm = mutableListOf<ItemRealm>()

            for (index in listOfPickedItems.indices) {
                if (listOfPickedItems[index]) {
                    receivedMachines[index]?.let {
                        val machineToAdd = ItemRealm(it.machineName, it.numberOfMachines)
                        listOfItemRealm.add(machineToAdd)
                    }
                }
            }

            viewModelMainMachines.addListOfPickedMachines(listOfItemRealm)
        }

        override fun onChangeNumberOfItems(currentValue: Int, position: Int, itemName: String) {
            val maxValue: Int = viewModelReceivedMachines.findMaxValueOf(itemName)

            PickNumberDialog(
                currentValue,
                maxValue,
                object :
                    PickNumberDialog.OnChosenNumberListener {
                    override fun onChosenNumber(chosenNumber: Int) {
                        viewModelMainMachines.updateNumberOfMachines(chosenNumber, position)
                    }
                }).show(childFragmentManager, null)
        }

        override fun onChangeFlagToOpposite(position: Int) {
            viewModelMainMachines.reverseFlagOnMachine(position)
        }

        override fun onDeleteItemClick(id: Long?) {
            viewModelMainMachines.deleteItemFromList(id)
        }

        companion object {
            fun create(gardenID: Long): MachineFragment {
                val fragment = MachineFragment()
                fragment.arguments = MachineViewModel.toBundle(gardenID)
                return fragment
            }

        }

    }

    //Class No6 - Properties
    class PropertyFragment : Fragment(), OnDeleteItemListener {

        private lateinit var binding: FragmentToolsInViewpagerBinding
        private val gardenID: Long by lazy {
            PropertyViewModel.fromBundle(requireArguments())
        }
        private val viewModelMainProperties: PropertyViewModel by viewModels {
            GardenViewModelFactory(
                gardenID
            )
        }
        private val viewModelReceivedProperties: PropertiesChildViewModel by lazy {
            ViewModelProvider(this).get(PropertiesChildViewModel::class.java)
        }
        private val receivedProperties: RealmResults<PropertyRealm> by lazy {
            viewModelReceivedProperties.getListOfPropertiesAsRealmResults()
        }

        private val receivedPropertyNames = mutableListOf<String>()
        private lateinit var adapter: AddedItemAdapter

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_tools_in_viewpager,
                container,
                false
            )

            return binding.root
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            presetRecyclerView()
            mapReceivedPropertiesIntoNames()
            observeListOfAddedProperties()
            setOnAddPropertiesButtonListener()
        }

        private fun presetRecyclerView() {
            binding.recyclerViewAddedTools.layoutManager =
                LinearLayoutManager(requireContext())
        }

        private fun mapReceivedPropertiesIntoNames() {
            receivedProperties.forEach { receivedPropertyNames.add(it.propertyName) }
        }

        private fun observeListOfAddedProperties() {
            viewModelMainProperties.listOfProperties
                .observe(viewLifecycleOwner, Observer {
                    adapter = AddedItemAdapter(it, this)
                    binding.recyclerViewAddedTools.adapter = adapter
                })
        }

        private fun setOnAddPropertiesButtonListener() {
            binding.materialButtonAddTools.text = getString(R.string.others)
            binding.materialButtonAddTools.icon = GardenerApp.res.getDrawable(R.drawable.ic_canister, null)
            binding.materialButtonAddTools.setOnClickListener {
                SheetToolsFragment(
                    receivedPropertyNames,
                    object : SheetToolsFragment.OnGetListOfPickedItemsListener {
                        override fun onGetListOfPickedItems(listOfPickedItems: List<Boolean>) {
                            addListOfPickedPropertiesToMainList(listOfPickedItems)
                        }
                    }
                ).show(childFragmentManager, null)
            }
        }

        private fun addListOfPickedPropertiesToMainList(listOfPickedItems: List<Boolean>) {
            val listOfItemRealm = mutableListOf<ItemRealm>()

            for (index in listOfPickedItems.indices) {
                if (listOfPickedItems[index]) {
                    receivedProperties[index]?.let {
                        val propertyToAdd = ItemRealm(it.propertyName, it.numberOfProperties)
                        listOfItemRealm.add(propertyToAdd)
                    }
                }
            }

            viewModelMainProperties.addListOfPickedProperties(listOfItemRealm)
        }


        override fun onChangeNumberOfItems(currentValue: Int, position: Int, itemName: String) {
            val maxValue: Int = viewModelReceivedProperties.findMaxValueOf(itemName)

            PickNumberDialog(
                currentValue,
                maxValue,
                object :
                    PickNumberDialog.OnChosenNumberListener {
                    override fun onChosenNumber(chosenNumber: Int) {
                        viewModelMainProperties.updateNumberOfProperties(chosenNumber, position)
                    }
                }).show(childFragmentManager, null)
        }

        override fun onDeleteItemClick(id: Long?) {
            viewModelMainProperties.deleteItemFromList(id)
        }

        override fun onChangeFlagToOpposite(position: Int) {
            viewModelMainProperties.reverseFlagOnProperty(position)
        }

        companion object {
            fun create(gardenID: Long): PropertyFragment {
                val fragment = PropertyFragment()
                fragment.arguments = PropertyViewModel.toBundle(gardenID)
                return fragment
            }
        }
    }

    //Class No7 - Shopping
    class ShoppingFragment : Fragment(), OnDeleteItemListener {

        private lateinit var binding: FragmentRecViewWithBottomViewBinding
        private val gardenID: Long by lazy {
            ShoppingViewModel.fromBundle(requireArguments())
        }
        private val viewModelGarden: ShoppingViewModel by viewModels {
            GardenViewModelFactory(
                gardenID
            )
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_rec_view_with_bottom_view,
                container,
                false
            )

            return binding.root
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            presetRecyclerView()
            observeListOfShopping()
            setOnAddShoppingNoteButtonListener()
        }

        private fun presetRecyclerView() {
            binding.recyclerViewSingleItems.layoutManager =
                LinearLayoutManager(requireContext())
        }

        private fun observeListOfShopping() {
            viewModelGarden.listOfShopping.observe(viewLifecycleOwner, Observer {
                binding.recyclerViewSingleItems.adapter =
                    SingleItemAdapter(
                        it
                    ).apply { setListener(this@ShoppingFragment) }
            })
        }

        private fun setOnAddShoppingNoteButtonListener() {
            binding.imageButtonAddItem.setOnClickListener {
                insertNewShoppingNote()
            }
        }

        private fun insertNewShoppingNote() {
            insertShoppingNoteToViewModel()
            clearView()
            setFocusToEditTextView()
        }

        private fun insertShoppingNoteToViewModel() {
            val userShoppingNote: String = binding.editTextBotItemName.text.toString()
            viewModelGarden.addShoppingNoteToList(userShoppingNote)
        }

        private fun clearView() {
            binding.editTextBotItemName.text.clear()
        }

        private fun setFocusToEditTextView() {
            binding.editTextBotItemName.requestFocus()
        }

        override fun onDeleteItemClick(id: Long?) {
            viewModelGarden.deleteShoppingNoteFromList(id)
        }

        companion object {
            fun create(gardenID: Long): ShoppingFragment {
                val fragment = ShoppingFragment()
                fragment.arguments = ShoppingViewModel.toBundle(gardenID)
                return fragment
            }
        }
    }

    //Class No8 - Man hours
    class ManHoursFragment : Fragment() {

        private lateinit var binding: ExpandableListsOfManHoursBinding
        private val gardenID: Long by lazy {
            ManHoursViewModel.fromBundle(requireArguments())
        }
        private val viewModel: ManHoursViewModel by viewModels {
            GardenViewModelFactory(
                gardenID
            )
        }
        private lateinit var workersList: RealmResults<WorkerRealm>

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = DataBindingUtil.inflate(
                inflater,
                R.layout.expandable_lists_of_man_hours,
                container,
                false
            )
            return binding.root
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            observeMapOfWorkedHours()
            setOnAddWorkerButtonListener()
            setOnAddManHoursButtonListener()
        }

        private fun observeMapOfWorkedHours() {
            val expandableList = binding.expandableListView

            viewModel.mapOfWorkedHours.observe(
                viewLifecycleOwner,
                Observer { mapOfWorkedHours ->
                    expandableList.setAdapter(
                        ExpandableListAdapter(
                            requireContext(), mapOfWorkedHours
                        )
                    )
                })
        }

        private fun setOnAddWorkerButtonListener() {
            workersList = viewModel.getReceivedWorkers()

            binding.materialButtonAddWorkers.setOnClickListener {
                SheetAssignWorkerFragment(
                    workersList,
                    object :
                        SheetAssignWorkerFragment.OnGetListOfWorkersFullNameListener {
                        override fun onGetListOfWorkersFullName(listOfWorkersFullName: List<String>) {
                            viewModel.addListOfWorkersFullNames(listOfWorkersFullName)
                        }
                    }).show(childFragmentManager, null)
            }
        }

        private fun setOnAddManHoursButtonListener() {

            binding.materialButtonAddManHours.setOnClickListener {
                SheetManHoursFragment(
                    viewModel.getWorkersFullNames(),
                    object :
                        SheetManHoursFragment.OnGetListOfWorkedHoursWithPickedDate {
                        override fun onGetListOfWorkedHoursWithPickedDate(
                            listOfWorkedHours: List<Double>,
                            date: Date
                        ) {
                            viewModel.addListOfWorkedHoursWithPickedDate(listOfWorkedHours, date)
                        }
                    }).show(childFragmentManager, null)
            }
        }

        companion object {
            fun create(gardenID: Long): ManHoursFragment {
                val fragment = ManHoursFragment()
                fragment.arguments = ManHoursViewModel.toBundle(gardenID)
                return fragment
            }
        }
    }

    // Photos
    class PhotosFragment : Fragment() {
        private lateinit var binding: FragmentPhotosBinding
        private val gardenID: Long by lazy {
            PhotosViewModel.fromBundle(requireArguments())
        }

        private val viewModel: PhotosViewModel by viewModels {
            GardenViewModelFactory(
                gardenID
            )
        }

        private val fileProviderPath: File by lazy {
            requireContext().filesDir
        }

        private val targetWidth: Int =
            GardenerApp.res.getDimensionPixelSize(R.dimen.image_size_small)

        private val targetHeight: Int = targetWidth
        private var uniquePhotoName: String = ""

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photos, container, false)
            return binding.root
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            observePhotosListFromDb()
            setOnTakePhotoButtonListener()
            setOnCompleteGardenButtonListener()
        }

        private fun observePhotosListFromDb() {
            viewModel.listOfPicturesPaths.observe(
                viewLifecycleOwner,
                Observer { listOfPicturesPaths ->
                    if (listOfPicturesPaths.size == MAX_NUMBER_OF_POSSIBLE_PICTURES) {
                        binding.materialButtonTakePhoto.isEnabled = false
                    }

                    setReceivedPhotos(listOfPicturesPaths)
                })
        }

        private fun setReceivedPhotos(listOfPicturesPaths: RealmList<String>) {

            for (index in listOfPicturesPaths.indices) {
                val absolutePath = getAbsolutePathFrom(listOfPicturesPaths[index]!!)
                val scaledBitmap = getScaledBitmap(absolutePath)

                when (index) {
                    0 -> binding.imageViewPhoto0.setImageBitmap(scaledBitmap)

                    1 -> binding.imageViewPhoto1.setImageBitmap(scaledBitmap)

                    2 -> binding.imageViewPhoto2.setImageBitmap(scaledBitmap)

                    3 -> binding.imageViewPhoto3.setImageBitmap(scaledBitmap)

                    4 -> binding.imageViewPhoto4.setImageBitmap(scaledBitmap)

                    5 -> binding.imageViewPhoto5.setImageBitmap(scaledBitmap)

                    6 -> binding.imageViewPhoto6.setImageBitmap(scaledBitmap)

                    7 -> binding.imageViewPhoto7.setImageBitmap(scaledBitmap)

                    8 -> binding.imageViewPhoto8.setImageBitmap(scaledBitmap)

                    9 -> binding.imageViewPhoto9.setImageBitmap(scaledBitmap)

                    10 -> binding.imageViewPhoto10.setImageBitmap(scaledBitmap)

                    11 -> binding.imageViewPhoto11.setImageBitmap(scaledBitmap)

                }
            }

        }

        private fun getAbsolutePathFrom(fileName: String): String =
            File(fileProviderPath, fileName).absolutePath


        private fun getScaledBitmap(path: String): Bitmap {
            val bmOptions = BitmapFactory.Options().apply {
                // Get the dimensions of the bitmap
                inJustDecodeBounds = true

                val photoW: Int = outWidth
                val photoH: Int = outHeight

                // Determine how much to scale down the image
                val scaleFactor: Int = min(photoW / targetWidth, photoH / targetHeight)

                // Decode the image file into a Bitmap sized to fill the View
                inJustDecodeBounds = false
                inSampleSize = scaleFactor
            }
            return BitmapFactory.decodeFile(path, bmOptions)
        }

        private fun setOnTakePhotoButtonListener() {
            binding.materialButtonTakePhoto.setOnClickListener {
                val packageManager = requireContext().packageManager
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    takePictureIntent.resolveActivity(packageManager)?.also {
                        val photoFile: File? = try {
                            createImageFile()
                        } catch (ex: IOException) {

                            null
                        }

                        photoFile?.also {
                            val photoUri: Uri = FileProvider.getUriForFile(
                                requireContext(),
                                GardenerApp.WRITTEN_FILE_LOCATION,
                                it
                            )
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
                        }
                    }
                }
            }
        }

        @Throws(IOException::class)
        private fun createImageFile(): File {
            uniquePhotoName = "${System.currentTimeMillis()}.jpg"

            return File(fileProviderPath, uniquePhotoName)
        }


        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
                viewModel.addPictureToList(uniquePhotoName)
            }
        }

        private fun setOnCompleteGardenButtonListener() {
            //TODO - zaimplementowac konczenie ogrodu kiedys tam
        }

        companion object {
            const val CAMERA_REQUEST_CODE = 1001
            const val MAX_NUMBER_OF_POSSIBLE_PICTURES = 12

            fun create(gardenID: Long): PhotosFragment {
                val fragment = PhotosFragment()
                fragment.arguments = PhotosViewModel.toBundle(gardenID)
                return fragment
            }
        }
    }

    companion object {
        fun create(position: Int, gardenID: Long): Fragment {
            return when (position) {
                0 -> BasicGardenFragment.create(gardenID)
                1 -> DescriptionFragment.create(gardenID)
                2 -> NoteFragment.create(gardenID)
                3 -> ToolFragment.create(gardenID)
                4 -> MachineFragment.create(gardenID)
                5 -> PropertyFragment.create(gardenID)
                6 -> ShoppingFragment.create(gardenID)
                7 -> ManHoursFragment.create(gardenID)
                8 -> PhotosFragment.create(gardenID)
                else -> throw Exception("Program didn't find proper Fragment!")
            }
        }
    }
}