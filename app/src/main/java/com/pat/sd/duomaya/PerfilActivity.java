package com.pat.sd.duomaya;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class PerfilActivity extends AppCompatActivity {
    TextView txtUsuario, txtPuntos;
    ImageView imgFotoPerfil;
    Button btnRanking, btnEditar;
    SharedPreferences preferences;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        txtPuntos = findViewById(R.id.txtPuntos);
        txtUsuario = findViewById(R.id.txtUsuario);

        preferences = getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        queue = Volley.newRequestQueue(this);

        GetPuntos();

        imgFotoPerfil = findViewById(R.id.imgFotoPerfil);
        Picasso.get().load(preferences.getString("FotoPerfil","")).into(imgFotoPerfil);
        txtUsuario.setText(preferences.getString("Nombre", "") + " " + preferences.getString("Apellido","") +"("+preferences.getString("NickName","") + ")");

    }

    public void GetPuntos()
    {
        String URL ="http://aprendermayaws.gear.host/AprenderMayaWS.asmx/GetTotalPuntos";
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                txtPuntos.setText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("CodigoUsuario", preferences.getInt("Codigo",0)+"");
                return params;
            }
        };
        queue.add(request);
    }

}
