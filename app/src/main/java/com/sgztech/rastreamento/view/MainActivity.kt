package com.sgztech.rastreamento.view

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.sgztech.rastreamento.R
import com.sgztech.rastreamento.extension.openActivity
import com.sgztech.rastreamento.util.AlertDialogUtil
import com.sgztech.rastreamento.util.GoogleSignInUtil.signOut
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.view.*
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : AppCompatActivity() {

    private val account: GoogleSignInAccount? by lazy {
        GoogleSignIn.getLastSignedInAccount(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar()
        setupDrawer()
        openTrackFragment()
    }

    private fun setupToolbar() {
        toolbar.title = getString(R.string.toolbar_title_main)
        setSupportActionBar(toolbar)
    }

    private fun setupDrawer() {
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        setupDrawerItemClickListener()
        setupHeaderDrawer()
    }

    private fun setupDrawerItemClickListener() {
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_item_logout -> {
                    showDialogLogout()
                }
                R.id.nav_item_about -> {
                    AlertDialogUtil.showSimpleDialog(
                        this,
                        R.string.dialog_about_app_title,
                        R.string.dialog_about_app_message
                    )
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun showDialogLogout() {
        val dialog = AlertDialogUtil.create(
            this,
            R.string.dialog_message_logout
        ) {
            logout()
        }
        dialog.show()
    }

    private fun logout() {
        signOut(this)
        openActivity(LoginActivity::class.java)
    }

    private fun setupHeaderDrawer() {
        val headerView = navView.getHeaderView(0)
        headerView?.let {
            it.nav_header_name.text = account?.displayName
            it.nav_header_email.text = account?.email
            Picasso.get().load(account?.photoUrl).into(it.nav_header_imageView)
        }
    }

    private fun openTrackFragment(){
        supportFragmentManager.beginTransaction().replace(R.id.content_frame, TrackFragment(), null)
            .commitAllowingStateLoss()
    }
}
