package pl.podwikagrzegorz.gardener.ui.auth

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.podwikagrzegorz.gardener.data.repo.UserRepository

class AuthViewModel @ViewModelInject constructor(
    private val repository: UserRepository
) : ViewModel() {

    var authListener: AuthListener? = null

    val user by lazy {
        repository.currentUser()
    }

    fun login(account: GoogleSignInAccount) = viewModelScope.launch(Dispatchers.Main) {
        authListener?.onStarted()

        withContext(Dispatchers.IO) {
            repository.login(account)
        }

        if (repository.isSuccess) {
            authListener?.onSuccess()
        } else {
            authListener?.onFailure()
        }
    }


    fun logout() {
        repository.logout()
    }

}

/*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory(
    private val repository: UserRepository
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthViewModel(repository) as T
    }
}*/
