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
import pl.podwikagrzegorz.gardener.data.realm.NoteRealm
import pl.podwikagrzegorz.gardener.databinding.FragmentPriceListBinding

class PriceListFragment : Fragment(), OnDeleteItemListener {

    private lateinit var priceListBinding: FragmentPriceListBinding
    private val priceListVM: PriceListViewModel by lazy {
        ViewModelProvider(this).get(
            PriceListViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        priceListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_price_list, container, false)
        return priceListBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setOnAddServiceListener()
        observeNoteData()
    }

    private fun setOnAddServiceListener() {
        priceListBinding.imageButtonAddService.setOnClickListener {
            insertServiceWithPrice()
        }
    }

    private fun insertServiceWithPrice() {
        addNote()
        clearViews()
        setFocusOnFirstEditText()
    }

    private fun addNote() {
        val note = NoteRealm(
            0,
            priceListBinding.editTextService.text.toString(),
            priceListBinding.editTextPriceOfService.text.toString()
        )
        priceListVM.addNote(note)
    }

    private fun clearViews() {
        priceListBinding.editTextService.text.clear()
        priceListBinding.editTextPriceOfService.text.clear()
    }

    private fun setFocusOnFirstEditText() {
        priceListBinding.editTextService.requestFocus()
    }

    private fun observeNoteData() {
        priceListBinding.recyclerViewPriceList.layoutManager = LinearLayoutManager(requireContext())

        priceListVM.priceList.observe(viewLifecycleOwner,
            Observer { notes ->
                priceListBinding.recyclerViewPriceList.also {
                    it.adapter = NoteAdapter(notes, this)
                }
            })
    }

    override fun onDeleteItemClick(id: Long?) {
        priceListVM.deleteNote(id)
    }

    companion object {
        fun newInstance() = PriceListFragment()
    }

}
