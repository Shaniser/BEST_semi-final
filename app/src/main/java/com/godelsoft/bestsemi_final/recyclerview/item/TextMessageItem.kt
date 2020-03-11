package com.godelsoft.bestsemi_final.recyclerview.item

import android.content.Context
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import android.view.Gravity
import android.widget.FrameLayout
import com.godelsoft.bestsemi_final.R
import com.godelsoft.bestsemi_final.model.TextMessage
import com.godelsoft.bestsemi_final.util.CalFormatter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.card_text_message.*
import org.jetbrains.anko.wrapContent


class TextMessageItem(
    val message: TextMessage,
    val context: Context
) : MessageItem(message) {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.textView_message_text.text = message.text
        super.bind(viewHolder, position)
    }

    override fun getLayout() = R.layout.card_text_message

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

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }

}
