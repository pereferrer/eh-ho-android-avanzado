package io.keepcoding.eh_ho.data.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.android.volley.NetworkError
import com.android.volley.Request
import com.android.volley.ServerError
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.data.service.AdminRequest
import io.keepcoding.eh_ho.data.service.ApiRequestQueue
import io.keepcoding.eh_ho.data.service.ApiRoutes
import io.keepcoding.eh_ho.data.service.RequestError
import io.keepcoding.eh_ho.domain.SignInModel
import io.keepcoding.eh_ho.domain.SignUpModel

const val PREFERENCES_SESSION = "session"
const val PREFERENCES_SESSION_USERNAME = "username"

object UserRepo: LoginRepository{

    lateinit var ctx: Context

    override fun signIn(
        signInModel: SignInModel,
        onSuccess: (SignInModel) -> Unit,
        onError: (RequestError) -> Unit
    ) {

        val request =
            AdminRequest(Request.Method.GET,
                ApiRoutes.signIn(signInModel.username),
                null,
                { response ->
                    saveSession(
                        ctx,
                        signInModel.username
                    )
                    onSuccess.invoke(signInModel)
                },
                { error ->
                    error.printStackTrace()

                    if (error is ServerError && error.networkResponse.statusCode == 404)
                        onError.invoke(
                            RequestError(
                                error,
                                messageResId = R.string.error_not_registered
                            )
                        )
                    else if (error is NetworkError)
                        onError.invoke(
                            RequestError(
                                error,
                                messageResId = R.string.error_network
                            )
                        )
                    else
                        onError.invoke(
                            RequestError(
                                error
                            )
                        )
                })

        ApiRequestQueue.getRequestQueue(ctx)
            .add(request)
    }

    override fun signUp(
        context: Context,
        signUpModel: SignUpModel,
        onSuccess: (SignUpModel) -> Unit,
        onError: (RequestError) -> Unit
    ) {
        val request = AdminRequest(
            Request.Method.POST,
            ApiRoutes.signUp(),
            signUpModel.toJson(),
            { response ->

                response?.let {
                    if (it.optBoolean("success")) {
                        saveSession(
                            context,
                            signUpModel.username
                        )
                        onSuccess.invoke(signUpModel)
                    } else
                        onError.invoke(
                            RequestError(
                                message = it.getString("message")
                            )
                        )
                }

                if (response == null)
                    onError.invoke(
                        RequestError(
                            messageResId = R.string.error_invalid_response
                        )
                    )

            },
            {
                it.printStackTrace()
                if (it is NetworkError)
                    onError.invoke(
                        RequestError(
                            it,
                            messageResId = R.string.error_network
                        )
                    )
                else
                    onError.invoke(
                        RequestError(
                            it
                        )
                    )
            }
        )

        ApiRequestQueue.getRequestQueue(context)
            .add(request)
    }

    override fun isLogged(context: Context): Boolean {
        val pref = context.getSharedPreferences(PREFERENCES_SESSION, MODE_PRIVATE)
        val user = pref.getString(PREFERENCES_SESSION_USERNAME, null)
        return user != null
    }

    fun getUsername(context: Context): String {
        val pref = context.getSharedPreferences(PREFERENCES_SESSION, MODE_PRIVATE)
        val user = pref.getString(PREFERENCES_SESSION_USERNAME, "") ?: ""

        return user
    }

    fun logOut(context: Context) {
        val pref = context.getSharedPreferences(PREFERENCES_SESSION, MODE_PRIVATE)
        pref.edit()
            .remove(PREFERENCES_SESSION_USERNAME)
            .apply()
    }

    private fun saveSession(context: Context, username: String) {
        val pref =
            context.applicationContext.getSharedPreferences(PREFERENCES_SESSION, MODE_PRIVATE)
        pref.edit()
            .putString(PREFERENCES_SESSION_USERNAME, username)
            .apply()
    }
}