package com.godelsoft.bestsemi_final

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.godelsoft.bestsemi_final.model.ImageMessage
import com.godelsoft.bestsemi_final.model.MessageType
import com.godelsoft.bestsemi_final.model.TextMessage
import com.godelsoft.bestsemi_final.util.FirestoreUtil
import com.godelsoft.bestsemi_final.util.StorageUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.activity_chat.*
import java.io.ByteArrayOutputStream
import java.util.*

private const val selectImage = 2


class ChatActivity : AppCompatActivity() {

    private lateinit var currentChannelId: String

    private lateinit var messagesListenerRegistration: ListenerRegistration
    private var shouldInitRecyclerView = true
    private lateinit var messagesSection: Section

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        findViewById<View>(R.id.conLay).apply {
            back.setOnClickListener {
                onBackPressed()
            }
            name.text = intent.getStringExtra(AppConstants.USER_NAME)
        }

        val otherUserId = intent.getStringExtra(AppConstants.USER_ID)!!
        FirestoreUtil.getOrCreateChatChannel(otherUserId) { channelId ->
            currentChannelId = channelId

            messagesListenerRegistration =
                FirestoreUtil.addChatMessagesListener(channelId, this, this::updateRecyclerView)

            send.setOnClickListener {
                if (!editText_message.text.toString().isBlank()) {
                    val messageToSend =
                        TextMessage(
                            editText_message.text.toString(), Calendar.getInstance().time,
                            FirebaseAuth.getInstance().currentUser!!.uid, MessageType.TEXT
                        )
                    editText_message.setText("")
                    FirestoreUtil.sendMessage(messageToSend, channelId)
                }
            }

            addImage.setOnClickListener {
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                }
                startActivityForResult(Intent.createChooser(intent, "Select Image"), selectImage)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == selectImage && resultCode == Activity.RESULT_OK &&
                data != null && data.data != null) {
            Log.e("IMAGE_TEST", "TEST2")
            val selectedImagePath = data.data

            val selectedImageBmp = MediaStore.Images.Media
                .getBitmap(contentResolver, selectedImagePath)
            val outputStream = ByteArrayOutputStream()
            Log.e("IMAGE_TEST", "TEST3")

            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            val selectedImageBytes = outputStream.toByteArray()

            Log.e("IMAGE_TEST", "TEST4")
            StorageUtil.uploadMessageImage(selectedImageBytes) { imagePath ->
                Log.e("IMAGE_TEST", "TEST5")
                val messageToSend =
                    ImageMessage(imagePath, Calendar.getInstance().time,
                        FirebaseAuth.getInstance().currentUser!!.uid)
                FirestoreUtil.sendMessage(messageToSend, currentChannelId)
            }
        }
    }

    private fun updateRecyclerView(messages: List<Item>) {
        fun init() {
            recycler_view_messages.apply {
                layoutManager = LinearLayoutManager(this@ChatActivity)
                adapter = GroupAdapter<GroupieViewHolder>().apply {
                    messagesSection = Section(messages)
                    this.add(messagesSection)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() = messagesSection.update(messages)

        if (shouldInitRecyclerView)
            init()
        else
            updateItems()

        recycler_view_messages.scrollToPosition(recycler_view_messages.adapter!!.itemCount - 1)
    }
}
