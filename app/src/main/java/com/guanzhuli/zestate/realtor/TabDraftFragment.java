package com.guanzhuli.zestate.realtor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guanzhuli.zestate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabDraftFragment extends Fragment {


    public TabDraftFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_draft, container, false);
    }

}
