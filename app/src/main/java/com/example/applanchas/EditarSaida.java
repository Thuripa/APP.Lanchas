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
import io.realm.RealmResults;

public class EditarSaida extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    static final int LANCHA_SELECIONADA = 1002;

    Realm realm;
    Saida saida;
    Lancha lancha;

    Calendar data;
    Date dataDate;
    int ano = -1;
    int mes = -1;
    int dia = -1;
    int hora = -1;
    int minuto = -1;
    DatePickerDialog dpd;

    TextView tvEscolhaUmaLanchaEdited;
    TextView tvDataSaidaEdited;
    TextView tvHoraEdited;

    Button btnLanchaSaidaEdited;
    Button btnEditarData;
    Button btnEditarHora;
    Button btnCadastrarSaidaEdited;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_saida);

        realm = Realm.getDefaultInstance();

        Intent intent = getIntent();
        saida = intent.getParcelableExtra("saida");

        tvDataSaidaEdited = findViewById(R.id.tvDataSaidaEdited);
        tvHoraEdited = findViewById(R.id.tvHoraEdited);

        tvEscolhaUmaLanchaEdited = findViewById(R.id.tvEscolhaUmaLanchaEdited);
        tvEscolhaUmaLanchaEdited.setText(saida.getNomeLancha());

        btnLanchaSaidaEdited = findViewById(R.id.btnLanchaSaidaEdited);
        btnLanchaSaidaEdited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarSaida.this, Lanchas.class);
                intent.putExtra("code", LANCHA_SELECIONADA);
                startActivityForResult(intent, LANCHA_SELECIONADA);
            }
        });

        btnEditarData = findViewById(R.id.btnEscolherDataEdited);
        btnEditarData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = Calendar.getInstance();
                escolherData();
                tvDataSaidaEdited.setText(dia+"/"+mes);
            }
        });

        btnEditarHora = findViewById(R.id.btnEscolherHoraEdited);
        btnEditarHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escolherHora();
                tvHoraEdited.setText(hora+":"+minuto);
            }
        });

        btnCadastrarSaidaEdited = findViewById(R.id.btnCadastrarSaidaEdited);
        btnCadastrarSaidaEdited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data != null && hora != -1 && lancha != null) {
                    /*if (hora < 8 || hora > 16) {
                        Toast.makeText(AddSaida.this, "Horário Inválido",
                                Toast.LENGTH_LONG).show();
                    }*/
                    data.getTime();
                    editarSaida();
                    Intent intent = new Intent(EditarSaida.this, Saidas.class);
                    startActivity(intent);

                } else {
                    if (data == null) {
                        Toast.makeText(EditarSaida.this, "Data = NULL",
                                Toast.LENGTH_LONG).show();
                    } else if (hora == -1) {
                        Toast.makeText(EditarSaida.this, "hora = -1",
                                Toast.LENGTH_LONG).show();
                    } else if (lancha == null) {
                        Toast.makeText(EditarSaida.this, "lancha = NULL",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(EditarSaida.this, "SLA MAN",
                                Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

    }

    private void editarSaida() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm1) {

                saida.setLancha(lancha);
                saida.setNomeLancha(lancha.getNomeLancha());
                Date dataDate = data.getTime();
                saida.setData(dataDate);

            }
        });
        realm.commitTransaction();
        realm.close();
        Intent intent = new Intent(this, Saidas.class);
        startActivity(intent);
    }

    private void escolherHora() {
        TimePickerFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
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


    private void escolherData() {
        int day = data.get(Calendar.DAY_OF_MONTH);
        int month = data.get(Calendar.MONTH);
        int year = data.get(Calendar.YEAR);


        dpd = new DatePickerDialog(EditarSaida.this, new DatePickerDialog.OnDateSetListener() {
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == LANCHA_SELECIONADA) {
            if (resultCode == RESULT_OK) {

                Auxiliador auxiliador = new Auxiliador(realm);
                auxiliador.selectFromDB();
                if (data != null && data.getData() != null) {
                    String lanchaSelecionadaString = data.getData().toString();
                    int position = Integer.valueOf(lanchaSelecionadaString);
                    this.lancha = auxiliador.justRefresh().get(position);
                    tvEscolhaUmaLanchaEdited.setText(lancha.getNomeLancha());
                }

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editar_saida, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSalvarEdicaoSaida:
                editarSaida();
                realm.close();
                Intent intent = new Intent(EditarSaida.this, Saidas.class);
                startActivity(intent);
                break;
            case R.id.menuExcluirSaida:
                excluirSaida();
                realm.close();
                Intent intent1 = new Intent(EditarSaida.this, Saidas.class);
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void excluirSaida() {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm1) {

                RealmResults<Saida> results = bgRealm1.where(Saida.class)
                        .equalTo("saida_id", saida.getSaida_id())
                        .findAll();

                results.deleteAllFromRealm();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent voltaPainel = new Intent (EditarSaida.this, Saidas.class);
        startActivity(voltaPainel);
    }
}
