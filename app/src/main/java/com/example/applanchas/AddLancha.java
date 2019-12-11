package com.example.applanchas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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

public class AddLancha extends AppCompatActivity {

    Realm realm;

    EditText etNomeLancha;
    EditText etNomeDono;
    EditText etModelo;
    EditText etTamanho;
    ImageView ivFotografia;
    Button btnCadastrarLancha;

    Uri uri_imagem;

    //codigo de permissão referente à camera
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lancha);

        realm = Realm.getDefaultInstance();

        etNomeLancha = findViewById(R.id.etNomeLancha);
        etNomeDono = findViewById(R.id.etNomeDono);
        etModelo = findViewById(R.id.etModelo);
        etTamanho = findViewById(R.id.etTamanho);
        ivFotografia = findViewById(R.id.ivFotografia);
        btnCadastrarLancha = findViewById(R.id.btnCadastrarLancha);
        btnCadastrarLancha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionar();
                realm.close();
                Intent intent = new Intent(AddLancha.this, Lanchas.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_lancha, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuAdicionarNovaLancha:
                adicionar();
                realm.close();
                break;
            case R.id.menuCancelar:
                finish();
                break;
            case R.id.menuAdicionarImagem:
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

        return super.onOptionsItemSelected(item);
    }

    private void abrirCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Nova Foto");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Tirada Pela Camera");
        uri_imagem = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Toast.makeText(AddLancha.this, "Abrindo Camera", Toast.LENGTH_LONG).show();

        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri_imagem);
        startActivityForResult(intentCamera, IMAGE_CAPTURE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK) {
            //seta a foto tirada no ImageView da tela
            ivFotografia.setImageURI(uri_imagem);

            /*Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivFotografia.setImageBitmap(imageBitmap);
*/

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

    public void adicionar() {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                Number maxId = bgRealm.where(Lancha.class).max("lancha_id");

                int newKey = (maxId == null) ? 1 : maxId.intValue()+1;

                Lancha lancha = bgRealm.createObject(Lancha.class, newKey);

                lancha.setNomeLancha(etNomeLancha.getText().toString());
                lancha.setNomeDono(etNomeDono.getText().toString());
                lancha.setModelo(etModelo.getText().toString());
                lancha.setTamanho(Integer.valueOf(etTamanho.getText().toString()));

                if (uri_imagem != null) {
                    lancha.setUri_Imagem(uri_imagem.toString());
                }

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transação feita com sucesso
                Toast.makeText(AddLancha.this, "Sucesso!", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transação falhou e automaticamente foi cancelada
                Toast.makeText(AddLancha.this, "Falhou!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent voltaPainel = new Intent (AddLancha.this, Lanchas.class);
        startActivity(voltaPainel);
    }
}
