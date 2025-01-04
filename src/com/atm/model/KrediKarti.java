package com.atm.model;

public class KrediKarti {
    private int id;
    private int hesapId;
    private String kartNo;
    private String sonKullanim;
    private String cvv;
    private double bakiye;
    private double limit;
    private double faizOrani;
    
    public KrediKarti(int id, int hesapId, String kartNo, String sonKullanim, String cvv, double bakiye, double limit, double faizOrani) {
        this.id = id;
        this.hesapId = hesapId;
        this.kartNo = kartNo;
        this.sonKullanim = sonKullanim;
        this.cvv = cvv;
        this.bakiye = bakiye;
        this.limit = limit;
        this.faizOrani = faizOrani;
    }
    
    public int getId() { return id; }
    public int getHesapId() { return hesapId; }
    public String getKartNo() { return kartNo; }
    public String getSonKullanim() { return sonKullanim; }
    public String getCvv() { return cvv; }
    public double getBakiye() { return bakiye; }
    public double getLimit() { return limit; }
    public double getFaizOrani() { return faizOrani; }
    
    public double faizliTutar(double miktar) {
        return miktar * (1 + (faizOrani / 100));
    }
}