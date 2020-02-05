package io.keepcoding.eh_ho.topics

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
import com.google.android.material.navigation.NavigationView
import io.keepcoding.eh_ho.*
import io.keepcoding.eh_ho.Posts.EXTRA_TOPIC_ID
import io.keepcoding.eh_ho.Posts.EXTRA_TOPIC_TITLE
import io.keepcoding.eh_ho.Posts.PostsActivity
import io.keepcoding.eh_ho.data.LatestPost
import io.keepcoding.eh_ho.data.Topic
import io.keepcoding.eh_ho.data.UserRepo
import io.keepcoding.eh_ho.login.LoginActivity
import io.keepcoding.eh_ho.topics.LatestPosts.LatestNewsFragment
import kotlinx.android.synthetic.main.activity_topics.*

const val TRANSACTION_CREATE_TOPIC = "create_topic"

class TopicsActivity : AppCompatActivity(), TopicsFragment.TopicsInteractionListener, CreateTopicFragment.CreateTopicInteractionListener, NavigationView.OnNavigationItemSelectedListener,
    LatestNewsFragment.LatestNewsInteractionListener {


    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topics)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, TopicsFragment())
                .commit()
        }

        initNavigationDrawer()
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
                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
                onGoToTopics()
            }
            R.id.nav_latest_news -> {
                Toast.makeText(this, "Messages clicked", Toast.LENGTH_SHORT).show()
                onGoToLatestNews()
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onTopicSelected(topic: Topic) {
        goToPosts(topic)
    }

    override fun onPostSelected(latestPost: LatestPost) {
        goToPostsByLatestPost(latestPost)
    }

    override fun onGoToCreateTopic() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, CreateTopicFragment())
            .addToBackStack(TRANSACTION_CREATE_TOPIC)
            .commit()
    }

    private fun onGoToTopics() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, TopicsFragment())
            .addToBackStack(TRANSACTION_CREATE_TOPIC)
            .commit()
    }

    private fun onGoToLatestNews(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, LatestNewsFragment())
            .addToBackStack(TRANSACTION_CREATE_TOPIC)
            .commit()
    }

    override fun onLogOut() {
        UserRepo.logOut(this)

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onTopicCreated() {
        supportFragmentManager.popBackStack()
    }

    private fun goToPosts(topic: Topic) {
        val intent = Intent(this, PostsActivity::class.java)
        intent.putExtra(EXTRA_TOPIC_ID, topic.id)
        intent.putExtra(EXTRA_TOPIC_TITLE, topic.title)
        startActivity(intent)
    }

    private fun goToPostsByLatestPost(latestPost: LatestPost) {
        val intent = Intent(this, PostsActivity::class.java)
        intent.putExtra(EXTRA_TOPIC_ID, latestPost.id)
        intent.putExtra(EXTRA_TOPIC_TITLE, latestPost.topic_title)
        startActivity(intent)
    }

}