package br.bonilha.buscaescola;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static br.bonilha.buscaescola.R.id.btnCadastrar;
import static br.bonilha.buscaescola.R.id.textEmail;
import static br.bonilha.buscaescola.R.id.textNome;
import static br.bonilha.buscaescola.R.id.textPassword;

public class CadastroActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

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

            mAuth = FirebaseAuth.getInstance();

            Button cadastrar = (Button) findViewById(R.id.btnCadastrar);
            cadastrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText textEmail = (EditText) findViewById(R.id.textEmail);
                EditText textPassword = (EditText) findViewById(R.id.textPassword);

                mAuth.createUserWithEmailAndPassword(textEmail.getText().toString(), textPassword.getText().toString())
                        .addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(CadastroActivity.this, "Usuario Cadastrado", Toast.LENGTH_SHORT).show();
                                    finish();

                                }
                            }
                        });
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
