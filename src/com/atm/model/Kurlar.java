package com.atm.model;

public class Kurlar {
    private double dolarKuru;
    private double euroKuru;
    private double gramAltin;
    private double ceyrekAltin;
    private double yarimAltin;
    private double cumhuriyetAltin;
    private double faizOrani;
    
    public Kurlar(double dolarKuru, double euroKuru, double gramAltin,
                 double ceyrekAltin, double yarimAltin, double cumhuriyetAltin,
                 double faizOrani) {
        this.dolarKuru = dolarKuru;
        this.euroKuru = euroKuru;
        this.gramAltin = gramAltin;
        this.ceyrekAltin = ceyrekAltin;
        this.yarimAltin = yarimAltin;
        this.cumhuriyetAltin = cumhuriyetAltin;
        this.faizOrani = faizOrani;
    }
    
    public double getDolarKuru() { return dolarKuru; }
    public double getEuroKuru() { return euroKuru; }
    public double getGramAltin() { return gramAltin; }
    public double getCeyrekAltin() { return ceyrekAltin; }
    public double getYarimAltin() { return yarimAltin; }
    public double getCumhuriyetAltin() { return cumhuriyetAltin; }
    public double getFaizOrani() { return faizOrani; }
}