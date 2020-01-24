package com.example.myapplication;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.net.URLConnection;
        import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
        import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.Button;
        import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button button;
    TextView outputText;

    //public static final String URL = "http://10.0.2.2:9997//se4med_servlet/Se4MedDataRegServlet?action=getListPatientDoc&useremail=test@unibg.it&password=9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08&idapp=AcuityTest";
    //public static final String URL = "http://10.0.2.2:9997//se4med_servlet/Se4MedDataRegServlet?action=getResultList&docemail=test@unibg.it&password=9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08&username=null&idpatient=10&idapp=AcuityTest";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewsById();

       button.setOnClickListener(this);

    }

    private void findViewsById() {
        button = (Button) findViewById(R.id.button);
        outputText = (TextView) findViewById(R.id.outputTxt);
    }

    public void onClick(View view) {
        GetXMLTask task = new GetXMLTask();
        // task.execute(new String[] { URL });
        Log.d("myTag", "AAAA");
        startActivity(new Intent(MainActivity.this, login.class));
    }

    private class GetXMLTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String output = null;
            for (String url : urls) {
                output = getOutputFromUrl(url);

            }
            return output;
        }

        private String getOutputFromUrl(String url) {

            StringBuffer output = new StringBuffer("");
            try {

                InputStream stream = getHttpConnection(url);
                BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
                String s = "";
                while ((s = buffer.readLine()) != null)
                    output.append(s);
            } catch (IOException e1) {
                e1.printStackTrace();
            }



            return output.toString();
        }

        // Makes HttpURLConnection and returns InputStream
        private InputStream getHttpConnection(String urlString)
                throws IOException {

            InputStream stream = null;
            java.net.URL url = new URL(urlString);

            URLConnection connection = url.openConnection();

            try {

                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                Log.d("myTag", httpConnection.toString());
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    stream = httpConnection.getInputStream();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return stream;
        }

        @Override
        protected void onPostExecute(String output) {
            outputText.setText(output);
        }
    }
}

