package com.igordutrasanches.anajatubaboletim.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.igordutrasanches.anajatubaboletim.MainActivity;
import com.igordutrasanches.anajatubaboletim.R;
import com.igordutrasanches.anajatubaboletim.activity.LoginEmailActivity;
import com.igordutrasanches.anajatubaboletim.conta.Conta;
import com.igordutrasanches.anajatubaboletim.conta.User;
import com.igordutrasanches.anajatubaboletim.data.Chave;
import com.igordutrasanches.anajatubaboletim.manager.Email;
import com.igordutrasanches.anajatubaboletim.services.Conexao;
import com.igordutrasanches.anajatubaboletim.services.FirebaseUtils;
import com.igordutrasanches.anajatubaboletim.tools.MyToast;

public class LoginViewFragment extends Fragment {

    private BottomSheetDialog sheetDialog;
    private BottomSheetBehavior sheetBehavior;
    private View sheetBottom;
    private Context x;
    private View root;
    private EditText emailUser, senhaUser;
    private CardView login, reegister;
    private TextView reset;
    private FirebaseAuth mAuth;
    private ProgressDialog onProgress;
    private DatabaseReference mRef;

    private void load(){
        sheetBottom = root.findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(sheetBottom);
        onProgress = new ProgressDialog(x);
        onProgress.setCancelable(false);
        onProgress.setMessage("Aguarde...");
        emailUser = root.findViewById(R.id.emailUser);
        senhaUser = root.findViewById(R.id.senhaUser);
        reset = root.findViewById(R.id.reset);
        login = root.findViewById(R.id.login);
        reegister = root.findViewById(R.id.reegister);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReset();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin();
            }
        });
        reegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginEmailActivity.getInstance().setPosition(1);
            }
        });
    }

    private void onReset() {
        if(sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        View _view = getLayoutInflater().inflate(R.layout.card_redefinir_senha, null);

        final EditText emailB2 = _view.findViewById(R.id.emailB2);

        CardView btnRec= _view.findViewById(R.id.btnRec);
        btnRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String _emailB2 = emailB2.getText().toString();
                if (Conexao.isConnected(x)) {
                    if(!_emailB2.equals("")) {

                        if (Email.validar(_emailB2)) {
                            onRecuperar(_emailB2);
                        }else{
                            showProgress(false);
                            toast("Digite um E-Mail válido", 2);
                        }

                    }else{
                        showProgress(false);
                        toast("Digite seu endereço de E-Mail", 2);
                    }


                }else{
                    showProgress(false);
                    toast("Sem conxão com a internet", 2);

                } }
        });

        sheetDialog = new BottomSheetDialog(getActivity());
        sheetDialog.setContentView(_view);
        if(Build.VERSION.SDK_INT >= 21){
            sheetDialog.getWindow().addFlags(67108864);
        }
        ((View)_view.getParent()).setBackgroundColor(getResources().getColor(R.color.light_grey_100));
        sheetDialog.show();
        sheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
    }

    private void onRecuperar(String email){
        sheetDialog.dismiss();
        toast("Estamos verificando seu E-Mail", 0);
        showProgress(true);
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            toast("Você ira receber um E-Mail para tocar sua senha", 1);
                            showProgress(false);
                        }else{
                            try{
                                throw task.getException();
                            }catch (FirebaseAuthInvalidUserException user) {
                                emailUser.requestFocus();
                                toast(getString(R.string.firebaseAuthInvalidUserException), 2);
                              }catch (Exception e) {
                                toast(getString(R.string.authException), 2);
                                e.printStackTrace();
                            }
                            sheetDialog.show();
                            showProgress(false);
                        }
                    }
                });
    }

    private void onLogin() {
           try{
               if(check()) {
                   String email = emailUser.getText().toString().trim();
                   String senha = senhaUser.getText().toString().trim();
                   showProgress(true);
                   if (mAuth != null) {
                       mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                           @Override
                           public void onComplete(@NonNull Task<AuthResult> task) {
                               if (task.isSuccessful()) {
                                   showProgress(false);
                                   FirebaseUser user = mAuth.getCurrentUser();
                                   if (user != null) {
                                       loginDataUser(user);
                                   }

                               } else {
                                   try {
                                       throw task.getException();
                                   } catch (FirebaseAuthInvalidUserException user) {
                                       senhaUser.requestFocus();
                                       toast(getString(R.string.firebaseAuthInvalidUserException), 2);
                                   } catch (FirebaseAuthInvalidCredentialsException user) {
                                       emailUser.requestFocus();
                                       toast(getString(R.string.firebaseAuthInvalidCredentialsException), 2);
                                   } catch (Exception e) {
                                       toast(getString(R.string.authException), 2);
                                       e.printStackTrace();
                                   }
                                   showProgress(false);
                               }
                           }
                       });

                   } else {
                       showProgress(false);
                   }
               }
           }catch (Exception x){
               toast(x.getMessage(), 2);
           }
    }

    private void loginDataUser(FirebaseUser user) {
        Conta.saveUID(user.getUid(), x);
        mRef.child(Chave.PRIMARIA).child(Chave.USUARIO).child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() != null){
                            User user_ = dataSnapshot.getValue(User.class);
                            continueWhithUser(user_);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    private void onFinish(){
        x.startActivity(new Intent(x, MainActivity.class));
        getActivity().finish();
    }

    private void continueWhithUser(User user) {
        Conta.setUserProfile(user, x);
        onFinish();
    }

    private void showProgress(boolean isTrue){
        if(isTrue){
            onProgress.show();
        }else{
            onProgress.hide();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_login_view, container, false);
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
        String email = emailUser.getText().toString().trim();
        String senha = senhaUser.getText().toString().trim();

        if (Conexao.isConnected(x)) {
            if(!email.equals("")){

                if(Email.validar(email)){

                    if(!senha.equals("")){

                        isX = true;

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
            toast("Sem conxão com a internet", 2);

        }

        return isX;
    }
}