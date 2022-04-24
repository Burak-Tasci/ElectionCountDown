package com.tsci.electioncountdown.presentation.ui.countdown

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tsci.electioncountdown.R
import com.tsci.electioncountdown.databinding.CountdownFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val TAG = "CountdownFragment.kt"
@AndroidEntryPoint
class CountdownFragment : Fragment() {

    companion object {
        const val COUNTRY_NAME: String = "country_name"
    }

    private val viewModel: CountdownViewModel by viewModels()

    private var _binding: CountdownFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CountdownFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()

        viewModel.getProgress().observe(this.viewLifecycleOwner){
            binding.progressbar.progress = it
            binding.progressbarText.text = it.toString()
        }
        binding.countdown.start(viewModel.countDownTime())

        Glide
            .with(this)
            .load(viewModel.getFlagImageUrl())
            .apply(RequestOptions().override(800, 400))
            .placeholder(R.drawable.ic_launcher_background)
            .into(binding.flagItem.flagImageView)

        binding.changeCountryButton.setOnClickListener {
            val action = CountdownFragmentDirections.actionCountdownFragmentToSettingsFragment()
            findNavController().navigate(action)
        }
    }


    private fun hasAccessFineLocation() = ActivityCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun hasAccessCoarseLocation() = ActivityCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions(){

        val permissionsToRequest = mutableListOf<String>()
        if (!hasAccessCoarseLocation()){
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (!hasAccessFineLocation()){
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (permissionsToRequest.isNotEmpty()){
            ActivityCompat.requestPermissions(requireActivity(),
                permissionsToRequest.toTypedArray(),
                0
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty()){
            for(i in grantResults.indices){
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    Log.d(com.tsci.electioncountdown.presentation.TAG, "onRequestPermissionsResult: ${permissions[i]} granted.")
                }
            }
        }
    }
}