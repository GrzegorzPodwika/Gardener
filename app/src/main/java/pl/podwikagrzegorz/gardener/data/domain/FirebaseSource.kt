package pl.podwikagrzegorz.gardener.data.domain

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirebaseSource {

    private val firebaseAuth : FirebaseAuth by lazy {
        Firebase.auth
    }
    val firestore: FirebaseFirestore by lazy {
        Firebase.firestore
    }
    val storage: FirebaseStorage by lazy {
        Firebase.storage
    }
    var isSuccess: Boolean = false


    suspend fun login(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        isSuccess = try {
            firebaseAuth.signInWithCredential(credential).await()
            Timber.i("Logged in successfully.")
            true
        }catch (e: Exception) {
            Timber.e(e)
            false
        }
    }

    fun logout() {
        firebaseAuth.signOut()
        Timber.i("Successfully logged out.")
    }

    fun currentUser() : FirebaseUser?{
        //Timber.i("Current user = ${firebaseAuth.currentUser?.displayName}")
        return firebaseAuth.currentUser
    }
}