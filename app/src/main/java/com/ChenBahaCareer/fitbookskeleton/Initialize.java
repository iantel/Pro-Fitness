package com.ChenBahaCareer.fitbookskeleton;

/**
 * Created by IK on 30/08/2015.
 */
import android.app.Application;

import com.parse.Parse;

public class Initialize extends Application {

    public void onCreate(){
        Parse.initialize(this, "ZlzULTgrXQC0lyYZLb8kX7fgz2TQU2YDRsjGkQsg", "NT6sZm7jhwdSfYffKbFWBe4GeRT0VyB36sxHHL6L");
    }

}
