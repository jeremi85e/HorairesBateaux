package com.example.horairesbateaux.Utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;

public class TraverseesHttpURLConnection {

    public static String startHttpRequest(String urlString1, String urlString2, String urlString3, String urlString4, String urlString5, String urlString6, String urlString7, String urlString8){

        StringBuilder stringBuilder1 = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        StringBuilder stringBuilder3 = new StringBuilder();
        StringBuilder stringBuilderFinal = new StringBuilder();
        StringBuilder stringBuilder5 = new StringBuilder();
        StringBuilder stringBuilder6 = new StringBuilder();
        StringBuilder stringBuilder7 = new StringBuilder();

        Log.e("AsyncTask", urlString5);

        try {
            CookieManager cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);

            stringBuilderFinal.append("{\"yc\":");

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

            stringBuilderFinal.append(", \"vendeenne\":");

            // 1 - Declare a URL Connection
            URL url5 = new URL(urlString5);
            HttpURLConnection conn5 = (HttpURLConnection) url5.openConnection();
            // 2 - Open InputStream to connection
            conn5.connect();
            InputStream in5 = conn5.getInputStream();
            // 3 - Download and decode the string response
            BufferedReader reader5 = new BufferedReader(new InputStreamReader(in5));
            String line5;
            while ((line5 = reader5.readLine()) != null) {
                stringBuilder5.append(line5);
            }

            if (stringBuilder5.toString().contains("\"succes\":true,")){
                // 1 - Declare a URL Connection
                URL url6 = new URL(urlString6);
                HttpURLConnection conn6 = (HttpURLConnection) url6.openConnection();
                // 2 - Open InputStream to connection
                conn6.connect();
                InputStream in6 = conn6.getInputStream();
                // 3 - Download and decode the string response
                BufferedReader reader6 = new BufferedReader(new InputStreamReader(in6));
                String line6;
                while ((line6 = reader6.readLine()) != null) {
                    stringBuilder6.append(line6);
                }

                if (stringBuilder6.toString().contains("\"succes\":true,")) {
                    // 1 - Declare a URL Connection
                    URL url7 = new URL(urlString7);
                    HttpURLConnection conn7 = (HttpURLConnection) url7.openConnection();
                    // 2 - Open InputStream to connection
                    conn7.connect();
                    InputStream in7 = conn7.getInputStream();
                    // 3 - Download and decode the string response
                    BufferedReader reader7 = new BufferedReader(new InputStreamReader(in7));
                    String line7;
                    while ((line7 = reader7.readLine()) != null) {
                        stringBuilder7.append(line7);
                    }

                    if (stringBuilder7.toString().contains("\"succes\":true,")) {
                        // 1 - Declare a URL Connection
                        URL url8 = new URL(urlString8);
                        HttpURLConnection conn8 = (HttpURLConnection) url8.openConnection();
                        // 2 - Open InputStream to connection
                        conn8.connect();
                        InputStream in8 = conn8.getInputStream();
                        // 3 - Download and decode the string response
                        BufferedReader reader8 = new BufferedReader(new InputStreamReader(in8));
                        String line8;
                        while ((line8 = reader8.readLine()) != null) {
                            stringBuilderFinal.append(line8);
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

            stringBuilderFinal.append("}");

            Log.e("AsyncTask", stringBuilderFinal.toString());

        } catch (Exception e){
            Log.e("TraverseesHttpURLConnec", e.toString());
        }

        return stringBuilderFinal.toString();
    }
}