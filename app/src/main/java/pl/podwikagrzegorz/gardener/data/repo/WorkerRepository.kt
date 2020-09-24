package pl.podwikagrzegorz.gardener.data.repo

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import pl.podwikagrzegorz.gardener.data.domain.Worker
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_ROOT
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_WORKERS
import pl.podwikagrzegorz.gardener.data.domain.FirebaseSource
import timber.log.Timber
import javax.inject.Inject


class WorkerRepository @Inject constructor(
    private val firebaseSource: FirebaseSource
) : Dao<Worker>{
    private val workersCollectionRef = firebaseSource.firestore
        .collection(FIREBASE_ROOT).document(firebaseSource.currentUser()!!.uid)
        .collection(FIREBASE_WORKERS)

    override suspend fun insert(item: Worker) {
        try {
            workersCollectionRef.add(item).await()
            Timber.i("$item has been successfully inserted.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun update(documentId: String, newItem: Worker) {
        try {
            workersCollectionRef.document(documentId).set(newItem, SetOptions.merge()).await()
            Timber.i("Worker $documentId updated successfully with $newItem")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun delete(documentId: String) {
        try {
            workersCollectionRef.document(documentId).delete().await()
            Timber.i("Worker $documentId deleted successfully.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun getQuery(): Query =
        workersCollectionRef

    override fun getQuerySortedByName(): Query =
        workersCollectionRef.orderBy(FIELD_NAME)

    override fun getQuerySortedByTimestamp(): Query =
        workersCollectionRef.orderBy(FIELD_TIMESTAMP)

    suspend fun getAllWorkers() : List<Worker> {
        return withContext(Dispatchers.IO) {
            val listOfWorkers = mutableListOf<Worker>()

            try {
                val querySnapshot = workersCollectionRef.get().await()
                for (document in querySnapshot){
                    listOfWorkers.add(document.toObject())
                }
                Timber.i("Workers has been downloaded successfully.")
            } catch (e: Exception) {
                Timber.e(e)
            }

            listOfWorkers
        }
    }

    companion object {
        private const val FIELD_NAME = "name"
        private const val FIELD_TIMESTAMP = "timestamp"
    }

}