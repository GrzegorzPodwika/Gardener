package pl.podwikagrzegorz.gardener.data.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import pl.podwikagrzegorz.gardener.data.domain.Note

open class NoteRealm(
    @PrimaryKey var id: Long = 0,
    var service: String = "",
    var priceOfService: String = ""
) : RealmObject() {
    fun asNote(): Note =
        Note(id, service, priceOfService)
}