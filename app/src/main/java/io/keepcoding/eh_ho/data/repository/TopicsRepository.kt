package io.keepcoding.eh_ho.data.repository

import io.keepcoding.eh_ho.data.service.RequestError
import io.keepcoding.eh_ho.domain.CreateTopicModel
import io.keepcoding.eh_ho.domain.Topic

interface TopicsRepository {

    fun getTopics(onSuccess: (List<Topic>) -> Unit, onError: (RequestError) -> Unit)

    fun createTopic(
        model: CreateTopicModel,
        onSuccess: (CreateTopicModel) -> Unit, onError: (RequestError) -> Unit
    )

}