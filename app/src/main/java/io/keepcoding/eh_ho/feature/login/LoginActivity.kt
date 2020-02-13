package io.keepcoding.eh_ho.feature.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.data.repository.LoginRepository
import io.keepcoding.eh_ho.data.service.RequestError
import io.keepcoding.eh_ho.domain.SignInModel
import io.keepcoding.eh_ho.domain.SignUpModel
import io.keepcoding.eh_ho.data.repository.UserRepo
import io.keepcoding.eh_ho.di.DaggerApplicationGraph
import io.keepcoding.eh_ho.di.UtilsModule
import io.keepcoding.eh_ho.feature.topics.view.ui.TopicsActivity
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity(),
    SignInFragment.SignInInteractionListener,
    SignUpFragment.SignUpInteractionListener {

    val signInFragment: SignInFragment =
        SignInFragment()
    val signUpFragment: SignUpFragment =
        SignUpFragment()
    @Inject
    lateinit var userRepo: LoginRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerApplicationGraph.builder()
            .utilsModule(UtilsModule(applicationContext))
            .build()
            .inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (savedInstanceState == null) {
            checkSession()
        }


    }

    private fun checkSession() {
        if (userRepo.isLogged(this))
            launchTopicsActivity()
        else
            onGoToSignIn()
    }

    override fun onGoToSignUp() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, signUpFragment)
            .commit()
    }

    override fun onGoToSignIn() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, signInFragment)
            .commit()
    }

    override fun onSignIn(signInModel: SignInModel) {
        enableLoading(true)
        userRepo.signIn(signInModel,
            {
                enableLoading(false)
                launchTopicsActivity()
            },
            {
                enableLoading(false)
                handleRequestError(it)
            })
    }

    override fun onSignUp(signUpModel: SignUpModel) {
        enableLoading(true)
        userRepo.signUp(this,
            signUpModel,
            {
                enableLoading(false)
                launchTopicsActivity()
            },
            {
                enableLoading(false)
                handleRequestError(it)
            })
    }

    private fun handleRequestError(requestError: RequestError) {
        val message = if (requestError.messageResId != null)
            getString(requestError.messageResId)
        else if (requestError.message != null)
            requestError.message
        else
            getString(R.string.error_request_default)

        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG).show()
    }

    private fun enableLoading(enabled: Boolean) {
        if (enabled) {
            fragmentContainer.visibility = View.INVISIBLE
            viewLoading.visibility = View.VISIBLE
        } else {
            fragmentContainer.visibility = View.VISIBLE
            viewLoading.visibility = View.INVISIBLE
        }
    }

    private fun launchTopicsActivity() {
        val intent = Intent(this, TopicsActivity::class.java)
        startActivity(intent)
        finish()
    }
}
