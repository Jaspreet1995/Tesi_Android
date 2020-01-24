package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import java.security.NoSuchAlgorithmException;

public class login extends AppCompatActivity implements View.OnClickListener {

    TextView username;
    TextView password;
    String user;
    String password_hash;
    SharedPreferences pref;
    public static final String MyPREFERENCES = "MyPrefs" ;

    Button login;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        login = (Button) findViewById(R.id.button_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        try{
            password_hash = HashFunction.toSha256(password.getText().toString());
        }catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        user = username.getText().toString();
        //Log.d("aia", password_hash);
        Accesso_Database p = new Accesso_Database("authenticate&useremail=" + user +
                "&password=" + password_hash );

        String result =  p.finale;
        SharedPreferences.Editor editor = pref.edit();
        if(result.endsWith("LOGIN OK}")){

            //l'utente è presente nel DB quindi carico i dati in sessione
            editor.putString("user", user);
            editor.putString("password", password_hash);
            editor.commit();

            //controllo se è un paziente o un dottore
            p = new Accesso_Database("authenticateDoctorNameSurname&useremail=" + user +
                    "&password=" + password_hash + "&idapp=" + "AcuityTest" );
            //Log.d("aia", p.finale.substring(18,26).equals("login_ok"));

            if(p.finale.contains("login_ok")){
                //è un dottore, vado alla Pazienti
                editor.putString("DOC", "1");
                editor.commit();
                startActivity(new Intent(login.this, Pazienti.class));
            }else{
                //è un paziente reg, vado a pag 2
                editor.putString("DOC", "0");
                editor.commit();
                startActivity(new Intent(login.this, pag_2.class));
            }
        }else{
            editor.putString("user", null);
            editor.putString("password", null);
            editor.commit();

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage("Login fallito. Controllate le credenziali e riprovate.");
            alertDialog.create();
            alertDialog.show();
        }

    }
}