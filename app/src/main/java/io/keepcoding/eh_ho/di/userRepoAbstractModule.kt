package io.keepcoding.eh_ho.di

import dagger.Binds
import dagger.Module
import io.keepcoding.eh_ho.data.repository.LoginRepository
import io.keepcoding.eh_ho.data.repository.UserRepo

@Module
abstract class UserRepoAbstractModule {

    @Binds
    abstract fun provideLoginRepository(userRepo: UserRepo): LoginRepository

}