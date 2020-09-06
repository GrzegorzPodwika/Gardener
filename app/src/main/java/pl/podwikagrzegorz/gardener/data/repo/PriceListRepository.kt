package pl.podwikagrzegorz.gardener.data.repo

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import pl.podwikagrzegorz.gardener.data.domain.Note
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_PRICE_LIST
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_ROOT
import pl.podwikagrzegorz.gardener.data.domain.FirebaseSource
import timber.log.Timber
import javax.inject.Inject

class PriceListRepository @Inject constructor(
    firebaseSource: FirebaseSource
) : Dao<Note> {
    private val priceListCollectionRef = firebaseSource.firestore
        .collection(FIREBASE_ROOT).document(firebaseSource.currentUser()!!.uid)
        .collection(FIREBASE_PRICE_LIST)

    override suspend fun insert(item: Note) {
        try {
            //val newNoteDocument = priceListCollectionRef.document()
            priceListCollectionRef.add(item).await()
            Timber.i("$item has been successfully inserted.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun update(documentId: String, newItem: Note) {
        try {
            priceListCollectionRef.document(documentId)
                .set(newItem, SetOptions.merge()).await()
            Timber.i("Note $documentId updated successfully with $newItem")

        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun delete(documentId: String) {
        try {
            priceListCollectionRef.document(documentId)
                .delete().await()
            Timber.i("Note $documentId deleted successfully")

        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun getQuery(): Query =
        priceListCollectionRef

    override fun getQuerySortedByName(): Query =
        priceListCollectionRef.orderBy(FIELD_SERVICE)

    fun getQuerySortedByPrice(): Query =
        priceListCollectionRef.orderBy(FIELD_PRICE_OF_SERVICE)

    override fun getQuerySortedByTimestamp(): Query =
        priceListCollectionRef.orderBy(FIELD_TIMESTAMP)

    companion object {
        private const val FIELD_SERVICE = "service"
        private const val FIELD_PRICE_OF_SERVICE = "priceOfService"
        private const val FIELD_TIMESTAMP = "timestamp"
    }

}