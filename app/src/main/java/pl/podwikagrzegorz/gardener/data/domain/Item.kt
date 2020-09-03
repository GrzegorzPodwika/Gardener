package pl.podwikagrzegorz.gardener.data.domain

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Item(
    val itemName: String = "",
    val numberOfItems: Int = 0,
    val isActive: Boolean = true,
    val timestamp: Timestamp = Timestamp.now(),
    @DocumentId val documentId: String = ""
)