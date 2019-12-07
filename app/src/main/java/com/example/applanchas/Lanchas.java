package com.example.applanchas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.applanchas.Adaptador.Adaptador;
import com.example.applanchas.Modelos.Auxiliador;
import com.example.applanchas.Modelos.Lancha;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;

public class Lanchas extends AppCompatActivity implements Adaptador.OnClickListener {

    FloatingActionButton fabAdicionarLanhca;

    RecyclerView rvLanchas;
    Realm realm;
    Auxiliador auxiliador;
    RealmChangeListener realmChangeListener;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lanchas);

        fabAdicionarLanhca = findViewById(R.id.floatingActionButton);
        fabAdicionarLanhca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamaTelaAddLancha();
            }
        });

        realm = Realm.getDefaultInstance();

        rvLanchas = findViewById(R.id.rvLanchas);

        auxiliador = new Auxiliador(realm);
        auxiliador.selectFromDB();

        Adaptador adaptador = new Adaptador(this, auxiliador.justRefresh());
        rvLanchas.setLayoutManager(new LinearLayoutManager(this));
        rvLanchas.setAdapter(adaptador);

        reFresh();

    }

    @Override
    protected void onResume() {
        super.onResume();

        reFresh();
    }

    public void chamaTelaAddLancha() {
        Intent intent = new Intent(this, AddLancha.class);
        startActivity(intent);
    }

    private void reFresh() {
        realmChangeListener = new RealmChangeListener() {
            @Override
            public void onChange(Object o) {
                Adaptador adaptador = new Adaptador(Lanchas.this, auxiliador.justRefresh());
                rvLanchas.setAdapter(adaptador);
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
    public void aoClicarLancha(int position) {

        //pega a posição da lancha selecionada por meio do meu auxiliador
        Lancha lanchaSelecionada = auxiliador.justRefresh().get(position);

        Intent abrirEditor = new Intent(this, EditarLancha.class);
        abrirEditor.putExtra("lancha", lanchaSelecionada);
        startActivity(abrirEditor);
    }
}
