package com.example.horairesbateaux.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Traversee implements Parcelable, Comparable<Traversee> {
    private Date datePassage;
    private String typeBateau;
    private Trajet trajet;
    private int tempsTraversee;
    private boolean trajetFacultatif;
    private String messageDispo;

    public Traversee(Date datePassage, String typeBateau, Trajet trajet, int tempsTraversee, boolean trajetFacultatif) {
        this.datePassage = datePassage;
        this.typeBateau = typeBateau;
        this.trajet = trajet;
        this.tempsTraversee = tempsTraversee;
        this.trajetFacultatif = trajetFacultatif;
        messageDispo = "";
    }

    /////////////////Parcelable/////////////////
    protected Traversee(Parcel in) {
        datePassage = new Date(in.readLong());
        typeBateau = in.readString();
        trajet = in.readParcelable(Trajet.class.getClassLoader());
        tempsTraversee = in.readInt();
        trajetFacultatif = in.readInt() == 1;
        messageDispo = "";
    }

    public static final Creator<Traversee> CREATOR = new Creator<Traversee>() {
        @Override
        public Traversee createFromParcel(Parcel in) {
            return new Traversee(in);
        }

        @Override
        public Traversee[] newArray(int size) {
            return new Traversee[size];
        }
    };
    /////////////////Parcelable/////////////////

    public Date getDatePassage() {
        return datePassage;
    }

    public void setDatePassage(Date datePassage) {
        this.datePassage = datePassage;
    }

    public String getTypeBateau() {
        return typeBateau;
    }

    public void setTypeBateau(String typeBateau) {
        this.typeBateau = typeBateau;
    }

    public Trajet getTrajet() {
        return trajet;
    }

    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
    }

    public int getTempsTraversee() {
        return tempsTraversee;
    }

    public void setTempsTraversee(int tempsTraversee) {
        this.tempsTraversee = tempsTraversee;
    }

    public boolean isTrajetFacultatif() {
        return trajetFacultatif;
    }

    public void setTrajetFacultatif(boolean trajetFacultatif) {
        this.trajetFacultatif = trajetFacultatif;
    }

    public String getMessageDispo() {
        return messageDispo;
    }

    public void setMessageDispo(String messageDispo) {
        this.messageDispo = messageDispo;
    }

    @Override
    public String toString() {
        return "Traversee{" +
                "datePassage=" + datePassage +
                ", typeBateau='" + typeBateau + '\'' +
                ", trajet=" + trajet +
                ", tempsTraversee=" + tempsTraversee +
                ", trajetFacultatif=" + trajetFacultatif +
                '}';
    }

    /////////////////Parcelable/////////////////
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(datePassage.getTime());
        parcel.writeString(typeBateau);
        parcel.writeParcelable(trajet, 0);
        parcel.writeInt(tempsTraversee);
        parcel.writeInt(trajetFacultatif ? 1 : 0);
    }
    /////////////////Parcelable/////////////////

    @Override
    public int compareTo(Traversee traversee) {
        return getDatePassage().compareTo(traversee.getDatePassage());
    }
}
