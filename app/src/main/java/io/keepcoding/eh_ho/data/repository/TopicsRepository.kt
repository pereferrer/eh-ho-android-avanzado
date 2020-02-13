package io.keepcoding.eh_ho.data.repository

import io.keepcoding.eh_ho.domain.*
import retrofit2.Response

interface TopicsRepository {
    suspend fun getTopics(): Response<ListTopic>
    suspend fun createTopic(createTopicModel: CreateTopicModel): Response<CreateTopicModelResponse>
}