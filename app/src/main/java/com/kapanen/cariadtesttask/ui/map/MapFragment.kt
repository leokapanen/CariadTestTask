package com.kapanen.cariadtesttask.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener
import org.osmdroid.views.overlay.ItemizedOverlay
import org.osmdroid.views.overlay.OverlayItem
import java.util.*


private const val DEFAULT_ZOOM_LEVEL = 7.0
private const val DEFAULT_ZOOM_LEVEL_ON_TAP = 19.0

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
            binding.mapview.overlayManager.clear()
            pois.forEach { binding.mapview.addPin(it) }
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
        binding.mapview.overlayManager.clear()
        hideDetails()

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

            detailsAddressValue.text = poi.getAddressStr()

            detailsChargingPointsValue.text = poi.numberOfPoints.toString()
        }
    }

    private fun hideDetails() {
        binding.detailsCard.isVisible = false
    }

    private fun MapView.addPin(poi: Poi) {
        val geoPoint = GeoPoint(poi.addressInfo.latitude, poi.addressInfo.longitude)

        val overlayItem = OverlayItem(poi.addressInfo.title, poi.getAddressStr(), geoPoint)
        val markerDrawable = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.ic_pin
        )
        overlayItem.setMarker(markerDrawable)

        val overlayItemArrayList: ArrayList<OverlayItem> = ArrayList()
        overlayItemArrayList.add(overlayItem)
        val locationOverlay: ItemizedOverlay<OverlayItem> =
            ItemizedIconOverlay(overlayItemArrayList, object : OnItemGestureListener<OverlayItem> {
                override fun onItemSingleTapUp(i: Int, overlayItem: OverlayItem): Boolean {
                    controller.setZoom(DEFAULT_ZOOM_LEVEL_ON_TAP)
                    controller.setCenter(geoPoint)
                    showDetails(poi)
                    return true
                }

                override fun onItemLongPress(i: Int, overlayItem: OverlayItem): Boolean {
                    return false
                }
            }, requireContext())
        overlayManager.add(locationOverlay)
    }

    private fun Poi.getAddressStr(): String {
        return listOf(
            addressInfo.addressLine1,
            addressInfo.addressLine2,
            addressInfo.town,
            addressInfo.stateOrProvince,
            addressInfo.postcode,
            addressInfo.country?.title
        )
            .filter { !it.isNullOrBlank() }
            .joinToString(separator = addressSeparator)
    }

}
