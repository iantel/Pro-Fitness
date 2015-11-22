package com.ChenBahaCareer.fitbookskeleton;

/**
 * Created by IK on 25/12/2014.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class TrainingActivity extends ActionBarActivity {

    ArrayList<String> allExers = new ArrayList<String>();
    String id;
    int itemClicked;
    String wName;
    TableLayout tableLayout;
    static ArrayList<String> current_friends = new ArrayList<String>();
    EditText exerName;
    EditText weightNum;
    EditText setNum;
    EditText repNum;
    EditText goalRepNum;
    TextView exer,weight,set,rep,goalRep;

    GridLayout.Spec exerSpec = GridLayout.spec(0);
    GridLayout.Spec weightSpec = GridLayout.spec(0);
    GridLayout.Spec repSpec = GridLayout.spec(0);
    GridLayout.Spec setSpec = GridLayout.spec(0);
    GridLayout.Spec goalRepSpec = GridLayout.spec(1);

    GridLayout.Spec exerCol = GridLayout.spec(0);
    GridLayout.Spec weightCol = GridLayout.spec(2);
    GridLayout.Spec repCol = GridLayout.spec(4);
    GridLayout.Spec setCol = GridLayout.spec(6);
    GridLayout.Spec repGoalCol = GridLayout.spec(2);

    GridLayout.LayoutParams exerParams = new GridLayout.LayoutParams(exerSpec,exerCol);
    GridLayout.LayoutParams weightParams = new GridLayout.LayoutParams(weightSpec,weightCol);
    GridLayout.LayoutParams repParams = new GridLayout.LayoutParams(repSpec,repCol);
    GridLayout.LayoutParams setParams = new GridLayout.LayoutParams(setSpec,setCol);
    GridLayout.LayoutParams goalRepParams = new GridLayout.LayoutParams(goalRepSpec,repGoalCol);

    GridLayout.Spec weightTextRow = GridLayout.spec(0);
    GridLayout.Spec repTextRow = GridLayout.spec(0);
    GridLayout.Spec setTextRow = GridLayout.spec(0);
    GridLayout.Spec goalRepTextRow = GridLayout.spec(1);

    GridLayout.Spec weightTextCol = GridLayout.spec(1);
    GridLayout.Spec repTextCol = GridLayout.spec(3);
    GridLayout.Spec setTextCol = GridLayout.spec(5);
    GridLayout.Spec goalrepTextCol = GridLayout.spec(1);

    GridLayout.LayoutParams weightTextParams = new GridLayout.LayoutParams(weightTextRow,weightTextCol);
    GridLayout.LayoutParams repTextParams = new GridLayout.LayoutParams(repTextRow,repTextCol);
    GridLayout.LayoutParams setTextParams = new GridLayout.LayoutParams(setTextRow,setTextCol);
    GridLayout.LayoutParams goalRepTextParams = new GridLayout.LayoutParams(goalRepTextRow,goalrepTextCol);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Parse.initialize(this, "ZlzULTgrXQC0lyYZLb8kX7fgz2TQU2YDRsjGkQsg", "NT6sZm7jhwdSfYffKbFWBe4GeRT0VyB36sxHHL6L");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabletest);
        View btn = findViewById(R.id.button3);
        btn.setVisibility(View.GONE);
        Bundle b = getIntent().getExtras();
        if (b!= null){
            String [] str;
            String toSplit = b.getString("objID");
            str = toSplit.split(" ");
            id = str[0];
            itemClicked = Integer.parseInt(str[1]);
            wName = str[2];
            System.out.println("Selected OBJID: " + id);
            System.out.println("Selected NUMBER: " + itemClicked);
            System.out.println("Selected NAME: " + wName);
        }
        prepData(itemClicked);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void newRow2 (View v){
        View btn = findViewById(R.id.button3);
        btn.setVisibility(View.VISIBLE);
        //LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //LayoutParams tableParams = new TableRow.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT,GridLayout.LayoutParams.WRAP_CONTENT);
        GridLayout tbr = new GridLayout(this);
        tbr.setColumnCount(8);
        tbr.setRowCount(2);

        TextView weight = new TextView(this);
        weight.setTextSize(12);
        weight.setLayoutParams(weightTextParams);
        TextView reps = new TextView(this);
        reps.setTextSize(12);
        reps.setLayoutParams(repTextParams);
        TextView sets = new TextView(this);
        sets.setTextSize(12);
        sets.setLayoutParams(setTextParams);
        TextView goalreps = new TextView(this);
        goalreps.setTextSize(12);
        goalreps.setLayoutParams(goalRepTextParams);

        weight.setText("Weight:");
        reps.setText("Reps:");
        sets.setText("Sets:");
        goalreps.setText("Goal Reps:");

        tableLayout  = (TableLayout) findViewById(R.id.MainTrainingLayout);

        exerName = new EditText(this);
        exerName.setHint("Name");
        exerName.setId(View.generateViewId());
        exerName.setLayoutParams(exerParams);
        exerName.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));

        weightNum = new EditText(this);
        weightNum.setTextSize(15);
        weightNum.setInputType(InputType.TYPE_CLASS_NUMBER);
        weightNum.setHint("Weight");
        weightNum.setId(View.generateViewId());
        weightNum.setLayoutParams(weightParams);

        repNum = new EditText(this);
        repNum.setTextSize(15);
        repNum.setInputType(InputType.TYPE_CLASS_NUMBER);
        repNum.setHint("Reps");
        repNum.setId(View.generateViewId());
        repNum.setLayoutParams(repParams);

        setNum = new EditText(this);
        setNum.setTextSize(15);
        setNum.setInputType(InputType.TYPE_CLASS_NUMBER);
        setNum.setHint("Sets");
        setNum.setId(View.generateViewId());
        setNum.setLayoutParams(setParams);

        goalRepNum = new EditText(this);
        goalRepNum.setTextSize(15);
        goalRepNum.setInputType(InputType.TYPE_CLASS_NUMBER);
        goalRepNum.setHint("Reps");
        goalRepNum.setId(View.generateViewId());
        goalRepNum.setLayoutParams(goalRepParams);

        tbr.addView(exerName,0);
        tbr.addView(weightNum,1);
        tbr.addView(repNum,2);
        tbr.addView(setNum,3);
        tbr.addView(goalRepNum,4);
        tbr.addView(weight);
        tbr.addView(reps);
        tbr.addView(sets);
        tbr.addView(goalreps);
        tableLayout.addView(tbr);
        System.out.println("Rows: " + tableLayout.getChildCount());
        System.out.println("ExerID: " + exerName.getId());
        System.out.println("WeightID: " + weightNum.getId());

        GridLayout test = (GridLayout) tableLayout.getChildAt(1);
        EditText  edittest = (EditText) test.getChildAt(0);
        String str = edittest.getText().toString();
        System.out.println("TESTNAME: " + str);

    }

    public void getExercise(View v){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        allExers.clear();

        TableLayout exercises = (TableLayout) findViewById(R.id.MainTrainingLayout);
        for (int i = 1; i < exercises.getChildCount(); i++){
            GridLayout exer = (GridLayout) exercises.getChildAt(i);
            EditText exername = (EditText) exer.getChildAt(0);
            EditText weight = (EditText) exer.getChildAt(1);
            EditText rep = (EditText) exer.getChildAt(2);
            EditText set = (EditText) exer.getChildAt(3);
            EditText goalrep = (EditText) exer.getChildAt(4);
            String e = exername.getText().toString();
            String w = weight.getText().toString();
            String r = rep.getText().toString();
            String s = set.getText().toString();
            String gr = goalrep.getText().toString();
            allExers.add(e);
            allExers.add(w);
            allExers.add(r);
            allExers.add(s);
            allExers.add(gr);
        }
        System.out.println(allExers);
        ParseQuery<ParseObject> exerQuery = ParseQuery.getQuery("Workouts");
        exerQuery.getInBackground(id, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                try {
                    parseObject.remove("Exercises");
                    parseObject.addAll("Exercises",allExers);
                    parseObject.saveInBackground();
                    Toast.makeText(TrainingActivity.this,"Exercise Saved!",Toast.LENGTH_SHORT).show();
                }catch (NullPointerException f){
                    parseObject.addAll("Exercises",allExers);
                    parseObject.saveInBackground();
                    Toast.makeText(TrainingActivity.this,"Exercise Saved!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //feed//
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Feed");
        query2.whereContainedIn("username", current_friends);
        query2.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {

                    for (int i = 0; i < parseObjects.size(); i++) {
                        String goo = ParseUser.getCurrentUser().getUsername() + " has updated their workout called " + wName;
                        parseObjects.get(i).add("feed", goo);
                        parseObjects.get(i).saveInBackground();

                    }
                } else
                    System.out.println("Workout feed failed.");

            }
        });
    }

    public void prepData(final int t){
        ParseQuery<ParseObject> user = ParseQuery.getQuery("Workouts");
        user.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());

        ParseQuery<ParseObject> share = ParseQuery.getQuery("Workouts");
        share.whereEqualTo("Share", ParseUser.getCurrentUser().getUsername());

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(user);
        queries.add(share);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                try {
                    System.out.println("NUMBER OF WORKOUTS: " + list.size());
                    List<String> array = list.get(t).getList("Exercises");
                    System.out.println("EXERCISES: " + array);
                    getData(array);
                }catch(NullPointerException f){
                    Toast.makeText(TrainingActivity.this, "No exercises in this workout", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getData(List<String> array){

        tableLayout  = (TableLayout) findViewById(R.id.MainTrainingLayout);
        ArrayList <String> exers = new ArrayList<String>();
        exers.addAll(array);
        for (int x = 1; x < tableLayout.getChildCount(); x++){
            tableLayout.removeViewAt(x);
        }

        for (int i = 0; i < exers.size()/5; i++){
            GridLayout tbr = new GridLayout(this);
            tbr.setColumnCount(8);
            tbr.setRowCount(2);

            TextView weight = new TextView(this);
            weight.setTextSize(12);
            weight.setLayoutParams(weightTextParams);
            TextView reps = new TextView(this);
            reps.setTextSize(12);
            reps.setLayoutParams(repTextParams);
            TextView sets = new TextView(this);
            sets.setTextSize(12);
            sets.setLayoutParams(setTextParams);
            TextView goalreps = new TextView(this);
            goalreps.setTextSize(12);
            goalreps.setLayoutParams(goalRepTextParams);

            weight.setText("Weight:");
            reps.setText("Reps:");
            sets.setText("Sets:");
            goalreps.setText("Goal Reps:");

            EditText loadedExer = new EditText(this);
            loadedExer.setText(exers.get(5*i));
            loadedExer.setId(View.generateViewId());
            loadedExer.setLayoutParams(exerParams);

            EditText loadedWeight = new EditText(this);
            loadedWeight.setText(exers.get(1 + (5*i)));
            loadedWeight.setId(View.generateViewId());
            loadedWeight.setLayoutParams(weightParams);

            EditText loadedRep = new EditText(this);
            loadedRep.setText(exers.get(2 + (5*i)));
            loadedRep.setId(View.generateViewId());
            loadedRep.setLayoutParams(repParams);

            EditText loadedSet = new EditText(this);
            loadedSet.setText(exers.get(3 + (5*i)));
            loadedSet.setId(View.generateViewId());
            loadedSet.setLayoutParams(setParams);

            EditText loadedGoal = new EditText(this);
            loadedGoal.setText(exers.get(4 + (5*i)));
            loadedGoal.setId(View.generateViewId());
            loadedGoal.setLayoutParams(goalRepParams);

            tbr.addView(loadedExer,0);
            tbr.addView(loadedWeight,1);
            tbr.addView(loadedRep,2);
            tbr.addView(loadedSet,3);
            tbr.addView(loadedGoal,4);

            tbr.addView(weight);
            tbr.addView(reps);
            tbr.addView(sets);
            tbr.addView(goalreps);
            tableLayout.addView(tbr);
        }


    }

    public void storeData(){
            System.out.println("id: "+id);
            System.out.println("allExers: "+ allExers);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Workouts");
            query.whereEqualTo("WorkoutName",id);
            query.getInBackground(id, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException g) {
                    if (g == null) {
                        object.addAll("Exercises", allExers);
                        System.out.println("getInBackground allExers: "+ allExers);
                        object.saveInBackground();
                        Toast.makeText(TrainingActivity.this, "Workout Saved", Toast.LENGTH_SHORT).show();
                        //allExers.clear();
                    } else {
                        Log.d("failed", "failed");
                    }
                }
            });
    }
}



