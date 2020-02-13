package io.keepcoding.eh_ho.di

import dagger.Component
import io.keepcoding.eh_ho.feature.login.LoginActivity
import io.keepcoding.eh_ho.feature.topics.view.ui.TopicsActivity
import javax.inject.Singleton

// @Component makes Dagger create a graph of dependencies
@Singleton
@Component(modules = [UserRepoModule::class, UserRepoAbstractModule::class, TopicsModule::class, UtilsModule::class, TopicsAbstractModule::class, PostsModuleModule::class, PostsAbstractModule::class])
interface ApplicationGraph {


    // Add here as well functions whose input argument is the entity in which Dagger can add any
    // dependency you want
    fun inject(topicsActivity: TopicsActivity)
    fun inject(loginActivity: LoginActivity)
}