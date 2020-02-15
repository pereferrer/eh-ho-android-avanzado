package io.keepcoding.eh_ho.data.repository

import android.content.Context
import io.keepcoding.eh_ho.data.service.*
import io.keepcoding.eh_ho.domain.*
import retrofit2.Response
import retrofit2.Retrofit


object TopicsRepo: TopicsRepository {

    lateinit var ctx: Context
    lateinit var retroF: Retrofit

    override suspend fun getTopics(): Response<ListTopic> {
        val a =    retroF.create(TopicsService::class.java).getTopics()
        println("" + retroF.toString())
        return a
    }

    override suspend fun createTopic(createTopicModel: CreateTopicModel): Response<CreateTopicModelResponse> {
        val a =    retroF.create(TopicsService::class.java).loginUserWithCoroutines(createTopicModel)
        println("" + retroF.toString())
        return a
    }
}