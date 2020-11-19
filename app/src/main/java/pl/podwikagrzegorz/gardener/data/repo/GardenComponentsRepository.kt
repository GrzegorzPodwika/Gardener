package pl.podwikagrzegorz.gardener.data.repo

import android.net.Uri
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import pl.podwikagrzegorz.gardener.data.domain.*
import pl.podwikagrzegorz.gardener.extensions.Constants
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_DESCRIPTIONS
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_MAN_HOURS
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_MAP_OF_MAN_HOURS
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_NOTES
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_SHOPPING
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_TAKEN_MACHINES
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_TAKEN_PICTURES
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_TAKEN_PROPERTIES
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_TAKEN_TOOLS
import pl.podwikagrzegorz.gardener.extensions.deleteCaptionedImage
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.OnProgressListener
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class GardenComponentsRepository @Inject constructor(
    private val firebaseSource: FirebaseSource
) {
    private val gardenCollectionRef = firebaseSource.firestore
        .collection(Constants.FIREBASE_ROOT).document(firebaseSource.currentUser()!!.uid)
        .collection(Constants.FIREBASE_GARDENS)

    val gardenPictureRef = firebaseSource.storage.reference
        .child(Constants.FIREBASE_ROOT)
        .child(firebaseSource.currentUser()!!.uid)
        .child(Constants.FIREBASE_IMAGES)

    var listener: OnExecuteTransactionListener? = null

    /**
     * 1. Basic Garden
     */
    suspend fun getBasicGarden(documentId: String): BasicGarden {
        var basicGarden = BasicGarden()

        try {
            val documentSnapshot = gardenCollectionRef.document(documentId).get().await()

            val bs = documentSnapshot.toObject<BasicGarden>()
            bs?.let {
                basicGarden = it
            }

        } catch (e: Exception) {
            Timber.e(e)
        }

        return basicGarden
    }

    suspend fun getMapSnapshotStorageRef(documentId: String): StorageReference? {

        try {
            val documentSnapshot = gardenCollectionRef.document(documentId).get().await()
            val basicGarden = documentSnapshot.toObject<BasicGarden>()

            basicGarden?.let {
                return gardenPictureRef.child(it.uniqueSnapshotName)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }

        return null
    }

    /**
     * 2. List of descriptions
     */
    suspend fun insertDescription(documentId: String, description: ActiveString) {
        try {
            gardenCollectionRef.document(documentId).collection(FIREBASE_DESCRIPTIONS)
                .add(description).await()
            Timber.i("$description has been successfully inserted.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun reverseFlagOnDescription(
        documentId: String,
        childDocumentId: String,
        isActive: Boolean
    ) {
        try {
            val docRef = gardenCollectionRef.document(documentId)
                .collection(FIREBASE_DESCRIPTIONS).document(childDocumentId)
            docRef.update(FIELD_IS_ACTIVE, !isActive).await()

            Timber.i("$childDocumentId has been successfully changed.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun updateDescription(
        documentId: String,
        childDocumentId: String,
        newDescription: ActiveString
    ) {
        try {
            gardenCollectionRef.document(documentId)
                .collection(FIREBASE_DESCRIPTIONS).document(childDocumentId)
                .set(newDescription, SetOptions.merge()).await()
            Timber.i("$childDocumentId has been successfully deleted.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }


    suspend fun deleteDescriptionFromList(documentId: String, childDocumentId: String) {
        try {
            gardenCollectionRef.document(documentId)
                .collection(FIREBASE_DESCRIPTIONS).document(childDocumentId)
                .delete().await()
            Timber.i("$childDocumentId has been successfully deleted.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun getDescriptionQuery(documentId: String): Query =
        gardenCollectionRef.document(documentId).collection(FIREBASE_DESCRIPTIONS)

    fun getDescriptionQuerySortedByTimestamp(documentId: String): Query =
        gardenCollectionRef.document(documentId).collection(FIREBASE_DESCRIPTIONS)
            .orderBy(FIELD_TIMESTAMP)

    fun getDescriptionQuerySortedByActivity(documentId: String): Query =
        gardenCollectionRef.document(documentId).collection(FIREBASE_DESCRIPTIONS)
            .orderBy(FIELD_IS_ACTIVE, Query.Direction.DESCENDING).orderBy(FIELD_TIMESTAMP)

    /**
     * 3. List of notes
     */
    suspend fun insertNote(documentId: String, note: ActiveString) {
        try {
            gardenCollectionRef.document(documentId).collection(FIREBASE_NOTES).add(note).await()
            Timber.i("$note has been successfully inserted.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun reverseFlagOnNote(documentId: String, childDocumentId: String, isActive: Boolean) {
        try {
            val docRef = gardenCollectionRef.document(documentId)
                .collection(FIREBASE_NOTES).document(childDocumentId)
            docRef.update(FIELD_IS_ACTIVE, !isActive).await()

            Timber.i("$childDocumentId has been successfully changed.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun updateNote(documentId: String, childDocumentId: String, newNote: ActiveString) {
        try {
            gardenCollectionRef.document(documentId)
                .collection(FIREBASE_NOTES).document(childDocumentId)
                .set(newNote, SetOptions.merge()).await()
            Timber.i("$childDocumentId has been successfully deleted.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun deleteNoteFromList(documentId: String, childDocumentId: String) {
        try {
            gardenCollectionRef.document(documentId)
                .collection(FIREBASE_NOTES).document(childDocumentId)
                .delete().await()
            Timber.i("$childDocumentId has been successfully deleted.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun getNoteQuery(documentId: String): Query =
        gardenCollectionRef.document(documentId).collection(FIREBASE_NOTES)

    fun getNoteQuerySortedByTimestamp(documentId: String): Query =
        gardenCollectionRef.document(documentId).collection(FIREBASE_NOTES)
            .orderBy(FIELD_TIMESTAMP)

    fun getNoteQuerySortedByActivity(documentId: String): Query =
        gardenCollectionRef.document(documentId).collection(FIREBASE_NOTES)
            .orderBy(FIELD_IS_ACTIVE, Query.Direction.DESCENDING).orderBy(FIELD_TIMESTAMP)

    /**
     * 4. List of tools
     */
    suspend fun addListOfPickedTools(documentId: String, listOfPickedTools: List<Item>) {
        try {
            for (tool in listOfPickedTools) {
                gardenCollectionRef.document(documentId)
                    .collection(FIREBASE_TAKEN_TOOLS).add(tool).await()
            }
            Timber.i("ListOfPickedTools has been successfully inserted.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun reverseFlagOnTool(documentId: String, childDocumentId: String, isActive: Boolean) {
        try {
            val docRef = gardenCollectionRef.document(documentId)
                .collection(FIREBASE_TAKEN_TOOLS).document(childDocumentId)
            docRef.update(FIELD_IS_ACTIVE, !isActive).await()

            Timber.i("$childDocumentId has been successfully changed.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun updateNumberOfTools(
        documentId: String,
        childDocumentId: String,
        chosenNumber: Int
    ) {
        try {
            val docRef = gardenCollectionRef.document(documentId)
                .collection(FIREBASE_TAKEN_TOOLS).document(childDocumentId)
            docRef.update(FIELD_NUMBER_OF_ITEMS, chosenNumber).await()

            Timber.i("$childDocumentId has been successfully changed.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun deleteToolFromList(documentId: String, childDocumentId: String) {
        try {
            gardenCollectionRef.document(documentId)
                .collection(FIREBASE_TAKEN_TOOLS).document(childDocumentId)
                .delete().await()
            Timber.i("$childDocumentId has been successfully deleted.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun getTakenToolsQuery(documentId: String): Query =
        gardenCollectionRef.document(documentId).collection(FIREBASE_TAKEN_TOOLS)

    fun getTakenToolsQuerySortedByTimestamp(documentId: String): Query =
        gardenCollectionRef.document(documentId).collection(FIREBASE_TAKEN_TOOLS)
            .orderBy(FIELD_TIMESTAMP)

    fun getTakenToolsQuerySortedByActivity(documentId: String): Query =
        gardenCollectionRef.document(documentId).collection(FIREBASE_TAKEN_TOOLS)
            .orderBy(FIELD_IS_ACTIVE, Query.Direction.DESCENDING).orderBy(FIELD_TIMESTAMP)

    /**
     * 5. List of machines
     */
    suspend fun addListOfPickedMachines(documentId: String, listOfPickedMachines: List<Item>) {
        try {
            for (machine in listOfPickedMachines) {
                gardenCollectionRef.document(documentId)
                    .collection(FIREBASE_TAKEN_MACHINES).add(machine).await()
            }
            Timber.i("ListOfPickedMachines has been successfully inserted.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun reverseFlagOnMachine(
        documentId: String,
        childDocumentId: String,
        isActive: Boolean
    ) {
        try {
            val docRef = gardenCollectionRef.document(documentId)
                .collection(FIREBASE_TAKEN_MACHINES).document(childDocumentId)
            docRef.update(FIELD_IS_ACTIVE, !isActive).await()

            Timber.i("$childDocumentId has been successfully changed.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun updateNumberOfMachines(
        documentId: String,
        childDocumentId: String,
        chosenNumber: Int
    ) {
        try {
            val docRef = gardenCollectionRef.document(documentId)
                .collection(FIREBASE_TAKEN_MACHINES).document(childDocumentId)
            docRef.update(FIELD_NUMBER_OF_ITEMS, chosenNumber).await()

            Timber.i("$childDocumentId has been successfully changed.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun deleteMachineFromList(documentId: String, childDocumentId: String) {
        try {
            gardenCollectionRef.document(documentId)
                .collection(FIREBASE_TAKEN_MACHINES).document(childDocumentId)
                .delete().await()
            Timber.i("$childDocumentId has been successfully deleted.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun getTakenMachinesQuery(documentId: String): Query =
        gardenCollectionRef.document(documentId).collection(FIREBASE_TAKEN_MACHINES)

    fun getTakenMachinesQuerySortedByTimestamp(documentId: String): Query =
        gardenCollectionRef.document(documentId).collection(FIREBASE_TAKEN_MACHINES)
            .orderBy(FIELD_TIMESTAMP)

    fun getTakenMachinesQuerySortedByActivity(documentId: String): Query =
        gardenCollectionRef.document(documentId).collection(FIREBASE_TAKEN_MACHINES)
            .orderBy(FIELD_IS_ACTIVE, Query.Direction.DESCENDING).orderBy(FIELD_TIMESTAMP)

    /**
     * 6. List of properties
     */
    suspend fun addListOfPickedProperties(
        documentId: String,
        listOfPickedProperties: List<ActiveProperty>
    ) {
        try {
            for (activeProperty in listOfPickedProperties) {
                gardenCollectionRef.document(documentId)
                    .collection(FIREBASE_TAKEN_PROPERTIES).add(activeProperty).await()
            }
            Timber.i("ListOfPickedMachines has been successfully inserted.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun reverseFlagOnProperty(
        documentId: String,
        childDocumentId: String,
        isActive: Boolean
    ) {
        try {
            val docRef = gardenCollectionRef.document(documentId)
                .collection(FIREBASE_TAKEN_PROPERTIES).document(childDocumentId)
            docRef.update(FIELD_IS_ACTIVE, !isActive).await()

            Timber.i("$childDocumentId has been successfully changed.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

/*    suspend fun updateNumberOfProperties(documentId: String, childDocumentId: String, chosenNumber: Int) {
        try {
            val docRef = gardenCollectionRef.document(documentId)
                .collection(FIREBASE_TAKEN_PROPERTIES).document(childDocumentId)
            docRef.update(FIELD_NUMBER_OF_ITEMS, chosenNumber).await()

            Timber.i("$childDocumentId has been successfully changed.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }*/

    suspend fun deletePropertyFromList(documentId: String, childDocumentId: String) {
        try {
            gardenCollectionRef.document(documentId)
                .collection(FIREBASE_TAKEN_PROPERTIES).document(childDocumentId)
                .delete().await()
            Timber.i("$childDocumentId has been successfully deleted.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun getTakenPropertiesQuery(documentId: String): Query =
        gardenCollectionRef.document(documentId).collection(FIREBASE_TAKEN_PROPERTIES)

    fun getTakenPropertiesQuerySortedByTimestamp(documentId: String): Query =
        gardenCollectionRef.document(documentId).collection(FIREBASE_TAKEN_PROPERTIES)
            .orderBy(FIELD_TIMESTAMP)

    fun getTakenPropertiesQuerySortedByActivity(documentId: String): Query =
        gardenCollectionRef.document(documentId).collection(FIREBASE_TAKEN_PROPERTIES)
            .orderBy(FIELD_IS_ACTIVE, Query.Direction.DESCENDING).orderBy(FIELD_TIMESTAMP)

    /**
     * 7. Map of manHours
     */
    suspend fun addListOfPickedWorkers(documentId: String, listOfPickedWorkers: List<Worker>) {
        try {
            val collRef =
                gardenCollectionRef.document(documentId).collection(FIREBASE_MAP_OF_MAN_HOURS)

            for (worker in listOfPickedWorkers) {
                val searchQuery = collRef.whereEqualTo(FIELD_NAME, worker.name).get().await()

                if (searchQuery.isEmpty) {
                    collRef.add(worker).await()

                }
                //Timber.i("Worker = ${worker.name} documentId ${worker.documentId}")

            }

            listener?.onTransactionSuccess()
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun addListOfWorkedHoursWithPickedDate(
        documentId: String,
        mapOfManHours: Map<String, ManHours>
    ) {
        try {
            val collRef =
                gardenCollectionRef.document(documentId).collection(FIREBASE_MAP_OF_MAN_HOURS)
            for ((childDocumentId, workedHours) in mapOfManHours) {
                collRef.document(childDocumentId).collection(FIREBASE_MAN_HOURS)
                    .add(workedHours).await()
            }
            listener?.onTransactionSuccess()
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun updateNumberOfManHours(
        documentId: String,
        workerDocumentId: String,
        manHoursDocumentId: String,
        updatedManHours: ManHours
    ) {
        try {
            val collRef =
                gardenCollectionRef.document(documentId).collection(FIREBASE_MAP_OF_MAN_HOURS)
                    .document(workerDocumentId).collection(FIREBASE_MAN_HOURS)

            collRef.document(manHoursDocumentId)
                .set(updatedManHours, SetOptions.merge()).await()
            Timber.i("Update of concrete man-hours has been successfully performed.")

            listener?.onTransactionSuccess()
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun deleteParentWorkerWithManHours(documentId: String, parentWorkerDocumentId: String) {
        try {
            val collRef = gardenCollectionRef.document(documentId).collection(FIREBASE_MAP_OF_MAN_HOURS)
                .document(parentWorkerDocumentId).collection(FIREBASE_MAN_HOURS)

            //delete whole subcollection
            val allManHoursQuery = collRef.get().await()
            for (manHours in allManHoursQuery) {
                val manHoursObject = manHours.toObject<ManHours>()
                collRef.document(manHoursObject.documentId).delete().await()
            }

            //at the end delete document
            gardenCollectionRef.document(documentId).collection(FIREBASE_MAP_OF_MAN_HOURS)
                .document(parentWorkerDocumentId).delete().await()

            listener?.onTransactionSuccess()
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun deleteConcreteManHours(
        documentId: String,
        workerDocumentId: String,
        manHoursDocumentId: String,
    ) {
        try {
            val collRef =
                gardenCollectionRef.document(documentId).collection(FIREBASE_MAP_OF_MAN_HOURS)
                    .document(workerDocumentId).collection(FIREBASE_MAN_HOURS)

            collRef.document(manHoursDocumentId)
                .delete().await()
            Timber.i("Delete of concrete man-hours has been successfully performed.")

            listener?.onTransactionSuccess()
        } catch (e: Exception) {
            Timber.e(e)
        }
    }


    suspend fun getMapOfManHours(documentId: String): List<ManHoursMap> {
        return withContext(Dispatchers.IO) {
            val listOfManHoursMap = mutableListOf<ManHoursMap>()
            //val listOfWorkers = mutableListOf<Worker>()

            try {
                val querySnapshot =
                    gardenCollectionRef.document(documentId).collection(FIREBASE_MAP_OF_MAN_HOURS)
                        .orderBy(FIELD_TIMESTAMP).get().await()

                for (workerQuery in querySnapshot) {
                    val worker = workerQuery.toObject<Worker>()
                    val listOfManHours = mutableListOf<ManHours>()
                    val manHoursSnapshot =
                        gardenCollectionRef.document(documentId).collection(FIREBASE_MAP_OF_MAN_HOURS)
                            .document(worker.documentId).collection(FIREBASE_MAN_HOURS).get().await()
                    manHoursSnapshot.forEach{ manHoursQuery->
                        listOfManHours.add(manHoursQuery.toObject())
                    }

                    listOfManHoursMap.add(ManHoursMap(worker, listOfManHours))
                }
            } catch (e: Exception) {
                Timber.e(e)
            }

            listOfManHoursMap
        }
    }

    /**
     * 8. List od shopping notes
     */
    suspend fun insertShoppingNote(documentId: String, shoppingNote: ActiveString) {
        try {
            gardenCollectionRef.document(documentId).collection(FIREBASE_SHOPPING)
                .add(shoppingNote).await()
            Timber.i("$shoppingNote has been successfully inserted.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun reverseFlagOnShoppingNote(
        documentId: String,
        childDocumentId: String,
        isActive: Boolean
    ) {
        try {
            val docRef = gardenCollectionRef.document(documentId)
                .collection(FIREBASE_SHOPPING).document(childDocumentId)
            docRef.update(FIELD_IS_ACTIVE, !isActive).await()

            Timber.i("$childDocumentId has been successfully changed.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun updateShoppingNote(
        documentId: String,
        childDocumentId: String,
        newShoppingNote: ActiveString
    ) {
        try {
            gardenCollectionRef.document(documentId)
                .collection(FIREBASE_SHOPPING).document(childDocumentId)
                .set(newShoppingNote, SetOptions.merge()).await()
            Timber.i("$childDocumentId has been successfully deleted.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun deleteShoppingNoteFromList(documentId: String, childDocumentId: String) {
        try {
            gardenCollectionRef.document(documentId)
                .collection(FIREBASE_SHOPPING).document(childDocumentId)
                .delete().await()
            Timber.i("$childDocumentId has been successfully deleted.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun getShoppingNotesQuery(documentId: String): Query =
        gardenCollectionRef.document(documentId).collection(FIREBASE_SHOPPING)

    fun getShoppingNotesQuerySortedByActivity(documentId: String): Query =
        gardenCollectionRef.document(documentId).collection(FIREBASE_SHOPPING)
            .orderBy(FIELD_IS_ACTIVE, Query.Direction.DESCENDING).orderBy(FIELD_TIMESTAMP)

    /**
     * 8. List od shopping notes
     */
    suspend fun insertPictureToList(
        documentId: String,
        absolutePath: String,
        listener: OnProgressListener
    ) {
        try {
            val uri = Uri.fromFile(File(absolutePath))

            withContext(Dispatchers.Main) {
                listener.onStarted()
            }
            gardenPictureRef.child(uri.lastPathSegment!!).putFile(uri).await()
            withContext(Dispatchers.Main) {
                listener.onFinished()
            }

            Timber.i("Image with uri $uri has been successfully inserted to STORAGE.")

            absolutePath.deleteCaptionedImage()

            val picture = Picture(uri.lastPathSegment!!)
            gardenCollectionRef.document(documentId).collection(FIREBASE_TAKEN_PICTURES)
                .add(picture).await()
            Timber.i("Image unique name $picture has been successfully inserted.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun getListOfPictureRef(documentId: String): List<StorageReference> {
        return withContext(Dispatchers.IO) {
            val listOfPictureRef = mutableListOf<StorageReference>()

            try {
                val querySnapshot = gardenCollectionRef.document(documentId)
                    .collection(FIREBASE_TAKEN_PICTURES).orderBy(FIELD_TIMESTAMP).get().await()

                for (document in querySnapshot) {
                    val picture = document.toObject<Picture>()
                    val imageRef = gardenPictureRef.child(picture.uniquePictureName)
                    listOfPictureRef.add(imageRef)
                }
                Timber.i("Size of listOfPictureUris: ${listOfPictureRef.size}")
            } catch (e: Exception) {
                Timber.e(e)
            }

            listOfPictureRef
        }
    }

    fun getTakenPhotoQuerySortedByTimestamp(documentId: String): Query =
        gardenCollectionRef.document(documentId).collection(FIREBASE_TAKEN_PICTURES)
            .orderBy(FIELD_TIMESTAMP)


    companion object {
        private const val FIELD_GARDEN_TITLE = "gardenTitle"
        private const val FIELD_TIMESTAMP = "timestamp"
        private const val FIELD_LIST_OF_WORKED_HOURS = "listOfManHours"
        private const val FIELD_WORKER_NAME = "workerFullName"
        private const val FIELD_NAME = "name"

        private const val FIELD_IS_ACTIVE = "active"
        private const val FIELD_NUMBER_OF_ITEMS = "numberOfItems"
    }
}