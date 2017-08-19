package br.bonilha.buscaescola;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button cadastrar = (Button) findViewById(R.id.btnCadastrar);

        cadastrar.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick (View v){
                startActivity(new Intent(LoginActivity.this, CadastroActivity.class));

            }
        });

        Button logar = (Button) findViewById(R.id.btnLogar);
        logar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                startActivity(new Intent(LoginActivity.this, MainActivity.class));

            }
        });


    }
}
