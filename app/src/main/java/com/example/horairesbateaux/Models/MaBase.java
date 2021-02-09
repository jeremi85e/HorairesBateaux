package com.example.horairesbateaux.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.horairesbateaux.Utils.MySQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MaBase {

    private String nomBase = "bdTraversees";
    private Integer versionBase = 34; // Toujours ajouter +1 lorsque je fais une MAJ sur la BD
    private MySQLiteOpenHelper accesBD;
    private SQLiteDatabase bd;
    private SimpleDateFormat sdfDateSqlite = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.FRENCH);

    public MaBase(Context context){
        accesBD = new MySQLiteOpenHelper(context, nomBase, null, versionBase);
    }

    public Trajet getTrajet(int id){
        Trajet trajet = null;
        bd = accesBD.getReadableDatabase();
        String requete = "select * from trajets where id = '" + id + "';";
        Cursor c = bd.rawQuery(requete, null);
        if(c != null && c.moveToFirst()){
            trajet = new Trajet(c.getInt(0), c.getString(1), c.getString(2));
        }
        c.close();
        return trajet;
    }

    public ArrayList<Trajet> getTrajets(){
        ArrayList<Trajet> listeTrajets = new ArrayList<>();
        bd = accesBD.getReadableDatabase();
        String requete = "select * from trajets;";
        Cursor c = bd.rawQuery(requete, null);
        if(c != null && c.moveToFirst()){
            do {
                listeTrajets.add(new Trajet(c.getInt(0), c.getString(1), c.getString(2)));
            } while (c.moveToNext());
        }
        c.close();
        return listeTrajets;
    }

    public ArrayList<Traversee> getTraverseesParJour(String dateTraversee, int idTrajet){
        ArrayList<Traversee> listeTraversees = new ArrayList<>();
        bd = accesBD.getReadableDatabase();
        String requete = "select * from traversees where date(datePassage) == date('" + dateTraversee + "') AND typeTrajet == " + idTrajet + ";";
        Cursor c = bd.rawQuery(requete, null);
        if(c != null && c.moveToFirst()){
            do {
                try {
                    listeTraversees.add(new Traversee(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(c.getString(0)), c.getString(1), this.getTrajet(c.getInt(2)), c.getInt(3), c.getInt(4) > 0));
                } catch (ParseException e){
                    Log.e("Erreur MaBase", e.getMessage());
                }
            } while (c.moveToNext());
        }
        c.close();
        return listeTraversees;
    }

    public ArrayList<Traversee> getMesTraversees(){
        ArrayList<Traversee> listeMesTraversees = new ArrayList<>();
        bd = accesBD.getReadableDatabase();
        String requete = "select * from mesTraversees;";
        Cursor c = bd.rawQuery(requete, null);
        if(c != null && c.moveToFirst()){
            do {
                try {
                    listeMesTraversees.add(new Traversee(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(c.getString(0)), c.getString(1), this.getTrajet(c.getInt(2)), c.getInt(3), c.getInt(4) > 0));
                } catch (ParseException e){
                    Log.e("Erreur MaBase", e.getMessage());
                }
            } while (c.moveToNext());
        }
        c.close();
        return listeMesTraversees;
    }

    public long insertMaTraversee(Traversee maTraversee){
        bd = accesBD.getWritableDatabase();
        ContentValues traverseeAAjouter = new ContentValues();
        traverseeAAjouter.put("datePassage", sdfDateSqlite.format(maTraversee.getDatePassage()));
        traverseeAAjouter.put("typeBateau", maTraversee.getTypeBateau());
        traverseeAAjouter.put("typeTrajet", maTraversee.getTrajet().getId());
        traverseeAAjouter.put("tempsTraversee", maTraversee.getTempsTraversee());
        traverseeAAjouter.put("trajetFacultatif", maTraversee.isTrajetFacultatif() ? 1 : 0);

        long numberOfMaTraverseeDeleted = bd.insert("mesTraversees", null, traverseeAAjouter);
        return numberOfMaTraverseeDeleted;
    }

    public int deleteMaTraversee(Traversee maTraversee){
        bd = accesBD.getWritableDatabase();
        String where = "datePassage=? AND typeBateau=? AND typeTrajet=?";
        int numberOfMaTraverseeDeleted = bd.delete("mesTraversees", where, new String[]{sdfDateSqlite.format(maTraversee.getDatePassage()), maTraversee.getTypeBateau(), maTraversee.getTrajet().getId() + ""});
        return numberOfMaTraverseeDeleted;
    }
}
