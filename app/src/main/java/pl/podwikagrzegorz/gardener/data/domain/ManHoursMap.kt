package pl.podwikagrzegorz.gardener.data.domain


data class ManHoursMap(
    val worker: Worker,
    val listOfManHours: List<ManHours>
)