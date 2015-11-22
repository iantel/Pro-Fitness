package com.ChenBahaCareer.fitbookskeleton;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by Darren on 2015-07-06.
 */

public class notifications extends ActionBarActivity implements AdapterView.OnItemClickListener {
    static ArrayList<String> feed = new ArrayList<String>();
    static ListView notifs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);
        Parse.initialize(this, "ZlzULTgrXQC0lyYZLb8kX7fgz2TQU2YDRsjGkQsg", "NT6sZm7jhwdSfYffKbFWBe4GeRT0VyB36sxHHL6L");
        getQuery();
        Toast.makeText(getApplicationContext(), "Tap to dismiss.", Toast.LENGTH_LONG).show();
    }

    public void getQuery(){
        feed.removeAll(feed);
        ParseQuery <ParseObject> query = ParseQuery.getQuery("Feed");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List <ParseObject>list, ParseException e) {
                try {
                    ArrayList temp = (ArrayList<String>) list.get(0).get("feed");
                    Collections.reverse(temp);
                    feed.addAll(temp);
                    System.out.println(feed);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(notifications.this, android.R.layout.simple_list_item_1, feed);
                    notifs = (ListView) findViewById(R.id.listView);
                    adapter.notifyDataSetChanged();
                    notifs.setAdapter(adapter);
                    notifs.setOnItemClickListener(notifications.this);

                } catch (NullPointerException f) {

                }
            }

        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        feed.remove(i);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(notifications.this, android.R.layout.simple_list_item_1,feed);
        notifs.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        ParseQuery query = ParseQuery.getQuery("Feed");
        query.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List <ParseObject> list, ParseException e) {
                list.get(0).put("feed",feed);
                list.get(0).saveInBackground();
            }
        });
    }
}
