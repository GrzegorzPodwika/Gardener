package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.pojo.Machine
import pl.podwikagrzegorz.gardener.data.pojo.Property
import pl.podwikagrzegorz.gardener.data.pojo.Tool
import pl.podwikagrzegorz.gardener.data.realm.BasicGardenRealm
import pl.podwikagrzegorz.gardener.data.realm.ItemRealm
import pl.podwikagrzegorz.gardener.databinding.FragmentAddGardenBinding
import pl.podwikagrzegorz.gardener.databinding.FragmentRecViewWithBottomViewBinding
import pl.podwikagrzegorz.gardener.databinding.FragmentToolDividedVerticalBinding
import pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools.MachinesChildViewModel
import pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools.PropertiesChildViewModel
import pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools.ToolsChildViewModel
import pl.podwikagrzegorz.gardener.ui.price_list.OnDeleteItemListener

//TODO("Zajac sie kolejnymi fragmentem Basic Garden moze zeby Intent -> callPhone
sealed class GardenFragmentHolder {

    //Class No1 - BasicGarden
    class BasicGardenFragment : Fragment() {

        private lateinit var gardenBinding: FragmentAddGardenBinding
        private val gardenID: Long by lazy {
            BasicGardenViewModel.fromBundle(requireArguments())
        }
        private val viewModelGarden: BasicGardenViewModel by viewModels {
            GardenViewModelFactory(
                gardenID
            )
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            gardenBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_add_garden, container, false)

            return gardenBinding.root
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            val basicGarden = viewModelGarden.getBasicGarden()
            presetViews()
            setViewsWithContent(basicGarden)
        }

        private fun presetViews() {
            gardenBinding.textInputEditTextGardenTitle.isEnabled = false
            gardenBinding.textInputEditTextPhoneNumber.isEnabled = false
            gardenBinding.materialButtonStartGardenDate.isEnabled = false
            gardenBinding.materialButtonEndGardenDate.isEnabled = false
            gardenBinding.switchMaterialGardenOrService.isClickable = false
            gardenBinding.materialButtonLocateGarden.isEnabled = false

            gardenBinding.materialButtonCallToClient.isEnabled = true
            gardenBinding.materialButtonCallToClient.setOnClickListener {
                val phoneNumber = gardenBinding.textInputEditTextPhoneNumber.text.toString()
                val implicitIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phoneNumber}"))
                startActivity(implicitIntent)
            }
        }

        private fun setViewsWithContent(basicGarden: BasicGardenRealm?) {
            if (basicGarden != null) {
                gardenBinding.textInputEditTextGardenTitle.setText(basicGarden.gardenTitle)
                gardenBinding.textInputEditTextPhoneNumber.setText(basicGarden.phoneNumber.toString())
                gardenBinding.materialTextViewPickedPeriod.text =
                    basicGarden.period?.getPeriodAsString()

                if (basicGarden.isGarden) {
                    gardenBinding.switchMaterialGardenOrService.isChecked = true

                } else {
                    gardenBinding.switchMaterialGardenOrService.isChecked = false
                    gardenBinding.switchMaterialGardenOrService.setText(R.string.service)
                    gardenBinding.shapeableImageViewGardenOrService.setImageResource(R.drawable.ic_lawn_mower)
                }

                gardenBinding.shapeableImageViewPickedLocalization.setImageDrawable(
                    Drawable.createFromPath(
                        basicGarden.snapshotPath
                    )
                )
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

        private lateinit var recViewBinding: FragmentRecViewWithBottomViewBinding
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

            recViewBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_rec_view_with_bottom_view,
                container,
                false
            )

            return recViewBinding.root
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            viewModelGarden.getItemsList()
                ?.observe(viewLifecycleOwner, Observer { descriptionsList ->
                    recViewBinding.recyclerViewSingleItems.also {
                        it.layoutManager = LinearLayoutManager(requireContext())
                        it.adapter = SingleItemAdapter(descriptionsList, this)
                    }
                })

            recViewBinding.imageButtonAddItem.setOnClickListener {
                insertUserDescription()
            }
        }

        private fun insertUserDescription() {
            val userDescription: String = recViewBinding.editTextBotItemName.text.toString()
            viewModelGarden.addItemToList(userDescription)
        }

        override fun onDeleteItemClick(id: Long?) {
            viewModelGarden.deleteItemFromList(id)
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

        private lateinit var recViewBinding: FragmentRecViewWithBottomViewBinding
        private val gardenID: Long by lazy {
            NoteViewModel.fromBundle(requireArguments())
        }
        private val viewModelGarden: NoteViewModel by viewModels { GardenViewModelFactory(gardenID) }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            recViewBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_rec_view_with_bottom_view,
                container,
                false
            )

            return recViewBinding.root
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            viewModelGarden.getItemsList()
                ?.observe(viewLifecycleOwner, Observer { notesList ->
                    recViewBinding.recyclerViewSingleItems.also {
                        it.layoutManager = LinearLayoutManager(requireContext())
                        it.adapter = SingleItemAdapter(notesList, this)
                    }
                })

            recViewBinding.imageButtonAddItem.setOnClickListener {
                insertUserNote()
            }
        }

        private fun insertUserNote() {
            val userNote: String = recViewBinding.editTextBotItemName.text.toString()
            viewModelGarden.addItemToList(userNote)
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
    class ToolFragment : Fragment(), OnDeleteItemListener, OnPushItemListener {
        private lateinit var recViewBinding: FragmentToolDividedVerticalBinding
        private val gardenID: Long by lazy {
            ToolViewModel.fromBundle(requireArguments())
        }
        private val viewModelGarden: ToolViewModel by viewModels {
            GardenViewModelFactory(gardenID)
        }
        private val viewModelTools: ToolsChildViewModel by lazy {
            ViewModelProvider(this).get(ToolsChildViewModel::class.java)
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            recViewBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_tool_divided_vertical,
                container,
                false
            )

            return recViewBinding.root
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            viewModelTools.getToolData().observe(viewLifecycleOwner, Observer { receivedTools ->
                recViewBinding.recyclerViewReceivedTools.also {
                    it.layoutManager = LinearLayoutManager(requireContext())
                    it.adapter = ReceivedToolsAdapter(receivedTools, this)
                }
            })


            viewModelGarden.getToolsList()
                ?.observe(viewLifecycleOwner, Observer { toolsList ->
                    recViewBinding.recyclerViewAddedTools.also {
                        it.layoutManager = LinearLayoutManager(requireContext())
                        it.adapter = AddedItemAdapter(toolsList, this)
                    }
                })

        }

        override fun onChangeNumberOfItems(noItems: Int, position: Int) {
            viewModelGarden.updateNumberOfTools(noItems, position)
        }

        override fun onDeleteItemClick(id: Long?) {
            viewModelGarden.deleteItemFromList(id)
        }

        override fun onPushItemClick(id: Long?) {
            val receivedTool: Tool? = viewModelTools.getSingleTool(id)
            receivedTool?.let {
                val toolToAdd = ItemRealm(it.toolName, it.numberOfTools)
                viewModelGarden.addToolToList(toolToAdd)
            }
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
    class MachineFragment : Fragment(), OnDeleteItemListener, OnPushItemListener {
        private lateinit var recViewBinding: FragmentToolDividedVerticalBinding
        private val gardenID: Long by lazy {
            MachineViewModel.fromBundle(requireArguments())
        }
        private val viewModelGarden: MachineViewModel by viewModels {
            GardenViewModelFactory(
                gardenID
            )
        }
        private val viewModelMachines: MachinesChildViewModel by lazy {
            ViewModelProvider(this).get(MachinesChildViewModel::class.java)
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            recViewBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_tool_divided_vertical,
                container,
                false
            )

            return recViewBinding.root
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            viewModelMachines.getMachineData()
                .observe(viewLifecycleOwner, Observer { receivedMachines ->
                    recViewBinding.recyclerViewReceivedTools.also {
                        it.layoutManager = LinearLayoutManager(requireContext())
                        it.adapter = ReceivedMachinesAdapter(receivedMachines, this)
                    }
                })

            viewModelGarden.getMachinesList()
                ?.observe(viewLifecycleOwner, Observer { machinesList ->
                    recViewBinding.recyclerViewAddedTools.also {
                        it.layoutManager = LinearLayoutManager(requireContext())
                        it.adapter = AddedItemAdapter(machinesList, this)
                    }
                })

        }

        override fun onChangeNumberOfItems(noItems: Int, position: Int) {
            viewModelGarden.updateNumberOfMachines(noItems, position)
        }

        override fun onPushItemClick(id: Long?) {
            val receivedMachine: Machine? = viewModelMachines.getSingleMachine(id)
            receivedMachine?.let {
                val machineToAdd = ItemRealm(it.machineName, it.numberOfMachines)
                viewModelGarden.addMachineToList(machineToAdd)
            }
        }

        override fun onDeleteItemClick(id: Long?) {
            viewModelGarden.deleteItemFromList(id)
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
    class PropertyFragment : Fragment(), OnDeleteItemListener, OnPushItemListener {

        private lateinit var recViewBinding: FragmentToolDividedVerticalBinding
        private val gardenID: Long by lazy {
            PropertyViewModel.fromBundle(requireArguments())
        }
        private val viewModelGarden: PropertyViewModel by viewModels {
            GardenViewModelFactory(
                gardenID
            )
        }
        private val viewModelProperties: PropertiesChildViewModel by lazy {
            ViewModelProvider(this).get(PropertiesChildViewModel::class.java)
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            recViewBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_tool_divided_vertical,
                container,
                false
            )

            return recViewBinding.root
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            viewModelProperties.getPropertyData()
                .observe(viewLifecycleOwner, Observer { receivedProperties ->
                    recViewBinding.recyclerViewReceivedTools.also {
                        it.layoutManager = LinearLayoutManager(requireContext())
                        it.adapter = ReceivedPropertiesAdapter(receivedProperties, this)
                    }
                })

            viewModelGarden.getPropertiesList()
                ?.observe(viewLifecycleOwner, Observer { propertiesList ->
                    recViewBinding.recyclerViewAddedTools.also {
                        it.layoutManager = LinearLayoutManager(requireContext())
                        it.adapter = AddedItemAdapter(propertiesList, this)
                    }
                })
        }

        override fun onChangeNumberOfItems(noItems: Int, position: Int) {
            viewModelGarden.updateNumberOfProperties(noItems, position)
        }

        override fun onDeleteItemClick(id: Long?) {
            viewModelGarden.deleteItemFromList(id)
        }

        override fun onPushItemClick(id: Long?) {
            val receivedProperty: Property? = viewModelProperties.getSingleProperty(id)
            receivedProperty?.let {
                val propertyToAdd = ItemRealm(it.propertyName, it.numberOfProperties)
                viewModelGarden.addPropertyToList(propertyToAdd)
            }
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

        private lateinit var recViewBinding: FragmentRecViewWithBottomViewBinding
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
            recViewBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_rec_view_with_bottom_view,
                container,
                false
            )

            return recViewBinding.root
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)

            viewModelGarden.getItemsList()?.observe(viewLifecycleOwner, Observer { shoppingList ->
                recViewBinding.recyclerViewSingleItems.also {
                    it.layoutManager = LinearLayoutManager(requireContext())
                    it.adapter = SingleItemAdapter(shoppingList, this)
                }
            })

            recViewBinding.imageButtonAddItem.setOnClickListener {
                insertNewShoppingNote()
            }
        }

        private fun insertNewShoppingNote() {
            val userShoppingNote: String = recViewBinding.editTextBotItemName.text.toString()
            viewModelGarden.addItemToList(userShoppingNote)
        }

        override fun onDeleteItemClick(id: Long?) {
            viewModelGarden.deleteItemFromList(id)
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
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return super.onCreateView(inflater, container, savedInstanceState)
        }

        companion object {
            fun create(gardenID: Long): ManHoursFragment {
                val fragment = ManHoursFragment()
                return fragment
            }
        }
    }

    //Class No9 - Photos
    class PhotosFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return super.onCreateView(inflater, container, savedInstanceState)
        }

        companion object {
            fun create(gardenID: Long): PhotosFragment {
                val fragment = PhotosFragment()
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