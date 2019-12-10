package com.example.fridgy;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    Button search, aleatory, myRecipes;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        search = findViewById(R.id.searchRecipes);
        aleatory = findViewById(R.id.aleatory);
        myRecipes = findViewById(R.id.myRecipes);

        name = getIntent().getStringExtra("name");
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        aleatory.setEnabled(false);
        myRecipes.setEnabled(false);

        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ResultActivity.class);
                startActivity(intent);
            }});

    }

}
