package com.example.horairesbateaux.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Trajet implements Parcelable {
    private int id;
    private String portDepart;
    private String portArrivee;

    public Trajet(int id, String portDepart, String portArrivee) {
        this.id = id;
        this.portDepart = portDepart;
        this.portArrivee = portArrivee;
    }

    /////////////////Parcelable/////////////////
    protected Trajet(Parcel in) {
        id = in.readInt();
        portDepart = in.readString();
        portArrivee = in.readString();
    }

    public static final Creator<Trajet> CREATOR = new Creator<Trajet>() {
        @Override
        public Trajet createFromParcel(Parcel in) {
            return new Trajet(in);
        }

        @Override
        public Trajet[] newArray(int size) {
            return new Trajet[size];
        }
    };
    /////////////////Parcelable/////////////////

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPortDepart() {
        return portDepart;
    }

    public void setPortDepart(String portDepart) {
        this.portDepart = portDepart;
    }

    public String getPortArrivee() {
        return portArrivee;
    }

    public void setPortArrivee(String portArrivee) {
        this.portArrivee = portArrivee;
    }

    @Override
    public String toString() {
        return getPortDepart() + " --> " + getPortArrivee();
    }

    /////////////////Parcelable/////////////////
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(portDepart);
        parcel.writeString(portArrivee);
    }
    /////////////////Parcelable/////////////////
}
