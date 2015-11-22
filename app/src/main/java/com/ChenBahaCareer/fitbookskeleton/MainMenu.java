package com.ChenBahaCareer.fitbookskeleton;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Darren on 2015-08-04.
 */
public class MainMenu extends Activity implements AdapterView.OnItemClickListener,Runnable {
    static ArrayList<String> feed = new ArrayList<String>();
    ImageView imageView, frame;
    TextView number;
    int feednum;
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_user_menu);
        loadPic();
        loadNotificationNum();
        frame = (ImageView) findViewById(R.id.notifframe);
        number = (TextView) findViewById(R.id.notifnum);

    }

    public void loadNotificationNum() {
        //System.out.println("Notification Loading...");
        feed.removeAll(feed);
        ParseQuery <ParseObject> query = ParseQuery.getQuery("Feed");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                try {
                    ArrayList temp = (ArrayList<String>) list.get(0).get("feed");
                    Collections.reverse(temp);
                    feed.addAll(temp);
                    feednum = feed.size();
                    System.out.println("Number of Notifications : " + feednum);
                    number.setText("" + feednum);
                } catch (NullPointerException f) {
                    feednum = 0;
                    System.out.println("Number of Notifications : " + feednum);
                    number.setText("0");
                }
            }

        });
    }

    public void onResume() {
        super.onResume();
        System.out.println("On resume called");
        imageView = (ImageView) findViewById(R.id.profilepicmain);
        ParseQuery query = ParseQuery.getQuery("_User");
        try {
            ParseObject fileholder = query.get(ParseUser.getCurrentUser().getObjectId());
            ParseFile dled = (ParseFile) fileholder.get("ProfilePic");
            byte [] file = dled.getData();
            Bitmap photo = BitmapFactory.decodeByteArray(file, 0, file.length);
            photo = getRoundedCornerBitmap(photo, 12);
            //imageView.setImageBitmap(Bitmap.createScaledBitmap(photo,153,204,true));
            imageView.setImageBitmap(photo);
        } catch (ParseException g){
        } catch (NullPointerException n) {
        } catch (ClassCastException z) {
        }
        loadNotificationNum();
    }

    public void loadPic(){
        imageView = (ImageView) findViewById(R.id.profilepicmain);
        ParseQuery query = ParseQuery.getQuery("_User");
        try {
            ParseObject fileholder = query.get(ParseUser.getCurrentUser().getObjectId());
            ParseFile dled = (ParseFile) fileholder.get("ProfilePic");
            byte [] file = dled.getData();
            Bitmap photo = BitmapFactory.decodeByteArray(file, 0, file.length);
            photo = getRoundedCornerBitmap(photo, 12);
            //imageView.setImageBitmap(Bitmap.createScaledBitmap(photo,153,204,true));
            imageView.setImageBitmap(photo);
        } catch (ParseException g){
        } catch (NullPointerException n) {
        } catch (ClassCastException z) {
        }

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

    public void ProfileClass (View g){
        Intent intent = new Intent(this, profile.class);
        startActivity(intent);
    }

    public void settings (View g) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void openContact(View v){
        Intent contactpage = new Intent(this,SearchFriends.class);
        startActivity(contactpage);
    }
    public void openTraining (View v){
        Intent workouts = new Intent(this,Workouts.class);
        startActivity(workouts);
    }

    public void Notifications(View g) {
        Intent intent = new Intent(this, notifications.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keyCode, event);
    }

    // Alternative variant for API 5 and higher
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void run() {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
