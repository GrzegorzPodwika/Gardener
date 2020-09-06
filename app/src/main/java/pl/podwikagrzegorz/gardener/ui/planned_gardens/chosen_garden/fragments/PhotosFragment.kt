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
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.SmallPhotoAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets.FullImageDialogFragment
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

        val photoStorageReference : StorageReference = viewModel.getPhotoStorageReference()
        smallPhotoAdapter = SmallPhotoAdapter(options, photoStorageReference)
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
        observeOnPhotoClick()
    }

    private fun observeOnTakePhotoButton() {
        viewModel.eventOnTakePhoto.observe(viewLifecycleOwner, Observer { hasClicked ->
            if (hasClicked) {
                tryTakePhoto()
            }
        })
    }

    private fun observeOnPhotoClick() {
        viewModel.eventOnPhotoClick.observe(viewLifecycleOwner, {hasClicked ->
            if (hasClicked) {
                showFullResolutionImagesDialog()
            }
        })
    }



    private fun showFullResolutionImagesDialog() {
        if (viewModel.listOfPictureStorageRef.value != null) {
            FullImageDialogFragment(viewModel.listOfPictureStorageRef.value!!)
                .show(childFragmentManager, null)
        }
        viewModel.onPhotoClickComplete()
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
            viewModel.addPictureToList(absolutePicturePath)
        }
    }

    private fun setOnCompleteGardenButtonListener() {
        //TODO - zaimplementowac konczenie ogrodu kiedys tam
    }

    companion object {
        const val CAMERA_REQUEST_CODE = 1001
        const val MAX_NUMBER_OF_POSSIBLE_PICTURES = 12

        fun create(gardenTitle: String): PhotosFragment {
            val fragment = PhotosFragment()
            fragment.arguments = toBundle(gardenTitle)
            return fragment
        }
    }
}

/*
    private fun setOnTakePhotoButtonListener() {
        binding.materialButtonTakePhoto.setOnClickListener {
            val packageManager = ctx.packageManager
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        null
                    }

                    photoFile?.also {
                        val photoUri: Uri = FileProvider.getUriForFile(
                            ctx,
                            GardenerApp.WRITTEN_FILE_LOCATION,
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                        startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
                    }
                }
            }
        }
    }

    /*    private fun observePhotosListFromDb() {
        photosViewModel.listOfPicturesPaths.observe(
            viewLifecycleOwner,
            Observer { listOfPicturesPaths ->
                if (listOfPicturesPaths.size == MAX_NUMBER_OF_POSSIBLE_PICTURES) {
                    binding.materialButtonTakePhoto.isEnabled = false
                }

                updateListOfAbsolutePaths(listOfPicturesPaths)
                setReceivedPhotos()
            })
    }

    private fun updateListOfAbsolutePaths(listOfPicturesPaths: RealmList<String>) {
        listOfActualAbsolutePicturePaths = mutableListOf()

        for (path in listOfPicturesPaths) {
            listOfActualAbsolutePicturePaths.add(ctx.getAbsoluteFilePath(path))
        }
    }

    private fun setReceivedPhotos() {

        for (index in listOfActualAbsolutePicturePaths.indices) {
            val absolutePath = listOfActualAbsolutePicturePaths[index]

            when (index) {
                0 -> setUpImageView(binding.imageViewPhoto0, absolutePath)

                1 -> setUpImageView(binding.imageViewPhoto1, absolutePath)

                2 -> setUpImageView(binding.imageViewPhoto2, absolutePath)

                3 -> setUpImageView(binding.imageViewPhoto3, absolutePath)

                4 -> setUpImageView(binding.imageViewPhoto4, absolutePath)

                5 -> setUpImageView(binding.imageViewPhoto5, absolutePath)

                6 -> setUpImageView(binding.imageViewPhoto6, absolutePath)

                7 -> setUpImageView(binding.imageViewPhoto7, absolutePath)

                8 -> setUpImageView(binding.imageViewPhoto8, absolutePath)

                9 -> setUpImageView(binding.imageViewPhoto9, absolutePath)

                10 -> setUpImageView(binding.imageViewPhoto10, absolutePath)

                11 -> setUpImageView(binding.imageViewPhoto11, absolutePath)

            }
        }

    }

    private fun setUpImageView(imageView: ImageView, absolutePath: String) {
        imageView.load(File(absolutePath))
        imageView.setOnClickListener {
            val fullResolutionImageDialog =
                FullImageDialogFragment(listOfActualAbsolutePicturePaths)
            fullResolutionImageDialog.show(childFragmentManager, null)
        }
    }*/
*/
