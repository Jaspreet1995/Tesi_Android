package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Pazienti extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences pref;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences.Editor editor;

    ListView listBox;
    Accesso_Database p;
    String ListaUtenti;
    String[] utenti;
    Object clickItemObj;

    String user;
    String password;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pazienti);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);

        pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        editor = pref.edit();

        listBox = findViewById(R.id.listBox);

        user = pref.getString("user",null);
        password = pref.getString("password",null);

        p = new Accesso_Database("getListPatientDoc&useremail=" + user + "&password=" + password +
                "&idapp=" + "AcuityTest" );

        ListaUtenti = p.finale;

        utenti = ListaUtenti.substring(ListaUtenti.indexOf("{")+1,ListaUtenti.indexOf("}")).split((","));
        init(utenti);

    }


    public void init(String[] utenti) {

        String[] singoli_dati;
        TableLayout table = (TableLayout) findViewById(R.id.Table);


        for (int i = 0; i < utenti.length; i++) {
            singoli_dati = utenti[i].split(" ");

            final TableRow row1 = new TableRow(this);

            TextView tv0 = new TextView(this);
            TextView tv1 = new TextView(this);
            TextView tv2 = new TextView(this);
            TextView tv3 = new TextView(this);
            TextView tv4 = new TextView(this);
            TextView tv5 = new TextView(this);
            if(i%2==0){
                row1.setBackgroundColor(Color.parseColor("#f3f5f7"));
            }
            tv0.setText(singoli_dati[2]);
            tv1.setText(singoli_dati[3]);
            tv2.setText(singoli_dati[4]);
            tv3.setText(singoli_dati[5]);
            tv4.setText(singoli_dati[1]);
            tv5.setText(singoli_dati[0]);
            tv5.setVisibility(View.INVISIBLE);
            row1.addView(tv0);
            row1.addView(tv1);
            row1.addView(tv2);
            row1.addView(tv3);
            row1.addView(tv4);
            row1.addView(tv5);
            row1.setClickable(true);
            row1.setId(i);
            table.addView(row1);

            row1.setOnClickListener(onClickListener);
        }
    }
    private View.OnClickListener onClickListener= new View.OnClickListener() {
        public void onClick(View v) {
            Log.d("url", "l'id è: " + v.getId());

            String[] singoli_dati = utenti[v.getId()].split(" ");
            String idPaziente = singoli_dati[0];
            String emailUser = singoli_dati[5];
            String username = singoli_dati[1];

            editor.putString("idPaziente", idPaziente);
            editor.putString("emailUser", emailUser);
            editor.putString("username", username);
            editor.commit();

            startActivity(new Intent(Pazienti.this, Profilo_paziente.class));

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            TypedValue tv = new TypedValue();
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());


            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.nav_doctor, null, false),width,height, true);

            pw.showAtLocation(this.findViewById(R.id.main), 0,0, 0);


            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        SharedPreferences.Editor editor = pref.edit();
        int id= v.getId();
        switch(id)
        {
            case R.id.menu_aggiungi_utente:
                startActivity(new Intent(this, aggiungi_utente.class));
                Log.d("si","click1");
                break;

            case R.id.menu_home:
                startActivity(new Intent(this, Pazienti.class));
                break;

            case R.id.menu_logout:
                editor.clear();
                editor.commit();
                startActivity(new Intent(this, login.class));
                break;
        }
    }
}