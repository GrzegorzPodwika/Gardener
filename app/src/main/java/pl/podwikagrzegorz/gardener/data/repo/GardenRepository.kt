package pl.podwikagrzegorz.gardener.data.repo

import android.net.Uri
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import pl.podwikagrzegorz.gardener.data.domain.BasicGarden
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_GARDENS
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_IMAGES
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_ROOT
import pl.podwikagrzegorz.gardener.data.domain.FirebaseSource
import pl.podwikagrzegorz.gardener.extensions.deleteCaptionedImage
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class GardenRepository @Inject constructor(
    private val firebaseSource: FirebaseSource
) : Dao<BasicGarden> {

    private val gardenCollectionRef = firebaseSource.firestore
        .collection(FIREBASE_ROOT).document(firebaseSource.currentUser()!!.uid)
        .collection(FIREBASE_GARDENS)

    private val gardenPictureRef = firebaseSource.storage.reference
        .child(FIREBASE_ROOT)
        .child(firebaseSource.currentUser()!!.uid)
        .child(FIREBASE_IMAGES)

    override suspend fun insert(item: BasicGarden) {
        try {
            val file = Uri.fromFile(File(item.uniqueSnapshotName))
            gardenPictureRef.child("${file.lastPathSegment}").putFile(file).await()

            item.uniqueSnapshotName.deleteCaptionedImage()

            item.uniqueSnapshotName = file.lastPathSegment!!
            gardenCollectionRef.add(item).await()
            Timber.i("$item has been successfully inserted.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun update(documentId: String, newItem: BasicGarden) {
        try {
            gardenCollectionRef.document(documentId)
                .set(newItem, SetOptions.merge()).await()
            Timber.i("Note $documentId updated successfully with $newItem")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun delete(documentId: String) {
        try {
            gardenCollectionRef.document(documentId).delete().await()
            Timber.i("Garden $documentId deleted successfully.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun getQuery(): Query =
        gardenCollectionRef

    override fun getQuerySortedByName(): Query =
        gardenCollectionRef.orderBy(FIELD_GARDEN_TITLE)

    override fun getQuerySortedByTimestamp(): Query =
        gardenCollectionRef.orderBy(FIELD_TIMESTAMP)

    companion object {
        private const val FIELD_GARDEN_TITLE = "gardenTitle"
        private const val FIELD_TIMESTAMP = "timestamp"
    }
}