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

    private val priceListVM: PriceListViewModel by lazy {
        ViewModelProvider(this).get(
            PriceListViewModel::class.java
        )
    }
    private lateinit var priceListBinding: FragmentPriceListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        priceListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_price_list, container, false)
        return priceListBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setOnAddServiceListener()
        observeNoteData()
    }

    private fun setOnAddServiceListener() {
        priceListBinding.imageButtonAddService.setOnClickListener { insertServiceWithPrice() }
    }

    private fun observeNoteData() {
        priceListVM.getNoteData().observe(viewLifecycleOwner,
            Observer { notes ->
                priceListBinding.recyclerViewPriceList.also {
                    it.layoutManager = LinearLayoutManager(requireContext())
                    it.adapter = NoteAdapter(notes, this)
                }
            })
    }

    private fun insertServiceWithPrice() {
        addNote()
        clearViews()
        setFocusOnFirstEditText()
    }

    private fun addNote() {
        val note = Note(
            0,
            priceListBinding.editTextService.text.toString(),
            priceListBinding.editTextPriceOfService.text.toString()
        )
        priceListVM.addNote(note)
    }

    private fun clearViews() {
        priceListBinding.editTextService.text = null
        priceListBinding.editTextPriceOfService.text = null

    }

    private fun setFocusOnFirstEditText() {
        priceListBinding.editTextService.requestFocus()
    }

    override fun onDeleteItemClick(id: Long?) {
        priceListVM.deleteNote(id)
    }

    companion object {
        fun newInstance() = PriceListFragment()
    }

}
