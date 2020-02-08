package io.keepcoding.eh_ho.feature.topics.latestPosts.view.state

import io.keepcoding.eh_ho.data.service.RequestError
import io.keepcoding.eh_ho.domain.LatestPost
import io.keepcoding.eh_ho.domain.Post
import io.keepcoding.eh_ho.feature.topics.view.state.TopicManagementState

sealed class LatestPostManagementState {
    object Loading : LatestPostManagementState()
    class LoadPostList(val postList: List<LatestPost>) : LatestPostManagementState()
    class RequestErrorReported(val requestError: RequestError) : LatestPostManagementState()

}