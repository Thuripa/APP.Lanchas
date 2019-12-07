package com.example.applanchas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.applanchas.Modelos.Lancha;

import io.realm.Realm;

public class AddLancha extends AppCompatActivity {

    Realm realm;

    EditText etNomeLancha;
    EditText etNomeDono;
    EditText etModelo;
    EditText etTamanho;
    Button btnCadastrarLancha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lancha);

        realm = Realm.getDefaultInstance();

        etNomeLancha = findViewById(R.id.etNomeLancha);
        etNomeDono = findViewById(R.id.etNomeDono);
        etModelo = findViewById(R.id.etModelo);
        etTamanho = findViewById(R.id.etTamanho);
        btnCadastrarLancha = findViewById(R.id.btnCadastrarLancha);
        btnCadastrarLancha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionar();
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
            case R.id.menuAdicionarLancha:
                adicionar();
                break;
            case R.id.menuCancelar:
                finish();
                break;
            case R.id.menuAdicionarImagem:
                chamaTelaFotoLancha();
        }

        return super.onOptionsItemSelected(item);
    }

    public void chamaTelaFotoLancha() {
        Intent intent = new Intent(this, FotoLancha.class);
        startActivity(intent);
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

}
