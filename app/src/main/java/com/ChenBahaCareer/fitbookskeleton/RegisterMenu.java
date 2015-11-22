package com.ChenBahaCareer.fitbookskeleton;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.regex.Pattern;

/**
 *Code created by Pro-Fit team
 *Copyrighted and ownership of OTG corp.
 *Code Name : Register Menu
 *Function : Register handler
 */

public class RegisterMenu extends ActionBarActivity {
    RadioButton male, female;
    EditText name, lastname, username, password, cpassword, email;
    Spinner year, month, day;
    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registermenu);
    //Connect to the database
    //PLEASE NOTE THAT YOU NEED WIFI FOR THIS TO WORK!!!
    if (isOnline() == true) {
        Parse.initialize(this, "ZlzULTgrXQC0lyYZLb8kX7fgz2TQU2YDRsjGkQsg", "NT6sZm7jhwdSfYffKbFWBe4GeRT0VyB36sxHHL6L");
        } else {
        Toast.makeText(getApplicationContext(), "You are not connected to the internet.", Toast.LENGTH_LONG).show();
        }

    }

    public boolean isOnline() {
        ConnectivityManager cm =
        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public void ClearFemale(View g) {
        male = (RadioButton) findViewById(R.id.maleButton);
        female = (RadioButton) findViewById(R.id.femaleButton);
        female.setChecked(false);

    }

    public void ClearMale(View g) {
        male = (RadioButton) findViewById(R.id.maleButton);
        female = (RadioButton) findViewById(R.id.femaleButton);

        male.setChecked(false);
    }

    //To do : make more memory efficient?
    //Almost in the future add more restrictions to certain things like username
    //password etc.. so user can't put weird symbols.
    //Other then that this is pretty much complete
    public void Fuck(View g) {
        name = (EditText) findViewById(R.id.registerName);
        lastname = (EditText) findViewById(R.id.registerLastName);
        username = (EditText) findViewById(R.id.registerUsername);
        password = (EditText) findViewById(R.id.registerPassword);
        cpassword = (EditText) findViewById(R.id.registerPasswordConfirm);
        email = (EditText) findViewById(R.id.registerEmailAddress);
        year = (Spinner) findViewById(R.id.registerYear);
        month = (Spinner) findViewById(R.id.registerMonth);
        day = (Spinner) findViewById(R.id.registerDay);
        male = (RadioButton) findViewById(R.id.maleButton);
        female = (RadioButton) findViewById(R.id.femaleButton);
        done = (Button) findViewById(R.id.registerfinal);
        String sEmailId = email.getText().toString();
        final String nameToString = name.getText().toString();
        final String lastnameToString = lastname.getText().toString();
        final String usernameToString = username.getText().toString();
        String passwordToString = password.getText().toString();
        String cpasswordToString = cpassword.getText().toString();

        if ((!CheckEmail(sEmailId)))
        {
            Toast.makeText(getApplicationContext(), "Please enter a valid email",Toast.LENGTH_LONG).show();
        } else if (!male.isChecked() && !female.isChecked()) {
            Toast.makeText(getApplicationContext(), "Please select if you're male or female",Toast.LENGTH_LONG).show();
        } else if (nameToString.length() < 1) {
            Toast.makeText(getApplicationContext(), "Name is too short.",Toast.LENGTH_LONG).show();
        } else if (lastnameToString.length() < 1) {
            Toast.makeText(getApplicationContext(), "Last Name is too short.",Toast.LENGTH_LONG).show();
        } else if (usernameToString.length() < 5) {
            Toast.makeText(getApplicationContext(), "Username is too short.", Toast.LENGTH_LONG).show();
        } else if (passwordToString.length() < 4) {
            Toast.makeText(getApplicationContext(), "Password is too short", Toast.LENGTH_LONG).show();
        } else if (!cpasswordToString.equals(passwordToString)) {
            Toast.makeText(getApplicationContext(), "Password's do not match", Toast.LENGTH_LONG).show();

        } else {
            final ParseUser user2 = new ParseUser();
            user2.setUsername(usernameToString);
            user2.setPassword(passwordToString);
            user2.setEmail(sEmailId);
            user2.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(com.parse.ParseException e) {
                    if (e == null) {
                        user2.put("FirstName", nameToString);
                        user2.put("LastName", lastnameToString);
                        if (male.isChecked()) {
                            user2.put("Gender", "Male");
                        } else {
                            user2.put("Gender", "Female");
                        }
                        user2.put("BirthdayYear", year.getSelectedItem().toString());
                        user2.put("BirthdayMonth", month.getSelectedItem().toString());
                        user2.put("BirthdayDay", day.getSelectedItem().toString());
                        user2.put("exercise1", "Exercise 1");
                        user2.put("exercise2", "Exercise 2");
                        user2.put("exercise3", "Exercise 3");
                        user2.put("exerciseNum1", "0");
                        user2.put("exerciseNum2", "0");
                        user2.put("exerciseNum3", "0");
                        user2.put("unitsMeasure", "1");
                        user2.put("unitsWeight", "1");
                        user2.saveInBackground();
                        ParseObject feed = new ParseObject("Feed");
                        feed.put("username", usernameToString);
                        feed.saveInBackground();
                        Toast.makeText(getApplicationContext(), "Account Created!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterMenu.this, MainActivity.class);
                        startActivity(intent);
                        RegisterMenu.this.startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "The username is already taken.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

    private static final Pattern EMAIL_PATTERN = Pattern
            .compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    private boolean CheckEmail(String sEmailId) {

        return EMAIL_PATTERN.matcher(sEmailId).matches();
    }
}
