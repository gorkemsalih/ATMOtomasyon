package com.atm.model;

public class KrediKartiLimit {
    private static final double VARSAYILAN_LIMIT = 10000.0;
    private static final double VARSAYILAN_FAIZ = 1.5;
    
    private final double limit;
    private final double faizOrani;
    
    public KrediKartiLimit(double limit, double faizOrani) {
        this.limit = limit;
        this.faizOrani = faizOrani;
    }
    
    public static KrediKartiLimit varsayilan() {
        return new KrediKartiLimit(VARSAYILAN_LIMIT, VARSAYILAN_FAIZ);
    }
    
    public double getLimit() { return limit; }
    public double getFaizOrani() { return faizOrani; }
}