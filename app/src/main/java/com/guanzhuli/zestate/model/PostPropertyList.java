package com.guanzhuli.zestate.model;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.guanzhuli.zestate.controller.VolleyController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Guanzhu Li on 1/22/2017.
 */

public class PostPropertyList extends ArrayList<Property> {
    private static final String GET_PROPERTY_URL = "http://www.rjtmobile.com/realestate/getproperty.php?all&userid=";
    private static final String DELETE_PROPERTY_URL = "http://www.rjtmobile.com/realestate/register.php?property&delete&pptyid=";
    private static String TAG = "PropertyProcess";
    public boolean workStatus = false;

    private static PostPropertyList mInstance = null;

    public static PostPropertyList getInstance() {
        if (mInstance == null) {
            mInstance = new PostPropertyList();
        }
        return mInstance;
    }

    private PostPropertyList() {
    }

    public void updateData(String id) {
        String url = GET_PROPERTY_URL + id;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                mInstance.clear();
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
                        property.setImage1(parseURL(person.getString("Property Image 1")));
                        property.setImage2(parseURL(person.getString("Property Image 2")));
                        property.setImage3(parseURL(person.getString("Property Image 3")));
                        property.setLatitude(Double.parseDouble(person.getString("Property Latitude")));
                        property.setLongitude(Double.parseDouble(person.getString("Property Longitude")));
                        property.setCost(person.getString("Property Cost"));
                        property.setSize(person.getString("Property Size"));
                        property.setDescription(person.getString("Property Desc"));
                        property.setPublishDate(person.getString("Property Published Date"));
                        property.setModifyDate(person.getString("Property Modify Date"));
                        property.setStatus(person.getString("Property Status"));
                        property.setUserId(person.getString("User Id"));
                        mInstance.add(property);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                workStatus = true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d(TAG, "Error: " + volleyError.getMessage());
            }
        });
        VolleyController.getInstance().addToRequestQueue(jsonArrayRequest);
    }


    public void deleteData(final String propertyId) {
        String url = DELETE_PROPERTY_URL + propertyId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.contains("true")) {
                    for (Property property : mInstance) {
                        if (property.getId().equals(propertyId)) {
                            String userID = property.getUserId();
                            mInstance.remove(property);
                            return;
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d(TAG, "Error: " + volleyError.getMessage());
            }
        });
        VolleyController.getInstance().addToRequestQueue(stringRequest);
    }


    private String parseURL(String rawUrl) {
        rawUrl.trim();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < rawUrl.length(); i++) {
            if (rawUrl.charAt(i) != '\\') {
                stringBuilder.append(rawUrl.charAt(i));
            }
        }
        String rawURL = stringBuilder.toString();
        return rawURL.replace(" ", "%20");
    }
}
