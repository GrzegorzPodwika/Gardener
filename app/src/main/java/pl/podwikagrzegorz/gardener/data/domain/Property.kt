package pl.podwikagrzegorz.gardener.data.domain

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Property(
    val propertyName: String = "",
    val numberOfProperties: Int = 0,
    val timestamp: Timestamp = Timestamp.now(),
    @DocumentId val documentId: String = ""
)