package pl.podwikagrzegorz.gardener.ui.price_list

import java.text.FieldPosition

interface OnDeleteItemListener {
    fun onDeleteItemClick(id: Long?)
    fun onDeleteItemLongClick(id: Long?) {}
    fun onChangeNumberOfItems(noItems: Int, position: Int){}
}