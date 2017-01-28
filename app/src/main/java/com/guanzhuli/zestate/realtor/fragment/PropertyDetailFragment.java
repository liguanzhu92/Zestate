package com.guanzhuli.zestate.realtor.fragment;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.guanzhuli.zestate.R;
import com.guanzhuli.zestate.model.PostPropertyList;
import com.guanzhuli.zestate.model.Property;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PropertyDetailFragment extends Fragment {
    private View mView;
    private ViewPager mPager;
    private DetailPagerAdapter mDetailPagerAdapter;
    private Button mButtonEdit, mButtonDelete;
    private ImageView mImageLocation;
    private TextView mTextAddress,mTextName,mTextType, mTextCategory, mTextCost, mTextSize, mTextDescription;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private int[] imageSrc = {R.drawable.house_sample1, R.drawable.house_sample2, R.drawable.house_sample3};
    private PostPropertyList mProperties = PostPropertyList.getInstance();
    private Property mProperty;
    private ArrayList<String> mImageUrls = new ArrayList<>();

    public PropertyDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_property_detail, container, false);
        initView();
        setContent();
        mMapView.onCreate(savedInstanceState);
        if (mMapView != null) {
            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mGoogleMap = googleMap;
                    getPermission();
                    Log.d("map", String.valueOf(mProperty.getLatitude()) + "    " + String.valueOf(mProperty.getLongitude()));
                }
            });

        }
        return mView;
    }

    private void getPermission() {
        if (mGoogleMap == null)
            return;

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        LatLng setLocation = new LatLng(mProperty.getLatitude(), mProperty.getLatitude());
                        mGoogleMap.clear();
                        mGoogleMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location))
                                .anchor(0.0f, 1.0f)
                                .position(setLocation));
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(setLocation));
                        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .build();
        mGoogleApiClient.connect();
    }


    private void setContent() {
        final int position = getArguments().getInt("DetailPosition");
        mProperty = mProperties.get(position);
        mTextAddress.setText(mProperty.getAddress1() + mProperty.getAddress2() );
        mTextName.setText(mProperty.getName());
        mTextType.setText(mProperty.getType());
        mTextCategory.setText(String.valueOf(mProperty.getmCategory()));
        mTextCost.setText(mProperty.getCost());
        mTextSize.setText(mProperty.getSize());
        mTextDescription.setText(mProperty.getDescription());
        String imageUrl1 = mProperty.getImage1();
        String imageUrl2 = mProperty.getImage2();
        String imageUrl3 = mProperty.getImage3();
        if (!imageUrl1.equals("")) {    mImageUrls.add(imageUrl1);        }
        if (!imageUrl2.equals("")) {    mImageUrls.add(imageUrl2);        }
        if (!imageUrl3.equals("")) {    mImageUrls.add(imageUrl3);        }
        int mImageUrlsSize = mImageUrls.size();
        mDetailPagerAdapter = new DetailPagerAdapter(getContext(),mImageUrlsSize);
        mPager.setAdapter(mDetailPagerAdapter);

        mButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewPropertyFragment newPropertyFragment = new NewPropertyFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("EditFlag", true);
                bundle.putBoolean("AddFlag", false);
                bundle.putInt("EditPosition", position);
                newPropertyFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.seller_activity_container, newPropertyFragment)
                        .addToBackStack(PropertyDetailFragment.class.getName())
                        .commit();
            }
        });
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String propertyId = mProperty.getId();
                String userId = mProperty.getUserId();
                PostPropertyList.getInstance().deleteData(propertyId);
                SellerHomeFragment sellerHomeFragment = new SellerHomeFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.seller_activity_container, sellerHomeFragment)
                        .addToBackStack(PropertyDetailFragment.class.getName())
                        .commit();
            }
        });
        mImageLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initView() {
        mPager = (ViewPager) mView.findViewById(R.id.property_detail_pager);
        mTextAddress = (TextView) mView.findViewById(R.id.seller_detail_address);
        mTextName = (TextView) mView.findViewById(R.id.seller_detail_name);
        mTextType = (TextView) mView.findViewById(R.id.seller_detail_type);
        mTextCategory = (TextView) mView.findViewById(R.id.seller_detail_category);
        mTextCost = (TextView) mView.findViewById(R.id.seller_detail_cost);
        mTextSize = (TextView) mView.findViewById(R.id.seller_detail_size);
        mTextDescription = (TextView) mView.findViewById(R.id.seller_detail_description);
        mButtonEdit = (Button) mView.findViewById(R.id.seller_detail_button_edit);
        mButtonDelete = (Button) mView.findViewById(R.id.seller_detail_button_delete);
        mImageLocation = (ImageView) mView.findViewById(R.id.seller_detail_location);
        mMapView = (MapView) mView.findViewById(R.id.detail_map_view);
    }

    class DetailPagerAdapter extends PagerAdapter {
        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private int mWebImageSize;

        public DetailPagerAdapter(Context context, int i) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mWebImageSize = i;
        }


        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.detail_pager_item, container, false);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            if (position < mWebImageSize) {
                Picasso.with(getContext())
                        .load("http://" + mImageUrls.get(position))
                        .into(imageView);
            } else {
                imageView.setImageResource(imageSrc[position]);
            }

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
