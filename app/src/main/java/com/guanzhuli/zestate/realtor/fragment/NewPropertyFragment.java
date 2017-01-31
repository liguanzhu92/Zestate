package com.guanzhuli.zestate.realtor.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.CursorLoader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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
import com.guanzhuli.zestate.controller.VolleyMultipartRequest;
import com.guanzhuli.zestate.model.PostPropertyList;
import com.guanzhuli.zestate.model.Property;
import com.guanzhuli.zestate.realtor.util.Tool;
import com.squareup.picasso.Picasso;
import id.zelory.compressor.Compressor;
import android.database.Cursor;

import java.io.*;
import java.util.HashMap;
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
    private String filePath;
    private int position;

    private boolean mBooleanAdd, mBooleanEdit;
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
        Log.d("newProperty","onCreateView");
        mView =  inflater.inflate(R.layout.fragment_new_property, container, false);
        initView();
        getFlag();
        return mView;
    }

    private void getFlag() {
        mBundle = this.getArguments();
        // from new: add = true, edit = false
        // from map edit: add = true, edit = true;
        // from detail: add = false, edit = true;
        mBooleanEdit = mBundle.getBoolean("EditFlag");
        mBooleanAdd = mBundle.getBoolean("AddFlag");
        if (mBooleanEdit == true && mBooleanAdd == false) {
            position = mBundle.getInt("EditPosition");
            mProperty = PostPropertyList.getInstance().get(position);
            setContent();
        } else if (mBooleanAdd == true && mBooleanEdit == true) {
            mProperty = new Property();
            mProperty.setAddress1(mBundle.getString("address"));
            mProperty.setLongitude(mBundle.getDouble("curLongitude"));
            mProperty.setLatitude(mBundle.getDouble("curLatitude"));
            setContent();
        }
        VolleyLog.DEBUG = true;
        mImageLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "show map to find location", Toast.LENGTH_LONG).show();
            }
        });
        setClickListener();
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    private void setClickListener() {
        mButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "send infor to web service", Toast.LENGTH_LONG).show();
                if (mBooleanAdd) {
                    /*addProperty();*/
                    addProperty1();
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

    private void addProperty1() {
        final ProgressDialog pDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Processing...");
        pDialog.show();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ADD_PROPERTY_URL, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse networkResponse) {
                pDialog.dismiss();
                Log.i("volley","success");
                SellerHomeFragment sellerHomeFragment = new SellerHomeFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.seller_activity_container, sellerHomeFragment).commit();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d("volley", "Error: " + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("propertyname", mEditName.getText().toString());
                params.put("propertytype", mEditType.getText().toString());
                params.put("propertycat", "2");
                // separate address
                separate(mProperty.getAddress1());
                params.put("propertyaddress1",mProperty.getAddress1());
                params.put("propertyaddress2",mProperty.getAddress2());
                params.put("propertyzip", String.valueOf(mProperty.getZip()));
                params.put("propertylat", String.valueOf(mProperty.getLatitude()));
                params.put("propertylong", String.valueOf(mProperty.getLongitude()));
                params.put("propertycost", mEditCost.getText().toString());
                params.put("propertysize", mEditSize.getText().toString());
                params.put("propertydesc", mEditDescription.getText().toString());
                params.put("propertystatus", "yes");
                params.put("userid", "17");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                if (mBitmap1 != null) {
                    params.put("propertyimg1", new DataPart(mEditName.getText().toString() + "1.jpg", getFileDataFromBitmap(mBitmap1)));
                }
                if (mBitmap2 != null) {
                    params.put("propertyimg2", new DataPart(mEditName.getText().toString() + "2.jpg", getFileDataFromBitmap(mBitmap2)));
                }
                if (mBitmap3 != null) {
                    params.put("propertyimg3", new DataPart(mEditName.getText().toString() + "3.jpg", getFileDataFromBitmap(mBitmap3)));
                }
                return params;
            }
        };
        VolleyController.getInstance().addToRequestQueue(volleyMultipartRequest);
    }

    private void separate(String s) {
        String temp = s;
        mProperty.setAddress1(temp.substring(0,temp.indexOf(',')).trim());
        String temp2 = s;
        temp2 = temp2.substring(temp2.indexOf(',') + 1, temp2.lastIndexOf(',')).trim();
        int t = temp2.lastIndexOf(' ');
        String temp3 = temp2;
        mProperty.setAddress2(temp2.substring(0, t));
        mProperty.setZip(Integer.parseInt(temp3.substring(t).trim()));
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
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse networkResponse) {
                pDialog.dismiss();
                Log.i("volley","success");
                SellerHomeFragment sellerHomeFragment = new SellerHomeFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.seller_activity_container, sellerHomeFragment).commit();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d("volley", "Error: " + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("propertyname", mEditName.getText().toString());
                params.put("propertytype", mEditType.getText().toString());
                params.put("propertycat", "2");
                params.put("propertyaddress1",mProperty.getAddress1());
                params.put("propertyaddress2",mProperty.getAddress2());
                params.put("propertyzip", String.valueOf(mProperty.getZip()));
                params.put("propertylat", String.valueOf(mProperty.getLatitude()));
                params.put("propertylong", String.valueOf(mProperty.getLongitude()));
                params.put("propertycost", mEditCost.getText().toString());
                params.put("propertysize", mEditSize.getText().toString());
                params.put("propertydesc", mEditDescription.getText().toString());
                params.put("propertystatus", "yes");
                params.put("userid", "17");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                if (mBitmap1 != null) {
                    params.put("propertyimg1", new DataPart(mEditName.getText().toString() + "1.jpg", getFileDataFromBitmap(mBitmap1)));
                }
                if (mBitmap2 != null) {
                    params.put("propertyimg2", new DataPart(mEditName.getText().toString() + "2.jpg", getFileDataFromBitmap(mBitmap2)));
                }
                if (mBitmap3 != null) {
                    params.put("propertyimg3", new DataPart(mEditName.getText().toString() + "3.jpg", getFileDataFromBitmap(mBitmap3)));
                }
                return params;
            }
        };
        VolleyController.getInstance().addToRequestQueue(volleyMultipartRequest);
    }


    private void setContent() {
        if (mProperty.getAddress2() == null) {
            mTextAddress.setText(mProperty.getAddress1());
        } else {
            mTextAddress.setText(mProperty.getAddress1() + mProperty.getAddress2());
        }
        mEditName.setText(mProperty.getName());
        mEditType.setText(mProperty.getType());
        mEditCost.setText(mProperty.getCost());
        mEditSize.setText(mProperty.getSize());
        mEditDescription.setText(mProperty.getDescription());
        if (mProperty.getImage1() != null) {
            Picasso.with(getContext())
                    .load(mProperty.getImage1())
                    .into(mImageUpload1);
        }
        if (mProperty.getImage2() != null) {
            Picasso.with(getContext())
                    .load(mProperty.getImage2())
                    .into(mImageUpload2);
        }
        if (mProperty.getImage3() != null) {
            Picasso.with(getContext())
                    .load(mProperty.getImage3())
                    .into(mImageUpload3);
        }
    }

    private void initView() {
        mTextAddress = (TextView) mView.findViewById(R.id.new_property_address);
        mTextAddress.setText("");
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
        Log.d("newProperty","onActivityResult");
        if (requestCode == PICK_IMAGE_REQUEST_1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri picUri = data.getData();
            filePath = getPath(picUri);
            Log.d("picUri", picUri.toString());
          //  Log.d("filePath", filePath);
            mImageUpload1.setImageURI(picUri);
            // mBitmap1 = setImage(data.getData(), mImageUpload1);
            try {
                mBitmap1 = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            mImageUpload1.setImageBitmap(mBitmap1);
//            mProperty.setBitmap1(mBitmap1);

        } else if (requestCode == PICK_IMAGE_REQUEST_2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri picUri = data.getData();
            filePath = getPath(picUri);
            Log.d("picUri", picUri.toString());
            //Log.d("filePath", filePath);
            mImageUpload1.setImageURI(picUri);
            try {
                mBitmap2 = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            mImageUpload2.setImageBitmap(mBitmap2);
  //          mProperty.setBitmap2(mBitmap2);
        } else if (requestCode == PICK_IMAGE_REQUEST_3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri picUri = data.getData();
            filePath = getPath(picUri);
            Log.d("picUri", picUri.toString());
            //Log.d("filePath", filePath);
            mImageUpload1.setImageURI(picUri);
            try {
                mBitmap3 = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            mImageUpload3.setImageBitmap(mBitmap3);
    //        mProperty.setBitmap3(mBitmap3);
        }
    }
    private String getPath(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getActivity().getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private byte[] getFileDataFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private Bitmap getBitmapFromByte(byte[] file) {
        return BitmapFactory.decodeByteArray(file, 0, file.length);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("newProperty","onPause");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("newProperty","onSaveInstanceState");
        outState.putString("mName",mEditName.getText().toString());
        outState.putString("mType",mEditType.getText().toString());
        outState.putString("mCost",mEditCost.getText().toString());
        outState.putString("mSize",mEditSize.getText().toString());
        outState.putString("mDescription",mEditDescription.getText().toString());
        if (mBitmap1 != null) {
            outState.putByteArray("mImage1", getFileDataFromBitmap(mBitmap1));
        }
        if (mBitmap2 != null) {
            outState.putByteArray("mImage2", getFileDataFromBitmap(mBitmap2));
        }
        if (mBitmap3 != null) {
            outState.putByteArray("mImage3", getFileDataFromBitmap(mBitmap3));
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("newProperty","onActivityCreated");
        customRestoreView(savedInstanceState);

    }

    private void customRestoreView(Bundle savedInstanceState) {
        if (savedInstanceState != null){
            mEditName.setText(savedInstanceState.getString("mName"));
            mEditType.setText(savedInstanceState.getString("mType"));
            mEditCost.setText(savedInstanceState.getString("mCost"));
            mEditSize.setText(savedInstanceState.getString("mSize"));
            mEditDescription.setText(savedInstanceState.getString("mDescription"));
            if (savedInstanceState.getByteArray("mImage1") != null) {
                mImageUpload1.setImageBitmap(getBitmapFromByte(savedInstanceState.getByteArray("mImage1")));
            }
            if (savedInstanceState.getByteArray("mImage2") != null) {
                mImageUpload2.setImageBitmap(getBitmapFromByte(savedInstanceState.getByteArray("mImage2")));
            }
            if (savedInstanceState.getByteArray("mImage3") != null) {
                mImageUpload3.setImageBitmap(getBitmapFromByte(savedInstanceState.getByteArray("mImage3")));
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("newProperty","onStop");
    }
}