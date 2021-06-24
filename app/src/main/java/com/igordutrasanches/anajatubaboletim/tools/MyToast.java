package com.igordutrasanches.anajatubaboletim.tools;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.igordutrasanches.anajatubaboletim.R;

public class MyToast{

  private Context context;
  public final static int ERROR = 0, SUCCESS = 1, NOME = 2;
  private String msg;
  private int color;

  private MyToast(Context context, String msg, int color){
    this.context = context;
    this.color = color;
    this.msg = msg;
  }

  public static MyToast makeText(Context context, String msg, int color){
    return new MyToast(context, msg, color);
  }

  public void show(){
    int colorTextRes = context.getResources().getColor(R.color.light_grey_100);
    int colorCardRes = 0;
    if(color == ERROR){
      colorCardRes = context.getResources().getColor(android.R.color.holo_red_dark);
    }else if(color == SUCCESS){
      colorCardRes = context.getResources().getColor(android.R.color.holo_green_dark);
    }else{
      colorCardRes = context.getResources().getColor(R.color.colorPrimaryAccent);
    }

    View root = View.inflate(context, R.layout.my_toast_layout, null);
    if(root != null){
      CardView card = root.findViewById(R.id.toastCard);
      TextView text = root.findViewById(R.id.toastTextColor);
      card.setCardBackgroundColor(colorCardRes);
      text.setTextColor(colorTextRes);
      text.setText(msg);
      Toast toast = new Toast(context);
      toast.setView(root);
      toast.setDuration(Toast.LENGTH_LONG);
      toast.show();
    }
  }
}
