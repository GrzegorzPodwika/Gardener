package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.FragmentAddGardenBinding

sealed class ComponentFragmentHolder {

    //Class No1 - BasicGarden
    class BasicGardenFragment : Fragment(){

        private lateinit var gardenBinding : FragmentAddGardenBinding

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            gardenBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_add_garden, container, false)
            return gardenBinding.root
        }

        companion object{
            fun create() : BasicGardenFragment{
                val fragment = BasicGardenFragment()

                return fragment
            }
        }
    }

    //Class No2 - Description
    class DescriptionFragment : Fragment(){
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {


            return inflater.inflate(R.layout.fragment_rec_view_with_bottom_view, container, false)
        }

        companion object{
            fun create() : DescriptionFragment{
                val fragment = DescriptionFragment()
                return fragment
            }
        }
    }

    //Class No3 - Notes
    class NoteFragment : Fragment(){
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return super.onCreateView(inflater, container, savedInstanceState)
        }

        companion object{
            fun create() : NoteFragment{
                val fragment = NoteFragment()
                return fragment
            }
        }
    }

    //Class No4 - Tools
    class ToolFragment : Fragment(){
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return super.onCreateView(inflater, container, savedInstanceState)
        }

        companion object{
            fun create() : ToolFragment{
                val fragment = ToolFragment()
                return fragment
            }
        }
    }

    //Class No5 - Machines
    class MachineFragment : Fragment(){
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return super.onCreateView(inflater, container, savedInstanceState)
        }

        companion object{
            fun create() : MachineFragment{
                val fragment = MachineFragment()
                return fragment
            }
        }
    }

    //Class No6 - Properties
    class PropertyFragment : Fragment(){
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return super.onCreateView(inflater, container, savedInstanceState)
        }

        companion object{
            fun create() : MachineFragment{
                val fragment = MachineFragment()
                return fragment
            }
        }
    }

    //Class No7 - Shopping
    class ShoppingFragment : Fragment(){
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return super.onCreateView(inflater, container, savedInstanceState)
        }

        companion object{
            fun create() : ShoppingFragment{
                val fragment = ShoppingFragment()
                return fragment
            }
        }
    }

    //Class No8 - Man hours
    class ManHoursFragment : Fragment(){
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return super.onCreateView(inflater, container, savedInstanceState)
        }

        companion object{
            fun create() : MachineFragment{
                val fragment = MachineFragment()
                return fragment
            }
        }
    }

    //Class No9 - Photos
    class PhotosFragment : Fragment(){
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return super.onCreateView(inflater, container, savedInstanceState)
        }

        companion object{
            fun create() : MachineFragment{
                val fragment = MachineFragment()
                return fragment
            }
        }
    }

    companion object{
        fun create(position: Int) : Fragment{
            return when(position){
                0 -> BasicGardenFragment.create()
                1 -> DescriptionFragment.create()
                2 -> NoteFragment.create()
                3 -> ToolFragment.create()
                4 -> MachineFragment.create()
                5 -> PropertyFragment.create()
                6 -> ShoppingFragment.create()
                7 -> ManHoursFragment.create()
                8 -> PhotosFragment.create()
                else -> throw Exception("Program didn't find proper Fragment!")
            }
        }
    }
}