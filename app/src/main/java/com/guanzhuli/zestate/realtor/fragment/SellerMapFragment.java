package com.guanzhuli.zestate.realtor.fragment;


import android.Manifest;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.guanzhuli.zestate.R;
import com.guanzhuli.zestate.controller.VolleyController;
import com.guanzhuli.zestate.model.PostPropertyList;
import org.json.JSONObject;

public class SellerMapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private View mView;
    private EditText mEditSearch;
    private ImageView mImageSearch, mImageConfirm;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private static final String TEST_URL = "https://maps.googleapis.com/maps/api/place/autocomplete/xml?input=Amoeba&types=establishment&location=37.76999,-122.44696&radius=500&key=AIzaSyBclxE2Q51yBTUCXBtr2vz2qj2t11qRDCo";
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private LatLng currentLatLng = null;
    private double currentLatitude, currentLongitude;
    private Marker mCurrLocationMarker;

    public SellerMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_seller_map, container, false);
        mEditSearch = (EditText) mView.findViewById(R.id.seller_map_edit_search);
        mImageSearch = (ImageView) mView.findViewById(R.id.seller_map_search_btn);
        mImageSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                Log.i("map", "click search");
                LatLng latLng = new LatLng(30.2741, 120.1551);
                updateMapView(latLng);*/
                Log.i("map", "click search");
                String address = mEditSearch.getText().toString().trim();
                if(address.equals("")){
                    Toast.makeText(getContext(), "Please Input Correct Address!", Toast.LENGTH_LONG).show();
                    return;
                }
                // check address start with number
                try{
                    getLocationInfo(address);
                }catch (Exception e){
                    Toast.makeText(getContext(), "Error Location, Please Try Again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mImageConfirm = (ImageView) mView.findViewById(R.id.seller_map_confirm_btn);
        mImageConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // put address, longitude, latitude, pass data
                String address = mEditSearch.getText().toString().trim();
                NewPropertyFragment newPropertyFragment = new NewPropertyFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("EditFlag", true);
                bundle.putBoolean("AddFlag", true);
                bundle.putDouble("curLongitude", currentLongitude);
                bundle.putDouble("curLatitude", currentLatitude);
                bundle.putString("address", address);
                newPropertyFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.seller_activity_container, newPropertyFragment).commit();

            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.seller_map_view);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                //Initialize Google Play Services
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        buildGoogleApiClient();
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else {
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                }
                if (!getArguments().getBoolean("ADD_PROPERTY")) {
                    LinearLayout linearLayout = (LinearLayout) mView.findViewById(R.id.seller_map_search_linear);
                    linearLayout.setVisibility(View.INVISIBLE);
                    currentLatLng = new LatLng(getArguments().getDouble("LATITUDE"),getArguments().getDouble("LONGITUDE"));
                    Log.d("map", String.valueOf(getArguments().getDouble("LATITUDE")) + "  " + String.valueOf(getArguments().getDouble("LONGITUDE")));
                    updateMapView(currentLatLng);
                }
            }
        });
        return mView;
    }

    private String buildUrl(String address){
        String[] strArr = address.split(" ");
        StringBuilder sb = new StringBuilder();
        sb.append("http://maps.google.com/maps/api/geocode/json?address=");
        sb.append(strArr[0]);
        for (int i=1; i<strArr.length; i++){
            sb.append("%20" + strArr[i]);
        }
        sb.append("ka&sensor=false");
        return sb.toString();
    }

    private void getLocationInfo(String address){
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, buildUrl(address), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("MAPS", jsonObject.toString());

                try{
                    JSONObject location = jsonObject.getJSONArray("results")
                            .getJSONObject(0)
                            .getJSONObject("geometry").getJSONObject("location");
                    currentLongitude = location.getDouble("lng");
                    currentLatitude = location.getDouble("lat");

                    Log.e("MAPS", "lat: " + currentLatitude+ "lng: " +currentLongitude);
                    updateMapView(new LatLng(currentLatitude,currentLongitude));
                }catch (Exception e){
                    System.out.println(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d("MAPS", "ERROR" + volleyError.getMessage());
                Toast.makeText(getContext(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        VolleyController.getInstance().addToRequestQueue(jsonObjReq);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    private void updateMapView(LatLng lat){
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(lat).title("Set Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lat));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}
