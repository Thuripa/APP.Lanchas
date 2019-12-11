package com.example.applanchas.Modelos;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class AuxiliadorSaidas {

    Realm realm;
    RealmResults<Saida> saidas;

    public AuxiliadorSaidas(Realm realm) {
        this.realm = realm;
    }

    public void selectFromDB() {
        saidas = realm.where(Saida.class).findAll();
    }


    // justRefresh ou recemAtualizado é chamado quando eu quero uma "nova" lista de lanchas recem atualizada
    public ArrayList<Saida> justRefresh() {
        ArrayList<Saida> listItem = new ArrayList<>();
        for (Saida saida: saidas) {
            listItem.add(saida);
        }

        return listItem;
    }

}
