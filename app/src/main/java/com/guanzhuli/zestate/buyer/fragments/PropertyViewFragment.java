package com.guanzhuli.zestate.buyer.fragments;


import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.guanzhuli.zestate.R;
import com.guanzhuli.zestate.buyer.adapters.AppointmentReminder;
import com.guanzhuli.zestate.buyer.adapters.PropertyImagesRecyclerView;
import com.guanzhuli.zestate.buyer.adapters.PropertyRecyclerView;
import com.guanzhuli.zestate.controller.VolleyController;
import com.guanzhuli.zestate.model.AgentInfo;
import com.guanzhuli.zestate.model.Property;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;


public class PropertyViewFragment extends Fragment implements
        OnMapReadyCallback , DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private Property mProperty;
    private View view;
    private GoogleMap mCardMap;
    private AgentInfo agentInfo;
    private Button contactAgent, shceduleAppointment, messageAgent, callAgent;
    private EditText buyerPhoneNumber, buyerEmailId, propertyDescription;
    private String messageToAgent;
    TextView mSelectedDateText,mSelectedTimeText;
    private  int appointmentDay,appointmentMonth,appointmentHours,appointmentMinnutes;
    private int appointmentYear;


    public PropertyViewFragment() {
        // Required empty public constructor
    }

    public static PropertyViewFragment newInstance(String param1) {
        PropertyViewFragment fragment = new PropertyViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mProperty = VolleyController.getInstance().getmProperty().getPropertyByID(mParam1);
            Log.d(Property.class.getSimpleName(), mParam1 + "--" + mProperty.getId());

        }
        getAgentDetails();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_property_view, container, false);
        TextView address1 = (TextView) view.findViewById(R.id.property_view_Address1);
        TextView address2 = (TextView) view.findViewById(R.id.property_view_Address2);
        TextView propertySize = (TextView) view.findViewById(R.id.property_view_size);
        TextView propertyType = (TextView) view.findViewById(R.id.property_view_type);
        TextView propertyCost = (TextView) view.findViewById(R.id.property_view_cost);
        TextView propertyStatus = (TextView) view.findViewById(R.id.property_view_status);
        TextView propertyDesc = (TextView) view.findViewById(R.id.property_desc_text_view);
        contactAgent = (Button) view.findViewById(R.id.contact_agent_message);
        callAgent = (Button) view.findViewById(R.id.contact_agent_call);
        buyerPhoneNumber = (EditText) view.findViewById(R.id.buyer_phone_to_conatct);
        buyerEmailId = (EditText) view.findViewById(R.id.buyer_email_to_contact);
        propertyDescription = (EditText) view.findViewById(R.id.agent_property_description);
        CardView dateCard = (CardView) view.findViewById(R.id.shcedule_select_date);
        CardView timeCard = (CardView) view.findViewById(R.id.schedule_time_select);
        mSelectedDateText = (TextView) view.findViewById(R.id.schedule_date_text);
        mSelectedTimeText = (TextView) view.findViewById(R.id.schedule_time_text);
        shceduleAppointment = (Button) view.findViewById(R.id.shcedule_appointment_agent);
        propertyDescription.setText("Intrested in" + mProperty.getName() + mProperty.getAddress1());
        address1.setText(String.format("%s,%s", mProperty.getName(), mProperty.getAddress1()));
        address2.setText(String.format("%s,%d", mProperty.getAddress2(), mProperty.getZip()));
        propertySize.setText(mProperty.getSize() + "Sq.ft");
        propertyCost.setText("$" + mProperty.getCost());
        propertyType.setText(mProperty.getType());
        propertyStatus.setText(mProperty.getStatus());
        propertyDesc.setText(mProperty.getDescription());
        setPropertyImages();
        contactAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageToAgent();
            }
        });

        callAgent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callToAgent();
            }
        });
        dateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });
        timeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime();
            }
        });
        shceduleAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setAppointment();
            }
        });
        return view;
    }


    private void sendMessageToAgent() {
        if (errorIncontacts()) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                //smsManager.sendTextMessage(agentInfo.getPhoneNumber(),null,messageToAgent,null,null);
                smsManager.sendTextMessage("+7818271119", null, messageToAgent, null, null);
                Toast.makeText(getActivity(), "message sent", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getActivity(), "message sending failed", Toast.LENGTH_LONG).show();
            }

        }

    }

    private boolean errorIncontacts() {
        String phoneNumber = buyerPhoneNumber.getText().toString();
        String email = buyerEmailId.getText().toString();
        String description = propertyDescription.getText().toString();
        boolean phone = false;
        boolean emai, descr;
        if (phoneNumber.equals("")) {
            buyerPhoneNumber.setError("we suggest to enter phone number");
        } else if (!Pattern.matches("[a-zA-Z]+", phoneNumber)) {
            phone = true;
        } else
            buyerPhoneNumber.setError("phone Format error ennter correct");

        if (email.equals("")) {
            emai = false;
            buyerEmailId.setError("can't be empty");
        } else
            emai = true;

        if (description.equals("")) {
            descr = false;
            propertyDescription.setError("has to have some description");
        } else {
            descr = true;
        }
        if (phone && emai && descr)
            messageToAgent = phoneNumber + "\n" + email + "\n" + description;
        return phone && emai && descr;
    }

    private void callToAgent() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + "7818271119"));
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},100);
                    Log.d(PropertyViewFragment.class.getSimpleName(), "permission Failed");
            return;
        }
        Log.d(PropertyViewFragment.class.getSimpleName(),"permission is good");
        getActivity().startActivity(intent);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MapView mMapView = (MapView) view.findViewById(R.id.property_location_map);
        if (mMapView != null) {
            mMapView.onCreate(savedInstanceState);
            mMapView.getMapAsync(this);
        }
    }

    private void setPropertyImages() {

        String[] propertyImages = new String[3];
        int count = 0;
        if (!(mProperty.getImage1().equals(""))) {
            propertyImages[0] = mProperty.getImage1();
            count++;
            if (!(mProperty.getImage2().equals(""))) {
                count++;
                propertyImages[1] = mProperty.getImage2();
                if (!(mProperty.getImage3().equals(""))) {
                    count++;
                    propertyImages[2] = mProperty.getImage2();
                }
            }

        } else {
            count++;
            propertyImages[0] = "no image";
        }

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_images);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(new PropertyImagesRecyclerView(getActivity(), propertyImages, count));
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mCardMap = googleMap;
            LatLng ll = new LatLng(mProperty.getLatitude(),mProperty.getLongitude());
            mCardMap.addMarker(new MarkerOptions().position(ll)
            .title("property location"));
            mCardMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 12.0f));

        mCardMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }
        });
    }

    void getAgentDetails(){
        String agentUrl = "http://rjtmobile.com/realestate/register.php?userid="+mProperty.getUserId();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(agentUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.d("PropertViewFragment",jsonArray.toString());
                for (int i = 0;i<jsonArray.length();i++){
                    try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Log.d("PropertViewFragment",jsonObject.toString());

                        String userName = jsonObject.getString("User Name");
                        String userPhone = jsonObject.getString("User Mobile");
                        String userEmail = jsonObject.getString("User Email");
                        agentInfo = new AgentInfo(userName,userPhone,userEmail);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        Log.d("PropertViewFragment",jsonArrayRequest.getUrl());

        VolleyController.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //need to handle code here
    }

    private void selectTime() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),true);
        tpd.dismissOnPause(true);
        Timepoint[] timepoints = new Timepoint[4];
        Timepoint timepoint = new Timepoint(10,30);
        Timepoint timepoint1 = new Timepoint(12,00);
        Timepoint timepoint2 = new Timepoint(14,00);
        Timepoint timepoint3 = new Timepoint(16,30);
        timepoints[0]=timepoint ;
        timepoints[1]=timepoint1 ;
        timepoints[2]=timepoint2 ;
        timepoints[3]=timepoint3 ;
        tpd.setSelectableTimes(timepoints);
        tpd.show(getActivity().getFragmentManager(),"TimePicker");
    }

    private void selectDate() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(this,now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH));
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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        appointmentYear =year;
        appointmentMonth=monthOfYear;
        appointmentDay = dayOfMonth;
        mSelectedDateText.setText(dayOfMonth+"/"+(++monthOfYear)+"/"+year);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String time = hourString+"h"+minuteString+"m";
        appointmentHours=hourOfDay;
        appointmentMinnutes = minute;
        mSelectedTimeText.setText(time);
    }
    private void setAppointment() {
        Calendar notifyTime = Calendar.getInstance();
        notifyTime.set(appointmentYear,appointmentMonth-1,appointmentDay,appointmentHours,appointmentMinnutes);
        //Long alarmT = notifyTime.getTimeInMillis()-(2*60*60*100);
        Long alarmTime = new GregorianCalendar().getTimeInMillis()+5*1000;
        Intent alertIntent = new Intent(getActivity(), AppointmentReminder.class);
        alertIntent.putExtra("agentName",agentInfo.getUserName());
        alertIntent.putExtra("agentPhone",agentInfo.getPhoneNumber());
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,alarmTime, PendingIntent.getBroadcast(getActivity(),101,
                alertIntent,PendingIntent.FLAG_UPDATE_CURRENT));


    }
}
