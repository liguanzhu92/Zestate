package com.guanzhuli.zestate.buyer;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.guanzhuli.zestate.R;
import com.guanzhuli.zestate.buyer.adapters.PropertyRecyclerView;
import com.guanzhuli.zestate.buyer.fragments.PropertyListView;
import com.guanzhuli.zestate.controller.VolleyController;
import com.guanzhuli.zestate.model.Property;
import com.guanzhuli.zestate.model.UserLocation;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BuyerNavigation extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener
        , PropertyRecyclerView.OnClickCard {

    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private Boolean mLocationPermissionEnabled = false;
    private LocationRequest mLocationRequest;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;
    private Marker mPreviousMarker;
    private LatLng currentLocation;
    private SlidingUpPanelLayout mSlidingUpPanelLayout;
    private PropertyRecyclerView mPropertyadapter;
    RecyclerView mRecyclerView;
    HashMap<String, Integer> markerIdHashMap;
    HashMap<Integer, Marker> markerHashMap;
    private ProgressDialog pd;
    private Location mLocation;
    private ArrayList<Property> propertyList;
    private LinearLayoutManager mLinearLayoutManager;
    private static final int INTERVAL = 60 * 1000;
    private static final int FASTINTERVAL = 60 * 500;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!googleServiceAvilable()) {
            //show other layout need to handle those we don't need that right now
        } else {
            setContentView(R.layout.activity_buyer_navigation);
            initMap();
            Toolbar toolbar = (Toolbar) findViewById(R.id.map_view_toolbar);
            setSupportActionBar(toolbar);
            pd = new ProgressDialog(this);
            pd.setMessage("please wait");
            pd.setCancelable(false);
            BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bootomNavigation);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            mSearchView = (SearchView) toolbar.findViewById(R.id.search_by_intrest);
            mRecyclerView = (RecyclerView) findViewById(R.id.map_recycler_view);
            mLinearLayoutManager = new LinearLayoutManager(BuyerNavigation.this, LinearLayoutManager.HORIZONTAL, false);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(mRecyclerView);
            mPropertyadapter = new PropertyRecyclerView(BuyerNavigation.this, this);
            mRecyclerView.setAdapter(mPropertyadapter);
            mPropertyadapter.notifyDataSetChanged();
            mSlidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.slideUpPanelLayout);
            mSlidingUpPanelLayout.setPanelHeight(0);
            //mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            mSlidingUpPanelLayout.setCoveredFadeColor(0x0ffffff);
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {

                    int id = item.getItemId();

                    if (id == R.id.nav_camera) {
                        // Handle the camera action
                    } else if (id == R.id.nav_gallery) {

                    } else if (id == R.id.nav_slideshow) {

                    } else if (id == R.id.nav_manage) {

                    } else if (id == R.id.nav_share) {

                    } else if (id == R.id.nav_send) {

                    }
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);

                    return true;
                }
            });

            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {

                    switch (item.getItemId()) {

                        case R.id.bottom_filter:
                            break;
                        case R.id.bottom_view_type:
                            if (item.getTitle().equals("List")) {
                                item.setTitle("Map");
                                if (mSlidingUpPanelLayout != null &&
                                        (mSlidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mSlidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
                                    mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                                }
                                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.fragment_replaceble, new PropertyListView()).addToBackStack(null).commit();
                            } else {
                                item.setTitle("List");
                                onBackPressed();
                            }
                            break;
                    }

                    return true;
                }
            });
            setLocation();
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    if (mLinearLayoutManager.findFirstCompletelyVisibleItemPosition() > 0) {
                        if (mPreviousMarker != null)
                            setMarkerOptionsOnChange(markerIdHashMap.get(mPreviousMarker.getId()));
                        Integer postion = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        Marker marker = markerHashMap.get(postion);
                        markerIdHashMap.remove(marker.getId());
                        markerHashMap.remove(postion);
                        marker.remove();
                        Property property = propertyList.get(postion);
                        MarkerOptions markerOptions = new MarkerOptions();
                        LatLng latLng = new LatLng(property.getLatitude(), property.getLongitude());
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        markerOptions.position(latLng);
                        markerOptions.title(property.getAddress1());
                        Marker newmarker = mGoogleMap.addMarker(markerOptions);
                        mPreviousMarker = newmarker;
                        markerIdHashMap.put(newmarker.getId(), postion);
                        markerHashMap.put(postion, newmarker);
                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                    }

                }
            });
        }

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(BuyerNavigation.class.getSimpleName(),"query Submited");
                if(!query.equalsIgnoreCase("")){
                    Geocoder geo = new Geocoder(BuyerNavigation.this);
                    try {
                        List<Address> addresses = geo.getFromLocationName(query,1);
                        if(addresses.size()>0) {
                            Address address = addresses.get(0);
                            Double lat = address.getLatitude();
                            Double longit= address.getLongitude();
                            int size = propertyList.size();
                            propertyList=VolleyController.getInstance().getmProperty().getPropertyList(lat,longit);
                            if(propertyList.size()>0){
                                markerIdHashMap.clear();
                                for (int i = 0;i<size;i++){
                                    Marker marker =markerHashMap.get(i);
                                    marker.remove();
                                }
                                markerHashMap.clear();
                                mPropertyadapter.notifyOnDataChange(lat,longit);
                                showAllPropertiesList();

                            } else{
                                Toast.makeText(BuyerNavigation.this,"no Location found",Toast.LENGTH_LONG).show();
                                Snackbar.make(mSearchView,"no Location found",Snackbar.LENGTH_LONG).show();
                                propertyList=VolleyController.getInstance().getmProperty().getPropertyList(null,null);
                                markerIdHashMap.clear();
                                for (int i = 0;i<size;i++){
                                    Marker marker =markerHashMap.get(i);
                                    marker.remove();
                                }
                                markerHashMap.clear();
                                mPropertyadapter.notifyOnDataChange(lat, longit);
                                showAllPropertiesList();
                            }
                        }else{
                            Toast.makeText(BuyerNavigation.this,"no Location found",Toast.LENGTH_LONG).show();
                            Snackbar.make(mSearchView,"no Location found",Snackbar.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(BuyerNavigation.class.getSimpleName(),"query changed");
                return false;
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mSlidingUpPanelLayout != null &&
                (mSlidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mSlidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.buyer_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Boolean googleServiceAvilable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int avilablity = api.isGooglePlayServicesAvailable(this);
        if (avilablity == ConnectionResult.SUCCESS)
            return true;
        else if (api.isUserResolvableError(avilablity)) {
            Dialog dialog = api.getErrorDialog(this, avilablity, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "This app need google services", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        getRealEstatedata();
        mGoogleMap = googleMap;
        markerIdHashMap = new HashMap<>();
        markerHashMap = new HashMap<>();
        //setLocation();
        if (mGoogleMap != null) {
            mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    mSlidingUpPanelLayout.setAnchorPoint(1.0f);
                    mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                    mRecyclerView.getLayoutManager().scrollToPosition(markerIdHashMap.get(marker.getId()));
                    return true;
                }
            });

            mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
            });
        }
    }

    private void setLocation() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTINTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void getRealEstatedata() {
        pd.show();
        String getData = "http://www.rjtmobile.com/realestate/getproperty.php?all";
        final ArrayList<Property> mPropertyList = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(getData, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.d(BuyerNavigation.class.getSimpleName(), jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    Property property = new Property();
                    try {
                        JSONObject jsonProperty = jsonArray.getJSONObject(i);
                        property.setId(jsonProperty.getString("Property Id"));
                        property.setName(jsonProperty.getString("Property Name"));
                        property.setType(jsonProperty.getString("Property Type"));
                        property.setCategory(jsonProperty.getString("Property Category"));
                        property.setAddress1(jsonProperty.getString("Property Address1"));
                        property.setAddress2(jsonProperty.getString("Property Address2"));
                        property.setZip(Integer.parseInt(jsonProperty.getString("Property Zip")));
                        property.setImage1(jsonProperty.getString("Property Image 1"));
                        property.setImage2(jsonProperty.getString("Property Image 2"));
                        property.setImage3(jsonProperty.getString("Property Image 3"));
                        property.setLatitude(Double.parseDouble(jsonProperty.getString("Property Latitude")));
                        property.setLongitude(Double.parseDouble(jsonProperty.getString("Property Longitude")));
                        property.setCost(jsonProperty.getString("Property Cost"));
                        property.setSize(jsonProperty.getString("Property Size"));
                        property.setDescription(jsonProperty.getString("Property Desc"));
                        property.setPublishDate(jsonProperty.getString("Property Published Date"));
                        property.setModifyDate(jsonProperty.getString("Property Modify Date"));
                        property.setStatus(jsonProperty.getString("Property Status"));
                        property.setUserId(jsonProperty.getString("User Id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (pd.isShowing())
                            pd.dismiss();
                    }
                    mPropertyList.add(property);
                }
                VolleyController.getInstance().getmProperty().setPropertyList(mPropertyList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mLocation == null) {
                            if (ActivityCompat.checkSelfPermission(BuyerNavigation.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                    ActivityCompat.checkSelfPermission(BuyerNavigation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        }
                        UserLocation userLocation = VolleyController.getInstance().getUserLocation();
                        userLocation.setmCurrentLocation(mLocation);
                        mPropertyadapter.notifyOnDataChange(null,null);
                        showAllPropertiesList();
                    }
                });
                if(pd.isShowing())
                    pd.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(pd.isShowing())
                    pd.dismiss();
                Log.d(BuyerNavigation.class.getSimpleName(), volleyError.getMessage());
            }
        });
        Log.d(BuyerNavigation.class.getSimpleName(), jsonArrayRequest.getUrl());
        VolleyController.getInstance().addToRequestQueue(jsonArrayRequest);
    }


    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        } else {
            mLocationPermissionEnabled = true;
        }
        if (mLocationPermissionEnabled) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {

        } else {
            mLocation = location;
            UserLocation userLocation = VolleyController.getInstance().getUserLocation();
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            currentLocation = ll;
            currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
            userLocation.setmCurrentLocation(location);
            Geocoder geo = new Geocoder(this);
            try {
                List<Address> addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 2);
                if (addresses.size() > 0)
                    userLocation.setmCurrentAddress(addresses.get(0));
            } catch (IOException e) {
                e.printStackTrace();
            }

            //showMarkers(ll);
            if(pd.isShowing())
                pd.dismiss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionEnabled = true;
            } else {
                mLocationPermissionEnabled = false;
                //handle request Denied here
            }
        }
    }

    /*//show markers
    public void showMarkers(LatLng latLang) {
        mCurrentMarker = mGoogleMap.addMarker(new MarkerOptions().position(latLang)
                .title("current Location")
                .snippet(VolleyController.getInstance().getUserLocation().getmCurrentAddress().getAddressLine(0)));
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLang, 15f));
    }
*/
    public void showAllPropertiesList() {
        if(propertyList == null)
        propertyList = VolleyController.getInstance().getmProperty().getPropertyList(null,null);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < propertyList.size(); i++) {
            String houseType = propertyList.get(i).getType();
            setMarkerColor(houseType);
            MarkerOptions markerOptions = setMarkerColor(houseType);
            LatLng latLng = new LatLng(propertyList.get(i).getLatitude(),propertyList.get(i).getLongitude());
            markerOptions.position(latLng);
            markerOptions.title(propertyList.get(i).getAddress1());
            Marker marker = mGoogleMap.addMarker(markerOptions);
            markerIdHashMap.put(marker.getId(),i);
            markerHashMap.put(i,marker);
            builder.include(latLng);
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 10);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = mLocation;
       // Log.d("BuyerNavigation",location.toString()+"uhgvhyjk");
        if(location == null){
            Log.d("BuyerNavigation","location is null here ");
            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            Log.d("BuyerNavigation",location.toString());
        }
        if(location !=null) {
            VolleyController.getInstance().getUserLocation().setmCurrentLocation(location);
            currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
        }
        else{
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
        }
    }

    @Override
    public void onClickOfCardItem() {
        if (mSlidingUpPanelLayout != null && (mSlidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mSlidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED))
            mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    MarkerOptions setMarkerColor(String houseType){
        MarkerOptions markerOptions = new MarkerOptions();

        if(houseType.equalsIgnoreCase("Rent")){
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        }else if(houseType.equalsIgnoreCase("villa")){
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        }else if(houseType.equalsIgnoreCase("house"))
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        else
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        return markerOptions;
    }

    //for previous marker changing positons
    private void setMarkerOptionsOnChange(int pos) {

        Property property = propertyList.get(pos);
        String houseType = property.getType();
        MarkerOptions markerOptions = setMarkerColor(houseType);
        LatLng latLng = new LatLng(property.getLatitude(),property.getLongitude());
        markerOptions.position(latLng);
        markerOptions.title(property.getAddress1());
        Marker marker = mGoogleMap.addMarker(markerOptions);
        markerIdHashMap.put(marker.getId(),pos);
        markerHashMap.put(pos,marker);
    }

}
