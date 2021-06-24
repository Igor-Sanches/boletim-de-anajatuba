package com.igordutrasanches.anajatubaboletim.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.SkuDetails;
import com.igordutrasanches.anajatubaboletim.R;
import com.igordutrasanches.anajatubaboletim.activity.DonateActivity;
import com.igordutrasanches.anajatubaboletim.models.DonateClient;
import com.igordutrasanches.anajatubaboletim.models.Produto;

import java.util.List;

public class ProdutoDonateAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Produto> skuDetailsList;
    public boolean on_atach = true;
    private IProductClickListener onIProductClickListener;
    private Context context;

    public void setOnIProductClickListener(IProductClickListener onIProductClickListener) {
        this.onIProductClickListener = onIProductClickListener;
    }

    public ProdutoDonateAdapter(List<Produto> skuDetailsList, Context context) {
        this.context=context;
        this.skuDetailsList=skuDetailsList;
    }


    public interface IProductClickListener{
        void onProductClickListener(View v, Produto produto, int position);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView donateValor;
        public CardView layout;


        public OriginalViewHolder(@NonNull View root) {
            super(root);
            donateValor = root.findViewById(R.id.donateValor);
            layout = root.findViewById(R.id.layout);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OriginalViewHolder(LayoutInflater.from(context).inflate(R.layout.donate_client_view, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        try{
            if(holder instanceof OriginalViewHolder) {
                OriginalViewHolder viewHolder = (OriginalViewHolder) holder;
                final Produto _mDonateClient = (Produto) skuDetailsList.get(position);
                viewHolder.donateValor.setText(_mDonateClient.getName());
                viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onIProductClickListener.onProductClickListener(v, _mDonateClient, position);
                    }
                });
            }
            }catch (Exception x){
                Toast.makeText(context, x.getMessage(), Toast.LENGTH_SHORT).show();
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
        return skuDetailsList.size();
    }
}



