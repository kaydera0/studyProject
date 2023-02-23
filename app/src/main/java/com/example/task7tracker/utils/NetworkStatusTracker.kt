package com.example.task7tracker.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.example.task7tracker.viewModels.TrackingViewModel

class NetworkStatusTracker(context: Context, vm: TrackingViewModel) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    init {
        val networkStatusCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onUnavailable() = vm.networkStatus.postValue(false)
            override fun onAvailable(network: Network) = vm.networkStatus.postValue(true)
            override fun onLost(network: Network) = vm.networkStatus.postValue(false)
        }
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, networkStatusCallback)
    }
}