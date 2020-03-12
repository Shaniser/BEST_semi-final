package com.godelsoft.bestsemi_final

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.jetbrains.anko.startActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        if (FirebaseAuth.getInstance().currentUser == null)
            startActivity<SignInActivity>()
        else
            startActivity<MainActivity>()

        finish()
    }
}