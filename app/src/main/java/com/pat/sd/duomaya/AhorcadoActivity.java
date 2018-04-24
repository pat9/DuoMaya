package com.pat.sd.duomaya;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import Adapters.SlideAdapter;
import Clases.Palabras;

public class AhorcadoActivity extends AppCompatActivity {
    SharedPreferences preferences;
    ImageView imgAhorcado;
    LinearLayout Letras, Espacios;
    ArrayList<View> BotonesLetras;
    TextView txtPista, RelojAhorcado, IntentosAhorcado;
    Integer Intentos, PalabraLength;
    ProgressDialog progressDialog;
    ProgressBar ProgressAhorcado;
    CountDownTimer cdt;
    boolean JuegoTerminado = false;
    char[] Palabra;
    String Pista;
    Integer Accion = 0;
    JsonArrayRequest arrayRequest;
    ArrayList<Palabras> palabras;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ahorcado);

        Letras = findViewById(R.id.Letras);
        Espacios = findViewById(R.id.Espacios);
        txtPista = findViewById(R.id.txtPista);
        RelojAhorcado = findViewById(R.id.RelojAhorcado);
        ProgressAhorcado = findViewById(R.id.ProgressAhorcado);
        IntentosAhorcado = findViewById(R.id.IntentosAhorcado);
        BotonesLetras = Letras.getTouchables();
        palabras = new ArrayList<>();
        Intentos = 0;
        PalabraLength = 0;
        preferences = this.getSharedPreferences("Usuario", Context.MODE_PRIVATE);
        Habilitar(false);
        ObtenerPalabra();



    }

    public void Verificar(View view)
    {
        boolean Encontrado = false;
        Button btn = (Button)view;
        CharSequence Letra = btn.getText();
        for(int i=0; i<Palabra.length; i++)
        {
            if(Letra.charAt(0) == Palabra[i])
            {
                TextView Text = findViewById(i);
                Text.setText(Letra);
                btn.setEnabled(false);
                PalabraLength--;
                Encontrado = true;

                if(PalabraLength == 0)
                {
                    Habilitar(false);
                    Toast.makeText(this, "Ganaste :D", Toast.LENGTH_SHORT).show();
                    JuegoGanado();
                    return;
                }
            }
        }
        if(!Encontrado)
        {
            btn.setEnabled(false);
            Intentos++;
            IntentosAhorcado.setText(Intentos + " de 4 intentos");
        }

        if(Intentos == 4)
        {
            JuegoPerdido();
            return;
        }
    }

    public void Habilitar(boolean Estado)
    {
        for(View Boton: BotonesLetras)
        {
            Boton.setEnabled(Estado);
        }
    }

    public void ObtenerPalabra()
    {
        Bundle bundle = getIntent().getExtras();
        String URL="http://aprendermayaws.gearhostpreview.com/AprenderMayaWS.asmx/ListaPalabrasLeccion?ID="+bundle.getInt("ID");
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

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                int NumeroAletorio = (int) (Math.random() * palabras.size()) ;
                Palabra = palabras.get(NumeroAletorio).PalabraMaya.toUpperCase().toCharArray();
                Pista = palabras.get(NumeroAletorio).PalabraEsp;
                txtPista.setText("Se traduce al español como: " + Pista);
                PalabraLength = Palabra.length;
                Construye();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(AhorcadoActivity.this);
        requestQueue.add(arrayRequest);


    }

    public void Construye()
    {
        for(int i = 0; i < Palabra.length; i++)
        {
            TextView row = new TextView(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));
            row.setId(i);
            row.setText("-");
            row.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            row.setPadding(0, 0, 0, 0);
            row.setWidth(100);
            Espacios.addView(row);
        }
        Habilitar(true);
        cdt = new CountDownTimer(30000, 1000)
        {

            @Override
            public void onTick(long millisUntilFinished) {
                if(!JuegoTerminado)
                {
                    Integer Seconds = (int)(millisUntilFinished/1000);
                    Integer Progress = ProgressAhorcado.getProgress();
                    if(Seconds >= 10)
                    {
                        RelojAhorcado.setText("00:" + Seconds);
                    }
                    else
                    {
                        RelojAhorcado.setText("00:0" + Seconds);
                    }
                    ProgressAhorcado.setProgress(Progress - 1);
                }

            }

            @Override
            public void onFinish() {
                if(!JuegoTerminado)
                {
                    RelojAhorcado.setText("00:00");
                    ProgressAhorcado.setProgress(0);
                    JuegoPerdido();
                }
            }
        }.start();
    }

    public void JuegoGanado()
    {
        cdt.cancel();
        JuegoTerminado = true;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Gasnaste");
        builder.setMessage("Has ganado el ahorcado");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog = new ProgressDialog(AhorcadoActivity.this);
                progressDialog.setTitle("Registrando victoria...");
                progressDialog.setMessage("Espere por favor");
                progressDialog.show();
                Accion = 2;
                String URL = "http://aprendermayaws.gear.host/AprenderMayaWS.asmx/RegistrarPuntos";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("CodigoUsuario", preferences.getInt("Codigo",0)+"");
                        params.put("Puntos","10");
                        params.put("Descripcion", "Ahorcado");
                        return  params;
                    }
                };

                requestQueue.add(stringRequest);

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void JuegoPerdido()
    {
        cdt.cancel();
        JuegoTerminado = true;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Perdiste");
        builder.setMessage("Has perdido el ahorcado");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CerrarActivity();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        for(int i=0; i<Palabra.length; i++)
        {

            TextView Text = findViewById(i);
            Text.setText(""+Palabra[i]);
        }
        Habilitar(false);

    }

    public void CerrarActivity()
    {

        Intent intent = new Intent(this, JuegoActivity.class);
        startActivity(intent);
        finish();
    }

}
