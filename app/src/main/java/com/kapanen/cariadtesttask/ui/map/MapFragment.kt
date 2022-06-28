package com.kapanen.cariadtesttask.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kapanen.cariadtesttask.BuildConfig
import com.kapanen.cariadtesttask.R
import com.kapanen.cariadtesttask.databinding.FragmentMapBinding
import com.kapanen.cariadtesttask.model.Poi
import com.kapanen.cariadtesttask.model.isActive
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import timber.log.Timber
import java.util.*

private const val DEFAULT_ZOOM_LEVEL = 10.0

@AndroidEntryPoint
class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapViewModel: MapViewModel

    private lateinit var connectionTypesSeparator: String
    private lateinit var addressSeparator: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mapViewModel = ViewModelProvider(this)[MapViewModel::class.java]
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        connectionTypesSeparator = resources.getString(R.string.connection_types_separator)
        addressSeparator = resources.getString(R.string.address_separator)

        binding.fabFilterButton.setOnClickListener {
            findNavController().navigate(MapFragmentDirections.actionNavigationMapToNavigationFiltering())
        }

        mapViewModel.observePois().observe(viewLifecycleOwner) { pois ->
            showDetails(pois[20])
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

        binding.detailsView.detailsCloseButton.setOnClickListener {
            hideDetails()
        }

        binding.mapview.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        binding.mapview.setTileSource(TileSourceFactory.MAPNIK)
        binding.mapview.setMultiTouchControls(true)
        binding.mapview.controller.setCenter(
            GeoPoint(
                BuildConfig.INITIAL_LATITUDE,
                BuildConfig.INITIAL_LONGITUDE
            )
        )
        binding.mapview.controller.setZoom(DEFAULT_ZOOM_LEVEL)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        mapViewModel.enableLiveUpdate()
        binding.mapview.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapViewModel.disableLiveUpdate()
        binding.mapview.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDetails(poi: Poi) {
        binding.detailsCard.isVisible = true
        binding.detailsView.apply {
            detailsTitle.text = poi.addressInfo.title
            detailsInactiveIcon.isVisible = !poi.isActive()

            detailsConnectionTypesValue.text =
                poi.connections.toSet()
                    .joinToString(separator = connectionTypesSeparator) { it.connectionType.title }

            detailsAddressValue.text =
                listOf(
                    poi.addressInfo.addressLine1,
                    poi.addressInfo.addressLine2,
                    poi.addressInfo.town,
                    poi.addressInfo.stateOrProvince,
                    poi.addressInfo.postcode,
                    poi.addressInfo.country?.title
                )
                    .filter { !it.isNullOrBlank() }
                    .joinToString(separator = addressSeparator)

            detailsChargingPointsValue.text = poi.numberOfPoints.toString()
        }
    }

    private fun hideDetails() {
        binding.detailsCard.isVisible = false
    }

}
