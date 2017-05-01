package com.example.nora.bcarebabymonitor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login";
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    public static final String SHARED_PREF_NAME = "myloginapp";
    //This would be used to store the email of current logged in user
    public static final String EMAIL_SHARED_PREF = "username";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";

    private boolean loggedIn = false;

    private EditText inputUser;
    private EditText inputPass;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ubah font
        TextView txt_logo = (TextView) findViewById(R.id.txt_logo);
        Typeface logo_font = Typeface.createFromAsset(getAssets(), "fonts/LobsterTwo-Regular.ttf");
        txt_logo.setTypeface(logo_font);

        inputUser= (EditText) findViewById(R.id.etInput_Username);
        inputPass = (EditText) findViewById(R.id.etInput_Password);


        buttonLogin = (Button) findViewById(R.id.btn_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        loggedIn = preferences.getBoolean(LOGGEDIN_SHARED_PREF, false);

        if (loggedIn){
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }

    }

    public void login() {
        Log.d(TAG, "Login");

        String username = inputUser.getText().toString();
        String password = inputPass.getText().toString();

        if (username.isEmpty()) {
            inputUser.setError("Masukkan username dengan benar");
        } else if (password.isEmpty() || password.length() < 5) {
            inputPass.setError("Masukkan password dengan benar");
        } else {

            buttonLogin.setEnabled(false);


            new AsyncLogin().execute(username, password);

            // TODO: Implement your own authentication logic here.

        }
    }

    private class AsyncLogin extends AsyncTask<String, String, String>{
        ProgressDialog progressDialog = new ProgressDialog(Login.this,
                R.style.AppTheme_Dark_Dialog);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                // Masukan alamat url phpnya
                url = new URL("http://bumbleb-care.com/Android/Login/login.php");
            } catch (MalformedURLException e){
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // menambahkan parameter ke URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1]);
                String query = builder.build().getEncodedQuery();

                // Open connection untuk mengirimkan data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8")
                );
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
            } catch (IOException e1){
                e1.printStackTrace();
                return "exception";
            }
            try{
                int response_code = conn.getResponseCode();

                // Cek jika connection berhasil
                if(response_code == HttpURLConnection.HTTP_OK){

                    // Membaca data terkirim dari server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    //Pass data to onPostExecute method
                    return (result.toString());
                } else {
                    return ("unsuccessful");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // this method will be running on UI thread
            progressDialog.dismiss();

            if(result.equalsIgnoreCase("true")) {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                SharedPreferences sharedPreferences = Login.this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putBoolean(LOGGEDIN_SHARED_PREF, true);
                editor.commit();

                Intent intent = new Intent(Login.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                Login.this.finish();
            } else if (result.equalsIgnoreCase("false")) {
                // If username and password does not match display a error message
                Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                buttonLogin.setEnabled(true);
            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
                Toast.makeText(Login.this, "Ada yang salah. Masalah koneksi", Toast.LENGTH_LONG).show();
                buttonLogin.setEnabled(true);
            }
        }
    }
    }