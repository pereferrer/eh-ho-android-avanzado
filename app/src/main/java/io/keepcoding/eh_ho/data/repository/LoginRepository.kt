package io.keepcoding.eh_ho.data.repository

import android.content.Context
import io.keepcoding.eh_ho.data.service.RequestError
import io.keepcoding.eh_ho.domain.SignInModel
import io.keepcoding.eh_ho.domain.SignUpModel
import retrofit2.Response

interface LoginRepository {

    fun signIn(
        signInModel: SignInModel,
        onSuccess: (SignInModel) -> Unit, onError: (RequestError) -> Unit
    )

    fun isLogged(context: Context): Boolean

    fun signUp(
        context: Context,
        signUpModel: SignUpModel,
        onSuccess: (SignUpModel) -> Unit,
        onError: (RequestError) -> Unit
    )
}