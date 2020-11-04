package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.data.domain.Picture
import pl.podwikagrzegorz.gardener.databinding.FragmentPhotosBinding
import pl.podwikagrzegorz.gardener.extensions.getAbsoluteFilePath
import pl.podwikagrzegorz.gardener.extensions.toBundle
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.OnProgressListener
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.SmallPhotoAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.dialogs_sheets.FullImageDialogFragment
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.PhotosViewModel
import java.io.File
import java.io.IOException

// Photos
@AndroidEntryPoint
class PhotosFragment : Fragment() {

    private lateinit var binding: FragmentPhotosBinding
    private val viewModel: PhotosViewModel by viewModels()
    private val fileProviderPath: File by lazy {
        requireContext().filesDir
    }
    private var uniquePhotoName: String = ""
    private lateinit var smallPhotoAdapter: SmallPhotoAdapter
    private val progressListener: OnProgressListener = object : OnProgressListener {
        override fun onStarted() {
            binding.progressBarUploadImage.visibility = View.VISIBLE
            binding.materialButtonTakePhoto.isEnabled = false
            binding.materialButtonCompleteGarden.isEnabled = false
        }

        override fun onProgress(progress: Double) {
            binding.progressBarUploadImage.progress = progress.toInt()
        }

        override fun onFinished() {
            binding.progressBarUploadImage.visibility = View.GONE
            binding.materialButtonTakePhoto.isEnabled = true
            binding.materialButtonCompleteGarden.isEnabled = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotosBinding.inflate(inflater, container, false)

        connectRecyclerViewWithQuery()
        setUpViewModelWithBinding()
        observeEventsFromViewModel()
        //setOnCompleteGardenButtonListener()

        return binding.root
    }

    private fun connectRecyclerViewWithQuery() {
        val options = FirestoreRecyclerOptions.Builder<Picture>()
            .setQuery(viewModel.getTakenPhotoQuerySortedByTimestamp(), Picture::class.java)
            .setLifecycleOwner(this)
            .build()

        val photoStorageReference: StorageReference = viewModel.getPhotoStorageReference()
        smallPhotoAdapter = SmallPhotoAdapter(options, photoStorageReference) {
            showFullResolutionImagesDialog()
        }
    }

    private fun setUpViewModelWithBinding() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            photosViewModel = viewModel
            recyclerViewTakenPhotos.adapter = smallPhotoAdapter
        }
    }

    private fun observeEventsFromViewModel() {
        observeOnTakePhotoButton()
    }

    private fun observeOnTakePhotoButton() {
        viewModel.eventOnTakePhoto.observe(viewLifecycleOwner, Observer { hasClicked ->
            if (hasClicked) {
                tryTakePhoto()
            }
        })
    }


    private fun showFullResolutionImagesDialog() {
        if (viewModel.listOfPictureStorageRef.value != null) {
            FullImageDialogFragment(viewModel.listOfPictureStorageRef.value!!)
                .show(childFragmentManager, null)
        }
    }


    private fun tryTakePhoto() {
        val packageManager = requireContext().packageManager
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }

                photoFile?.also {
                    val photoUri: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        GardenerApp.WRITTEN_FILE_LOCATION,
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
                }
            }
        }
        viewModel.onTakePhotoComplete()
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        uniquePhotoName = "${System.currentTimeMillis()}.jpg"

        return File(fileProviderPath, uniquePhotoName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val absolutePicturePath = requireContext().getAbsoluteFilePath(uniquePhotoName)
            viewModel.addPictureToList(absolutePicturePath, progressListener)
        }
    }

    private fun setOnCompleteGardenButtonListener() {
        //TODO - implement in the future complete garden
    }

    companion object {
        const val CAMERA_REQUEST_CODE = 1001
        const val MAX_NUMBER_OF_POSSIBLE_PICTURES = 12

        fun create(documentId: String): PhotosFragment {
            val fragment = PhotosFragment()
            fragment.arguments = toBundle(documentId)
            return fragment
        }
    }
}
