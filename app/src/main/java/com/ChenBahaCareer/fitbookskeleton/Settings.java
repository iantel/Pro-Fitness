package com.ChenBahaCareer.fitbookskeleton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.regex.Pattern;

/**
 * Created by Darren on 2015-08-02.
 */
public class Settings extends Activity implements AdapterView.OnItemClickListener,Runnable{

    EditText name, lastname, email, current_password, new_password, confirm_password;
    Spinner year, month, day;
    String currentPassword;

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_settings);
        LoadId();
        LoadInformation();
    }

    public void LoadId() {
        name = (EditText) findViewById(R.id.a_name);
        lastname = (EditText) findViewById(R.id.a_lastname);
        email = (EditText) findViewById(R.id.a_email);
        current_password = (EditText) findViewById(R.id.a_currentpassword);
        new_password = (EditText) findViewById(R.id.a_newpassword);
        confirm_password = (EditText) findViewById(R.id.a_confirmpassword);
        year = (Spinner) findViewById(R.id.a_year);
        month = (Spinner) findViewById(R.id.a_month);
        day = (Spinner) findViewById(R.id.a_date);
    }

    public void LoadInformation() {
   final ParseUser user = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.getInBackground(user.getObjectId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
            if (e == null) {
            String Name = object.getString("FirstName");
            String LastName = object.getString("LastName");
            String Email = object.getString("email");
            currentPassword = object.getString("password");
            String Year = object.getString("BirthdayYear");
            String Day = object.getString("BirthdayDay");
            String Month = object.getString("BirthdayMonth");
            name.setText(Name);
            lastname.setText(LastName);
            email.setText(Email);
            year.setSelection(getIndex(year, Year));
            month.setSelection(getIndex(month, Month));
            day.setSelection(getIndex(day, Day));
            } else {

            }
        }
        });
    }

    public void SaveInformation(final View g) {
        final Intent mainmenu = new Intent(this, MainMenu.class);
        if (current_password.length() > 0) { //If they enter a new password
            ParseUser.logInInBackground(ParseUser.getCurrentUser().getUsername(), current_password.getText().toString(), new LogInCallback() {
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        // Current password worked
                        if (new_password.getText().toString().equals(confirm_password.getText().toString()) || confirm_password.getText().toString().equals(new_password.getText().toString())) {
                            ParseUser user2 = ParseUser.getCurrentUser();
                            user2.put("FirstName", name.getText().toString());
                            user2.put("LastName", lastname.getText().toString());
                            user2.put("email", email.getText().toString());
                            user2.put("BirthdayYear", year.getSelectedItem().toString());
                            user2.put("BirthdayDay", day.getSelectedItem().toString());
                            user2.put("BirthdayMonth", month.getSelectedItem().toString());
                            user2.setPassword(new_password.getText().toString());
                            user2.saveInBackground();
                            Toast.makeText(getApplicationContext(), "Changes have been saved.", Toast.LENGTH_LONG).show();
                            startActivity(mainmenu);
                        } else {
                            Toast.makeText(getApplicationContext(), "The new passwords don't match.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Current password does not match this account.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else if (current_password.length() < 1 && new_password.length() < 1 && confirm_password.length() < 1) {
            if (name.length() < 1 || lastname.length() < 1) {
                Toast.makeText(getApplicationContext(), "Please enter a name.", Toast.LENGTH_LONG).show();
            } else if (lastname.length() < 1) {
                Toast.makeText(getApplicationContext(), "Please enter a last name", Toast.LENGTH_LONG).show();
            } else if (!CheckEmail(email.getText().toString()) || email.length() < 1) {
                Toast.makeText(getApplicationContext(), "Please enter a valid E-mail address.", Toast.LENGTH_LONG).show();
            } else {
                ParseUser user = ParseUser.getCurrentUser();
                user.put("FirstName", name.getText().toString());
                user.put("LastName", lastname.getText().toString());
                user.put("email", email.getText().toString());
                user.put("BirthdayYear", year.getSelectedItem().toString());
                user.put("BirthdayDay", day.getSelectedItem().toString());
                user.put("BirthdayMonth", month.getSelectedItem().toString());
                user.saveInBackground();
                startActivity(mainmenu);
                Toast.makeText(getApplicationContext(), "Changes have been saved.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private static final Pattern EMAIL_PATTERN = Pattern
            .compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    private boolean CheckEmail(String sEmailId) {

        return EMAIL_PATTERN.matcher(sEmailId).matches();
    }

    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void run() {

    }
}
