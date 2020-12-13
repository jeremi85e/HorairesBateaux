package com.example.horairesbateaux.Utils;

import android.util.Log;

import java.lang.ref.WeakReference;

public class TraverseesAsyncTask extends android.os.AsyncTask<String, Void, String> {

    public interface Listeners {
        void onPreExecute();
        void doInBackground();
        void onPostExecute(String success);
    }

    private final WeakReference<Listeners> callback;

    public TraverseesAsyncTask(Listeners callback){
        this.callback = new WeakReference<>(callback);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.callback.get().onPreExecute();
        Log.e("TAG", "AsyncTask is started.");
    }

    @Override
    protected void onPostExecute(String success) {
        super.onPostExecute(success);
        this.callback.get().onPostExecute(success);
        Log.e("TAG", "AsyncTask is finished.");
    }

    @Override
    protected String doInBackground(String... url) {
        this.callback.get().doInBackground();
        Log.e("TAG", "AsyncTask doing some big work...");
        return TraverseesHttpURLConnection.startHttpRequest(url[0], url[1], url[2], url[3], url[4], url[5], url[6], url[7]);
    }
}