package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters

import pl.podwikagrzegorz.gardener.data.domain.ManHours

interface EditDeleteWorkedHoursListener {
    fun onEditClick(manHours: ManHours, workerDocumentId: String, manHoursDocumentId: String)
    fun onDeleteClick(workerDocumentId: String, manHoursDocumentId: String)
}