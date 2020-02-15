package io.keepcoding.eh_ho.data.repository


import android.content.Context
import com.android.volley.NetworkError
import com.android.volley.Request
import com.android.volley.ServerError
import io.keepcoding.eh_ho.Database.LatestNewEntity
import io.keepcoding.eh_ho.Database.LatestNewsDatabase
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.data.service.*
import io.keepcoding.eh_ho.domain.*
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit

object PostsRepo : PostsRepository{

    lateinit var db: LatestNewsDatabase
    lateinit var ctx: Context
    lateinit var retroF: Retrofit


    fun getPosts(idPost:String,
                 context: Context,
                 onSuccess:(List<Post>) ->Unit,
                 onError:(RequestError)->Unit)
    {

        val username = UserRepo.getUsername(context)
        val request = UserRequest(
            username,
            Request.Method.GET,
            ApiRoutes.getPostsByTopicId(idPost),
            null,
            {
                it?.let {
                    onSuccess.invoke(
                        Post.parsePosts(
                            it
                        )
                    )
                }

                if (it == null)
                    onError.invoke(
                        RequestError(
                            messageResId = R.string.error_invalid_response
                        )
                    )
            },
            {
                it.printStackTrace()
                if (it is NetworkError)
                    onError.invoke(
                        RequestError(
                            messageResId = R.string.error_network
                        )
                    )
                else
                    onError.invoke(
                        RequestError(
                            it
                        )
                    )
            })

        ApiRequestQueue.getRequestQueue(context)
            .add(request)
    }

    override suspend fun getPostsAcrossTopics(): Response<LatestPostRetrofit> {
        if(retroF == null){
            println("Hola pere")
        }
        println("asddsdaassdasa"  + retroF)

        val a =  retroF.create(PostService::class.java).getPostsAcrossTopics()
        println("He acabat retrofit")
        return a
    }



    fun createPost(
        context: Context,
        model: CreatePostModel,
        onSuccess: (CreatePostModel) -> Unit,
        onError: (RequestError) -> Unit
    ) {
        val username = UserRepo.getUsername(context)
        val request = UserRequest(
            username,
            Request.Method.POST,
            ApiRoutes.createTopic(),
            model.toJson(),
            {
                it?.let {
                    onSuccess.invoke(model)
                }

                if (it == null)
                    onError.invoke(
                        RequestError(
                            messageResId = R.string.error_invalid_response
                        )
                    )
            },
            {
                it.printStackTrace()

                if (it is ServerError && it.networkResponse.statusCode == 422) {
                    val body = String(it.networkResponse.data, Charsets.UTF_8)
                    val jsonError = JSONObject(body)
                    val errors = jsonError.getJSONArray("errors")
                    var errorMessage = ""

                    for (i in 0 until errors.length()) {
                        errorMessage += "${errors[i]} "
                    }

                    onError.invoke(
                        RequestError(
                            it,
                            message = errorMessage
                        )
                    )

                } else if (it is NetworkError)
                    onError.invoke(
                        RequestError(
                            it,
                            messageResId = R.string.error_network
                        )
                    )
                else
                    onError.invoke(
                        RequestError(
                            it
                        )
                    )
            }
        )

        ApiRequestQueue.getRequestQueue(context)
            .add(request)
    }
}


private fun List<LatestNewEntity>.toModel(): List<LatestPost> = map { it.toModel() }

private fun LatestNewEntity.toModel(): LatestPost = LatestPost(

    id = topicId,
    topic_title = title,
    topic_slug = slug,
    post_number = posts,
    score = score
)


private fun List<LatestPost>.toEntity(): List<LatestNewEntity> = map { it.toEntity() }

private fun LatestPost.toEntity(): LatestNewEntity = LatestNewEntity(
    topicId = id,
    title = topic_title,
    slug = topic_slug,
    date = created_at.toString(),
    posts = post_number,
    score = score

)