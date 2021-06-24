package com.igordutrasanches.anajatubaboletim.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.igordutrasanches.anajatubaboletim.R;
import com.igordutrasanches.anajatubaboletim.manager.ImageUtils;
import com.igordutrasanches.anajatubaboletim.models.Localidades;
import com.igordutrasanches.anajatubaboletim.models.Message;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class AdapterMessage extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Message> messages;
        private Context context;
        private int animation = 2, lastPosition = -1;
        public boolean on_atach = true;

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView name, msg;
        private CircularImageView photo;
        private ImageButton copy;
        public View layout;

        public OriginalViewHolder(@NonNull View root) {
            super(root);
            name = root.findViewById(R.id.name);
            photo = root.findViewById(R.id.photo);
            copy = root.findViewById(R.id.copy);
            msg = root.findViewById(R.id.msg);
            layout = root.findViewById(R.id.layout);
        }
    }

    public void clear() {
            if(messages.size() > 0){
                messages.clear();
                notifyDataSetChanged();
            }
        }

    public AdapterMessage(List<Message> mMessage, Context context) {
        this.messages = mMessage;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OriginalViewHolder(LayoutInflater.from(context).inflate(R.layout.timelime_view, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof OriginalViewHolder){
            try{
                OriginalViewHolder viewHolder = (OriginalViewHolder)holder;
                Message mMessages = (Message) messages.get(position);
                viewHolder.msg.setText(mMessages.getMsg());
                viewHolder.name.setText(mMessages.getNome());
                ImageUtils.displayImageFromUrl(context, mMessages.getPhoto(), viewHolder.photo, context.getResources().getDrawable(R.drawable.app_icone));
                viewHolder.copy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)).setText(messages.get(position).getMsg());
                        Toast.makeText(context, "Copiado com sucesso!", Toast.LENGTH_SHORT).show();

                    }
                });
            }catch (Exception x){
                Toast.makeText(context, x.getMessage(), Toast.LENGTH_SHORT).show();
            }


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
        return messages.size();
    }
}



