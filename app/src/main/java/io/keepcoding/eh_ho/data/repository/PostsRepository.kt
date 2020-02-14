package io.keepcoding.eh_ho.data.repository

import io.keepcoding.eh_ho.data.service.RequestError
import io.keepcoding.eh_ho.domain.LatestPost
import io.keepcoding.eh_ho.domain.LatestPostRetrofit
import io.keepcoding.eh_ho.domain.ListTopic
import retrofit2.Response

interface PostsRepository {

    suspend fun getPostsAcrossTopics(): Response<LatestPostRetrofit>

}