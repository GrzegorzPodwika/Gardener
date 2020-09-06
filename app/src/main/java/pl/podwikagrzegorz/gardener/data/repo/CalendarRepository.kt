package pl.podwikagrzegorz.gardener.data.repo

import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import pl.podwikagrzegorz.gardener.data.domain.BasicGarden
import pl.podwikagrzegorz.gardener.data.domain.FirebaseSource
import pl.podwikagrzegorz.gardener.data.domain.Period
import pl.podwikagrzegorz.gardener.extensions.Constants
import timber.log.Timber
import javax.inject.Inject

class CalendarRepository @Inject constructor(
    firebaseSource: FirebaseSource
) {
    private val gardenCollectionRef = firebaseSource.firestore
        .collection(Constants.FIREBASE_ROOT).document(firebaseSource.currentUser()!!.uid)
        .collection(Constants.FIREBASE_GARDENS)

    suspend fun getAllPeriods() : List<Period> {
        return withContext(Dispatchers.IO) {
            val listOfPeriods = mutableListOf<Period>()

            try {
                val querySnapshot = gardenCollectionRef.get().await()

                for (document in querySnapshot) {
                    val basicGarden = document.toObject<BasicGarden>()
                    listOfPeriods.add(basicGarden.period)
                }
                Timber.i("ListOfPeriods size = ${listOfPeriods.size}")
            }catch (e: Exception) {
                Timber.e(e)
            }


            listOfPeriods
        }
    }
}