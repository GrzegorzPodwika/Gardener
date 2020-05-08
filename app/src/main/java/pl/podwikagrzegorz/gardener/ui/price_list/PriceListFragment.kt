package pl.podwikagrzegorz.gardener.ui.price_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.pojo.Note
import pl.podwikagrzegorz.gardener.databinding.FragmentPriceListBinding

class PriceListFragment : Fragment(), OnDeleteItemListener {

    private lateinit var viewModel: PriceListViewModel
    private lateinit var binding : FragmentPriceListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_price_list, container, false)
        binding.imageButtonAddService.setOnClickListener{ insertUserData() }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PriceListViewModel::class.java)

        viewModel.getNoteData().observe(viewLifecycleOwner,
            Observer{notes ->
                binding.recyclerViewPriceList.also {
                    it.layoutManager = LinearLayoutManager(requireContext())
                    it.adapter = NoteAdapter(notes, this)
                }
            })
    }

    private fun insertUserData() {
        val note = Note(0 , binding.editTextService.text.toString(), binding.editTextPriceOfService.text.toString())
        viewModel.addNote(note)
    }

    override fun onDeleteItemClick(id: Long?) {
        viewModel.deleteNote(id)
    }

    companion object {
        fun newInstance() = PriceListFragment()
    }

}
