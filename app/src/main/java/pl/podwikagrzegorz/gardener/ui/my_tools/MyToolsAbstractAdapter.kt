package pl.podwikagrzegorz.gardener.ui.my_tools

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.ListMaterialcardviewBinding
import pl.podwikagrzegorz.gardener.ui.price_list.OnDeleteItemListener

abstract class MyToolsAbstractAdapter <T>(
    open val itemRealmResults: RealmResults<T>,
    open val listener: OnDeleteItemListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val materialCVBinding =
            DataBindingUtil.inflate<ListMaterialcardviewBinding>(inflater, R.layout.list_materialcardview, parent, false)

        return getViewHolder(materialCVBinding, viewType)
    }

    override fun getItemCount(): Int = itemRealmResults.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (itemRealmResults[position] != null){
            (holder as Binder<T>).bind(itemRealmResults[position]!!, listener)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return itemRealmResults[position]?.let { getLayoutId(position, it) } ?: R.layout.list_materialcardview
    }

    protected abstract fun getLayoutId(position: Int, obj: T): Int

    abstract fun getViewHolder( binding: ListMaterialcardviewBinding, viewType: Int):RecyclerView.ViewHolder

    internal interface Binder<T>{
        fun bind(data: T, listener: OnDeleteItemListener)
    }
}