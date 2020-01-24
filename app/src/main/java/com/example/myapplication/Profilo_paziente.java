package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Profilo_paziente extends Activity implements View.OnClickListener {

    SharedPreferences pref;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences.Editor editor;

    ListView listBox;
    ImageView foto_profilo;
    Button test;
    Button user_informations;
    Button test_performed;
    LinearLayout temp = null;

    Accesso_Database p;
    String ListaRisultati;
    String listaRisultatiTest;
    String[] risultati;
    String[] singoloRisultato;
    Object clickItemObj;

    String idPaziente;
    String username;
    String emailUser;
    String email;
    String password;

    String[] res_dx;
    String[] res_sx;
    String percentuale_dx;
    String percentuale_sx;
    String percentuale_test;

    String nome_utente;
    TextView utente;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.profilo_paziente);
        pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        editor = pref.edit();

        idPaziente = pref.getString("idPaziente", null);
        username = pref.getString("username", null);
        emailUser = pref.getString("emailUser", null);
        email = pref.getString("user", null);
        password = pref.getString("password", null);



        //controllo se è un paziente registrato o non registrato
        String user_type;
        //se l'id in sessione è null -> il paziente scelto è registrato
        if(idPaziente.compareTo("null")==0){
            user_type = "1";
            p = new Accesso_Database("getPatientInfo&patienttype=" + user_type + "&useremail=" + emailUser + "&username=" + username +
                    "&idapp=" + "AcuityTest");
        }else{
            user_type = "0";
            p = new Accesso_Database("getPatientInfo&patienttype=" + user_type + "&useremail=" + emailUser + "&username=" + idPaziente +
                    "&idapp=" + "AcuityTest");
        }


        nome_utente = p.finale.substring(p.finale.indexOf("{")+1, p.finale.indexOf("}"));

        utente = findViewById(R.id.User);
        utente.setText(nome_utente);

        //creo la lista dei test eseguiti
        listBox = findViewById(R.id.listBox);

        creaLista();

        test = findViewById(R.id.test);
        user_informations = findViewById(R.id.User_info);
        test_performed = findViewById(R.id.Test_performed);

        test.setOnClickListener(this);
        user_informations.setOnClickListener(this);
        test_performed.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.test:
                startActivity(new Intent(Profilo_paziente.this, tutorial.class));
                break;

            case R.id.User_info:
                creaAreaInfo();
                break;

            case R.id.Test_performed:
                showAreaTest();
                break;

            default:
                break;
        }


    }

    public void creaLista(){

        if (idPaziente.compareTo("null")==0){
            //paziente registrato
            p = new Accesso_Database("getResultList&docemail=" + email + "&password=" + password + "&username=" + username +
                    "&idpatient=" + idPaziente + "&idapp=" + "AcuityTest");
        }else{
            //paziente non registrato
            p = new Accesso_Database("getResultList&docemail=" + email + "&password=" + password + "&username=" + "null" +
                    "&idpatient=" + idPaziente + "&idapp=" + "AcuityTest");
        }


        ListaRisultati = p.finale;
        if(ListaRisultati.contains("status:Error")){
            //l'utente non ha ancora eseguito test
            TextView temp = findViewById(R.id.textView6);
            temp.setText("Nessun test eseguito");
            TableLayout table = (TableLayout) findViewById(R.id.table);
            table.setVisibility(View.INVISIBLE);
            //Log.d("prova", "nooo");
        }else{
            ListaRisultati = ListaRisultati.substring(ListaRisultati.indexOf("{")+1, ListaRisultati.indexOf("}"));
            ListaRisultati = ListaRisultati.replace("[","").replace("]","");
            risultati = ListaRisultati.split((","));

            //Log.d("prova", risultati[0]);

            final List<String> result_list = new ArrayList<String>(Arrays.asList(risultati));

            // Create an ArrayAdapter from List
            //final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, user_list);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, result_list);
            // DataBind ListView with items from ArrayAdapter
            listBox.setAdapter(arrayAdapter);

            listBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                    clickItemObj = adapterView.getAdapter().getItem(index);

                    String id_riga = clickItemObj.toString().split(" - ")[0];
                    creaTabellaRisultati(id_riga);
                }
            });
        }


    }

    public void creaTabellaRisultati(String id_riga){

        if (idPaziente.compareTo("null")==0){
            //paziente registrato
            p = new Accesso_Database("getResult&docemail=" + email + "&password=" + password + "&username=" + username +
                    "&idpatient=" + idPaziente + "&idriga=" + id_riga.trim());
        }else{
            //paziente non registrato
            p = new Accesso_Database("getResult&docemail=" + email + "&password=" + password + "&username=" + "null" +
                    "&idpatient=" + idPaziente + "&idriga=" + id_riga.trim());
        }


        listaRisultatiTest =  p.finale;
        //Log.d("prova", clickItemObj.toString());
        listaRisultatiTest = listaRisultatiTest.substring(listaRisultatiTest.indexOf("{")+1, listaRisultatiTest.indexOf("}"));
        singoloRisultato = listaRisultatiTest.split("/");

        TableLayout table = (TableLayout) findViewById(R.id.table);

        singoloRisultato[0] = singoloRisultato[0].substring(3,singoloRisultato[0].length());
        singoloRisultato[1] = singoloRisultato[1].substring(3,singoloRisultato[1].length());
        singoloRisultato[2] = singoloRisultato[2].substring(7,singoloRisultato[2].length());
        singoloRisultato[3] = singoloRisultato[3].substring(7,singoloRisultato[3].length());
        singoloRisultato[4] = singoloRisultato[4].substring(4,singoloRisultato[4].length());

        String riga;

        for (int i = 0; i <= singoloRisultato[0].split("-").length-1; i++) {

            if(i==0){
                riga = singoloRisultato[0].split("-")[i] + "/" + singoloRisultato[1].split("-")[i]
                        + "/" + singoloRisultato[2].split("-")[i]+ "/" + singoloRisultato[3].split("-")[i]
                        + "/" + singoloRisultato[4].split("-")[i];
            }else{
                riga = singoloRisultato[0].split("-")[i] + "/" + singoloRisultato[1].split("-")[i];
            }


            String[] singoli_dati = riga.split("/");

            final TableRow row1 = new TableRow(this);
            if(i==0){
                TextView tv0 = new TextView(this);
                TextView tv1 = new TextView(this);
                TextView tv2 = new TextView(this);
                TextView tv3 = new TextView(this);
                TextView tv4 = new TextView(this);
                if(i%2==0){
                    row1.setBackgroundColor(Color.parseColor("#f3f5f7"));
                }

                tv0.setText(singoli_dati[0]);
                tv1.setText(singoli_dati[1]);
                tv2.setText(singoli_dati[2]);
                tv3.setText(singoli_dati[3]);
                tv4.setText(singoli_dati[4]);
                row1.addView(tv0);
                row1.addView(tv1);
                row1.addView(tv2);
                row1.addView(tv3);
                row1.addView(tv4);
            }else{
                TextView tv0 = new TextView(this);
                TextView tv1 = new TextView(this);
                if(i%2==0){
                    row1.setBackgroundColor(Color.parseColor("#f3f5f7"));
                }

                tv0.setText(singoli_dati[0]);
                tv1.setText(singoli_dati[1]);
                row1.addView(tv0);
                row1.addView(tv1);
            }


            table.addView(row1);
        }

    }

    public void creaAreaInfo(){

        LinearLayout container_lista = findViewById(R.id.container_lista);
        LinearLayout container_info = findViewById(R.id.container_info);
        LinearLayout container_button = findViewById(R.id.button_container);



        ViewGroup parent = (ViewGroup) container_button.getParent();

        String s = parent.getChildAt(3).toString();
        s = s.substring(0,s.length()-1).split("/")[1];

        if(s.compareTo("container_info")==0){

        }else{
            temp = container_lista;
            parent.getChildAt(4).setVisibility(View.VISIBLE);
            parent.removeViewAt(3);
            //parent.addView(container_info,3);
            parent.addView(temp,4);
            parent.getChildAt(4).setVisibility(View.INVISIBLE);
        }

    }

    public void showAreaTest(){
        LinearLayout container_lista = findViewById(R.id.container_lista);
        LinearLayout container_info = findViewById(R.id.container_info);
        LinearLayout container_button = findViewById(R.id.button_container);

        ViewGroup parent = (ViewGroup) container_button.getParent();

        String s = parent.getChildAt(3).toString();
        s = s.substring(0,s.length()-1).split("/")[1];

        if(s.compareTo("container_lista")==0){

        }else{
            temp = container_info;
            parent.getChildAt(4).setVisibility(View.VISIBLE);
            parent.removeViewAt(3);
            parent.addView(temp,4);
            parent.getChildAt(4).setVisibility(View.INVISIBLE);
        }

    }
}
