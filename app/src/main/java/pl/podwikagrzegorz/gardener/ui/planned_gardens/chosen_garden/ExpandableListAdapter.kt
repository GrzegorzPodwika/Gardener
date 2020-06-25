package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import androidx.databinding.DataBindingUtil
import io.realm.RealmList
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.realm.ManHoursMapRealm
import pl.podwikagrzegorz.gardener.data.realm.ManHoursRealm
import pl.podwikagrzegorz.gardener.data.realm.WorkerRealm
import pl.podwikagrzegorz.gardener.databinding.ExpandableListGroupBinding
import pl.podwikagrzegorz.gardener.databinding.ExpandableListItemBinding
import pl.podwikagrzegorz.gardener.extensions.toSimpleFormat
import java.lang.StringBuilder

class ExpandableListAdapter(
    private val context: Context,
    private val workersList: RealmResults<WorkerRealm>,
    private val manHoursMap: RealmList<ManHoursMapRealm>
) :
    BaseExpandableListAdapter() {

    override fun getChild(parentPosition: Int, childPosition: Int): ManHoursRealm =
        manHoursMap[parentPosition]!!.listOfManHours[childPosition]!!

    override fun getChildId(parentPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun getChildView(
        parentPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val manHoursRealm: ManHoursRealm = getChild(parentPosition, childPosition)

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
        binding.expandedListDate.text = manHoursRealm.date.toSimpleFormat()
        binding.expandedListHours.text = manHoursRealm.numberOfWorkedHours.toString()

        return binding.root
    }

    override fun getChildrenCount(parentPosition: Int): Int =
        manHoursMap[parentPosition]!!.listOfManHours.size

    override fun getGroup(parentPosition: Int): Long = manHoursMap[parentPosition]!!.idWorker

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
        val workerId: Long = getGroup(parentPosition)

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
        binding.listTitle.text = getWorkerFullName(workerId)

        return binding.root
    }

    private fun getWorkerFullName(workerId: Long): String {
        val searchingWorker = workersList.find { it.id == workerId }

        return if (searchingWorker != null)
            StringBuilder().append(searchingWorker.name).append(" ").append(searchingWorker.surname)
                .toString()
        else "null"
    }

    override fun hasStableIds(): Boolean = false

    override fun isChildSelectable(parentPosition: Int, childPosition: Int): Boolean = true
}