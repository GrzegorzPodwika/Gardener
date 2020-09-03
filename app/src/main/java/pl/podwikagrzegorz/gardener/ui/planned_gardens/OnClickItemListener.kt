package pl.podwikagrzegorz.gardener.ui.planned_gardens

interface OnClickItemListener {
    fun onClickItem(documentId: String) {}
    fun onLongClick(documentId: String) {}

    fun onChangeFlagToOpposite(documentId: String) {}
    //fun onChangeNumberOfItems(currentValue: Int, position: Int, itemName: String){}
}