package com.manuelcanul.loginvolley;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    String URL_LOGIN = "https://manuelcanul.000webhostapp.com/android-login-con-mysql-php-volley/login.php";
    public static final String MY_PREFERENCES = "MyPrefs";
    public static final String ID_USUARIO = "id";
    public static final String USERNAME = "username";

    EditText username,password;

    public static final String STATUS = "status";
    private boolean status;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.edUsername);
        password = (EditText) findViewById(R.id.edPassword);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        status = sharedPreferences.getBoolean(STATUS,false);

        if(status){
            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void iniciarLogin(View view) {
        final String user = username.getText().toString();
        final String pass = password.getText().toString();

        if (user.isEmpty() || pass.isEmpty()){
            Toast.makeText(this, "Por favor, llene los campos.", Toast.LENGTH_SHORT).show();
        }else{
            class Login extends AsyncTask<Void,Void,String>{
                ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pdLoading.setMessage("\tIniciando Sesión...");
                    pdLoading.setCancelable(false);
                    pdLoading.show();
                }

                @Override
                protected String doInBackground(Void... voids) {
                   RequestHandler requestHandler = new RequestHandler();

                    HashMap<String, String> params = new HashMap<>();
                    params.put("username",user);
                    params.put("password",pass);

                    return requestHandler.sendPostRequest(URL_LOGIN,params);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    pdLoading.dismiss();

                    try{

                        JSONObject obj = new JSONObject(s);
                        if (!obj.getBoolean("error")){
                            String id_usu = obj.getString("id");
                            String user_usu = obj.getString("username");

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(ID_USUARIO,id_usu);
                            editor.putString(USERNAME,user_usu);
                            editor.putBoolean(STATUS,true);
                            editor.apply();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }catch (JSONException e){
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Exception: " + e, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            Login login = new Login();
            login.execute();
        }
    }
}
