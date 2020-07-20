package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import coil.api.load
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.DialogWholePictureBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.ImageSliderAdapter
import java.io.File

class FullImageDialogFragment(
    private val listOfPicturePaths : List<String>
) : DialogFragment() {

    private lateinit var binding: DialogWholePictureBinding
/*    private val absoluteImagePath: String? by lazy {
        pathFromBundle(requireArguments())
    }*/

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        dialog?.apply {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            window?.setLayout(width, height)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_whole_picture, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setImageSliderAdapter()
    }

    private fun setImageSliderAdapter() {
        binding.imageSlider.apply {
            setSliderAdapter(ImageSliderAdapter(requireContext(), listOfPicturePaths))
            setIndicatorAnimation(IndicatorAnimationType.WORM)
            setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            indicatorSelectedColor = Color.WHITE;
            indicatorUnselectedColor = Color.GRAY
            scrollTimeInSec = 3
        }
    }


}