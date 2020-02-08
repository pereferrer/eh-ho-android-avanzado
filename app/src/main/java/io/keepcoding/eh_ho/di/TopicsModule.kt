package io.keepcoding.eh_ho.di

import android.content.Context
import dagger.Module
import dagger.Provides
import io.keepcoding.eh_ho.data.repository.TopicsRepo
import javax.inject.Singleton

@Module
class TopicsModule {

    @Singleton
    @Provides
    fun provideTopicsRepo(context: Context): TopicsRepo =
        TopicsRepo.apply {
            ctx = context
        }

}