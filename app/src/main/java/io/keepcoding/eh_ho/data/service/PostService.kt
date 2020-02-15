package io.keepcoding.eh_ho.data.service

import io.keepcoding.eh_ho.domain.LatestPostRetrofit
import retrofit2.Response
import retrofit2.http.GET

interface PostService {

    @GET("posts.json")
    suspend fun getPostsAcrossTopics(): Response<LatestPostRetrofit>
}