package com.tsci.electioncountdown.presentation.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import com.tsci.electioncountdown.data.model.CountryItem
import com.tsci.electioncountdown.databinding.SettingsFragmentBinding
import com.tsci.electioncountdown.presentation.ui.MainViewModel
import com.tsci.electioncountdown.presentation.ui.MainViewModel.Companion.COUNTRY_NAME
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "SettingsFragment.kt"

@AndroidEntryPoint
class SettingsFragment : Fragment() {


    private val viewModel: MainViewModel by activityViewModels()

    private var _binding: SettingsFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var database: List<CountryItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSpinner()
    }

    private fun setUpSpinner() {
        binding.countrySpinner.adapter = CountryArrayAdapter(
            requireContext(),
            database
        )

        binding.countrySpinner.setSelection(
            viewModel.getCountryItemByName(
                PreferenceManager.getDefaultSharedPreferences(requireContext()).getString(COUNTRY_NAME, null).toString()
            )!!.id.toInt() - 1
        )

        binding.countrySpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                viewModel.countryItem.value = database[position]
                PreferenceManager.getDefaultSharedPreferences(context!!)
                    .edit()
                    .putString(COUNTRY_NAME, viewModel.countryItem.value!!.country)
                    .apply()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                viewModel.countryItem.value = viewModel.getCountryItemByName(
                    PreferenceManager.getDefaultSharedPreferences(context!!).getString(
                        COUNTRY_NAME, null).toString()
                )!!

            }
        }
    }
}