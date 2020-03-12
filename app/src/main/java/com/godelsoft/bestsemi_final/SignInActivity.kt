package com.godelsoft.bestsemi_final

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import kotlinx.android.synthetic.main.activity_sign_in.*
import android.util.Log
import com.godelsoft.bestsemi_final.service.MyFirebaseInstanceIDService
import com.godelsoft.bestsemi_final.util.FirestoreUtil
import com.google.firebase.iid.FirebaseInstanceId
import org.jetbrains.anko.*


class SignInActivity : AppCompatActivity() {
    private val signIn = 1
    private val signInProviders =
        listOf(AuthUI.IdpConfig.EmailBuilder()
            .setAllowNewAccounts(true)
            .setRequireName(true)
            .build())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val intent = AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(signInProviders)
            .build()
        startActivityForResult(intent, signIn)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == signIn) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                val progressDialog = indeterminateProgressDialog("Setting up your account")
                FirestoreUtil.initCurrentUserIfFirstTime {
                    startActivity(intentFor<MainActivity>().newTask().clearTask())

                    val registrationToken = FirebaseInstanceId.getInstance().token
                    MyFirebaseInstanceIDService.addTokenToFirestore(registrationToken)

                    progressDialog.dismiss()
                }
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                if (response == null) return

                when (response.error?.errorCode) {
                    ErrorCodes.NO_NETWORK ->
                        toast("No network")
                    ErrorCodes.UNKNOWN_ERROR ->
                        toast("Unknown error")
                }
            }
        }
    }
}
