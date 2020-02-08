package io.keepcoding.eh_ho.data.repository

import io.keepcoding.eh_ho.data.service.RequestError
import io.keepcoding.eh_ho.domain.LatestPost

interface PostsRepository {

    fun getPostsAcrossTopics(onSuccess: (List<LatestPost>) -> Unit, onError: (RequestError) -> Unit)

}