package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.util.Random;

import static android.content.ContentValues.TAG;

public class test extends Activity implements View.OnClickListener {


    ImageView up;
    ImageView down;
    ImageView right;
    ImageView left;
    ImageView c_land;
    int orientamento;
    String posizione;
    String button_click;
    int step = 1;
    String occhio="dx";
    String stringa_risultati_dx = "";
    String stringa_risultati_sx = "";

    SharedPreferences pref;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test);

        pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        editor = pref.edit();


        up = (ImageView) findViewById(R.id.up);
        down = (ImageView) findViewById(R.id.down);
        right = (ImageView) findViewById(R.id.right);
        left = (ImageView) findViewById(R.id.left_1);
        c_land = (ImageView) findViewById(R.id.cland);


        up.setImageResource(R.drawable.up);
        down.setImageResource(R.drawable.up);
        right.setImageResource(R.drawable.up);
        left.setImageResource(R.drawable.up);
        c_land.setImageResource(R.drawable.clandolt);

        right.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {

                button_click = "dx";
                Log.v(TAG, " click_right");
                init(button_click);
            }
        });

        up.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {

                button_click = "top";
                Log.v(TAG, " click_up");
                init(button_click);
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {

                button_click = "sx";
                Log.v(TAG, " click_left");
                init(button_click);
            }
        });

        down.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {

                button_click = "bottom";
                Log.v(TAG, " click_down");
                init(button_click);
            }
        });


    }


    public void init(String button_click) {

        Log.v(TAG, " ruotate");
        Random rand1 = new Random();
        int rand = rand1.nextInt(4 - 0) + 0;
        orientamento = Math.round(c_land.getRotation());


        switch (orientamento) {
            case 0:
                posizione = "sx";
                Log.v(TAG, " sx");
                break;
            case 90:
                posizione = "top";
                Log.v(TAG, " top");
                break;
            case -90:
                posizione = "bottom";
                Log.v(TAG, " b");
                break;
            case 180:
                posizione = "dx";
                Log.v(TAG, " dx");
                break;
            case 360:
                posizione = "sx";
                Log.v(TAG, " sx");
                break;
        }

        switch (rand) {
            case 0:
                c_land.setRotation(90);
                Log.v(TAG, " 0");
                break;
            case 1:
                c_land.setRotation(-90);
                Log.v(TAG, " 1");
                break;
            case 2:
                c_land.setRotation(180);
                Log.v(TAG, " 2");
                break;
            case 3:
                c_land.setRotation(360);
                Log.v(TAG, " 3");
                break;
        }

        if (step < 13) {

            if (posizione == button_click) {
                if(occhio=="dx") {
                    stringa_risultati_dx += step + "-";
                    Log.v(TAG, " OKKKKK_DX");
                } else {
                    stringa_risultati_sx += step + "-";
                    Log.v(TAG, " OKKKKK_SX");
                }
            } else {
                if(occhio=="dx") {
                    stringa_risultati_dx += 0 + "-";
                    Log.v(TAG, " NON OKKK_DX");
                } else {
                    stringa_risultati_sx += 0 + "-";
                    Log.v(TAG, " NON OKKK_SX");
                }
            }
            step = step + 1;
            Log.d(TAG, "STRINGGG DX: " + stringa_risultati_dx);
            Log.d(TAG, "STRINGGG SX: " + stringa_risultati_sx);
        } else {
            if(occhio=="dx"){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setMessage("Cover your right eye");
                alertDialog.create();
                alertDialog.show();
                occhio="sx";
                step=1;
            } else {
                Log.v(TAG, "FINEEEEEEEEE");
                editor.putString("risultato_dx", stringa_risultati_dx);
                editor.putString("risultato_sx", stringa_risultati_sx);
                editor.commit();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setMessage("Test completed,bravo");
                alertDialog.create();
                alertDialog.show();
                startActivity(new Intent(test.this, risultato_test.class));
            }
        }


        if (step % 2 == 0) {
            switch (step) {
                case 2:
                    c_land.setScaleX(1f);
                    c_land.setScaleY(1f);
                    c_land.setAdjustViewBounds(true);
                    break;
                case 4:
                    c_land.setScaleX(0.85f);
                    c_land.setScaleY(0.85f);
                    c_land.setAdjustViewBounds(true);
                    break;
                case 6:
                    c_land.setScaleX(0.7f);
                    c_land.setScaleY(0.7f);
                    c_land.setAdjustViewBounds(true);
                    break;
                case 8:
                    c_land.setScaleX(0.55f);
                    c_land.setScaleY(0.55f);
                    c_land.setAdjustViewBounds(true);
                    break;
                case 10:
                    c_land.setScaleX(0.4f);
                    c_land.setScaleY(0.4f);
                    c_land.setAdjustViewBounds(true);
                    break;
                case 12:
                    c_land.setScaleX(0.25f);
                    c_land.setScaleY(0.25f);
                    c_land.setAdjustViewBounds(true);
                    break;
            }

            // c_land.setRotation(orientamento+90);
        } else {
            switch (step) {
                case 1:
                    c_land.setScaleX(1f);
                    c_land.setScaleY(1f);
                    c_land.setAdjustViewBounds(true);
                    break;
                case 3:
                    c_land.setScaleX(0.85f);
                    c_land.setScaleY(0.85f);
                    c_land.setAdjustViewBounds(true);
                    break;
                case 5:
                    c_land.setScaleX(0.7f);
                    c_land.setScaleY(0.7f);
                    c_land.setAdjustViewBounds(true);
                    break;
                case 7:
                    c_land.setScaleX(0.55f);
                    c_land.setScaleY(0.55f);
                    c_land.setAdjustViewBounds(true);
                    break;
                case 9:
                    c_land.setScaleX(0.4f);
                    c_land.setScaleY(0.4f);
                    c_land.setAdjustViewBounds(true);
                    break;
                case 11:
                    c_land.setScaleX(0.25f);
                    c_land.setScaleY(0.25f);
                    c_land.setAdjustViewBounds(true);
                    break;
            }
        }




    }

    @Override
    public void onClick(View view) {

    }
}

