package pl.podwikagrzegorz.gardener.data.repo

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import pl.podwikagrzegorz.gardener.data.domain.Machine
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_MACHINES
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_ROOT
import pl.podwikagrzegorz.gardener.data.domain.FirebaseSource
import timber.log.Timber
import javax.inject.Inject

class MachineRepository @Inject constructor(
    private val firebaseSource: FirebaseSource
) : Dao<Machine> {

    private val machineCollectionRef = firebaseSource.firestore
        .collection(FIREBASE_ROOT).document(firebaseSource.currentUser()!!.uid)
        .collection(FIREBASE_MACHINES)

    override suspend fun insert(item: Machine) {
        try {
            machineCollectionRef.add(item).await()
            Timber.i("$item has been successfully inserted.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun update(documentId: String, newItem: Machine) {
        try {
            machineCollectionRef.document(documentId)
                .set(newItem, SetOptions.merge()).await()
            Timber.i("Note $documentId updated successfully with $newItem")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun delete(documentId: String) {
        try {
            machineCollectionRef.document(documentId).delete().await()
            Timber.i("Note $documentId deleted successfully.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun getAllMachines(): List<Machine> =
        withContext(Dispatchers.IO) {
            val listOfMachines = mutableListOf<Machine>()

            try {
                val collectionOfMachines = machineCollectionRef.get().await()
                for (machine in collectionOfMachines) {
                    listOfMachines.add(machine.toObject<Machine>())
                }
            } catch (e: Exception) {
                Timber.e(e)
            }

            listOfMachines
        }

    override fun getQuery(): Query =
        machineCollectionRef

    override fun getQuerySortedByName(): Query =
        machineCollectionRef.orderBy(FIELD_MACHINE_NAME)

    fun getQueryByNumberOfMachines(): Query =
        machineCollectionRef.orderBy(FIELD_NUMBER_OF_MACHINES)

    override fun getQuerySortedByTimestamp(): Query =
        machineCollectionRef.orderBy(FIELD_TIMESTAMP)


    companion object {
        private const val FIELD_MACHINE_NAME = "machineName"
        private const val FIELD_NUMBER_OF_MACHINES = "numberOfMachines"
        private const val FIELD_TIMESTAMP = "timestamp"
    }

}