package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.firebase.storage.StorageReference
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import pl.podwikagrzegorz.gardener.databinding.DialogWholePictureBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.ImageSliderAdapter

class FullImageDialogFragment(
    private val listOfPictureRef : List<StorageReference>
) : DialogFragment() {

    private lateinit var binding: DialogWholePictureBinding

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
        binding = DialogWholePictureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setImageSliderAdapter()
    }

    private fun setImageSliderAdapter() {
        binding.imageSlider.apply {
            setSliderAdapter(ImageSliderAdapter(requireContext(), listOfPictureRef))
            setIndicatorAnimation(IndicatorAnimationType.WORM)
            setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            indicatorSelectedColor = Color.WHITE;
            indicatorUnselectedColor = Color.GRAY
            scrollTimeInSec = 3
        }
    }


}