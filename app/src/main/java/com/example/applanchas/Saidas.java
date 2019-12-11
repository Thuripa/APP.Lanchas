package com.example.applanchas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.applanchas.Adaptador.Adaptador;
import com.example.applanchas.Adaptador.AdaptadorSaidas;
import com.example.applanchas.Modelos.Auxiliador;
import com.example.applanchas.Modelos.AuxiliadorSaidas;
import com.example.applanchas.Modelos.Lancha;
import com.example.applanchas.Modelos.Saida;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;

public class Saidas extends AppCompatActivity implements AdaptadorSaidas.OnClickListener1 {

    FloatingActionButton fabAdicionarSaidas;

    RecyclerView rvSaidas;
    Realm realm;
    AuxiliadorSaidas auxiliador;
    RealmChangeListener realmChangeListener;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saidas);

        fabAdicionarSaidas = findViewById(R.id.floatingActionButton1);
        fabAdicionarSaidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamaTelaAddSaida();
            }
        });

        realm = Realm.getDefaultInstance();

        rvSaidas = findViewById(R.id.rvSaidas);

        auxiliador = new AuxiliadorSaidas(realm);
        auxiliador.selectFromDB();

        AdaptadorSaidas adaptadorSaidas = new AdaptadorSaidas(this, auxiliador.justRefresh(), this);
        rvSaidas.setLayoutManager(new LinearLayoutManager(this));
        rvSaidas.setAdapter(adaptadorSaidas);

        reFresh();

    }

    @Override
    protected void onResume() {
        super.onResume();

        reFresh();
    }

    public void chamaTelaAddSaida() {
        Intent intent = new Intent(this, AddSaida.class);
        startActivity(intent);
    }

    private void reFresh() {
        realmChangeListener = new RealmChangeListener() {
            @Override
            public void onChange(Object o) {
                AdaptadorSaidas adaptadorSaidas = new AdaptadorSaidas(Saidas.this, auxiliador.justRefresh(),
                        Saidas.this);

                rvSaidas.setAdapter(adaptadorSaidas);
            }
        };
        realm.addChangeListener(realmChangeListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.removeChangeListener(realmChangeListener);
        realm.close();
    }

    @Override
    public void aoClicarSaida(int position) {
        //pega a posição da saida selecionada por meio do meu auxiliador
        Saida saidaSelecionada = auxiliador.justRefresh().get(position);

        Intent abrirEditor = new Intent(this, EditarSaida.class);
        abrirEditor.putExtra("saida", saidaSelecionada);
        startActivity(abrirEditor);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent voltaPainel = new Intent (Saidas.this, PainelUsuario.class);
        startActivity(voltaPainel);
    }

}
