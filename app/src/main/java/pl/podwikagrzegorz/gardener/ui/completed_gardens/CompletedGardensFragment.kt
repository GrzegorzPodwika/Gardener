package pl.podwikagrzegorz.gardener.ui.completed_gardens

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import pl.podwikagrzegorz.gardener.R

class CompletedGardensFragment : Fragment() {

    companion object {
        fun newInstance() = CompletedGardensFragment()
    }

    private lateinit var viewModel: CompletedGardensViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_completed_gardens, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CompletedGardensViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
