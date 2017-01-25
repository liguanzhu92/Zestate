package com.guanzhuli.zestate.realtor.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import com.daimajia.swipe.util.Attributes;
import com.guanzhuli.zestate.R;
import com.guanzhuli.zestate.model.PostPropertyList;
import com.guanzhuli.zestate.model.Property;
import com.guanzhuli.zestate.realtor.adapter.SwipeSellerAdapter;
import com.guanzhuli.zestate.realtor.util.RecyclerItemClickListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabPostFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private SwipeSellerAdapter mAdapter;
    private PostPropertyList mList = PostPropertyList.getInstance();
    private SwipeRefreshLayout mRefreshLayout;
    private String mUserId;

    public TabPostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_post, container, false);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.tab_post_refresh);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mUserId = "17";
                mList.updateData(mUserId);
                if (mList.workStatus) {
                    mRefreshLayout.setRefreshing(false);
                    mList.workStatus = false;
                }
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_tab_post);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("PostTab", String.valueOf(position));
/*                Toast.makeText(getContext(), "nav to detail", Toast.LENGTH_SHORT).show();
                PropertyDetailFragment propertyDetailFragment = new PropertyDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("DetailPosition", position);
                propertyDetailFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.seller_activity_container, propertyDetailFragment)
                        .addToBackStack(SellerHomeFragment.class.getName())
                        .commit();*/
            }
        }));
        mAdapter = new SwipeSellerAdapter(getContext(), mList);
        ((SwipeSellerAdapter) mAdapter).setMode(Attributes.Mode.Single);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

}
