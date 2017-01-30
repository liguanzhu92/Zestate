package com.guanzhuli.zestate.model;

import android.graphics.Bitmap;
import android.util.Log;

import com.guanzhuli.zestate.buyer.adapters.PropertyRecyclerView;

import java.util.ArrayList;

/**
 * Created by Guanzhu Li on 1/21/2017.
 */
public class Property {
    private String mId;
    private String mName;
    private String mType;
    private String mCategory;
    private String mAddress1;
    private String mAddress2;
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

    public Bitmap getBitmap1() {
        return mBitmap1;
    }

    public void setBitmap1(Bitmap bitmap1) {
        mBitmap1 = bitmap1;
    }

    public Bitmap getBitmap2() {
        return mBitmap2;
    }

    public void setBitmap2(Bitmap bitmap2) {
        mBitmap2 = bitmap2;
    }

    public Bitmap getBitmap3() {
        return mBitmap3;
    }

    public void setBitmap3(Bitmap bitmap3) {
        mBitmap3 = bitmap3;
    }

    private Bitmap mBitmap1;
    private Bitmap mBitmap2;
    private Bitmap mBitmap3;

    private ArrayList<Property> mPropertyList = new ArrayList<>();

    public ArrayList<Property> getPropertyList() {
        return mPropertyList;
    }

    public void setPropertyList(ArrayList<Property> mPropertyList) {
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

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String Category) {
        if (Category.equals("1")) {
            mCategory = "sell";
        } else {
            mCategory = "rent";
        }
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

}
