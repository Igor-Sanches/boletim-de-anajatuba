package com.igordutrasanches.anajatubaboletim.models;

public class Donate {
    private String doador, valor, msg, data, uid, photo;
    private long id;

    public Donate(){}

    public Donate(long id, String doador, String valor, String data, String msg, String photo, String uid) {
        this.doador = doador;
        this.valor = valor;
        this.msg = msg;
        this.data = data;
        this.uid = uid;
        this.photo = photo;
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDoador() {
        return doador;
    }

    public void setDoador(String doador) {
        this.doador = doador;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
