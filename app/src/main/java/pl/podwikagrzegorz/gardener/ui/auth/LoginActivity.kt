package pl.podwikagrzegorz.gardener.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.ActivityLoginBinding
import pl.podwikagrzegorz.gardener.extensions.snackbar
import pl.podwikagrzegorz.gardener.extensions.startHomeActivity

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), AuthListener{

    private val viewModel: AuthViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding

    override fun onStart() {
        super.onStart()

        checkIfUserIsAlreadyLoggedIn()
    }

    private fun checkIfUserIsAlreadyLoggedIn() {
        viewModel.user?.let {
            startHomeActivity()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //checkIfUserIsAlreadyLoggedIn()

        viewModel.authListener = this
        setOnLogInButtonListener()
    }

    private fun setOnLogInButtonListener() {
        binding.signInButton.setOnClickListener {
            binding.progressBarAuth.visibility = View.VISIBLE
            val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val signInClient = GoogleSignIn.getClient(this, options)
            signInClient.signInIntent.also {
                startActivityForResult(it, REQUEST_CODE_SIGN_IN)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SIGN_IN) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
            account?.let {
                viewModel.login(it)
            }
        }
    }

    override fun onStarted() {
        binding.progressBarAuth.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        binding.progressBarAuth.visibility = View.GONE
        startHomeActivity()
    }

    override fun onFailure() {
        binding.progressBarAuth.visibility = View.GONE
        binding.root.snackbar("Login Failure!", Snackbar.LENGTH_LONG)
    }

    companion object {
        const val REQUEST_CODE_SIGN_IN = 1
    }
}