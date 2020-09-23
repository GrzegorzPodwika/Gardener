package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.storage.StorageReference
import pl.podwikagrzegorz.gardener.data.domain.Picture
import pl.podwikagrzegorz.gardener.databinding.SingleTakenPhotoSmallBinding
import pl.podwikagrzegorz.gardener.extensions.loadViaReference
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener

class SmallPhotoAdapter(
    options: FirestoreRecyclerOptions<Picture>,
    private val photoStorageReference: StorageReference,
    private val listener: OnClickPhotoListener
) : FirestoreRecyclerAdapter<Picture, SmallPhotoAdapter.SmallPhotoHolder>(options){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmallPhotoHolder {
        return SmallPhotoHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SmallPhotoHolder, position: Int, model: Picture) {
        val childStorageReference = photoStorageReference.child(model.uniquePictureName)
        holder.bind(childStorageReference, listener)
    }

    class SmallPhotoHolder private constructor(private val binding: SingleTakenPhotoSmallBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(childStorageReference: StorageReference, listener: OnClickPhotoListener) {
            binding.photoStorageReference = childStorageReference
            binding.listener = listener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) : SmallPhotoHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SingleTakenPhotoSmallBinding.inflate(layoutInflater, parent, false)

                return SmallPhotoHolder(binding)
            }
        }

    }


}