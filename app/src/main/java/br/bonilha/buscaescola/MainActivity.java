package br.bonilha.buscaescola;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private RecyclerView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    //inicializando as views

    private void initViews() {
        lista = (RecyclerView) findViewById(R.id.lista);
        lista.setHasFixedSize(true);
        lista.setItemAnimator(new DefaultItemAnimator());
        lista.setLayoutManager(new LinearLayoutManager(this));
        buscarDados();
    }

    private void buscarDados() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://mobile-aceite.tcu.gov.br/nossaEscolaRS/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EscolaInterface service = retrofit.create(EscolaInterface.class);

        Call<List<Escola>> escolas = service.listarEscolas();

        escolas.enqueue(new Callback<List<Escola>>() {
            @Override
            public void onResponse(Call<List<Escola>> call,
                                   Response<List<Escola>> response) {

                List<Escola> lista = response.body();

                for (Escola e : lista) {
                    Log.d("RETROFIT", e.getNome() + " " + e.getRede());
                }
            }

            @Override
            public void onFailure(Call<List<Escola>> call, Throwable t) {
            }
        });
    }

    public void disconnectUser(View view) {
        FirebaseAuth.getInstance().signOut();
        finish();
    }

}

