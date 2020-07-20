package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmList
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.realm.ItemRealm
import pl.podwikagrzegorz.gardener.databinding.McvAddedToolBinding
import pl.podwikagrzegorz.gardener.ui.price_list.OnDeleteItemListener

//1 adapter for added data : Tool, Machine and Property values separately
class AddedItemAdapter(
    private val listOfItems: RealmList<ItemRealm>,
    private val listener: OnDeleteItemListener
) : RecyclerView.Adapter<AddedItemAdapter.AddedItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddedItemHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: McvAddedToolBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.mcv_added_tool, parent, false)

        return AddedItemHolder(binding)
    }

    override fun getItemCount(): Int = listOfItems.size

    override fun onBindViewHolder(holder: AddedItemHolder, position: Int) {
        val item = listOfItems[position]
        holder.bind(item)

        holder.binding.textViewAddedTool.setOnClickListener {
            listener.onChangeFlagToOpposite(position)
        }
        holder.binding.textViewNumbOfTools.setOnClickListener {
            listener.onChangeNumberOfItems(item?.numberOfItems ?: 0, position, item?.itemName ?: "")
        }

        holder.binding.imageButtonToolToDelete.setOnClickListener {
            listener.onDeleteItemClick(position.toLong())
        }

    }


    class AddedItemHolder(val binding: McvAddedToolBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tool: ItemRealm?) {
            tool?.let {
                binding.textViewAddedTool.text = it.itemName
                binding.textViewNumbOfTools.text = it.numberOfItems.toString()

                checkIfTextViewShouldBeCrossed(it.isActive)
            }
        }

        private fun checkIfTextViewShouldBeCrossed(isActive: Boolean) {
            if (isActive) {
                binding.mcvAddedTool.foreground = null
                enableButtonAndNumberPicker()
            } else {
                binding.mcvAddedTool.foreground = strikeThroughForeground
                disableButtonAndNumberPicker()
            }
        }

        private fun enableButtonAndNumberPicker() {
            binding.textViewNumbOfTools.isEnabled = true
            binding.imageButtonToolToDelete.isEnabled = true
        }

        private fun disableButtonAndNumberPicker() {
            binding.textViewNumbOfTools.isEnabled = false
            binding.imageButtonToolToDelete.isEnabled = false
        }

    }

    companion object {
        private val strikeThroughForeground =
            GardenerApp.res.getDrawable(R.drawable.stroke_foreground, null)

    }
}
