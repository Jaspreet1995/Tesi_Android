package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.sql.Timestamp;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class risultato_test extends Activity implements View.OnClickListener {

    String risultato_dx;
    String risultato_sx;
    double somma_finale;
    double somma_dx;
    double somma_sx;
    String risultato_test;
    String[] res_per_occhio_dx;
    String[] res_per_occhio_sx;
    String risultato;
    String immagine_vista;
    String descrizione_vista;
    ImageView ris;
    TextView title;
    TextView desc;
    Button home;

    String email;
    String password;
    String doc;
    String idPaziente;
    String username;
    String emailuser;


    Accesso_Database p;

    SharedPreferences pref;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.risultato_test);

        title = findViewById(R.id.res);
        ris = findViewById(R.id.image_ris);
        desc = findViewById(R.id.desc);
        home = findViewById(R.id.home);

        pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        editor = pref.edit();

        risultato_dx = pref.getString("risultato_dx",null);
        risultato_sx = pref.getString("risultato_sx",null);

        Log.v(TAG, "RIS_DX" + risultato_dx);
        Log.v(TAG, "RIS_SX" + risultato_sx);

        res_per_occhio_dx = risultato_dx.split("-");
        res_per_occhio_sx = risultato_sx.split("-");

        somma_dx=calcolaRisultato(res_per_occhio_dx);
        somma_sx=calcolaRisultato(res_per_occhio_sx);
        somma_finale=(somma_dx+somma_sx)/2;
        risultato = "dx:" + risultato_dx + "/sx:" + risultato_sx;
        risultato_test = risultato + "/res_dx:" + somma_dx + "/res_sx:" + somma_sx + "/res:" + somma_finale;

        Log.v(TAG, "SOMMMMMMA" + somma_finale);
        Log.v(TAG, "RISULTATO FINALISSIMO:" + risultato_test);

        int valore_vista = valutaVista(somma_finale);
        int valore_vista_dx = valutaVista(somma_dx);
        int valore_vista_sx = valutaVista(somma_sx);

        //immagine_vista = getImmagine(valore_vista);
        setImmagine(valore_vista);
        descrizione_vista = getDescrizioneVista(valore_vista, valore_vista_dx, valore_vista_sx);
        desc.setText(descrizione_vista);
        salvataggio();


    }

    @Override
    public void onClick(View view) {




    }

    public double calcolaRisultato(String[] risultato) {

        double somma = 0;
        double[] pesi = { 2.5, 2.5, 4.5, 4.5, 6.5, 6.5, 9, 9, 12, 12, 15.5, 15.5 };

        for (int i = 0; i < risultato.length; i++) {

            if (risultato[i].compareTo("0") != 0) {
                somma = somma + pesi[i];
            }

        }

        return somma;

    }

    public int valutaVista(double somma) {

        if (somma <= 33.3) {
            // VISTA SCADENTE
            return 1;
        } else if (somma <= 66.6) {
            // VISTA MEDIA
            return 2;
        } else {
            // VISTA BUONA
            return 3;
        }

    }


    public void setImmagine(int valore) {
        // classifico i vari risultati in base ai punteggi e quindi attribuisco immagini
        // e descrizioni correlate
        if (valore == 1) {
            // VISTA SCADENTE
            ris.setImageResource(R.drawable.unhappy_face);
        } else if (valore == 2) {
            // VISTA MEDIA
            ris.setImageResource(R.drawable.medium_face);
        } else {
            // VISTA BUONA
            ris.setImageResource(R.drawable.happy_face);
        }
    }


    public String getDescrizioneVista(int i, int dx, int sx) {
        String descr = "";

        if (i == 1) {
            // vista scadente

            if (dx > sx) {
                // il destro è migliore del sinistro
                descr = "You seem to have difficulties recognising small characters with your left eye.\r\n" + "\r\n"
                        + "We recommend you have a vision exam with an eye care professional.";
            } else if (dx < sx) {
                // il sinistro è migliore del destro
                descr = "You seem to have difficulties recognising small characters with your right eye.\r\n" + "\r\n"
                        + "We recommend you have a vision exam with an eye care professional.";
            } else {
                descr = "You seem to have difficulties recognising small characters with all of your eyes.\r\n" + "\r\n"
                        + "We recommend you have a vision exam with an eye care professional.";
            }

        } else if (i == 2) {
            descr = "You seem to have difficulties recognising small characters with one of your eyes.\r\n" + "\r\n"
                    + "We recommend you have a vision exam with an eye care professional.";
        } else {
            descr = "Congratulations, your visual acuity seems good in both eyes.\r\n"
                    + "You can read even small print with ease.\r\n"
                    + "Feel free to redo this test regularly to monitor your vision. However, to verify the health of your eyes, don't hesitate to fix an appointment with an eye care professional.\r\n"
                    + "";
        }

        return descr;
    }


    public void salvataggio() {

        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());

        String save_session = pref.getString("stato_salvataggio", null);
        doc = pref.getString("DOC", null);
        email = pref.getString("user", null);
        username = pref.getString("username", null);
        password = pref.getString("password", null);
        emailuser = pref.getString("emailUser", null);
        idPaziente = pref.getString("idPaziente", null);

        int salvato;

        //se il risultato è già stato salvato, non salvo
        if (save_session != "si") {

            // il salvataggio dipende se viene effettuato dall'account del dottore o del
            // paziente stesso
            if (doc == "0") {
                // siamo nel caso in cui il salvataggio viene fatto dal profilo del paziente
                // direttamente
                p = new Accesso_Database("storeresults&username=" + username + "&useremail=" +
                        email + "&password=" + password + "&idapp=" + "AcuityTest" +
                        "&result=" + risultato_test);
                //salvato = conn.saveResults(username, email, "AcuityTest", ts, risultato_test);
                editor.putString("salvato", "si");
                Log.v(TAG, "SALVATOOO:");

            } else {

                //sto accedendo con il profilo del paziente
                if (idPaziente.compareTo("null") == 0) {
                    //se è non registrato

                    String prova = "storeuserresfromdoc&idapp=" + "AcuityTest" + "&username=" +
                            username + "&docemail=" + email + "&useremail=" + emailuser + "&result="
                            + risultato_test + "&password=" + password;

                   // String prova2 = prova.replace(" ","%20");


                    p = new Accesso_Database(prova);
                    // salvato = conn.storeResultsFromDoc(email, password, username, emailuser, "AcuityTest", ts, risultato_test);
                    editor.putString("salvato", "si");
                    Log.v(TAG, "SALVATOOO:" + prova);
                } else {
                    //se è registrato
                    String prova = "storeresnotregistered&idapp=" + "AcuityTest" + "&docemail=" +
                            email + "&result=" + risultato_test+ "&password=" + password +
                            "&idpatient=" + idPaziente;

                    //String prova2= prova.replace(" ","%20");

                    p = new Accesso_Database(prova);
                    //salvato = conn.storeResultsNotRegistered(email, password, idPaziente, "AcuityTest", ts, risultato_test);
                    editor.putString("salvato", "si");
                    Log.v(TAG, "SALVATOOO:" + prova);

                }
            }

        }

    }
}
