package pl.podwikagrzegorz.gardener.data.domain

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class BasicGarden(
    var gardenTitle: String = "",
    var phoneNumber: Int = 0,
    var period: Period = Period(),
    var isGarden: Boolean = true,
    var uniqueSnapshotName: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var timestamp: Timestamp = Timestamp.now(),
    @DocumentId var documentId: String = ""
)