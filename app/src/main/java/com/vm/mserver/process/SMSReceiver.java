package com.vm.mserver.process;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.vm.mserver.util.Config;

/**
 * Created by VanManh on 04-Dec-17.
 */

public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isRunning = Config.isServerRun(context);
        if (isRunning) {
            final Bundle bundle = intent.getExtras();
            try {
                if (bundle != null) {
                    final Object[] pdusObj = (Object[]) bundle.get("pdus");
                    for (int i = 0; i < pdusObj.length; i++) {
                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                        String message = currentMessage.getDisplayMessageBody();

                        Intent intent1 = new Intent(context, SMSService.class);
                        intent1.putExtra("phoneNum", phoneNumber);
                        intent1.putExtra("mess", message);
                        context.startService(intent1);
                    }
                }
            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" + e);
            }
        } else {
            Toast.makeText(context, "Server đã tắt!", Toast.LENGTH_SHORT).show();
        }
    }
}
