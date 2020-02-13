package io.keepcoding.eh_ho.di

import android.content.Context
import dagger.Module
import dagger.Provides
import io.keepcoding.eh_ho.data.repository.TopicsRepo
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class TopicsModule {

    @Singleton
    @Provides
    fun provideTopicsRepo(context: Context, retrofit: Retrofit): TopicsRepo =
        TopicsRepo.apply {
            ctx = context
            retroF = retrofit
        }
}