// Author Euan Millar, S1820947
package com.example.millar_euan_s1820947;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class listDateActivity extends AppCompatActivity
{
    private TextView rawDataDisplay;
    private String result = "";
    private String url1="";
    private Button backButton;
    private ArrayList<String> listItems=new ArrayList<String>();
    private LinkedList<incidentData> blist =  new LinkedList<incidentData>();
    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    private ArrayAdapter<String> adapter;
    private Date target_date;
    final Calendar myCalendar= Calendar.getInstance();
    EditText editText;
    private SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMM yyyy - HH:mm");
    private SimpleDateFormat calendarformatter = new SimpleDateFormat("dd MM yyyy");
    // Traffic Scotland Planned Roadworks XML link
    private ProgressBar spinner;
    private Button colourButton;
    private int total=0;
    private ListView listView;
    private Date start_date;
    private Date end_date;
    private String
            urlSource="https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    // Create a List from String Array elements
    List<String> urls = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        urls.add("https://trafficscotland.org/rss/feeds/roadworks.aspx");
        urls.add("https://trafficscotland.org/rss/feeds/plannedroadworks.aspx");
        urls.add("https://trafficscotland.org/rss/feeds/currentincidents.aspx");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_date);
        editText = (EditText) findViewById(R.id.datepick);
        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        spinner.getIndeterminateDrawable().setColorFilter(0xFF3700B3, android.graphics.PorterDuff.Mode.MULTIPLY);
        backButton = (Button)findViewById(R.id.backButton);
        backButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(listDateActivity.this,MainActivity.class);
                        startActivity(i);
                    }
                }
        );
        colourButton = (Button)findViewById(R.id.colourButton);
        colourButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("counter", String.valueOf(listView.getAdapter().getCount()));
                        Log.d("blist", String.valueOf(blist.size()));

                        for(int i = 0; i<listView.getChildCount(); i++){
                            incidentData incident = blist.get(i);
                            String[] incident_description = incident.getDescription().split("<br /");
                            String start = incident_description[0].split(": ")[1];
                            String end = incident_description[1].split(": ")[1];
                            try {
                                start_date = formatter.parse(start);
                                end_date = formatter.parse(end);
                                long diffInMillies = Math.abs(end_date.getTime() - start_date.getTime());
                                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                                Log.d("hello ",diff+"days"+" "+ incident.getTitle() );

                                View view1 = listView.getChildAt(0);
                                int nFirstPos = listView.getFirstVisiblePosition();
                                int nWantedPos = i - nFirstPos;

                                if ((nWantedPos >= 0) && (nWantedPos <= listView.getChildCount()))
                                {
                                    view1 = listView.getChildAt(nWantedPos);
                                    if (view1 == null)
                                        return;
                                    // else we have the view we want
                                }
                                if (diff<=7){view1.setBackgroundResource(R.color.purple_700);}
                                // Here, i want to change Previously Selected Item's Background Color to it's original(Which is 'Orange').
                                if (diff<=7){view1.setBackgroundResource(R.color.purple_700);}
                                if (diff<=31 && diff>7){view1.setBackgroundResource(R.color.green);}
                                if (diff<=180 && diff>31){view1.setBackgroundResource(R.color.yellow);}
                                if (diff<=365 && diff>180){view1.setBackgroundResource(R.color.orange);}
                                if ( diff>365){view1.setBackgroundResource(R.color.red);}
                            } catch (ParseException e) {
                                e.printStackTrace();
                                Log.d("ERRORNOW", String.valueOf(e));
                            }
                        }
                    }
                }
        );
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                spinner.setVisibility(View.VISIBLE);
                listItems.clear();
                blist.clear();
                adapter.notifyDataSetChanged();
                Log.d("calendar", String.valueOf(year));
                Log.d("calendar", String.valueOf(month));
                Log.d("calendar", String.valueOf(day));
                try {
                    target_date = calendarformatter.parse(day + " " + (month + 1) + " " + (year));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d("TargetDate", String.valueOf(target_date));
                startProgress();

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
                new DatePickerDialog(listDateActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        Log.e("MyTag","in onCreate");
        // Set up the raw links to the graphical components

        listView = (ListView) findViewById(R.id.ListViewID);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                Intent i = new Intent(listDateActivity.this,detailActivity.class);
                i.putExtra("selected_incident", (Parcelable) blist.get(arg2));
                startActivity(i);
            }


        });


        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems
        );



        listView.setAdapter(adapter);


        // Initializing a new String Array



        // Create an ArrayAdapter from List

        final ListView ListView = (ListView) findViewById(R.id.ListViewID);

    }

    private LinkedList<incidentData> parseData(String dataToParse)
    {
        Log.e("ERROR", "Begin Parse");
        incidentData widget = null;
        LinkedList <incidentData> alist = null;
        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( new StringReader( dataToParse ) );
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                // Found a start tag
                if(eventType == XmlPullParser.START_TAG)
                {
                    // Check which Tag we have
                    if (xpp.getName().equalsIgnoreCase("channel"))
                    {
                        alist  = new LinkedList<incidentData>();
                    }
                    else
                    if (xpp.getName().equalsIgnoreCase("item"))
                    {
                        Log.e("MyTag","Item Start Tag found");
                        widget = new incidentData();
                    }
                    else
                    if (xpp.getName().equalsIgnoreCase("title"))
                    {
                        if (widget!=null) {
                        // Now just get the associated text
                        String temp = xpp.nextText();
                        // Do something with text
                        Log.e("MyTag", "Title is " + temp);

                            widget.setTitle(temp);

                    }
                    }
                    else
                        // Check which Tag we have
                        if (xpp.getName().equalsIgnoreCase("description"))
                        {
                            if (widget!=null) {
                                // Now just get the associated text
                                String temp = xpp.nextText();
                                // Do something with text
                                Log.e("MyTag", "Description is " + temp);
                                widget.setDescription(temp);
                            }
                        }
                        else
                            // Check which Tag we have
                            if (xpp.getName().equalsIgnoreCase("point"))
                            {
                                // Now just get the associated text
                                String temp = xpp.nextText();
                                // Do something with text
                                Log.e("MyTag","Point is " + temp);
                                widget.setPoint(temp);
                            }
                            else
                                // Check which Tag we have
                                if (xpp.getName().equalsIgnoreCase("pubDate"))
                                {
                                    // Now just get the associated text
                                    String temp = xpp.nextText();
                                    // Do something with text
                                    Log.e("MyTag","Published date is " + temp);
                                    widget.setPubDate(temp);
                                }
                }
                else
                if(eventType == XmlPullParser.END_TAG)
                {
                    if (xpp.getName().equalsIgnoreCase("item"))
                    {
                        String[] incident_description = widget.getDescription().split("<br /");
                        String start_date = incident_description[0].split(": ")[1];
                        String end_date = incident_description[1].split(": ")[1];
                        if(target_date.after(formatter.parse(start_date)) && target_date.before(formatter.parse(end_date))) {
                            alist.add(widget);
                            blist.add(widget);
                            Log.d("startDate: ", String.valueOf(formatter.parse(start_date)));
                            Log.d("endDate: ", String.valueOf(formatter.parse(end_date)));
                            Log.d("targetDate: ", String.valueOf(target_date));
                            listItems.add(widget.getTitle());
                        adapter.notifyDataSetChanged();
                        }
                    }
                    else
                    if (xpp.getName().equalsIgnoreCase("channel"))
                    {
                        int size;
                        size = alist.size();
                        Log.e("MyTag","widgetcollection size is " + size);



                    }
                }


                // Get the next event
                eventType = xpp.next();

            } // End of while

            //return alist;
        }
        catch (XmlPullParserException ae1)
        {
            Log.e("MyTag","Parsing error" + ae1.toString());
        }
        catch (IOException ae1)
        {
            Log.e("MyTag","IO error during parsing");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.e("MyTag","End document");
        Log.e("MyTag",alist.toString());



        return alist;

    }
    public void startProgress()
    {
        // Run network access on a separate thread;

            new Thread(new Task(urls)).start();

    } //

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private List<String> urls2;
        public Task(List<String> aurls)
        {
                urls2 = aurls;}

        @Override
        public void run()
        {
            for(String url:urls2) {
                URL aurl;
                URLConnection yc;
                BufferedReader in = null;
                String inputLine = "";
                Log.e("MyTag", "in run");
                try {
                    Log.e("MyTag", "in try");
                    aurl = new URL(url);
                    yc = aurl.openConnection();
                    in = new BufferedReader(new
                            InputStreamReader(yc.getInputStream()));
                    Log.e("MyTag", "after ready");
                    //
                    // Now read the data. Make sure that there are no specific headers
                    // in the data file that you need to ignore.
                    // The useful data that you need is in each of the item entries
                    //
                    while ((inputLine = in.readLine()) != null) {
                        result = result + inputLine;

                    }
                    in.close();
                } catch (IOException ae) {
                    Log.e("MyTag", "ioexception in run");
                }
                //
                // Now that you have the xml data you can parse it

                // Now update the TextView to display raw XML data
                // Probably not the best way to update TextView
                // but we are just getting started !
            }
            listDateActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    spinner.setVisibility(View.GONE);
                    LinkedList<incidentData> alist = parseData(result);

                    Log.d("UI thread", "I am the UI thread");


                }
            });
        }
    }
}