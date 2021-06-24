package com.igordutrasanches.anajatubaboletim.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.igordutrasanches.anajatubaboletim.R;
import com.igordutrasanches.anajatubaboletim.models.Donate;
import com.igordutrasanches.anajatubaboletim.models.Localidades;

import java.util.List;

public class AdapterDanates extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Donate> mDonate;
        private Context context;
        public OnLocalidadesClickListener mOnLocalidadesClickListener;
        private int animation = 2, lastPosition = -1;
        public boolean on_atach = true;

    public interface OnLocalidadesClickListener{
        void OnLocalidadesClickListener(View view, Donate mDonate, int position);
    }

    public void setOnContatoClickListener(OnLocalidadesClickListener mOnLocalidadesClickListener){
        this.mOnLocalidadesClickListener = mOnLocalidadesClickListener;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView displayName, cassos;


        public OriginalViewHolder(@NonNull View root) {
            super(root);
            displayName = root.findViewById(R.id.displayName);
           // cassos = root.findViewById(R.id.cassos);
            
        }
    }

    public void clear() {
            if(mDonate.size() > 0){
                mDonate.clear();
                notifyDataSetChanged();
            }
        }

    public AdapterDanates(List<Donate> mDonate, Context context) {
        this.mDonate = mDonate;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OriginalViewHolder(LayoutInflater.from(context).inflate(R.layout.donate_layout_list, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof OriginalViewHolder){
            OriginalViewHolder viewHolder = (OriginalViewHolder)holder;
            Donate _Donate = (Donate) mDonate.get(position);


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
        return mDonate.size();
    }
}



