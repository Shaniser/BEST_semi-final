package com.godelsoft.bestsemi_final

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import com.firebase.ui.auth.AuthUI
import com.godelsoft.bestsemi_final.glide.GlideApp
import com.godelsoft.bestsemi_final.util.FirestoreUtil
import com.godelsoft.bestsemi_final.util.StorageUtil
import kotlinx.android.synthetic.main.activity_my_account.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.jetbrains.anko.toast
import java.io.ByteArrayOutputStream

class MyAccountActivity : AppCompatActivity() {

    private val selectImage = 2
    private lateinit var selectedImageBytes: ByteArray
    private var pictureJustChanged = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account)

        findViewById<View>(R.id.conLay).apply {
            back.setOnClickListener {
                onBackPressed()
            }
            imageView_profile.setOnClickListener {
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                }
                startActivityForResult(Intent.createChooser(intent, "SelectImage"), selectImage)
            }

            btn_save.setOnClickListener {
                if (::selectedImageBytes.isInitialized)
                    StorageUtil.uploadProfilePhoto(selectedImageBytes) { imagePath ->
                        FirestoreUtil.updateCurrentUser(
                            editText_name.text.toString(),
                            editText_bio.text.toString(), imagePath
                        )
                    }
                else
                    FirestoreUtil.updateCurrentUser(
                        editText_name.text.toString(),
                        editText_bio.text.toString(), null
                    )
                toast("Saving")
                //finish()
            }

            btn_sign_out.setOnClickListener {
                AuthUI.getInstance()
                    .signOut(this.context!!)
                    .addOnCompleteListener {
                        startActivity(intentFor<SignInActivity>().newTask().clearTask())
                    }
            }
        }

        FirestoreUtil.getCurrentUser { user ->
            if (!isDestroyed) {
                editText_name.setText(user.name)
                editText_bio.setText(user.bio)
                if (!pictureJustChanged && user.profilePicture != null)
                    GlideApp.with(this)
                        .load(StorageUtil.pathToReference(user.profilePicture))
                        .placeholder(R.drawable.ic_account_circle_black_24dp)
                        .into(imageView_profile)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == selectImage && resultCode == Activity.RESULT_OK &&
            data != null && data.data != null
        ) {
            val selectedImagePath = data.data
            val selectedImageBmp = MediaStore.Images.Media
                .getBitmap(contentResolver, selectedImagePath)

            val outputStream = ByteArrayOutputStream()
            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            selectedImageBytes = outputStream.toByteArray()

            GlideApp.with(this)
                .load(selectedImageBytes)
                .into(imageView_profile)

            pictureJustChanged = true
        }
    }
}