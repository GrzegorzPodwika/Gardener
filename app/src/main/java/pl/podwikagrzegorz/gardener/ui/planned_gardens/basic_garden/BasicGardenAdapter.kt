package pl.podwikagrzegorz.gardener.ui.planned_gardens.basic_garden

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.R

import pl.podwikagrzegorz.gardener.data.realm.BasicGardenRealm
import pl.podwikagrzegorz.gardener.databinding.McvSingleGardenBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener

class BasicGardenAdapter(
    private val basicGardenRealmResults: RealmResults<BasicGardenRealm>,
    private val listener: OnClickItemListener
) : RecyclerView.Adapter<BasicGardenAdapter.BasicGardenHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : BasicGardenHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<McvSingleGardenBinding>(
            inflater,
            R.layout.mcv_single_garden,
            parent,
            false
        )

        return BasicGardenHolder(
            binding
        )
    }

    override fun getItemCount(): Int = basicGardenRealmResults.size

    override fun onBindViewHolder(holder: BasicGardenHolder, position: Int) {
        val realmBasicGarden = basicGardenRealmResults[position]

        realmBasicGarden?.apply {
            holder.bind(this)
            val gardenId = realmBasicGarden.id

            holder.binding.root.setOnClickListener {
                listener.onClickListener(gardenId)
            }

            holder.binding.root.setOnLongClickListener {
                listener.onLongClickListener(gardenId)
                true
            }
        }
    }

    class BasicGardenHolder(val binding: McvSingleGardenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(garden: BasicGardenRealm) {
            binding.materialTextViewGardenTitle.text = garden.gardenTitle
            binding.textViewPhoneNumber.text = garden.phoneNumber.toString()
            binding.materialTextViewPlannedPeriod.text = garden.period!!.getPeriodAsString()

            if (!garden.isGarden)
                binding.shapeableImageViewPlannedGarden.setImageResource(R.drawable.ic_lawn_mower)
        }
    }
}
