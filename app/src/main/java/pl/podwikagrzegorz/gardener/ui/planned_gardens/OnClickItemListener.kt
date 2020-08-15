package pl.podwikagrzegorz.gardener.ui.planned_gardens

interface OnClickItemListener {
    fun onClick(id: Long = 0)
    fun onLongClick(id: Long) {}
    fun onChangeNumberOfItems(currentValue: Int, position: Int, itemName: String){}
    fun onChangeFlagToOpposite(position: Int) {}
}