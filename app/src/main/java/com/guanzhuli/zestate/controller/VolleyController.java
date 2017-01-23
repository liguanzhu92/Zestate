package com.guanzhuli.zestate.controller;

import android.app.Application;
import android.text.TextUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.guanzhuli.zestate.model.Property;
import com.guanzhuli.zestate.model.UserLocation;

/**
 * Created by Guanzhu Li on 1/13/2017.
 */
public class VolleyController extends Application {

    public static final String TAG = VolleyController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;

    private static VolleyController mInstance;
    private static Property mProperty;
    private static UserLocation mUserLocation;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized VolleyController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public Property getmProperty(){
        if(mProperty == null)
            mProperty = new Property();

        return mProperty;
    }

    public UserLocation getUserLocation(){
        if(mUserLocation == null){
            mUserLocation = new UserLocation();
        }
        return mUserLocation;
    }
}