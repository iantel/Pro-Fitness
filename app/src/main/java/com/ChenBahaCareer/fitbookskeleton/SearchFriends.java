package com.ChenBahaCareer.fitbookskeleton;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 *Code created by Pro-Fit team
 *Copyrighted and ownership of OTG corp.
 *Code Name : AddFriends
 *Function : Add friends!
 */
public class SearchFriends extends Activity implements AdapterView.OnItemClickListener,Runnable {
    ListView friends;
    String friendID;
    static ArrayList<String> current_friends = new ArrayList<String>();
    static EditText friendedit, profiledata;


    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfriends);
        Parse.initialize(this, "ZlzULTgrXQC0lyYZLb8kX7fgz2TQU2YDRsjGkQsg", "NT6sZm7jhwdSfYffKbFWBe4GeRT0VyB36sxHHL6L");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, current_friends);
        friends = (ListView) findViewById(R.id.friendView);
        registerForContextMenu(friends);
        friends.setAdapter(adapter);
        friendedit = (EditText) findViewById(R.id.worked);
        getFriends();

    }
    @Override
    public void run() {
        //getFriends();
    }

    public void getFriends(){
        friends.setOnItemClickListener(this);
        current_friends.removeAll(current_friends);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Friendship");
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Friendship");
        query.whereEqualTo("toUser", ParseUser.getCurrentUser().getUsername());
        query2.whereEqualTo("fromUser", ParseUser.getCurrentUser().getUsername());
        query.whereEqualTo("status", "Mutual");
        query2.whereEqualTo("status", "Mutual");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null)
                    for (int i = 0; i < parseObjects.size(); i++) {
                        current_friends.add(parseObjects.get(i).get("fromUser").toString());
                    }
                else
                    System.out.println("Query Failed");
            }
        });
        query2.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null)
                    for (int i = 0; i < parseObjects.size(); i++) {
                        current_friends.add(parseObjects.get(i).get("toUser").toString());
                    }
                else
                    System.out.println("Query Failed");
            }
        });
        System.out.println(current_friends);
        friends = (ListView) findViewById(R.id.friendView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, current_friends);
        friends.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public String searchFriends(View v) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, current_friends);
        friends.setAdapter(adapter);
        friendedit = (EditText) findViewById(R.id.worked);
        imm.hideSoftInputFromWindow(friendedit.getWindowToken(), 0);
        final String ftest = friendedit.getText().toString();
        if (current_friends.contains(ftest)){
            Toast.makeText(getApplicationContext(), "Already Searched!", Toast.LENGTH_LONG).show();
            return "";
        }
        else if (current_friends.contains(ParseUser.getCurrentUser().getUsername())){
            Toast.makeText(getApplicationContext(), "That's You!", Toast.LENGTH_LONG).show();
            return "";
        }
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("username", ftest);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> object, ParseException e) {
                if (e == null && object.size() != 0) {
                    friendID = object.get(0).getObjectId();
                    System.out.println(object.get(0).getObjectId());
                    adapter.add(ftest);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "Not Found", Toast.LENGTH_LONG).show();
                }
            }
        });
        return ftest;
    }


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Delete");
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent fp = new Intent(this, FriendProfile.class);
        profiledata = (EditText) findViewById(R.id.worked);
        final String actualname = adapterView.getItemAtPosition(i).toString();
        fp.putExtra("ProfileName", actualname);
        fp.putExtra("ProfileID", friendID);
        startActivity(fp);
    }


}
