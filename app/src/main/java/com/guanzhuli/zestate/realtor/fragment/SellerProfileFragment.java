package com.guanzhuli.zestate.realtor.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.guanzhuli.zestate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerProfileFragment extends Fragment {
    private TextView mTextUsername, mTextMobile, mTextEmail, mTextLogOut;
    private Button mButtonEditProfile, mButtonSaveProfile, mButtonDiscardProfile;
    private EditText mEditUserName, mEditMobile, mEditEmail;
    private LinearLayout mLinearLayout;
    private View mView;


    public SellerProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_seller_profile, container, false);
        initView();
        setData();
        return mView;
    }

    private void setData() {
        mButtonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLinearLayout.setVisibility(View.VISIBLE);
            }
        });
        mButtonDiscardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLinearLayout.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void initView() {
        mTextLogOut = (TextView) mView.findViewById(R.id.profile_logout);
        mTextUsername = (TextView) mView.findViewById(R.id.profile_username);
        mTextMobile = (TextView) mView.findViewById(R.id.profile_mobile);
        mTextEmail = (TextView) mView.findViewById(R.id.profile_email);
        mButtonEditProfile = (Button) mView.findViewById(R.id.profile_edit_button);
        mButtonSaveProfile = (Button) mView.findViewById(R.id.profile_save_button);
        mButtonDiscardProfile = (Button) mView.findViewById(R.id.profile_discard_button);
        mLinearLayout = (LinearLayout) mView.findViewById(R.id.seller_profile_linear);
        mLinearLayout.setVisibility(View.INVISIBLE);
    }

}
