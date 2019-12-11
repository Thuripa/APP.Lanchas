package com.example.applanchas.Modelos;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class Migration implements RealmMigration {


    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        // DynamicRealm exposes an editable schema
        RealmSchema schema = realm.getSchema();




        if (oldVersion == 0) {
            schema.get("Lancha")
                    .addField("uri_imagem", String.class);
            oldVersion++;
        }


        if (oldVersion == 1) {
            schema.create("Saida")
                    .addField("saida_id", int.class, FieldAttribute.PRIMARY_KEY)
                    .addField("nomeLancha", String.class)
                    .addRealmObjectField("lancha", schema.get("Lancha"))
                    .addField("data", Date.class);
            oldVersion++;
        }
    }
}
