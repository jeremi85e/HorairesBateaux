package com.example.horairesbateaux.Controllers;

import android.content.Context;
import android.util.Log;

import com.example.horairesbateaux.Models.Trajet;
import com.example.horairesbateaux.Models.Traversee;
import com.example.horairesbateaux.Models.TraverseesBase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TraverseesControleur {
    private TraverseesBase traverseesBase;

    public TraverseesControleur(Context context){
        traverseesBase = new TraverseesBase(context);
    }

    public TraverseesBase getTraverseesBase() {
        return traverseesBase;
    }

    public void setTraverseesBase(TraverseesBase traverseesBase) {
        this.traverseesBase = traverseesBase;
    }

    public Trajet getTrajet(int id){
        return traverseesBase.getTrajet(id);
    }

    public ArrayList<Trajet> getTrajets(){
        return traverseesBase.getTrajets();
    }

    public ArrayList<Traversee> getTraverseesParJour(Date date, Trajet trajet){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return traverseesBase.getTraverseesParJour(dateFormat.format(date), trajet.getId());
    }
}
