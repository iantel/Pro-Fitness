package com.ChenBahaCareer.fitbookskeleton;

import android.content.Context;
import android.content.Intent;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 *Code created by Pro-Fit team
 *Copyrighted and ownership of OTG.
 *Code Name : FriendProfileBroadcastReceiver
 *Function : Receives Push notifications
 */
public class FriendProfileBroadcastReceiver extends ParsePushBroadcastReceiver {
    private static final String TAG = "";


    @Override
    public void onPushOpen(Context context, Intent intent) {

        try {
            if (intent == null) {
                System.out.println("Receiver intent null");
            } else {
                String action = intent.getAction();
                System.out.println("got action " + action);
                    String channel = intent.getExtras().getString("com.parse.Channel");
                    JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                    System.out.println (json);
                    System.out.println("got action " + action + " on channel " + channel + " with:");
                    Iterator itr = json.keys();
                    while (itr.hasNext()) {
                        String key = (String) itr.next();
                        if (key.equals("FriendProfile")) {
                            FriendProfile.globalUsername = json.get("FriendProfile").toString();
                            System.out.println(json.get("FriendProfile").toString() + "Broadcast Receiver method");
                            Intent pupInt = new Intent(context, FriendProfile.class);
                            pupInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.getApplicationContext().startActivity(pupInt);
                        }
                        if (key.equals("Workouts")){
                            System.out.println("FriendProfileBroadcastReceiver");
                            WorkoutsNoEdits.globalWorkoutsUser = json.get("Workouts").toString();
                            System.out.println(json.get("Workouts").toString() + " Broadcast Receiver method");
                            Intent pupInt = new Intent(context, WorkoutsNoEdits.class);
                            pupInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.getApplicationContext().startActivity(pupInt);
                        }
                        System.out.println("..." + key + " => " + json.getString(key));
                    }
            }

        } catch (JSONException e) {
            System.out.println("JSONException: " + e.getMessage());
        }
    }
}