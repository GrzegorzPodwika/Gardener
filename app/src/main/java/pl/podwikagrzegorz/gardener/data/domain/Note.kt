package pl.podwikagrzegorz.gardener.data.domain

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Note(
    var service: String = "",
    var priceOfService: String = "",
    var timestamp: Timestamp = Timestamp.now(),
    @DocumentId var documentId: String = ""
)