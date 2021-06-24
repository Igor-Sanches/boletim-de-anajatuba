package com.igordutrasanches.anajatubaboletim.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.igordutrasanches.anajatubaboletim.R;
import com.igordutrasanches.anajatubaboletim.data.Chave;
import com.igordutrasanches.anajatubaboletim.models.Boletim;
import com.igordutrasanches.anajatubaboletim.services.FirebaseUtils;

import java.util.ArrayList;

public class GraficosActivity extends AppCompatActivity {

    private PieChart graficoPizza;
    private BarChart graficoBarra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_graficos);
            graficoPizza=findViewById(R.id.graficoPizza);
            graficoBarra=findViewById(R.id.graficoBarra);
            initLoader();
            ((ImageButton)findViewById(R.id.backBtn))
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
        }catch (Exception x){
            Toast.makeText(this, x.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void criarGraficoPizza(Boletim mBoletim) {
        String TotaldeTestes = mBoletim.getTotalTestes();
        String Suspeitos =mBoletim.getSuspeitos();
        String Confirmados = mBoletim.getConfirmados();
        String TotalAtivos  = mBoletim.getAtivos();
        String Recuperados = mBoletim.getRecuperados();
        String Obitos = mBoletim.getObitos();
        String Descartados = mBoletim.getDescartados();
        String valueMulher =mBoletim.getMulher();
        String valueHomem =mBoletim.getHomem();
        int _mulher = Integer.valueOf(valueMulher);
        int _Homem = Integer.valueOf(valueHomem);
        int _TotaldeTestes = Integer.valueOf(TotaldeTestes);
        int _Suspeitos = Integer.valueOf(Suspeitos);
        int _Confirmados = Integer.valueOf(Confirmados);
        int _TotalAtivos = Integer.valueOf(TotalAtivos);
        int _Recuperados = Integer.valueOf(Recuperados);
        int _Obitos = Integer.valueOf(Obitos);
        int _valueMaxTotaldeTestes = Integer.valueOf(TotaldeTestes);
        int _Descartados = Integer.valueOf(Descartados);
        graficoPizza.getDescription().setEnabled(false);
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(_Confirmados, _valueMaxTotaldeTestes));
        pieEntries.add(new PieEntry(_Descartados, _valueMaxTotaldeTestes));
        pieEntries.add(new PieEntry(_TotalAtivos, _Confirmados));
        pieEntries.add(new PieEntry(_Recuperados, _Confirmados));
        pieEntries.add(new PieEntry(_Obitos, _Confirmados));
        pieEntries.add(new PieEntry(_mulher, _Confirmados));
        pieEntries.add(new PieEntry(_Homem, _Confirmados));

        PieDataSet pieDateSet = new PieDataSet(pieEntries, "");
        pieDateSet.setColors(getColorsPizza());
        pieDateSet.setLabel("");
        PieData pieData = new PieData(pieDateSet);
        graficoPizza.setData(pieData);

    }

    private void criarGraficoBarra(Boletim mBoletim) {
        graficoBarra.getDescription().setEnabled(false);
        setData(mBoletim);
        graficoBarra.setFitBars(true);

    }

    private void setData(Boletim mBoletim) {

        String TotaldeTestes = mBoletim.getTotalTestes();
        String Suspeitos =mBoletim.getSuspeitos();
        String Confirmados = mBoletim.getConfirmados();
        String TotalAtivos  = mBoletim.getAtivos();
        String Recuperados = mBoletim.getRecuperados();
        String Obitos = mBoletim.getObitos();
        String Descartados = mBoletim.getDescartados();
        String valueMulher =mBoletim.getMulher();
        String valueHomem =mBoletim.getHomem();
        int _mulher = Integer.valueOf(valueMulher);
        int _Homem = Integer.valueOf(valueHomem);
        int _TotaldeTestes = Integer.valueOf(TotaldeTestes);
        int _Suspeitos = Integer.valueOf(Suspeitos);
        int _Confirmados = Integer.valueOf(Confirmados);
        int _TotalAtivos = Integer.valueOf(TotalAtivos);
        int _Recuperados = Integer.valueOf(Recuperados);
        int _Obitos = Integer.valueOf(Obitos);
        int _Descartados = Integer.valueOf(Descartados);
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, _Suspeitos));
        barEntries.add(new BarEntry(2, _Confirmados));
        barEntries.add(new BarEntry(3, _Descartados));
        barEntries.add(new BarEntry(4, _TotalAtivos));
        barEntries.add(new BarEntry(5, _Recuperados));
        barEntries.add(new BarEntry(6, _Obitos));
        barEntries.add(new BarEntry(7, _mulher));
        barEntries.add(new BarEntry(8, _Homem));



        BarDataSet set = new BarDataSet(barEntries, "");
        set.setColors(getColors());
        set.setStackLabels(getLabels());
        set.setDrawValues(false);

        BarData data = new BarData(set);
        graficoBarra.setData(data);
        graficoBarra.invalidate();
        graficoBarra.animateY(500);
    }

    private int[] getColors() {
        int[] color = new int[8];
        color[0] = getResources().getColor(android.R.color.darker_gray);
        color[1] = getResources().getColor(android.R.color.holo_red_dark);
        color[2] = getResources().getColor(android.R.color.holo_purple);
        color[3] = getResources().getColor(android.R.color.holo_orange_dark);
        color[4] = getResources().getColor(android.R.color.holo_blue_dark);
        color[5] = getResources().getColor(android.R.color.black);
        color[6] = getResources().getColor(R.color.pink);
        color[7] = getResources().getColor(android.R.color.holo_blue_bright);
        return color;
    }

    private int[] getColorsPizza() {
        int[] color = new int[7];
        color[0] = getResources().getColor(android.R.color.holo_red_dark);
        color[1] = getResources().getColor(android.R.color.holo_purple);
        color[2] = getResources().getColor(android.R.color.holo_orange_dark);
        color[3] = getResources().getColor(android.R.color.holo_blue_dark);
        color[4] = getResources().getColor(android.R.color.black);
        color[5] = getResources().getColor(R.color.pink);
        color[6] = getResources().getColor(android.R.color.holo_blue_bright);
        return color;
    }

    private String[] getLabels() {
        String[] label = new String[8];
        label[0] = "Suspeitos";
        label[1] = "Confirmados";
        label[2] = "Descartados";
        label[3] = "Ativos";
        label[4] = "Recuperados";
        label[5] = "óbitos";
        label[6] = "Mulher";
        label[7] = "Homem";
        return label;
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.getValue() != null){
                Boletim mBoletim = (Boletim)dataSnapshot.getValue(Boletim.class);
                if(mBoletim != null){
                    criarGraficoPizza(mBoletim);
                    criarGraficoBarra(mBoletim);
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
        }catch (Exception x) {

        }
    }

}