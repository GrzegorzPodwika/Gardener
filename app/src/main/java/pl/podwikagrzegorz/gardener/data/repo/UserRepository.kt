package pl.podwikagrzegorz.gardener.data.repo

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import pl.podwikagrzegorz.gardener.data.domain.FirebaseSource
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val firebaseSource: FirebaseSource
) {
    val isSuccess
        get() = firebaseSource.isSuccess

    suspend fun login(account: GoogleSignInAccount) = firebaseSource.login(account)

    fun currentUser() = firebaseSource.currentUser()

    fun logout() = firebaseSource.logout()
}