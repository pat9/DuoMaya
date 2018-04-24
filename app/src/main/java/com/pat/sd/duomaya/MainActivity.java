package com.pat.sd.duomaya;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    CardView perfilCard, leccionesCard, juegosCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        perfilCard = findViewById(R.id.perfilCard);
        perfilCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PerfilActivity.class);
                startActivity(i);
            }
        });
        leccionesCard = findViewById(R.id.leccionesCard);
        leccionesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =  new Intent(MainActivity.this, LeccionesActivity.class);
                startActivity(i);
            }
        });
        juegosCard = findViewById(R.id.juegosCard);
        juegosCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =  new Intent(MainActivity.this, JuegoActivity.class);
                startActivity(i);
            }
        });


    }
}
