package com.igordutrasanches.anajatubaboletim.models;

public class Message {
    private String photo, nome, data, msg;
    private long id;

    public Message(){

    }

    public Message(long id, String nome, String data, String msg, String photo) {
        this.photo = photo;
        this.nome = nome;
        this.data = data;
        this.msg = msg;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
