package pl.podwikagrzegorz.gardener.data.domain

data class Garden(
    val id: Long = 0,
    val basicGarden: BasicGarden = BasicGarden(),
    val listOfDescriptions: List<ActiveString> = listOf(),
    val listOfNotes: List<ActiveString> = listOf(),

    val listOfTools: List<Item> = listOf(),
    val listOfMachines: List<Item> = listOf(),
    val listOfProperties: List<Item> = listOf(),

    val listOfShopping: List<ActiveString> = listOf(),
    val mapOfWorkedHours: List<ManHoursMap> = listOf(),
    val listOfPicturesPaths: List<String> = listOf()
)