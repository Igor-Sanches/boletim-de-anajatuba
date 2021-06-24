package com.igordutrasanches.anajatubaboletim.conta;

import com.google.firebase.auth.FirebaseUser;
import com.igordutrasanches.anajatubaboletim.services.DateTime;

import java.io.Serializable;

public class User implements Serializable {
    private String avatar;
    private String email;
    private String name;
    private Boolean donate = false;
    private Boolean msgApoio = false;
    private Boolean bam = false;
    private String dataMsg = "";
    private String msg = "";
    private String uid;
    private String data = "";
    private String localidade = "";
    private String genero = "";

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getDataMsg() {
        return dataMsg;
    }

    public User(int i) {
        this.avatar = null;
        this.email = null;
        this.name = null;
        this.donate = null;
        this.msgApoio = null;
        this.bam = null;
        this.dataMsg = null;
        this.msg = null;
        this.uid = null;
        this.data = null;
    }

    public void setDataMsg(String dataMsg) {
        this.dataMsg = dataMsg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isMsgApoio() {
        return msgApoio;
    }

    public void setMsgApoio(boolean msgApoio) {
        this.msgApoio = msgApoio;
    }

    public boolean isBam() {
        return bam;
    }

    public void setBam(boolean bam) {
        this.bam = bam;
    }

    public boolean isDonate() {
        return donate;
    }

    public void setDonate(boolean donate) {
        this.donate = donate;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public User() {
    }

    public User(FirebaseUser firebaseUser) {
        this.uid = firebaseUser.getUid();
        this.email = firebaseUser.getEmail();
        if(firebaseUser.getDisplayName() != null){
            this.name = firebaseUser.getDisplayName();
        }
        if(firebaseUser.getPhotoUrl()!= null){
            this.avatar = firebaseUser.getPhotoUrl().toString();
        }
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String str) {
        this.avatar = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String str) {
        this.uid = str;
    }
}
