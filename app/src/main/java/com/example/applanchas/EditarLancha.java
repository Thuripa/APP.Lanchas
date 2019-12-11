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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.applanchas.Modelos.Lancha;

import io.realm.Realm;
import io.realm.RealmResults;

public class EditarLancha extends AppCompatActivity {


    //codigo de permissão referente à camera
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    Realm realm;
    // URI DA FOTO
    Uri uri_imagem;
    ImageView ivFotoLancha2;
    Button btnTirarFoto;
    Button btnEditarLancha;
    EditText etEditNomeLancha;
    EditText etEditNomeDono;
    EditText etEditModelo;
    EditText etEditTamanho;

    Lancha lancha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_lancha);

        realm = Realm.getDefaultInstance();

        Intent intent = getIntent();
        lancha = intent.getParcelableExtra("lancha");

        etEditNomeLancha = findViewById(R.id.etEditNomeLancha);
        etEditNomeDono = findViewById(R.id.etEditNomeDono);
        etEditModelo = findViewById(R.id.etEditModelo);
        ivFotoLancha2 = findViewById(R.id.ivFotoLancha2);

        if (lancha.getUri_imagem() != null) {

            String uriString = lancha.getUri_imagem();
            ivFotoLancha2.setImageURI(Uri.parse(uriString));

        }

        etEditTamanho = findViewById(R.id.etEditTamanho);

        btnTirarFoto = findViewById(R.id.btnTirarFoto);
        btnEditarLancha = findViewById(R.id.btnEditarLancha);

        btnTirarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        tirarFoto();
                    }

                }
                else {
                    // Versão Android < Marshmallow (23)
                    Log.d("ALCM", "Chamando Metodo abrirCamera");
                    tirarFoto();
                }
            }
        });

        btnEditarLancha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarLancha();
            }
        });

        if (lancha != null){
            etEditNomeLancha.setText(lancha.getNomeLancha());
            etEditNomeDono.setText(lancha.getNomeDono());
            etEditModelo.setText(String.valueOf(lancha.getModelo()));
            etEditTamanho.setText(String.valueOf(lancha.getTamanho()));
        }

    }

    private void editarLancha() {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm1) {

                lancha.setNomeLancha(etEditNomeLancha.getText().toString());
                lancha.setNomeDono(etEditNomeDono.getText().toString());
                lancha.setModelo(etEditModelo.getText().toString());
                lancha.setTamanho(Integer.valueOf(etEditTamanho.getText().toString()));
            }
        });
        realm.commitTransaction();
        realm.close();
        Intent intent = new Intent(this, Lanchas.class);
        startActivity(intent);
    }

    // Método para Abrir Câmera
    private void tirarFoto() {
        Log.d("ALCM", "Metodo Abrir Camera Chamado");
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Nova Foto");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Tirada Pela Camera");
        uri_imagem = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Toast.makeText(EditarLancha.this, "Abrindo Camera", Toast.LENGTH_LONG).show();

        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri_imagem);
        startActivityForResult(intentCamera, IMAGE_CAPTURE_CODE);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editar_lancha, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuSalvarEdicaoLancha:
                editarLancha();
                realm.close();
                Intent intent = new Intent(EditarLancha.this, Lanchas.class);
                startActivity(intent);
                break;
            case R.id.menuExcluirLancha:
                excluirLancha();
                realm.close();
                Intent intent1 = new Intent(EditarLancha.this, Lanchas.class);
                startActivity(intent1);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void excluirLancha() {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm1) {

                RealmResults<Lancha> results = bgRealm1.where(Lancha.class)
                        .equalTo("lancha_id", lancha.getLancha_id())
                        .findAll();

                results.deleteAllFromRealm();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK) {
            //seta a foto tirada no ImageView da tela
            ivFotoLancha2.setImageURI(uri_imagem);
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
                    tirarFoto();
                }
                else {
                    //Permissão Negada
                    Toast.makeText(this, "Permissão de Câmera Negada", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent voltaPainel = new Intent (EditarLancha.this, Lanchas.class);
        startActivity(voltaPainel);
    }
}
