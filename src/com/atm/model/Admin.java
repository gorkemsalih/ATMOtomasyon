package com.atm.model;

public class Admin {
    private int id;
    private String kullaniciAdi;
    private String sifre;
    private String adSoyad;
    
    public Admin(int id, String kullaniciAdi, String sifre, String adSoyad) {
        this.id = id;
        this.kullaniciAdi = kullaniciAdi;
        this.sifre = sifre;
        this.adSoyad = adSoyad;
    }
    
    public int getId() { return id; }
    public String getKullaniciAdi() { return kullaniciAdi; }
    public String getSifre() { return sifre; }
    public String getAdSoyad() { return adSoyad; }
}