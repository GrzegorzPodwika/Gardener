package pl.podwikagrzegorz.gardener.data.repo

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import pl.podwikagrzegorz.gardener.data.domain.Property
import pl.podwikagrzegorz.gardener.extensions.Constants
import pl.podwikagrzegorz.gardener.data.domain.FirebaseSource
import timber.log.Timber
import javax.inject.Inject

class PropertyRepository @Inject constructor(
    private val firebaseSource: FirebaseSource
) : Dao<Property>{

    private val propertyCollectionRef = firebaseSource.firestore
        .collection(Constants.FIREBASE_ROOT).document(firebaseSource.currentUser()!!.uid)
        .collection(Constants.FIREBASE_PROPERTIES)

    override suspend fun insert(item: Property) {
        try {
            propertyCollectionRef.add(item).await()
            Timber.i("$item has been successfully inserted.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun update(documentId: String, newItem: Property) {
        try {
            propertyCollectionRef.document(documentId)
                .set(newItem, SetOptions.merge()).await()
            Timber.i("Note $documentId updated successfully with $newItem")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun delete(documentId: String) {
        try {
            propertyCollectionRef.document(documentId).delete().await()
            Timber.i("Note $documentId deleted successfully.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun getAllProperties(): List<Property> =
        withContext(Dispatchers.IO) {
            val listOfProperties = mutableListOf<Property>()

            try {
                val querySnapshotOfProperties = propertyCollectionRef.get().await()
                for (property in querySnapshotOfProperties) {
                    listOfProperties.add(property.toObject())
                }
            } catch (e: Exception) {
                Timber.e(e)
            }

            listOfProperties
        }

    override fun getQuery(): Query =
        propertyCollectionRef

    override fun getQuerySortedByName(): Query =
        propertyCollectionRef.orderBy(FIELD_PROPERTY_NAME)

    fun getQuerySortedByNumberOfProperties() : Query =
        propertyCollectionRef.orderBy(FIELD_NUMBER_OF_PROPERTIES)

    override fun getQuerySortedByTimestamp(): Query =
        propertyCollectionRef.orderBy(FIELD_TIMESTAMP)

    companion object {
        private const val FIELD_PROPERTY_NAME = "propertyName"
        private const val FIELD_NUMBER_OF_PROPERTIES = "numberOfProperties"
        private const val FIELD_TIMESTAMP = "timestamp"
    }
}