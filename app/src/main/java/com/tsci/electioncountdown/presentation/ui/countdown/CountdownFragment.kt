package com.tsci.electioncountdown.presentation.ui.countdown

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tsci.electioncountdown.R
import com.tsci.electioncountdown.data.model.getFlagImageUrl
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
        _binding = CountdownFragmentBinding.inflate(inflater, container, false)
        binding.mainViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.flagItem.mainViewModel = viewModel
        binding.flagItem.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.countryItem.observe(this.viewLifecycleOwner) { countryItem ->
            binding.countdown.start(viewModel.countDownTime())
            Glide
                .with(this)
                .load(countryItem.getFlagImageUrl())
                .apply(RequestOptions().override(800, 600))
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.flagItem.flagImageView)
        }

        binding.changeCountryButton.setOnClickListener {
            val action = CountdownFragmentDirections.actionCountdownFragmentToSettingsFragment()
            findNavController().navigate(action)
        }
    }

}