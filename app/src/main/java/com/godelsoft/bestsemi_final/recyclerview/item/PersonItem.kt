package com.godelsoft.bestsemi_final.recyclerview.item

import android.content.Context
import com.godelsoft.bestsemi_final.R
import com.godelsoft.bestsemi_final.glide.GlideApp
import com.godelsoft.bestsemi_final.model.User
import com.godelsoft.bestsemi_final.util.StorageUtil
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.person_card.*

class PersonItem(
    val person: User,
    val userId: String,
    private val context: Context
) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.textView_name.text = person.name
        viewHolder.textView_bio.text = person.bio
        if (person.profilePicture != null)
            GlideApp.with(context)
                .load(StorageUtil.pathToReference(person.profilePicture))
                .placeholder(R.drawable.ic_account_circle_black_24dp)
                .into(viewHolder.imageView_profile_picture)
    }

    override fun getLayout() = R.layout.person_card
}
