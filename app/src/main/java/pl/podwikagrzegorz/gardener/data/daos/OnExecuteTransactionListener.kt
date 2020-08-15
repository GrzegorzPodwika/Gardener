package pl.podwikagrzegorz.gardener.data.daos

interface OnExecuteTransactionListener {
    fun onTransactionSuccess() {}
    fun onAsyncTransactionSuccess() {}
}