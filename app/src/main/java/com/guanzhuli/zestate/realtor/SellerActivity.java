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
import com.guanzhuli.zestate.realtor.fragment.SellerHomeFragment;
import com.guanzhuli.zestate.realtor.fragment.SellerProfileFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SellerActivity extends AppCompatActivity {
    private String userId;
    private static final String basicURL = "http://www.rjtmobile.com/realestate/getproperty.php?all&userid=";
    private PostPropertyList mProperties = PostPropertyList.getInstance();
    private static String TAG = SellerActivity.class.getSimpleName();
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
        // initialize property list
        userId = "0";
        initData(userId);
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
                    case R.id.bottom_seller_map:
                        break;
                    case R.id.bottom_seller_profile:
                        SellerProfileFragment sellerProfileFragment = new SellerProfileFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.seller_activity_container, sellerProfileFragment).commit();
                    default:
                        break;
                }

                return false;
            }
        });
    }

    private void initData(String userId) {
        String url = basicURL + userId;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.d(TAG, jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject person = (JSONObject) jsonArray.get(i);
                        Property property = new Property();
                        property.setId(person.getString("Property Id"));
                        property.setName(person.getString("Property Name"));
                        property.setType(person.getString("Property Type"));
                        property.setmCategory(person.getString("Property Category"));
                        property.setAddress1(person.getString("Property Address1"));
                        property.setAddress2(person.getString("Property Address2"));
                        property.setZip(Integer.parseInt(person.getString("Property Zip")));
                        property.setImage1(person.getString("Property Image 1"));
                        property.setImage2(person.getString("Property Image 2"));
                        property.setImage3(person.getString("Property Image 3"));
                        property.setLatitude(Double.parseDouble(person.getString("Property Latitude")));
                        property.setLongitude(Double.parseDouble(person.getString("Property Longitude")));
                        property.setCost(person.getString("Property Cost"));
                        property.setSize(person.getString("Property Size"));
                        property.setDescription(person.getString("Property Desc"));
                        property.setPublishDate(person.getString("Property Published Date"));
                        property.setModifyDate(person.getString("Property Modify Date"));
                        property.setStatus(person.getString("Property Status"));
                        property.setUserId(person.getString("User Id"));
                        mProperties.add(property);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d(TAG, "Error: " + volleyError.getMessage());
                Toast.makeText(getApplicationContext(),
                        volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        VolleyController.getInstance().addToRequestQueue(jsonArrayRequest);
    }
}
