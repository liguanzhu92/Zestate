package com.guanzhuli.zestate.realtor.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.guanzhuli.zestate.R;
import com.guanzhuli.zestate.controller.VolleyController;
import com.guanzhuli.zestate.model.PostPropertyList;
import com.guanzhuli.zestate.model.Property;
import com.guanzhuli.zestate.realtor.util.Tool;
import com.squareup.picasso.Picasso;
import id.zelory.compressor.Compressor;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewPropertyFragment extends Fragment {

    private View mView;
    private TextView mTextAddress;
    private EditText mEditName, mEditType, mEditCost, mEditSize, mEditDescription;
    private Button mButtonPost, mButtonDraft, mButtonDiscard;
    private ImageView mImageLocation, mImageUpload1, mImageUpload2, mImageUpload3;
    private Property mProperty;
    private Bundle mBundle;
    private Bitmap mBitmap1, mBitmap2, mBitmap3;
    private int position;

    private boolean mBooleanAdd;
    /*-------image-----*/
    private TextView mTextImageName1,mTextUploadButton1,mTextImageName2,mTextUploadButton2,mTextImageName3,mTextUploadButton3;
    /*-------image-----*/


    private int PICK_IMAGE_REQUEST_1 = 1;
    private int PICK_IMAGE_REQUEST_2 = 2;
    private int PICK_IMAGE_REQUEST_3 = 3;
    private static final String ADD_PROPERTY_URL = "http://www.rjtmobile.com/realestate/register.php?property&add";
    private static final String EDIT_PROPERTY_URL = "http://www.rjtmobile.com/realestate/register.php?property&edit&pptyid=";


    public NewPropertyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_new_property, container, false);
        initView();
        mBundle = this.getArguments();
        if (mBundle != null ) {
            mBooleanAdd = false;
            position = mBundle.getInt("EditPosition");
            mProperty = PostPropertyList.getInstance().get(position);
            setContent();
        } else {
            mBooleanAdd = true;
        }
        VolleyLog.DEBUG = true;
        mImageLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "show map to find location", Toast.LENGTH_LONG).show();
            }
        });
        setClickListener();
        return mView;
    }

    private void setClickListener() {
        mButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "send infor to web service", Toast.LENGTH_LONG).show();
                if (mBooleanAdd) {
                    /*addProperty();*/
                    addProperty();
                } else {
                    editProperty();
                }

            }
        });
        mButtonDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "save change in local", Toast.LENGTH_LONG).show();
            }
        });
        mButtonDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SellerHomeFragment sellerHomeFragment = new SellerHomeFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.seller_activity_container, sellerHomeFragment)
                        .commit();
            }
        });
        mImageLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SellerMapFragment sellerMapFragment = new SellerMapFragment();
                Bundle bundle = new Bundle();
                if (mBooleanAdd) {
                    // search
                    bundle.putBoolean("ADD_PROPERTY", true);
                } else {
                    // show current location
                    bundle.putBoolean("ADD_PROPERTY", false);
                    bundle.putDouble("LATITUDE", mProperty.getLatitude());
                    bundle.putDouble("LONGITUDE", mProperty.getLongitude());
                }
                sellerMapFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fade_in,R.anim.fade_out,R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.seller_activity_container, sellerMapFragment)
                        .addToBackStack(NewPropertyFragment.class.getName())
                        .commit();
            }
        });

        // image
        mTextUploadButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadClick(PICK_IMAGE_REQUEST_1);
            }
        });
        mTextUploadButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadClick(PICK_IMAGE_REQUEST_2);
            }
        });
        mTextUploadButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadClick(PICK_IMAGE_REQUEST_3);
            }
        });
    }

    private void uploadClick(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), requestCode);
    }

    private void editProperty() {
        final ProgressDialog pDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Processing...");
        pDialog.show();
        String url = EDIT_PROPERTY_URL + mProperty.getId();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("NewProperty", "update success");
                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d("NewProperty", "Error: " + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("propertyname", mEditName.getText().toString());
                params.put("propertytype", mEditType.getText().toString());
                params.put("propertycat", "2");
                params.put("propertyaddress1", mProperty.getAddress1());
                params.put("propertyaddress2", mProperty.getAddress2());
                params.put("propertyzip", String.valueOf(mProperty.getZip()));
                params.put("propertylat", String.valueOf(mProperty.getLatitude()));
                params.put("propertylong", String.valueOf(mProperty.getLongitude()));
                params.put("propertycost", mEditCost.getText().toString());
                params.put("propertysize", mEditSize.getText().toString());
                params.put("propertydesc", mEditDescription.getText().toString());
                params.put("propertystatus", "yes");
                params.put("propertyimg1", "");
                params.put("propertyimg2", "");
                params.put("propertyimg3", "");
                params.put("userid", "162");
                return params;
            }
        };
        VolleyController.getInstance().addToRequestQueue(stringRequest);
    }

    private void addProperty() {
        final ProgressDialog pDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Processing...");
        pDialog.show();
        File file= new File("/sdcard/DCIM/Camera/" + "cache");
        Tool.saveBmpToFile(mBitmap1, file, 100);
        final Bitmap compress_mBitmap1 = new Compressor.Builder(getActivity())
                .setMaxWidth(640)
                .setMaxHeight(480)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .build()
                .compressToBitmap(file);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_PROPERTY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.contains("true")) {
                    pDialog.dismiss();
                    Log.d("NewProperty", "add success");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d("NewProperty", "Error: " + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("propertyname", mEditName.getText().toString());
                params.put("propertytype", mEditType.getText().toString());
                params.put("propertycat", "2");
                params.put("propertyaddress1", "xihu qu");
                params.put("propertyaddress2", "hangzhou");
                params.put("propertyzip", "60174");
                params.put("propertylat", "30");
                params.put("propertylong", "120");
                params.put("propertycost", mEditCost.getText().toString());
                params.put("propertysize", mEditSize.getText().toString());
                params.put("propertydesc", mEditDescription.getText().toString());
                params.put("propertystatus", "yes");
                params.put("propertyimg1", Tool.bitmapToBase64(compress_mBitmap1));
                params.put("propertyimg2", "");
                params.put("propertyimg3", "");
                params.put("userid", "162");
                return params;
            }
        };
        VolleyController.getInstance().addToRequestQueue(stringRequest);
    }

    private void setContent() {
        mTextAddress.setText(mProperty.getAddress1() + mProperty.getAddress2() );
        mEditName.setText(mProperty.getName());
        mEditType.setText(mProperty.getType());
        mEditCost.setText(mProperty.getCost());
        mEditSize.setText(mProperty.getSize());
        mEditDescription.setText(mProperty.getDescription());
        Picasso.with(getContext())
                .load("http://" + mProperty.getImage1())
                .into(mImageUpload1);
        Picasso.with(getContext())
                .load("http://" + mProperty.getImage2())
                .into(mImageUpload2);
        Picasso.with(getContext())
                .load("http://" + mProperty.getImage3())
                .into(mImageUpload3);
    }

    private void initView() {
        mTextAddress = (TextView) mView.findViewById(R.id.new_property_address);
        mEditName = (EditText) mView.findViewById(R.id.new_property_name);
        mEditType = (EditText) mView.findViewById(R.id.new_property_type);
        mEditCost = (EditText) mView.findViewById(R.id.new_property_cost);
        mEditSize = (EditText) mView.findViewById(R.id.new_property_size);
        mEditDescription = (EditText) mView.findViewById(R.id.new_property_description);
        mButtonPost = (Button) mView.findViewById(R.id.new_property_post);
        mButtonDraft = (Button) mView.findViewById(R.id.new_property_draft);
        mButtonDiscard = (Button) mView.findViewById(R.id.new_property_discard);
        mImageLocation = (ImageView) mView.findViewById(R.id.new_property_location);
        /*-------image-----*/
        mTextImageName1 = (TextView) mView.findViewById(R.id.new_image_name1);
        mTextUploadButton1 = (TextView) mView.findViewById(R.id.new_image_upload1);
        mImageUpload1 = (ImageView) mView.findViewById(R.id.upload_image1);
        mTextImageName2 = (TextView) mView.findViewById(R.id.new_image_name2);
        mTextUploadButton2 = (TextView) mView.findViewById(R.id.new_image_upload2);
        mImageUpload2 = (ImageView) mView.findViewById(R.id.upload_image2);
        mTextImageName3 = (TextView) mView.findViewById(R.id.new_image_name3);
        mTextUploadButton3 = (TextView) mView.findViewById(R.id.new_image_upload3);
        mImageUpload3 = (ImageView) mView.findViewById(R.id.upload_image3);
        /*-------image-----*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST_1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mBitmap1 = setImage(data.getData(), mImageUpload1);

        } else if (requestCode == PICK_IMAGE_REQUEST_2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mBitmap2 = setImage(data.getData(), mImageUpload2);
        } else if (requestCode == PICK_IMAGE_REQUEST_3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mBitmap3 = setImage(data.getData(), mImageUpload3);
        }
    }

    private Bitmap setImage(Uri filePath, ImageView imageUpload1) {
        Bitmap bitmap = null;
        try {
/*            getContext().getContentResolver().openInputStream(filePath);
            mFileList.add(new File(getRealPathFromURI(getContext(), filePath)));*/
            //Getting the Bitmap from Gallery
            bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
            File file= new File("/sdcard/DCIM/Camera/" + "cache");
            //Setting the Bitmap to ImageView
            imageUpload1.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return bitmap;
        }
    }

}