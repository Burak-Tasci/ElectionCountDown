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

}