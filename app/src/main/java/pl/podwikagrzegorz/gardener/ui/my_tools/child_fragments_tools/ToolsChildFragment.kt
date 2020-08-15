package pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.FragmentChildToolsBinding
import pl.podwikagrzegorz.gardener.extensions.toast
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener

class ToolsChildFragment : Fragment() {

    private lateinit var binding: FragmentChildToolsBinding
    private val viewModel: ToolsChildViewModel by lazy {
        ViewModelProvider(this).get(ToolsChildViewModel::class.java)
    }
    private val toolAdapter: ToolAdapter by lazy {
        ToolAdapter(object : OnClickItemListener {
            override fun onClick(id: Long) {
                deleteToolFromDb(id)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChildToolsBinding.inflate(inflater, container, false)

        setUpBindingWithViewModel()
        observeAddToolButton()
        observeToolData()

        return binding.root
    }

    private fun setUpBindingWithViewModel() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            toolsChildViewModel = viewModel
            recyclerViewMyTools.adapter = toolAdapter
        }
    }

    private fun observeAddToolButton() {
        viewModel.eventAddTool.observe(viewLifecycleOwner, Observer { hasAdded ->
            if (hasAdded) {
                cleanUp()
            }
        })

        viewModel.errorEditTextEmpty.observe(viewLifecycleOwner, Observer { isEmpty ->
            if (isEmpty) {
                showErrorToast()
            }
        })

    }

    private fun showErrorToast() {
        context?.toast(getString(R.string.fill_all_fields))
        viewModel.onErrorShowComplete()
    }

    private fun cleanUp() {
        clearViews()
        setFocusOnFirstEditText()
        viewModel.onAddToolComplete()
    }

    private fun clearViews() {
        binding.editTextToolNameAdd.text = null
        binding.editTextPriceOfTool.text = null
    }

    private fun setFocusOnFirstEditText() {
        binding.editTextToolNameAdd.requestFocus()
    }

    private fun observeToolData() {
        viewModel.listOfTools.observe(viewLifecycleOwner, Observer { listOfTools ->
            toolAdapter.submitList(listOfTools)
        })
    }

    private fun deleteToolFromDb(id: Long) {
        viewModel.deleteTool(id)
    }

}
