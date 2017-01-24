package com.guanzhuli.zestate.realtor.fragment;


import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewPropertyFragment extends Fragment {
    private View mView;
    private TextView mTextAddress;
    private EditText mEditName, mEditType, mEditCost, mEditSize, mEditDescription;
    private Button mButtonPost, mButtonDraft, mButtonDiscard;
    private ImageView mImageLocation;
    private PostPropertyList mProperties = PostPropertyList.getInstance();
    private Bundle mBundle;
    private int position;
    private static final String ADD_PROPERTY_URL = "http://www.rjtmobile.com/realestate/register.php?property&add";
    private static final String EDIT_PROPERTY_URL = "http://www.rjtmobile.com/realestate/register.php?property&edit&pptyid=";
    private boolean mBooleanAdd;
    /*-------image-----*/
    private TextView mTextImageName,mTextUploadButton;
    /*-------image-----*/


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
            setContent();
        } else {
            mBooleanAdd = true;
        }
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
    }

    private void editProperty() {
        String url = EDIT_PROPERTY_URL + mProperties.get(position).getId();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("NewProperty", "update success");
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
                params.put("propertyaddress1", mProperties.get(position).getAddress1());
                params.put("propertyaddress2", mProperties.get(position).getAddress2());
                params.put("propertyzip", String.valueOf(mProperties.get(position).getZip()));
                params.put("propertylat", String.valueOf(mProperties.get(position).getLatitude()));
                params.put("propertylong", String.valueOf(mProperties.get(position).getLongitude()));
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_PROPERTY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.contains("true")) {
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
                params.put("propertyimg1", "");
                params.put("propertyimg2", "");
                params.put("propertyimg3", "");
                params.put("userid", "162");
                return params;
            }
        };
        VolleyController.getInstance().addToRequestQueue(stringRequest);
    }

    private void setContent() {
        mTextAddress.setText(mProperties.get(position).getAddress1() + mProperties.get(position).getAddress2() );
        mEditName.setText(mProperties.get(position).getName());
        mEditType.setText(mProperties.get(position).getType());
        mEditCost.setText(mProperties.get(position).getCost());
        mEditSize.setText(mProperties.get(position).getSize());
        mEditDescription.setText(mProperties.get(position).getDescription());
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
        mTextImageName = (TextView) mView.findViewById(R.id.new_image_name1);
        mTextUploadButton = (TextView) mView.findViewById(R.id.new_image_upload1);
        /*-------image-----*/
    }

}
