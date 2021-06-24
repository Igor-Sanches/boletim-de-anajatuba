package com.igordutrasanches.anajatubaboletim.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.igordutrasanches.anajatubaboletim.R;
import com.igordutrasanches.anajatubaboletim.models.Localidades;

import java.util.List;

public class AdapterLocalidades extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Localidades> mLocalidades;
    private Context context;
    public boolean on_atach = true;


    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView displayName, ativos, recuperados, obitos, positivos;
        public CardView layout;

        public OriginalViewHolder(@NonNull View root) {
            super(root);
            displayName = root.findViewById(R.id.displayName);
            ativos = root.findViewById(R.id.ativos);
            recuperados = root.findViewById(R.id.recuperados);
            obitos = root.findViewById(R.id.obitos);
            positivos = root.findViewById(R.id.positivos);
            layout = root.findViewById(R.id.layout);
        }
    }

    public void clear() {
        if(mLocalidades.size() > 0){
            mLocalidades.clear();
            notifyDataSetChanged();
        }
    }

    public AdapterLocalidades(List<Localidades> mLocalidades, Context context) {
        this.mLocalidades = mLocalidades;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OriginalViewHolder(LayoutInflater.from(context).inflate(R.layout.lista_localidades, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof OriginalViewHolder){
            OriginalViewHolder viewHolder = (OriginalViewHolder)holder;
            Localidades _mLocalidades = (Localidades) mLocalidades.get(position);
            viewHolder.displayName.setText(_mLocalidades.getNome());
            viewHolder.positivos.setText(_mLocalidades.getPositivos());
            viewHolder.ativos.setText(_mLocalidades.getAtivos());
            viewHolder.recuperados.setText(_mLocalidades.getRecuperados());
            viewHolder.obitos.setText(_mLocalidades.getObitos());

        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                on_atach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return mLocalidades.size();
    }
}



