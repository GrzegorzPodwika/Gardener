package pl.podwikagrzegorz.gardener

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp
import pl.podwikagrzegorz.gardener.data.repo.PreferenceRepository
import timber.log.Timber

@HiltAndroidApp
class GardenerApp: Application() {

    lateinit var preferenceRepository: PreferenceRepository

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        ctx = applicationContext
        res = resources

        preferenceRepository = PreferenceRepository(getSharedPreferences(DEFAULT_PREFERENCES, MODE_PRIVATE))
    }

    companion object{
        lateinit var ctx: Context
        lateinit var res: Resources

        const val WRITTEN_FILE_LOCATION = "pl.podwikagrzegorz.gardener.fileprovider"
        const val DEFAULT_PREFERENCES = "default_preferences"

    }
}