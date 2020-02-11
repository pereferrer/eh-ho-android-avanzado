    package io.keepcoding.eh_ho.feature.topics.viewmodel

    import android.content.Context
    import android.widget.Toast
    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.ViewModel
    import io.keepcoding.eh_ho.R
    import io.keepcoding.eh_ho.data.repository.TopicsRepo
    import io.keepcoding.eh_ho.data.repository.TopicsRepository
    import io.keepcoding.eh_ho.data.repository.UserRepo
    import io.keepcoding.eh_ho.data.service.RequestError
    import io.keepcoding.eh_ho.domain.CreateTopicModel
    import io.keepcoding.eh_ho.domain.LatestPost
    import io.keepcoding.eh_ho.domain.SignInModel
    import io.keepcoding.eh_ho.domain.Topic
    import io.keepcoding.eh_ho.feature.topics.view.state.TopicManagementState
    import kotlinx.android.synthetic.main.fragment_create_topic.*
    import kotlinx.coroutines.*
    import retrofit2.Response
    import javax.inject.Inject
    import kotlin.coroutines.CoroutineContext


    class TopicViewModel @Inject constructor(private val topicsRepo: TopicsRepository) : ViewModel(), CoroutineScope {

        private val job = Job()
        override val coroutineContext: CoroutineContext
            get() = job + Dispatchers.IO


        private lateinit var _topicManagementState: MutableLiveData<TopicManagementState>
        val topicManagementState: LiveData<TopicManagementState>
            get() {
                if (!::_topicManagementState.isInitialized) {
                    _topicManagementState = MutableLiveData()
                }
                return _topicManagementState
            }

        // Navigate to topic detail view and display associated data
        fun onTopicSelected(topic: Topic) {
            _topicManagementState.value = TopicManagementState.GoToPosts(topic =  topic)
        }

        fun onGoToTopics(context: Context?){
            _topicManagementState.value = TopicManagementState.OnGoToTopics
            Toast.makeText(context, "Profile clicked", Toast.LENGTH_SHORT).show()
        }

        fun onGoToLatestNews(context: Context?){
            _topicManagementState.value = TopicManagementState.OnGoToLatestNews
            Toast.makeText(context, "Messages clicked", Toast.LENGTH_SHORT).show()
        }

        fun onPostSelected(latestPost: LatestPost){
            _topicManagementState.value = TopicManagementState.GoToPostsByLatestPost(latestPost)
        }

        fun navigateToCreateTopic(){
            _topicManagementState.value = TopicManagementState.NavigateToCreateTopic
        }

        fun onLogOut(context: Context){
            UserRepo.logOut(context = context)
            _topicManagementState.value = TopicManagementState.OnLogOut
        }

        fun onRetryButtonClicked(context: Context?) {
            _topicManagementState.value = TopicManagementState.Loading
            fetchTopicsAndHandleResponse(context = context)
        }

        fun onTopicsFragmentResumed(context: Context?) {
            _topicManagementState.value = TopicManagementState.Loading
            fetchTopicsAndHandleResponse(context = context)
        }

        fun onCreateTopicOptionClicked(context: Context, createTopicModel: CreateTopicModel) {
            if (isFormValid(model = createTopicModel)) {
                //todo habilitar loading

                val job = async{
                    val a = TopicsRepo.createTopic(
                        createTopicModel,
                        {
                            //todo deshabilitar loading
                            _topicManagementState.value = TopicManagementState.CreateTopicCompleted
                            TopicManagementState.TopicCreatedSuccessfully(msg = context.getString(R.string.message_topic_created))
                        },
                        {
                            //todo deshabilitar loading
                            _topicManagementState.value = TopicManagementState.CreateTopicCompleted
                            TopicManagementState.RequestErrorReported(requestError = it)
                        }
                    )

                    println("Done async")
                    a
                }

                launch(Dispatchers.Main) {
                    val response: Response<CreateTopicModel> = job.await()
                    println("Done await")

                    //todo deshabilitar loading
                    if (response.isSuccessful) {
                        response.body().takeIf { it != null }
                            ?.let {
                                _topicManagementState.value = TopicManagementState.CreateTopicCompleted
                                TopicManagementState.TopicCreatedSuccessfully(msg = context.getString(R.string.message_topic_created))
                            }
                            ?: run { _topicManagementState.value = TopicManagementState.CreateTopicCompleted
                                //Todo TopicManagementState.RequestErrorReported(requestError = it)
                                }
                    } else {
                        //Todo TopicManagementState.RequestErrorReported(requestError = it)
                    }
                    println("Done launch")
                }
                println("Done!")
            } else {
                //Todo show error
            }

        }



        private fun isFormValid(model: CreateTopicModel) =
            with(model) { title.isNotEmpty() && content.isNotEmpty() }

        private fun fetchTopicsAndHandleResponse(context: Context?) {
            context?.let {
                topicsRepo.getTopics(
                    { topics ->
                        _topicManagementState.value =
                            TopicManagementState.LoadTopicList(topicList = topics)
                    },
                    { error ->
                        _topicManagementState.value =
                            TopicManagementState.RequestErrorReported(requestError = error)
                    })
            }
        }
    }

