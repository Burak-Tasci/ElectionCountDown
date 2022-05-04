package com.tsci.electioncountdown.presentation.ui.settings

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tsci.electioncountdown.R
import com.tsci.electioncountdown.data.model.CountryItem
import com.tsci.electioncountdown.data.model.getFlagImageUrl
import com.tsci.electioncountdown.presentation.ui.MainViewModel
import com.tsci.electioncountdown.presentation.ui.MainViewModel.Companion.COUNTRY_NAME
import com.tsci.electioncountdown.presentation.ui.countdown.CountdownFragment

private const val TAG = "CountryArrayAdapter.kt"

class CountryArrayAdapter(
    context: Context,
    database: List<CountryItem>
) : ArrayAdapter<CountryItem>(
    context, 0,
    database
) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {

        val country = getItem(position)
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.spinner_item, parent, false)
        Glide
            .with(context)
            .load(country!!.getFlagImageUrl())
            .apply(RequestOptions().override(48))
            .placeholder(R.drawable.ic_launcher_background)
            .into(view.findViewById(R.id.country_image))
        country.country.also { view.findViewById<TextView>(R.id.country_name).text = it }

        return view
    }

}