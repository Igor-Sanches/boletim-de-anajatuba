package com.igordutrasanches.anajatubaboletim.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.igordutrasanches.anajatubaboletim.R;
import com.igordutrasanches.anajatubaboletim.adapter.AdapterMessage;
import com.igordutrasanches.anajatubaboletim.conta.Conta;
import com.igordutrasanches.anajatubaboletim.conta.User;
import com.igordutrasanches.anajatubaboletim.data.Chave;
import com.igordutrasanches.anajatubaboletim.data.Data;
import com.igordutrasanches.anajatubaboletim.data.DataMsgDB;
import com.igordutrasanches.anajatubaboletim.manager.Filter;
import com.igordutrasanches.anajatubaboletim.models.Message;
import com.igordutrasanches.anajatubaboletim.services.DateTime;
import com.igordutrasanches.anajatubaboletim.services.FirebaseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TimeLineActivity extends AppCompatActivity {

    private DatabaseReference ref;
    private MenuItem itemEditar, itemPostar, sort;
    private CardView cardBDonateInfo;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private DataMsgDB db;
    private EditText msg;
    private ImageView iconviewNotView;
    private ImageButton cardBDonateInfoBtn;
    private User MyUser;

    private void getUser(){
        if(Data.getUid(this) != null){
            FirebaseUtils.getDatabaseRef().child(Chave.PRIMARIA)
                    .child(Chave.USUARIO)
                    .child(Conta.getUID(this))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue() != null){
                                MyUser = dataSnapshot.getValue(User.class);
                                if(MyUser != null){
                                    shotUserInfo();
                                }else{
                                    Toast.makeText(TimeLineActivity.this, "Não conseguimos nos conectar", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }


    }

    private void shotUserInfo() {
       try{
           btns();

           msg.setText(MyUser.getMsg());
       }catch (Exception x){
           Toast.makeText(this, x.getMessage(), Toast.LENGTH_SHORT).show();
       }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline_menu, menu);
        itemEditar = menu.findItem(R.id.editar);
        itemPostar = menu.findItem(R.id.Postar);
        sort = menu.findItem(R.id.sort);
        btns();
        return super.onCreateOptionsMenu(menu);
    }

    private void btns() {
        if(MyUser != null) {
            if (itemPostar != null) {
                itemPostar.setVisible(MyUser.getMsg().equals(""));
            }
            if (itemEditar != null) {
                itemEditar.setVisible(!MyUser.getMsg().equals(""));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }else if (item.getItemId() == R.id.sort){
            showSorting();
        }else{
            showCard();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSorting() {
        try{

            final AlertDialog dialog = new AlertDialog.Builder(this).create();
            String[] sorting = new String[]{ "Mais novo", "Mais antigo"};
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            p.setMargins(10,10,10,10);
            ll.setLayoutParams(p);
            for(int i =0; i < sorting.length; i++){
                Button btn = new Button(this);
                btn.setTag(i);
                btn.setText(sorting[i]);
                TypedValue value = new TypedValue();
                getTheme().resolveAttribute(android.R.attr.selectableItemBackground, value, true);
                btn.setBackgroundResource(value.resourceId);
                btn.setAllCaps(false);

                if(Integer.valueOf(btn.getTag().toString()) == Data.getSortingMessageApoio(this)){
                    btn.setTextColor(getResources().getColor(R.color.colorBlueAccent));
                }

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button btn = (Button)v;
                        Data.setSortingMessageApoio(Integer.valueOf(btn.getTag().toString()), TimeLineActivity.this);
                        dialog.dismiss();
                        shotUsers();
                    }
                });
                ll.addView(btn);
            }
            dialog.setView(ll);
            dialog.show();

        }catch (Exception x){

        }
    }

    private void showCard() {
       if(MyUser != null){
           itemPostar.setVisible(false);
           itemEditar.setVisible(false);
           msg.setText(MyUser.getMsg());
           ((LinearLayout)findViewById(R.id.viewNotView)).setVisibility(MyUser.getMsg().equals("") ? View.GONE : View.VISIBLE);
           iconviewNotView.setImageResource(MyUser.isMsgApoio() ? R.drawable.ic_action_inview : R.drawable.ic_action_view);
           cardBDonateInfo.setVisibility(View.VISIBLE);
       }
    }

    private void hideCard(){
        btns();
        cardBDonateInfo.setVisibility(View.GONE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ref = FirebaseUtils.getDatabaseRef();
        getUser();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        cardBDonateInfo = findViewById(R.id.cardBDonateInfo);
        mSwipeRefreshLayout= findViewById(R.id.mSwipeRefreshLayout);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        msg = findViewById(R.id.msg);
        cardBDonateInfoBtn= findViewById(R.id.cardBDonateInfoBtn);
        iconviewNotView=findViewById(R.id.iconviewNotView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        db = new DataMsgDB(this);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shotUsers();
            }
        });
         shotUsers();
        ((Button)findViewById(R.id.btnSalvar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave();
            }
        });
        ((LinearLayout)findViewById(R.id.viewNotView)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInvisible();
            }
        });
        cardBDonateInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCard();
            }
        });
    }

    private void onSave() {
       try{
           String myMsg = msg.getText().toString().trim();
          if(!myMsg.equals("")){
            if(Filter.isText(myMsg, this)){

                if(MyUser != null) {
                    newUser = MyUser;
                    newUser.setMsgApoio(true);
                    newUser.setMsg(myMsg);
                    newUser.setDataMsg(DateTime.Now(this).toString());
                    ref.child(Chave.PRIMARIA)
                            .child(Chave.USUARIO)
                            .child(Data.getUid(this)).setValue(newUser)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(TimeLineActivity.this, "Publicação postada", Toast.LENGTH_SHORT).show();
                                        hideCard();
                                    } else {
                                        Toast.makeText(TimeLineActivity.this, "Não conseguimos postar", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    getUser();
                    Toast.makeText(TimeLineActivity.this, "Houve um erro tente novamente", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(TimeLineActivity.this, "Você digitou palavras que não podem ser adicionadas na mensagem", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(TimeLineActivity.this, "Digite sua mensagem", Toast.LENGTH_SHORT).show();
        }
    }catch (Exception x){
        Toast.makeText(this, x.getMessage(), Toast.LENGTH_SHORT).show();
    }
    }

    private User newUser;
    private void onInvisible() {
        newUser = MyUser;
        if(newUser != null){
        newUser.setMsgApoio(!MyUser.isMsgApoio());
            hideCard();
            ref.child(Chave.PRIMARIA)
                .child(Chave.USUARIO)
                .child(Data.getUid(this)).setValue(newUser)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(TimeLineActivity.this, newUser.isMsgApoio() ? "Sua publicação esta visivel para todos" : "Sua publicação agora so pode ser vista por você", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(TimeLineActivity.this, "Não conseguimos modificar", Toast.LENGTH_SHORT).show();
                }
            }
        });
        }else{
            getUser();
            Toast.makeText(TimeLineActivity.this, "Houve um erro tente novamente", Toast.LENGTH_SHORT).show();
        }
     }

    private void shotUsers(){
        try{
            mSwipeRefreshLayout.setRefreshing(true);
            ref.child(Chave.PRIMARIA)
                    .child(Chave.USUARIO)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue() != null){
                                List<Message> msgList = new ArrayList<>();
                                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                    User user = dataSnapshot1.getValue(User.class);
                                    if(user != null){
                                        if(user.isMsgApoio()){
                                            Message msg = new Message();
                                            msg.setData(user.getDataMsg());
                                            msg.setMsg(user.getMsg());
                                            msg.setNome(user.getName());
                                            if(user.getAvatar() != null) {
                                                msg.setPhoto(user.getAvatar());
                                            }
                                            msgList.add(msg);
                                        }
                                    }
                                }
                                listar(msgList);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        }catch (Exception x){

        }
    }

    private void listar(List<Message> msgList) {
        mSwipeRefreshLayout.setRefreshing(false);
        db.apagar(0, true);
        db.adicionar(msgList);
        List lista = db.lista();
        AdapterMessage adapterMessage= new AdapterMessage(lista, this);
        mRecyclerView.setAdapter(adapterMessage);
        if(sort!= null){
            sort.setVisible(lista.size() >= 2);
        }
        ((TextView)findViewById(R.id.notitemliste)).setVisibility(lista.size() == 0 ? View.VISIBLE : View.GONE);
        mRecyclerView.setVisibility(lista.size() > 0 ? View.VISIBLE : View.GONE);
    }
}