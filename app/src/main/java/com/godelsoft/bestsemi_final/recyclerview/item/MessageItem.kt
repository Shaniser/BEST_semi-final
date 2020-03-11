package com.godelsoft.bestsemi_final.recyclerview.item

import android.view.Gravity
import android.widget.FrameLayout
import com.godelsoft.bestsemi_final.EventsProvider
import com.godelsoft.bestsemi_final.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_text_message.*
import org.jetbrains.anko.wrapContent


abstract class MessageItem(
    private val message: Message
) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        setTimeText(viewHolder)
        setMessageRootGravity(viewHolder)
    }

    private fun setTimeText(viewHolder: GroupieViewHolder) {
        val c = EventsProvider.getCalendarFromDate(message.time)
        viewHolder.textView_message_time.text =
            "${EventsProvider.formatDate(c)} ${EventsProvider.formatTime(c)}"
    }

    private fun setMessageRootGravity(viewHolder: GroupieViewHolder) {
        if (message.senderId == FirebaseAuth.getInstance().currentUser?.uid) {
            viewHolder.message_root.apply {
                val lParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.END)
                this.layoutParams = lParams
            }
        }
        else {
            viewHolder.message_root.apply {
                val lParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.START)
                this.layoutParams = lParams
            }
        }
    }
}
