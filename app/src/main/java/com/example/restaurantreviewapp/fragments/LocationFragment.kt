package com.example.restaurantreviewapp.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hotelreviewapp.R
import com.example.restaurantreviewapp.helper.HomeViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton

class LocationFragment : Fragment(), OnMapReadyCallback {

    private var longitude: Double = 0.0
    private var latitude: Double = 0.0

    private lateinit var locationByNetwork: Location
    private lateinit var locationByGps: Location
    private lateinit var mMap: GoogleMap

    private var currentLocation: Location? = null
    lateinit var locationManager: LocationManager
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        val view: View = inflater.inflate(R.layout.fragment_location, container, false)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        view.findViewById<MaterialButton>(R.id.save_button).setOnClickListener {
            Toast.makeText(requireActivity(), "Location Selected", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        //------------------------------------------------------//
        val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (hasGps) {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,
                0F,
                gpsLocationListener
            )
        }
//------------------------------------------------------//
        if (hasNetwork) {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                5000,
                0F,
                networkLocationListener
            )
        }

        val lastKnownLocationByGps =
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        lastKnownLocationByGps?.let {
            locationByGps = lastKnownLocationByGps
        }
//------------------------------------------------------//
        val lastKnownLocationByNetwork =
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        lastKnownLocationByNetwork?.let {
            locationByNetwork = lastKnownLocationByNetwork
        }
//------------------------------------------------------//
        if (locationByGps != null && locationByNetwork != null) {
            if (locationByGps.accuracy > locationByNetwork!!.accuracy) {
                currentLocation = locationByGps
                latitude = currentLocation!!.latitude
                longitude = currentLocation!!.longitude
                // use latitude and longitude as per your need
            } else {
                currentLocation = locationByNetwork
                latitude = currentLocation!!.latitude
                longitude = currentLocation!!.longitude
                // use latitude and longitude as per your need
            }
        }

    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0

        // Add a marker in Sydney and move the camera

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(latitude, longitude)
        viewModel.setLatLng(sydney)
        mMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney")
        )

        mMap.setMinZoomPreference(14.0f)
        mMap.setMaxZoomPreference(20.0f)

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        mMap.setOnMapClickListener {
            viewModel.setLatLng(it)
            mMap.clear()
            mMap.addMarker(
                MarkerOptions()
                    .position(it)
                    .title("Location")
            )
        }
    }


    private val gpsLocationListener: LocationListener =
        LocationListener { location -> locationByGps = location }

    //------------------------------------------------------//
    private val networkLocationListener: LocationListener =
        LocationListener { location -> locationByNetwork = location }
}