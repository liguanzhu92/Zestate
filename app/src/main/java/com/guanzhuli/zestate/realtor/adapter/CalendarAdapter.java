package com.guanzhuli.zestate.realtor.adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.guanzhuli.zestate.R;
import com.guanzhuli.zestate.realtor.util.CalendarCard;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Guanzhu Li on 1/29/2017.
 */
public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarHolder> {

    private Context mContext;
    private List<CalendarCard> mCardList;
    private int mYear, mMonth, mDay;

    public CalendarAdapter(Context context, List<CalendarCard> cardList, int year, int month, int day) {
        mContext = context;
        mCardList = cardList;
        mYear = year;
        mMonth = month;
        mDay = day;
    }

    @Override
    public CalendarHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_calender, parent, false);
        CalendarHolder calendarHolder = new CalendarHolder(view);
        return calendarHolder;
    }

    @Override
    public void onBindViewHolder(CalendarHolder holder, final int position) {
        holder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent calIntent = new Intent(Intent.ACTION_INSERT);
                calIntent.setType("vnd.android.cursor.item/event");
                calIntent.putExtra(CalendarContract.Events.TITLE, "Meeting with " + mCardList.get(position).getName());
                calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, mCardList.get(position).getAddress());
                calIntent.putExtra(CalendarContract.Events.DESCRIPTION, mCardList.get(position).getMobile());

                GregorianCalendar calDate = new GregorianCalendar(mYear, mMonth, mDay);
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                        calDate.getTimeInMillis() + getStartMinutes(mCardList.get(position).getTimeSlot()) * 60 * 1000);
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                        calDate.getTimeInMillis() + getEndMinutes(mCardList.get(position).getTimeSlot()) * 60 * 1000);
                (mContext).startActivity(calIntent);
            }
        });
        holder.mTextName.setText(mCardList.get(position).getName());
        holder.mTextMobile.setText(mCardList.get(position).getMobile());
        holder.mTextTimeSlot.setText(mCardList.get(position).getTimeSlot());
        holder.mTextAddress.setText(mCardList.get(position).getAddress());
    }

    private int getEndMinutes(String s) {
        String temp  = s.substring(s.indexOf("to") + 2).trim();
        String hour = s.substring(0, s.indexOf(':'));
        String minute  = temp.substring(temp.indexOf(':') + 1);
        return Integer.parseInt(hour) * 60 + Integer.parseInt(minute);
    }

    private int getStartMinutes(String s) {
        int end  = s.indexOf("to");
        String temp = s.substring(0, end).trim();
        String hour = s.substring(0, s.indexOf(':'));
        String minute  = temp.substring(temp.indexOf(':') + 1);
        return Integer.parseInt(hour) * 60 + Integer.parseInt(minute);
    }

    @Override
    public int getItemCount() {
        return mCardList.size();
    }


    class CalendarHolder extends RecyclerView.ViewHolder{
        private TextView mTextName, mTextMobile, mTextAddress, mTextTimeSlot;
        private ImageView mImage;

        public CalendarHolder(View itemView) {
            super(itemView);
            mImage = (ImageView) itemView.findViewById(R.id.calendar_add);
            mTextName = (TextView) itemView.findViewById(R.id.calendar_name);
            mTextMobile = (TextView) itemView.findViewById(R.id.calendar_mobile);
            mTextAddress = (TextView) itemView.findViewById(R.id.calendar_address);
            mTextTimeSlot = (TextView) itemView.findViewById(R.id.calendar_time);
        }
    }
}


