package pl.podwikagrzegorz.gardener.data.repo

interface OnExecuteTransactionListener {
    fun onTransactionSuccess()
    fun onTransactionFailure(exception: Exception) {}
}