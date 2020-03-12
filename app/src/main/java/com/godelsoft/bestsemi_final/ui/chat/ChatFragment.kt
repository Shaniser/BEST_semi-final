package com.godelsoft.bestsemi_final.ui.chat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.godelsoft.bestsemi_final.AppConstants
import com.godelsoft.bestsemi_final.ChatActivity
import com.godelsoft.bestsemi_final.MainActivity
import com.godelsoft.bestsemi_final.R
import com.godelsoft.bestsemi_final.recyclerview.item.PersonItem
import com.godelsoft.bestsemi_final.util.FirestoreUtil
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item

class ChatFragment : Fragment() {

    private lateinit var chatViewModel: ChatViewModel

    private lateinit var userListenerRegistration: ListenerRegistration

    private var shouldInitRecyclerView = true

    private lateinit var container: ViewGroup

    private lateinit var peopleSection: Section

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        chatViewModel =
                ViewModelProviders.of(this).get(ChatViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_chat, container, false)
        if (container != null) {
            this.container = container
            userListenerRegistration =
                FirestoreUtil.addUsersListener(container.context, this::updateRecyclerView)
        }
        if (activity is MainActivity) {
            (activity as MainActivity).hideFAB()
            (activity as MainActivity).headerMain.text = ""
        }
        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        FirestoreUtil.removeListener(userListenerRegistration)
        shouldInitRecyclerView = true
    }

    private fun updateRecyclerView(items: List<Item>) {

        fun init() {
            container.findViewById<RecyclerView>(R.id.recycler_view_people).apply {
                layoutManager = LinearLayoutManager(this.context)
                adapter = GroupAdapter<GroupieViewHolder>().apply {
                    peopleSection = Section(items)
                    add(peopleSection)
                    setOnItemClickListener(onItemClick)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() = peopleSection.update(items)

        if (isVisible) {
            if (shouldInitRecyclerView)
                init()
            else
                updateItems()
        }
    }

    private val onItemClick = OnItemClickListener {item, view ->
        if (item is PersonItem) {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra(AppConstants.USER_NAME, item.person.name)
            intent.putExtra(AppConstants.USER_ID, item.userId)
            startActivity(intent)
        }
    }
}