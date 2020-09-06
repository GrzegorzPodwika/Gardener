package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.storage.StorageReference
import com.smarteist.autoimageslider.SliderViewAdapter
import pl.podwikagrzegorz.gardener.databinding.SingleImageBinding
import pl.podwikagrzegorz.gardener.extensions.loadViaReference

class ImageSliderAdapter(
    private val context: Context,
    private val listOfPictureRef: List<StorageReference>
) : SliderViewAdapter<ImageSliderAdapter.ImageSliderHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?): ImageSliderHolder {
        return ImageSliderHolder.from(context, parent)
    }

    override fun getCount(): Int =
        listOfPictureRef.size

    override fun onBindViewHolder(viewHolder: ImageSliderHolder?, position: Int) {
        viewHolder?.bind(listOfPictureRef[position])
    }

    class ImageSliderHolder private constructor(val binding: SingleImageBinding) :
        SliderViewAdapter.ViewHolder(binding.root) {

        fun bind(pictureStorageRef: StorageReference) {
            binding.imageViewTakenPhoto.loadViaReference(pictureStorageRef)
            binding.executePendingBindings()
        }

        companion object {
            fun from(context: Context, parent: ViewGroup?) : ImageSliderHolder {
                val layoutInflater = LayoutInflater.from(context)
                val binding = SingleImageBinding.inflate(layoutInflater, parent, false)
                return ImageSliderHolder(binding)
            }
        }
    }
}