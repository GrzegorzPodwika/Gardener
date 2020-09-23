package pl.podwikagrzegorz.gardener.data.domain

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class ActiveProperty(
    var propertyName: String = "",
    var amountOfProperties: String = "",
    var isActive: Boolean = true,
    var timestamp: Timestamp = Timestamp.now(),
    @DocumentId var documentId: String = ""
)