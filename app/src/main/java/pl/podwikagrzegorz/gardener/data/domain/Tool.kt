package pl.podwikagrzegorz.gardener.data.domain

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

//TODO zmienic dalej nastepne klasy
data class Tool(
    val toolName: String = "",
    val numberOfTools: Int = 0,
    val timestamp: Timestamp = Timestamp.now(),
    @DocumentId val documentId: String = ""
)