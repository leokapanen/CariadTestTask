package com.kapanen.cariadtesttask.ui.filtering

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kapanen.cariadtesttask.databinding.FragmentFilteringBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilteringFragment : Fragment() {

    private var _binding: FragmentFilteringBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapViewModel: FilteringViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mapViewModel = ViewModelProvider(this)[FilteringViewModel::class.java]
        _binding = FragmentFilteringBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
