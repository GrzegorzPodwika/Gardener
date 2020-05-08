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
            holder.binding.root.setOnLongClickListener {
                val id = basicGardenRealmResults[position]?.id
                listener.onDeleteItemClick(id)
                true
            }
        }
    }

    class BasicGardenHolder( val binding: McvSingleGardenBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(garden: BasicGardenRealm){
            binding.materialTextViewGardenTitle.text = garden.gardenTitle
            binding.materialTextViewPlannedPhone.text = garden.phoneNumber.toString()
            binding.materialTextViewPlannedPeriod.text = formatPeriodToString(garden.period)

            if (!garden.isGarden)
                binding.shapeableImageViewPlannedGarden.setImageResource(R.drawable.ic_lawn_mower)
        }

        private fun formatPeriodToString(period: PeriodRealm?) : String{
            return if (period != null){
                val result = String.format(
                    "%02d.%02d.%s  -  %02d.%02d.%s",
                    period.startDay,
                    period.startMonth,
                    period.startYear,
                    period.endDay,
                    period.endMonth,
                    period.endYear)

                result
            }else
                "Error"
        }
    }
}
