package pl.podwikagrzegorz.gardener.ui.planned_gardens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.app_bar_main.*
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.FragmentPlannedGardensBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.garden_to_add.AddGardenFragment

class PlannedGardensFragment : Fragment() {

    private lateinit var plannedGardensViewModel: PlannedGardensViewModel
    private lateinit var binding: FragmentPlannedGardensBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_planned_gardens, container, false)
        binding.fabAddGarden.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.nav_add_garden)
        }
        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        plannedGardensViewModel = ViewModelProvider(this).get(PlannedGardensViewModel::class.java)
    }
}
