package com.ChenBahaCareer.fitbookskeleton;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
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
import com.parse.GetCallback;
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
public class Workouts extends Activity implements AdapterView.OnItemClickListener {
    static ListView work;
    ArrayList<String> workouts = new ArrayList<String>();
    EditText workoutedit;
    ArrayList<String> workoutIDs = new ArrayList<String>();
    ArrayList<String> current_friends = new ArrayList<String>();
    String globalWorkoutsUser = null;

    public void onCreate(Bundle savedInstanceState){
        Parse.initialize(this, "ZlzULTgrXQC0lyYZLb8kX7fgz2TQU2YDRsjGkQsg", "NT6sZm7jhwdSfYffKbFWBe4GeRT0VyB36sxHHL6L");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training);
    }

    public void onResume(){
        super.onResume();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,workouts);
        work = (ListView) findViewById(R.id.listView);
        registerForContextMenu(work);
        work.setAdapter(adapter);
        work.setOnItemClickListener(this);
        System.out.println ("Getting Workouts");
        getWorkouts();

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
    }
    public void addItemClick (View v){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,workouts);
        work.setAdapter(adapter);
        workoutedit = (EditText) findViewById(R.id.works);
        final String wtest = workoutedit.getText().toString();
        if (wtest.equals("")) {
            Toast.makeText(this, "Invalid Name", Toast.LENGTH_SHORT).show();
        }
        else{
            registerForContextMenu(findViewById(R.id.button1));
            openContextMenu(findViewById(R.id.button1));
        }

    }

    public void addItem (View v){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,workouts);
        //work.setAdapter(adapter);
        workoutedit = (EditText) findViewById(R.id.works);
        final String wtest = workoutedit.getText().toString();
        if (wtest.equals("")) {
            Toast.makeText(this, "Invalid Name", Toast.LENGTH_SHORT).show();
        }
        else{
            ParseObject wkout = new ParseObject("Workouts");
            workouts.add(wtest);
            workoutedit.setText("");
            imm.hideSoftInputFromWindow(workoutedit.getWindowToken(), 0);

            String cuser = ParseUser.getCurrentUser().getUsername();
            wkout.put("Username",cuser);
            wkout.put("WorkoutName", wtest);
            wkout.saveInBackground();
            ParseQuery<ParseObject> getID = ParseQuery.getQuery("Workouts");
            getID.whereEqualTo("WorkoutName",wtest);
            getID.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List <ParseObject> list, ParseException e) {
                    try {
                        String s = list.get(0).getObjectId();
                        workoutIDs.add(s);
                    }catch (IndexOutOfBoundsException f){
                        Toast.makeText(Workouts.this,"INDEX OUT OF BOUNDS", Toast.LENGTH_LONG);
                    }
                }
            });


            //Feed//
            ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Feed");
            query2.whereContainedIn("username", current_friends);
            query2.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    if (e == null) {

                        for (int i = 0; i < parseObjects.size(); i++) {
                            String goo = ParseUser.getCurrentUser().getUsername() + " has created a new workout called " + wtest;
                            parseObjects.get(i).add("feed", goo);
                            parseObjects.get(i).saveInBackground();

                        }
                    } else
                        System.out.println("Workout feed failed.");
                }
            });
        }
        System.out.println(workoutIDs);
        getFriends();
        getWorkouts();
    }
    public void getWorkouts(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Workouts");
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Workouts");
        if (globalWorkoutsUser!=null){
            query.whereEqualTo("Username", globalWorkoutsUser);
        }
        else {
            workouts.removeAll(workouts);
            workoutIDs.clear();
            query.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Workouts.this, android.R.layout.simple_list_item_1, workouts);
                    for (int i = 0; i < parseObjects.size(); i++) {
                        System.out.println ("Getting normal workouts");
                        workouts.add(parseObjects.get(i).get("WorkoutName").toString());
                        workoutIDs.add(parseObjects.get(i).getObjectId());
                        System.out.println ("Storing ObjectID: "+ parseObjects.get(i).getObjectId());
                    }
                    work.setAdapter(adapter);
                }
            });
            query2.whereEqualTo("Share", ParseUser.getCurrentUser().getUsername());
            query2.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Workouts.this, android.R.layout.simple_list_item_1, workouts);
                    for (int i = 0; i < parseObjects.size(); i++) {
                        System.out.println ("Got workout: "+ parseObjects.get(i).get("WorkoutName").toString());
                        workouts.add(parseObjects.get(i).get("WorkoutName").toString());
                        workoutIDs.add(parseObjects.get(i).getObjectId());
                    }
                    work.setAdapter(adapter);
                }
            });
        }
        globalWorkoutsUser = null;
    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()== R.id.listView) {
            menu.add("Delete");
            //NO SHARING FUNCTIONALITY IN ALPHA
            //menu.add("Share");
        }
//      REMOVED FOR ALPHA VERSION -- SHAREPAGE IS NOT ACCESSIBLE
        if (v.getId()== R.id.button1){
            menu.add("Personal workout");
            //menu.add("Share with a friend");
        }
    }
    public boolean onContextItemSelected(MenuItem item) {
        System.out.println(workoutIDs);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,workouts);
        work.setAdapter(adapter);
        if (item.getTitle()== "Delete"){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            final int position = info.position;
            //System.out.println(position);
            //System.out.println(workoutIDs.get(position));
            adapter.remove(adapter.getItem(position));
            ParseQuery query = ParseQuery.getQuery("Workouts");
            query.getInBackground(workoutIDs.get(position), new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {
                        parseObject.deleteInBackground();
                        Toast.makeText(Workouts.this, "Workout Deleted", Toast.LENGTH_SHORT).show();
                        System.out.println(workoutIDs);
                        workoutIDs.remove(position);
                        System.out.println(workoutIDs);

                    } else
                        Toast.makeText(Workouts.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                }
            });
            adapter.notifyDataSetChanged();
        }
        else if (item.getTitle() == "Share"){
            Intent friendIntent = new Intent (this, SearchFriends.class);
            startActivity(friendIntent);
        }
        else if (item.getTitle()=="Personal workout"){
            addItem(findViewById(R.id.button1));
        }
        else if (item.getTitle()=="Share with a friend"){
            shareWithFriends();
        }
        return true;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
        final Intent table = new Intent(Workouts.this, TrainingActivity.class);
        try {
            System.out.println("test" + workouts);
            System.out.println(i);
            String objname = workoutIDs.get(i);
            System.out.println("objID: "+ objname + " " + i + " " + workouts.get(i).trim());
            table.putExtra("objID", objname + " " + i + " " + workouts.get(i).trim());
            startActivity(table);
        }catch(IndexOutOfBoundsException g){
            ParseQuery query = ParseQuery.getQuery("Workouts");
            query.whereEqualTo("WorkoutName",adapterView.getItemAtPosition(i).toString());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    System.out.println("test" + workouts);
                    String objname = parseObjects.get(0).getObjectId();
                    System.out.println("objID: " + objname + " " + i + " " + workouts.get(i).trim());
                    table.putExtra("objID",objname + " " + i + " " + workouts.get(i).trim());
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

    public void shareWithFriends(){
        Intent share = new Intent(this, SharePage.class);
        startActivity(share);
    }

    public static void sharedWorkout(String username){
        System.out.println ("This is the sharing function " + username);
    }

    public void sendPush (String username){
        ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
        query.whereEqualTo("username", username);
        ParsePush push = new ParsePush();
        push.setQuery(query);
        JSONObject obj = null;
        try {
            obj=new JSONObject();
            obj.put("alert", ParseUser.getCurrentUser().getUsername()+" has added a new workout");
            obj.put("action","com.ChenBahaCareer.fitbookskeleton.UPDATE_STATUS");
            obj.put("Workouts", ParseUser.getCurrentUser().getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println ("Issue creating JSON object");
        }
        push.setData(obj);
        push.sendInBackground();
    }

}
