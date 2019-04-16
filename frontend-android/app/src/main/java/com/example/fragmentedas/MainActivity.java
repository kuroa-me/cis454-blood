package com.example.fragmentedas;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBar;

public class MainActivity extends AppCompatActivity implements select.OnFragmentInteractionListener, register.OnFragmentInteractionListener, login.OnFragmentInteractionListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mainTB = findViewById(R.id.my_toolbar);
        setSupportActionBar(mainTB);
        ActionBar mainAB = getSupportActionBar();

    }


    public boolean onCreatedOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
