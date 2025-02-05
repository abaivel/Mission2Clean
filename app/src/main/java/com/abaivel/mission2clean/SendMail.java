package com.abaivel.mission2clean;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.Toast;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;

public class SendMail extends AsyncTask<Message, String, String> {

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Message... messages){
        try {
            Transport.send(messages[0]);
            return "Success";
        }catch (MessagingException e){
            e.printStackTrace();
            return "Error";
        }
    }

    @Override
    protected void onPostExecute(String s){
        super.onPostExecute(s);
        if (s.equals("Success")){
            Toast.makeText(MissionActivity.Companion.getContext(), "Email envoyé", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MissionActivity.Companion.getContext(), "Il y a eu un problème dans l'envoi du mail", Toast.LENGTH_SHORT).show();
        }
    }
}
