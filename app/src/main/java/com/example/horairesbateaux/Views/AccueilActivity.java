package com.example.horairesbateaux.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.horairesbateaux.Controllers.MesTraverseesControleur;
import com.example.horairesbateaux.Controllers.TrajetControleur;
import com.example.horairesbateaux.Controllers.TraverseesControleur;
import com.example.horairesbateaux.Models.Trajet;
import com.example.horairesbateaux.Models.Traversee;
import com.example.horairesbateaux.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccueilActivity extends AppCompatActivity {

    @BindView(R.id.boutonRecherche) Button boutonRecherche;
    @BindView(R.id.spinner_trajets) Spinner spinnerTrajets;
    @BindView(R.id.editDate) EditText editDate;

    TraverseesControleur traverseesControleur;
    TrajetControleur trajetControleur;
    MesTraverseesControleur mesTraverseesControleur;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.accueil_activity);

        ButterKnife.bind(this);

        Date dateDuJour = new Date();
        traverseesControleur = new TraverseesControleur(this);
        trajetControleur = new TrajetControleur(this);
        mesTraverseesControleur = new MesTraverseesControleur(this);

        // Supprimer mesTraversees passées
        this.deleteMesTraverseesPassees();

        spinnerTrajets.setAdapter(new ArrayAdapter<>(this, R.layout.trajets_spinner_item, trajetControleur.getTrajets()));

        editDate.setText(simpleDateFormat.format(dateDuJour));
    }

    @OnClick(R.id.editDate)
    public void clickedOnSelectTrajet() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        DatePickerDialog picker = new DatePickerDialog(AccueilActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editDate.setText(new DecimalFormat("00").format(dayOfMonth) + "/" + new DecimalFormat("00").format(monthOfYear + 1) + "/" + year);
            }
        }, year, month, day);

        picker.show();
    }

    @OnClick(R.id.boutonRecherche)
    public void clickedOnBoutonRecheche() {
        try {
            Date dateSouhaitee = simpleDateFormat.parse(editDate.getText() + "");
            Trajet trajetSouhaite = (Trajet) spinnerTrajets.getSelectedItem();

            Intent intent = new Intent().setClass(AccueilActivity.this, TraverseesActivity.class);
            intent.putExtra("trajet", trajetSouhaite);
            intent.putExtra("date", dateSouhaitee.getTime());

            startActivity(intent);
        } catch (ParseException e){
            Log.e("Parse Exception", e.toString());
        }
    }

    @OnClick(R.id.logoAppli)
    public void clickedOnLogoAppli() {
        Intent intent = new Intent().setClass(AccueilActivity.this, MesTraverseesActivity.class);
        startActivity(intent);
    }

    public void deleteMesTraverseesPassees() {
        ArrayList<Traversee> mesTraversees = this.mesTraverseesControleur.getMesTraversees();
        for (Traversee traversee : mesTraversees) {
            if (traversee.getDatePassage().before(new Date())) {
                this.mesTraverseesControleur.deleteMaTraversee(traversee);
            }
        }
    }
}