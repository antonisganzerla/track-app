package com.sgztech.rastreamento.util

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.sgztech.rastreamento.R
import com.sgztech.rastreamento.extension.showLog

object GoogleSignInUtil {

    fun googleSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
    }

    fun getAccount(context: Context): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }

    fun googleSignInClient(context: Context): GoogleSignInClient {
        return GoogleSignIn.getClient(context, googleSignInOptions())
    }

    fun googleSignLogout(activity: Activity, callback: () -> Unit) {
        googleSignInClient(activity).signOut()
            .addOnCompleteListener(activity) {
                callback()
            }
    }

    fun signOut(activity: AppCompatActivity) {
        googleSignLogout(activity) {
            showLog(activity.getString(R.string.msg_logout_success))
        }
    }

}