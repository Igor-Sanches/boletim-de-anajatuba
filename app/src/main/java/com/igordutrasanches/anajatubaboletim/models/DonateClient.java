package com.igordutrasanches.anajatubaboletim.models;

public class DonateClient {

    private String uid, valor, titulo;
    private String id;

    public DonateClient(String titulo, String uid, String valor, String id) {
        this.uid = uid;
        this.valor = valor;
        this.titulo = titulo;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
