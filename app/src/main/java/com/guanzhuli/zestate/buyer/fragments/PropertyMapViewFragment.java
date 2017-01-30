package com.guanzhuli.zestate.buyer.fragments;


import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.guanzhuli.zestate.R;
import com.guanzhuli.zestate.controller.VolleyController;
import com.guanzhuli.zestate.model.Property;

/**
 * A simple {@link Fragment} subclass.
 */
public class PropertyMapViewFragment extends Fragment implements OnMapReadyCallback{
    private static final String ARG_PARAM1 = "param1";
    private Property mProperty;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private BottomNavigationView bo;

    public PropertyMapViewFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProperty = VolleyController.getInstance().getmProperty().getPropertyByID(getArguments().getString(ARG_PARAM1));
    }

    public static PropertyMapViewFragment newInstace(String propertyId){
        PropertyMapViewFragment fragment = new PropertyMapViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1, propertyId);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_property_map_view, container, false);
        bo = (BottomNavigationView) getActivity().findViewById(R.id.bootomNavigation);
        mMapView = (MapView) view.findViewById(R.id.property_complete_view_map);
        mMapView.getMapAsync(this);
        mMapView.onCreate(savedInstanceState);
        bo.getItemBackgroundResource();
        setMenuItems(bo.getMenu());

        bo.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.bottom_filter:
                        if (item.getTitle().equals("map")) {
                            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        }
                        break;
                    case R.id.bottom_view_type:
                        if (item.getTitle().equals("Satillite")) {
                            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        }
                        break;
                }

                return true;
            }
        });

        return view;
    }

    private void setMenuItems(Menu menu) {
        MenuItem menuItem1=menu.getItem(0);
        MenuItem menuItem2=menu.getItem(1);
        MenuItem menuItem3=menu.getItem(2);

        menuItem1.setTitle("map");
        menuItem2.setTitle("LotLine");
        menuItem3.setTitle("Satillite");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng latla = new LatLng(mProperty.getLatitude(),mProperty.getLongitude());
        markerOptions.position(latla);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latla, 18f));

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Menu menu = bo.getMenu();
        MenuItem menuItem1=menu.getItem(0);
        MenuItem menuItem2=menu.getItem(1);
        MenuItem menuItem3=menu.getItem(2);

        menuItem1.setTitle("filter");
        menuItem2.setTitle("List");
        menuItem3.setTitle("List");
    }
}
