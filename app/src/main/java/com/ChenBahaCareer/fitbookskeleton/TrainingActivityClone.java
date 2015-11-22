package com.ChenBahaCareer.fitbookskeleton;

/**
 * Created by IK on 25/12/2014.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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

public class TrainingActivityClone extends ActionBarActivity {

    static ArrayList<String> allExers = new ArrayList<String>();
    String id;
    int itemClicked;
    static TableLayout tableLayout;
    ArrayList <String> currentWorkout = new ArrayList <String>();
    EditText exerName;
    EditText weightNum;
    EditText setNum;
    EditText repNum;
    EditText goalRepNum;
    TextView exer,weight,set,rep,goalRep;
    static String globalTrainingActivityUsername = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Parse.initialize(this, "ZlzULTgrXQC0lyYZLb8kX7fgz2TQU2YDRsjGkQsg", "NT6sZm7jhwdSfYffKbFWBe4GeRT0VyB36sxHHL6L");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabletest);
        Bundle b = getIntent().getExtras();
        if (b!= null){
            String [] str;
            String toSplit = b.getString("objID");
            str = toSplit.split(" ");
            id = str[0];
            itemClicked = Integer.parseInt(str[1]);
            System.out.println(id);
            System.out.println(itemClicked);
        }
        prepData(itemClicked);
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

    public void getExercise(View v){

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        allExers.removeAll(allExers);

        String e = exerName.getText().toString();
        String w = weightNum.getText().toString();
        String s = setNum.getText().toString();
        String r = repNum.getText().toString();
        String gr = goalRepNum.getText().toString();
        imm.hideSoftInputFromWindow(repNum.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(exerName.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(setNum.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(weightNum.getWindowToken(), 0);
        allExers.add(e);
        allExers.add(w);
        allExers.add(s);
        allExers.add(r);
        allExers.add(gr);
        storeData();
        EditText newExer = new EditText(TrainingActivityClone.this);
        EditText newWeight = new EditText(TrainingActivityClone.this);
        EditText newSet = new EditText(TrainingActivityClone.this);
        EditText newRep = new EditText(TrainingActivityClone.this);
        EditText newGoalRep = new EditText(TrainingActivityClone.this);
        exerName.setId(newExer.getId());
        weightNum.setId(newWeight.getId());
        setNum.setId(newSet.getId());
        repNum.setId(newRep.getId());
        goalRepNum.setId(newGoalRep.getId());
    }
    public void prepData(final int t){

        ParseQuery query = ParseQuery.getQuery("Workouts");
        if (globalTrainingActivityUsername!=null){
            query.whereEqualTo("Username",globalTrainingActivityUsername);
        }
        else query.whereEqualTo("Username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List <ParseObject> list, ParseException e) {
                try {
                        System.out.println(list.size());
                        List<String> array = list.get(t).getList("Exercises");
                        System.out.println(array);
                        getData(array);
                }catch(NullPointerException f){
                    Toast.makeText(TrainingActivityClone.this, "No exercises in this workout", Toast.LENGTH_SHORT).show();
                }
             }
        });
    }
    public void getData(List<String> array){
        ArrayList <String> exers = new ArrayList<String>();
        exers.addAll(array);
        for (int x = 0; x < exers.size()/5; x++){
            int multiple = x;
            LayoutInflater inflater = (LayoutInflater)TrainingActivityClone.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            tableLayout = (TableLayout) findViewById(R.id.MainTrainingLayout);
            View tr = inflater.inflate(R.layout.row_noneditable, tableLayout, false);
            tableLayout.addView(tr,new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            exer = (TextView) findViewById(R.id.exerciseText);
            weight = (TextView) findViewById(R.id.weightText);
            set = (TextView) findViewById(R.id.setText);
            rep = (TextView) findViewById(R.id.repText);
            goalRep = (TextView) findViewById(R.id.goalRepText);
            exer.setText(exers.get(0 + 5*multiple));
            weight.setText(exers.get(1 + 5 * multiple));
            set.setText(exers.get(2 + 5*multiple));
            rep.setText(exers.get(3 + 5*multiple));
            goalRep.setText(exers.get(4 + 5*multiple));
            TextView newExer = new TextView(TrainingActivityClone.this);
            TextView newWeight = new TextView(TrainingActivityClone.this);
            TextView newSet = new TextView(TrainingActivityClone.this);
            TextView newRep = new TextView(TrainingActivityClone.this);
            TextView newGoalRep = new TextView(TrainingActivityClone.this);
            exer.setId(newExer.getId());
            weight.setId(newWeight.getId());
            set.setId(newSet.getId());
            rep.setId(newRep.getId());
            goalRep.setId(newGoalRep.getId());
        }
    }



    public void storeData(){
            System.out.println(id);
            System.out.println(allExers);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Workouts");
            query.whereEqualTo("WorkoutName",id);
            query.getInBackground(id, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException g) {
                    if (g == null) {
                        object.addAll("Exercises", allExers);
                        object.saveInBackground();
                        Toast.makeText(TrainingActivityClone.this, "Workout Saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("failed", "failed");
                    }
                }
            });
    }

}
