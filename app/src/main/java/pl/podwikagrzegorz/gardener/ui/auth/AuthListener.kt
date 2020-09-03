package pl.podwikagrzegorz.gardener.ui.auth

interface AuthListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure()
}