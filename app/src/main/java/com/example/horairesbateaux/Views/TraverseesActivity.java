package com.example.horairesbateaux.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.horairesbateaux.Adapters.TraverseesAdapter;
import com.example.horairesbateaux.Controllers.TrajetControleur;
import com.example.horairesbateaux.Controllers.TraverseesControleur;
import com.example.horairesbateaux.Models.Trajet;
import com.example.horairesbateaux.Models.Traversee;
import com.example.horairesbateaux.R;
import com.example.horairesbateaux.Utils.OnSwipeTouchListener;
import com.example.horairesbateaux.Utils.BateauxAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class TraverseesActivity extends AppCompatActivity implements BateauxAsyncTask.Listeners {

    Button boutonRetour;
    ImageButton boutonJourPrecedent;
    ImageButton boutonJourSuivant;
    ListView listview;
    TextView textviewTraversees;
    Trajet trajetSouhaite;
    TraverseesControleur traverseesControleur;
    TrajetControleur trajetControleur;
    SimpleDateFormat sdfTextViewTraversees = new SimpleDateFormat("EEE dd/MM/yyyy");
    SimpleDateFormat sdfDateCollee = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat sdfDateTexte = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat sdfDateHeure = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    private Date dateTraversees;

    private ArrayList<ArrayList> listNbPlacesRestantes = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.traversees_activity);

        traverseesControleur = new TraverseesControleur(this);
        trajetControleur = new TrajetControleur(this);

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
        StringBuilder dateTexte = new StringBuilder(sdfTextViewTraversees.format(dateTraversees));
        dateTexte.setCharAt(0, Character.toUpperCase(dateTexte.charAt(0)));
        textviewTraversees.setText(trajetSouhaite.toString() + " " + dateTexte);

        executeHttpRequests();

        boutonJourPrecedent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ajoutJours(-1);
                listNbPlacesRestantes = new ArrayList();
                updateUI();
            }
        });

        textviewTraversees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trajetSouhaite.getId() % 2 == 0){
                    trajetSouhaite = trajetControleur.getTrajet(trajetSouhaite.getId() - 1);
                } else {
                    trajetSouhaite = trajetControleur.getTrajet(trajetSouhaite.getId() + 1);
                }
                listNbPlacesRestantes = new ArrayList();
                updateUI();
            }
        });

        boutonJourSuivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ajoutJours(1);
                listNbPlacesRestantes = new ArrayList();
                updateUI();
            }
        });

        listview.setOnTouchListener(new OnSwipeTouchListener(TraverseesActivity.this) {
            public void onSwipeRight() {
                ajoutJours(-1);
                listNbPlacesRestantes = new ArrayList();
                updateUI();
            }
            public void onSwipeLeft() {
                ajoutJours(1);
                listNbPlacesRestantes = new ArrayList();
                updateUI();
            }
        });

        boutonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void ajoutJours(int nbJours) {
        Calendar c = Calendar.getInstance();
        c.setTime(dateTraversees);
        c.add(Calendar.DATE, nbJours);
        dateTraversees = c.getTime();
    }

    private void updateUI() {
        StringBuilder dateTexte = new StringBuilder(sdfTextViewTraversees.format(dateTraversees));
        dateTexte.setCharAt(0, Character.toUpperCase(dateTexte.charAt(0)));
        textviewTraversees.setText(trajetSouhaite.toString() + " " + dateTexte);
        ArrayList<Traversee> listeTraversees = traverseesControleur.getTraverseesParJour(dateTraversees, trajetSouhaite);
        Collections.sort(listeTraversees);
        listview.setAdapter(new TraverseesAdapter(TraverseesActivity.this, listeTraversees));
        executeHttpRequests();
    }

    // ------------------
    //  HTTP REQUEST
    // ------------------

    private void executeHttpRequests(){
        new BateauxAsyncTask(this).execute(
                "https://resa-prod.yeu-continent.fr/ws/?&func=set_id_voyage&num_voyage=0&id_voyage=" + trajetControleur.getIdVoyagesNbPlacesYC(trajetSouhaite),
                "https://resa-prod.yeu-continent.fr/ws/?&func=liste_dates&num_voyage=0",
                "https://resa-prod.yeu-continent.fr/ws/?&func=set_date_depart&num_voyage=0&num_passage=0&date_depart=" + sdfDateCollee.format(dateTraversees),
                "https://resa-prod.yeu-continent.fr/ws/?&func=liste_horaires&num_voyage=0&num_passage=0",
                "https://resa-prod.compagnie-vendeenne.com/ws/?&func=set_id_voyage&num_voyage=0&id_voyage=" + trajetControleur.getIdVoyagesNbPlacesVendeenne(trajetSouhaite),
                "https://resa-prod.compagnie-vendeenne.com/ws/?&func=liste_dates&num_voyage=0",
                "https://resa-prod.compagnie-vendeenne.com/ws/?&func=set_date_depart&num_voyage=0&num_passage=0&date_depart=" + sdfDateCollee.format(dateTraversees),
                "https://resa-prod.compagnie-vendeenne.com/ws/?&func=liste_horaires&num_voyage=0&num_passage=0");
    }

    @Override
    public void onPreExecute() {
    }

    @Override
    public void doInBackground() { }

    @Override
    public void onPostExecute(String response) {
        try {
            JSONObject json = new JSONObject(response);

            JSONObject ycJson = json.getJSONObject("yc");
            if (ycJson.has("liste_horaires")){
                JSONArray listeHorairesYc = ycJson.getJSONArray("liste_horaires");
                for (int i = 0 ; i < listeHorairesYc.length() ; i++) {
                    ArrayList arrayListYc = new ArrayList();
                    arrayListYc.add(0, "YC");
                    arrayListYc.add(1, listeHorairesYc.getJSONObject(i).getString("heure_depart"));
                    arrayListYc.add(2, listeHorairesYc.getJSONObject(i).getString("places_dispos"));
                    arrayListYc.add(3, listeHorairesYc.getJSONObject(i).getString("texte_dispo"));
                    this.listNbPlacesRestantes.add(arrayListYc);
                }
            }
            JSONObject vendeenneJson = json.getJSONObject("vendeenne");

            if (vendeenneJson.has("liste_horaires")){
                JSONArray listeHorairesVendeenne = vendeenneJson.getJSONArray("liste_horaires");
                for (int i = 0 ; i < listeHorairesVendeenne.length() ; i++) {
                    JSONObject jsonTraversee = listeHorairesVendeenne.getJSONObject(i);
                    if (!jsonTraversee.getString("heure_depart").isEmpty()){
                        ArrayList arrayListVendeenne = new ArrayList();
                        arrayListVendeenne.add(0, "Vendeenne");
                        arrayListVendeenne.add(1, jsonTraversee.getString("heure_depart"));
                        arrayListVendeenne.add(2, jsonTraversee.getString("places_dispos"));
                        arrayListVendeenne.add(3, jsonTraversee.getString("texte_dispo"));
                        this.listNbPlacesRestantes.add(arrayListVendeenne);
                    }
                }
            }

            ArrayList<Traversee> listeTraversees = traverseesControleur.getTraverseesParJour(dateTraversees, trajetSouhaite);
            Log.e("COUCOU", this.listNbPlacesRestantes.size() + "");
            for (ArrayList<String> arrayListNbPlacesRestantes : this.listNbPlacesRestantes) {
                String heureTraversee = arrayListNbPlacesRestantes.get(1);
                Log.e("COUCOU", arrayListNbPlacesRestantes.toString());
                if (heureTraversee.length() % 2 == 1) {
                    heureTraversee = "0" + heureTraversee.charAt(0) + ":" + heureTraversee.substring(1, 3);
                } else {
                    heureTraversee = heureTraversee.substring(0, 2) + ":" + heureTraversee.substring(2, 4);
                }
                Date dateNbPlacesRestantes = sdfDateHeure.parse(sdfDateTexte.format(dateTraversees) + " " + heureTraversee);
                for (Traversee traversee : listeTraversees) {
                    if (traversee.getDatePassage().equals(dateNbPlacesRestantes)){
                        if (traversee.getTypeBateau().equals(arrayListNbPlacesRestantes.get(0))){ //if vendeenne
                            if (arrayListNbPlacesRestantes.get(3).contains("Réservation fermée")){
                                traversee.setMessageDispo("Réservations fermées");
                            } else {
                                if (arrayListNbPlacesRestantes.get(3).contains("COMPLET") || Integer.parseInt(arrayListNbPlacesRestantes.get(2)) == 0){
                                    traversee.setMessageDispo("COMPLET !");
                                } else {
                                    traversee.setMessageDispo(Math.abs(Integer.parseInt(arrayListNbPlacesRestantes.get(2))) + " places restantes");
                                }
                            }
                        } else { //if YC
                            if (arrayListNbPlacesRestantes.get(0).equals("YC") && (traversee.getTypeBateau().equals("Catamaran") || traversee.getTypeBateau().equals("Insula"))){
                                if (arrayListNbPlacesRestantes.get(3).contains("Réservation fermée")){
                                    String[] splitPlacesRestantes = arrayListNbPlacesRestantes.get(3).split(" ");
                                    if (Integer.parseInt(splitPlacesRestantes[0]) == 0) {
                                        traversee.setMessageDispo("COMPLET !");
                                    } else {
                                        Log.e("COUCOUUUU", splitPlacesRestantes[0]);
                                        traversee.setMessageDispo(splitPlacesRestantes[0] + " places restantes");
                                    }
                                } else {
                                    if (arrayListNbPlacesRestantes.get(3).contains("complète") || Integer.parseInt(arrayListNbPlacesRestantes.get(2)) == 0){
                                        traversee.setMessageDispo("COMPLET !");
                                    } else {
                                        traversee.setMessageDispo(Math.abs(Integer.parseInt(arrayListNbPlacesRestantes.get(2))) + " places restantes");
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Collections.sort(listeTraversees);
            listview.setAdapter(new TraverseesAdapter(TraverseesActivity.this, listeTraversees));
        } catch (JSONException e) {
            Log.e("TraverseesActivity", e.toString());
        } catch (ParseException e) {
            Log.e("TraverseesActivity", e.toString());
        }
    }
}
