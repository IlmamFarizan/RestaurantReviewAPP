package com.example.restaurantreviewapp.helper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantreviewapp.model.Restaurant
import com.google.android.gms.maps.model.LatLng

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    private val ltLg = MutableLiveData<LatLng>().apply {
        0.0
    }

    private val restaurant = MutableLiveData<Restaurant>().apply {
    }

    val ltLng: MutableLiveData<LatLng> = ltLg
    var selectedRestaurant: MutableLiveData<Restaurant> = restaurant

    fun setLatLng(lat: LatLng) {
        ltLng.value = lat
    }

    fun getLatLng(): MutableLiveData<LatLng> {
        return ltLng
    }


    fun setHotel(lat: Restaurant) {
        selectedRestaurant.value = lat
    }

    fun getHotel(): MutableLiveData<Restaurant> {
        return selectedRestaurant
    }

}