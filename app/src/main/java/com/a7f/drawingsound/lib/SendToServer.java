package com.a7f.drawingsound.lib;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.a7f.drawingsound.FindResultActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SendToServer extends AsyncTask<Void, Void, Void>{
    private String fileName;
    private String path;
    private ProgressDialog asyncDialog;
    private Context context;
    private String result;


    public SendToServer(String filename, Context context){
        super();
        this.fileName = filename;
        path = Environment.getExternalStorageDirectory().toString();
        this.context = context;
        asyncDialog = null;
        result=null;
    }

    @Override
    protected void onPreExecute() {
        asyncDialog = new ProgressDialog(context);
        asyncDialog.setMessage("유사한 음악을 찾는 중입니다.");
        asyncDialog.setCancelable(false);
        asyncDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void ... voids) {
        result = sendWav();
        return null;
    }

    @Override
    protected void onPostExecute(Void r) {
        asyncDialog.dismiss();
        Intent intent = new Intent(context, FindResultActivity.class);
        intent.putExtra("result", result);
        context.startActivity(intent);
        super.onPostExecute(r);
    }


    public String sendWav(){
        try {
            String url = "http://drawingsound.com/model/musicname";
            Log.e("sendwav","sendwav");
            File file = new File(Environment.getExternalStorageDirectory(),fileName);

            HttpClient httpclient = new DefaultHttpClient();

            HttpPost httppost = new HttpPost(url);

            InputStreamEntity reqEntity = new InputStreamEntity(
                    new FileInputStream(file), -1);
            reqEntity.setContentType("audio/wav");
            reqEntity.setChunked(true); // Send in multiple parts if needed
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);

            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            // show error
            Log.e("send wav", e.getMessage());
        } catch (Exception e){
            Log.e("sendwav exception", e.getMessage());
        }
       return null;
    }
}
