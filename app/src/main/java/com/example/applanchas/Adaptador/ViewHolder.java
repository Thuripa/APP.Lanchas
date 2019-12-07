package com.example.applanchas.Adaptador;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applanchas.R;

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
        //listener.aoClicarLancha();
    }
}
