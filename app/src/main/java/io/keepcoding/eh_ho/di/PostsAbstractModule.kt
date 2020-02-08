package io.keepcoding.eh_ho.di

import dagger.Binds
import dagger.Module
import io.keepcoding.eh_ho.data.repository.PostsRepo
import io.keepcoding.eh_ho.data.repository.PostsRepository

@Module
abstract class PostsAbstractModule {

    @Binds
    abstract fun providePostsRepository(postRepo: PostsRepo): PostsRepository

}