package com.ChenBahaCareer.fitbookskeleton;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;

import java.util.ArrayList;
import java.util.List;

/**
 *Code created by Pro-Fit team
 *Copyrighted and ownership of OTG corp.
 *Code Name : MainActivity
 *Function : When you click the app and login. This file should be renamed to login page.
 */

public class MainActivity extends ActionBarActivity implements OnItemClickListener{
	Button profileButton;
	EditText userInput, passInput;
	RelativeLayout loginlayout, img;
	TextView loginfailed, loginButton;
    ImageView imageView;
    ListView work;
    ListView friends;
    ArrayList<String> workouts = new ArrayList<String>();
    ArrayList<String> people = new ArrayList<String>();
    private static final long GET_DATA_INTERVAL = 5000;
    int images[] = {R.drawable.bg1,R.drawable.bg2};
    int index = 0;
    int currentlayout;
    Handler hand = new Handler();
    static boolean onLogin = false;
    String url = "https://www.iubenda.com/privacy-policy/284354/full-legal";
    WebView tos;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (RelativeLayout) findViewById(R.id.loginlayout);
        hand.postDelayed(run, GET_DATA_INTERVAL);
        if (isOnline() == true) {
            Parse.initialize(this, "ZlzULTgrXQC0lyYZLb8kX7fgz2TQU2YDRsjGkQsg", "NT6sZm7jhwdSfYffKbFWBe4GeRT0VyB36sxHHL6L");
            PushService.setDefaultPushCallback(this, MainActivity.class);
            ParseInstallation.getCurrentInstallation().saveInBackground();
            currentlayout = R.id.loginlayout;
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

    Runnable run = new Runnable() {
        @Override
        public void run() {

            img.setBackgroundDrawable(getResources().getDrawable(images[index++]));
            if (index == images.length)
                index = 0;
         //   hand.postDelayed(run, GET_DATA_INTERVAL);
        }
    };

    public void Login(View g) {
        final Intent mainmenu = new Intent(this, MainMenu.class);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        final Animation a = AnimationUtils.loadAnimation(this, R.anim.abc_slide_out_top);
        a.reset();

        //login variable to tell if user is logged in
        loginButton = (TextView) findViewById(R.id.loginbutton); //This line links XML and java together
        profileButton = (Button) findViewById(R.id.profileButton);
        userInput = (EditText) findViewById(R.id.registerName);
        passInput = (EditText) findViewById(R.id.passwordInput);
        loginlayout = (RelativeLayout) findViewById(R.id.loginpage);
        loginfailed = (TextView) findViewById(R.id.loginfailed);
        imm.hideSoftInputFromWindow(passInput.getWindowToken(), 0);

        final String userInputToString = userInput.getText().toString();
                final String passInputToString = passInput.getText().toString();

                if (isOnline() == true) {
                    ParseUser.logInInBackground(userInputToString, passInputToString, new LogInCallback() {

                        @Override
                        public void done(ParseUser parseUser, com.parse.ParseException e) {
                            if (parseUser != null) {
                                loginfailed.setVisibility(View.GONE);
                                ParseInstallation.getCurrentInstallation().saveInBackground();
                                ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                                installation.put("username", ParseUser.getCurrentUser());
                                //TODO
                                ParseQuery query = ParseInstallation.getQuery();
                                //    query.whereEqualTo("objectId",ParseUser.getCurrentUser().get("objectId"));
                                System.out.println ("1: User Object ID: "+ installation.getInstallationId());
                                System.out.println ("2: "+ ParseUser.getCurrentUser().getObjectId());
                                System.out.println ("3: " + installation.getObjectId());
                                query.whereEqualTo("objectId", installation.getObjectId());
                                /*
                                query.getInBackground(installation.getObjectId(), new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject parseObject, ParseException e) {
                                        if (e == null) {
                                            System.out.println("Successful");
                                            parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                                            parseObject.saveInBackground();
                                        } else {
                                            System.out.println("Could not find ID");
                                            e.printStackTrace();
                                        }
                                        ;
                                    }
                                });
                                */
                                query.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> parseObjects, ParseException e) {
                                        if (e == null&&parseObjects.size()>0) {
                                            System.out.println("Successful");
                                            parseObjects.get(0).put("username", ParseUser.getCurrentUser().getUsername());
                                            parseObjects.get(0).saveInBackground();
                                        } else {
                                            System.out.println("Could not find ID");
                                        }
                                    }
                                });
                                installation.saveInBackground();
                                onLogin = false;
                                startActivity(mainmenu);
                            } else {
                                loginfailed.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                } else {
                Toast.makeText(getApplicationContext(), "You are not connected to the internet.", Toast.LENGTH_LONG).show();
        }
 }

    public void openContact(View v){
        Intent contactpage = new Intent(this,SearchFriends.class);
        startActivity(contactpage);
    }

    public void openTraining (View v){
        Intent workouts = new Intent(this,Workouts.class);
        startActivity(workouts);
    }

    public void loadPic(){
        imageView = (ImageView) findViewById(R.id.profilepicmain);
        ParseQuery query = ParseQuery.getQuery("_User");
        try {
            ParseObject fileholder = query.get(ParseUser.getCurrentUser().getObjectId());
            ParseFile dled = (ParseFile) fileholder.get("ProfilePic");
            byte [] file = dled.getData();
            Bitmap photo = BitmapFactory.decodeByteArray(file, 0, file.length);
            imageView.setImageBitmap(Bitmap.createScaledBitmap(photo,160,150,true));
        } catch (ParseException g){
            Toast.makeText(this, "Welcome to profit.",Toast.LENGTH_SHORT).show();
        } catch (NullPointerException n) {
            Toast.makeText(this, "Welcome to profit.", Toast.LENGTH_SHORT).show();
        } catch (ClassCastException z) {
            Toast.makeText(this, "Welcome to profit.", Toast.LENGTH_SHORT).show();
        }

    }

    //These are button functions
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
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
            adapter.remove(adapter.getItem(position));
            adapter.notifyDataSetChanged();
        }
        else if (item.getTitle() == "Share"){
            setContentView(R.layout.addfriends);
            friends = (ListView) findViewById(R.id.friendView);
            ArrayAdapter fdptr = new ArrayAdapter(this,android.R.layout.simple_list_item_1, people);
            friends.setAdapter(fdptr);
        }
        return true;
    }
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent table = new Intent(this,TrainingActivity.class);
        startActivity(table);
    }

    public void OpenLogin(View g) {
        onLogin = true;
        setContentView(R.layout.login);
        loginfailed = (TextView) findViewById(R.id.loginfailed);
        loginfailed.setVisibility(View.GONE);
    }

//Register

    public void RegisterMenu(final View g) {
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setView(inflater.inflate(R.layout.tos, null));
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Agree", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StartRegisterMenu(g);
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Disagree", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
        tos = (WebView) alertDialog.findViewById(R.id.tos);
        tos.getSettings().setJavaScriptEnabled(true);
        tos.loadUrl(url);
    }

    public void StartRegisterMenu(View g) {
        Intent intent = new Intent(this, RegisterMenu.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && onLogin) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keyCode, event);
    }

    // Alternative variant for API 5 and higher
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}