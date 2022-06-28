package com.kapanen.cariadtesttask

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import com.kapanen.cariadtesttask.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.config.Configuration


private const val MULTIPLE_PERMISSION_REQUEST_CODE = 4

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment_activity_main).navigateUp()
                || super.onSupportNavigateUp()
    }

    private fun checkPermissionsState() {
        val internetPermissionCheck: Int = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.INTERNET
        )
        val networkStatePermissionCheck: Int = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_NETWORK_STATE
        )
        val writeExternalStoragePermissionCheck: Int = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val coarseLocationPermissionCheck: Int = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val fineLocationPermissionCheck: Int = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val wifiStatePermissionCheck: Int = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_WIFI_STATE
        )
        if (internetPermissionCheck != PackageManager.PERMISSION_GRANTED
            && networkStatePermissionCheck != PackageManager.PERMISSION_GRANTED
            && writeExternalStoragePermissionCheck != PackageManager.PERMISSION_GRANTED
            && coarseLocationPermissionCheck != PackageManager.PERMISSION_GRANTED
            && fineLocationPermissionCheck != PackageManager.PERMISSION_GRANTED
            && wifiStatePermissionCheck != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE
                ),
                MULTIPLE_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MULTIPLE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    var somePermissionWasDenied = false
                    for (result in grantResults) {
                        if (result == PackageManager.PERMISSION_DENIED) {
                            somePermissionWasDenied = true
                        }
                    }
                    if (somePermissionWasDenied) {
                        Toast.makeText(
                            this,
                            "Cant load maps without all the permissions granted",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Cant load maps without all the permissions granted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
    }

}
