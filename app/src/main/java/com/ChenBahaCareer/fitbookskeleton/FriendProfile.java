package com.ChenBahaCareer.fitbookskeleton;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *Code created by Pro-Fit team
 *Copyrighted and ownership of OTG.
 *Code Name : FriendProfile
 *Function : When you click someone else's profile
 */
public class FriendProfile extends ActionBarActivity {
    static ArrayList<String> currentFriends = new ArrayList<String>();
    TextView profilename, viewText1, viewText2, viewText3, viewNum1, viewNum2, viewNum3;
    Button addOrRemove;
    int status;
    String id, profileNameRC, current_user, goo;
    static String globalUsername=null;
    //ParseUser test = ParseUser.getCurrentUser();

    //A lot of the loading will be done when the page is created and not after
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        if (isOnline() == true) {
            Parse.initialize(this, "ZlzULTgrXQC0lyYZLb8kX7fgz2TQU2YDRsjGkQsg", "NT6sZm7jhwdSfYffKbFWBe4GeRT0VyB36sxHHL6L");
            setContentView(R.layout.viewprofiles);
            LoadInformation(); //Loads profile information
            LoadRelationship(); //Load relationship
            sendFriendRequest(); //Add or Remove function
            viewText1 = (TextView) findViewById(R.id.viewExercise1);
            viewText2 = (TextView) findViewById(R.id.viewExercise2);
            viewText3 = (TextView) findViewById(R.id.viewExercise3);
            viewNum1 = (TextView) findViewById(R.id.viewExerciseNum1);
            viewNum2 = (TextView) findViewById(R.id.viewExerciseNum2);
            viewNum3 = (TextView) findViewById(R.id.viewExerciseNum3);
            LoadPic();
            current_user = ParseUser.getCurrentUser().getUsername();
            goo = current_user +  " wants to be your friend.";
        } else {
            Toast.makeText(getApplicationContext(), "You are not connected to the internet.", Toast.LENGTH_LONG).show();
        }
    }

    public void LoadRelationship() {
        Bundle extras = getIntent().getExtras();
        if (globalUsername!=null) {
            profileNameRC =  globalUsername;
        }
        else{
            extras = getIntent().getExtras();
            profileNameRC = extras.getString("ProfileName");
        }
        System.out.println("ProfileNameRc : " + profileNameRC);
        addOrRemove = (Button) findViewById(R.id.addORremove);
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Friendship");
        query.whereEqualTo("toUser", profileNameRC);
        query.whereEqualTo("fromUser", currentUser.getUsername());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {
                    LoadRelationship2(profileNameRC);
                } else {
                    String relationship = parseObject.getString("status");
                    if (relationship.equals("Requested")) {
                        status = 1;
                        addOrRemove.setText("Pending");
                    } else if (relationship.equals("Mutual")) {
                        status = 2;
                        addOrRemove.setText("Remove");

                    }
                }
            }
        });
      //  globalUsername=null;
    }

    public void LoadRelationship2(String profileName) {
        String profileNameRC = profileName;
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Friendship");
        query.whereEqualTo("fromUser", profileNameRC);
        query.whereEqualTo("toUser", currentUser.getUsername());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {
                    status = 3;
                    addOrRemove.setText("Add Friend");
                } else {
                    String relationship = parseObject.getString("status");
                    if (relationship.equals("Requested")) {
                        status = 4;
                        Toast.makeText(getApplicationContext(), "This user added you to their friends list", Toast.LENGTH_LONG).show();
                        addOrRemove.setText("Accept as a Friend");
                        id = parseObject.getObjectId().toString();
                    } else if (relationship.equals("Mutual")) {
                        status = 2;
                        addOrRemove.setText("Remove");

                    }
                }
            }
        });
    }

    public void sendFriendRequest() {
        Bundle extras = getIntent().getExtras();
        addOrRemove = (Button) findViewById(R.id.addORremove);
        addOrRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status == 1) { //Pending
                    Toast.makeText(getApplicationContext(), "A friend request has already been sent to this user", Toast.LENGTH_LONG).show();
                } else if (status == 3) { //Not friends
                    ParseUser currentUser = ParseUser. getCurrentUser();
                    ParseObject friendshipPrototype = new ParseObject("Friendship");
                    friendshipPrototype.put("fromUser", currentUser.getUsername());
                    friendshipPrototype.put("toUser", profileNameRC);
                    friendshipPrototype.put("status", "Requested");
                    friendshipPrototype.saveInBackground();
                    Toast.makeText(getApplicationContext(), "Friend request sent", Toast.LENGTH_LONG).show();
                    RequestFeed();
                    addOrRemove.setText("Pending");
                    currentFriends.add(profileNameRC);
                    //push notifications//

                    ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
                    query.whereEqualTo("username", profileNameRC);
                    ParsePush push = new ParsePush();
                    push.setQuery(query);
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject();
                        obj.put("alert", ParseUser.getCurrentUser().getUsername() + " wants to be your friend");
                        obj.put("action", "com.ChenBahaCareer.fitbookskeleton.UPDATE_STATUS");
                        obj.put("FriendProfile", ParseUser.getCurrentUser().getUsername());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //push.setMessage(ParseUser.getCurrentUser().getUsername().toString() + " has added you to their friends list.");
                    push.setData(obj);
                    push.sendInBackground();
                    status = 1;
                    //search.whereEqualTo("username", )
                } else if (status == 2) { //Friends
                    Toast.makeText(getApplicationContext(), "Unfriended " + profileNameRC, Toast.LENGTH_LONG).show();
                    removeFriend();
                    addOrRemove.setText("Add Friend");
                } else if (status == 4) { //Accept
                    acceptFriend();
                    ParseQuery<ParseInstallation> query = ParseInstallation.getQuery();
                    query.whereEqualTo("username", profileNameRC);
                    ParsePush push = new ParsePush();
                    push.setQuery(query);
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject();
                        obj.put("alert", ParseUser.getCurrentUser().getUsername() + " has accepted your friend request");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //push.setMessage(ParseUser.getCurrentUser().getUsername().toString() + " has added you to their friends list.");
                    ParseQuery query2 = ParseQuery.getQuery("Feed");
                    query2.whereEqualTo("username", profileNameRC);
                    query2.getFirstInBackground( new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException g) {
                            if (g == null) {
                                String goo2 = current_user + " has accepted your friend request";
                                object.add("feed", goo2);
                                object.saveInBackground();
                            } else {

                            }
                        }
                    });
                }
            }
        });
    }

    public void acceptFriend() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Friendship");
        query.getInBackground(id, new GetCallback<ParseObject>() {
            public void done(ParseObject gameScore, ParseException e) {
                if (e == null) {
                    gameScore.put("status", "Mutual");
                    gameScore.saveInBackground();
                    Toast.makeText(getApplicationContext(), "Accepted friend request.", Toast.LENGTH_LONG).show();
                }
            }
        });
        addOrRemove.setText("Remove Friend");
        status = 2;
    }


    public void LoadPic() {
        Bundle extras;
        String friendname;
        final ImageView pic = (ImageView) findViewById(R.id.profileview2);
        if (globalUsername != null) {
        friendname = globalUsername;
        }
        else{
            System.out.println ("LoadPic getting extras: ");
            extras = getIntent().getExtras();
            friendname = extras.getString("ProfileName");
        }
        ParseQuery query = ParseQuery.getQuery("_User");
            query.whereEqualTo("username",friendname);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    try {
                        ParseFile dled = (ParseFile) parseObject.get("ProfilePic");
                        byte[] file = dled.getData();
                        Bitmap photo = BitmapFactory.decodeByteArray(file, 0, file.length);
                        //pic.setImageBitmap(Bitmap.createScaledBitmap(photo, 130, 130, true));
                        pic.setImageBitmap(photo);
                    }catch(ParseException f){
                        System.out.println("test");
                    }catch (NullPointerException n) {
                        Toast.makeText(FriendProfile.this, "No Profile picture uploaded", Toast.LENGTH_SHORT).show();
                   }
                }
            });
        globalUsername=null;
    }


    public void removeFriend() {
        Bundle extras = getIntent().getExtras();
        addOrRemove = (Button) findViewById(R.id.addORremove);
        final String profileNameRC = extras.getString("ProfileName");
        final String profileID = extras.getString("ProfileID");
        final ParseUser currentUser = ParseUser.getCurrentUser();
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Friendship");
        query.whereEqualTo("toUser", profileNameRC);
        query.whereEqualTo("fromUser", currentUser.getUsername());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {
                    removeFriend2();
                    Toast.makeText(getApplicationContext(), "Load relationship 2", Toast.LENGTH_LONG).show();
                } else {
                    String relationship = parseObject.getString("status");
                   if (relationship.equals("Mutual")) {
                        status = 3;
                        addOrRemove.setText("Add Friend");
                        parseObject.deleteEventually();
                        Toast.makeText(getApplicationContext(), "Removed row", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public void removeFriend2() {
        Bundle extras = getIntent().getExtras();
        addOrRemove = (Button) findViewById(R.id.addORremove);
        final String profileNameRC = extras.getString("ProfileName");
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Friendship");
        query.whereEqualTo("toUser", currentUser.getUsername());
        query.whereEqualTo("fromUser", profileNameRC);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (parseObject == null) {
                    Toast.makeText(getApplicationContext(), "Not possible", Toast.LENGTH_LONG).show();
                } else {
                    String relationship = parseObject.getString("status");
                    if (relationship.equals("Mutual")) {
                        status = 3;
                        addOrRemove.setText("Add Friend");
                        parseObject.deleteEventually();
                        Toast.makeText(getApplicationContext(), "Removed row", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public void openTrainingFriend(View g) {
        Intent intent = new Intent(this, WorkoutFriend.class);
        intent.putExtra("username", profileNameRC);
        startActivity(intent);
    }

    public void LoadInformation() {
        Bundle extras = null;
        if (globalUsername!=null) {
            profileNameRC =  globalUsername;
        } else {
            extras = getIntent().getExtras();
            profileNameRC = extras.getString("ProfileName");
        }
        System.out.println(profileNameRC + "LoadInformation");
        profilename = (TextView) findViewById(R.id.loadprofilename);

            ParseQuery query = new ParseQuery("_User");
            query.whereEqualTo("username", profileNameRC);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if (parseObject == null) {
                        System.out.println("The getFirst request failed.");
                    } else {
                       String FirstName =  parseObject.getString("FirstName");
                       String LastName = parseObject.getString("LastName");
                       profilename.setText(FirstName + " " + LastName);
                       String exercise1 = parseObject.getString("exercise1");
                       String exercise2 = parseObject.getString("exercise2");
                       String exercise3 = parseObject.getString("exercise3");
                       String exerciseNum1 = parseObject.getString("exerciseNum1");
                       String exerciseNum2 = parseObject.getString("exerciseNum2");
                       String exerciseNum3 = parseObject.getString("exerciseNum3");
                       String unitsWeight = parseObject.getString("unitsWeight");
                        if (unitsWeight.equals("1")) { //pounds
                            viewNum1.setText(exerciseNum1 + "lb");
                            viewNum2.setText(exerciseNum2 + "lb");
                            viewNum3.setText(exerciseNum3 + "lb");
                            viewText1.setText(exercise1);
                            viewText2.setText(exercise2);
                            viewText3.setText(exercise3);
                        } else { //kilo
                            viewNum1.setText(exerciseNum1 + "kg");
                            viewNum2.setText(exerciseNum2 + "kg");
                            viewNum3.setText(exerciseNum3 + "kg");
                            viewText1.setText(exercise1);
                            viewText2.setText(exercise2);
                            viewText3.setText(exercise3);
                        }
                    }
                }
            });
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

    public void RequestFeed() {
        System.out.println("Running RequestFeed method...");
        Bundle extras = getIntent().getExtras();
        final String profileID = extras.getString("ProfileID");
        ParseQuery query2 = ParseQuery.getQuery("Feed");
        query2.whereEqualTo("username", profileNameRC);
        query2.getFirstInBackground( new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException g) {
                if (g == null) {
                    object.add("feed", goo);
                    String user = object.getString("username");
                    object.saveInBackground();
                } else {

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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


}
