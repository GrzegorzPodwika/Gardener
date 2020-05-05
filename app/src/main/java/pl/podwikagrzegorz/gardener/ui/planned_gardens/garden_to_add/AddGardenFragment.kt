package pl.podwikagrzegorz.gardener.ui.planned_gardens.garden_to_add

import android.app.Activity.RESULT_OK
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.FragmentAddGardenBinding
import pl.podwikagrzegorz.gardener.maps.MapsActivity
import java.io.File

//TODO(zajac sie tym fragmentem)
class AddGardenFragment : Fragment() {

    private lateinit var gardenBinding : FragmentAddGardenBinding
    private var snapshotPath :String? = null
    private var snapshotLatitude :Double? = null
    private var snapshotLongitude :Double? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        gardenBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_garden, container, false)
        gardenBinding.materialButtonLocateGarden.setOnClickListener {
            val intent = Intent(context, MapsActivity::class.java)
            startActivityForResult(intent, REQUEST_TAKEN_SNAPSHOT_OK)
        }
        gardenBinding.fabConfirmAddingGarden.setOnClickListener {

        }
        return gardenBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //TODO zajac sie resezta pobrania snapshota
        if (requestCode == REQUEST_TAKEN_SNAPSHOT_OK){
            if (resultCode == RESULT_OK){
                val tmpSnapshotPath = data?.getStringExtra(MapsActivity.CAPTIONED_SNAPSHOT_PATH)
                snapshotLatitude = data?.getDoubleExtra(MapsActivity.CAPTIONED_SNAPSHOT_LATITUDE, MapsActivity.defaultCoordinates.latitude)
                snapshotLongitude = data?.getDoubleExtra(MapsActivity.CAPTIONED_SNAPSHOT_LONGITUDE, MapsActivity.defaultCoordinates.longitude)


                // getting absolute path to captioned snapshot
                val file: File = ContextWrapper(context).getFileStreamPath(tmpSnapshotPath)
                snapshotPath = file.absolutePath

                gardenBinding.shapeableImageViewPickedLocalization.setImageDrawable(Drawable.createFromPath(snapshotPath))
            }
        }
    }

    companion object{
        const val REQUEST_TAKEN_SNAPSHOT_OK = 1
    }
}
