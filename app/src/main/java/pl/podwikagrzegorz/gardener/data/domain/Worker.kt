package pl.podwikagrzegorz.gardener.data.domain

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Worker(
    val name: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    @DocumentId val documentId: String = ""
)