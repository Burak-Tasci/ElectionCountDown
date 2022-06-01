package com.tsci.electioncountdown.presentation.ui.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tsci.electioncountdown.data.model.CountryItem
import com.tsci.electioncountdown.data.model.getFlagImageUrl

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
            .inflate(com.tsci.electioncountdown.R.layout.spinner_item, parent, false)
        Glide
            .with(context)
            .load(country!!.getFlagImageUrl())
            .apply(RequestOptions().override(48))
            .placeholder(com.tsci.electioncountdown.R.drawable.loading_animation)
            .into(view.findViewById(com.tsci.electioncountdown.R.id.country_image))
        country.country.also { view.findViewById<TextView>(com.tsci.electioncountdown.R.id.country_name).text = it }

        return view
    }

}