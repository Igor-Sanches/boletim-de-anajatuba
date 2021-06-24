package com.igordutrasanches.anajatubaboletim.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.igordutrasanches.anajatubaboletim.models.Localidades;

import java.util.LinkedList;
import java.util.List;

public class DataLocalidadesDB extends SQLiteOpenHelper {
    private static final String TABELA = "scan_scan", ID = "rowid", UID = "UID", LOCAL = "LOCAL", DATA = "DATA", ATIVOS = "ATIVOS", POSITIVOS = "POSITIVOS", RECUPERADOS = "RECUPERADOS", OBITOS = "OBITOS", ID_DEFAUL = "ID_DEFAUL";

    private final String[] dados = new String[]{ID, UID, LOCAL, DATA, POSITIVOS, RECUPERADOS, ATIVOS, OBITOS, ID_DEFAUL};
    private Context context;

    public DataLocalidadesDB(Context mContext){
        super(mContext, "IGOR_SANCHES_LOCALIDADES", null, 3);
        this.context = mContext;
    }

    public List<Localidades> lista(){
        SQLiteDatabase sqLiteDatabase;
        String ordem = "ASC";
        String por = LOCAL;
        Cursor cursor;
        LinkedList lista = new LinkedList();
        switch (Data.getSortingLocalidade(context)){
            case 0:
                por = ID_DEFAUL;
                ordem = "DESC";
                break;
            case 1:
                por = POSITIVOS;
                ordem = "DESC";
                break;
            case 2:
                por = ATIVOS;
                ordem = "DESC";
                break;
            case 3:
                por = RECUPERADOS;
                ordem = "DESC";
                break;
            case 4:
                por = OBITOS;
                ordem = "DESC";
                break;
        }
        if((cursor = (sqLiteDatabase = this.getWritableDatabase()).rawQuery("SELECT rowid,* FROM " + TABELA + " ORDER BY " + por + " " + ordem, null)).moveToFirst()){
            do{
                lista.add(new Localidades(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return lista;
    }

    public void adicionar(List<Localidades> index){
        for(Localidades info : index){
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            ContentValues valores = new ContentValues();
            valores.put(LOCAL, info.getNome());
            valores.put(DATA, info.getData());
            valores.put(POSITIVOS, info.getPositivos());
            valores.put(ATIVOS, info.getAtivos());
            valores.put(OBITOS, info.getObitos());
            valores.put(RECUPERADOS, info.getRecuperados());
            valores.put(UID, info.getUid());
            valores.put(ID_DEFAUL, info.getIdDefault());
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
        sqLiteDatabase.execSQL("CREATE TABLE " + TABELA + " (" + UID + " , " + LOCAL + " TEXT, " + DATA + " TEXT, " + POSITIVOS + " NUMBER, " + RECUPERADOS + " NUMBER, " + ATIVOS + " NUMBER, " + OBITOS + " NUMBER, " + ID_DEFAUL + " NUMBER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i == 2 && i1 == 3){
            sqLiteDatabase.execSQL("ALTER TABLE " + TABELA + " ADD COLUMN " + UID + " TEXT;");
        }
    }
}