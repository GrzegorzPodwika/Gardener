package pl.podwikagrzegorz.gardener.ui.planned_gardens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import pl.podwikagrzegorz.gardener.R

class PlannedGardensFragment : Fragment() {

    private lateinit var plannedGardensViewModel: PlannedGardensViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        plannedGardensViewModel =
                ViewModelProvider(this).get(PlannedGardensViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_planned_gardens, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        plannedGardensViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
