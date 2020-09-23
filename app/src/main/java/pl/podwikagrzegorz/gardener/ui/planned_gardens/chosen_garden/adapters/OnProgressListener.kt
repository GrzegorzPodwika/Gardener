package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters

interface OnProgressListener {
    fun onStarted()
    fun onProgress(progress: Double)
    fun onFinished()
}