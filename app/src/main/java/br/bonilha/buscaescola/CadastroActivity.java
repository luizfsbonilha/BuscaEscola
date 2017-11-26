package br.bonilha.buscaescola;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class CadastroActivity extends AppCompatActivity {

    EditText textNome;
    EditText textSobrenome;
    EditText textEmail;
    EditText textPassword;
    EditText textConfirmaPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        mAuth = FirebaseAuth.getInstance();

        textNome = (EditText) findViewById(R.id.textNome);
        textSobrenome = (EditText) findViewById(R.id.textSobrenome);
        textEmail = (EditText) findViewById(R.id.textEmail);
        textPassword = (EditText) findViewById(R.id.textPassword);
        textConfirmaPassword = (EditText) findViewById(R.id.textConfirmaPassword);
        Button btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        Button btnCancelar = (Button) findViewById(R.id.btnCancelar);


        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textNome.getText() != null && textNome.getText().toString().equals("")) {
                    Toast.makeText(CadastroActivity.this, "Nome Inválido!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (textSobrenome.getText() != null && textSobrenome.getText().toString().equals("")) {
                    Toast.makeText(CadastroActivity.this, "Sobrenome Inválido!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (textEmail.getText() != null && textEmail.getText().toString().equals("")) {
                    Toast.makeText(CadastroActivity.this, "Email Inválido!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (textPassword.getText() != null && textPassword.getText().toString().equals("")) {
                    Toast.makeText(CadastroActivity.this, "Senha Inválida!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (textConfirmaPassword.getText() != null && textConfirmaPassword.getText().toString().equals("") && textConfirmaPassword.getText() != textPassword) {
                    Toast.makeText(CadastroActivity.this, "As senhas não são iguais!", Toast.LENGTH_LONG).show();
                    return;
                }
                register();
            }

        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(CadastroActivity.this, LoginActivity.class);
                startActivity(it);
            }
        });
    }

    private void register() {


        mAuth.createUserWithEmailAndPassword(textEmail.getText().toString(), textPassword.getText().toString())
                .addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CadastroActivity.this, "Usuario Cadastrado", Toast.LENGTH_SHORT).show();
                            Intent it = new Intent(CadastroActivity.this, LoginActivity.class);
                            startActivity(it);

                        }
                    }
                });
    }
}
