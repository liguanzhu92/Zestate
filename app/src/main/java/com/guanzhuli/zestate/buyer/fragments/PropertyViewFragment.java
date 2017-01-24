package com.guanzhuli.zestate.buyer.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.guanzhuli.zestate.R;
import com.guanzhuli.zestate.buyer.adapters.PropertyImagesRecyclerView;
import com.guanzhuli.zestate.buyer.adapters.PropertyRecyclerView;
import com.guanzhuli.zestate.controller.VolleyController;
import com.guanzhuli.zestate.model.Property;


public class PropertyViewFragment extends Fragment implements OnMapReadyCallback {
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private Property mProperty;
    private View view;
    private GoogleMap mCardMap;


    public PropertyViewFragment() {
        // Required empty public constructor
    }

    public static PropertyViewFragment newInstance(String param1) {
        PropertyViewFragment fragment = new PropertyViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mProperty = VolleyController.getInstance().getmProperty().getPropertyByID(mParam1);
            Log.d(Property.class.getSimpleName(), mParam1 + "--" + mProperty.getId());

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_property_view, container, false);
        TextView address1 = (TextView) view.findViewById(R.id.property_view_Address1);
        TextView address2 = (TextView) view.findViewById(R.id.property_view_Address2);
        TextView propertySize = (TextView) view.findViewById(R.id.property_view_size);
        TextView propertyType = (TextView) view.findViewById(R.id.property_view_type);
        TextView propertyCost = (TextView) view.findViewById(R.id.property_view_cost);
        TextView propertyStatus = (TextView) view.findViewById(R.id.property_view_status);
        TextView propertyDesc = (TextView) view.findViewById(R.id.property_desc_text_view);
        CardView mMapCardView = (CardView) view.findViewById(R.id.property_card_map_view);
        address1.setText(String.format("%s,%s", mProperty.getName(), mProperty.getAddress1()));
        address2.setText(String.format("%s,%d", mProperty.getAddress2(), mProperty.getZip()));
        propertySize.setText(mProperty.getSize() + "Sq.ft");
        propertyCost.setText("$" + mProperty.getCost());
        propertyType.setText(mProperty.getType());
        propertyStatus.setText(mProperty.getStatus());
        propertyDesc.setText(mProperty.getDescription());
        setPropertyImages();
        mMapCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MapView mMapView = (MapView) view.findViewById(R.id.property_location_map);
        if (mMapView != null) {
            mMapView.onCreate(savedInstanceState);
            mMapView.getMapAsync(this);
        }
    }

    private void setPropertyImages() {

        String[] propertyImages = new String[3];
        int count = 0;
        if (!(mProperty.getImage1().equals(""))) {
            propertyImages[0] = mProperty.getImage1();
            count++;
            if (!(mProperty.getImage2().equals(""))) {
                count++;
                propertyImages[1] = mProperty.getImage2();
                if (!(mProperty.getImage3().equals(""))) {
                    count++;
                    propertyImages[2] = mProperty.getImage2();
                }
            }

        } else {
            count++;
            propertyImages[0] = "no image";
        }

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_images);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(new PropertyImagesRecyclerView(getActivity(), propertyImages, count));
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mCardMap = googleMap;
            LatLng ll = new LatLng(mProperty.getLatitude(),mProperty.getLongitude());
            mCardMap.addMarker(new MarkerOptions().position(ll)
            .title("property location"));
            mCardMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,12.0f));
    }
}
