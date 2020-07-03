package pl.podwikagrzegorz.gardener

import android.app.Application
import android.content.Context
import android.content.res.Resources
import io.realm.Realm
import pl.podwikagrzegorz.gardener.data.PreferenceRepository

class GardenerApp: Application() {

    lateinit var preferenceRepository: PreferenceRepository

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        ctx = applicationContext
        res = resources
        preferenceRepository = PreferenceRepository(getSharedPreferences(DEFAULT_PREFERENCES, Context.MODE_PRIVATE))
    }

    companion object{
        lateinit var ctx: Context
        lateinit var res: Resources
        const val WRITTEN_FILE_LOCATION = "pl.podwikagrzegorz.gardener.fileprovider"
        const val DEFAULT_PREFERENCES = "default_preferences"
    }
}