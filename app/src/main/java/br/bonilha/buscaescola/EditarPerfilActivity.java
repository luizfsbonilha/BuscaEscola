package br.bonilha.buscaescola;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class EditarPerfilActivity extends AppCompatActivity {
    public static final String CAMERA_PERMISSION = android.Manifest.permission.CAMERA;
    public static final String READ_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String WRITE_EXTERNAL_STORAGE_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

    public static final Integer CAMERA_REQUEST_CODE = 9001;
    public static final Integer CROP_REQUEST_CODE = 9002;
    public static final Integer GALLERY_REQUEST_CODE = 9003;

    public static final Integer CAMERA_PERMISSION_REQUEST_CODE = 8001;

    private Uri pictureUri;
    private String photoPath;
    String photoBase64;
    EditText txtUsuario;
    EditText txtTelefone;
    ImageView btnCamera;

    DatabaseReference mDb = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);


        txtTelefone = (EditText) findViewById(R.id.telefone);
        txtUsuario = (EditText) findViewById(R.id.nomeUsuario);

        if (user != null) {
            txtTelefone.setText(user.getEmail());
            txtUsuario.setText(user.getDisplayName());
        }

        btnCamera = (ImageView) findViewById(R.id.cam);
        Button btnSalvar = (Button) findViewById(R.id.salvar);
        final Button btnCancelar = (Button) findViewById(R.id.cancelar2);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraPermission();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtTelefone.getText() != null && txtTelefone.getText().toString().equals("")) {
                    Toast.makeText(EditarPerfilActivity.this, "Nome Inválido!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (txtUsuario.getText() != null && txtUsuario.getText().toString().equals("")) {
                    Toast.makeText(EditarPerfilActivity.this, "Sobrenome Inválido!", Toast.LENGTH_LONG).show();
                    return;
                }
                mDb.child(user.getUid()).child("nome").setValue(txtUsuario.getText().toString());
                mDb.child(user.getUid()).child("telefone").setValue(txtTelefone.getText().toString());
                mDb.child(user.getUid()).child("foto").setValue(photoBase64);
                Intent it = new Intent(EditarPerfilActivity.this, LogadoActivity.class);
                startActivity(it);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(EditarPerfilActivity.this, LogadoActivity.class);
                startActivity(it);
            }
        });
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = ImageProvider.getInstance().createImageFile(this);
            } catch (Exception e) {

            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.fpharma.findpharma.fileprovider", photoFile);
                pictureUri = photoURI;
                photoPath = photoFile.getAbsolutePath();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        }
    }


    public void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, CAMERA_PERMISSION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE_PERMISSION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{CAMERA_PERMISSION, WRITE_EXTERNAL_STORAGE_PERMISSION, READ_EXTERNAL_STORAGE_PERMISSION}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            openCamera();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                photoBase64 = encodeImage(photoPath);
                Uri uri = ImageProvider.getInstance().getImageUri(this, photoPath);
                btnCamera.setImageURI(uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            openCamera();
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private String encodeImage(String path) {
        File imagefile = new File(path);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imagefile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        //Base64.de
        return encImage;

    }

}
