package com.example.horairesbateaux.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.horairesbateaux.Adapters.TraverseesAdapter;
import com.example.horairesbateaux.Controllers.TraverseesControleur;
import com.example.horairesbateaux.Models.Trajet;
import com.example.horairesbateaux.Models.Traversee;
import com.example.horairesbateaux.R;
import com.example.horairesbateaux.Utils.OnSwipeTouchListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class TraverseesActivity extends AppCompatActivity {

    Button boutonRetour;
    ImageButton boutonJourPrecedent;
    ImageButton boutonJourSuivant;
    ListView listview;
    TextView textviewTraversees;
    Trajet trajetSouhaite;
    TraverseesControleur traverseesControleur;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE dd/MM/yyyy");

    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    private Date dateTraversees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.traversees_activity);

        traverseesControleur = new TraverseesControleur(this);

        boutonRetour = (Button) findViewById(R.id.boutonRetour);
        boutonJourPrecedent = (ImageButton) findViewById(R.id.boutonJourPrecedent);
        boutonJourSuivant = (ImageButton) findViewById(R.id.boutonJourSuivant);
        listview = (ListView) findViewById(R.id.listviewTraversees);
        textviewTraversees = (TextView) findViewById(R.id.textViewTraversees);

        //Récupération de l'intent
        Intent intent = getIntent();
        final ArrayList<Traversee> listeTraversees = intent.getParcelableArrayListExtra("listeTraversees");
        trajetSouhaite = intent.getParcelableExtra("trajet");
        dateTraversees = new Date(intent.getLongExtra("date", 1));
        Collections.sort(listeTraversees);
        listview.setAdapter(new TraverseesAdapter(TraverseesActivity.this, listeTraversees));
        StringBuilder dateTexte = new StringBuilder(simpleDateFormat.format(dateTraversees));
        dateTexte.setCharAt(0, Character.toUpperCase(dateTexte.charAt(0)));
        textviewTraversees.setText(trajetSouhaite.toString() + " " + dateTexte);

        boutonJourPrecedent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                c.setTime(dateTraversees);
                c.add(Calendar.DATE, -1);
                dateTraversees = c.getTime();
                StringBuilder dateTexte = new StringBuilder(simpleDateFormat.format(dateTraversees));
                dateTexte.setCharAt(0, Character.toUpperCase(dateTexte.charAt(0)));
                textviewTraversees.setText(trajetSouhaite.toString() + " " + dateTexte);
                ArrayList<Traversee> listeTraversees = traverseesControleur.getTraverseesParJour(dateTraversees, trajetSouhaite);
                Collections.sort(listeTraversees);
                listview.setAdapter(new TraverseesAdapter(TraverseesActivity.this, listeTraversees));
            }
        });

        textviewTraversees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trajetSouhaite.getId() % 2 == 0){
                    trajetSouhaite = traverseesControleur.getTrajet(trajetSouhaite.getId() - 1);
                } else {
                    trajetSouhaite = traverseesControleur.getTrajet(trajetSouhaite.getId() + 1);
                }
                StringBuilder dateTexte = new StringBuilder(simpleDateFormat.format(dateTraversees));
                dateTexte.setCharAt(0, Character.toUpperCase(dateTexte.charAt(0)));
                textviewTraversees.setText(trajetSouhaite.toString() + " " + dateTexte);
                ArrayList<Traversee> listeTraversees = traverseesControleur.getTraverseesParJour(dateTraversees, trajetSouhaite);
                Collections.sort(listeTraversees);
                listview.setAdapter(new TraverseesAdapter(TraverseesActivity.this, listeTraversees));
            }
        });

        boutonJourSuivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                c.setTime(dateTraversees);
                c.add(Calendar.DATE, 1);
                dateTraversees = c.getTime();
                StringBuilder dateTexte = new StringBuilder(simpleDateFormat.format(dateTraversees));
                dateTexte.setCharAt(0, Character.toUpperCase(dateTexte.charAt(0)));
                textviewTraversees.setText(trajetSouhaite.toString() + " " + dateTexte);
                ArrayList<Traversee> listeTraversees = traverseesControleur.getTraverseesParJour(dateTraversees, trajetSouhaite);
                Collections.sort(listeTraversees);
                listview.setAdapter(new TraverseesAdapter(TraverseesActivity.this, listeTraversees));
            }
        });

        listview.setOnTouchListener(new OnSwipeTouchListener(TraverseesActivity.this) {
            public void onSwipeRight() {
                Calendar c = Calendar.getInstance();
                c.setTime(dateTraversees);
                c.add(Calendar.DATE, -1);
                dateTraversees = c.getTime();
                StringBuilder dateTexte = new StringBuilder(simpleDateFormat.format(dateTraversees));
                dateTexte.setCharAt(0, Character.toUpperCase(dateTexte.charAt(0)));
                textviewTraversees.setText(trajetSouhaite.toString() + " " + dateTexte);
                ArrayList<Traversee> listeTraversees = traverseesControleur.getTraverseesParJour(dateTraversees, trajetSouhaite);
                Collections.sort(listeTraversees);
                listview.setAdapter(new TraverseesAdapter(TraverseesActivity.this, listeTraversees));
            }
            public void onSwipeLeft() {
                Calendar c = Calendar.getInstance();
                c.setTime(dateTraversees);
                c.add(Calendar.DATE, 1);
                dateTraversees = c.getTime();
                StringBuilder dateTexte = new StringBuilder(simpleDateFormat.format(dateTraversees));
                dateTexte.setCharAt(0, Character.toUpperCase(dateTexte.charAt(0)));
                textviewTraversees.setText(trajetSouhaite.toString() + " " + dateTexte);
                ArrayList<Traversee> listeTraversees = traverseesControleur.getTraverseesParJour(dateTraversees, trajetSouhaite);
                Collections.sort(listeTraversees);
                listview.setAdapter(new TraverseesAdapter(TraverseesActivity.this, listeTraversees));
            }
        });

        boutonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}
