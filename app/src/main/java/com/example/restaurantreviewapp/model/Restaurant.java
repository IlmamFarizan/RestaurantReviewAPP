package com.example.restaurantreviewapp.model;

public class Restaurant {

    String id;
    String name;
    String location;
    Double lat;
    Double lon;
    String hotelImage;
    Float rating;
    String description;

    public Restaurant() {
    }

    public Restaurant(String id, String name, String location, Double lat, Double lon, String hotelImage, Float rating, String description) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.lat = lat;
        this.lon = lon;
        this.hotelImage = hotelImage;
        this.rating = rating;
        this.description = description;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getHotelImage() {
        return hotelImage;
    }

    public void setHotelImage(String hotelImage) {
        this.hotelImage = hotelImage;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}


