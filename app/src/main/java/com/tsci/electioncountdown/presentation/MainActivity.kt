package com.tsci.electioncountdown.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.tsci.electioncountdown.R
import com.tsci.electioncountdown.presentation.ui.countdown.CountdownFragmentDirections
import dagger.hilt.android.AndroidEntryPoint


const val TAG = "MainActivity.kt"
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.settings -> {
                val action = CountdownFragmentDirections.actionCountdownFragmentToSettingsFragment()
                supportFragmentManager.primaryNavigationFragment!!
                .findNavController().navigate(action)
            }
        }
        return super.onOptionsItemSelected(item)
    }


}