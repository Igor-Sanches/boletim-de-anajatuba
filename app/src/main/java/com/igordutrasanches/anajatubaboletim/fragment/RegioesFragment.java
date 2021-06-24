package com.igordutrasanches.anajatubaboletim.fragment;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.igordutrasanches.anajatubaboletim.R;
import com.igordutrasanches.anajatubaboletim.adapter.AdapterLocalidades;
import com.igordutrasanches.anajatubaboletim.data.Chave;
import com.igordutrasanches.anajatubaboletim.data.Data;
import com.igordutrasanches.anajatubaboletim.data.DataLocalidadesDB;
import com.igordutrasanches.anajatubaboletim.models.Localidades;
import com.igordutrasanches.anajatubaboletim.services.DateTime;
import com.igordutrasanches.anajatubaboletim.services.FirebaseUtils;
import com.igordutrasanches.anajatubaboletim.tools.MyToast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RegioesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerListFrends;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View root;
    private LinearLayout layout;
    private DataLocalidadesDB mDataLocalidadesDB;
    private AdapterLocalidades mAdapterLocalidades;
    private TextView texNd;


    public RegioesFragment() {

    }

    private static RegioesFragment instance;
    public static RegioesFragment getInstance(){
        return instance;
    }

    public void showSorting() {
        try{
            final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
            String[] sorting = new String[]{ "Novo", "Positivos", "Ativos", "Recuperados", "Óbitos"};
            LinearLayout ll = new LinearLayout(getActivity());
            ll.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            p.setMargins(10,10,10,10);
            ll.setLayoutParams(p);
            for(int i =0; i < sorting.length; i++){
                Button btn = new Button(getActivity());
                btn.setTag(i);
                btn.setText(sorting[i]);
                TypedValue value = new TypedValue();
                getActivity().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, value, true);
                btn.setBackgroundResource(value.resourceId);
                btn.setAllCaps(false);

                if(Integer.valueOf(btn.getTag().toString()) == Data.getSortingLocalidade(getActivity())){
                    btn.setTextColor(getResources().getColor(R.color.colorBlueAccent));
                }

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button btn = (Button)v;
                        Data.setSortingLocalidade(Integer.valueOf(btn.getTag().toString()), getActivity());
                        dialog.dismiss();
                        showDataList();
                    }
                });
                ll.addView(btn);
            }
            dialog.setView(ll);
            dialog.show();

        }catch (Exception x){
            toast(x.getMessage(), 2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try{
            root = inflater.inflate(R.layout.fragment_regioes, container, false);
            instance = this;
            texNd=root.findViewById(R.id.texNd);
            mSwipeRefreshLayout=root.findViewById(R.id.mSwipeRefreshLayout);
            mDataLocalidadesDB = new DataLocalidadesDB(getActivity());
            recyclerListFrends=root.findViewById(R.id.recyclerListFrends);
            layout=root.findViewById(R.id.layout);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerListFrends.setLayoutManager(linearLayoutManager);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            showDataList();
        }catch (Exception x){
            toast(x.getMessage(), 2);
        }
        return root;
    }

    private void showDataList() {
        try{
            layout.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(true);

            FirebaseUtils.getDatabaseRef().child(Chave.PRIMARIA).child(Chave.LOCALIDADES).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null){
                        try{
                            List<Localidades> _Localidades = new ArrayList<>();
                            for(DataSnapshot local : dataSnapshot.getChildren()){
                                Localidades _local = local.getValue(Localidades.class);
                                _Localidades.add(_local);
                            }
                            lista(_Localidades);
                        }catch (Exception x){
                            toast(x.getMessage(), 2);
                        }
                    }else{
                        texNd.setVisibility(View.VISIBLE);
                        recyclerListFrends.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }catch (Exception x){
            toast(x.getMessage(), 2);
        }
    }

    private void lista(List<Localidades> localidadesList) {
        try{
            mDataLocalidadesDB.apagar(0, true);
            mDataLocalidadesDB.adicionar(localidadesList);
            List lista = mDataLocalidadesDB.lista();
            mAdapterLocalidades = new AdapterLocalidades(lista, getActivity());
            recyclerListFrends.setAdapter(mAdapterLocalidades);

            layout.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setRefreshing(false);
            if(lista.size() > 0){
                texNd.setVisibility(View.GONE);
                recyclerListFrends.setVisibility(View.VISIBLE);
            }else{
                texNd.setVisibility(View.VISIBLE);
                recyclerListFrends.setVisibility(View.GONE);

            }
        }catch (Exception x){
            toast(x.getMessage(), 2);
        }
    }

    @Override
    public void onRefresh() {
        showDataList();
    }

    private void toast(String msg, int x){
        int index = 0;
        switch (x){
            case 0: index = MyToast.NOME; break;
            case 1: index = MyToast.SUCCESS; break;
            case 2: index = MyToast.ERROR; break;
        }
        MyToast.makeText(getActivity(), msg, index).show();
    }
}
