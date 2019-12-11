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

public class PainelUsuario extends AppCompatActivity {

    Button btnSaidas;
    Button btnLanchas;
    Button btnSair;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painel_usuario);

        //seta views nos objetos correspondentes
        btnSaidas = findViewById(R.id.btnSaidas);
        btnSaidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamaTelaSaidas();
            }
        });

        btnLanchas = findViewById(R.id.btnCadastrarSaidaEdited);
        btnLanchas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamaTelaLanchas();
            }
        });

        btnSair = findViewById(R.id.btnSair);
        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sair();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_painel_usuario, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemAdicionarLancha:
                chamaTelaAddLancha();
                break;
            case R.id.itemAdicionarSaida:
                chamaTelaSaidas();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void chamaTelaAddLancha() {
        Intent intent = new Intent(this, AddLancha.class);
        startActivity(intent);
    }

    public void chamaTelaSaidas() {
        Intent intent = new Intent(this, Saidas.class);
        startActivity(intent);
    }

    public void chamaTelaLanchas() {
        Intent intent = new Intent(this, Lanchas.class);
        startActivity(intent);
    }

    public void sair() {
        finish();
    }

}
