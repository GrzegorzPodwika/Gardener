package pl.podwikagrzegorz.gardener.extensions

import android.net.Uri
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import coil.api.load
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.card.MaterialCardView
import com.google.firebase.storage.StorageReference
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
        setImageResource(R.drawable.ic_farm)
    } else {
        setImageResource(R.drawable.ic_lawn_mower)
    }
}

@BindingAdapter("shouldBeCrossed")
fun MaterialCardView.checkIfShouldBeCrossed(isActive: Boolean) {
    foreground = if (isActive) {
        null
    } else {
        ResourcesCompat.getDrawable(resources, R.drawable.stroke_foreground, null)
    }
}

@BindingAdapter("visibility")
fun Button.visibility(numberOfPhotos: Int) {
    isEnabled = numberOfPhotos < PhotosFragment.MAX_NUMBER_OF_POSSIBLE_PICTURES
}

