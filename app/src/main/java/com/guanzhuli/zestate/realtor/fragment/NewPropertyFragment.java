package com.guanzhuli.zestate.realtor.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.guanzhuli.zestate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewPropertyFragment extends Fragment {
    private View mView;
    private TextView mTextAddress;
    private EditText mEditName, mEditType, mEditCost, mEditSize, mEditDescription;
    private Button mButtonPost, mButtonDraft, mButtonDiscard;
    private Bundle mBundle;
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
            setContent();
        }
        return mView;
    }

    private void setContent() {
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
        /*-------image-----*/
        mTextImageName = (TextView) mView.findViewById(R.id.new_image_name1);
        mTextUploadButton = (TextView) mView.findViewById(R.id.new_image_upload1);
        /*-------image-----*/
    }

}
