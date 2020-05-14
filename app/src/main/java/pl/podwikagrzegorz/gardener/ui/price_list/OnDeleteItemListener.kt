package pl.podwikagrzegorz.gardener.ui.price_list

interface OnDeleteItemListener {
    fun onDeleteItemClick(id: Long?)
    fun onDeleteItemLongClick(id: Long?) {}
}