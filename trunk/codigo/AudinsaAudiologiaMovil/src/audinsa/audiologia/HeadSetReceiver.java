package audinsa.audiologia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class HeadSetReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);
            switch (state) {
            case 0:
            	intent.putExtra("headsetPluggedIn", false);
                break;
            case 1:
            	intent.putExtra("headsetPluggedIn", true);
                break;
            }
        }
    }
}
