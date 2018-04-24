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

import Adapters.LeccionesAdapter;
import Clases.Leccion;
import Interfaces.CustomItemClickListener;

public class LeccionesActivity extends AppCompatActivity {
    ArrayList<Leccion> Lecciones;
    JsonArrayRequest arrayRequest;
    RequestQueue requestQueue;
    RecyclerView lstLecciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecciones);
        Lecciones = new ArrayList<>();
        lstLecciones = findViewById(R.id.lstLecciones);
        lstLecciones.setLayoutManager(new GridLayoutManager(this, 2));
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
                        Leccion leccion = new Leccion();
                        leccion.CodigoLeccion = object.getInt("CodigoLeccion");
                        leccion.Nombre = object.getString("Nombre");
                        leccion.Imagen = object.getString("ImagenLeccion");
                        leccion.Decripcion = object.getString("Descripcion");
                        Lecciones.add(leccion);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                LeccionesAdapter adapter = new LeccionesAdapter(LeccionesActivity.this, Lecciones, new CustomItemClickListener() {
                    @Override
                    public void OnItemClick(View v, int Position) {
                        Intent intent = new Intent(LeccionesActivity.this, LeccionActivity.class);
                        intent.putExtra("Codigo", Lecciones.get(Position).CodigoLeccion);
                        startActivity(intent);
                    }
                });
                lstLecciones.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(LeccionesActivity.this);
        requestQueue.add(arrayRequest);
    }

}
