package com.example.applanchas.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applanchas.Modelos.Lancha;
import com.example.applanchas.R;

import java.util.ArrayList;

public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder> {

    OnClickListener listener;

    Context c;
    ArrayList<Lancha> lanchas;

    public Adaptador(Context c, ArrayList<Lancha> lanchas, OnClickListener listener) {
        this.c = c;
        this.lanchas = lanchas;
        this.listener = listener;
    }

    public interface OnClickListener {
        void aoClicarLancha(int position);
    }

    // -----    -----   -----   -----   -----  <CLASSE VIEWHOLDER  -----   -----   -----   -----   -----


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvNomeLancha;
        Adaptador.OnClickListener listener;

        public ViewHolder(@NonNull View itemView, Adaptador.OnClickListener listener) {
            super(itemView);

            this.listener = listener;

            tvNomeLancha = itemView.findViewById(R.id.tvNomeLancha);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.aoClicarLancha(getAdapterPosition());
        }
    }

    // -----    -----   -----   -----   -----  </CLASSE VIEWHOLDER  -----   -----   -----   -----   -----

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.item_lancha, parent, false);
        return new ViewHolder(view, listener);
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
