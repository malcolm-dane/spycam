package stupuid.maga.legacy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

public class LocationChange extends BroadcastReceiver {
    private static final String TAG = "LocationProviderChanged";
    boolean isGpsEnabled;
    boolean isNetworkEnabled;

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
            Intent i;
            Log.i(TAG, "Location Providers changed");
            LocationManager locationManager = (LocationManager) context.getSystemService("location");
            this.isGpsEnabled = locationManager.isProviderEnabled("gps");
            this.isNetworkEnabled = locationManager.isProviderEnabled("network");
            if (this.isGpsEnabled || this.isNetworkEnabled) {
                i = new Intent(context, LocationChangeTracker.class);
                i.putExtra("direction", "turned on");
                context.startService(i);
            }
            if (!this.isGpsEnabled || !this.isNetworkEnabled) {
                i = new Intent(context, LocationChangeTracker.class);
                i.putExtra("direction", "turned off");
                context.startService(i);
            }
        }
    }
}
