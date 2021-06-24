package com.igordutrasanches.anajatubaboletim.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;
import com.google.firebase.storage.UploadTask;
import com.igordutrasanches.anajatubaboletim.MainActivity;
import com.igordutrasanches.anajatubaboletim.R;
import com.igordutrasanches.anajatubaboletim.conta.Conta;
import com.igordutrasanches.anajatubaboletim.conta.User;
import com.igordutrasanches.anajatubaboletim.data.Chave;
import com.igordutrasanches.anajatubaboletim.data.Data;
import com.igordutrasanches.anajatubaboletim.manager.ImageUtils;
import com.igordutrasanches.anajatubaboletim.services.Conexao;
import com.igordutrasanches.anajatubaboletim.services.FirebaseUtils;
import com.igordutrasanches.anajatubaboletim.tools.MyToast;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.regex.Pattern;

public class ContaActivity extends AppCompatActivity{

    private DatabaseReference mRef;
    private User user;
    private Conta conta;
    private TextView nameDisplay;

    private void exit(){
        finish();
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private Button localidadeBtn, GeneroBtn;
    private Pattern namePattern = Pattern.compile("[a-zA-Z áãâÀÂÃÉêéÊÚûÛúÓÕôÔõóíÎÎÍÇç]+");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta);
        mRef = FirebaseUtils.getDatabaseRef();
        load();
        getUser();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
               if(item.getItemId() == R.id.exit){
                   sairConta();
               }
                return false;
            }
        });
        localidadeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialogLocalidade();
            }
        });
        ((Button)findViewById(R.id.senhaBtn))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        senhaReset();
                    }
                });
        nameDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameEdit();
            }
        });
        ((FloatingActionButton)findViewById(R.id.addPhoto))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newPhoto();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                try {
                    updateAvatar(getContentResolver().openInputStream(result.getUri()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void updateAvatar(InputStream openInputStream) {
    try{
        final ProgressDialog dialog = ProgressDialog.show(this, "Aguarde...", "Espere um instante...", true);
        FirebaseStorage.getInstance().getReference().child("Imagens").child(user.getUid()).putStream(openInputStream)
        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    try{
                        task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful()){
                                    user.setAvatar(task.getResult().toString());
                                    saveData();
                                    dialog.dismiss();
                                }else{
                                    toast("Ocorreu uma falha ao atualizar os dados", 2);
                                    dialog.dismiss();
                                }
                            }
                        });

                    }catch (Exception x){
                        Toast.makeText(ContaActivity.this, x.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }else{
                    toast("Ocorreu uma falha ao atualizar os dados", 2);
                    dialog.dismiss();
                }
            }
        });

    }catch (Exception x){
        Toast.makeText(ContaActivity.this, x.getMessage(), Toast.LENGTH_SHORT).show();
    }
    }

    private void newPhoto() {
        CropImage.activity(null).setAspectRatio(512, 512).setGuidelines(CropImageView.Guidelines.ON).start(this);
    }

    private void openDialogLocalidade() {
        final AlertDialog builder = new AlertDialog.Builder(this).create();
        View view = getLayoutInflater().inflate(R.layout.dialog_add, null);
        builder.setView(view);
        final TextInputLayout layoutEdit = view.findViewById(R.id.layoutEdit);
        layoutEdit.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        if(!user.getLocalidade().equals("")){
            layoutEdit.getEditText().setText(user.getLocalidade());
        }
        layoutEdit.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layoutEdit.setError("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ((Button)view.findViewById(R.id.btnCancelar))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
        ((Button)view.findViewById(R.id.btnSalvar))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String local = layoutEdit.getEditText().getText().toString().trim();
                        if(!local.equals("")){
                            if(!local.equals(user.getLocalidade())){
                                if(namePattern.matcher(local).matches()) {
                                    user.setLocalidade(local);
                                    saveData();
                                    builder.dismiss();
                                }else{
                                    layoutEdit.setError("Digite um local valido");
                                }
                            }else{
                                builder.dismiss();
                            }

                        }else{
                            layoutEdit.setError("Digite o nome da sua localidade");
                        }

                    }
                });
        builder.show();
    }

    private void nameEdit() {
       try{
           final AlertDialog builder = new AlertDialog.Builder(this).create();
           View view = getLayoutInflater().inflate(R.layout.dialog_add, null);
           builder.setView(view);
           final TextInputLayout layoutEdit = view.findViewById(R.id.layoutEdit);
           layoutEdit.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
           if(!user.getName().equals("")){
               layoutEdit.getEditText().setText(user.getName());
           }
           layoutEdit.getEditText().addTextChangedListener(new TextWatcher() {
               @Override
               public void beforeTextChanged(CharSequence s, int start, int count, int after) {

               }

               @Override
               public void onTextChanged(CharSequence s, int start, int before, int count) {
                   layoutEdit.setError("");
               }

               @Override
               public void afterTextChanged(Editable s) {

               }
           });
           ((Button)view.findViewById(R.id.btnCancelar))
                   .setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           builder.dismiss();
                       }
                   });
           ((Button)view.findViewById(R.id.btnSalvar))
                   .setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {

                           String name = layoutEdit.getEditText().getText().toString().trim();
                           if(!name.equals("")){
                              if(!name.equals(user.getName())){
                                  if(namePattern.matcher(name).matches()) {
                                      user.setName(name);
                                      saveData();
                                      builder.dismiss();
                                  }else{
                                      layoutEdit.setError("Digite um nome valido");
                                  }
                              }else{
                                  builder.dismiss();
                              }
                           }else{
                               layoutEdit.setError("Digite seu nome");
                           }

                       }
                   });
           builder.show();
       }catch (Exception x){
           toast(x.getMessage(), 2);
       }
    }

    private void saveData() {
        mRef.child(Chave.PRIMARIA)
                .child(Chave.USUARIO)
                .child(Data.getUid(this))
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Conta.setUserProfile(user, ContaActivity.this);
                            toast("Dados salvos", 1);
                        }else{
                            toast("Erro ao salvar", 2);
                        }
                    }
                });
    }

    private void load() {
        localidadeBtn = findViewById(R.id.localidadeBtn);
        nameDisplay = findViewById(R.id.nameDisplay);
        GeneroBtn = findViewById(R.id.GeneroBtn);
    }

    private void getUser() {
        try{

            mRef.child(Chave.PRIMARIA)
                    .child(Chave.USUARIO)
                    .child(Data.getUid(this))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue() != null){
                                user = dataSnapshot.getValue(User.class);
                                if(user != null){
                                    loadData();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        }catch (Exception x){

        }
    }

    private void loadData() {
       try{
           nameDisplay.setText(user.getName());
           GeneroBtn.setText(user.getGenero());
           ((Button)findViewById(R.id.emailBtn)).setText(user.getEmail());
           ((Button)findViewById(R.id.stateConta)).setText(user.isBam() ? "Desativada" : "Ativa");
           localidadeBtn.setText(user.getLocalidade().equals("") ? "Adicionar local" : user.getLocalidade());
           ImageUtils.displayImageFromUrl(this, user.getAvatar(), ((CircularImageView)findViewById(R.id.appprofile)), getResources().getDrawable(R.drawable.app_icone));

       }catch (Exception x){

       }
         }

    private void sairConta() {
        try{

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Tem certeza que deseja sair?");
            dialog.setNeutralButton("Fechar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.setPositiveButton("Sair", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sairApp();
                }
            });
            dialog.show();

        }catch (Exception x){

        }
    }

    private void sairApp() {
        Conexao.getFirebaseAuth().signOut();
        Data.removeValue(this);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void senhaReset(){
        final ProgressDialog dialog = ProgressDialog.show(this, "Aguarde...", "Espere um instante...", true);
        dialog.setCancelable(false);
        Conexao.getFirebaseAuth().sendPasswordResetEmail(user.getEmail())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            toast("Verifique sua caixa de E-Mail, e troque sua senha atravez de um link", 1);
                        }else{
                            toast("Ocorreu um erro", 2);
                        }
                        dialog.dismiss();
                    }
                });

    }

    private void toast(String msg, int x){
        int index = 0;
        switch (x){
            case 0: index = MyToast.NOME; break;
            case 1: index = MyToast.SUCCESS; break;
            case 2: index = MyToast.ERROR; break;
        }
        MyToast.makeText(this, msg, index).show();
    }
}