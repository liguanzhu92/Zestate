package com.guanzhuli.zestate.realtor;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.guanzhuli.zestate.R;
import com.guanzhuli.zestate.controller.VolleyController;
import com.guanzhuli.zestate.model.Property;
import com.guanzhuli.zestate.model.PostPropertyList;
import com.guanzhuli.zestate.realtor.fragment.AppointmentRecordFragment;
import com.guanzhuli.zestate.realtor.fragment.NewPropertyFragment;
import com.guanzhuli.zestate.realtor.fragment.SellerHomeFragment;
import com.guanzhuli.zestate.realtor.fragment.SellerProfileFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SellerActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
        // initialize property list

        if(findViewById(R.id.seller_activity_container) != null) {
            SellerHomeFragment sellerHomeFragment = new SellerHomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.seller_activity_container, sellerHomeFragment).commit();
        }
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.seller_bottom_nav);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_seller_list:
                        SellerHomeFragment sellerHomeFragment = new SellerHomeFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.seller_activity_container, sellerHomeFragment).commit();
                        break;
                    case R.id.bottom_seller_add:
                        NewPropertyFragment newPropertyFragment = new NewPropertyFragment();
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("EditFlag", false);
                        bundle.putBoolean("AddFlag", true);
                        newPropertyFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.seller_activity_container, newPropertyFragment).commit();
                        break;
                    case R.id.bottom_seller_appointment:
                        AppointmentRecordFragment appointmentRecordFragment = new AppointmentRecordFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.seller_activity_container, appointmentRecordFragment).commit();
                        break;
                    case R.id.bottom_seller_profile:
                        SellerProfileFragment sellerProfileFragment = new SellerProfileFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.seller_activity_container, sellerProfileFragment).commit();
                        break;
                }
                return true;
            }
        });
    }


}
