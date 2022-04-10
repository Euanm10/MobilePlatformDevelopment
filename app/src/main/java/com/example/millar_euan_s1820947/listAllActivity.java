// Author Euan Millar, S1820947
package com.example.millar_euan_s1820947;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class listAllActivity extends AppCompatActivity
{
    private TextView rawDataDisplay;

    private String result = "";
    private String url1="";
    private ArrayList<String> listItems=new ArrayList<String>();
    private LinkedList<incidentData> blist =  new LinkedList<incidentData>();
    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    private ArrayAdapter<String> adapter;
    private ProgressBar spinner;
    private Button backButton;


    // Traffic Scotland Planned Roadworks XML link
    private String
            urlSource="https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    // Create a List from String Array elements
    List<String> urls = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        urls.add("https://trafficscotland.org/rss/feeds/roadworks.aspx");
        urls.add("https://trafficscotland.org/rss/feeds/plannedroadworks.aspx");
        urls.add("https://trafficscotland.org/rss/feeds/currentincidents.aspx");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all);
        Log.e("MyTag","in onCreate");
        // Set up the raw links to the graphical components
        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        spinner.getIndeterminateDrawable().setColorFilter(0xFF3700B3, android.graphics.PorterDuff.Mode.MULTIPLY);
        spinner.setVisibility(View.VISIBLE);
        backButton = (Button)findViewById(R.id.backButton);
        backButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(listAllActivity.this,MainActivity.class);
                        startActivity(i);
                    }
                }
        );
        android.widget.ListView listView = (ListView) findViewById(R.id.ListViewID);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                Intent i = new Intent(listAllActivity.this,detailActivity.class);
                i.putExtra("selected_incident", (Parcelable) blist.get(arg2));
                startActivity(i);

            }

        });
        Log.e("MyTag","after startButton");
        startProgress();
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
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
                        Log.e("MyTag","widget is " + widget.toString());
                        alist.add(widget);
                        blist.add(widget);
                        listItems.add(widget.getTitle());
                        adapter.notifyDataSetChanged();
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
            listAllActivity.this.runOnUiThread(new Runnable()
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