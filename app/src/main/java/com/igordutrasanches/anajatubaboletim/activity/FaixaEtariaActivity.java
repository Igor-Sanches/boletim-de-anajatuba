package com.igordutrasanches.anajatubaboletim.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.igordutrasanches.anajatubaboletim.R;
import com.igordutrasanches.anajatubaboletim.data.Chave;
import com.igordutrasanches.anajatubaboletim.data.Data;
import com.igordutrasanches.anajatubaboletim.models.Boletim;
import com.igordutrasanches.anajatubaboletim.services.Conexao;
import com.igordutrasanches.anajatubaboletim.services.FirebaseUtils;
import com.igordutrasanches.anajatubaboletim.tools.MyToast;

import java.util.Objects;

public class FaixaEtariaActivity extends AppCompatActivity {

    private SwipeRefreshLayout refress;
    private LinearLayout layoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faixa_etaria);
        setToolbar();
        layoutView= findViewById(R.id.layoutView);
        refress= findViewById(R.id.refress);
        showLoader();
    }

    private void showLoader() {
        refress.setRefreshing(true);
        layoutView.setVisibility(View.GONE);
        initLoader();
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.getValue() != null){
                Boletim mBoletim = (Boletim)dataSnapshot.getValue(Boletim.class);
                if(mBoletim != null){
                    setData(mBoletim);
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void initLoader(){
        try{
            DatabaseReference db = FirebaseUtils.getDatabaseRef();
            db.child(Chave.PRIMARIA).child(Chave.BOLETIM).addValueEventListener(valueEventListener);
            if(!Conexao.isConnected(this)) {
                toast("Sem conexão com a internet para atualizar", 2);

            }
        }catch (Exception x) {
            toast(x.getMessage(), 2);
        }
    }

    private void setData(Boletim mBoletim) {
        ((TextView)findViewById(R.id.fe_09)).setText(mBoletim.getFe_09());
        ((TextView)findViewById(R.id.fe_1019)).setText(mBoletim.getFe_1019());
        ((TextView)findViewById(R.id.fe_2029)).setText(mBoletim.getFe_2029());
        ((TextView)findViewById(R.id.fe_3039)).setText(mBoletim.getFe_3039());
        ((TextView)findViewById(R.id.fe_4049)).setText(mBoletim.getFe_4049());
        ((TextView)findViewById(R.id.fe_5059)).setText(mBoletim.getFe_5059());
        ((TextView)findViewById(R.id.fe_6069)).setText(mBoletim.getFe_6069());
        ((TextView)findViewById(R.id.fe_70)).setText(mBoletim.getFe_70());
        refress.setRefreshing(false);
        layoutView.setVisibility(View.VISIBLE);
    }

    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit(){
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            exit();
        }

        return super.onOptionsItemSelected(item);
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
