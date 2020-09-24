package pl.podwikagrzegorz.gardener.extensions

import android.graphics.Paint
import android.net.Uri
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.api.load
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.card.MaterialCardView
import com.google.firebase.storage.StorageReference
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.di.GlideApp
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.fragments.PhotosFragment
import java.io.File

@BindingAdapter("loadImageViaName")
fun ImageView.loadImageWithName(uniqueName: String?) {
    if (!uniqueName.isNullOrEmpty()) {
        val absolutePath = context.getAbsoluteFilePath(uniqueName)
        load(File(absolutePath))
    }
}

@BindingAdapter("loadImageViaStorageRef")
fun loadImageViaStorageReference(imageView: ImageView, imageStorageRef: StorageReference?) {
    GlideApp.with(imageView.context)
        .load(imageStorageRef)
        .transition(DrawableTransitionOptions.withCrossFade())
        .placeholder(R.drawable.ic_place_holder)
        .into(imageView)
}

@BindingAdapter("loadImageViaUri")
fun loadImageViaUri(imageView : ImageView, uri: Uri?) {
    imageView.load(uri) {
        crossfade(true)
        placeholder(R.drawable.ic_place_holder)
    }
}

@BindingAdapter("loadIconIfIsGarden")
fun ImageView.loadIconDrawable(isGarden: Boolean) {
    if (isGarden) {
        setImageResource(R.drawable.ic_garden_park)
    } else {
        setImageResource(R.drawable.ic_wheelbarrow)
    }
}

@BindingAdapter("shouldBeGrayedOut")
fun MaterialCardView.shouldBeGrayedOut(isActive: Boolean) {
    if (isActive) {
        setCardBackgroundColor(GardenerApp.res.getColor(R.color.green_600, null))
    } else {
        setCardBackgroundColor(GardenerApp.res.getColor(R.color.colorGrayLight, null))
    }
}

@BindingAdapter("shouldBeCrossedOut")
fun shouldBeCrossedOut(textView: TextView, isActive: Boolean) {
    textView.apply {
        paintFlags = if (isActive) {
            paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        } else {
            paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        }
    }
}

@BindingAdapter("visibility")
fun Button.visibility(numberOfPhotos: Int) {
    isEnabled = numberOfPhotos < PhotosFragment.MAX_NUMBER_OF_POSSIBLE_PICTURES
}

