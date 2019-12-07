package com.example.applanchas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class FotoLancha extends AppCompatActivity {


    ImageView imageView;
    Button btnTirarFoto;

    Uri uri_imagem;

    //codigo de permissão referente à camera
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_lancha);


        imageView = findViewById(R.id.imageView);
        btnTirarFoto = findViewById(R.id.btnTirarFoto);
        btnTirarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verifica se versão do Android >= Marshmallow (23)

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if(checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED) {

                        //Permissão Negada
                        String[] permission = {Manifest.permission.CAMERA,
                                               Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    }
                    else {
                        //Permissão Aceita
                        abrirCamera();
                    }

                }
                else {
                    // Versão Android < Marshmallow (23)
                    Log.d("ALCM", "Chamando Metodo abrirCamera");
                    abrirCamera();
                }


            }
        });


    }

    private void abrirCamera() {
        Log.d("ALCM", "Metodo Abrir Camera Chamado");
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Nova Foto");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Tirada Pela Camera");
        uri_imagem = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Toast.makeText(FotoLancha.this, "Abrindo Camera", Toast.LENGTH_LONG).show();

        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri_imagem);
        startActivityForResult(intentCamera, IMAGE_CAPTURE_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK) {
            //seta a foto tirada no ImageView da tela
            imageView.setImageURI(uri_imagem);
        }

    }

    // Método para Lidar com as Permissões
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if(grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    //Permissão Aceita
                    abrirCamera();
                }
                else {
                    //Permissão Negada
                    Toast.makeText(this, "Permissão de Câmera Negada", Toast.LENGTH_LONG).show();
                }
            }

        }
    }
}
