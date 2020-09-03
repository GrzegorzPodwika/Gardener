package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import androidx.databinding.DataBindingUtil
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.domain.ManHours
import pl.podwikagrzegorz.gardener.data.domain.ManHoursMap
import pl.podwikagrzegorz.gardener.databinding.ExpandableListGroupBinding
import pl.podwikagrzegorz.gardener.databinding.ExpandableListItemBinding
import pl.podwikagrzegorz.gardener.extensions.toSimpleFormat

class ExpandableListAdapter internal constructor(
    private val context: Context,
    private val manHoursMap: List<ManHoursMap>
) : BaseExpandableListAdapter() {

    override fun getChild(parentPosition: Int, childPosition: Int): ManHours =
        manHoursMap[parentPosition].listOfManHours[childPosition]

    override fun getChildId(parentPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun getChildView(
        parentPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var manHoursRealm: ManHours? = null

        val sumAsString  = GardenerApp.res.getString(R.string.sum_of_hours)
        var sumOfWorkedHours = 0.0

        if (childPosition == getChildrenCount(parentPosition) - 1) {
            sumOfWorkedHours = getSumOfWorkedHoursFor(parentPosition)
        } else {
            manHoursRealm = getChild(parentPosition, childPosition)
        }


        val binding: ExpandableListItemBinding

        binding = if (convertView == null) {
            val layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.expandable_list_item,
                parent,
                false
            )
        } else {
            DataBindingUtil.bind(convertView)!!
        }

        if (manHoursRealm != null){
            binding.expandedListDate.text = manHoursRealm.date.toSimpleFormat()
            binding.expandedListHours.text = manHoursRealm.numberOfWorkedHours.toString()
        } else {
            binding.expandedListDate.text = sumAsString
            binding.expandedListHours.text = sumOfWorkedHours.toString()
        }

        return binding.root
    }

    private fun getSumOfWorkedHoursFor(parentPosition: Int): Double {
        var sumOfWorkedHours = 0.0

        val listOfWorkedHours = manHoursMap[parentPosition].listOfManHours

        for (hour in listOfWorkedHours){
            sumOfWorkedHours += hour.numberOfWorkedHours
        }

        return sumOfWorkedHours
    }

    // The Last position is for the total hours worked by each worker.
    override fun getChildrenCount(parentPosition: Int): Int =
        manHoursMap[parentPosition].listOfManHours.size + 1

    override fun getGroup(parentPosition: Int): String =
        manHoursMap[parentPosition].workerFullName

    override fun getGroupCount(): Int =
        manHoursMap.size


    override fun getGroupId(parentPosition: Int): Long =
        parentPosition.toLong()

    override fun getGroupView(
        parentPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val workerFullName: String = getGroup(parentPosition)

        val binding: ExpandableListGroupBinding

        binding = if (convertView == null) {
            val layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.expandable_list_group,
                parent,
                false
            )

        } else {
            DataBindingUtil.bind(convertView)!!
        }
        binding.listTitle.text = workerFullName

        return binding.root
    }


    override fun hasStableIds(): Boolean = false

    override fun isChildSelectable(parentPosition: Int, childPosition: Int): Boolean = true

}