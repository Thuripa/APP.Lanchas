package com.example.applanchas.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applanchas.Modelos.Saida;
import com.example.applanchas.R;

import java.util.ArrayList;

public class AdaptadorSaidas extends RecyclerView.Adapter<AdaptadorSaidas.ViewHolder> {

    OnClickListener1 listener;

    Context c;
    ArrayList<Saida> saidas;

    public AdaptadorSaidas(Context c, ArrayList<Saida> saidas, OnClickListener1 listener) {
        this.c = c;
        this.saidas = saidas;
        this.listener = listener;
    }

    public interface OnClickListener1 {

        void aoClicarSaida(int position);
    }

    // -----    -----   -----   -----   -----  <CLASSE VIEWHOLDER  -----   -----   -----   -----   -----


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvNomeLancha;
        TextView tvHora;
        AdaptadorSaidas.OnClickListener1 listener;

        public ViewHolder(@NonNull View itemView, AdaptadorSaidas.OnClickListener1 listener) {
            super(itemView);

            this.listener = listener;

            tvNomeLancha = itemView.findViewById(R.id.tvNomeLancha);
            tvHora = itemView.findViewById(R.id.tvHoraEdited);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.aoClicarSaida(getAdapterPosition());
        }
    }

    // -----    -----   -----   -----   -----  </CLASSE VIEWHOLDER  -----   -----   -----   -----   -----

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.item_saida, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Saida saida = saidas.get(position);
        holder.tvNomeLancha.setText(saida.getNomeLancha());
        if (saida.getData() == null) {
                holder.tvHora.setText("Data Ausente");
        } else {
            holder.tvHora.setText(saida.getData().toString());
        }

    }


    @Override
    public int getItemCount() {
        return saidas.size();
    }
}
