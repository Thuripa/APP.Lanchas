package com.example.applanchas.Modelos;

import android.content.Context;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

// Classe de auxílio para buscar novos items e contexto do DB
public class Auxiliador {

    Realm realm;
    RealmResults<Lancha> lanchas;

    public Auxiliador(Realm realm) {
        this.realm = realm;
    }

    public void selectFromDB() {
        lanchas = realm.where(Lancha.class).findAll();
    }


    // justRefresh ou recemAtualizado é chamado quando eu quero uma "nova" lista de lanchas recem atualizada
    public ArrayList<Lancha> justRefresh() {
        ArrayList<Lancha> listItem = new ArrayList<>();
        for (Lancha l: lanchas) {
            listItem.add(l);
        }

        return listItem;
    }

}
