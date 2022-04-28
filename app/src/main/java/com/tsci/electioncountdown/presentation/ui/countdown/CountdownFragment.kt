package com.tsci.electioncountdown.presentation.ui.countdown

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tsci.electioncountdown.R
import com.tsci.electioncountdown.databinding.CountdownFragmentBinding
import com.tsci.electioncountdown.presentation.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "CountdownFragment.kt"

@AndroidEntryPoint
class CountdownFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    private var _binding: CountdownFragmentBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.countdown_fragment,
            container,
            false
        )
        binding.run {
            mainViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
            flagItem.mainViewModel = viewModel
            flagItem.lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()


        viewModel.countryItem.observe(this.viewLifecycleOwner) {
            binding.countdown.start(viewModel.countDownTime())
            Glide
                .with(this)
                .load(viewModel.getFlagImageUrl())
                .apply(RequestOptions().override(800, 600))
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.flagItem.flagImageView)
        }



        GlobalScope.launch {
            while (true) {
                Log.d(TAG, "onViewCreated: ${viewModel.countryItem.value}")
                delay(5000)
            }
        }

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

    private fun requestPermissions() {

        val permissionsToRequest = mutableListOf<String>()
        if (!hasAccessCoarseLocation()) {
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (!hasAccessFineLocation()) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
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
        if (requestCode == 0 && grantResults.isNotEmpty()) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(
                        com.tsci.electioncountdown.presentation.TAG,
                        "onRequestPermissionsResult: ${permissions[i]} granted."
                    )
                }
            }
        }
    }
}