package com.atm.util;

import java.util.Random;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class KrediKartiUtil {
    private static final Random random = new Random();
    private static final String KART_NO_FORMAT = "%04d";
    private static final String CVV_FORMAT = "%03d";
    private static final String TARIH_FORMAT = "MM/yy";
    private static final int GECERLILIK_YILI = 4;
    
    public static String kartNoUret() {
        StringBuilder kartNo = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            if (i > 0) kartNo.append("-");
            kartNo.append(String.format(KART_NO_FORMAT, random.nextInt(10000)));
        }
        return kartNo.toString();
    }
    
    public static String cvvUret() {
        return String.format(CVV_FORMAT, random.nextInt(1000));
    }
    
    public static String sonKullanimUret() {
        LocalDate now = LocalDate.now();
        LocalDate sonKullanim = now.plusYears(GECERLILIK_YILI);
        return sonKullanim.format(DateTimeFormatter.ofPattern(TARIH_FORMAT));
    }
    
    public static boolean kartNoGecerliMi(String kartNo) {
        return kartNo != null && kartNo.matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}");
    }
    
    public static boolean cvvGecerliMi(String cvv) {
        return cvv != null && cvv.matches("\\d{3}");
    }
}