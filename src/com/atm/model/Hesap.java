package com.atm.model;

public class Hesap {
    private int id;
    private String hesapNo;
    private String adSoyad;
    private String sifre;
    private double bankaBakiye;
    private double dolarBakiye;
    private double euroBakiye;
    private double gramAltin;
    private double ceyrekAltin;
    private double yarimAltin;
    private double cumhuriyetAltin;
    
    public Hesap(int id, String hesapNo, String adSoyad, String sifre, double bankaBakiye, 
                 double dolarBakiye, double euroBakiye, double gramAltin, 
                 double ceyrekAltin, double yarimAltin, double cumhuriyetAltin) {
        this.id = id;
        this.hesapNo = hesapNo;
        this.adSoyad = adSoyad;
        this.sifre = sifre;
        this.bankaBakiye = bankaBakiye;
        this.dolarBakiye = dolarBakiye;
        this.euroBakiye = euroBakiye;
        this.gramAltin = gramAltin;
        this.ceyrekAltin = ceyrekAltin;
        this.yarimAltin = yarimAltin;
        this.cumhuriyetAltin = cumhuriyetAltin;
    }
    
    public int getId() { return id; }
    public String getHesapNo() { return hesapNo; }
    public String getAdSoyad() { return adSoyad; }
    public String getSifre() { return sifre; }
    public double getBankaBakiye() { return bankaBakiye; }
    public double getDolarBakiye() { return dolarBakiye; }
    public double getEuroBakiye() { return euroBakiye; }
    public double getGramAltin() { return gramAltin; }
    public double getCeyrekAltin() { return ceyrekAltin; }
    public double getYarimAltin() { return yarimAltin; }
    public double getCumhuriyetAltin() { return cumhuriyetAltin; }
    
    public void setBankaBakiye(double bakiye) { this.bankaBakiye = bakiye; }
    public void setDolarBakiye(double bakiye) { this.dolarBakiye = bakiye; }
    public void setEuroBakiye(double bakiye) { this.euroBakiye = bakiye; }
    public void setGramAltin(double miktar) { this.gramAltin = miktar; }
    public void setCeyrekAltin(double miktar) { this.ceyrekAltin = miktar; }
    public void setYarimAltin(double miktar) { this.yarimAltin = miktar; }
    public void setCumhuriyetAltin(double miktar) { this.cumhuriyetAltin = miktar; }
    public void setSifre(String sifre) { this.sifre = sifre; }
}