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
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.databinding.FragmentPhotosBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets.FullImageDialogFragment
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.GardenViewModelFactory
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.PhotosViewModel
import java.io.File
import java.io.IOException

// Photos
class PhotosFragment : Fragment() {
    private lateinit var binding: FragmentPhotosBinding
    private val viewModel: PhotosViewModel by viewModels {
        GardenViewModelFactory(
            gardenID
        )
    }
    private val gardenID: Long by lazy {
        PhotosViewModel.fromBundle(requireArguments())
    }

    private val fileProviderPath: File by lazy {
        requireContext().filesDir
    }
    private var uniquePhotoName: String = ""

    private val listener : OnClickItemListener = object : OnClickItemListener {
        override fun onClick(id: Long) {
            showFullResolutionImagesDialog()
        }
    }

    private fun showFullResolutionImagesDialog() {
        if (viewModel.listOfPicturesPaths.value != null) {
            FullImageDialogFragment(viewModel.listOfPicturesPaths.value!!)
                .show(childFragmentManager, null)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotosBinding.inflate(inflater, container, false)

        setUpViewModelWithBinding()
        observeOnTakePhotoButton()

        //setOnCompleteGardenButtonListener()

        return binding.root
    }

    private fun setUpViewModelWithBinding() {
        binding.apply {
            lifecycleOwner = this@PhotosFragment
            photosViewModel = viewModel
            onClickListener = listener
        }
    }

    private fun observeOnTakePhotoButton() {
        viewModel.eventOnTakePhoto.observe(viewLifecycleOwner, Observer { hasClicked ->
            if (hasClicked) {
                tryTakePhoto()
            }
        })
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
            viewModel.addPictureToList(uniquePhotoName)
        }
    }

    private fun setOnCompleteGardenButtonListener() {
        //TODO - zaimplementowac konczenie ogrodu kiedys tam
    }

    companion object {
        const val CAMERA_REQUEST_CODE = 1001
        const val MAX_NUMBER_OF_POSSIBLE_PICTURES = 12

        fun create(gardenID: Long): PhotosFragment {
            val fragment = PhotosFragment()
            fragment.arguments = PhotosViewModel.toBundle(gardenID)
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