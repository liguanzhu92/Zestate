package com.guanzhuli.zestate.realtor;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import com.guanzhuli.zestate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabAllFragment extends Fragment {


    public TabAllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_all, container, false);
        TextView textView = (TextView)view.findViewById(R.id.tab_all_textview);
        Typeface custom_font = Typeface.createFromAsset(getContext().getAssets(),  "fonts/CaviarDreams.ttf");

        textView.setTypeface(custom_font);
        return view;
    }

}
