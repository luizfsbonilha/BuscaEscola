package br.bonilha.buscaescola;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.R.attr.name;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Escola> listEscola = new ArrayList<>();
    private ListaAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new ListaAdapter(this, listEscola);
        recyclerView = (RecyclerView) findViewById(R.id.lista);
        recyclerView.setHasFixedSize(true);
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
                    listEscola.add(e);
                }
                adapter.notifyDataSetChanged();
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

