package com.ChenBahaCareer.fitbookskeleton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IK on 30/12/2014.
 */
public class WorkoutsNoEdits extends Activity implements AdapterView.OnItemClickListener {
    static ListView work;
    static ArrayList<String> workouts = new ArrayList<String>();
    EditText workoutedit;
    static ArrayList<String> workoutIDs = new ArrayList<String>();
    static String globalWorkoutsUser = null;

    public void onCreate(Bundle savedInstanceState){
        Parse.initialize(this, "ZlzULTgrXQC0lyYZLb8kX7fgz2TQU2YDRsjGkQsg", "NT6sZm7jhwdSfYffKbFWBe4GeRT0VyB36sxHHL6L");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,workouts);
        work = (ListView) findViewById(R.id.listView);
        registerForContextMenu(work);
        work.setAdapter(adapter);
        work.setOnItemClickListener(this);
        getWorkouts();
    }

    public void getWorkouts(){
        ParseQuery <ParseObject> query = ParseQuery.getQuery("Workouts");
        if (globalWorkoutsUser!=null){
            query.whereEqualTo("Username",globalWorkoutsUser);
        }
        else query.whereEqualTo("Username",ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                workouts.removeAll(workouts);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(WorkoutsNoEdits.this, android.R.layout.simple_list_item_1, workouts);
                for (int i = 0; i < parseObjects.size(); i++) {
                    workouts.add(parseObjects.get(i).get("WorkoutName").toString());
                    workoutIDs.add(parseObjects.get(i).getObjectId());
                }
                work.setAdapter(adapter);
            }
        });
        globalWorkoutsUser = null;
    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add("Delete");
        menu.add("Share");
    }
    public boolean onContextItemSelected(MenuItem item) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,workouts);
        work.setAdapter(adapter);
        if (item.getTitle()== "Delete"){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int position = info.position;
            super.onContextItemSelected(item);
            System.out.println(position);
            adapter.remove(adapter.getItem(position));
            adapter.notifyDataSetChanged();
            long currentwork = adapter.getItemId(position);
            System.out.println(currentwork);
        }
        else if (item.getTitle() == "Share"){
            Intent friendIntent = new Intent (this, SearchFriends.class);
            startActivity(friendIntent);
        }
        return true;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
        final Intent table = new Intent(WorkoutsNoEdits.this, TrainingActivityClone.class);
        try {
            System.out.println(i);
            String objname = workoutIDs.get(i);
            System.out.println(objname);
            table.putExtra("objID", objname + " " + i);
            startActivity(table);
        }catch(IndexOutOfBoundsException g){
            ParseQuery query = ParseQuery.getQuery("Workouts");
            query.whereEqualTo("WorkoutName",adapterView.getItemAtPosition(i).toString());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    String objname = parseObjects.get(0).getObjectId();
                    System.out.println(objname);
                    table.putExtra("objID",objname + " " + i);
                    startActivity(table);
                }
            });

        }
    }

    public void getFriends (){
        String [] mutualFriends = {""};
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Friendship");
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Friendship");
        query.whereEqualTo("toUser", currentUser.getUsername());
        query2.whereEqualTo("fromUser", currentUser.getUsername());
        query.whereEqualTo("status", "Mutual");
        query2.whereEqualTo("status", "Mutual");
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects,
                             ParseException e) {
                if (e == null) {
                    Log.d("Brand", "Retrieved " + objects.size() + " Brands");
                    for (ParseObject dealsObject : objects) {
                        // use dealsObject.get('columnName') to access the properties of the Deals object.
                        Object testing = new Object();
                        testing = dealsObject.get("fromUser");
                        System.out.println(testing.toString());
                        sendPush(testing.toString());
                    }
                } else {
                    Log.d("Brand", "Error: " + e.getMessage());
                }

            }
        });
        query2.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects,
                             ParseException e) {
                if (e == null) {
                    Log.d("Brand", "Retrieved " + objects.size() + " Brands");
                    for (ParseObject dealsObject : objects) {
                        Object testing = new Object();
                        // use dealsObject.get('columnName') to access the properties of the Deals object.
                        testing =  dealsObject.get("toUser");
                        sendPush(testing.toString());
                    }
                } else {
                    Log.d("Brand", "Error: " + e.getMessage());
                }

            }
        });
    }
    public void sendPush (String username){
        ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
        query.whereEqualTo("username", username);
        ParsePush push = new ParsePush();
        push.setQuery(query);
        JSONObject obj = null;
        try {
            obj=new JSONObject();
            obj.put("alert",ParseUser.getCurrentUser().getUsername()+" has added a new workout");
            obj.put("action","com.ChenBahaCareer.fitbookskeleton.UPDATE_STATUS");
            obj.put("Workouts",ParseUser.getCurrentUser().getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println ("Issue creating JSON object");
        }
        System.out.println ("ayy");
        push.setData(obj);
        push.sendInBackground();
    }

}
