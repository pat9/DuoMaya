package com.pat.sd.duomaya;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EditarPerfilActivity extends AppCompatActivity {
    TextView txtUsuario,txtPassword,txtCorreo,txtNombre,txtApellido;
    ImageView imgFotoPerfil;
    Button btnRegistro;
    private File FotoFinal;
    String Accion="";
    String URLRESULTADO = "";
    Integer CodigoUsuario=0;
    SharedPreferences preferences;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        preferences = getSharedPreferences("Usuario", Context.MODE_PRIVATE);

        txtApellido = findViewById(R.id.txtApellido);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtNombre= findViewById(R.id.txtNombre);
        txtPassword = findViewById(R.id.txtPassword);

        btnRegistro = findViewById(R.id.btnRegistro);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActualizarDatos();
            }
        });

        imgFotoPerfil = findViewById(R.id.imgFotoPerfil);
        imgFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbrirMenuFoto();
            }
        });

        CodigoUsuario = preferences.getInt("Codigo",0);
        Picasso.get().load(preferences.getString("FotoPerfil",""));
        txtApellido.setText(preferences.getString("Apellido",""));
        txtCorreo.setText(preferences.getString("Correo",""));
        txtNombre.setText(preferences.getString("Nombre",""));
        txtPassword.setText(preferences.getString("Contraseña",""));

        requestQueue = Volley.newRequestQueue(this);

    }

    public void ActualizarDatos()
    {
        String URLDatos="http://aprendermayaws.gear.host/AprenderMayaWS.asmx/ModificarPerfil";
        String URLImagen = "http://aprendermayaws.gear.host/AprenderMayaWS.asmx/CambiarImagen";

        StringRequest requestDatos = new StringRequest(Request.Method.POST, URLDatos, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String,String> getParams()
            {
                Map<String,String> params = new HashMap<>();
                params.put("Codigo",CodigoUsuario.toString());
                params.put("Contra", txtPassword.getText().toString());
                params.put("Correo", txtCorreo.getText().toString());
                params.put("Nombre", txtNombre.getText().toString());
                params.put("Apellido", txtApellido.getText().toString());
                return params;
            }
        };

        requestQueue.add(requestDatos);

        if(URLRESULTADO != "")
        {
            StringRequest requestImagen = new StringRequest(Request.Method.POST, URLImagen, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                public Map<String,String> getParams()
                {
                    Map<String,String> params = new HashMap<>();
                    params.put("CodigoUsuario",CodigoUsuario.toString());
                    params.put("URL", URLRESULTADO);
                    return params;
                }
            };
            requestQueue.add(requestImagen);
        }
    }

    public void AbrirMenuFoto()
    {
        final  CharSequence[] arreglo = {"Camara", "Galeria"};
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Selecciona");
        builder.setSingleChoiceItems(arreglo, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0)
                {
                    Accion = "CAMARA";
                    dispatchTakePictureIntent();
                }
                else
                {
                    Accion ="GALERIA";
                    SelectGaleria();
                }
                dialog.cancel();
            }
        });
        android.support.v7.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        String state= Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)) {
            long captureTime = System.currentTimeMillis();
            String photoPath = Environment.getExternalStorageDirectory() + "/DCIM/Camera/Point" + captureTime + ".jpg";
            try {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                FotoFinal = new File(photoPath);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(FotoFinal));
                startActivityForResult(Intent.createChooser(intent, "Capture una foto"), 1);
            } catch (Exception e) {

            }
        }
    }

    private static final  int SELECT_FILE  =1;
    protected void SelectGaleria()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccione una imagen"), SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data)
    {
        if (requestcode == REQUEST_IMAGE_CAPTURE && resultcode == RESULT_OK && Accion =="CAMARA") {
            Bundle extras = data.getExtras();

            if(extras != null)
            {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Uri img = Uri.fromFile(FotoFinal);
                imgFotoPerfil.setImageBitmap(circulo(imageBitmap));
                String RequestID = MediaManager.get().upload(img).callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {

                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {

                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        URLRESULTADO =  resultData.get("url").toString();

                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {

                    }
                }).dispatch();

            }
        }
        else
        {
            if(resultcode == RESULT_OK && Accion =="GALERIA")
            {
                Uri selectedImage = data.getData();
                InputStream is;
                try
                {
                    is = getContentResolver().openInputStream(selectedImage);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    String resID = MediaManager.get().upload(selectedImage).callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {

                        }

                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {

                        }

                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            URLRESULTADO =  resultData.get("url").toString();
                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {

                        }

                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {

                        }
                    }).dispatch();
                    Picasso.get().load(selectedImage).transform(new CircleTransform()).into(imgFotoPerfil);

                } catch (FileNotFoundException e) {
                    System.out.print("Ups algo salio mal");
                    e.printStackTrace();
                }

            }
            else
            {
                Toast.makeText(this, "Salio de la galeria", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

    public Bitmap circulo(Bitmap source)
    {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap,
                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);

        squaredBitmap.recycle();
        return bitmap;
    }


}
