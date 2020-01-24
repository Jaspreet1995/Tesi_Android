package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class tutorial extends Activity implements View.OnClickListener {

    Button test;
    TextView title;
    TextView Guideline1;
    TextView Guideline2;
    TextView Guideline3;
    TextView Guideline4;
    ImageView imGuide1;
    ImageView imGuide2;
    ImageView imGuide3;
    ImageView imGuide4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tutorial);

        test = findViewById(R.id.go_test);
        test.setText("Start the test");
        test.setOnClickListener(this);
        title = findViewById(R.id.Title);
        title.setText("Guidelines");

        Guideline1 = findViewById(R.id.Guide1);
        Guideline2 = findViewById(R.id.Guide2);
        Guideline3 = findViewById(R.id.Guide3);
        Guideline4 = findViewById(R.id.Guide4);


        Guideline1.setText("1 - Place yourself 80 cm from the screen.");
        Guideline2.setText("2 - If you have glasses for distance vision or glasses with progressive lenses, keep them on.");
        Guideline3.setText("3 - Without pressing on the eyelid, cover your left/right eye with your hand.");
        Guideline4.setText("4 - Indicate which way the open side of the  is facing.");


        imGuide1 = (ImageView) findViewById(R.id.imageGuide1);
        imGuide2 = (ImageView) findViewById(R.id.imageGuide2);
        imGuide3 = (ImageView) findViewById(R.id.imageGuide3);
        imGuide4 = (ImageView) findViewById(R.id.imageGuide4);

        imGuide1.setImageResource(R.drawable.dist);
        imGuide2.setImageResource(R.drawable.glasses);
        imGuide3.setImageResource(R.drawable.covereye);
        imGuide4.setImageResource(R.drawable.clandolt);

    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(tutorial.this, test.class));
    }
}
