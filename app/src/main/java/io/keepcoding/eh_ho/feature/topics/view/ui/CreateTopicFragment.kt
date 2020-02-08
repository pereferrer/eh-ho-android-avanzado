package io.keepcoding.eh_ho.feature.topics.view.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.keepcoding.eh_ho.feature.LoadingDialogFragment
import io.keepcoding.eh_ho.R
import io.keepcoding.eh_ho.domain.CreateTopicModel
import io.keepcoding.eh_ho.data.service.RequestError
import io.keepcoding.eh_ho.data.repository.TopicsRepo
import kotlinx.android.synthetic.main.fragment_create_topic.*

const val TAG_LOADING_DIALOG = "loading_dialog"

class CreateTopicFragment : Fragment() {

    var listener: CreateTopicInteractionListener? = null
    lateinit var loadingDialog: LoadingDialogFragment

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is CreateTopicInteractionListener)
            listener = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        loadingDialog = LoadingDialogFragment.newInstance(getString(R.string.label_create_topic))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_topic, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_create_topic, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_send -> listener?.onCreateTopicOptionClicked(
                model = CreateTopicModel(
                    title = inputTitle.text.toString(),
                    content = inputContent.text.toString()
                )
            )
        }

        return super.onOptionsItemSelected(item)
    }


    interface CreateTopicInteractionListener {
        fun onTopicCreated()
        fun onCreateTopicOptionClicked(model: CreateTopicModel)
    }
}