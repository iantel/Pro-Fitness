package com.ChenBahaCareer.fitbookskeleton;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class physique_page extends ActionBarActivity {

    static String currentUser = ParseUser.getCurrentUser().getObjectId();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Parse.initialize(this, "ZlzULTgrXQC0lyYZLb8kX7fgz2TQU2YDRsjGkQsg", "NT6sZm7jhwdSfYffKbFWBe4GeRT0VyB36sxHHL6L");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject user2, ParseException e) {
                if (e == null) {
                    String gender = user2.getString("Gender");
                    if (gender.equals("Male")) {
                        setContentView(R.layout.profile_page_2);
                    } else if (gender.equals("Female")) {
                        setContentView(R.layout.profile_page_2_female);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Connection Error : Please reload this page.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //Dialogs

    public void ArmDialog(View g) {
        LayoutInflater inflater = physique_page.this.getLayoutInflater();
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setView(inflater.inflate(R.layout.arm_dialog, null));
        ParseQuery getCurrent = ParseQuery.getQuery("_User");
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //set data
                //save data
                ParseQuery query = ParseQuery.getQuery("_User");
                query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject armUpload, ParseException e) {
                        if (e == null) {
                            String armSize = ((EditText)alertDialog.findViewById(R.id.armEdit)).getText().toString();;
                            System.out.println(armSize);
                            armUpload.put("Arm", armSize);
                            if (armSize.trim().length() == 0) {
                                Toast.makeText(getApplicationContext(), "No Measurements Saved", Toast.LENGTH_SHORT).show();
                                alertDialog.cancel();
                            }
                            else {
                                armUpload.saveInBackground();
                                Toast.makeText(getApplicationContext(), "Measurement Saved", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                Toast.makeText(getApplicationContext(), "Measurement Saved", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();
        Button save = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        save.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.buttongeneric));
        save.setTextColor(Color.parseColor("#c40000"));
    }

    public void ChestDialog(View g) {
        LayoutInflater inflater = physique_page.this.getLayoutInflater();
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setView(inflater.inflate(R.layout.chest_dialog, null));
        ParseQuery getCurrent = ParseQuery.getQuery("_User");
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //set data
                //save data
                ParseQuery query = ParseQuery.getQuery("_User");
                query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject chestUpload, ParseException e) {
                        if (e == null) {
                            String chestSize = ((EditText)alertDialog.findViewById(R.id.chestEdit)).getText().toString();;
                            chestUpload.put("Chest", chestSize);
                            if (chestSize.trim().length() == 0) {
                                Toast.makeText(getApplicationContext(), "No Measurements Saved", Toast.LENGTH_SHORT).show();
                                alertDialog.cancel();
                            }
                            else {
                                chestUpload.saveInBackground();
                                Toast.makeText(getApplicationContext(), "Measurement Saved", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                Toast.makeText(getApplicationContext(), "Measurement Saved", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();
        Button save = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        save.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.buttongeneric));
        save.setTextColor(Color.parseColor("#c40000"));
    }

    public void WaistDialog(View g) {
        LayoutInflater inflater = physique_page.this.getLayoutInflater();
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setView(inflater.inflate(R.layout.waist_dialog, null));
        ParseQuery getCurrent = ParseQuery.getQuery("_User");
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //set data
                //save data
                ParseQuery query = ParseQuery.getQuery("_User");
                query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject waistUpload, ParseException e) {
                        if (e == null) {
                            String waistSize = ((EditText)alertDialog.findViewById(R.id.waistEdit)).getText().toString();;
                            waistUpload.put("Waist", waistSize);
                            if (waistSize.trim().length() == 0) {
                                Toast.makeText(getApplicationContext(), "No Measurements Saved", Toast.LENGTH_SHORT).show();
                                alertDialog.cancel();
                            }
                            else {
                                waistUpload.saveInBackground();
                                Toast.makeText(getApplicationContext(), "Measurement Saved", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                Toast.makeText(getApplicationContext(), "Measurement Saved", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();
        Button save = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        save.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.buttongeneric));
        save.setTextColor(Color.parseColor("#c40000"));
    }

    public void QuadDialog(View g) {
        LayoutInflater inflater = physique_page.this.getLayoutInflater();
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setView(inflater.inflate(R.layout.quad_dialog, null));
        ParseQuery getCurrent = ParseQuery.getQuery("_User");
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //set data
                //save data
                ParseQuery query = ParseQuery.getQuery("_User");
                query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject quadUpload, ParseException e) {
                        if (e == null) {
                            String quadSize = ((EditText) alertDialog.findViewById(R.id.quadEdit)).getText().toString();
                            ;
                            quadUpload.put("Quad", quadSize);
                            if (quadSize.trim().length() == 0) {
                                Toast.makeText(getApplicationContext(), "No Measurements Saved", Toast.LENGTH_SHORT).show();
                                alertDialog.cancel();
                            } else {
                                quadUpload.saveInBackground();
                                Toast.makeText(getApplicationContext(), "Measurement Saved", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
        alertDialog.show();
        Button save = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        save.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.buttongeneric));
        save.setTextColor(Color.parseColor("#c40000"));
    }

    public void CalfDialog(View g) {
        LayoutInflater inflater = physique_page.this.getLayoutInflater();
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setView(inflater.inflate(R.layout.calf_dialog, null));
        ParseQuery getCurrent = ParseQuery.getQuery("_User");
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //set data
                //save data
                ParseQuery query = ParseQuery.getQuery("_User");
                query.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject calfUpload, ParseException e) {
                        if (e == null) {
                            String calfSize = ((EditText)alertDialog.findViewById(R.id.editCalf)).getText().toString();;
                            calfUpload.put("Calf", calfSize);
                            if (calfSize.trim().length() == 0) {
                                Toast.makeText(getApplicationContext(), "No Measurements Saved", Toast.LENGTH_SHORT).show();
                                alertDialog.cancel();
                            }
                            else{
                                calfUpload.saveInBackground();
                                Toast.makeText(getApplicationContext(), "Measurement Saved", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                Toast.makeText(getApplicationContext(), "Measurement Saved", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();
        Button save = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        save.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.buttongeneric));
        save.setTextColor(Color.parseColor("#c40000"));
    }
}
