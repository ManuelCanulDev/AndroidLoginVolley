package com.manuelcanul.loginvolley;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class HomeActivity extends AppCompatActivity {

    public static final String MY_PREFERENCES = "MyPrefs";
    public static final String ID_USUARIO = "id";
    public static final String USERNAME = "username";
    public static final String STATUS = "status";
    private boolean status;
    SharedPreferences sharedPreferences;

    TextView id,user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES,Context.MODE_PRIVATE);
        id = (TextView)findViewById(R.id.tvid);
        user = (TextView)findViewById(R.id.tvuser);

        id.setText("EL ID ES: " + sharedPreferences.getString(ID_USUARIO,""));
        user.setText("EL USERNAME ES: " + sharedPreferences.getString(USERNAME,""));
    }

    public void logout(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent =  new Intent(HomeActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
