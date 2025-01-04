package com.atm.email;

import com.atm.model.Hesap;

public class EmailFormatter {
    public static String formatHesapBilgileri(Hesap hesap) {
        return String.format("""
            Sayın %s,
            
            Hesap Bilgileriniz:
            
            Hesap No: %s
            Banka Hesabı Bakiyesi: %.2f TL
            Dolar Bakiyesi: %.2f USD
            Euro Bakiyesi: %.2f EUR
            Gram Altın: %.2f gr
            Çeyrek Altın: %.2f adet
            Yarım Altın: %.2f adet
            Cumhuriyet Altını: %.2f adet
            
            İyi günler dileriz.
            Gelişim Bankası
            """,
            hesap.getAdSoyad(),
            hesap.getHesapNo(),
            hesap.getBankaBakiye(),
            hesap.getDolarBakiye(),
            hesap.getEuroBakiye(),
            hesap.getGramAltin(),
            hesap.getCeyrekAltin(),
            hesap.getYarimAltin(),
            hesap.getCumhuriyetAltin()
        );
    }
}