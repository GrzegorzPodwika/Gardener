package pl.podwikagrzegorz.gardener.data.domain

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Note(
    val service: String = "",
    val priceOfService: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    @DocumentId val documentId: String = ""
)