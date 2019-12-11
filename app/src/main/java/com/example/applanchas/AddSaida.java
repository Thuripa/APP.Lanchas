package com.example.applanchas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.applanchas.Modelos.Auxiliador;
import com.example.applanchas.Modelos.Lancha;
import com.example.applanchas.Modelos.Saida;
import com.example.applanchas.Modelos.TimePickerFragment;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;

public class AddSaida extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    Realm realm;

    static final int LANCHA_SELECIONADA = 1002;
    Lancha lancha;
    TextView tvData;
    TextView tvHora;
    TextView tvEscolhaUmaLancha;
    Button btnLanchas;
    Button btnCadastrarSaida;
    Button btnEscolherData;
    Button btnEscolherHora;
    Calendar data;
    Date dataDate;
    int ano = -1;
    int mes = -1;
    int dia = -1;
    int hora = -1;
    int minuto = -1;
    DatePickerDialog dpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_saida);

        realm = Realm.getDefaultInstance();

        tvEscolhaUmaLancha = findViewById(R.id.tvEscolhaUmaLancha);
        tvData = findViewById(R.id.tvDataSaida);
        tvHora = findViewById(R.id.tvHora);


        btnEscolherData = findViewById(R.id.btnEscolherData);
        btnEscolherData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = Calendar.getInstance();
                escolherData();
                tvData.setText(dia+"/"+mes);
            }
        });

        btnEscolherHora = findViewById(R.id.btnEscolherHora);
        btnEscolherHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escolherHora();
                tvHora.setText(hora+":"+minuto);
            }
        });

        btnLanchas = findViewById(R.id.btnLanchaSaida);
        btnLanchas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddSaida.this, Lanchas.class);
                intent.putExtra("code", LANCHA_SELECIONADA);
                startActivityForResult(intent, LANCHA_SELECIONADA);
            }
        });

        btnCadastrarSaida = findViewById(R.id.btnCadastrarSaida);
        btnCadastrarSaida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data != null && hora != -1 && lancha != null) {
                    /*if (hora < 8 || hora > 16) {
                        Toast.makeText(AddSaida.this, "Horário Inválido",
                                Toast.LENGTH_LONG).show();
                    }*/
                    data.getTime();
                    adicionar();
                    Intent intent = new Intent(AddSaida.this, Saidas.class);
                    startActivity(intent);

                }
            }
        });

    }

    private void escolherHora() {
        TimePickerFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    private void escolherData() {

        int day = data.get(Calendar.DAY_OF_MONTH);
        int month = data.get(Calendar.MONTH);
        int year = data.get(Calendar.YEAR);


        dpd = new DatePickerDialog(AddSaida.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                ano = year;
                mes = month;
                dia = dayOfMonth;
            }
        }, year, month, day);

        dpd.show();
        data.set(ano, mes, dia);

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        if (data == null) {
            hora = hourOfDay;
            minuto = minute;

        } else {
            hora = hourOfDay;
            minuto = minute;
            data.set(ano, mes, dia, hourOfDay, minute);
        }
    }

    public void adicionar() {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm1) {

                    Number idMax = bgRealm1.where(Saida.class).max("saida_id");
                    int newID = (idMax == null) ? 1 : idMax.intValue()+1;
                    Saida saida = bgRealm1.createObject(Saida.class, newID);
                    dataDate = data.getTime();
                    saida.setData(dataDate);
                    saida.setLancha(lancha);
                    saida.setNomeLancha(lancha.getNomeLancha());
                }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == LANCHA_SELECIONADA) {
            if (resultCode == RESULT_OK) {

                Auxiliador auxiliador = new Auxiliador(realm);
                auxiliador.selectFromDB();
                if (data != null && data.getData() != null) {
                    String lanchaSelecionadaString = data.getData().toString();
                    int position = Integer.valueOf(lanchaSelecionadaString);
                    this.lancha = auxiliador.justRefresh().get(position);
                    tvEscolhaUmaLancha.setText(lancha.getNomeLancha());
                }

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_saida, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAdicionarSaida:
                adicionar();
                realm.close();
                break;
            case R.id.menuCancelarSaida:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent voltaPainel = new Intent (AddSaida.this, Saidas.class);
        startActivity(voltaPainel);
    }
}
