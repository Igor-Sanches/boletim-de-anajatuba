package com.igordutrasanches.anajatubaboletim.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.igordutrasanches.anajatubaboletim.models.Donate;
import com.igordutrasanches.anajatubaboletim.models.Localidades;

import java.util.LinkedList;
import java.util.List;

public class DataDonateDB extends SQLiteOpenHelper {
    private static final String TABELA = "scan_scan", ID = "rowid", DONATE_NAME = "TITLE", DATE = "DATE", VALOR = "CASSOS", MSG="MSG", PHOTO="PHOTO", UID="UID";

    private final String[] dados = new String[]{ID, DONATE_NAME, VALOR, DATE, MSG, PHOTO, UID};
    private Context context;

    public DataDonateDB(Context mContext){
        super(mContext, "IGOR_SANCHES", null, 3);
        this.context = mContext;
    }

    public long getID(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT count(*) FROM " + TABELA, null);
        long id = -1L;
        if(cursor.moveToFirst()){
            do {
                id = cursor.getLong(0);
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return id;
    }

    public Donate getID(long id){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Donate info;
        String[] isDados = dados;;
        String[] isDadosDB = new String[]{String.valueOf(id)};
        Cursor cursor = sqLiteDatabase.query(TABELA, isDados, ID + " = ?", isDadosDB, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
                    info = new Donate(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
            cursor.close();
        }
        else{
            info = null;
        }
        sqLiteDatabase.close();
        return info;
    }

    public List<Donate> lista(){
        SQLiteDatabase sqLiteDatabase;
        String ordem = "ASC";
        String por = DONATE_NAME;
        Cursor cursor;
        LinkedList lista = new LinkedList();
        int i = 2;
        switch (i){
            case 0:
                por = DONATE_NAME;
                ordem = "ASC";
                break;
            case 1:
                por = DONATE_NAME;
                ordem = "DESC";
                break;
            case 2:
                por = VALOR;
                ordem = "DESC";
                break;
            case 3:
                por = VALOR;
                ordem = "ASC";
                break;
        }
        if((cursor = (sqLiteDatabase = this.getWritableDatabase()).rawQuery("SELECT rowid,* FROM " + TABELA + " ORDER BY " + por + " " + ordem, null)).moveToFirst()){
            do{
                lista.add(new Donate(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return lista;
    }

    public void adicionar(List<Donate> index){
        for(Donate info : index){////
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            ContentValues valores = new ContentValues();
            valores.put(DONATE_NAME, info.getDoador());
            valores.put(VALOR, info.getValor());
            valores.put(DATE, info.getData());
            valores.put(MSG, info.getMsg());
            valores.put(PHOTO, info.getPhoto());
            valores.put(UID, info.getUid());
            sqLiteDatabase.insert(TABELA, null, valores);
            sqLiteDatabase.close();
        }
        //Toast.makeText(mContext, Resource.getString(R.string.salvar_nota, mContext), Toast.LENGTH_LONG).show();
    }

    public void atualizar(Donate info){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(DONATE_NAME, info.getDoador());
        valores.put(VALOR, info.getValor());
        valores.put(DATE, info.getData());
        valores.put(MSG, info.getMsg());
        valores.put(PHOTO, info.getPhoto());
        valores.put(UID, info.getUid());
        String[] id = new String[]{String.valueOf(info.getId())};
        sqLiteDatabase.update(TABELA, valores, ID + " = ?", id);
        sqLiteDatabase.close();
        //Toast.makeText(mContext, Resource.getString(R.string.nota_atualizada, mContext), Toast.LENGTH_LONG).show();
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
        sqLiteDatabase.execSQL("CREATE TABLE " + TABELA + " (" + DONATE_NAME + " TEXT, " + VALOR + " TEXT, " +DATE + " TEXT, " +MSG + " TEXT, " +PHOTO + " TEXT, " + UID + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i == 2 && i1 == 3){
            sqLiteDatabase.execSQL("ALTER TABLE " + TABELA + " ADD COLUMN " + VALOR + " TEXT;");
        }
    }
}