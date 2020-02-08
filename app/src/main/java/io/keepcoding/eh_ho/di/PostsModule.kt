package io.keepcoding.eh_ho.di

import android.content.Context
import dagger.Module
import dagger.Provides
import io.keepcoding.eh_ho.Database.LatestNewsDatabase
import io.keepcoding.eh_ho.data.repository.PostsRepo
import javax.inject.Singleton

@Module
class PostsModuleModule {

    @Singleton
    @Provides
    fun provideTopicsRepo(context: Context, latestNewsDatabase: LatestNewsDatabase): PostsRepo =
        PostsRepo.apply {
            db = latestNewsDatabase
            ctx = context
        }

}