package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmList
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.McvSingleItemBinding
import pl.podwikagrzegorz.gardener.ui.price_list.OnDeleteItemListener

class SingleItemAdapter(
    private val listOfResults: RealmList<String>,
    private val listener: OnDeleteItemListener
) : RecyclerView.Adapter<SingleItemAdapter.SingleItemHolder>() {

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

        holder.binding.imageButtonItemToDelete.setOnClickListener{
            listener.onDeleteItemClick(position.toLong())
        }
    }

    class SingleItemHolder(val binding: McvSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemName: String?) {
            binding.textViewItemName.text = itemName
        }
    }
}