package com.bingle.ameba.bingle_bar.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.bingle.ameba.bingle_bar.interfaces.SmsListener;

/**
 * Created by ErAshwini on 22/3/18.
 */

public class SMSBReceiver extends BroadcastReceiver
    {

        private static SmsListener mlisListener;
        @Override
        public void onReceive(Context context, Intent intent)
        {

            Log.e("onReceive","onReceive"+intent);
            // Get Bundle object contained in the SMS intent passed in
            Bundle bundle = intent.getExtras();
            SmsMessage[] smsm = null;
            String sms_str ="";
            if (bundle != null)
            {
                Log.e("bundle","bundle"+bundle);
                // Get the SMS message
                Object[] pdus = (Object[]) bundle.get("pdus");
                smsm = new SmsMessage[pdus.length];
                for (int i=0; i<smsm.length; i++){
                    smsm[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                    Log.e("ms===","=="+i);
                   /* sms_str += "Sent From: " + smsm[i].getOriginatingAddress();
                    sms_str += "\r\nMessage: ";*/
                    sms_str += smsm[i].getMessageBody().toString();
                    sms_str+= "\r\n";
                }
                // Start Application's  MainActivty activity
              /*  Intent smsIntent=new Intent(context,MainActivity.class);
                smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                smsIntent.putExtra("sms_str", sms_str);
                context.startActivity(smsIntent);*/
              try{ mlisListener.messageReceived(sms_str);}
              catch(Exception e){
                  Log.e("In exception=","ex"+e);

            }

              //mlisListener.messageReceived(sms_str);
            }
        }

        public static void bindListener(SmsListener smsListener) {
            mlisListener=smsListener;
        }
    }
