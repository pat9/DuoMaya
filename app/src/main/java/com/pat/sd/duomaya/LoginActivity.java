package com.pat.sd.duomaya;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cloudinary.android.MediaManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Button btnEntrar, btnRegistro;
    EditText txtUsuario, txtPassword;
    RequestQueue requestQueue;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtUsuario = findViewById(R.id.txtUsuario);
        txtPassword = findViewById(R.id.txtPassword);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Iniciar();
            }
        });
        btnRegistro = findViewById(R.id.btnRegistro);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        preferences = this.getSharedPreferences("Usuario",Context.MODE_PRIVATE);

        PermisoCamara();

        MediaManager.init(this,Configuracion());


    }

    public void PermisoCamara()
    {
        if ((ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No se necesita dar una explicación al usuario, sólo pedimos el permiso.
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                // MY_PERMISSIONS_REQUEST_CAMARA es una constante definida en la app. El método callback obtiene el resultado de la petición.
            }
        }
    }


    public static Map Configuracion()
    {
        Map Config = new HashMap();
        Config.put("cloud_name", "pat9");
        Config.put("api_key", "481118882942431");
        Config.put("api_secret", "ZiGf8m8U772_wHl-Oa0VMvymB4o");
        return Config;
    }

    public void Iniciar()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Espere...");
        progressDialog.setTitle("Iniciando");
        progressDialog.show();
        String URL = "http://aprendermayaws.gear.host/AprenderMayaWS.asmx/Login";

        StringRequest objectRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getInt("Codigo")>0)
                    {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("Codigo", object.getInt("Codigo"));
                        editor.putString("NickName", object.getString("NickName"));
                        editor.putString("Contraseña", object.getString("Contraseña"));
                        editor.putString("FotoPerfil", object.getString("FotoPerfil"));
                        editor.putString("Correo", object.getString("Correo"));
                        editor.putString("Nombre", object.getString("Nombre"));
                        editor.putString("Apellido", object.getString("Apellido"));
                        editor.commit();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        progressDialog.hide();
                        startActivity(intent);
                    }
                    else
                    {
                        progressDialog.hide();
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage("Error al iniciar");
                        builder.setTitle("Fallo el inicio");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("Error al iniciar");
                builder.setTitle("Sin conexión");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }){
            @Override
            public Map<String, String> getParams()
            {
                Map<String,String> params = new HashMap<>();
                params.put("Usuario", txtUsuario.getText().toString());
                params.put("Password", txtPassword.getText().toString());
                return params;
            }
        };

        requestQueue.add(objectRequest);


    }

}
