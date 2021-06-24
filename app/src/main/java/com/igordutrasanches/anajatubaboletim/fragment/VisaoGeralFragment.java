package com.igordutrasanches.anajatubaboletim.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.igordutrasanches.anajatubaboletim.R;
import com.igordutrasanches.anajatubaboletim.activity.ActivityUpdate;
import com.igordutrasanches.anajatubaboletim.activity.FaixaEtariaActivity;
import com.igordutrasanches.anajatubaboletim.activity.GraficosActivity;
import com.igordutrasanches.anajatubaboletim.data.Chave;
import com.igordutrasanches.anajatubaboletim.data.Data;
import com.igordutrasanches.anajatubaboletim.models.Boletim;
import com.igordutrasanches.anajatubaboletim.services.Conexao;
import com.igordutrasanches.anajatubaboletim.services.FirebaseUtils;
import com.igordutrasanches.anajatubaboletim.tools.MyToast;


public class VisaoGeralFragment extends Fragment {

    private TextView update, Suspeitos, Descartados, Confirmados, TotalAtivos, Recuperados, Obitos, PCR, TesteRapido, TotaldeTestes, Homem, Mulher;
    private ProgressBar DescartadosProgressBar, ConfirmadosProgressBar, TotalAtivosProgressBar, RecuperadosProgressBar, ObitosProgressBar, PCRProgressBar, TesteRapidoProgressBar, HomemProgressBar, MulherProgressBar;
    private TextView DescartadosProgressBarPorcent, ConfirmadosProgressBarPorcent, TotalAtivosProgressBarPorcent, RecuperadosProgressBarPorcent, ObitosProgressBarPorcent, PCRProgressBarPorcent, TesteRapidoProgressBarPorcent, HomemProgressBarPorcent, MulherProgressBarPorcent;

    private SwipeRefreshLayout refress;
    private LinearLayout layoutView;
    private CardView cardFaixa, graficos;

    private View root;

    public VisaoGeralFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root=  inflater.inflate(R.layout.fragment_visao_geral, container, false);
        try{
            layoutView=root.findViewById(R.id.layoutView);
            refress=root.findViewById(R.id.refress);
            update = root.findViewById(R.id.update);
            Suspeitos = root.findViewById(R.id.Suspeitos);
            Descartados = root.findViewById(R.id.Descartados);
            Confirmados = root.findViewById(R.id.Confirmados);
            TotalAtivos = root.findViewById(R.id.TotalAtivos);
            Recuperados = root.findViewById(R.id.Recuperados);
            Obitos = root.findViewById(R.id.Obitos);
            PCR = root.findViewById(R.id.PCR);
            TesteRapido = root.findViewById(R.id.TesteRapido);
            TotaldeTestes = root.findViewById(R.id.TotaldeTestes);
            Homem = root.findViewById(R.id.Homem);
            Mulher = root.findViewById(R.id.Mulher);

            graficos = root.findViewById(R.id.graficos);
            graficos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), graficos, "cardGrafico");
                            startGraficoActivity(activityOptionsCompat);
                        }
                    });

            DescartadosProgressBar = root.findViewById(R.id.DescartadosProgressBar);
            ConfirmadosProgressBar = root.findViewById(R.id.ConfirmadosProgressBar);
            TotalAtivosProgressBar = root.findViewById(R.id.TotalAtivosProgressBar);
            RecuperadosProgressBar = root.findViewById(R.id.RecuperadosProgressBar);
            ObitosProgressBar = root.findViewById(R.id.ObitosProgressBar);
            PCRProgressBar = root.findViewById(R.id.PCRProgressBar);
            TesteRapidoProgressBar = root.findViewById(R.id.TesteRapidoProgressBar);
            HomemProgressBar = root.findViewById(R.id.HomemProgressBar);
            MulherProgressBar = root.findViewById(R.id.MulherProgressBar);

            DescartadosProgressBarPorcent = root.findViewById(R.id.DescartadosProgressBarPorcent);
            ConfirmadosProgressBarPorcent = root.findViewById(R.id.ConfirmadosProgressBarPorcent);
            TotalAtivosProgressBarPorcent = root.findViewById(R.id.TotalAtivosProgressBarPorcent);
            RecuperadosProgressBarPorcent = root.findViewById(R.id.RecuperadosProgressBarPorcent);
            ObitosProgressBarPorcent = root.findViewById(R.id.ObitosProgressBarPorcent);
            PCRProgressBarPorcent = root.findViewById(R.id.PCRProgressBarPorcent);
            TesteRapidoProgressBarPorcent = root.findViewById(R.id.TesteRapidoProgressBarPorcent);
            HomemProgressBarPorcent = root.findViewById(R.id.HomemProgressBarPorcent);
            MulherProgressBarPorcent = root.findViewById(R.id.MulherProgressBarPorcent);

            showLoader();
            refress.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    showLoader();
                }
            });

            cardFaixa = root.findViewById(R.id.faxaEtariaBtn);
            cardFaixa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), ((TextView)root.findViewById(R.id.cardTitle)), "cardTitle");
                    startFEActivity(activityOptionsCompat);

                }
            });

        }catch (Exception x){
            toast(x.getMessage(), 2);
        }
        return root;
    }

    private void startGraficoActivity(ActivityOptionsCompat activityOptionsCompat) {
        try{
            Intent in = new Intent(getActivity(), GraficosActivity.class);
            startActivity(in,activityOptionsCompat.toBundle());
        }catch (Exception x){
            toast(x.getMessage(), 2);
        }
    }

    private void startFEActivity(ActivityOptionsCompat activityOptionsCompat) {
        Intent in = new Intent(getActivity(), FaixaEtariaActivity.class);
        startActivity(in,activityOptionsCompat.toBundle());
    }

    private void showLoader() {
        try{
            refress.setRefreshing(true);
            layoutView.setVisibility(View.GONE);
            initLoader();
        }catch (Exception x){
            toast(x.getMessage(), 2);
        }
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.getValue() != null){
                Boletim mBoletim = (Boletim)dataSnapshot.getValue(Boletim.class);
                if(mBoletim != null){
                    Data.setDataUpate(mBoletim.getUpdate(), getActivity());
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
            if(!Conexao.isConnected(getActivity())) {
                toast("Sem conexão com a internet para atualizar", 2);

        }
        }catch (Exception x) {
            toast(x.getMessage(), 2);
        }
    }

    private void setData(Boletim mBoletim) {
     try{   try{
            int version = 9;
            try {
                version = getActivity().getPackageManager().getPackageInfo((String)getActivity().getPackageName(), (int) 0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
             if(version >= mBoletim.getVersion()){
                setVisaoGeral(mBoletim);
                setVisaoTestes(mBoletim);
                setVisaoSexo(mBoletim);
                refress.setRefreshing(false);
                layoutView.setVisibility(View.VISIBLE);
            }else{
                getActivity().startActivity(new Intent(getActivity(), ActivityUpdate.class));
                getActivity().finish();
            }

        }catch (Exception x){
            toast(x.getMessage(), 2);
        }
     }catch (Exception x){
         toast(x.getMessage(), 2);
     }
    }

    private void setVisaoGeral(Boletim mBoletim) {
       try{ String _TotaldeTestes = mBoletim.getTotalTestes();
        int _valueMaxTotaldeTestes = Integer.valueOf(_TotaldeTestes);
        update.setText(mBoletim.getUpdate());
        String _Suspeitos =mBoletim.getSuspeitos();
        String _Confirmados = mBoletim.getConfirmados();
        String _TotalAtivos  = mBoletim.getAtivos();
        String _Recuperados = mBoletim.getRecuperados();
        String _Obitos = mBoletim.getObitos();
        String _Descartados = mBoletim.getDescartados();
        int _maxDescardatos = Integer.valueOf(_Descartados);
        int _maxConfirmados = Integer.valueOf(_Confirmados);
        int _valueTotalAtivoss = Integer.valueOf(_TotalAtivos);
        int _valueRecuperados = Integer.valueOf(_Recuperados);
        int _valueObitos = Integer.valueOf(_Obitos);
        Suspeitos.setText(_Suspeitos);
        Descartados.setText(_Descartados);
        DescartadosProgressBar.setMax(_valueMaxTotaldeTestes);
        DescartadosProgressBar.setProgress(_maxDescardatos);
        DescartadosProgressBarPorcent.setText(getPorcent(_maxDescardatos,_valueMaxTotaldeTestes));
        Confirmados.setText(_Confirmados);
        ConfirmadosProgressBar.setMax(_valueMaxTotaldeTestes);
        ConfirmadosProgressBar.setProgress(_maxConfirmados);
        ConfirmadosProgressBarPorcent.setText(getPorcent(_maxConfirmados,_valueMaxTotaldeTestes));
        TotalAtivos.setText(_TotalAtivos);
        TotalAtivosProgressBar.setMax(_maxConfirmados);
        TotalAtivosProgressBar.setProgress(_valueTotalAtivoss);
        TotalAtivosProgressBarPorcent.setText(getPorcent(_valueTotalAtivoss,_maxConfirmados));
        Recuperados.setText(_Recuperados);
        RecuperadosProgressBar.setMax(_maxConfirmados);
        RecuperadosProgressBar.setProgress(_valueRecuperados);
        RecuperadosProgressBarPorcent.setText(getPorcent(_valueRecuperados,_maxConfirmados));
        Obitos.setText(_Obitos);
        ObitosProgressBar.setMax(_maxConfirmados);
        ObitosProgressBar.setProgress(_valueObitos);
        ObitosProgressBarPorcent.setText(getPorcent(_valueObitos,_maxConfirmados));
       }catch (Exception x){
           toast(x.getMessage(), 2);
       }
    }

    private void setVisaoTestes(Boletim mBoletim) {
        String _TotaldeTestes = mBoletim.getTotalTestes();
        String _TesteRapido = mBoletim.getTestsRapidos();
        String _PCR = mBoletim.getPCR();
        int _valueMaxTotaldeTestes = Integer.valueOf(_TotaldeTestes);
        int _valueMaxTesteRapido = Integer.valueOf(_TesteRapido);
        int _valueMaxPCR = Integer.valueOf(_PCR);
        TotaldeTestes.setText(_TotaldeTestes);
        TesteRapido.setText(_TesteRapido);
        TesteRapidoProgressBar.setMax(_valueMaxTotaldeTestes);
        TesteRapidoProgressBar.setProgress(_valueMaxTesteRapido);
        TesteRapidoProgressBarPorcent.setText(getPorcent(_valueMaxTesteRapido,_valueMaxTotaldeTestes));

        PCR.setText(_PCR);
        PCRProgressBar.setMax(_valueMaxTotaldeTestes);
        PCRProgressBar.setProgress(_valueMaxPCR);
        PCRProgressBarPorcent.setText(getPorcent(_valueMaxPCR,_valueMaxTotaldeTestes));
    }

    private void setVisaoSexo(Boletim mBoletim) {
        String valueMulher =mBoletim.getMulher();
        String valueHomem =mBoletim.getHomem();
        int _mulher = Integer.valueOf(valueMulher);
        int _Homem = Integer.valueOf(valueHomem);
        int max = Integer.valueOf(mBoletim.getConfirmados());
        Homem.setText(valueHomem);
        HomemProgressBar.setMax(max);
        HomemProgressBar.setProgress(_Homem);
        HomemProgressBarPorcent.setText(getPorcent(_Homem, max));
        Mulher.setText(valueMulher);
        MulherProgressBar.setMax(max);
        MulherProgressBar.setProgress(_mulher);
        MulherProgressBarPorcent.setText(getPorcent(_mulher, max));
    }


    private String getPorcent(int value_, int max){
        int porcent = (100 * value_ / max);
        String value = String.valueOf(porcent);
        if(value.length() >= 2){
            value = value.substring(0,2);
        }else if(value.length() == 1){
            value = "0,"+ value;
        }
        return value + "%";
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
