package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import coil.api.load
import com.smarteist.autoimageslider.SliderViewAdapter
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.SingleImageBinding
import java.io.File

class ImageSliderAdapter(
    private val context: Context,
    private val listOfPicturePaths: List<String>
) : SliderViewAdapter<ImageSliderAdapter.ImageSliderHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?): ImageSliderHolder {
        return ImageSliderHolder.from(context, parent)
    }

    override fun getCount(): Int =
        listOfPicturePaths.size

    override fun onBindViewHolder(viewHolder: ImageSliderHolder?, position: Int) {
        viewHolder?.bind(listOfPicturePaths[position])
    }

    class ImageSliderHolder private constructor(val binding: SingleImageBinding) :
        SliderViewAdapter.ViewHolder(binding.root) {

        fun bind(uniqueSnapshotName: String) {
            binding.uniqueImageName = uniqueSnapshotName
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