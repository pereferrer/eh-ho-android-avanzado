package io.keepcoding.eh_ho.data.repository

import io.keepcoding.eh_ho.domain.LatestPostRetrofit
import retrofit2.Response

interface PostsRepository {

    suspend fun getPostsAcrossTopics(): Response<LatestPostRetrofit>

}