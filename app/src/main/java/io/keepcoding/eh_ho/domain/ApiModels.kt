package io.keepcoding.eh_ho.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

data class SignInModel(
    val username: String,
    val password: String
)

data class SignUpModel(
    val username: String,
    val email: String,
    val password: String
) {
    fun toJson(): JSONObject {
        return JSONObject()
            .put("name", username)
            .put("username", username)
            .put("email", email)
            .put("password", password)
            .put("active", true)
            .put("approved", true)
    }
}

data class CreateTopicModel(
    @Expose
    @SerializedName("title")
    val title: String,
    @Expose
    @SerializedName("raw")
    val raw: String
) {
    override fun toString(): String {
        return "CreateTopicModel(title='$title', raw='$raw')"
    }
}

data class CreateTopicModelResponse(
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("username")
    @Expose
    val username: String
)


data class CreatePostModel(
    val title: String,
    val topicId: String,
    val raw: String
) {
    fun toJson(): JSONObject {
        return JSONObject()
            .put("topic_id", topicId)
            .put("title", title)
            .put("raw", raw)
    }
}

data class ListTopic(
    @SerializedName("topic_list")
    val topic_list: TopicListResponse
)

data class TopicListResponse(
    @SerializedName("topics")
    val topics:List<Topic>
)