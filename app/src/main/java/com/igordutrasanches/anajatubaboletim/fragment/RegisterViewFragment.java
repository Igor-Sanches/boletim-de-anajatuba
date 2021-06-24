package com.igordutrasanches.anajatubaboletim.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.igordutrasanches.anajatubaboletim.MainActivity;
import com.igordutrasanches.anajatubaboletim.R;
import com.igordutrasanches.anajatubaboletim.activity.LoginEmailActivity;
import com.igordutrasanches.anajatubaboletim.conta.Conta;
import com.igordutrasanches.anajatubaboletim.conta.User;
import com.igordutrasanches.anajatubaboletim.data.Chave;
import com.igordutrasanches.anajatubaboletim.manager.Email;
import com.igordutrasanches.anajatubaboletim.services.Conexao;
import com.igordutrasanches.anajatubaboletim.services.DateTime;
import com.igordutrasanches.anajatubaboletim.services.FirebaseUtils;
import com.igordutrasanches.anajatubaboletim.tools.MyToast;

import java.util.regex.Pattern;

public class RegisterViewFragment extends Fragment {
    private Pattern namePattern = Pattern.compile("[a-zA-Z áãâÀÂÃÉêéÊÚûÛúÓÕôÔõóíÎÎÍÇç]+");
    private Context x;
    private View root;
    private EditText nameUser, emailUser, senhaUser, localUser;
    private Spinner generoUser;
    private CardView login, reegister;
    private FirebaseAuth mAuth;
    private ProgressDialog onProgress;
    private DatabaseReference mRef;

    private void load(){
        onProgress = new ProgressDialog(x);
        onProgress.setCancelable(false);
        onProgress.setMessage("Aguarde...");
        nameUser = root.findViewById(R.id.nameUser);
        emailUser = root.findViewById(R.id.emailUser);
        senhaUser = root.findViewById(R.id.senhaUser);
        localUser = root.findViewById(R.id.localUser);
        generoUser = root.findViewById(R.id.generoUser);
        login = root.findViewById(R.id.login);
        reegister = root.findViewById(R.id.reegister);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginEmailActivity.getInstance().setPosition(0);
            }
        });
        reegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegister();
            }
        });
    }

    private void showProgress(boolean isTrue){
        if(isTrue){
            onProgress.show();
        }else{
            onProgress.hide();
        }
    }

    private void onRegister() {
            if(check()){
                final String nome = nameUser.getText().toString().trim();
                String email = emailUser.getText().toString().trim();
                String senha = senhaUser.getText().toString().trim();
                showProgress(true);
                            if(mAuth != null){
                                mAuth.createUserWithEmailAndPassword(email, senha)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if(task.isSuccessful()){
                                                    try{
                                                        FirebaseUser user = mAuth.getCurrentUser();
                                                        showProgress(false);
                                                        if(user != null){
                                                            salvarDadosCriarDb(user, nome);
                                                        }
                                                    }catch (Exception x){
                                                        toast(x.getMessage(), 2);
                                                    }


                                                }else{
                                                    try{
                                                        throw task.getException();
                                                    }catch (FirebaseAuthWeakPasswordException user){
                                                        toast("Sua senha esta muito fraca", 2);
                                                        senhaUser.requestFocus();
                                                    }catch (FirebaseAuthInvalidCredentialsException user){
                                                        toast(getString(R.string.firebaseAuthInvalidCredentialsException), 2);
                                                        senhaUser.requestFocus();
                                                    }catch (FirebaseAuthUserCollisionException user){
                                                        toast(getString(R.string.firebaseAuthUserCollisionException), 2);
                                                        emailUser.requestFocus();
                                                    }catch (Exception e) {
                                                        // Toast.makeText(LoginEmailActivity.this, getString(R.string.authException), Toast.LENGTH_LONG).show();
                                                        toast(e.getMessage(), 2);
                                                        e.printStackTrace();
                                                    }
                                                    showProgress(false);
                                                }
                                            }
                                        });

                            }else{
                                showProgress(false);
                            }
        }
    }

    private void salvarDadosCriarDb(final FirebaseUser user_, String nome) {
        try{
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build();
            user_.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        continueWhith(user_);
                    }
                }
            });
        }catch (Exception x){
            toast(x.getMessage(), 2);
        }
    }

    private void continueWhith(FirebaseUser user_) {
        try{
            User user =new User(user_);
            String data = DateTime.Now(x).toString();
            user.setData(data);
            user.setLocalidade(localUser.getText().toString().trim());
            user.setGenero(generoUser.getSelectedItemPosition() == 1 ? "Feminino" : "Masculino");
            mRef.child(Chave.PRIMARIA).child(Chave.USUARIO).child(user_.getUid()).setValue(user);
            Conta.saveUID(user_.getUid(), x);
            Conta.setUserProfile(user, x);
            onFinish();
        }catch (Exception x){
            toast(x.getMessage(), 2);
        }
    }

    private void onFinish(){
        x.startActivity(new Intent(x, MainActivity.class));
        getActivity().finish();
    }

    public RegisterViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_register_view, container, false);
        x = getActivity();
        load();
        loadFirebase();
        return root;
    }

    private void loadFirebase() {
        mRef = FirebaseUtils.getDatabaseRef();
        mAuth = Conexao.getFirebaseAuth();
    }

    private void toast(String msg, int x){
        int index = 0;
        switch (x){
            case 0: index = MyToast.NOME; break;
            case 1: index = MyToast.SUCCESS; break;
            case 2: index = MyToast.ERROR; break;
        }
        MyToast.makeText(this.x, msg, index).show();
    }

    private boolean check(){
        boolean isX = false;
        String nome = nameUser.getText().toString().trim();
        String email = emailUser.getText().toString().trim();
        String senha = senhaUser.getText().toString().trim();
        String local = localUser.getText().toString().trim();
        if(Conexao.isConnected(x)) {
            if(!nome.equals("")){
            if(namePattern.matcher(nome).matches()){

                if(!email.equals("")){

                    if(Email.validar(email)){

                        if(!senha.equals("")){

                            if(!local.equals("")){

                                if(namePattern.matcher(local).matches()){

                                    if(generoUser.getSelectedItemPosition() != 0){
                                        isX = true;
                                    }else{
                                        toast("Selecione seu gênero", 2);
                                    }

                                }else{
                                    toast("Digite uma localidade valida", 2);
                                }

                            }else{
                                toast("Digite sua localidade", 2);
                            }

                        }else{
                            toast("Crie uma senha", 2);
                        }

                    }else{
                        toast("Digite um E-Mail valido", 2);
                    }

                }else{
                    toast("Digite seu E-Mail", 2);
                }

            }else{
                toast("Digite um nome valido", 2);
            }

        }else {
                toast("Digite seu nome", 2);
            }
        }else {
                 toast("Sem conxão com a internet", 2);

            }
        return isX;
    }
}