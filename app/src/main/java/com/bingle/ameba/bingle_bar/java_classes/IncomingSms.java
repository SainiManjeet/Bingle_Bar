package com.bingle.ameba.bingle_bar.java_classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.bingle.ameba.bingle_bar.fragments.ReceiveOtp;
import com.bingle.ameba.bingle_bar.interfaces.SmsListener;

/**
 * Created by ameba on 21/2/18.
 */

public class IncomingSms extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null)
            {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj .length; i++)
                {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[])
                            pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber ;
                    String message = currentMessage .getDisplayMessageBody();
                    try
                    {
                            ReceiveOtp Sms = new ReceiveOtp();
                            //Sms.recivedSms(message );
                    }
                    catch(Exception e){}

                }
            }

        } catch (Exception e)
        {

        }
    }

    public static void bindListener(SmsListener smsListener) {

    }
}
