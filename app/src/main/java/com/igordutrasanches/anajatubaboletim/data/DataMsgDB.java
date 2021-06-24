package com.igordutrasanches.anajatubaboletim.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.igordutrasanches.anajatubaboletim.models.Donate;
import com.igordutrasanches.anajatubaboletim.models.Message;

import java.util.LinkedList;
import java.util.List;

public class DataMsgDB extends SQLiteOpenHelper {
    private static final String TABELA = "scan_scan", ID = "rowid", NAME = "NAME", DATE = "DATE", MSG = "MSG", PHOTO="PHOTO";

    private final String[] dados = new String[]{ID, NAME, DATE, MSG, PHOTO};
    private Context context;

    public DataMsgDB(Context mContext){
        super(mContext, "IGOR_SANCHES", null, 3);
        this.context = mContext;
    }

    public List<Donate> lista(){
        SQLiteDatabase sqLiteDatabase;
        String ordem = "ASC";
        String por = DATE;
        Cursor cursor;
        LinkedList lista = new LinkedList();
        switch (Data.getSortingMessageApoio(context)){
            case 0:
                por = DATE;
                ordem = "DESC";
                break;
            case 1:
                por = DATE;
                ordem = "ASC";
                break;
        }
        if((cursor = (sqLiteDatabase = this.getWritableDatabase()).rawQuery("SELECT rowid,* FROM " + TABELA + " ORDER BY " + por + " " + ordem, null)).moveToFirst()){
            do{
                lista.add(new Message(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return lista;
    }

    public void adicionar(List<Message> index){
        for(Message info : index){////
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            ContentValues valores = new ContentValues();
            valores.put(NAME, info.getNome());
            valores.put(DATE, info.getData());
            valores.put(MSG, info.getMsg());
            valores.put(PHOTO, info.getPhoto());
            sqLiteDatabase.insert(TABELA, null, valores);
            sqLiteDatabase.close();
        }
        //Toast.makeText(mContext, Resource.getString(R.string.salvar_nota, mContext), Toast.LENGTH_LONG).show();
    }

    public void apagar(long id, boolean deletaAll){
        SQLiteDatabase sql = this.getReadableDatabase();
        if(deletaAll){
            sql.delete(TABELA, null, null);
        }else{
            String[] ids = new String[]{String.valueOf(id)};
            sql.delete(TABELA, ID + " = ?", ids);
            sql.close();
        }
        // if(show)
        //   Toast.makeText(mContext, Resource.getString(R.string.nota_apagada, mContext), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABELA + " (" + NAME + " TEXT, " +DATE + " TEXT, " +MSG + " TEXT, " +PHOTO + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i == 2 && i1 == 3){
            sqLiteDatabase.execSQL("ALTER TABLE " + TABELA + " ADD COLUMN " + DATE + " TEXT;");
        }
    }
}