package pl.podwikagrzegorz.gardener.data.repo

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import pl.podwikagrzegorz.gardener.data.domain.Tool
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_ROOT
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_TOOLS
import pl.podwikagrzegorz.gardener.data.domain.FirebaseSource
import timber.log.Timber
import javax.inject.Inject

class ToolRepository @Inject constructor(
    private val firebaseSource: FirebaseSource
) : Dao<Tool> {

    private val toolCollectionRef = firebaseSource.firestore
        .collection(FIREBASE_ROOT).document(firebaseSource.currentUser()!!.uid)
        .collection(FIREBASE_TOOLS)

    override suspend fun insert(item: Tool) {
        try {
            toolCollectionRef.add(item).await()
            Timber.i("$item has been successfully inserted.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun update(documentId: String, newItem: Tool) {
        try {
            toolCollectionRef.document(documentId)
                .set(newItem, SetOptions.merge()).await()
            Timber.i("Note $documentId has been successfully updated  with $newItem")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun delete(documentId: String) {
        try {
            toolCollectionRef.document(documentId).delete().await()
            Timber.i("Note $documentId has been successfully deleted.")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    suspend fun getAllTools(): List<Tool> =
        withContext(Dispatchers.IO) {
            val listOfTools = mutableListOf<Tool>()

            try {
                val querySnapshotOfTools = toolCollectionRef.get().await()
                for (tool in querySnapshotOfTools) {
                    listOfTools.add(tool.toObject<Tool>())
                }
            } catch (e: Exception) {
                Timber.e(e)

            }

            Timber.i("Successfully downloaded ${listOfTools.size}")
            listOfTools
        }


    override fun getQuery(): Query =
        toolCollectionRef

    override fun getQuerySortedByName(): Query =
        toolCollectionRef.orderBy(FIELD_TOOL_NAME)

    fun getQuerySortedByNumberOfTools(): Query =
        toolCollectionRef.orderBy(FIELD_NUMBER_OF_TOOLS)

    override fun getQuerySortedByTimestamp(): Query =
        toolCollectionRef.orderBy(FIELD_TIMESTAMP)

    companion object {
        private const val FIELD_TOOL_NAME = "toolName"
        private const val FIELD_NUMBER_OF_TOOLS = "numberOfTools"
        private const val FIELD_TIMESTAMP = "timestamp"
    }
}