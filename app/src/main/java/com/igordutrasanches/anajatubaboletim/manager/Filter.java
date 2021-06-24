package com.igordutrasanches.anajatubaboletim.manager;

import android.content.Context;

import com.igordutrasanches.anajatubaboletim.R;

import java.util.ArrayList;

public class Filter {

    public static boolean isText(String myMsg, Context context) {
        String[] lista =context.getResources().getStringArray(R.array.lista);
        boolean ok = false;

       for(int i = 0; i < lista.length; i++){
          ok = myMsg.contains(" " + lista[i] + " ");
       }
        return !ok;
    }
}
