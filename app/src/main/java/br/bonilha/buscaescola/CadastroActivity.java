package br.bonilha.buscaescola;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static br.bonilha.buscaescola.R.id.textEmail;
import static br.bonilha.buscaescola.R.id.textNome;
import static br.bonilha.buscaescola.R.id.textPassword;

public class CadastroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Button cancelar = (Button) findViewById(R.id.btnCancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

         public void validarCampos (View validar){
             //Toast.makeText(this, "VALIDANDO", Toast.LENGTH_LONG).show();

             EditText editNome = (EditText) findViewById(R.id.textNome);
             EditText editEmail = (EditText) findViewById(R.id.textEmail);
             EditText editPassword = (EditText) findViewById(R.id.textPassword);



            if (editNome.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Campo Nome está vazio!", Toast.LENGTH_SHORT).show();
                editNome.requestFocus();
           }
            else if (editEmail.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Campo E-mail está vazio!", Toast.LENGTH_SHORT).show();
                editEmail.requestFocus();
            }
            else if (editPassword.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(), "Campo Senha está vazio", Toast.LENGTH_SHORT).show();
                editPassword.requestFocus();
            }

            else {

            }
        }

    }
