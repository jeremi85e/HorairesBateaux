package com.example.horairesbateaux.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.horairesbateaux.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private Context context;

    private String creationTrajets = "create table trajets ("
            + "id INTEGER PRIMARY KEY,"
            + "portDepart TEXT NOT NULL,"
            + "portArrivee TEXT NOT NULL);";

    private String creationTraversees = "create table traversees ("
            + "datePassage DATETIME NOT NULL,"
            + "typeBateau TEXT NOT NULL,"
            + "typeTrajet INTERGER NOT NULL,"
            + "tempsTraversee INTEGER NOT NULL,"
            + "trajetFacultatif INTEGER NOT NULL,"
            + "PRIMARY KEY (datePassage, typeBateau, typeTrajet));";


    private String trajetYeuFrom = "insert into trajets (id, portDepart, portArrivee) values "
            + "(1,\"Yeu\",\"Fromentine\");";
    private String trajetFromYeu = "insert into trajets (id, portDepart, portArrivee) values "
            + "(2,\"Fromentine\",\"Yeu\");";
    private String trajetYeuStGilles = "insert into trajets (id, portDepart, portArrivee) values "
            + "(3,\"Yeu\",\"St Gilles\");";
    private String trajetStGillesYeu = "insert into trajets (id, portDepart, portArrivee) values "
            + "(4,\"St Gilles\",\"Yeu\");";
    private String trajetYeuBarbatre = "insert into trajets (id, portDepart, portArrivee) values "
            + "(5,\"Yeu\",\"Barbâtre\");";
    private String trajetBarbatreYeu = "insert into trajets (id, portDepart, portArrivee) values "
            + "(6,\"Barbâtre\",\"Yeu\");";

    public MySQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //!!!!!!!!!! NE PAS OUBLIER DE CHANGER LA VERSION DE LA BASE DANS TraverseesBase !!!!!!!!!!

        //Création des Tables
        sqLiteDatabase.execSQL(creationTraversees);
        sqLiteDatabase.execSQL(creationTrajets);
        //Création des Trajets
        sqLiteDatabase.execSQL(trajetYeuFrom);
        sqLiteDatabase.execSQL(trajetFromYeu);
        sqLiteDatabase.execSQL(trajetYeuStGilles);
        sqLiteDatabase.execSQL(trajetStGillesYeu);
        sqLiteDatabase.execSQL(trajetYeuBarbatre);
        sqLiteDatabase.execSQL(trajetBarbatreYeu);

        //Création des Traversées
        final InputStream[] tableauIS = {
            //Yeu Continent https://www.yeu-continent.fr/passages/
                //2020
                    context.getResources().openRawResource(R.raw.sql_yc_janvier_2020),
                    context.getResources().openRawResource(R.raw.sql_yc_fevrier_2020),
                    context.getResources().openRawResource(R.raw.sql_yc_mars_2020),
                    context.getResources().openRawResource(R.raw.sql_yc_avril_2020),
                    context.getResources().openRawResource(R.raw.sql_yc_mai_2020),
                    context.getResources().openRawResource(R.raw.sql_yc_juin_2020),
                    context.getResources().openRawResource(R.raw.sql_yc_juillet_2020),
                    context.getResources().openRawResource(R.raw.sql_yc_aout_2020),
                    context.getResources().openRawResource(R.raw.sql_yc_septembre_2020),
                    context.getResources().openRawResource(R.raw.sql_yc_octobre_2020),
                    context.getResources().openRawResource(R.raw.sql_yc_novembre_2020),
                    context.getResources().openRawResource(R.raw.sql_yc_decembre_2020),
                //2021
                    context.getResources().openRawResource(R.raw.sql_yc_janvier_2021),
                    context.getResources().openRawResource(R.raw.sql_yc_fevrier_2021),
                    context.getResources().openRawResource(R.raw.sql_yc_mars_2021),
                    context.getResources().openRawResource(R.raw.sql_yc_avril_2021),
                    context.getResources().openRawResource(R.raw.sql_yc_mai_2021),
                    context.getResources().openRawResource(R.raw.sql_yc_juin_2021),
                    context.getResources().openRawResource(R.raw.sql_yc_juillet_2021),
                    context.getResources().openRawResource(R.raw.sql_yc_aout_2021),
                    context.getResources().openRawResource(R.raw.sql_yc_septembre_2021),
                    context.getResources().openRawResource(R.raw.sql_yc_octobre_2021),
                    context.getResources().openRawResource(R.raw.sql_yc_novembre_2021),
                    context.getResources().openRawResource(R.raw.sql_yc_decembre_2021),
                //Yeu Continent Horaires Facultatifs
                    context.getResources().openRawResource(R.raw.sql_yc_supplementaires_2020),
                    context.getResources().openRawResource(R.raw.sql_yc_supplementaires_2021),
            //Vendeenne Yeu --> Fromentine
                context.getResources().openRawResource(R.raw.sql_vendeenne_yf_avril_2020),
                context.getResources().openRawResource(R.raw.sql_vendeenne_yf_mai_2020),
                context.getResources().openRawResource(R.raw.sql_vendeenne_yf_juin_2020),
                context.getResources().openRawResource(R.raw.sql_vendeenne_yf_juillet_2020),
                context.getResources().openRawResource(R.raw.sql_vendeenne_yf_aout_2020),
                context.getResources().openRawResource(R.raw.sql_vendeenne_yf_septembre_2020),
                context.getResources().openRawResource(R.raw.sql_vendeenne_yf_octobre_2020),
            //Vendeenne Fromentine --> Yeu
                context.getResources().openRawResource(R.raw.sql_vendeenne_fy_avril_2020),
                context.getResources().openRawResource(R.raw.sql_vendeenne_fy_mai_2020),
                context.getResources().openRawResource(R.raw.sql_vendeenne_fy_juin_2020),
                context.getResources().openRawResource(R.raw.sql_vendeenne_fy_juillet_2020),
                context.getResources().openRawResource(R.raw.sql_vendeenne_fy_aout_2020),
                context.getResources().openRawResource(R.raw.sql_vendeenne_fy_septembre_2020),
                context.getResources().openRawResource(R.raw.sql_vendeenne_fy_octobre_2020),
            //Vendeenne Yeu --> Saint Gilles
                context.getResources().openRawResource(R.raw.sql_vendeenne_yg_avril_2020),
                context.getResources().openRawResource(R.raw.sql_vendeenne_yg_mai_2020),
                context.getResources().openRawResource(R.raw.sql_vendeenne_yg_juin_2020),
                context.getResources().openRawResource(R.raw.sql_vendeenne_yg_juillet_2020),
                context.getResources().openRawResource(R.raw.sql_vendeenne_yg_aout_2020),
                context.getResources().openRawResource(R.raw.sql_vendeenne_yg_septembre_2020),
            //Vendeenne Saint Gilles --> Yeu
                context.getResources().openRawResource(R.raw.sql_vendeenne_gy_avril_2020),
                context.getResources().openRawResource(R.raw.sql_vendeenne_gy_mai_2020),
                context.getResources().openRawResource(R.raw.sql_vendeenne_gy_juin_2020),
                context.getResources().openRawResource(R.raw.sql_vendeenne_gy_juillet_2020),
                context.getResources().openRawResource(R.raw.sql_vendeenne_gy_aout_2020),
                context.getResources().openRawResource(R.raw.sql_vendeenne_gy_septembre_2020),
            //Vendeenne Yeu --> Barbatre
                context.getResources().openRawResource(R.raw.sql_vendeenne_yb_juillet_2020),
                context.getResources().openRawResource(R.raw.sql_vendeenne_yb_aout_2020),
            //Vendeenne Barbatre --> Yeu
                context.getResources().openRawResource(R.raw.sql_vendeenne_by_juillet_2020),
                context.getResources().openRawResource(R.raw.sql_vendeenne_by_aout_2020)
        };

        for (InputStream inputStream : tableauIS) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String str;
                while ((str = reader.readLine()) != null) {
                    System.out.println(str);
                    sqLiteDatabase.execSQL(str.trim());
                }
                reader.close();
                inputStream.close();
            } catch (java.io.FileNotFoundException e) {
                Log.e( "FileNotFoundException", e.getMessage());
            } catch (IOException e) {
                Log.e( "FileNotFoundException", e.getMessage());
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS traversees");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS trajets");
            onCreate(sqLiteDatabase);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS traversees");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS trajets");
            onCreate(sqLiteDatabase);
        }
    }
}
