package com.a7f.drawingsound.lib;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SendToServer extends AsyncTask<String, Void, String>{
    private String fileName;
    private String path;


    public SendToServer(String filename){
        this.fileName = filename + ".wav";
        path = Environment.getExternalStorageDirectory().toString();
    }
    @Override
    protected String doInBackground(String... url) {
        String response_str = sendWav();
        return response_str;
    }


    public String sendWav(){
        String url = "http://drawingsound.com/model/musicname";
        File file = new File(Environment.getExternalStorageDirectory(),fileName);

        try {
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
        }
       return null;
    }

}
