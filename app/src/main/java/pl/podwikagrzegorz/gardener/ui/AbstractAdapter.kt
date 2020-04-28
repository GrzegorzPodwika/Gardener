package pl.podwikagrzegorz.gardener.ui

import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults


//niewiem
abstract class AbstractAdapter<T>(
    open val itemRealmResults: RealmResults<T>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

/*    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

    }*/

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    //abstract fun getViewHolder(view: View, viewType: Int) : RecyclerView.ViewHolder
}