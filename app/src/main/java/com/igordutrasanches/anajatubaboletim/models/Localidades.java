package com.igordutrasanches.anajatubaboletim.models;

public class Localidades {
    public String nome, idDefault, data, uid, ativos, recuperados, obitos, positivos, total;
    private long id;

    public Localidades(long id, String uid, String nome, String data, String positivos, String recuperados, String ativos, String obitos, String idDefault) {
        this.nome = nome;
        this.data = data;
        this.uid = uid;
        this.ativos = ativos;
        this.recuperados = recuperados;
        this.obitos = obitos;
        this.positivos = positivos;
        this.id = id;
        this.idDefault=idDefault;
    }


    public Localidades(String nome, String data, String uid, String positivos, String recuperados, String ativos, String obitos, String idDefault) {
        this.nome = nome;
        this.data = data;
        this.uid = uid;
        this.idDefault=idDefault;
        this.ativos = ativos;
        this.recuperados = recuperados;
        this.obitos = obitos;
        this.positivos = positivos;
    }

    public String getIdDefault(){
        return idDefault;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAtivos() {
        return ativos;
    }

    public void setAtivos(String ativos) {
        this.ativos = ativos;
    }

    public String getRecuperados() {
        return recuperados;
    }

    public void setRecuperados(String recuperados) {
        this.recuperados = recuperados;
    }

    public String getObitos() {
        return obitos;
    }

    public void setObitos(String obitos) {
        this.obitos = obitos;
    }

    public String getPositivos() {
        return positivos;
    }

    public void setPositivos(String positivos) {
        this.positivos = positivos;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Localidades(){

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
