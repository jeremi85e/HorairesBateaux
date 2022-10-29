package com.example.horairesbateaux.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.horairesbateaux.Adapters.TraverseesAdapter;
import com.example.horairesbateaux.Controllers.MesTraverseesControleur;
import com.example.horairesbateaux.Controllers.TrajetControleur;
import com.example.horairesbateaux.Controllers.TraverseesControleur;
import com.example.horairesbateaux.Models.Trajet;
import com.example.horairesbateaux.Models.Traversee;
import com.example.horairesbateaux.R;
import com.example.horairesbateaux.Utils.OnSwipeTouchListener;
import com.example.horairesbateaux.Utils.TraverseesAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class TraverseesActivity extends AppCompatActivity implements TraverseesAsyncTask.Listeners {

    @BindView(R.id.boutonRetour) Button boutonRetour;
    @BindView(R.id.boutonJourPrecedent) ImageButton boutonJourPrecedent;
    @BindView(R.id.boutonJourSuivant) ImageButton boutonJourSuivant;
    @BindView(R.id.listviewTraversees) ListView listeViewTraversees;
    @BindView(R.id.textViewTraversees) TextView textViewTraversees;
    @BindView(R.id.textViewListeVide) TextView textViewListeVide;

    Trajet trajetSouhaite;
    TraverseesControleur traverseesControleur;
    TrajetControleur trajetControleur;
    MesTraverseesControleur mesTraverseesControleur;
    Date dateTraversees;
    ArrayList<ArrayList> listNbPlacesRestantes = new ArrayList<>();
    SimpleDateFormat sdfTextViewTraversees = new SimpleDateFormat("EEE dd/MM/yyyy", Locale.FRENCH);
    SimpleDateFormat sdfDateCollee = new SimpleDateFormat("yyyyMMdd", Locale.FRENCH);
    SimpleDateFormat sdfDateTexte = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
    SimpleDateFormat sdfHeureTexte = new SimpleDateFormat("HH:mm", Locale.FRENCH);
    SimpleDateFormat sdfDateHeure = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRENCH);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.traversees_activity);

        ButterKnife.bind(this);

        this.traverseesControleur = new TraverseesControleur(this);
        this.mesTraverseesControleur = new MesTraverseesControleur(this);
        this.trajetControleur = new TrajetControleur(this);

        //Récupération de l'intent
        Intent intent = getIntent();
        this.trajetSouhaite = intent.getParcelableExtra("trajet");
        this.dateTraversees = new Date(intent.getLongExtra("date", 1));

        this.rechercheTraversees();

        listeViewTraversees.setOnTouchListener(new OnSwipeTouchListener(TraverseesActivity.this) {
            public void onSwipeRight() {
                clickedOnBoutonJourPrecedent();
            }
            public void onSwipeLeft() {
                clickedOnBoutonJourSuivant();
            }
        });

    }

    private void rechercheTraversees() {
        StringBuilder dateTexte = new StringBuilder(this.sdfTextViewTraversees.format(this.dateTraversees));
        dateTexte.setCharAt(0, Character.toUpperCase(dateTexte.charAt(0)));
        this.textViewTraversees.setText(this.trajetSouhaite.toString() + " " + dateTexte);

        ArrayList<Traversee> listeTraversees = this.traverseesControleur.getTraverseesParJour(this.dateTraversees, this.trajetSouhaite);
        Collections.sort(listeTraversees);
        this.listeViewTraversees.setAdapter(new TraverseesAdapter(TraverseesActivity.this, listeTraversees));

        if (listeTraversees.isEmpty())  {
            this.textViewListeVide.setText(getString(R.string.listeTraverseesVide));
        } else {
            this.textViewListeVide.setText("");
        }

        this.executeHttpRequests();
    }

    @OnClick(R.id.boutonJourPrecedent)
    public void clickedOnBoutonJourPrecedent() {
        this.ajoutJours(-1);
        this.listNbPlacesRestantes = new ArrayList<>();
        this.rechercheTraversees();
    }

    @OnClick(R.id.boutonJourSuivant)
    public void clickedOnBoutonJourSuivant() {
        this.ajoutJours(1);
        this.listNbPlacesRestantes = new ArrayList<>();
        this.rechercheTraversees();
    }

    @OnClick(R.id.textViewTraversees)
    public void clickedOnChangeTraversee() {
        if (this.trajetSouhaite.getId() % 2 == 0){
            this.trajetSouhaite = this.trajetControleur.getTrajet(this.trajetSouhaite.getId() - 1);
        } else {
            this.trajetSouhaite = this.trajetControleur.getTrajet(this.trajetSouhaite.getId() + 1);
        }
        this.listNbPlacesRestantes = new ArrayList<>();
        this.rechercheTraversees();
    }

    @OnClick(R.id.boutonRetour)
    public void clickedOnBoutonRetour() {
        finish();
    }

    private void ajoutJours(int nbJours) {
        Calendar c = Calendar.getInstance();
        c.setTime(this.dateTraversees);
        c.add(Calendar.DATE, nbJours);
        this.dateTraversees = c.getTime();
    }

    @OnItemClick(R.id.listviewTraversees)
    public void onItemSelected(int position) {
        Traversee traverseeSelectionnee = (Traversee) this.listeViewTraversees.getItemAtPosition(position);
        creerModaleAjout(traverseeSelectionnee);
    }

    public void creerModaleAjout(Traversee traversee) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TraverseesActivity.this);

        alertDialogBuilder.setTitle(getString(R.string.myTraversees));
        alertDialogBuilder.setMessage(getString(R.string.insertMaTraverseeQuestion, traversee.getTrajet(), sdfDateTexte.format(traversee.getDatePassage()), sdfHeureTexte.format(traversee.getDatePassage())));

        alertDialogBuilder.setPositiveButton(getString(R.string.oui),new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                insertTraversee(traversee);
            }
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.non),new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                dialog.cancel();
            }
        });

        //create dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void insertTraversee(Traversee traversee) {
        // TODO : Ajouter dans la base de données !
        long nombreSuppr = this.mesTraverseesControleur.insertMaTraversee(traversee);
        Toast.makeText(TraverseesActivity.this, getString(R.string.traverseeAjoutee), Toast.LENGTH_LONG).show();
    }

    // ------------------
    //  HTTP REQUEST
    // ------------------

    private void executeHttpRequests(){
        new TraverseesAsyncTask(this).execute(
                "https://resa3.yeu-continent.fr/ws/?&func=set_id_voyage&num_voyage=0&id_voyage=" + this.trajetControleur.getIdVoyagesNbPlacesYC(this.trajetSouhaite),
                "https://resa3.yeu-continent.fr/ws/?&func=liste_dates&num_voyage=0",
                "https://resa3.yeu-continent.fr/ws/?&func=set_date_depart&num_voyage=0&num_passage=0&date_depart=" + this.sdfDateCollee.format(this.dateTraversees),
                "https://resa3.yeu-continent.fr/ws/?&func=liste_horaires&num_voyage=0&num_passage=0",
                "https://resa-prod.compagnie-vendeenne.com/ws/?&func=set_id_voyage&num_voyage=0&id_voyage=" + this.trajetControleur.getIdVoyagesNbPlacesVendeenne(this.trajetSouhaite),
                "https://resa-prod.compagnie-vendeenne.com/ws/?&func=liste_dates&num_voyage=0",
                "https://resa-prod.compagnie-vendeenne.com/ws/?&func=set_date_depart&num_voyage=0&num_passage=0&date_depart=" + this.sdfDateCollee.format(this.dateTraversees),
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

            ArrayList<Traversee> listeTraversees = this.traverseesControleur.getTraverseesParJour(this.dateTraversees, this.trajetSouhaite);
            for (ArrayList<String> arrayListNbPlacesRestantes : this.listNbPlacesRestantes) {
                String heureTraversee = arrayListNbPlacesRestantes.get(1);
                if (!heureTraversee.equals("")) {
                    textViewListeVide.setText("");
                    if (heureTraversee.length() % 2 == 1) {
                        heureTraversee = "0" + heureTraversee.charAt(0) + ":" + heureTraversee.substring(1, 3);
                    } else {
                        heureTraversee = heureTraversee.substring(0, 2) + ":" + heureTraversee.substring(2, 4);
                    }
                    Date dateNbPlacesRestantes = this.sdfDateHeure.parse(this.sdfDateTexte.format(this.dateTraversees) + " " + heureTraversee);
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
            }
            for (Traversee traversee : listeTraversees) {
                if (traversee.getMessageDispo().equals("")) {
                    traversee.setMessageDispo("ANNULÉ !");
                }
            }
            Collections.sort(listeTraversees);
            listeViewTraversees.setAdapter(new TraverseesAdapter(TraverseesActivity.this, listeTraversees));
        } catch (JSONException e) {
            Log.e("TraverseesActivity", e.toString());
        } catch (ParseException e) {
            Log.e("TraverseesActivity", e.toString());
        }
    }
}
