package com.example.applanchas.Modelos;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Saida extends RealmObject implements Parcelable {


    @PrimaryKey
    private int saida_id;
    private String nomeLancha;
    private Lancha lancha;
    private Date data;

    public Saida () {

    }

    public int getSaida_id() {
        return saida_id;
    }

    public void setSaida_id(int saida_id) {
        this.saida_id = saida_id;
    }

    public String getNomeLancha() {
        return nomeLancha;
    }

    public void setNomeLancha(String nomeLancha) {
        this.nomeLancha = nomeLancha;
    }

    public Lancha getLancha() {
        return lancha;
    }

    public void setLancha(Lancha lancha) {
        this.lancha = lancha;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    protected Saida(Parcel in) {
        saida_id = in.readInt();
        nomeLancha = in.readString();
        lancha = (Lancha) in.readValue(Lancha.class.getClassLoader());
        long tmpData = in.readLong();
        data = tmpData != -1 ? new Date(tmpData) : null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(saida_id);
        dest.writeString(nomeLancha);
        dest.writeValue(lancha);
        dest.writeLong(data != null ? data.getTime() : -1L);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Saida> CREATOR = new Parcelable.Creator<Saida>() {
        @Override
        public Saida createFromParcel(Parcel in) {
            return new Saida(in);
        }

        @Override
        public Saida[] newArray(int size) {
            return new Saida[size];
        }
    };

}
