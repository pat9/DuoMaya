package com.pat.sd.duomaya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapters.JuegosAdapter;
import Adapters.LeccionesAdapter;
import Clases.Juego;
import Clases.Leccion;
import Interfaces.CustomItemClickListener;

public class JuegoActivity extends AppCompatActivity {
    RecyclerView lstJuegos;
    ArrayList<Juego> Juegos;
    JsonArrayRequest arrayRequest;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        lstJuegos = findViewById(R.id.lstJuegos);
        lstJuegos.setLayoutManager(new GridLayoutManager(this, 2));

        Juegos = new ArrayList<>();
        GetJson();

    }

    public void GetJson()
    {
        String URL = "http://aprendermayaws.gear.host/AprenderMayaWS.asmx/LeccionesPublicas";
        arrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i=0; i<response.length();i++)
                {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Juego juegoAhorcado = new Juego(object.getString("Nombre"), "Ahorcado",R.drawable.ahorcado, object.getInt("CodigoLeccion"));
                        Juegos.add(juegoAhorcado);
                        Juego juegoAdivina = new Juego(object.getString("Nombre"), "Adivina",R.drawable.adivina, object.getInt("CodigoLeccion"));
                        Juegos.add(juegoAdivina);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                JuegosAdapter adapter = new JuegosAdapter(JuegoActivity.this, Juegos, new CustomItemClickListener() {
                    @Override
                    public void OnItemClick(View v, int Position) {
                        if(Juegos.get(Position).Tipo == "Ahorcado")
                        {
                            Intent i = new Intent(JuegoActivity.this, AhorcadoActivity.class);
                            i.putExtra("ID",Juegos.get(Position).CodigoLeccion);
                            startActivity(i);
                        }
                    }
                });

                lstJuegos.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(JuegoActivity.this);
        requestQueue.add(arrayRequest);
    }


}
