package com.example.horairesbateaux.Controllers;

import android.content.Context;

import com.example.horairesbateaux.Models.Trajet;
import com.example.horairesbateaux.Models.Traversee;
import com.example.horairesbateaux.Models.MaBase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MesTraverseesControleur {
    private MaBase maBase;

    public MesTraverseesControleur(Context context){
        maBase = new MaBase(context);
    }

    public MaBase getMaBase() {
        return maBase;
    }

    public void setMaBase(MaBase maBase) {
        this.maBase = maBase;
    }

    public ArrayList<Traversee> getMesTraversees(){
        return maBase.getMesTraversees();
    }

    public int deleteMaTraversee(Traversee maTraversee){
        return maBase.deleteMaTraversee(maTraversee);
    }

    public long insertMaTraversee(Traversee maTraversee){
        return maBase.insertMaTraversee(maTraversee);
    }
}
