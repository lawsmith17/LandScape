package com.example.landscape.utils;

import com.google.android.gms.maps.GoogleMap;

public class Tools {


    public static final String SHARED_PREF_NAME ="appPref";

    public   static final String CHILD_USER_LOGS ="LOG";
    public static final String CHILD_METRIC= "METRIC";
    public static final String CHILD_USER_INFO= "USER_INFO";
    public static final String USER_EMAIL= "USERS";
    public  static  final String UID="UID";

    public static final String USER_DISTANCE_METRIC="USER_DISTANCE_METRIC";

    public static  int ParseInteger(String gr) throws NumberFormatException
    {
        return Integer.parseInt(gr);
    }

    public static GoogleMap configActivityMaps(GoogleMap googleMap) {

        //Copy this it for the bottom navigation bar
        // set map type
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Enable / Disable zooming controls
        googleMap.getUiSettings().setZoomControlsEnabled(false);

        // Enable / Disable Compass icon
        googleMap.getUiSettings().setCompassEnabled(true);
        // Enable / Disable Rotate gesture
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        // Enable / Disable zooming functionality
        googleMap.getUiSettings().setZoomGesturesEnabled(true);

        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);

        return googleMap;
    }
}
