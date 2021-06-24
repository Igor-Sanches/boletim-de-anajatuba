package com.igordutrasanches.anajatubaboletim.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.igordutrasanches.anajatubaboletim.R;
import com.igordutrasanches.anajatubaboletim.adapter.ProdutoDonateAdapter;
import com.igordutrasanches.anajatubaboletim.conta.Conta;
import com.igordutrasanches.anajatubaboletim.conta.User;
import com.igordutrasanches.anajatubaboletim.data.Chave;
import com.igordutrasanches.anajatubaboletim.data.Data;
import com.igordutrasanches.anajatubaboletim.models.Produto;
import com.igordutrasanches.anajatubaboletim.services.FirebaseUtils;
import com.igordutrasanches.anajatubaboletim.tools.IabBroadcastReceiver;
import com.igordutrasanches.anajatubaboletim.tools.IabHelper;
import com.igordutrasanches.anajatubaboletim.tools.IabResult;
import com.igordutrasanches.anajatubaboletim.tools.Inventory;
import com.igordutrasanches.anajatubaboletim.tools.Purchase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DonateActivity extends AppCompatActivity implements IabBroadcastReceiver.IabBroadcastListener {

    private RecyclerView recyclerListFrends;
    private TextView notIdView;
    private IabHelper mHelper;
    private IabBroadcastReceiver mBroadcastReceiver;
    private User user;

    private ProdutoDonateAdapter adapter;
    private CardView cardBDonateInfo;
    public final String ApplicationKey(){
        return getString(R.string.applicationID);
    }


    private void cardState(){
        cardBDonateInfo.setVisibility(Data.isCheckedDialogDonate(this) ? View.GONE : View.VISIBLE);
    }

    public void onBack(View view){
        finish();
    }

    private List<String> getClient(){
        List<String> client = new ArrayList<>();
        client.add(new String("939jfjjkfmi34"));
        client.add(new String("jjfj499344fgg"));
        client.add(new String("kdjkfkjfjkfjkfmmn4955"));
        client.add(new String("hdhjdjjd094904094"));
        client.add(new String("djdjdj8894984"));
        client.add(new String( "hhjhjfn4898944"));
        client.add(new String( "hhjhjffn48489894"));
        client.add(new String("hjhjfnfnn8849984"));
        client.add(new String("hfhjjfhjfh489944"));
        return client;
    }
    private void getUser(){
        if(Data.getUid(this) != null){
            FirebaseUtils.getDatabaseRef().child(Chave.PRIMARIA)
                    .child(Chave.USUARIO)
                    .child(Conta.getUID(this))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue() != null){
                                user = dataSnapshot.getValue(User.class);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        try{
            setToolbar();
            try{
                getUser();
                ((Button)findViewById(R.id.cardBDonateInfoBtnDonate)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Data.setCheckedDialogDonate(true, DonateActivity.this);
                        cardState();


                    }
                });
                cardBDonateInfo=findViewById(R.id.cardBDonateInfo);
                recyclerListFrends=findViewById(R.id.recyclerListFrends);
                notIdView=findViewById(R.id.notIdView);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                recyclerListFrends.setLayoutManager(linearLayoutManager);
                cardState();
                iniciarDados();
                iniciarLista();
            }catch (Exception x){
                Toast.makeText(this, x.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }catch (Exception x){
            Toast.makeText(this, x.getMessage(), Toast.LENGTH_SHORT).show();
        }
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

    private List<Produto> skuDetailsList;
    private void iniciarLista(){
        skuDetailsList = new ArrayList<>();
        List<String> lista = getClient();
        skuDetailsList.add(new Produto("R$ 2,00", lista.get(0), 1));
        skuDetailsList.add(new Produto("R$ 5,00", lista.get(1), 2));
        skuDetailsList.add(new Produto("R$ 10,00", lista.get(2), 3));
        skuDetailsList.add(new Produto("R$ 15,00", lista.get(3), 4));
        skuDetailsList.add(new Produto("R$ 20,00", lista.get(4), 5));
        skuDetailsList.add(new Produto("R$ 25,00", lista.get(5), 6));
        skuDetailsList.add(new Produto("R$ 50,00", lista.get(6), 7));
        skuDetailsList.add(new Produto("R$ 100,00", lista.get(7), 8));
        skuDetailsList.add(new Produto("R$ 500,00", lista.get(8), 9));
        loadRecycleView(skuDetailsList);
    }

    private void iniciarDados() {
       try{
           mHelper = new IabHelper(this, ApplicationKey());
           mHelper.iniciarInstalacao(new IabHelper.OnIabSetupFinishedListener() {
               @Override
               public void onIabSetupFinished(IabResult result) {
                   if (mHelper != null && result.isSuccess()) {
                       mBroadcastReceiver = new IabBroadcastReceiver(DonateActivity.this);
                       IntentFilter filter = new IntentFilter(IabBroadcastReceiver.ACTION);
                       registerReceiver(mBroadcastReceiver, filter);
                       List<String> lista = getClient();
                       mHelper.consultarInventorioAsync(true, lista, goInventario);
                   }
               }
           });

       }catch (Exception x){
           Toast.makeText(this, x.getMessage(), Toast.LENGTH_SHORT).show();
       }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{

            if (mHelper == null) return;
           if (!mHelper.onActivityResult(requestCode, resultCode, data)) { //
               super.onActivityResult(requestCode, resultCode, data);
           } else {
               if(resultCode == RESULT_OK) {
                   Data.setDonate(true, this);
                   User MyUser = user;
                   if(MyUser != null){
                       MyUser.setDonate(true);
                       FirebaseUtils.getDatabaseRef().child(Chave.PRIMARIA).child(Chave.USUARIO).child(Data.getUid(this))
                               .setValue(user);
                   }
                   new AlertDialog.Builder(this)
                           .setTitle("Muito Obrigado")
                           .setMessage(getString(R.string.donate_msg))
                           .setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   dialog.dismiss();
                               }
                           }).show();
                   iniciarDados();
               }
           }
       }catch (Exception x){
           Toast.makeText(this, x.getMessage(), Toast.LENGTH_SHORT).show();
       }
    }

    private void loadRecycleView(List<Produto> skuDetailsList) {
       try{
           if(skuDetailsList.size() > 0){
               notIdView.setVisibility(View.GONE);
               recyclerListFrends.setVisibility(View.VISIBLE);
               adapter = new ProdutoDonateAdapter(skuDetailsList, this);
               recyclerListFrends.setAdapter(adapter);
               adapter.setOnIProductClickListener(new ProdutoDonateAdapter.IProductClickListener() {
                   @Override
                   public void onProductClickListener(View v, Produto produto, int position) {
                       try{
                           mHelper.iniciarCompra(DonateActivity.this, produto.getUid(), 100, goCompra, produto.getTokem());
                       }catch (Exception c){
                           Toast.makeText(DonateActivity.this, "Não conseguimos se conectar", Toast.LENGTH_SHORT).show();
                       }
                   }
               });
           }else{
               notIdView.setVisibility(View.VISIBLE);
               recyclerListFrends.setVisibility(View.GONE);

           }

       }catch (Exception x){
           Toast.makeText(this, x.getMessage(), Toast.LENGTH_SHORT).show();
       }
    }

    private IabHelper.OnIabPurchaseFinishedListener goCompra = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase info) {
            if(result.isSuccess()){

                   mHelper.consumeAsync(info, new IabHelper.OnConsumeFinishedListener() {
                        @Override
                        public void onConsumeFinished(Purchase purchase, IabResult result) {
                            if(result.isSuccess() && mHelper != null){

                                //Já pode comprar covamente todos os produtos menos a escrição
                            }
                        }
                    });

            }

        }
    };

    @Override
    public void receivedBroadcast() {
        mHelper.consultarInventorioAsync(goInventario);
    }

    private IabHelper.QueryInventoryFinishedListener goInventario = new IabHelper.QueryInventoryFinishedListener() {
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inv) {
            if(result.isSuccess() && mHelper != null){

            }else{
                iniciarDados();
            }
        }
    };

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
