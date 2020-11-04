package pl.podwikagrzegorz.gardener.maps

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.FragmentMapsBinding
import pl.podwikagrzegorz.gardener.extensions.getAbsoluteFilePath
import pl.podwikagrzegorz.gardener.extensions.snackbar
import pl.podwikagrzegorz.gardener.permissions.PermissionUtils
import timber.log.Timber
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class MapsFragment : Fragment(), GoogleMap.OnMyLocationButtonClickListener,
    ActivityCompat.OnRequestPermissionsResultCallback,
    GoogleMap.OnCameraIdleListener {

    private lateinit var binding: FragmentMapsBinding
    private lateinit var map: GoogleMap
    private lateinit var geoCoder : Geocoder

    private var currentCameraPosition: CameraPosition? = null
    private var uniqueSnapshotName: String = ""
    private var mPermissionDenied = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_maps, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        //sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        mapFragment?.getMapAsync(callback)
    }

    override fun onResume() {
        super.onResume()
        if (mPermissionDenied) {

            // Permission was not granted, display error dialog.
            showMissingPermissionError()
            mPermissionDenied = false
        }
    }

    private fun showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
            .newInstance(true).show(childFragmentManager, "dialog")
    }

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        geoCoder = Geocoder(requireContext())

        enableMyLocation()
        goToDefaultCoordinates()
        map.setOnMyLocationButtonClickListener(this)
        map.setOnCameraIdleListener(this)
        setOnMyMapClickListener()
        setOnTakeSnapshotButtonListener()
        setOnEditorActionListener()
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(
                requireActivity() as AppCompatActivity,
                LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                true
            )
        } else
            map.isMyLocationEnabled = true
    }

    private fun goToDefaultCoordinates() {
        val cracowPosition =
            CameraPosition.Builder().target(defaultCoordinates)
                .zoom(12.5f)
                .bearing(0f)
                .tilt(25f)
                .build()

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cracowPosition))
    }

    private fun setOnMyMapClickListener() {
        map.setOnMapClickListener { latLng ->
            addMarker(latLng)
        }
    }

    private fun addMarker(latLng: LatLng) {
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        map.clear()
        map.addMarker(markerOptions)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.5F))
    }

    private fun setOnTakeSnapshotButtonListener() {
        binding.materialButtonTakeSnapshot.setOnClickListener {
            setOnSnapshotReadyCallback()
        }
    }

    private fun setOnSnapshotReadyCallback() {
        map.snapshot { bitmap ->

            uniqueSnapshotName = "${System.currentTimeMillis()}.jpg"
            val outputStream: OutputStream?

            try {
                val absolutePath = requireContext().getAbsoluteFilePath(uniqueSnapshotName)
                outputStream = FileOutputStream(absolutePath)

                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                outputStream.flush()
                outputStream.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (io: IOException) {
                io.printStackTrace()
            }

            prepareBundleAndNavigateBackToAddGardenFragment()
        }
    }

    private fun prepareBundleAndNavigateBackToAddGardenFragment() {
        Timber.i("Snasphot has been taken, name $uniqueSnapshotName")
        if (currentCameraPosition != null && uniqueSnapshotName.isNotEmpty()) {
            prepareBundle()
            findNavController().popBackStack()
        }
    }

    private fun prepareBundle() {
        val bundle = bundleOf(
            REQUEST_LATITUDE to currentCameraPosition!!.target.latitude,
            REQUEST_LONGITUDE to currentCameraPosition!!.target.longitude,
            REQUEST_TAKEN_SNAPSHOT_NAME to uniqueSnapshotName
        )
        setFragmentResult(KEY_DATA_FROM_MAPS_FRAGMENT, bundle)
    }

    private fun setOnEditorActionListener() {
        binding.editTextSearchLocation.isSingleLine = true
        binding.editTextSearchLocation.setOnEditorActionListener { _, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_DONE
                || keyEvent.action == KeyEvent.ACTION_DOWN
                || keyEvent.action == KeyEvent.KEYCODE_ENTER) {
                geoLocate()
            }
            false
        }
    }

    private fun geoLocate() {
        val searchPlace = binding.editTextSearchLocation.text.toString()

        var listOfAddresses : List<Address> = mutableListOf()

        try {
            listOfAddresses = geoCoder.getFromLocationName(searchPlace, 1)
        } catch (ex : IOException) {
            Timber.e(ex)
            binding.root.snackbar(getString(R.string.searching_location_error))
        }

        if (listOfAddresses.isNotEmpty()) {
            val searchedAddress = listOfAddresses[0]
            Timber.i("Map geoLocate: $searchedAddress")
            moveToSearchedAddress(searchedAddress)

        }
    }

    private fun moveToSearchedAddress(searchedAddress: Address) {
        val latLng = LatLng(searchedAddress.latitude, searchedAddress.longitude)
        addMarker(latLng)
    }

    override fun onMyLocationButtonClick(): Boolean {
        return false
    }

    override fun onCameraIdle() {
        currentCameraPosition = map.cameraPosition
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
        val defaultCoordinates = LatLng(50.064651, 19.944981)

        const val KEY_DATA_FROM_MAPS_FRAGMENT = "KEY_DATA_FROM_MAPS_FRAGMENT"
        const val REQUEST_LATITUDE = "TAKEN_LATITUDE"
        const val REQUEST_LONGITUDE = "TAKEN_LONGITUDE"
        const val REQUEST_TAKEN_SNAPSHOT_NAME = "TAKEN_SNAPSHOT_NAME"
    }
}