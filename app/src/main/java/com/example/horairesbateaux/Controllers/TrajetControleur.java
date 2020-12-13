package com.example.horairesbateaux.Controllers;

import android.content.Context;

import com.example.horairesbateaux.Models.MaBase;
import com.example.horairesbateaux.Models.Trajet;

import java.util.ArrayList;

public class TrajetControleur {
    private MaBase maBase;

    public TrajetControleur(Context context){
        maBase = new MaBase(context);
    }

    public MaBase getMaBase() {
        return maBase;
    }

    public void setMaBase(MaBase maBase) {
        this.maBase = maBase;
    }

    public Trajet getTrajet(int id){
        return maBase.getTrajet(id);
    }

    public ArrayList<Trajet> getTrajets(){
        return maBase.getTrajets();
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
