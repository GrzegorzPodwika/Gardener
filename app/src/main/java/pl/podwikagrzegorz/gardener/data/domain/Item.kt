package pl.podwikagrzegorz.gardener.data.domain

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Item(
    var itemName: String = "",
    var numberOfItems: Int = 0,
    var maxNumberOfItems: Int = 0,
    var isActive: Boolean = true,
    var timestamp: Timestamp = Timestamp.now(),
    @DocumentId var documentId: String = ""
)