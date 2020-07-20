package pl.podwikagrzegorz.gardener.extensions

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import kotlin.math.min

class PictureUtils {
    companion object {
        fun getScaledBitmap(path: String, destWidth: Int, destHeight: Int) : Bitmap {
            val bmOptions = BitmapFactory.Options().apply {
                // Get the dimensions of the bitmap
                inJustDecodeBounds = true

                val photoW: Int = outWidth
                val photoH: Int = outHeight

                // Determine how much to scale down the image
                val scaleFactor: Int = min(photoW / destWidth, photoH / destHeight)

                // Decode the image file into a Bitmap sized to fill the View
                inJustDecodeBounds = false
                inSampleSize = scaleFactor
            }
            return BitmapFactory.decodeFile(path, bmOptions)
        }

        fun getScaledBitmap(path: String, activity: Activity) : Bitmap {
            val size = Point()
            activity.windowManager.defaultDisplay.getSize(size)
            return getScaledBitmap(path, size.x, size.y)
        }
    }
}