package com.example.applanchas.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applanchas.Modelos.Lancha;
import com.example.applanchas.R;

import java.util.ArrayList;

public class Adaptador extends RecyclerView.Adapter<ViewHolder> {

    Context c;
    ArrayList<Lancha> lanchas;

    public Adaptador(Context c, ArrayList<Lancha> lanchas) {
        this.c = c;
        this.lanchas = lanchas;
    }

    public interface OnClickListener {
        void aoClicarItem(int position);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.item_lancha, parent, false);
        //return new ViewHolder(view);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Lancha lancha = lanchas.get(position);
        holder.tvNomeLancha.setText(lancha.getNomeLancha());

    }

    @Override
    public int getItemCount() {
        return lanchas.size();
    }
}
