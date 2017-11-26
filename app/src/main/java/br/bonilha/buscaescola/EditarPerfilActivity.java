package br.bonilha.buscaescola;

import android.app.ProgressDialog;
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

import com.bumptech.glide.Glide;
import br.bonilha.buscaescola.MyGlideModule;
import br.bonilha.buscaescola.ImageProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

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
    Bitmap bitmap;
    String photoBase64;

    EditText textNome;
    EditText textSobrenome;
    EditText textEmail;
    EditText textTel;
    ImageView btnCamera;

    DatabaseReference mDb = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(user.getUid());
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Aguarde");

        textNome = (EditText) findViewById(R.id.textNome);
        textSobrenome = (EditText) findViewById(R.id.textSobrenome);
        textEmail = (EditText) findViewById(R.id.textEmail);
        textTel = (EditText) findViewById(R.id.tel);

        if (user != null) {
            mProgressDialog.show();
            mDb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mProgressDialog.dismiss();
                    HashMap<String, String> valores = (HashMap<String, String>) dataSnapshot.child(user.getUid()).getValue();
                    if (valores != null && !valores.isEmpty()) {
                        textNome.setText(valores.get("nome"));
                        textSobrenome.setText(valores.get("sobrenome"));
                        textEmail.setText(valores.get("email"));
                        textTel.setText(valores.get("telefone"));
                        GlideApp.with(getApplicationContext()).load(valores.get("avatar")).into(btnCamera);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
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

        btnSalvar.setOnClickListener(getSalvarListener());

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(EditarPerfilActivity.this, LogadoActivity.class);
                startActivity(it);
            }
        });
    }

    @NonNull
    private View.OnClickListener getSalvarListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textEmail.getText() != null && textEmail.getText().toString().equals("")) {
                    Toast.makeText(EditarPerfilActivity.this, "Email Inválido!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (textNome.getText() != null && textNome.getText().toString().equals("")) {
                    Toast.makeText(EditarPerfilActivity.this, "Nome Inválido!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (textSobrenome.getText() != null && textSobrenome.getText().toString().equals("")) {
                    Toast.makeText(EditarPerfilActivity.this, "Sobrenome Inválido!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (textTel.getText() != null && textTel.getText().toString().equals("")) {
                    Toast.makeText(EditarPerfilActivity.this, "Telefone Inválido!", Toast.LENGTH_LONG).show();
                    return;
                }

                mProgressDialog.show();
                mDb.child(user.getUid()).child("nome").setValue(textNome.getText().toString());
                mDb.child(user.getUid()).child("sobrenome").setValue(textSobrenome.getText().toString());
                mDb.child(user.getUid()).child("email").setValue(textEmail.getText().toString());
                mDb.child(user.getUid()).child("telefone").setValue(textTel.getText().toString());

                if (bitmap != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();
                    UploadTask uploadTask = storageRef.putBytes(data);
                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Uri downloadUrl = task.getResult().getDownloadUrl();
                            mDb.child(user.getUid()).child("avatar").setValue(downloadUrl.toString());
                            mProgressDialog.dismiss();
                            Intent it = new Intent(EditarPerfilActivity.this, LogadoActivity.class);
                            startActivity(it);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgressDialog.dismiss();
                            Toast.makeText(EditarPerfilActivity.this, "Ocorreu um erro ao fazer upload da foto.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };
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
                Uri photoURI = FileProvider.getUriForFile(this, "br.bonilha.buscaescola.fileprovider", photoFile);
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
//                photoBase64 = encodeImage(photoPath);
//                Uri uri = ImageProvider.getInstance().getImageUri(this, photoPath);
                bitmap = ImageProvider.getInstance().resizeRotatePicture(photoPath);
                btnCamera.setImageBitmap(bitmap);
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

