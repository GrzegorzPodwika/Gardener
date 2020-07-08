package pl.podwikagrzegorz.gardener.ui.workers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.WorkerFragmentDialogBinding

class AddWorkerDialog(
    private val listener: OnInputListener
) : DialogFragment() {
    private lateinit var binding : WorkerFragmentDialogBinding

    interface OnInputListener{
        fun sendInput(workerFullName: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.worker_fragment_dialog, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setOnCancelButtonListener()
        setOnAddWorkerButtonListener()
    }

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
    }
}