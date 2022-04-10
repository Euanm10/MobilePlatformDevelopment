// Author Euan Millar, S1820947
package com.example.millar_euan_s1820947;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements
        OnClickListener
{
    private EditText editText;
    private TextView errorList;
    private Button refreshButton;
    private Button listAllButton;
    private Button listRoadButton;
    private Button journeyButton;
    private String result = "";
    private String url1="";
    private String target_date;
    private Boolean is_internet = false;
    final Calendar myCalendar= Calendar.getInstance();
    private SimpleDateFormat calendarformatter = new SimpleDateFormat("dd MM yyyy");
    // Traffic Scotland Planned Roadworks XML link
    private String
            urlSource="https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private Button ListDateButton;
    Thread isInternet = new Thread(new Runnable() {

        @Override
        public void run() {
            try {
                Log.d("Thread", "Internet check begin.");
                InetAddress ipAddr = InetAddress.getByName("www.google.com");
                Log.d("Thread", String.valueOf(!ipAddr.equals("")));
                //You can replace it with your name
                is_internet = !ipAddr.equals("");

            } catch (Exception e) {
                Log.e("ERROR",e.toString());
                e.printStackTrace();
                is_internet = false;
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        isInternet.start();
        try {
            isInternet.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!is_internet){ setContentView(R.layout.activity_no_internet);
            refreshButton = (Button)findViewById(R.id.refresh);
            refreshButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(MainActivity.this,MainActivity.class);

                            startActivity(i);
                        }
                    }
            );}
        else{
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.journeyInput);
        Log.e("MyTag","in onCreate");
        // Set up the raw links to the graphical components

        listAllButton = (Button)findViewById(R.id.listAllButton);
        listAllButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MainActivity.this,listAllActivity.class);
                        startActivity(i);
                    }
                }
        );
        listRoadButton = (Button)findViewById(R.id.byRoad);
        listRoadButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MainActivity.this,listRoadActivity.class);
                        startActivity(i);
                    }
                }
        );
        ListDateButton = (Button)findViewById(R.id.byDate);
        ListDateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MainActivity.this,listDateActivity.class);
                        i.putExtra("selected_incident",  result);
                        startActivity(i);
                    }
                }
        );

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {


                target_date = (day + " " + (month + 1) + " " + (year));

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                String myFormat="dd/MM/yy";
                SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat);
                editText.setText(dateFormat.format(myCalendar.getTime()));

            }

        };
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MainActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        journeyButton = (Button)findViewById(R.id.journey);
        journeyButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (target_date !=null) {
                            errorList = (TextView) findViewById(R.id.errorempty);
                            errorList.setVisibility(View.GONE);
                            Intent i = new Intent(MainActivity.this, journeyPlanActivity.class);
                            Log.d("target_date", String.valueOf(target_date));
                            i.putExtra("target_date", String.valueOf(target_date));
                            startActivity(i);
                        }
                        else{
                            errorList = (TextView) findViewById(R.id.errorempty);
                            errorList.setVisibility(View.VISIBLE);
                        };
                    }
                }
        );
    }}


    @Override
    public void onClick(View view) {

    }
}