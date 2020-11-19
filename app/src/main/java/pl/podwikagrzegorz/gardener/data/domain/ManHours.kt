package pl.podwikagrzegorz.gardener.data.domain

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import java.util.*

data class ManHours(
    var date: Date = Date(),
    var numberOfWorkedHours: Double = 0.0,
    var timestamp: Timestamp = Timestamp.now(),
    @DocumentId var documentId: String = ""
)