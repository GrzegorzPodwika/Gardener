package pl.podwikagrzegorz.gardener.data.domain

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId


data class ManHoursMap(
    var workerFullName: String = "",
    var listOfManHours: List<ManHours> = arrayListOf(),
    var timestamp: Timestamp = Timestamp.now(),
    @DocumentId var documentId: String = ""
)