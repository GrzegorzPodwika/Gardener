package pl.podwikagrzegorz.gardener.ui.price_list

fun interface OnChangeItemListener<T> {
    fun onChangedItem(updatedItem: T)
}