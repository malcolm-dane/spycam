package stupuid.maga.legacy;

import android.app.Application;
import com.parse.Parse;
import com.parse.Parse.Configuration.Builder;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class Legacy extends Application {
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(new Builder(getApplicationContext()).applicationId("traintrackapp").clientKey("bcdeafg").server("https://traintrackapp.herokuapp.com/parse/").build());
        ParseUser.enableAutomaticUser();
        ParseACL.setDefaultACL(new ParseACL(), true);
    }
}
