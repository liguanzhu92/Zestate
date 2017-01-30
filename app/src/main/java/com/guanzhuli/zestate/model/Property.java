package com.guanzhuli.zestate.model;

import android.location.Location;
import android.util.Log;

import com.guanzhuli.zestate.buyer.BuyerNavigation;
import com.guanzhuli.zestate.buyer.adapters.PropertyRecyclerView;
import com.guanzhuli.zestate.controller.VolleyController;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Guanzhu Li on 1/21/2017.
 */
public class Property implements Comparable<Property>{
    private String mId;
    private String mName;
    private String mType;
    private String mCategory;
    private String mAddress1;
    private String mAddress2;
    private String mAddress;
    private int mZip;
    private String mImage1;
    private String mImage2;
    private String mImage3;
    private Double mLatitude;
    private Double mLongitude;
    private String mCost;
    private String mSize;
    private String mDescription;
    private String mPublishDate;
    private String mModifyDate;
    private String mStatus;
    private String mUserId;
    private ArrayList<Property> mPropertyList = new ArrayList<>();

    public ArrayList<Property> getmPropertyList() {
        Log.d(BuyerNavigation.class.getSimpleName(),mPropertyList.toString());
        Collections.sort(mPropertyList);
        Log.d(BuyerNavigation.class.getSimpleName(), mPropertyList.toString());
        return mPropertyList;
    }

    public void setmPropertyList(ArrayList<Property> mPropertyList) {
        this.mPropertyList = mPropertyList;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }
    public String getAddress1() {
        return mAddress1;
    }

    public void setAddress1(String address1) {
        mAddress1 = address1;
    }

    public String getAddress2() {
        return mAddress2;
    }

    public void setAddress2(String address2) {
        mAddress2 = address2;
    }

    public int getZip() {
        return mZip;
    }

    public void setZip(int zip) {
        mZip = zip;
    }

    public String getImage1() {
        return mImage1;
    }

    public void setImage1(String image1) {
        image1 = image1.replaceFirst("www.","http://");
        mImage1 = image1;
    }

    public String getImage2() {
        return mImage2;
    }

    public void setImage2(String image2)
    {
        image2 = image2.replaceFirst("www.","http://");
        mImage2 = image2;
    }

    public String getImage3() {
        return mImage3;
    }

    public void setImage3(String image3)
    {
        image3 = image3.replaceFirst("www.","http://");
        mImage3 = image3;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(Double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(Double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getCost() {
        return mCost;
    }

    public void setCost(String cost) {
        mCost = cost;
    }

    public String getSize() {
        return mSize;
    }

    public void setSize(String size) {
        mSize = size;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getPublishDate() {
        return mPublishDate;
    }

    public void setPublishDate(String publishDate) {
        mPublishDate = publishDate;
    }

    public String getModifyDate() {
        return mModifyDate;
    }

    public void setModifyDate(String modifyDate) {
        mModifyDate = modifyDate;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public Property getPropertyByID(String propertyId){
        Property property = null;
        for (Property property1:mPropertyList){
            Log.d(Property.class.getSimpleName(),property1.getId());
            if(property1.getId().equals(propertyId)) {
                property = property1;
                Log.d(Property.class.getSimpleName(),propertyId);
                return property;
            }
        }
        return property;
    }

    @Override
    public int compareTo(Property o) {
        Location location = VolleyController.getInstance().getUserLocation().getmCurrentLocation();
        Location thisLocati = new Location("");
        thisLocati.setLatitude(this.getLatitude());
        thisLocati.setLongitude(this.getLongitude());

        Location oLocati = new Location("");
        oLocati.setLatitude(o.getLatitude());
        oLocati.setLongitude(o.getLongitude());

        if(location.distanceTo(thisLocati)<location.distanceTo(oLocati)){
            Log.d(BuyerNavigation.class.getSimpleName(),this.getName()+location.distanceTo(thisLocati)/1000+":"+
                    location.distanceTo(oLocati)/1000+o.getName());
            return -1;
        }

        return 0;
    }



/*    	"Property Id": "171",
                "Property Name": "aaabc",
                "Property Type": "Vallia",
                "Property Category": "2",
                "Property Address1": "adsfsdfad",
                "Property Address2": "afafsd",
                "Property Zip": "11",
                "Property Image 1": "www.rjtmobile.com\\\/realestate\\\/images\\\/11\\\/fb_icon@2x.png",
                "Property Image 2": "www.rjtmobile.com\\\/realestate\\\/images\\\/11\\\/fb_icon@2x.png",
                "Property Image 3": "www.rjtmobile.com\\\/realestate\\\/images\\\/11\\\/fb_icon@2x.png",
                "Property Latitude": 11,
                "Property Longitude": 11,
                "Property Cost": "11",
                "Property Size": "11",
                "Property Desc": "sdfasdfa",
                "Property Published Date": "2016-10-27 22:15:38",
                "Property Modify Date": "2016-10-27 22:15:38",
                "Property Status": "yes",
                "User Id": "2"*/
}
