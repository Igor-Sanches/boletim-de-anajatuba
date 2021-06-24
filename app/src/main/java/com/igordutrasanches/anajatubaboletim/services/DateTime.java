package com.igordutrasanches.anajatubaboletim.services;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTime {

    private Context context;

    public DateTime(Context context){
        this.context = context;
    }

    public static DateTime Now(Context context){
        return new DateTime(context);
    }

    public String toString(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
        return format.format(new Date());
    }

    public String toStringResult(String data){
        String[] separator = data.split(" ");
        String[] separator2 = separator[0].split("/");
        String dia = separator2[1];
        String mes = separator2[0];
        String ano = separator2[2];
        String[] hrs = separator[1].split(":");
        String hr = hrs[0] + ":" + hrs[1];
        return dia + "/" + mes + "/" + ano + " " + hr;
    }


    public String toString(String date){
        SimpleDateFormat format = new SimpleDateFormat(date, Locale.US);
        return format.format(new Date());
    }

}