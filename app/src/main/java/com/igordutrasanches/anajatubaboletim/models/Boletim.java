package com.igordutrasanches.anajatubaboletim.models;

public class Boletim {
    private String Ativos, Homem, Mulher, TotalTestes, PCR, TestsRapidos, update, Suspeitos, Confirmados, Monitorados, Recuperados, Obitos, Descartados;
    private int version;
    private String fe_09, fe_1019, fe_2029, fe_3039, fe_4049, fe_5059, fe_6069, fe_70;

    public void setFe_09(String fe_09) {
        this.fe_09 = fe_09;
    }

    public void setFe_1019(String fe_1019) {
        this.fe_1019 = fe_1019;
    }

    public void setFe_2029(String fe_2029) {
        this.fe_2029 = fe_2029;
    }

    public void setFe_3039(String fe_3039) {
        this.fe_3039 = fe_3039;
    }

    public void setFe_4049(String fe_4049) {
        this.fe_4049 = fe_4049;
    }

    public void setFe_5059(String fe_5059) {
        this.fe_5059 = fe_5059;
    }

    public void setFe_6069(String fe_6069) {
        this.fe_6069 = fe_6069;
    }

    public void setFe_70(String fe_70) {
        this.fe_70 = fe_70;
    }

    public int getVersion(){
        return version;
    }

    public String getFe_09() {
        return fe_09;
    }

    public String getFe_1019() {
        return fe_1019;
    }

    public String getFe_2029() {
        return fe_2029;
    }

    public String getFe_3039() {
        return fe_3039;
    }

    public String getFe_4049() {
        return fe_4049;
    }

    public String getFe_5059() {
        return fe_5059;
    }

    public String getFe_6069() {
        return fe_6069;
    }

    public String getFe_70() {
        return fe_70;
    }

    public void setVersion(int version){
        this.version = version;
    }

    public String getAtivos() {
        return Ativos;
    }

    public void setAtivos(String ativos) {
        Ativos = ativos;
    }

    public String getUpdate() {
        return update;
    }

    public String getHomem() {
        return Homem;
    }

    public void setHomem(String homem) {
        Homem = homem;
    }

    public String getMulher() {
        return Mulher;
    }

    public void setMulher(String mulher) {
        Mulher = mulher;
    }

    public String getTotalTestes() {
        return TotalTestes;
    }

    public void setTotalTestes(String totalTestes) {
        TotalTestes = totalTestes;
    }

    public String getPCR() {
        return PCR;
    }

    public void setPCR(String PCR) {
        this.PCR = PCR;
    }

    public String getTestsRapidos() {
        return TestsRapidos;
    }

    public void setTestsRapidos(String testsRapidos) {
        TestsRapidos = testsRapidos;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getSuspeitos() {
        return Suspeitos;
    }

    public void setSuspeitos(String suspeitos) {
        Suspeitos = suspeitos;
    }

    public String getConfirmados() {
        return Confirmados;
    }

    public void setConfirmados(String confirmados) {
        Confirmados = confirmados;
    }

    public String getMonitorados() {
        return Monitorados;
    }

    public void setMonitorados(String monitorados) {
        Monitorados = monitorados;
    }

    public String getRecuperados() {
        return Recuperados;
    }

    public void setRecuperados(String recuperados) {
        Recuperados = recuperados;
    }

    public String getObitos() {
        return Obitos;
    }

    public void setObitos(String obitos) {
        Obitos = obitos;
    }

    public String getDescartados() {
        return Descartados;
    }

    public void setDescartados(String descartados) {
        Descartados = descartados;
    }
}
