package com.example.landscape.activities;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.landscape.R;
import com.example.landscape.classes.FavouritePlace;
import com.example.landscape.login_and_register.LoginRegisterActivity;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.landscape.utils.constants.CURRENT_DISTANCE_METRIC;


// This is the main class everything mostly runs through here for example the functions of the app like the map, the bottom navbar the search bar etc.
public class MainActivity extends BaseActivity  implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback , RoutingListener {


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private int menuOneSelectedItemId;

    private static final String TAG = MainActivity.class.getSimpleName();


    private GoogleMap map;
    private CameraPosition cameraPosition;

    // The entry point to the Places API.
    private PlacesClient placesClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;



    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    int seletedIndex;

    // Keys for storing activity state.
    // [START maps_current_place_state_keys]
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] likelyPlaceNames;
    private String[] likelyPlaceAddresses;
    private List[] likelyPlaceAttributions;
    private LatLng[] likelyPlaceLatLngs;


    // using google direction to draw ploylines

    private List<Polyline> polylines;
    protected LatLng start;
    protected LatLng end;


    // views
    TextView name;
    TextView distance_and_time;
    RatingBar bar;
    TextView rate_text;
    TextView address;
    TextView phone;
    TextView email;
    TextView openTime;

    Button button,savebtn;

    FirebaseDatabase database;

    private BottomSheetBehavior bottomSheetBehavior;

    String userID;
    DatabaseReference reference;

    FirebaseAuth mAuth;

    String Email;
    AutocompleteSessionToken token;
    private MaterialSearchBar materialSearchBar;
    private List<AutocompletePrediction> predictionList;

    LatLng markerLatLng ;
    String markerSnippet ;
    String markerplace ;

    String sName;
    String sAdress;
   double sLong;
    double sLat;
    public  static  boolean selectedSavedLocation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // [START maps_current_place_on_create_save_instance_state]
        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }


        setContentView(R.layout.activity_main);
button = findViewById(R.id.button);
        savebtn = findViewById(R.id.save_loc);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation_view);
        materialSearchBar = findViewById(R.id.searchBar);
        navigationView.setNavigationItemSelectedListener(this);




        name = findViewById(R.id.LocationName);
        distance_and_time = findViewById(R.id.distance_and_time);
        bar = findViewById(R.id.rate_bar);
        rate_text = findViewById(R.id.rate_text);
        address = findViewById(R.id.adresss);
        phone = findViewById(R.id.phone);
        openTime = findViewById(R.id.open_time);
        mAuth  = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
     userID = mAuth.getCurrentUser().getUid();
        reference = database.getReference("Users");
        Email = mAuth.getCurrentUser().getEmail();

        handleHeaderToggle();
        // Construct a PlacesClient
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        token = AutocompleteSessionToken.newInstance();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        initSearchBar();
        initComponent();

        if(selectedSavedLocation) {
            sName= getIntent().getStringExtra("placeName");
        sAdress = getIntent().getStringExtra("placeAddresses");
           sLong = getIntent().getDoubleExtra("placeLongitude", 12);
            sLat = getIntent().getDoubleExtra("placeLatitude", 12);


        }


        button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        showCurrentPlace();
    }
});


savebtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        savetodata();
    }
});

        FloatingActionButton routeGps = findViewById(R.id.fab_gps);
        routeGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (start != null && end != null) {
                    map.clear();
                    DisplayRoute(end.latitude, end.longitude);
                } else {
                    Toast.makeText(MainActivity.this, "please pick a destination first", Toast.LENGTH_LONG).show();
                }
            }
        });
       toggleNavigationDrawer();

      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.current_place_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.option_get_place) {
            showCurrentPlace();
        }
        return true;
    }


    private void handleHeaderToggle() {
        View header = navigationView.getHeaderView(0);
        email= header.findViewById(R.id.textView_email);

        email.setText(Email);
    }

    private void initSearchBar()
    {
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString(), true, null, true);
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                if (buttonCode == MaterialSearchBar.BUTTON_NAVIGATION) {
                 if(drawerLayout.isDrawerOpen(GravityCompat.START))
                 {
                     drawerLayout.closeDrawers();

                 }else
                 {
                     drawerLayout.openDrawer(GravityCompat.START);
                 }
                } else if (buttonCode == MaterialSearchBar.BUTTON_BACK) {
                    materialSearchBar.disableSearch();
                }
            }
        });


        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()
                        .setSessionToken(token)
                        .setQuery(s.toString())
                        .build();
                placesClient.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {
                        if (task.isSuccessful()) {
                            FindAutocompletePredictionsResponse predictionsResponse = task.getResult();
                            if (predictionsResponse != null) {
                                predictionList = predictionsResponse.getAutocompletePredictions();
                                List<String> suggestionsList = new ArrayList<>();
                                for (int i = 0; i < predictionList.size(); i++) {
                                    AutocompletePrediction prediction = predictionList.get(i);
                                    suggestionsList.add(prediction.getFullText(null).toString());
                                }
                                materialSearchBar.updateLastSuggestions(suggestionsList);
                                if (!materialSearchBar.isSuggestionsVisible()) {
                                    materialSearchBar.showSuggestionsList();
                                }
                            }
                        } else {
                            Log.i("mytag", "prediction fetching task unsuccessful");
                        }
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        materialSearchBar.setSuggstionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v) {
                if (position >= predictionList.size()) {
                    return;
                }
                AutocompletePrediction selectedPrediction = predictionList.get(position);
                String suggestion = materialSearchBar.getLastSuggestions().get(position).toString();
                materialSearchBar.setText(suggestion);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        materialSearchBar.clearSuggestions();
                    }
                }, 1000);
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(materialSearchBar.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                final String placeId = selectedPrediction.getPlaceId();


                List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.RATING, Place.Field.USER_RATINGS_TOTAL, Place.Field.ADDRESS, Place.Field.PHONE_NUMBER, Place.Field.OPENING_HOURS);

                FetchPlaceRequest fetchPlaceRequest = FetchPlaceRequest.builder(placeId, placeFields).build();
                placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                    @Override
                    public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                        Place place = fetchPlaceResponse.getPlace();


                        name.setText(place.getName());

                       distance_and_time.setText("12 min away 4"+getPString(CURRENT_DISTANCE_METRIC,"Meters"));

                       address.setText(place.getAddress());


                        Log.i(TAG, "Place found: " + place.getName());
                        LatLng latLngOfPlace = place.getLatLng();
                        String snnipet = place.getAddress();
                        if (latLngOfPlace != null) {
                           end = latLngOfPlace;
                            map.clear();
                          start = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());

                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOfPlace, DEFAULT_ZOOM));
                            MakeMaker( place.getName(),latLngOfPlace,snnipet);
                           markerLatLng= latLngOfPlace;
                           markerSnippet = snnipet;
                           markerplace = place.getName();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            apiException.printStackTrace();
                            int statusCode = apiException.getStatusCode();
                            Log.i("mytag", "place not found: " + e.getMessage());
                            Log.i("mytag", "status code: " + statusCode);
                        }
                    }
                });
            }

            @Override
            public void OnItemDeleteListener(int position, View v) {

            }
        });
    }



    public  void MakeMaker(String title,LatLng position,String snippet)
    {
        markerLatLng= position;
        markerSnippet = snippet;
        markerplace = title;

        name.setText(title);

        distance_and_time.setText("12 min away 4"+getPString(CURRENT_DISTANCE_METRIC,"Meters"));

        address.setText(snippet);

        map.addMarker(new MarkerOptions()
                .title(title)
                .position(position)
                .snippet(snippet));

    }



    private void toggleNavigationDrawer() {
        drawerLayout.addDrawerListener(new  ActionBarDrawerToggle(this, drawerLayout,
                R.string.open, R.string.close)
        {

        @Override
        public void onDrawerClosed(View drawerView)
        {
            super.onDrawerClosed(drawerView);

        }

        });


    }








    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        if (menuItem.getItemId() == menuOneSelectedItemId) {
            // Do nothing so that the fragment is not re-initialised
            Log.d("menu_primary", "onNavigationItemSelected: Same menuitem");
        } else {
            switch (menuItem.getItemId()) {
                case R.id.nav_saved_locations:

                   Intent location = new Intent(MainActivity.this,SavedLocationsActivity.class);
                   startActivity(location);

                    break;
                case R.id.nav_settings:

                   Intent settings = new Intent(MainActivity.this,SettingsActivity.class);
               startActivity(settings);

                    break;
                case R.id.nav_logout:
                    logout();
                    break;

            }
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;

    }

    public  void logout()
    {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), LoginRegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        map = googleMap;


        this.map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = infoWindow.findViewById(R.id.title);
                title.setText(marker.getTitle());

                TextView snippet = infoWindow.findViewById(R.id.snippet);
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                  end = latLng;
                map.clear();
                start = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
getAdress(latLng,false);


            }

        });

        getLocationPermission();
        // [END_EXCLUDE]

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();


    }







public void getAdress(LatLng latLng,boolean def)
{
    Geocoder geocoder;
    List<Address> addresses;
    geocoder = new Geocoder(this, Locale.getDefault());

    try {


    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

    String address1 = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
    String city = addresses.get(0).getLocality();
    String state = addresses.get(0).getAdminArea();
    String country = addresses.get(0).getCountryName();
    String postalCode = addresses.get(0).getPostalCode();
    String knownName = addresses.get(0).getFeatureName();

    if (def)
    {
        markerSnippet = address1;
        markerplace = knownName;
        markerLatLng = latLng;
        name.setText(knownName);

        distance_and_time.setText("0 min away 0"+getPString(CURRENT_DISTANCE_METRIC,"Meters"));

        address.setText(address1);
    }
    else
    {
        MakeMaker(knownName,latLng,address1);
    }

    }catch (IOException e)
    {
        MakeMaker("Location",latLng,latLng.toString());
    }
}














    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                if(selectedSavedLocation)
                                {


                                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                                new LatLng(sLat,
                                                        sLong), DEFAULT_ZOOM));
                                    LatLng s= new LatLng(sLat,
                                    sLong);
                                    MakeMaker(sName,s,sAdress);

                                }
                                else {
                                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                            new LatLng(lastKnownLocation.getLatitude(),
                                                    lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                    LatLng defplace = new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude());
                                    getAdress(defplace, true);
                                }
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }




    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    // [START maps_current_place_show_current_place]
    private void showCurrentPlace() {
        if (map == null) {
            return;
        }

        if (locationPermissionGranted) {
            // Use fields to define the data types to return.
            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.LAT_LNG);

            // Use the builder to create a FindCurrentPlaceRequest.
            FindCurrentPlaceRequest request =
                    FindCurrentPlaceRequest.newInstance(placeFields);

            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final
            Task<FindCurrentPlaceResponse> placeResult =
                    placesClient.findCurrentPlace(request);
            placeResult.addOnCompleteListener (new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        FindCurrentPlaceResponse likelyPlaces = task.getResult();

                        // Set the count, handling cases where less than 5 entries are returned.
                        int count;
                        if (likelyPlaces.getPlaceLikelihoods().size() < M_MAX_ENTRIES) {
                            count = likelyPlaces.getPlaceLikelihoods().size();
                        } else {
                            count = M_MAX_ENTRIES;
                        }

                        int i = 0;
                        likelyPlaceNames = new String[count];
                        likelyPlaceAddresses = new String[count];
                        likelyPlaceAttributions = new List[count];
                        likelyPlaceLatLngs = new LatLng[count];

                        for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
                            // Build a list of likely places to show the user.
                            likelyPlaceNames[i] = placeLikelihood.getPlace().getName();
                            likelyPlaceAddresses[i] = placeLikelihood.getPlace().getAddress();
                            likelyPlaceAttributions[i] = placeLikelihood.getPlace()
                                    .getAttributions();
                            likelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                            i++;
                            if (i > (count - 1)) {
                                break;
                            }
                        }

                        // Show a dialog offering the user the list of likely places, and add a
                        // marker at the selected place.
                        MainActivity.this.openPlacesDialog();
                    }
                    else {
                        Log.e(TAG, "Exception: %s", task.getException());
                    }
                }
            });
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            map.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(defaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }


    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
    // [START maps_current_place_open_places_dialog]
    private void openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               seletedIndex = which;
                // The "which" argument contains the position of the selected item.
                LatLng markerLatLng = likelyPlaceLatLngs[which];
                String markerSnippet = likelyPlaceAddresses[which];
                if (likelyPlaceAttributions[which] != null) {
                    markerSnippet = markerSnippet + "\n" + likelyPlaceAttributions[which];
                }

                // Add a marker for the selected place, with an info window
                // showing information about that place.
                map.clear();
                map.addMarker(new MarkerOptions()
                        .title(likelyPlaceNames[which])
                        .position(markerLatLng)
                        .snippet(markerSnippet));

                // Position the map's camera at the location of the marker.
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        DEFAULT_ZOOM));


                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                start= start = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());;
end= likelyPlaceLatLngs[which];

                String timeAndDuration = "5 "+getPString(CURRENT_DISTANCE_METRIC,"Meters")+" And 5 mins  away ";
                name.setText(likelyPlaceNames[which]);
                address.setText(likelyPlaceAddresses[which]);
                distance_and_time.setText(timeAndDuration);

            }
        };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.pick_place)
                .setItems(likelyPlaceNames, listener)
                .show();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */

    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }




    public void savetodata()
    {

        if(markerLatLng != null && markerSnippet != null && markerplace != null)
        {
            FavouritePlace placeselected = new FavouritePlace(markerplace, markerSnippet, markerLatLng.longitude, markerLatLng.latitude);
            Map<String, Object> upd = new HashMap<String, Object>();


            upd.put("placeName", placeselected.getPlaceName());
            upd.put("placeAddresses", placeselected.getPlaceAddresses());
            upd.put("placeLongitude", placeselected.getPlaceLongitude());
            upd.put("placeLatitude", placeselected.getPlaceLatitude());


            reference.child(userID).child("SavedLocations").child(markerplace).updateChildren(upd);
        }
        else {




        }
    }


    //making the bottom navgation bar

    private void initComponent() {
        // get the bottom sheet view
        LinearLayout llBottomSheet =  findViewById(R.id.bottom_sheet);

        // init the bottom sheet behavior
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);

        // change the state of the bottom sheet
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        (findViewById(R.id.fab_directions)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                try {
                    //this is where the onClick is for opening the navigation for google maps
                    Findroutes(start, end);

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "please pick a destination first", Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    public void Findroutes(LatLng Start, LatLng End) {
        if (Start == null || End == null) {
            Toast.makeText(MainActivity.this, "Unable to get location", Toast.LENGTH_LONG).show();
        } else {
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key( )  //dont forget to put your google api key here and in the google_map_api.xml in the res folder before runing 
                    .build();
            routing.execute();


        }
    }


    @Override
    public void onRoutingFailure(RouteException e) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG);
        snackbar.show();

    }

    //start to find shortest distance
    @Override
    public void onRoutingStart() {
        Toast.makeText(MainActivity.this, "Finding Route...", Toast.LENGTH_LONG).show();
    }

    //when finding  the the shortest distance between the user and the and selected point is successful
    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        map.clear();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        CameraUpdate center = CameraUpdateFactory.newLatLng(start);

        String timeAndDuration = route.get(shortestRouteIndex).getDurationText() + "  And   " + route.get(shortestRouteIndex).getDistanceText() + " away ";
        name.setText(route.get(shortestRouteIndex).getName());
        address.setText(route.get(shortestRouteIndex).getEndAddressText());
        distance_and_time.setText(timeAndDuration);


//This is where the line is drawn
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        if (polylines != null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng = null;
        LatLng polylineEndLatLng = null;

        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i < route.size(); i++) {

            if (i == shortestRouteIndex) {
                polyOptions.color(getResources().getColor(R.color.colorPrimary));
                polyOptions.width(7);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = map.addPolyline(polyOptions);
                polylineStartLatLng = polyline.getPoints().get(0);
                int k = polyline.getPoints().size();
                polylineEndLatLng = polyline.getPoints().get(k - 1);
                polylines.add(polyline);

            } else {

            }
        }
        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(polylineStartLatLng);
        startMarker.title("My Location");
        map.addMarker(startMarker);

        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.title("Destination");
        map.addMarker(endMarker);
    }

    //whena user press another location
    @Override
    public void onRoutingCancelled() {
        Findroutes(start, end);
    }


    // ok this is where if the user wants to get directions they press the pink button when the found a destination and the we pass it to google maps
    private void DisplayRoute(Double lat, Double lon) {
        try {
            //passing in the location here "google.navigation:q=latitude,longitude"   q means query
            Uri uri = Uri.parse("google.navigation:q=" + lat + "," + lon);

            //starting google maps
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } catch (ActivityNotFoundException er) {

            //if the user does not  have google maps taking them to playstore so they can install
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.gooogle.android.apps.maps");

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }
    }
}
