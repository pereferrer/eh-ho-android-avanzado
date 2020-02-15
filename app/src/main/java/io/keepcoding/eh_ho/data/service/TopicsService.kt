package io.keepcoding.eh_ho.data.service

import io.keepcoding.eh_ho.domain.*
import retrofit2.Response
import retrofit2.http.*

interface TopicsService {
    @POST("posts")
    suspend fun loginUserWithCoroutines(@Body createTopicModel: CreateTopicModel): Response<CreateTopicModelResponse>

    @GET("latest.json")
    suspend fun getTopics(): Response<ListTopic>
}