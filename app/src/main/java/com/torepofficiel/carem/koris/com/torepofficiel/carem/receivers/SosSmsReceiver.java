package com.torepofficiel.carem.koris.com.torepofficiel.carem.receivers;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import com.torepofficiel.carem.koris.ErrorActivity;
import com.torepofficiel.carem.koris.MainActivity;
import com.torepofficiel.carem.koris.R;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.async_task_implementations.DatabaseAsyncTask;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.bean.Utilisateur;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.metier.DataBaseHandler;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires.ClasseUtilitaire;
import com.torepofficiel.carem.koris.com.torepofficiel.carem.utilitaires.CryptageSha512;

import java.util.concurrent.ExecutionException;


public class SosSmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED") ){

            Bundle bundle = intent.getExtras();
            SmsMessage smsMsg = null;
            String msg_body = "";
            if(bundle != null){

                if(Build.VERSION.SDK_INT >=19){

                    SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                    smsMsg = msgs[0];

                } else {

                    Object[] pdus = (Object[]) bundle.get("pdus");
                    if(pdus != null)
                        //noinspection deprecation
                        smsMsg = SmsMessage.createFromPdu((byte[]) pdus[0] );
                }
                if(smsMsg != null)
                    msg_body = smsMsg.getMessageBody().toLowerCase().trim();

                if(msg_body.endsWith("sos-koris")){

                    Utilisateur utilisateur = new Utilisateur();

                    try {
                        utilisateur = DatabaseAsyncTask.selectionnerUtilisateur(1L);
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }

                    String[] content = msg_body.split("/");
                    if(content.length > 1){
                        if(!utilisateur.isEst_null() && utilisateur.getMot_de_passe().equals(CryptageSha512.crypt(content[0]))){

                            ContentValues values = new ContentValues();
                            values.put(DataBaseHandler.APP_FREEZED, true );

                            DatabaseAsyncTask.modifierParametres(values);

                            //write object utilisateur in file
                            try {
                                utilisateur.setHisAbonnement( DatabaseAsyncTask.getCurrentAbonnementsOfAnUtilisateur(
                                         1L
                                ));

                                utilisateur.setListeCode( DatabaseAsyncTask.selectionnerTousCodes(1L )
                                );

                                utilisateur.setApp_freezed(true);

                                ClasseUtilitaire.writeAnObjectInFile(utilisateur);

                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                            }


                            Intent intent1 = new Intent(context, ErrorActivity.class);
                            intent1.putExtra(MainActivity.APP_FREEZED, context.getString(R.string.app_freezed));
                            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            context.startActivity(intent1);


                        }

                    }
                }

            }
        }
    }

}
