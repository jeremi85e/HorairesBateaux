package com.example.horairesbateaux.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.horairesbateaux.Adapters.MesTraverseesAdapter;
import com.example.horairesbateaux.Controllers.MesTraverseesControleur;
import com.example.horairesbateaux.Controllers.TrajetControleur;
import com.example.horairesbateaux.Models.Traversee;
import com.example.horairesbateaux.R;
import com.example.horairesbateaux.Utils.MesTraverseesAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

public class MesTraverseesActivity extends AppCompatActivity implements MesTraverseesAsyncTask.Listeners {

    @BindView(R.id.textViewMesTraverseesListeVide) TextView textViewListeVide;
    @BindView(R.id.listviewMesTraversees) ListView listeViewMesTraversees;

    SimpleDateFormat sdfDateTexte = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
    SimpleDateFormat sdfDateCollee = new SimpleDateFormat("yyyyMMdd", Locale.FRENCH);
    SimpleDateFormat sdfHeureTexte = new SimpleDateFormat("HH:mm", Locale.FRENCH);
    SimpleDateFormat sdfPetiteHeureTexte = new SimpleDateFormat("Hmm", Locale.FRENCH);

    private ArrayList<Traversee> listeMesTraversees = new ArrayList<>();
    private MesTraverseesControleur mesTraverseesControleur;
    private TrajetControleur trajetControleur;
    private Traversee traverseeSelectionnee;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mes_traversees);

        ButterKnife.bind(this);

        this.mesTraverseesControleur = new MesTraverseesControleur(this);
        this.trajetControleur = new TrajetControleur(this);

        this.listeMesTraversees = this.mesTraverseesControleur.getMesTraversees();
        Collections.sort(this.listeMesTraversees);
        this.listeViewMesTraversees.setAdapter(new MesTraverseesAdapter(MesTraverseesActivity.this, this.listeMesTraversees));

        if (this.listeMesTraversees.isEmpty())  {
            this.textViewListeVide.setText(getString(R.string.listeMyTraverseesVide));
        } else {
            this.textViewListeVide.setText("");
        }
    }

    @OnClick(R.id.boutonMesTraverseesRetour)
    public void clickedOnBoutonRetour() {
        finish();
    }

    @OnItemClick(R.id.listviewMesTraversees)
    public void onItemClickSelected(int position) {
        Traversee traverseeSelectionnee = (Traversee) listeViewMesTraversees.getItemAtPosition(position);
        creerModaleSuppression(traverseeSelectionnee);
    }

    @OnItemLongClick(R.id.listviewMesTraversees)
    public void onItemLongClickSelected(int position) {
        this.traverseeSelectionnee = (Traversee) listeViewMesTraversees.getItemAtPosition(position);
        this.creerModaleRecherchePlacesRestantes(traverseeSelectionnee);
    }

    public void creerModaleSuppression(Traversee traversee) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MesTraverseesActivity.this);

        alertDialogBuilder.setTitle(getString(R.string.myTraversees));
        alertDialogBuilder.setMessage(getString(R.string.deleteMaTraverseeQuestion, traversee.getTrajet(), sdfDateTexte.format(traversee.getDatePassage()), sdfHeureTexte.format(traversee.getDatePassage())));

        alertDialogBuilder.setPositiveButton(getString(R.string.oui),new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteTraversee(traversee);
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

    public void deleteTraversee(Traversee traversee) {
        int nombreSuppr = this.mesTraverseesControleur.deleteMaTraversee(traversee);
        if (nombreSuppr == 1) {
            this.listeMesTraversees.remove(traversee);
            this.listeViewMesTraversees.setAdapter(new MesTraverseesAdapter(MesTraverseesActivity.this, this.listeMesTraversees));
            Toast.makeText(MesTraverseesActivity.this, getString(R.string.traverseeSupprimee), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MesTraverseesActivity.this, getString(R.string.traverseePasSupprimee), Toast.LENGTH_LONG).show();
        }
    }

    public void creerModaleRecherchePlacesRestantes(Traversee traversee) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MesTraverseesActivity.this);

        alertDialogBuilder.setTitle(getString(R.string.myTraversees));
        alertDialogBuilder.setMessage(getString(R.string.recherche));

        this.alertDialog = alertDialogBuilder.create();
        this.alertDialog.show();

        this.executeHttpRequests();
    }

    // ------------------
    //  HTTP REQUEST
    // ------------------

    private void executeHttpRequests(){
        if (traverseeSelectionnee.getTypeBateau() != "Vendeenne") {
            new MesTraverseesAsyncTask(this).execute(
                "https://resa3.yeu-continent.fr/ws/?&func=set_id_voyage&num_voyage=0&id_voyage=" + this.trajetControleur.getIdVoyagesNbPlacesYC(this.traverseeSelectionnee.getTrajet()),
                "https://resa3.yeu-continent.fr/ws/?&func=liste_dates&num_voyage=0",
                "https://resa3.yeu-continent.fr/ws/?&func=set_date_depart&num_voyage=0&num_passage=0&date_depart=" + this.sdfDateCollee.format(this.traverseeSelectionnee.getDatePassage()),
                "https://resa3.yeu-continent.fr/ws/?&func=liste_horaires&num_voyage=0&num_passage=0"
            );
        } else {
            new MesTraverseesAsyncTask(this).execute(
                "https://resa-prod.compagnie-vendeenne.com/ws/?&func=set_id_voyage&num_voyage=0&id_voyage=" + this.trajetControleur.getIdVoyagesNbPlacesVendeenne(this.traverseeSelectionnee.getTrajet()),
                "https://resa-prod.compagnie-vendeenne.com/ws/?&func=liste_dates&num_voyage=0",
                "https://resa-prod.compagnie-vendeenne.com/ws/?&func=set_date_depart&num_voyage=0&num_passage=0&date_depart=" + this.sdfDateCollee.format(this.traverseeSelectionnee.getDatePassage()),
                "https://resa-prod.compagnie-vendeenne.com/ws/?&func=liste_horaires&num_voyage=0&num_passage=0"
            );
        }
    }

    @Override
    public void onPreExecute() {
    }

    @Override
    public void doInBackground() { }

    @Override
    public void onPostExecute(String response) {
        try {
            String placesRestantes;

            JSONObject json = new JSONObject(response);

            if (json.has("liste_horaires")){
                JSONArray listeHoraires = json.getJSONArray("liste_horaires");
                for (int i = 0 ; i < listeHoraires.length() ; i++) {
                    JSONObject jsonTraversee = listeHoraires.getJSONObject(i);
                    if (!jsonTraversee.getString("heure_depart").isEmpty()){
                        if (sdfPetiteHeureTexte.format(this.traverseeSelectionnee.getDatePassage()).equals(jsonTraversee.getString("heure_depart"))){
                            placesRestantes = getPlacesRestantesByTraversee(jsonTraversee);
                            this.alertDialog.cancel();
                            this.creerModalePlacesRestantes(placesRestantes);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("MesTraverseesActivity", e.toString());
        }
    }

    public void creerModalePlacesRestantes(String placesRestantes) {
        AlertDialog.Builder alertDialogBuilderPlaces = new AlertDialog.Builder(MesTraverseesActivity.this);

        alertDialogBuilderPlaces.setTitle(getString(R.string.myTraversees));
        alertDialogBuilderPlaces.setMessage(getString(R.string.placesRestantesTraversee, placesRestantes));

        AlertDialog alertDialogPlaces = alertDialogBuilderPlaces.create();
        alertDialogPlaces.show();
    }

    public String getPlacesRestantesByTraversee(JSONObject jsonTraversee) {
        String placesRestantes = "";
        try {
            if (traverseeSelectionnee.getTypeBateau() != "Vendeenne") {
                if (jsonTraversee.getString("texte_dispo").contains("Réservation fermée")){
                    String[] splitPlacesRestantes = jsonTraversee.getString("texte_dispo").split(" ");
                    if (Integer.parseInt(splitPlacesRestantes[0]) == 0) {
                        placesRestantes = "COMPLET !";
                    } else {
                        placesRestantes = splitPlacesRestantes[0] + " places restantes";
                    }
                } else {
                    if (jsonTraversee.getString("texte_dispo").contains("complète") || Integer.parseInt(jsonTraversee.getString("places_dispos")) == 0){
                        placesRestantes = "COMPLET !";
                    } else {
                        placesRestantes = Math.abs(Integer.parseInt(jsonTraversee.getString("places_dispos"))) + " places restantes";
                    }
                }
            } else {
                if (jsonTraversee.getString("texte_dispo").contains("Réservation fermée")){
                    placesRestantes = "Réservations fermées";
                } else {
                    if (jsonTraversee.getString("texte_dispo").contains("COMPLET") || Integer.parseInt(jsonTraversee.getString("places_dispos")) == 0){
                        placesRestantes = "COMPLET !";
                    } else {
                        placesRestantes = Math.abs(Integer.parseInt(jsonTraversee.getString("places_dispos"))) + " places restantes";
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("MesTraverseesActivity", e.toString());
        }

        return placesRestantes;
    }
}
