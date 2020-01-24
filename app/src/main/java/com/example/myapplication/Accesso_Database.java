package com.example.myapplication;

        import java.io.BufferedReader;
        import java.io.Console;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.net.URLConnection;
        import java.util.concurrent.ExecutionException;

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

public class Accesso_Database {

    String prova_url;
    public final String URL;
    String finale;


    public Accesso_Database(String prova_url) {
        this.prova_url = prova_url;
        URL = "http://10.0.2.2:9997//se4med_servlet/Se4MedDataRegServlet?action=" + prova_url;
        Log.d("url", URL);
        GetXMLTask task = new GetXMLTask();
        //task.execute(new String[] { URL });
        try{
            String res = task.execute(new String[] { URL }).get();
            Log.d("myTag", res);
            finale = res;
        }catch (ExecutionException | InterruptedException ei) {
            ei.printStackTrace();
        }
    }



    //public static final String URL = "http://10.0.2.2:9997//se4med_servlet/Se4MedDataRegServlet?action=getListPatientDoc&useremail=test@unibg.it&password=9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08&idapp=AcuityTest";

    //public void onClick(View view) {

        //startActivity(new Intent(MainActivity.this, login.class));
    //}

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
            finale = output;
        }
    }
}








