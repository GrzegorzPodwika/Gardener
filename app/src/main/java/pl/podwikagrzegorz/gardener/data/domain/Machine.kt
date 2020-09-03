package pl.podwikagrzegorz.gardener.data.domain

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class  Machine(
    val machineName: String = "",
    val numberOfMachines: Int = 0,
    val timestamp: Timestamp = Timestamp.now(),
    @DocumentId val documentId: String = ""
)