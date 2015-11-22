package com.ChenBahaCareer.fitbookskeleton;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
 * Created by IK on 30/12/2014.
 */
public class WorkoutFriend extends Activity implements AdapterView.OnItemClickListener {
    static ListView work;
    ArrayList<String> workouts = new ArrayList<String>();
    EditText workoutedit;
    ArrayList<String> workoutIDs = new ArrayList<String>();
    String globalWorkoutsUser = null;
    String profileNameRC;

    public void onCreate(Bundle savedInstanceState){
        Parse.initialize(this, "ZlzULTgrXQC0lyYZLb8kX7fgz2TQU2YDRsjGkQsg", "NT6sZm7jhwdSfYffKbFWBe4GeRT0VyB36sxHHL6L");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_friend);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,workouts);
        work = (ListView) findViewById(R.id.listView);
        registerForContextMenu(work);
        work.setAdapter(adapter);
        work.setOnItemClickListener(this);
        System.out.println ("Getting Workouts");
        Bundle extras = getIntent().getExtras();
        profileNameRC = extras.getString("username");
        System.out.println("Username Training : " + profileNameRC);
        getWorkouts();
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

    public void getWorkouts(){
        ParseQuery <ParseObject> query = ParseQuery.getQuery("Workouts");
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Workouts");
        if (globalWorkoutsUser!=null){
            query.whereEqualTo("Username", globalWorkoutsUser);
        }
        else {
            workouts.removeAll(workouts);
            workoutIDs.clear();
            query.whereEqualTo("Username", profileNameRC);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(WorkoutFriend.this, android.R.layout.simple_list_item_1, workouts);
                    for (int i = 0; i < parseObjects.size(); i++) {
                        try{
                            System.out.println ("Getting normal workouts");
                            workouts.add(parseObjects.get(i).get("WorkoutName").toString());
                            workoutIDs.add(parseObjects.get(i).getObjectId());
                            System.out.println ("Storing ObjectID: "+ parseObjects.get(i).getObjectId());
                        }catch (NullPointerException npe) {
                            Toast.makeText(getApplicationContext(), "Loading", Toast.LENGTH_SHORT).show();
                            System.out.println ("Running getWorkouts();");
                        }

                    }
                    work.setAdapter(adapter);
                }
            });
            query2.whereEqualTo("Share", ParseUser.getCurrentUser().getUsername());
            query2.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(WorkoutFriend.this, android.R.layout.simple_list_item_1, workouts);
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

    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
        final Intent table = new Intent(WorkoutFriend.this, TrainingFriend.class);
        try {
            System.out.println("test" + workouts);
            System.out.println(i);
            String objname = workoutIDs.get(i);
            System.out.println("objID: "+ objname + " " + i + " " + workouts.get(i).trim());
            table.putExtra("objID", objname + " " + i + " " + workouts.get(i).trim());
            table.putExtra("username", profileNameRC);
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

}
