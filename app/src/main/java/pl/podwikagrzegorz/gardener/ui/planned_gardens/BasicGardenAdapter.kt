package pl.podwikagrzegorz.gardener.ui.planned_gardens

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.R

import pl.podwikagrzegorz.gardener.data.realm.BasicGardenRealm
import pl.podwikagrzegorz.gardener.data.realm.PeriodRealm
import pl.podwikagrzegorz.gardener.databinding.McvSingleGardenBinding
import pl.podwikagrzegorz.gardener.ui.price_list.OnDeleteItemListener

class BasicGardenAdapter(
    private val basicGardenRealmResults: RealmResults<BasicGardenRealm>,
    private val listener: OnDeleteItemListener
) : RecyclerView.Adapter<BasicGardenAdapter.BasicGardenHolder>(){

    private lateinit var ctx : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : BasicGardenHolder {
        ctx = parent.context

        val inflater = LayoutInflater.from(ctx)
        val binding = DataBindingUtil.inflate<McvSingleGardenBinding>(inflater, R.layout.mcv_single_garden, parent, false)

        return BasicGardenHolder(binding)
    }

    override fun getItemCount(): Int = basicGardenRealmResults.size

    override fun onBindViewHolder(holder: BasicGardenHolder, position: Int) {
        val realmBasicGarden = basicGardenRealmResults[position]

        realmBasicGarden?.apply {
            holder.bind(this)
            val gardenId = basicGardenRealmResults[position]?.id

            holder.binding.root.setOnClickListener {
                listener.onDeleteItemClick(gardenId)
            }

            holder.binding.root.setOnLongClickListener {
                listener.onDeleteItemLongClick(gardenId)
                true
            }
        }
    }

    class BasicGardenHolder( val binding: McvSingleGardenBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(garden: BasicGardenRealm){
            binding.materialTextViewGardenTitle.text = garden.gardenTitle
            binding.textViewPhoneNumber.text = garden.phoneNumber.toString()
            binding.materialTextViewPlannedPeriod.text = formatPeriodToString(garden.period)

            if (!garden.isGarden)
                binding.shapeableImageViewPlannedGarden.setImageResource(R.drawable.ic_lawn_mower)
        }


        private fun formatPeriodToString(period: PeriodRealm?) : String{
            return period?.getPeriodAsString() ?: "Error"
        }
    }
}
