package com.guanzhuli.zestate.model;

import android.location.Address;
import android.location.Location;

/**
 * Created by shashank reddy on 1/22/2017.
 */
public class UserLocation {
    Location mCurrentLocation;
    Address mCurrentAddress;

    public Location getmCurrentLocation() {
        return mCurrentLocation;
    }

    public void setmCurrentLocation(Location mCurrentLocation) {
        this.mCurrentLocation = mCurrentLocation;
    }

    public Address getmCurrentAddress() {
        return mCurrentAddress;
    }

    public void setmCurrentAddress(Address mCurrentAddress) {
        this.mCurrentAddress = mCurrentAddress;
    }
}
