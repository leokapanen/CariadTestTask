package com.kapanen.cariadtesttask.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kapanen.cariadtesttask.R
import com.kapanen.cariadtesttask.databinding.FragmentMapBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapViewModel: MapViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mapViewModel = ViewModelProvider(this)[MapViewModel::class.java]
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        binding.fabFilterButton.setOnClickListener {
            findNavController().navigate(MapFragmentDirections.actionNavigationMapToNavigationFiltering())
        }

        mapViewModel.observePois().observe(viewLifecycleOwner) { pois ->
            Timber.d(pois.toString())
        }

        mapViewModel.observeError().observe(viewLifecycleOwner) { error ->
            val errorMsg = error.message?.let { errorStr ->
                String.format(
                    Locale.ENGLISH,
                    resources.getString(R.string.error_msg_format),
                    errorStr
                )
            } ?: resources.getText(R.string.general_error_msg)

            Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        mapViewModel.enableLiveUpdate()
    }

    override fun onPause() {
        super.onPause()
        mapViewModel.disableLiveUpdate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
