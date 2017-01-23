package com.guanzhuli.zestate.realtor.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import com.guanzhuli.zestate.R;
import com.guanzhuli.zestate.model.PostPropertyList;

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
            int position = mBundle.getInt("EditPosition");
            setContent(position);
        }
        return mView;
    }

    private void setContent(int position) {
        mTextAddress.setText(mProperties.get(position).getAddress1() + mProperties.get(position).getAddress2() );
        mEditName.setText(mProperties.get(position).getName());
        mEditType.setText(mProperties.get(position).getType());
        mEditCost.setText(mProperties.get(position).getCost());
        mEditSize.setText(mProperties.get(position).getSize());
        mEditDescription.setText(mProperties.get(position).getDescription());
        mImageLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "show map to find location", Toast.LENGTH_LONG).show();
            }
        });
        mButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "send infor to web service", Toast.LENGTH_LONG).show();
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
