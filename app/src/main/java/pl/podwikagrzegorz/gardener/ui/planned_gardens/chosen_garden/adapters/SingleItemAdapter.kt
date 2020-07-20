package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmList
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.realm.ActiveStringRealm
import pl.podwikagrzegorz.gardener.databinding.McvSingleItemBinding
import pl.podwikagrzegorz.gardener.ui.price_list.OnDeleteItemListener

class SingleItemAdapter(
    private val listOfResults: RealmList<ActiveStringRealm>
) : RecyclerView.Adapter<SingleItemAdapter.SingleItemHolder>() {
    private var listener: OnDeleteItemListener? = null

    fun setListener(listener: OnDeleteItemListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleItemHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<McvSingleItemBinding>(
            layoutInflater, R.layout.mcv_single_item,
            parent, false
        )
        return SingleItemHolder(binding)
    }

    override fun getItemCount(): Int = listOfResults.size

    override fun onBindViewHolder(holder: SingleItemHolder, position: Int) {
        holder.bind(listOfResults[position])

        holder.binding.textViewItemName.setOnClickListener {
            listener?.onChangeFlagToOpposite(position)
        }

        holder.binding.imageButtonItemToDelete.setOnClickListener{
            listener?.onDeleteItemClick(position.toLong())
        }
    }

    class SingleItemHolder(val binding: McvSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(activeString: ActiveStringRealm?) {
            activeString?.let {
                binding.textViewItemName.text = it.name

                checkIfTexViewShouldBeCrossed(it.isActive)
            }
        }

        private fun checkIfTexViewShouldBeCrossed(isActive: Boolean) {
            if (isActive) {
                binding.mcvSingleItem.foreground = null
                enableDeleteButton()
            } else {
                binding.mcvSingleItem.foreground = strikeThroughForeground
                disableDeleteButton()
            }
        }

        private fun enableDeleteButton() {
            binding.imageButtonItemToDelete.isEnabled = true
        }

        private fun disableDeleteButton() {
            binding.imageButtonItemToDelete.isEnabled = false
        }
    }

    companion object {
        private val strikeThroughForeground =
            GardenerApp.res.getDrawable(R.drawable.stroke_foreground, null)

    }
}