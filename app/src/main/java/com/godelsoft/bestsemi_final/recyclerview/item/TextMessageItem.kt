package com.godelsoft.bestsemi_final.recyclerview.item

import android.content.Context
import android.view.Gravity
import android.widget.FrameLayout
import com.godelsoft.bestsemi_final.EventsProvider
import com.godelsoft.bestsemi_final.R
import com.godelsoft.bestsemi_final.model.TextMessage
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_text_message.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.wrapContent
import java.text.SimpleDateFormat
import java.util.*


class TextMessageItem(
    val message: TextMessage,
    val context: Context
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.textView_message_text.text = message.text
        setTimeText(viewHolder)
        setMessageRootGravity(viewHolder)
    }

    private fun setTimeText(viewHolder: GroupieViewHolder) {
        val c = EventsProvider.getCalendarFromDate(message.time)
        viewHolder.textView_message_time.text = "${EventsProvider.formatDate(c)} ${EventsProvider.formatTime(c)}"
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

    override fun getLayout() = R.layout.item_text_message

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is TextMessageItem)
            return false
        if (this.message != other.message)
            return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? TextMessageItem)
    }

}
