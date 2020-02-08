package io.keepcoding.eh_ho.feature.topics.latestPosts.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.keepcoding.eh_ho.data.repository.PostsRepository
import io.keepcoding.eh_ho.feature.topics.latestPosts.view.state.LatestPostManagementState
import io.keepcoding.eh_ho.feature.topics.view.state.TopicManagementState
import javax.inject.Inject

class LatestNewsViewModel @Inject constructor(private val postRepo: PostsRepository) : ViewModel() {

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
        context?.let {
            postRepo.getPostsAcrossTopics(//Todo pasar identificador topic
                {
                    _latestPostManagementState.value =
                        LatestPostManagementState.LoadPostList(postList = it)                },
                {
                    _latestPostManagementState.value =
                        LatestPostManagementState.RequestErrorReported(requestError = it)                })
        }
    }
}