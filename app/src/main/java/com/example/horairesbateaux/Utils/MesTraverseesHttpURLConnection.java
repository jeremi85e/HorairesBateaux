package com.example.horairesbateaux.Utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;

public class MesTraverseesHttpURLConnection {

    public static String startHttpRequest(String urlString1, String urlString2, String urlString3, String urlString4){

        StringBuilder stringBuilder1 = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        StringBuilder stringBuilder3 = new StringBuilder();
        StringBuilder stringBuilderFinal = new StringBuilder();

        try {
            CookieManager cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);

            // 1 - Declare a URL Connection
            URL url1 = new URL(urlString1);
            HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();
            // 2 - Open InputStream to connection
            conn1.connect();
            InputStream in1 = conn1.getInputStream();
            // 3 - Download and decode the string response
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(in1));
            String line1;
            while ((line1 = reader1.readLine()) != null) {
                stringBuilder1.append(line1);
            }

            if (stringBuilder1.toString().contains("\"succes\":true,")){
                // 1 - Declare a URL Connection
                URL url2 = new URL(urlString2);
                HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
                // 2 - Open InputStream to connection
                conn2.connect();
                InputStream in2 = conn2.getInputStream();
                // 3 - Download and decode the string response
                BufferedReader reader2 = new BufferedReader(new InputStreamReader(in2));
                String line2;
                while ((line2 = reader2.readLine()) != null) {
                    stringBuilder2.append(line2);
                }

                if (stringBuilder2.toString().contains("\"succes\":true,")) {
                    // 1 - Declare a URL Connection
                    URL url3 = new URL(urlString3);
                    HttpURLConnection conn3 = (HttpURLConnection) url3.openConnection();
                    // 2 - Open InputStream to connection
                    conn3.connect();
                    InputStream in3 = conn3.getInputStream();
                    // 3 - Download and decode the string response
                    BufferedReader reader3 = new BufferedReader(new InputStreamReader(in3));
                    String line3;
                    while ((line3 = reader3.readLine()) != null) {
                        stringBuilder3.append(line3);
                    }

                    if (stringBuilder3.toString().contains("\"succes\":true,")) {
                        // 1 - Declare a URL Connection
                        URL url4 = new URL(urlString4);
                        HttpURLConnection conn4 = (HttpURLConnection) url4.openConnection();
                        // 2 - Open InputStream to connection
                        conn4.connect();
                        InputStream in4 = conn4.getInputStream();
                        // 3 - Download and decode the string response
                        BufferedReader reader4 = new BufferedReader(new InputStreamReader(in4));
                        String line4;
                        while ((line4 = reader4.readLine()) != null) {
                            stringBuilderFinal.append(line4);
                        }
                    } else {
                        stringBuilderFinal.append("{}");
                    }
                } else {
                    stringBuilderFinal.append("{}");
                }
            } else {
                stringBuilderFinal.append("{}");
            }

            Log.e("AsyncTask", stringBuilderFinal.toString());

        } catch (Exception e){
            Log.e("MesTraverseesHttpURLCon", e.toString());
        }

        return stringBuilderFinal.toString();
    }
}