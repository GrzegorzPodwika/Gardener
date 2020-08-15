package pl.podwikagrzegorz.gardener.data.domain

import pl.podwikagrzegorz.gardener.data.realm.NoteRealm

data class Note(
    var id: Long = 0,
    var service: String = "",
    var priceOfService: String = ""
) {
    fun asNoteRealm() : NoteRealm =
        NoteRealm(id, service, priceOfService)
}
