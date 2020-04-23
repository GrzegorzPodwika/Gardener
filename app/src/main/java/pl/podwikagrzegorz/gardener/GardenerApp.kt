package pl.podwikagrzegorz.gardener

import android.app.Application
import android.content.Context
import android.content.res.Resources
import io.realm.Realm

class GardenerApp: Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        ctx = applicationContext
        res = resources
    }

    companion object{
        lateinit var ctx: Context
        lateinit var res: Resources
    }
}