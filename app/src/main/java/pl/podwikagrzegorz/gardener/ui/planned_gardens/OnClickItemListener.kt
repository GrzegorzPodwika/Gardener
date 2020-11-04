package pl.podwikagrzegorz.gardener.ui.planned_gardens

interface OnClickItemListener {
    fun onClickItem(documentId: String)
    fun onLongClick(documentId: String) {}

    fun onChangeFlagToOpposite(documentId: String, isActive: Boolean) {}
    fun onChangeNumberOfItems(documentId: String, currentNumberOfItems: Int, maxNumberOfItems: Int) {}
}