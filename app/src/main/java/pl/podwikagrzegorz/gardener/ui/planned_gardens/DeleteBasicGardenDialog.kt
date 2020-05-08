package pl.podwikagrzegorz.gardener.ui.planned_gardens

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import pl.podwikagrzegorz.gardener.R

class DeleteBasicGardenDialog(private val ctx: Context?,
                              private val listener: NoticeDialogListener
) : DialogFragment() {


    interface NoticeDialogListener {
        fun onDialogClick(isClickedPositive: Boolean)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return ctx?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.delete_basic_garden)
                .setPositiveButton(
                    R.string.confirm,
                    DialogInterface.OnClickListener { _, _ ->
                        listener.onDialogClick(true)
                    })
            builder.setNegativeButton(
                R.string.cancel,
                DialogInterface.OnClickListener { _, _ ->
                    listener.onDialogClick(false)
                })

            builder.create()
        } ?: throw IllegalStateException("Context cannot be null")
    }
}