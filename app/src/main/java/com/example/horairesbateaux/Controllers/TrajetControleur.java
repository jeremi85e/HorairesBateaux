package com.example.horairesbateaux.Controllers;

import android.content.Context;

import com.example.horairesbateaux.Models.Trajet;
import com.example.horairesbateaux.Models.Traversee;
import com.example.horairesbateaux.Models.TraverseesBase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TrajetControleur {
    private TraverseesBase traverseesBase;

    public TrajetControleur(Context context){
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

    public String getIdVoyagesNbPlacesVendeenne(Trajet trajet){
        switch (trajet.getPortDepart()){
            case "Yeu":
                switch (trajet.getPortArrivee()){
                    case "Fromentine":
                        return "1RSF";
                    case "Barbâtre":
                        return "1RSLF";
                    case "St Gilles":
                        return "1RS.2RSV";
                    default:
                        return "";
                }
            case "Fromentine":
                return "1ASF";
            case "Barbâtre":
                return "1ASLF";
            case "St Gilles":
                return "1AS.2ASV";
            default:
                return "";
        }
    }

    public String getIdVoyagesNbPlacesYC(Trajet trajet){
        if (trajet.getPortDepart().equals("Yeu") && trajet.getPortArrivee().equals("Fromentine")){
            return "1VERS2";
        } else {
            if (trajet.getPortDepart().equals("Fromentine") && trajet.getPortArrivee().equals("Yeu")){
                return "2VERS1";
            } else {
                return "";
            }
        }
    }
}
