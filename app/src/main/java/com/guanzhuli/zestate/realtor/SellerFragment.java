package com.guanzhuli.zestate.realtor;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import com.guanzhuli.zestate.R;
import com.guanzhuli.zestate.SellerActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerFragment extends Fragment {
    private Button mButtonConfirm;
    private EditText mEditFirst, mEditLast,mEditMobile,mEditEmail, mEditPwd, mEditBroker;
    public SellerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller, container, false);
        initViews(view);
        mButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SellerActivity.class));
                getActivity().finish();
            }
        });
        return view;
    }

    private void initViews(View view) {
        mButtonConfirm = (Button) view.findViewById(R.id.seller_signup_confirm);
        mEditFirst = (EditText) view.findViewById(R.id.seller_signup_fname);
        mEditLast = (EditText) view.findViewById(R.id.seller_signup_lname);
        mEditMobile = (EditText) view.findViewById(R.id.seller_signup_mobile);
        mEditEmail = (EditText) view.findViewById(R.id.seller_signup_email);
        mEditPwd = (EditText) view.findViewById(R.id.seller_signup_pwd);
        mEditBroker = (EditText) view.findViewById(R.id.seller_signup_broker);
    }

}
