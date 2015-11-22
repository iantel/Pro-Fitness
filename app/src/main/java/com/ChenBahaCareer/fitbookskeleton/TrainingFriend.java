package com.ChenBahaCareer.fitbookskeleton;

/**
 * Created by IK on 25/12/2014.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
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

import java.util.ArrayList;
import java.util.List;

public class TrainingFriend extends ActionBarActivity {

    ArrayList<String> allExers = new ArrayList<String>();
    String id;
    int itemClicked;
    String wName;
    TableLayout tableLayout;
    TextView exerName;
    TextView weightNum;
    TextView setNum;
    TextView repNum;
    TextView goalRepNum;
    TextView exer,weight,set,rep,goalRep;

    GridLayout.Spec exerSpec = GridLayout.spec(0);
    GridLayout.Spec weightSpec = GridLayout.spec(0);
    GridLayout.Spec repSpec = GridLayout.spec(0);
    GridLayout.Spec setSpec = GridLayout.spec(1);
    GridLayout.Spec goalRepSpec = GridLayout.spec(1);

    GridLayout.Spec exerCol = GridLayout.spec(0);
    GridLayout.Spec weightCol = GridLayout.spec(1);
    GridLayout.Spec repCol = GridLayout.spec(2);
    GridLayout.Spec setCol = GridLayout.spec(0);
    GridLayout.Spec repGoalCol = GridLayout.spec(1);

    GridLayout.LayoutParams exerParams = new GridLayout.LayoutParams(exerSpec,exerCol);
    GridLayout.LayoutParams weightParams = new GridLayout.LayoutParams(weightSpec,weightCol);
    GridLayout.LayoutParams repParams = new GridLayout.LayoutParams(repSpec,repCol);
    GridLayout.LayoutParams setParams = new GridLayout.LayoutParams(setSpec,setCol);
    GridLayout.LayoutParams goalRepParams = new GridLayout.LayoutParams(goalRepSpec,repGoalCol);

    TextView test;

    String profileNameRC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Parse.initialize(this, "ZlzULTgrXQC0lyYZLb8kX7fgz2TQU2YDRsjGkQsg", "NT6sZm7jhwdSfYffKbFWBe4GeRT0VyB36sxHHL6L");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabletestclone);
        Bundle b = getIntent().getExtras();
        profileNameRC = b.getString("username");
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
    }

    public void newRow2 (View v){
        View btn = findViewById(R.id.button3);
        btn.setVisibility(View.VISIBLE);
        //LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //LayoutParams tableParams = new TableRow.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT,GridLayout.LayoutParams.WRAP_CONTENT);
        GridLayout tbr = new GridLayout(this);
        tbr.setColumnCount(4);
        tbr.setRowCount(2);

        tableLayout  = (TableLayout) findViewById(R.id.MainTrainingLayout);

        exerName = new TextView(this);
        exerName.setHint("Exercise");
        exerName.setId(View.generateViewId());
        exerName.setLayoutParams(exerParams);
        exerName.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        weightNum = new TextView(this);
        weightNum.setTextSize(15);
        //weightNum.setInputType(InputType.TYPE_CLASS_NUMBER);
        weightNum.setHint("Weight");
        weightNum.setId(View.generateViewId());
        weightNum.setLayoutParams(weightParams);

        repNum = new TextView(this);
        repNum.setTextSize(15);
        //repNum.setInputType(InputType.TYPE_CLASS_NUMBER);
        repNum.setHint("Reps");
        repNum.setId(View.generateViewId());
        repNum.setLayoutParams(repParams);

        setNum = new TextView(this);
        setNum.setTextSize(15);
        //setNum.setInputType(InputType.TYPE_CLASS_NUMBER);
        setNum.setHint("Sets");
        setNum.setId(View.generateViewId());
        setNum.setLayoutParams(setParams);

        goalRepNum = new TextView(this);
        goalRepNum.setTextSize(15);
        //goalRepNum.setInputType(InputType.TYPE_CLASS_NUMBER);
        goalRepNum.setHint("Goal Reps");
        goalRepNum.setId(View.generateViewId());
        goalRepNum.setLayoutParams(goalRepParams);

        //exerName.setNextFocusDownId(weightNum.getId());
        //weightNum.setNextFocusDownId(repNum.getId());
        //repNum.setNextFocusDownId(setNum.getId());
        //setNum.setNextFocusDownId(goalRepNum.getId());


        test = new TextView(this);
        test.setText("Test");

        tbr.addView(exerName,0);
        tbr.addView(weightNum,1);
        tbr.addView(repNum,2);
        tbr.addView(setNum,3);
        tbr.addView(goalRepNum,4);
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
            TextView exername = (TextView) exer.getChildAt(0);
            TextView weight = (TextView) exer.getChildAt(1);
            TextView rep = (TextView) exer.getChildAt(2);
            TextView set = (TextView) exer.getChildAt(3);
            TextView goalrep = (TextView) exer.getChildAt(4);
            String e = exername.getText().toString();
            String w = "Weight: "+ weight.getText().toString().replace ("Weight: ","");
            String r = "Reps: "+ rep.getText().toString().replace("Reps: ", "");
            String s = "Sets: "+ set.getText().toString().replace("Sets: ", "");
            String gr = "Goal Reps: "+ goalrep.getText().toString().replace("Goal Reps: ", "");
            allExers.add(e);
            allExers.add(w);
            allExers.add(r);
            allExers.add(s);
            allExers.add(gr);
        }
        System.out.println(allExers);
        ParseQuery <ParseObject> exerQuery = ParseQuery.getQuery("Workouts");
        exerQuery.getInBackground(id, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                try {
                    parseObject.remove("Exercises");
                    parseObject.addAll("Exercises",allExers);
                    parseObject.saveInBackground();
                    Toast.makeText(TrainingFriend.this,"Exercise Saved!",Toast.LENGTH_SHORT).show();
                }catch (NullPointerException f){
                    parseObject.addAll("Exercises",allExers);
                    parseObject.saveInBackground();
                    Toast.makeText(TrainingFriend.this,"Exercise Saved!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void prepData(final int t){
        ParseQuery<ParseObject> user = ParseQuery.getQuery("Workouts");
        user.whereEqualTo("Username", profileNameRC);

        ParseQuery<ParseObject> share = ParseQuery.getQuery("Workouts");
        share.whereEqualTo("Share", profileNameRC);

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
                    Toast.makeText(TrainingFriend.this, "No exercises in this workout", Toast.LENGTH_SHORT).show();
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
            tbr.setColumnCount(4);
            tbr.setRowCount(2);

            TextView loadedExer = new TextView(this);
            loadedExer.setText(exers.get(5 * i));
            loadedExer.setId(View.generateViewId());
            loadedExer.setLayoutParams(exerParams);

            TextView loadedWeight = new TextView(this);
            loadedWeight.setText(exers.get(1 + (5 * i)));
            loadedWeight.setId(View.generateViewId());
            loadedWeight.setLayoutParams(weightParams);
            //loadedWeight.setInputType(InputType.TYPE_CLASS_NUMBER);
            loadedWeight.setOnFocusChangeListener(new appendOnFocusListener());

            TextView loadedRep = new TextView(this);
            loadedRep.setText(exers.get(2 + (5 * i)));
            loadedRep.setId(View.generateViewId());
            loadedRep.setLayoutParams(repParams);
            //loadedRep.setInputType(InputType.TYPE_CLASS_NUMBER);
            loadedRep.setOnFocusChangeListener(new appendOnFocusListener());

            TextView loadedSet = new TextView(this);
            loadedSet.setText(exers.get(3 + (5 * i)));
            loadedSet.setId(View.generateViewId());
            loadedSet.setLayoutParams(setParams);
            //loadedSet.setInputType(InputType.TYPE_CLASS_NUMBER);
            loadedSet.setOnFocusChangeListener(new appendOnFocusListener());

            TextView loadedGoal = new TextView(this);
            loadedGoal.setText(exers.get(4 + (5 * i)));
            loadedGoal.setId(View.generateViewId());
            loadedGoal.setLayoutParams(goalRepParams);
            //loadedGoal.setInputType(InputType.TYPE_CLASS_NUMBER);
            loadedGoal.setOnFocusChangeListener(new appendOnFocusListener());

            loadedExer.setNextFocusForwardId(loadedWeight.getId());
            loadedWeight.setNextFocusForwardId(loadedRep.getId());
            loadedRep.setNextFocusForwardId(loadedSet.getId());
            loadedSet.setNextFocusForwardId(loadedGoal.getId());

            tbr.addView(loadedExer, 0);
            tbr.addView(loadedWeight,1);
            tbr.addView(loadedRep,2);
            tbr.addView(loadedSet,3);
            tbr.addView(loadedGoal,4);
            tableLayout.addView(tbr);
        }


    }

    private class appendOnFocusListener implements View.OnFocusChangeListener{
        String text = null;
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            EditText v = (EditText)view;
            if (hasFocus){
                text = v.getText().toString();
                v.setText(text.replace((text.substring(0, text.indexOf(":") + 2)), ""));
                System.out.println ("has focus text value is: " + text);
            }
            if (!hasFocus){
                v.setText(//"Weight: "+
                        text);
                System.out.println("Lost focus text value is: " + text);
                text="";

            }
        }
    }


}



