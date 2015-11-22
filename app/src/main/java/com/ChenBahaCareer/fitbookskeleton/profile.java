package com.ChenBahaCareer.fitbookskeleton;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *Code created by Pro-Fit team
 *Copyrighted and ownership of OTG corp.
 *Code Name : Profile
 *Function : Load your own profile and edit information.
 */

public class profile extends ActionBarActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;
    ImageView imageView;
    CheckBox pound, kilo, inches, centimeter;
    TextView profilename, textworkout1, textworkout2, textworkout3, num1, num2, num3;
    Button pbutton;
    EditText texts1, texts2, texts3, nums1, nums2, nums3;
    int FirstNameLetter, LastNameLetter, NameAddition, weight, measurement;
    String ExerciseNum1, ExerciseNum2, ExerciseNum3;
    static ArrayList<String> workoutdata = new ArrayList<String>();
    static ArrayList<String> current_friends = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        Parse.initialize(this, "ZlzULTgrXQC0lyYZLb8kX7fgz2TQU2YDRsjGkQsg", "NT6sZm7jhwdSfYffKbFWBe4GeRT0VyB36sxHHL6L");
        final ParseUser user = ParseUser.getCurrentUser();
        profilename = (TextView) findViewById(R.id.loadprofilename);
        textworkout1 = (TextView) findViewById(R.id.workout1);
        textworkout2 = (TextView) findViewById(R.id.workout2);
        textworkout3 = (TextView) findViewById(R.id.workout3);
        num1 = (TextView) findViewById(R.id.viewOne);
        num2 = (TextView) findViewById(R.id.viewTwo);
        num3 = (TextView) findViewById(R.id.viewThree);
        pbutton = (Button) findViewById(R.id.pbutton);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.getInBackground(user.getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject obj, ParseException e) {
                if (e == null) {
                    //Profile Name
                    String FirstName = obj.getString("FirstName");
                    String LastName = obj.getString("LastName");
                    FirstNameLetter = FirstName.length();
                    LastNameLetter = LastName.length();
                    NameAddition = FirstNameLetter + LastNameLetter;
                    profilename.setText(FirstName + " " + LastName);
                    //Workout Data
                    String Exercise1 = obj.getString("exercise1");
                    String Exercise2 = obj.getString("exercise2");
                    String Exercise3 = obj.getString("exercise3");
                    ExerciseNum1 = obj.getString("exerciseNum1");
                    ExerciseNum2 = obj.getString("exerciseNum2");
                    ExerciseNum3 = obj.getString("exerciseNum3");
                    String UnitWeight = obj.getString("unitsWeight");
                    String UnitMeasurement = obj.getString("unitsMeasure");
                    if (UnitWeight.equals("1")) { //1 = pounds
                        num1.setText(ExerciseNum1 + "lbs");
                        num2.setText(ExerciseNum2 + "lbs");
                        num3.setText(ExerciseNum3 + "lbs");
                        weight = 1;
                    } else { //Must be 2 so Kg
                        num1.setText(ExerciseNum1 + "kg");
                        num2.setText(ExerciseNum2 + "kg");
                        num3.setText(ExerciseNum3 + "kg");
                        weight = 1;
                    }
                    if (UnitMeasurement.equals("1")) {
                        measurement = 1;
                    } else {
                        measurement = 2;
                    }
                    textworkout1.setText(Exercise1);
                    textworkout2.setText(Exercise2);
                    textworkout3.setText(Exercise3);
                } else {
                    profilename.setText("No internet connection");
                }
                loadPic();
            }
        });
        ParseQuery<ParseObject> query3 = ParseQuery.getQuery("Friendship");
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Friendship");
        query3.whereEqualTo("toUser", ParseUser.getCurrentUser().getUsername());
        query2.whereEqualTo("fromUser", ParseUser.getCurrentUser().getUsername());
        query3.whereEqualTo("status", "Mutual");
        query2.whereEqualTo("status", "Mutual");
        query3.findInBackground(new FindCallback<ParseObject>() {
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

    public void loadPic(){
        imageView = (ImageView) findViewById(R.id.profileview1);
        ParseQuery query = ParseQuery.getQuery("_User");
        try {
            ParseObject fileholder = query.get(ParseUser.getCurrentUser().getObjectId());
            ParseFile dled = (ParseFile) fileholder.get("ProfilePic");
            byte [] file = dled.getData();
            Bitmap photo = BitmapFactory.decodeByteArray(file,0,file.length);
            photo = getRoundedCornerBitmap(photo, 12);
            //imageView.setImageBitmap(Bitmap.createScaledBitmap(photo,153,204,true));
            imageView.setImageBitmap(photo);
        }catch (ParseException g){
            System.out.println("Load Pic : Caught parsse exception");
        }catch (NullPointerException n){
            System.out.println("Load Pic : Caught Null Pointer");
        }catch (ClassCastException z) {
            System.out.println("Load Pic : Caught Class");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        profilename = (TextView) findViewById(R.id.loadprofilename);

        getMenuInflater().inflate(R.menu.menu_profile, menu);

        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageView = (ImageView) findViewById(R.id.profileview1);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == CAMERA_REQUEST) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                photo = getRoundedCornerBitmap(photo, 12);
                byte[] pic = stream.toByteArray();
                ParseFile profilepic = new ParseFile(ParseUser.getCurrentUser().getObjectId() + ".jpeg", pic);
                profilepic.saveInBackground();
                ParseUser.getCurrentUser().put("ProfilePic", profilepic);
                ParseUser.getCurrentUser().saveInBackground();
                Toast.makeText(this, "Image Uploaded", Toast.LENGTH_SHORT).show();

                //imageView.setImageBitmap(Bitmap.createScaledBitmap(photo, 153, 153, true));
                imageView.setImageBitmap(photo);
                //Feed//
                ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Feed");
                query2.whereContainedIn("username", current_friends);
                query2.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> parseObjects, ParseException e) {
                        if (e == null) {

                            for (int i = 0; i < parseObjects.size(); i++) {
                                String goo = ParseUser.getCurrentUser().getUsername() + " has updated their profile picture";
                                parseObjects.get(i).add("feed", goo);
                                parseObjects.get(i).saveInBackground();

                            }
                        } else
                            System.out.println("Workout feed failed.");

                    }
                });
            } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                try {
                    Uri selectedImage = data.getData();
                    System.out.println(selectedImage.getPath());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Bitmap libphoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    Bitmap scaled = Bitmap.createScaledBitmap(libphoto, 153, 204, true);
                    scaled = getRoundedCornerBitmap(scaled, 12);
                    libphoto = getRoundedCornerBitmap(libphoto, 12);
                    scaled.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] pic = stream.toByteArray();
                    ParseFile profilepic = new ParseFile(ParseUser.getCurrentUser().getObjectId() + ".jpeg", pic);
                    profilepic.saveInBackground();
                    ParseUser.getCurrentUser().put("ProfilePic", profilepic);
                    ParseUser.getCurrentUser().saveInBackground();
                    Toast.makeText(this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                    imageView.setImageBitmap(libphoto);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setAdjustViewBounds(true);
                    //Feed//
                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Feed");
                    query2.whereContainedIn("username", current_friends);
                    query2.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> parseObjects, ParseException e) {
                            if (e == null) {

                                for (int i = 0; i < parseObjects.size(); i++) {
                                    String goo = ParseUser.getCurrentUser().getUsername() + " has updated their profile picture";
                                    parseObjects.get(i).add("feed", goo);
                                    parseObjects.get(i).saveInBackground();

                                }
                            } else
                                System.out.println("Workout feed failed.");

                        }
                    });
                } catch (FileNotFoundException f) {
                    Toast.makeText(this, "File Not Found", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(this, "Input Error", Toast.LENGTH_SHORT).show();
                }
                ////////////////////////////////////////////////////
            } else {
                setContentView(R.layout.profile);
            }
        }
    }

    public void ProfileSettings(View g) {
        LayoutInflater inflater = profile.this.getLayoutInflater();
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setView(inflater.inflate(R.layout.profilesettings_dialog, null));

        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //On the settings tab
                if (!pound.isChecked() && !kilo.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Please select a unit", Toast.LENGTH_LONG).show();
                } else {
                    final String texts1 = ((EditText) alertDialog.findViewById(R.id.settingsview1)).getText().toString();
                    final String texts2 = ((EditText) alertDialog.findViewById(R.id.settingsview2)).getText().toString();
                    final String texts3 = ((EditText) alertDialog.findViewById(R.id.settingsview3)).getText().toString();
                    final String nums1 = ((EditText) alertDialog.findViewById(R.id.settingsnum1)).getText().toString();
                    final String nums2 = ((EditText) alertDialog.findViewById(R.id.settingsnum2)).getText().toString();
                    final String nums3 = ((EditText) alertDialog.findViewById(R.id.settingsnum3)).getText().toString();
                    textworkout1.setText(texts1);
                    textworkout2.setText(texts2);
                    textworkout3.setText(texts3);
                    ParseUser user = ParseUser.getCurrentUser();
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                    query.getInBackground(user.getObjectId(), new GetCallback<ParseObject>() {
                        public void done(ParseObject obj, ParseException e) {
                            if (e == null) {
                                obj.put("exercise1", texts1);
                                obj.put("exercise2", texts2);
                                obj.put("exercise3", texts3);
                                obj.put("exerciseNum1", nums1);
                                obj.put("exerciseNum2", nums2);
                                obj.put("exerciseNum3", nums3);

                                if (pound.isChecked()) {
                                    obj.put("unitsWeight", "1");
                                    weight = 1;
                                } else {
                                    obj.put("unitsWeight", "2");
                                    weight = 2;
                                }
                               /* if (inches.isChecked()) {
                                    obj.put("unitsMeasure", "1");
                                    measurement = 1;
                                } else {
                                    obj.put("unitsMeasure", "2");
                                    measurement = 2;
                                }*/
                                obj.saveInBackground();
                                if (weight == 1) {
                                    num1.setText(nums1 + "lbs");
                                    num2.setText(nums2 + "lbs");
                                    num3.setText(nums3 + "lbs");
                                } else {
                                    num1.setText(nums1 + "kg");
                                    num2.setText(nums2 + "kg");
                                    num3.setText(nums3 + "kg");
                                }
                                ExerciseNum1 = nums1;
                                ExerciseNum2 = nums2;
                                ExerciseNum3 = nums3;
                                Toast.makeText(getApplicationContext(), "Favourite exercises saved!", Toast.LENGTH_LONG).show();
                                //Feed//
                                ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Feed");
                                query2.whereContainedIn("username", current_friends);
                                query2.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> parseObjects, ParseException e) {
                                        if (e == null) {

                                            for (int i = 0; i < parseObjects.size(); i++) {
                                                String goo = ParseUser.getCurrentUser().getUsername() + " has updated their favorite exercises";
                                                parseObjects.get(i).add("feed", goo);
                                                parseObjects.get(i).saveInBackground();

                                            }
                                        } else
                                            System.out.println("Workout feed failed.");

                                    }
                                });

                            } else {
                                profilename.setText("Error : Workout data not saved :(");
                            }
                        }
                    });
                }
            }
        });
            alertDialog.show();
            Button save = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
            save.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.buttongeneric));
            save.setTextColor(Color.parseColor("#c40000"));
            texts1 = (EditText) alertDialog.findViewById(R.id.settingsview1);
            texts1.setText(textworkout1.getText().toString());
            texts2 = (EditText) alertDialog.findViewById(R.id.settingsview2);
            texts2.setText(textworkout2.getText().toString());
            texts3 = (EditText) alertDialog.findViewById(R.id.settingsview3);
            texts3.setText(textworkout3.getText().toString());
            nums1 = (EditText) alertDialog.findViewById(R.id.settingsnum1);
            nums1.setText(ExerciseNum1);
            nums2 = (EditText) alertDialog.findViewById(R.id.settingsnum2);
            nums2.setText(ExerciseNum2);
            nums3 = (EditText) alertDialog.findViewById(R.id.settingsnum3);
            nums3.setText(ExerciseNum3);
            //
            pound = (CheckBox) alertDialog.findViewById(R.id.checkPound);
            kilo = (CheckBox) alertDialog.findViewById(R.id.checkKilo);
            //inches = (CheckBox) alertDialog.findViewById(R.id.checkInches);
            //centimeter = (CheckBox) alertDialog.findViewById(R.id.checkCentimeter);
            if (weight == 1) {
                pound.setChecked(true);
            } else {
                kilo.setChecked(true);
            }

    }

    public void ClearPounds(View g) {
           pound.setChecked(false);
    }

    public void ClearKilograms(View g) {
        kilo.setChecked(false);
    }

    public void LibraryCalled(MenuItem item) {
        Intent loadIntent = new Intent(
                Intent.ACTION_PICK, (android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
        startActivityForResult(loadIntent, RESULT_LOAD_IMAGE);
    }

    public void CameraCalled(MenuItem item) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    public void openTraining (View v){
        Intent workouts = new Intent(this,Workouts.class);
        startActivity(workouts);
    }
   public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        output.compress(Bitmap.CompressFormat.PNG,50,stream);
        return output;
    }

    public void PopupMenu(View g) {
        PopupMenu menu = new PopupMenu(this, g);
        menu.getMenuInflater().inflate(R.menu.upcameramenu, menu.getMenu());
        menu.show();
    }

    public void PhysiqueClass (View g){
        Intent intent = new Intent(this, physique_page.class);
        startActivity(intent);
    }
}
