package io.keepcoding.eh_ho.di

import dagger.Binds
import dagger.Module
import io.keepcoding.eh_ho.data.repository.TopicsRepo
import io.keepcoding.eh_ho.data.repository.TopicsRepository

@Module
abstract class TopicsAbstractModule {

    @Binds
    abstract fun provideLoginRepository(topicsRepo: TopicsRepo): TopicsRepository

}