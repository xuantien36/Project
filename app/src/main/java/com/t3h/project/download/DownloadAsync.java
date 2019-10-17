package com.t3h.project.download;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadAsync extends AsyncTask<String, Integer, String> {

    private DownloadCallback callback;
    public DownloadAsync(DownloadCallback callback) {
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String link = strings[0];
        return download(link);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        callback.onDownloadUpdate(values[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        callback.onDownloadSuccess(s);
    }

    private String download(String link) {
        @SuppressLint("SdCardPath") String path = "/sdcard/Download/"+ System.currentTimeMillis()+".html";
        Log.e("abc",path.toString());
        try {
            String USERAGENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_5_7; en-us) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Safari/530.17";

            URL url = new URL(link);
            //create the new connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("User-Agent", USERAGENT);  //if you are not sure of user agent just set choice=0
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();


            File file = new File(path);
            file.getParentFile().mkdirs();
            file.createNewFile();

            //this will be used to write the downloaded data into the file we created
            FileOutputStream fileOutput = new FileOutputStream(file);

            //this will be used in reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            //this is the total size of the file
            int totalSize = urlConnection.getContentLength();
            //variable to store total downloaded bytes
            int downloadedSize = 0;

            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0; //used to store a temporary size of the buffer

            //write the contents to the file
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
            }
            //close the output stream when done
            fileOutput.close();
            inputStream.close();
            urlConnection.disconnect();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return path ;
    }

    public interface DownloadCallback {
        void onDownloadUpdate(int percent);
        void onDownloadSuccess(String path);
    }

}
