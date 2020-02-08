package io.keepcoding.eh_ho.feature.topics.view.state

import io.keepcoding.eh_ho.data.service.RequestError
import io.keepcoding.eh_ho.domain.LatestPost
import io.keepcoding.eh_ho.domain.Topic

sealed class TopicManagementState {
    class GoToPosts(val topic: Topic): TopicManagementState()
    class GoToPostsByLatestPost(val latestPost: LatestPost): TopicManagementState()
    class LoadTopicList(val topicList: List<Topic>) : TopicManagementState()
    object OnGoToTopics : TopicManagementState()
    object OnGoToLatestNews : TopicManagementState()
    object NavigateToCreateTopic: TopicManagementState()
    object OnLogOut: TopicManagementState()
    object CreateTopicCompleted : TopicManagementState()
    object Loading : TopicManagementState()
    class TopicCreatedSuccessfully(val msg: String) : TopicManagementState()
    class RequestErrorReported(val requestError: RequestError) : TopicManagementState()

}