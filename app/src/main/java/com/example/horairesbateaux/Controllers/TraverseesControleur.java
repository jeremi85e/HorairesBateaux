package com.example.horairesbateaux.Controllers;

import android.content.Context;

import com.example.horairesbateaux.Models.MaBase;
import com.example.horairesbateaux.Models.Trajet;
import com.example.horairesbateaux.Models.Traversee;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TraverseesControleur {
    private MaBase maBase;

    public TraverseesControleur(Context context){
        maBase = new MaBase(context);
    }

    public MaBase getMaBase() {
        return maBase;
    }

    public void setMaBase(MaBase maBase) {
        this.maBase = maBase;
    }

    public ArrayList<Traversee> getTraverseesParJour(Date date, Trajet trajet){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return maBase.getTraverseesParJour(dateFormat.format(date), trajet.getId());
    }
}
