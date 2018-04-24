package com.pat.sd.duomaya;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapters.SlideAdapter;
import Clases.Palabras;

public class LeccionActivity extends AppCompatActivity {
    ArrayList<Palabras> palabras;
    ViewPager viewPager;
    SlideAdapter adapter;
    JsonArrayRequest arrayRequest;
    RequestQueue requestQueue;
    int CodigoLeccion=0;
    int[] Colors = {
            Color.rgb(55,55,55),
            Color.rgb(239,85,85),
            Color.rgb(110,49,89),
            Color.rgb(1,188,212)
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leccion);
        palabras = new ArrayList<>();
        viewPager = findViewById(R.id.viewPager);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            CodigoLeccion = (int) bundle.get("Codigo");
        }
        GetJson();

    }

    public void GetJson()
    {
        String URL = "http://aprendermayaws.gear.host/AprenderMayaWS.asmx/ListaPalabrasLeccion?ID="+CodigoLeccion;
        arrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                int[] colorsbg = new int[response.length()];
                int cont = 0;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Palabras palabra = new Palabras();
                        palabra.PalabraMaya = object.getString("PalabraMaya");
                        palabra.PalabraEsp = object.getString("PalabraEs");
                        palabra.AudioCont = object.getString("AudioCont");
                        palabra.ImagenCont = object.getString("ImagenCont");
                        palabras.add(palabra);

                        if(cont < Colors.length)
                        {
                            colorsbg[i] = Colors[cont];
                            cont++;
                            if(cont == Colors.length)
                            {
                                cont = 0;
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter = new SlideAdapter(LeccionActivity.this,palabras,LeccionActivity.this, colorsbg);
                viewPager.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(LeccionActivity.this);
        requestQueue.add(arrayRequest);

    }




}
