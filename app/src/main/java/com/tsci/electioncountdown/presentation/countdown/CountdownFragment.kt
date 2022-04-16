package com.tsci.electioncountdown.presentation.countdown

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tsci.electioncountdown.R
import com.tsci.electioncountdown.databinding.CountdownFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

const val TAG = "CountdownFragment.kt"
@AndroidEntryPoint
class CountdownFragment : Fragment() {


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


        viewModel.getProgress().observe(this.viewLifecycleOwner){
            binding.progressbar.progress = it
            binding.progressbarText.text = it.toString()
        }
        binding.countdown.start(viewModel.countDownTime())

        Glide
            .with(this)
            .load(viewModel.getFlagImageUrl())
            .apply(RequestOptions().override(600, 500))
            .placeholder(R.drawable.ic_launcher_background)
            .into(binding.flagItem.flagImageView)

    }


}