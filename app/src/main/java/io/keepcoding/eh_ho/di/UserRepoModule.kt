package io.keepcoding.eh_ho.di

import android.content.Context
import dagger.Module
import dagger.Provides
import io.keepcoding.eh_ho.data.repository.UserRepo
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class UserRepoModule {

    @Singleton
    @Provides
    fun provideUserRepo(context: Context, retrofit: Retrofit): UserRepo {
        UserRepo.ctx = context
        return UserRepo
    }
}