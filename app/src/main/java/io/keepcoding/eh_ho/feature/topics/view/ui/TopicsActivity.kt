package io.keepcoding.eh_ho.feature.topics.view.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import io.keepcoding.eh_ho.*
import io.keepcoding.eh_ho.feature.posts.EXTRA_TOPIC_ID
import io.keepcoding.eh_ho.feature.posts.EXTRA_TOPIC_TITLE
import io.keepcoding.eh_ho.feature.posts.PostsActivity
import io.keepcoding.eh_ho.domain.LatestPost
import io.keepcoding.eh_ho.domain.Topic
import io.keepcoding.eh_ho.data.service.RequestError
import io.keepcoding.eh_ho.di.DaggerApplicationGraph
import io.keepcoding.eh_ho.di.UtilsModule
import io.keepcoding.eh_ho.domain.CreateTopicModel
import io.keepcoding.eh_ho.feature.login.LoginActivity
import io.keepcoding.eh_ho.feature.topics.latestPosts.view.state.LatestPostManagementState
import io.keepcoding.eh_ho.feature.topics.latestPosts.view.ui.LATEST_NEWS_FRAGMENT_TAG
import io.keepcoding.eh_ho.feature.topics.latestPosts.view.ui.LatestNewsFragment
import io.keepcoding.eh_ho.feature.topics.latestPosts.viewmodel.LatestNewsViewModel
import io.keepcoding.eh_ho.feature.topics.view.state.TopicManagementState
import io.keepcoding.eh_ho.feature.topics.viewmodel.TopicViewModel
import kotlinx.android.synthetic.main.fragment_create_topic.*
import javax.inject.Inject

const val TRANSACTION_CREATE_TOPIC = "create_topic"

class TopicsActivity : AppCompatActivity(),
    TopicsFragment.TopicsInteractionListener, CreateTopicFragment.CreateTopicInteractionListener, NavigationView.OnNavigationItemSelectedListener,
    LatestNewsFragment.LatestNewsInteractionListener {


    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView
    @Inject
    lateinit var topicViewModel: TopicViewModel
    @Inject
    lateinit var latestNewsViewModel: LatestNewsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topics)

        DaggerApplicationGraph.builder()
            .utilsModule(UtilsModule(applicationContext)).build()
            .inject(this)

        initModel()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer,
                    TopicsFragment(),
                    TOPICS_FRAGMENT_TAG
                )
                .commit()
        }

        initNavigationDrawer()
    }

    private fun initModel() {
        topicViewModel.topicManagementState.observe(this, Observer { state ->
            when (state) {
                TopicManagementState.Loading -> enableLoadingView()
                is TopicManagementState.LoadTopicList -> loadTopicList(list = state.topicList)
                is TopicManagementState.GoToPosts -> goToPosts(state.topic)
                is TopicManagementState.GoToPostsByLatestPost -> goToPostsByLatestPost(state.latestPost)
                is TopicManagementState.TopicCreatedSuccessfully -> showMessage(msg = state.msg)
                is TopicManagementState.RequestErrorReported -> showRequestError(error = state.requestError)
                TopicManagementState.OnGoToTopics -> onGoToTopics()
                TopicManagementState.OnGoToLatestNews -> onGoToLatestNews()
                TopicManagementState.NavigateToCreateTopic -> navigateToCreateTopic()
                TopicManagementState.OnLogOut -> exit()
                TopicManagementState.CreateTopicCompleted -> {
                    onTopicCreated()
                }
            }
        })

        latestNewsViewModel.latestNewsManagementState.observe(this, Observer { state ->
            when(state){
                is LatestPostManagementState.RequestErrorReported -> showLatestNewsRequestError(error = state.requestError)
                is LatestPostManagementState.LoadPostList -> loadpostList(list = state.postList)
                LatestPostManagementState.Loading -> enableLatestNewsLoadingView()
            }
        })
    }

    private fun initNavigationDrawer(){
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_topics -> {
                topicViewModel.onGoToTopics(this)
            }
            R.id.nav_latest_news -> {
                topicViewModel.onGoToLatestNews(this)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onTopicSelected(topic: Topic) {
        topicViewModel.onTopicSelected(topic = topic)
    }

    override fun onPostSelected(latestPost: LatestPost) {
        topicViewModel.onPostSelected(latestPost)
    }

    override fun onGoToCreateTopic() {
        topicViewModel.navigateToCreateTopic()
    }

    override fun onLogOut() {
        topicViewModel.onLogOut(this)
    }

    override fun onTopicsFragmentResumed() {
        topicViewModel.onTopicsFragmentResumed(context = this)
    }

    override fun onRetryButtonClicked() {
        topicViewModel.onRetryButtonClicked(context = this)
    }

    override fun onRetryLatestNewsButtonClicked(){
        latestNewsViewModel.onRetryButtonClicked(context = this)
    }

    private fun onGoToTopics() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer,
                TopicsFragment(),
                TOPICS_FRAGMENT_TAG
            )
            .addToBackStack(TRANSACTION_CREATE_TOPIC)
            .commit()
    }

    private fun onGoToLatestNews(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer,
                LatestNewsFragment(),
                LATEST_NEWS_FRAGMENT_TAG
            )
            .addToBackStack(TRANSACTION_CREATE_TOPIC)
            .commit()
    }

    override fun onTopicCreated() {
        supportFragmentManager.popBackStack()
    }

    override fun onCreateTopicOptionClicked(model: CreateTopicModel) {
        topicViewModel.onCreateTopicOptionClicked(context = this, createTopicModel = model)
    }

    private fun goToPosts(topic: Topic) {
        val intent = Intent(this, PostsActivity::class.java)
        intent.putExtra(EXTRA_TOPIC_ID, topic.id)
        intent.putExtra(EXTRA_TOPIC_TITLE, topic.title)
        startActivity(intent)
    }


    private fun getTopicsFragmentIfAvailableOrNull(): TopicsFragment? {
        val fragment: Fragment? =
            supportFragmentManager.findFragmentByTag(TOPICS_FRAGMENT_TAG)

        return if (fragment != null && fragment.isVisible) {
            fragment as TopicsFragment
        } else {
            null
        }
    }

    private fun getLatestNewsFragmentIfAvailableOrNull(): LatestNewsFragment? {
        val fragment: Fragment? =
            supportFragmentManager.findFragmentByTag(LATEST_NEWS_FRAGMENT_TAG)

        return if (fragment != null && fragment.isVisible) {
            fragment as LatestNewsFragment
        } else {
            null
        }
    }

    private fun enableLoadingView() {
        getTopicsFragmentIfAvailableOrNull()?.enableLoading( true)
    }

    private fun enableLatestNewsLoadingView() {
        getLatestNewsFragmentIfAvailableOrNull()?.enableLoading( true)
    }

    private fun showRequestError(error: RequestError) {
        getTopicsFragmentIfAvailableOrNull()?.run {
            enableLoading(enabled = false)
            handleRequestError(requestError = error)
        }
    }

    private fun showLatestNewsRequestError(error: RequestError) {
        getLatestNewsFragmentIfAvailableOrNull()?.run {
            enableLoading(enabled = false)
            handleRequestError(requestError = error)
        }
    }


    private fun loadTopicList(list: List<Topic>) {
        getTopicsFragmentIfAvailableOrNull()?.run {
            enableLoading(enabled = false)
            loadTopicList(topicList = list)
        }
    }

    private fun loadpostList(list: List<LatestPost>) {
        Log.d("loadpostList","loadpostList")
        getLatestNewsFragmentIfAvailableOrNull()?.run {
            Log.d("enabled","enabled")

            enableLoading(enabled = false)
            loadLatestNews(latestNewsList = list)
        }
    }

    private fun goToPostsByLatestPost(latestPost: LatestPost) {
        val intent = Intent(this, PostsActivity::class.java)
        intent.putExtra(EXTRA_TOPIC_ID, latestPost.id)
        intent.putExtra(EXTRA_TOPIC_TITLE, latestPost.topic_title)
        startActivity(intent)
    }

    private fun navigateToCreateTopic(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer,
                CreateTopicFragment()
            )
            .addToBackStack(TRANSACTION_CREATE_TOPIC)
            .commit()
    }

    private fun exit(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}