package pl.podwikagrzegorz.gardener.ui.planned_gardens

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import pl.podwikagrzegorz.gardener.R

class DeleteGardenDialog(
    private val ctx: Context,
    private val listener: NoticeDialogListener
) : DialogFragment() {

    interface NoticeDialogListener {
        fun onDialogClick(isClickedPositive: Boolean)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(ctx)
            .setMessage(R.string.delete_basic_garden)
            .setPositiveButton(
                R.string.confirm
            ) { _, _ ->
                listener.onDialogClick(true)
            }
            .setNegativeButton(
                R.string.cancel
            ) { _, _ ->
                listener.onDialogClick(false)
            }
            .create()
    }
}