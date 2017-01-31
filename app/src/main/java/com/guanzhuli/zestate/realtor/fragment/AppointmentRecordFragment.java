package com.guanzhuli.zestate.realtor.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import com.guanzhuli.zestate.R;
import com.guanzhuli.zestate.realtor.adapter.CalendarAdapter;
import com.guanzhuli.zestate.realtor.util.CalendarCard;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentRecordFragment extends Fragment {

    private CalendarView mCalendarView;
    private TextView mTextShowCalendar;
    private RecyclerView mRecyclerView;
    private List<CalendarCard> mList = new ArrayList<>();

    public AppointmentRecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_appointment_record, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.seller_appointment_content);
        mCalendarView = (CalendarView) view.findViewById(R.id.seller_calendarView);
        mTextShowCalendar = (TextView) view.findViewById(R.id.appointment_show_calendar);
        mTextShowCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                if (mCalendarView.getVisibility() == View.VISIBLE) {
                    mCalendarView.setVisibility(View.GONE);
                    return;
                }
                if (mCalendarView.getVisibility() == View.GONE) {
                    mCalendarView.setVisibility(View.VISIBLE);
                    return;
                }*/
                selectDate();
            }
        });
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                // change adapter

                changeAdapter(String.valueOf(year) + month+day);
                CalendarAdapter adapter = new CalendarAdapter(getContext(), mList, year, month, day);
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.setHasFixedSize(false);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });

        return view;
    }
    private void selectDate() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                                                                @Override
                                                                public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                                                    Toast.makeText(getContext(), "Set Adapter", Toast.LENGTH_LONG).show();
                                                                }
                                                            }, now.get(Calendar.YEAR),
                now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        dpd.dismissOnPause(true);
        dpd.showYearPickerFirst(true);
        Calendar[] days = new Calendar[3];
        Calendar day = Calendar.getInstance();
        day.add(Calendar.DAY_OF_MONTH, 2);
        days[0] = day;
        Calendar day1 = Calendar.getInstance();
        day1.add(Calendar.DAY_OF_MONTH,3);
        days[1] = day1;
        Calendar day2 = Calendar.getInstance();
        day2.add(Calendar.DAY_OF_MONTH,5);
        days[2] = day2;
        dpd.setSelectableDays(days);
        dpd.show(getActivity().getFragmentManager(),"DatePicker");

    }
    private void changeAdapter(String s) {
        mList.clear();
        if (s.equals("2017028")) {
            List<String> mListName = Arrays.asList(getResources().getStringArray(R.array.buyer_name1));
            List<String> mListTime = Arrays.asList(getResources().getStringArray(R.array.timings_array1));
            List<String> mListMobile = Arrays.asList(getResources().getStringArray(R.array.mobile1));
            List<String> mListAddress = Arrays.asList(getResources().getStringArray(R.array.property_addr1));
            for (int i = 0; i < mListName.size(); i++) {
                CalendarCard calendarCard = new CalendarCard();
                calendarCard.setName(mListName.get(i));
                calendarCard.setAddress(mListAddress.get(i));
                calendarCard.setTimeSlot(mListTime.get(i));
                calendarCard.setMobile(mListMobile.get(i));
                mList.add(calendarCard);
            }

        } else if (s.equals("2017020")) {
            List<String> mListName = Arrays.asList(getResources().getStringArray(R.array.buyer_name2));
            List<String> mListTime = Arrays.asList(getResources().getStringArray(R.array.timings_array2));
            List<String> mListMobile = Arrays.asList(getResources().getStringArray(R.array.mobile2));
            List<String> mListAddress = Arrays.asList(getResources().getStringArray(R.array.property_addr2));
            for (int i = 0; i < mListName.size(); i++) {
                CalendarCard calendarCard = new CalendarCard();
                calendarCard.setName(mListName.get(i));
                calendarCard.setAddress(mListAddress.get(i));
                calendarCard.setTimeSlot(mListTime.get(i));
                calendarCard.setMobile(mListMobile.get(i));
                mList.add(calendarCard);
            }
        }
    }

}
