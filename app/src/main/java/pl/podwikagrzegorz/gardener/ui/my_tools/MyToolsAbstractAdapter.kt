package pl.podwikagrzegorz.gardener.ui.my_tools

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.McvSingleToolBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener

@Deprecated("Deprecated since I have switched from using straight realm data to domain data.")
abstract class MyToolsAbstractAdapter <T>(
    open val itemRealmResults: RealmResults<T>,
    open val listener: OnClickItemListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val mcvSingleTool = McvSingleToolBinding.inflate(inflater, parent, false)

        return getViewHolder(mcvSingleTool, viewType)
    }

    override fun getItemCount(): Int = itemRealmResults.size

    @Suppress("unchecked_cast")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (itemRealmResults[position] != null){
            (holder as Binder<T>).bind(itemRealmResults[position]!!, listener)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return itemRealmResults[position]?.let { getLayoutId(position, it) } ?: R.layout.mcv_single_tool
    }

    protected abstract fun getLayoutId(position: Int, obj: T): Int

    abstract fun getViewHolder( binding: McvSingleToolBinding, viewType: Int):RecyclerView.ViewHolder

    internal interface Binder<T>{
        fun bind(data: T, listener: OnClickItemListener)
    }
}