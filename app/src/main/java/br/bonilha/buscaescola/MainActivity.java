package br.bonilha.buscaescola;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private RecyclerView lista;
    private ArrayList<Contato> model = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Contato c1 = new Contato();
        c1.setEmail("maria@teste.com");
        c1.setNome("Maria");

        Contato c2 = new Contato();
        c2.setEmail("joao@teste.com");
        c2.setNome("João");

        model.add(c1);
        model.add(c2);

        ListaAdapter adaptador = new ListaAdapter(this, model);

        lista = (RecyclerView) findViewById(R.id.lista);
        lista.setItemAnimator(new DefaultItemAnimator());
        lista.setLayoutManager(new LinearLayoutManager(this));
        lista.setAdapter(adaptador);

    }
    public void disconnectUser(View view){
        FirebaseAuth.getInstance().signOut();
        finish();
    }
}

