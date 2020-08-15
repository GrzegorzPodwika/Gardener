package pl.podwikagrzegorz.gardener.ui.workers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.WorkerFragmentDialogBinding

class AddWorkerDialog(
    private val listener: OnInputListener
) : DialogFragment() {

    private lateinit var binding: WorkerFragmentDialogBinding
    private val viewModel: AddWorkerViewModel by lazy {
        ViewModelProvider(this).get(AddWorkerViewModel::class.java)
    }

    interface OnInputListener {
        fun onSendWorkerFullName(workerFullName: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = WorkerFragmentDialogBinding.inflate(inflater, container, false)

        setUpBindingWithViewModel()
        observeData()

        return binding.root
    }

    private fun setUpBindingWithViewModel() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            addWorkerViewModel = viewModel
        }
    }

    private fun observeData() {
        observeAddWorkerButton()
        observeDismissButton()
    }

    private fun observeAddWorkerButton() {
        viewModel.workerFullName.observe(viewLifecycleOwner, Observer { fullName ->
            fullName?.let {
                listener.onSendWorkerFullName(it)
                viewModel.onAddWorkerComplete()
                dismiss()
            }
        })
    }

    private fun observeDismissButton() {
        viewModel.eventDismiss.observe(viewLifecycleOwner, Observer { hasClickedDismiss ->
            if (hasClickedDismiss) {
                dismiss()
                viewModel.onDismissComplete()
            }
        })
    }
}

/*            setOnCancelButtonListener()
        setOnAddWorkerButtonListener()
private fun setOnCancelButtonListener() {
        binding.buttonCancel.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun setOnAddWorkerButtonListener() {
        binding.buttonAddWorker.setOnClickListener {
            listener.sendInput(binding.editTextWorkerName.text.toString())
            dialog?.dismiss()
        }
    }*/