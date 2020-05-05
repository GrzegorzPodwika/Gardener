package pl.podwikagrzegorz.gardener.maps

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import pl.podwikagrzegorz.gardener.R
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.OutputStream

class MapsActivity : AppCompatActivity(),
    OnMapReadyCallback,
    OnMyLocationButtonClickListener,
    OnRequestPermissionsResultCallback,
    OnCameraIdleListener {

    private lateinit var mMap: GoogleMap
    private lateinit var takeSnapshotButton: MaterialButton
    private var currentCameraPosition: CameraPosition? = null
    private var snapshotFilePath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        takeSnapshotButton = findViewById(R.id.materialButton_take_snapshot)
        takeSnapshotButton.setOnClickListener {
            takeSnapshot()
        }
    }

    private fun takeSnapshot() {
        mMap.snapshot { bitmap ->
            val b = bitmap

            snapshotFilePath = "${System.currentTimeMillis()}.png"
            var outputStream: OutputStream? = null

            try {
                outputStream = openFileOutput(snapshotFilePath, Context.MODE_PRIVATE)

                b.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
                outputStream.flush()
                outputStream.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (io: IOException) {
                io.printStackTrace()
            }

            closeActivity()
        }
    }

    private fun closeActivity() {
        val resultIntent = Intent()

        if (currentCameraPosition != null && snapshotFilePath != "") {
            resultIntent.putExtra(
                CAPTIONED_SNAPSHOT_LATITUDE,
                currentCameraPosition?.target?.latitude
            )
            resultIntent.putExtra(
                CAPTIONED_SNAPSHOT_LONGITUDE,
                currentCameraPosition?.target?.longitude
            )
            resultIntent.putExtra(
                CAPTIONED_SNAPSHOT_PATH,
                snapshotFilePath
            )
            setResult(Activity.RESULT_OK, resultIntent)
        } else {
            if (snapshotFilePath != "") deleteCaptionedSnapshot(snapshotFilePath)
            setResult(Activity.RESULT_CANCELED, resultIntent)
        }

        finish()
    }

    private fun deleteCaptionedSnapshot(filePath: String) {

        val fileToDelete = File(filePath)
        if (fileToDelete.exists()) {
            if (fileToDelete.delete()) {
                Toast.makeText(
                    baseContext,
                    "file Deleted :$filePath",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    baseContext,
                    "file not Deleted :$filePath",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        goToDefaultCoordinates()

        mMap.setOnMyLocationButtonClickListener { true }
        mMap.setOnMyLocationClickListener { true }


        setOnMyMapClickListener()
    }

    private fun setOnMyMapClickListener() {
        mMap.setOnMapClickListener { latLng ->
            val markerOptions = MarkerOptions()

            markerOptions.position(latLng)

            mMap.clear()

            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))

            mMap.addMarker(markerOptions)
        }
    }

    private fun goToDefaultCoordinates() {
        val KRAKOW =
            CameraPosition.Builder().target(defaultCoordinates)
                .zoom(12.5f)
                .bearing(0f)
                .tilt(25f)
                .build()

        mMap.addMarker(
            MarkerOptions().position(defaultCoordinates).title("Marker in Krakow")
        )
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(KRAKOW))
    }


    override fun onMyLocationButtonClick(): Boolean {
        if (currentCameraPosition != null) {
            val currentLatLng: LatLng = currentCameraPosition!!.target
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
        }

        return false
    }

    override fun onCameraIdle() {
        currentCameraPosition = mMap.cameraPosition
    }

    companion object {
        val defaultCoordinates = LatLng(50.064651, 19.944981)
        const val CAPTIONED_SNAPSHOT_PATH = "CAPTIONED_SNAPSHOT_PATH"
        const val CAPTIONED_SNAPSHOT_LATITUDE = "CAPTIONED_SNAPSHOT_LATITUDE"
        const val CAPTIONED_SNAPSHOT_LONGITUDE = "CAPTIONED_SNAPSHOT_LONGITUDE"
    }
}
