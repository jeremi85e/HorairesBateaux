package com.example.horairesbateaux.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.horairesbateaux.Models.Traversee;
import com.example.horairesbateaux.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TraverseesAdapter extends ArrayAdapter<Traversee> {

    private Context mContext;
    private ArrayList<Traversee> listetraversee;

    static final long ONE_MINUTE_IN_MILLIS=60000;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
    String horaires;

    public TraverseesAdapter(@NonNull Context context, ArrayList<Traversee> listetraverseeConstructeur) {
        super(context, 0 , listetraverseeConstructeur);
        mContext = context;
        listetraversee = listetraverseeConstructeur;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View traverseesItem = convertView;

        if(traverseesItem == null) {
            traverseesItem = LayoutInflater.from(mContext).inflate(R.layout.traversees_listview_item, parent, false);
        }

        Traversee currentTraversee = listetraversee.get(position);

        ImageView logoCompagnie = (ImageView) traverseesItem.findViewById(R.id.logoCompagnie);
        if (currentTraversee.getTypeBateau().equals("Vendeenne")){
            logoCompagnie.setImageResource(R.drawable.logo_vendeenne);
        } else {
            logoCompagnie.setImageResource(R.drawable.logo_yc);
        }

        TextView horaireDepart = (TextView) traverseesItem.findViewById(R.id.textViewHoraireDepart);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(currentTraversee.getDatePassage());
        Date dateArrivee = new Date(dateCalendar.getTimeInMillis() + (currentTraversee.getTempsTraversee() * ONE_MINUTE_IN_MILLIS));
        if (currentTraversee.isTrajetFacultatif()){
            horaires = "(" + simpleDateFormat.format(currentTraversee.getDatePassage()) + " --> " + simpleDateFormat.format(dateArrivee) + ")";
        } else {
            horaires = simpleDateFormat.format(currentTraversee.getDatePassage()) + " --> " + simpleDateFormat.format(dateArrivee);
        }
        horaireDepart.setText(horaires);

        TextView typeBateau = (TextView) traverseesItem.findViewById(R.id.textViewTypeBateau);
        if (currentTraversee.getTypeBateau().equals("Vendeenne")){
            typeBateau.setText("Vendéenne");
        } else {
            typeBateau.setText(currentTraversee.getTypeBateau());
        }


        return traverseesItem;
    }
}