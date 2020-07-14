package pl.podwikagrzegorz.gardener.maps

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.content.ContextCompat
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
import pl.podwikagrzegorz.gardener.permissions.PermissionUtils
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.OutputStream

//TODO 1. wyszukiwarka, znajdowanie danej miejscowosci jesli sie da
// 2. FragmentAddGarden zmienic zolty FAB na normalny button
// 3. add isActive attr to the rest of variables in GardenRealm
class MapsActivity : AppCompatActivity(),
    OnMapReadyCallback,
    OnMyLocationButtonClickListener,
    OnRequestPermissionsResultCallback,
    OnCameraIdleListener {

    private lateinit var mMap: GoogleMap
    private val takeSnapshotButton: MaterialButton by lazy {
        findViewById<MaterialButton>(R.id.materialButton_take_snapshot)
    }
    private var currentCameraPosition: CameraPosition? = null
    private var snapshotFilePath: String = ""
    private var mPermissionDenied = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

        enableMyLocation()
        goToDefaultCoordinates()

        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnCameraIdleListener(this)
        setOnMyMapClickListener()
        setOnTakeSnapshotButtonListener()
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(
                this, MapsActivity.LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION, true
            )
        } else
            mMap.isMyLocationEnabled = true
    }

    private fun goToDefaultCoordinates() {
        val cracowPosition =
            CameraPosition.Builder().target(defaultCoordinates)
                .zoom(12.5f)
                .bearing(0f)
                .tilt(25f)
                .build()

/*        mMap.addMarker(
            MarkerOptions().position(defaultCoordinates).title("Marker in Cracow")
        )*/
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cracowPosition))
    }

    private fun setOnMyMapClickListener() {
        mMap.setOnMapClickListener { latLng ->
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)

            mMap.clear()
            mMap.addMarker(markerOptions)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.5F))
        }
    }

    private fun setOnTakeSnapshotButtonListener() {
        takeSnapshotButton.setOnClickListener {
            setOnSnapshotReadyCallback()
        }
    }

    private fun setOnSnapshotReadyCallback() {
        mMap.snapshot { bitmap ->

            snapshotFilePath = "${System.currentTimeMillis()}.png"
            val outputStream: OutputStream?

            try {
                outputStream = openFileOutput(snapshotFilePath, Context.MODE_PRIVATE)

                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
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

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (mPermissionDenied) {

            // Permission was not granted, display error dialog.
            showMissingPermissionError()
            mPermissionDenied = false
        }
    }

    private fun showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
            .newInstance(true).show(supportFragmentManager, "dialog")
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






    override fun onMyLocationButtonClick(): Boolean {
/*        if (currentCameraPosition != null) {
            val currentLatLng: LatLng = currentCameraPosition!!.target
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mMap.cameraPosition.target, 16f))*/

        return false
    }


    override fun onCameraIdle() {
        currentCameraPosition = mMap.cameraPosition
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
        val defaultCoordinates = LatLng(50.064651, 19.944981)
        const val CAPTIONED_SNAPSHOT_PATH = "CAPTIONED_SNAPSHOT_PATH"
        const val CAPTIONED_SNAPSHOT_LATITUDE = "CAPTIONED_SNAPSHOT_LATITUDE"
        const val CAPTIONED_SNAPSHOT_LONGITUDE = "CAPTIONED_SNAPSHOT_LONGITUDE"
    }
}
