package com.guanzhuli.zestate.realtor.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import com.daimajia.swipe.util.Attributes;
import com.guanzhuli.zestate.R;
import com.guanzhuli.zestate.model.Property;
import com.guanzhuli.zestate.realtor.adapter.SwipeSellerAdapter;
import com.guanzhuli.zestate.realtor.util.RecyclerItemClickListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabDraftFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private SwipeSellerAdapter mAdapter;
    private ArrayList<Property> mList = new ArrayList<>();

    public TabDraftFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_draft, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_tab_draft);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("Draft", String.valueOf(position));
                Toast.makeText(getContext(), "nav to detail", Toast.LENGTH_SHORT).show();
                PropertyDetailFragment propertyDetailFragment = new PropertyDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("DetailPosition", position);
                propertyDetailFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.seller_activity_container, propertyDetailFragment)
                        .addToBackStack(SellerHomeFragment.class.getName())
                        .commit();
            }
        }));
        mAdapter = new SwipeSellerAdapter(getContext(), mList);
        ((SwipeSellerAdapter) mAdapter).setMode(Attributes.Mode.Single);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

}
