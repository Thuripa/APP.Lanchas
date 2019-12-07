package com.example.applanchas.Modelos;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Lancha extends RealmObject implements Parcelable {

    @PrimaryKey
    private int lancha_id;
    private String nomeLancha;
    private String nomeDono;
    private String modelo;
    private int tamanho;

    public Lancha () {

    }

    public void setNomeLancha(String nomeLancha) {
        this.nomeLancha = nomeLancha;
    }

    public String getNomeDono() {
        return nomeDono;
    }

    public void setNomeDono(String nomeDono) {
        this.nomeDono = nomeDono;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public int getLancha_id() {
        return lancha_id;
    }

    public void setLancha_id(int lancha_id) {
        this.lancha_id = lancha_id;
    }

    public Lancha(String nomeLancha) {
        this.nomeLancha = nomeLancha;
    }

    public String getNomeLancha() {
        return nomeLancha;
    }




    protected Lancha(Parcel in) {
        lancha_id = in.readInt();
        nomeLancha = in.readString();
        nomeDono = in.readString();
        modelo = in.readString();
        tamanho = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(lancha_id);
        dest.writeString(nomeLancha);
        dest.writeString(nomeDono);
        dest.writeString(modelo);
        dest.writeInt(tamanho);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Lancha> CREATOR = new Parcelable.Creator<Lancha>() {
        @Override
        public Lancha createFromParcel(Parcel in) {
            return new Lancha(in);
        }

        @Override
        public Lancha[] newArray(int size) {
            return new Lancha[size];
        }
    };

}
