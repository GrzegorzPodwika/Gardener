package pl.podwikagrzegorz.gardener.data.realm

import pl.podwikagrzegorz.gardener.data.pojo.Note

class NoteMapper : ItemMapper<Note, NoteRealm>{
    override fun fromRealm(itemRealm: NoteRealm): Note {
        val note = Note()
        note.id = itemRealm.id
        note.service = itemRealm.service
        note.priceOfService = itemRealm.priceOfService
        return note
    }

}