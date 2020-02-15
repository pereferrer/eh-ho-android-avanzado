package io.keepcoding.eh_ho.feature.topics.latestPosts.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.keepcoding.eh_ho.data.repository.PostsRepository
import io.keepcoding.eh_ho.domain.LatestPostRetrofit
import io.keepcoding.eh_ho.feature.topics.latestPosts.view.state.LatestPostManagementState
import kotlinx.coroutines.*
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LatestNewsViewModel @Inject constructor(private val postRepo: PostsRepository) : ViewModel(),
    CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private lateinit var _latestPostManagementState: MutableLiveData<LatestPostManagementState>
    val latestNewsManagementState: LiveData<LatestPostManagementState>
        get() {
            if (!::_latestPostManagementState.isInitialized) {
                _latestPostManagementState = MutableLiveData()
            }
            return _latestPostManagementState
        }

    fun onRetryButtonClicked(context: Context?) {
        _latestPostManagementState.value = LatestPostManagementState.Loading
        fetchLatestPostsAndHandleResponse(context = context)
    }

    private fun fetchLatestPostsAndHandleResponse(context: Context?) {
        println("Done entro  fwgsfdgsdf")

        val job = async {
            val a = postRepo.getPostsAcrossTopics()
            println("Done async  wgfws")
            a
        }

        launch(Dispatchers.Main) {
            val response: Response<LatestPostRetrofit> = job.await()
            println("Done await  gfswgaes")

            //todo deshabilitar loading
            if (response.isSuccessful) {
                response.body().takeIf { it != null }
                    ?.let {
                        _latestPostManagementState.value = LatestPostManagementState.LoadPostList(postList = it.latest_posts)
                    }
                    ?: run {
                        //todo _latestPostManagementState.value = LatestPostManagementState.RequestErrorReported(requestError = it)
                    }
            } else {
                //TOdo _latestPostManagementState.value = LatestPostManagementState.RequestErrorReported(requestError = it)
            }
            println("Done launch  greswgsfg")
        }
        println("Done!  safdqaw")
    }
}