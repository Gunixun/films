package com.example.myapplication.ui

import android.content.IntentFilter
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.ui.contacts.ContactsFragment
import com.example.myapplication.ui.list.MoviesListFragment
import com.example.myapplication.utils.TypeMovies
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavToolBar {

    private val receiver = MainBroadcastReceiver()

    private val drawer: DrawerLayout by lazy {
        findViewById(R.id.drawer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            openFragment(MoviesListFragment(), false)
        }
        registerReceiver(receiver, IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION))

        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.action_top_rated -> {
                    openFragment(MoviesListFragment.newInstance(TypeMovies.TOP_RATED), false)
                    drawer.closeDrawer(GravityCompat.START)
                    return@setNavigationItemSelectedListener true
                }
                R.id.action_popular -> {
                    openFragment(MoviesListFragment.newInstance(TypeMovies.POPULAR), false)
                    drawer.closeDrawer(GravityCompat.START)
                    return@setNavigationItemSelectedListener true
                }
                R.id.action_now_playing -> {
                    openFragment(MoviesListFragment.newInstance(TypeMovies.NOW_PLAYING), false)
                    drawer.closeDrawer(GravityCompat.START)
                    return@setNavigationItemSelectedListener true
                }
                R.id.action_bookmarks -> {
                    openFragment(MoviesListFragment.newInstance(TypeMovies.BOOKMARKS), false)
                    drawer.closeDrawer(GravityCompat.START)
                    return@setNavigationItemSelectedListener true
                }
                R.id.action_history -> {
                    openFragment(MoviesListFragment.newInstance(TypeMovies.HISTORY), false)
                    drawer.closeDrawer(GravityCompat.START)
                    return@setNavigationItemSelectedListener true
                }
                R.id.action_contacts -> {
                    openFragment(ContactsFragment(), false)
                    drawer.closeDrawer(GravityCompat.START)
                    return@setNavigationItemSelectedListener true
                }
                R.id.action_settings -> {
                    openFragment(SettingsFragment(), true)
                    drawer.closeDrawer(GravityCompat.START)
                    return@setNavigationItemSelectedListener true
                }
            }
            false
        }

    }

    private fun openFragment(fragment: Fragment, withTransaction: Boolean) {
        if (withTransaction) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("Transaction")
                .commit()
        } else {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
        }
    }

    override fun supplyToolbar(toolbar: Toolbar) {
        val toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.nav_app_bar_open_drawer_description,
            R.string.nav_app_bar_navigate_up_description
        )

        drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

}