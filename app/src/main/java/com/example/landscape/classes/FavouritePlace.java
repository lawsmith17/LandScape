package com.example.landscape.classes;

public class FavouritePlace {


        public String placeName;
        public String placeAddresses;
        public Double placeLongitude;
        public  Double placeLatitude;


    public FavouritePlace() {

    }
    public FavouritePlace(String placeName, String placeAddresses, Double placeLongitude, Double placeLatitude) {
        this.placeName = placeName;
        this.placeAddresses = placeAddresses;
        this.placeLongitude = placeLongitude;
        this.placeLatitude = placeLatitude;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceAddresses() {
        return placeAddresses;
    }

    public void setPlaceAddresses(String placeAddresses) {
        this.placeAddresses = placeAddresses;
    }

    public Double getPlaceLongitude() {
        return placeLongitude;
    }

    public void setPlaceLongitude(Double placeLongitude) {
        this.placeLongitude = placeLongitude;
    }

    public Double getPlaceLatitude() {
        return placeLatitude;
    }

    public void setPlaceLatitude(Double placeLatitude) {
        this.placeLatitude = placeLatitude;
    }
}

