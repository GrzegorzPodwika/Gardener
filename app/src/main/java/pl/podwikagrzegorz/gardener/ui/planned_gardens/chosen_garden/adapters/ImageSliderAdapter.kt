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
        val layoutInflater = LayoutInflater.from(context)
        val binding : SingleImageBinding = DataBindingUtil.inflate(layoutInflater, R.layout.single_image, parent, false)
        return ImageSliderHolder(binding)
    }

    override fun getCount(): Int =
        listOfPicturePaths.size

    override fun onBindViewHolder(viewHolder: ImageSliderHolder?, position: Int) {
        viewHolder?.bind(listOfPicturePaths[position])
    }

    class ImageSliderHolder(val binding: SingleImageBinding) :
        SliderViewAdapter.ViewHolder(binding.root) {

        fun bind(absolutePicturePath: String) {
            binding.imageViewTakenPhoto.load(File(absolutePicturePath))
        }
    }
}