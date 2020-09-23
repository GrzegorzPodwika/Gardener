package pl.podwikagrzegorz.gardener.data.domain

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class  Machine(
    var machineName: String = "",
    var numberOfMachines: Int = 0,
    var timestamp: Timestamp = Timestamp.now(),
    @DocumentId var documentId: String = ""
)