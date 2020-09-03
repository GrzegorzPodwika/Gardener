package pl.podwikagrzegorz.gardener.data.repo

import android.net.Uri
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import pl.podwikagrzegorz.gardener.data.domain.*
import pl.podwikagrzegorz.gardener.extensions.Constants
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_DESCRIPTIONS
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_MAP_OF_MAN_HOURS
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_NOTES
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_SHOPPING
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_TAKEN_MACHINES
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_TAKEN_PICTURES
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_TAKEN_PROPERTIES
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_TAKEN_TOOLS
import pl.podwikagrzegorz.gardener.data.domain.FirebaseSource
import pl.podwikagrzegorz.gardener.extensions.deleteCaptionedImage
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

    suspend fun reverseFlagOnDescription(documentId: String, childDocumentId: String) {
        try {
            firebaseSource.firestore.runTransaction { transaction ->
                val docRef = gardenCollectionRef.document(documentId)
                    .collection(FIREBASE_DESCRIPTIONS).document(childDocumentId)
                val activeString = transaction.get(docRef)
                var reversedActivity = activeString[FIELD_IS_ACTIVE] as Boolean
                reversedActivity = !reversedActivity
                transaction.update(docRef, FIELD_IS_ACTIVE, reversedActivity)
                null
            }.await()

            Timber.i("$childDocumentId has been successfully changed.")
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

    suspend fun reverseFlagOnNote(documentId: String, childDocumentId: String) {
        try {
            firebaseSource.firestore.runTransaction { transaction ->
                val docRef = gardenCollectionRef.document(documentId)
                    .collection(FIREBASE_NOTES).document(childDocumentId)
                val activeString = transaction.get(docRef)
                var reversedActivity = activeString[FIELD_IS_ACTIVE] as Boolean
                reversedActivity = !reversedActivity
                transaction.update(docRef, FIELD_IS_ACTIVE, reversedActivity)
                null
            }.await()

            Timber.i("$childDocumentId has been successfully changed.")
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

    suspend fun reverseFlagOnTool(documentId: String, childDocumentId: String) {
        try {
            firebaseSource.firestore.runTransaction { transaction ->
                val docRef = gardenCollectionRef.document(documentId)
                    .collection(FIREBASE_TAKEN_TOOLS).document(childDocumentId)
                val activeString = transaction.get(docRef)
                var reversedActivity = activeString[FIELD_IS_ACTIVE] as Boolean
                reversedActivity = !reversedActivity
                transaction.update(docRef, FIELD_IS_ACTIVE, reversedActivity)
                null
            }.await()

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

    suspend fun reverseFlagOnMachine(documentId: String, childDocumentId: String) {
        try {
            firebaseSource.firestore.runTransaction { transaction ->
                val docRef = gardenCollectionRef.document(documentId)
                    .collection(FIREBASE_TAKEN_MACHINES).document(childDocumentId)
                val activeString = transaction.get(docRef)
                var reversedActivity = activeString[FIELD_IS_ACTIVE] as Boolean
                reversedActivity = !reversedActivity
                transaction.update(docRef, FIELD_IS_ACTIVE, reversedActivity)
                null
            }.await()

            Timber.i("$childDocumentId has been successfully changed.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun deleteMachineFromList(documentId: String, childDocumentId: String) {
        try {
            gardenCollectionRef.document(documentId)
                .collection(FIREBASE_TAKEN_TOOLS).document(childDocumentId)
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

    /**
     * 6. List of properties
     */
    suspend fun addListOfPickedProperties(documentId: String, listOfPickedMachines: List<Item>) {
        try {
            for (machine in listOfPickedMachines) {
                gardenCollectionRef.document(documentId)
                    .collection(FIREBASE_TAKEN_PROPERTIES).add(machine).await()
            }
            Timber.i("ListOfPickedMachines has been successfully inserted.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun reverseFlagOnProperty(documentId: String, childDocumentId: String) {
        try {
            firebaseSource.firestore.runTransaction { transaction ->
                val docRef = gardenCollectionRef.document(documentId)
                    .collection(FIREBASE_TAKEN_PROPERTIES).document(childDocumentId)
                val activeString = transaction.get(docRef)
                var reversedActivity = activeString[FIELD_IS_ACTIVE] as Boolean
                reversedActivity = !reversedActivity
                transaction.update(docRef, FIELD_IS_ACTIVE, reversedActivity)
                null
            }.await()

            Timber.i("$childDocumentId has been successfully changed.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

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

    /**
     * 7. Map of manHours
     */
    suspend fun addListOfPickedWorkers(documentId: String, listOfWorkerNames: List<String>) {
        try {
            val collRef =
                gardenCollectionRef.document(documentId).collection(FIREBASE_MAP_OF_MAN_HOURS)

            for (name in listOfWorkerNames) {
                val searchingQuery = collRef.whereEqualTo(FIELD_WORKER_NAME, name).get().await()

                if (searchingQuery.documents.isEmpty()) {
                    collRef.add(ManHoursMap(name)).await()
                }
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
                collRef.document(childDocumentId)
                    .update(FIELD_LIST_OF_WORKED_HOURS, FieldValue.arrayUnion(workedHours)).await()
            }
            listener?.onTransactionSuccess()
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun getMapOfManHours(documentId: String): List<ManHoursMap> {
        return withContext(Dispatchers.IO) {
            val listOfManHours = mutableListOf<ManHoursMap>()

            try {
                val querySnapshot =
                    gardenCollectionRef.document(documentId).collection(FIREBASE_MAP_OF_MAN_HOURS)
                        .orderBy(FIELD_TIMESTAMP).get().await()

                for (manHoursMap in querySnapshot) {
                    listOfManHours.add(manHoursMap.toObject<ManHoursMap>())
                }
            } catch (e: Exception) {
                Timber.e(e)
            }

            listOfManHours
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

    suspend fun reverseFlagOnShoppingNote(documentId: String, childDocumentId: String) {
        try {
            firebaseSource.firestore.runTransaction { transaction ->
                val docRef = gardenCollectionRef.document(documentId)
                    .collection(FIREBASE_SHOPPING).document(childDocumentId)
                val activeString = transaction.get(docRef)
                var reversedActivity = activeString[FIELD_IS_ACTIVE] as Boolean
                reversedActivity = !reversedActivity
                transaction.update(docRef, FIELD_IS_ACTIVE, reversedActivity)
                null
            }.await()

            Timber.i("$childDocumentId has been successfully changed.")
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

    /**
     * 8. List od shopping notes
     */
    suspend fun insertPictureToList(documentId: String, absolutePath: String) {
        try {
            val uri = Uri.fromFile(File(absolutePath))
            gardenPictureRef.child(uri.lastPathSegment!!).putFile(uri).await()
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
                    .collection(FIREBASE_TAKEN_PICTURES).get().await()

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

    fun getTakenPhotoQuerySortedByTimestamp(documentId: String) : Query =
        gardenCollectionRef.document(documentId).collection(FIREBASE_TAKEN_PICTURES)

    companion object {
        private const val FIELD_GARDEN_TITLE = "gardenTitle"
        private const val FIELD_TIMESTAMP = "timestamp"
        private const val FIELD_LIST_OF_WORKED_HOURS = "listOfManHours"
        private const val FIELD_WORKER_NAME = "workerFullName"

        private const val FIELD_IS_ACTIVE = "active"
    }
}