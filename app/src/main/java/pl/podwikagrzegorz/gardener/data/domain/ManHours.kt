package pl.podwikagrzegorz.gardener.data.domain

import java.util.*

data class ManHours(
    var date: Date = Date(),
    var numberOfWorkedHours: Double = 0.0
)