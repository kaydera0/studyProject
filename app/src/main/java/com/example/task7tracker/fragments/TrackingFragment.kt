package com.example.task7tracker.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.task7tracker.R
import com.example.task7tracker.databinding.FragmentTrackingBinding
import com.example.task7tracker.utils.NetworkStatusTracker
import com.example.task7tracker.viewModels.TrackingViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TrackingFragment @Inject constructor() : Fragment(), OnMapReadyCallback {

    private val vm: TrackingViewModel by viewModels()
    private lateinit var binding: FragmentTrackingBinding
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    private var isTrackingActive = false
    private var userName = ""
    private val USERNAME_TAG = "userName"
    private val DEFAULT_STR = "userName"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTrackingBinding.inflate(layoutInflater)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        getLocationPermission()

        userName = arguments?.getString(USERNAME_TAG, DEFAULT_STR).toString()
        vm.networkStatus.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it) {
                binding.trackingImage.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireActivity().resources, R.drawable.point, null
                    )
                )
                vm.sendLocationsFromDBToFirebase(userName)
            } else {
                binding.trackingImage.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireActivity().resources, R.drawable.point_gps_off, null
                    )
                )
            }
        })

        NetworkStatusTracker(requireContext(), vm)

        binding.startBtn.setOnClickListener {
            if (!isTrackingActive) {
                isTrackingActive = true
                if (vm.networkStatus.value == true) {
                    binding.trackingImage.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            requireActivity().resources, R.drawable.point_active, null
                        )
                    )
                    binding.startBtn.background = ResourcesCompat.getDrawable(
                        requireActivity().resources, R.drawable.btn_tracking, null
                    )
                    binding.startBtn.text = getText(R.string.stop)
                    binding.startBtn.setTextColor(resources.getColor(R.color.black))

                    CoroutineScope(Dispatchers.Main).launch {
                        while (isTrackingActive) {
                            vm.getDeviceLocation(userName)


                            binding.progressBar.setProgress(100.0f, true, 3000)
                            delay(3000)
                            binding.progressBar.setProgress(0.0f)
                        }
                    }
                } else {
                    Toast.makeText(context, getString(R.string.turn_on_internet), Toast.LENGTH_SHORT).show()
                }
            } else {
                isTrackingActive = false
                binding.trackingImage.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        requireActivity().resources, R.drawable.point, null
                    )
                )
                binding.startBtn.background = ResourcesCompat.getDrawable(
                    requireActivity().resources, R.drawable.btn_sign_enable, null
                )
                binding.startBtn.text = getText(R.string.start)
                binding.startBtn.setTextColor(resources.getColor(R.color.white))
                binding.progressBar.setProgress(0.0f)
            }
        }
        binding.exitBtn.setOnClickListener {
            findNavController().navigate(R.id.action_trackingFragment_to_loginFragment)
        }
        return binding.root
    }

    private fun getLocationPermission() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            vm.locationPermissionGranted.value = true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        vm.locationPermissionGranted.value = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    vm.locationPermissionGranted.value = true
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }
}