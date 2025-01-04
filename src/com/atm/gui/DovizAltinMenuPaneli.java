package com.atm.gui;

import com.atm.model.Hesap;
import com.atm.util.ArayuzUtil;
import com.atm.dao.BankaHesapDAO;
import javax.swing.*;
import java.awt.*;

public class DovizAltinMenuPaneli extends JPanel {
    private static final double DOLAR_KURU = 35.32;
    private static final double EURO_KURU = 36.46;
    private static final double GRAM_ALTIN = 3000.0;
    private static final double CEYREK_ALTIN = 4900.0;
    private static final double YARIM_ALTIN = 9800.0;
    private static final double CUMHURIYET_ALTIN = 19500.0;
    
    private Hesap hesap;
    private ATMFrame ebeveyn;
    
    public DovizAltinMenuPaneli(ATMFrame ebeveyn, Hesap hesap) {
        this.ebeveyn = ebeveyn;
        this.hesap = hesap;
        
        setLayout(new GridLayout(4, 2, 15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        bilesenlerOlustur();
    }
    
    private void bilesenlerOlustur() {
        String[] secenekler = {
            "Döviz Al",
            "Döviz Sat",
            "Altın Al",
            "Altın Sat",
            "Kurları Görüntüle",
            "Ana Menü"
        };
        
        for (String secenek : secenekler) {
            JButton buton = ArayuzUtil.stilliButonOlustur(secenek);
            buton.addActionListener(e -> menuSecenekIsle(secenek));
            add(buton);
        }
    }
    
    private void menuSecenekIsle(String secenek) {
        switch (secenek) {
            case "Döviz Al":
                dovizAl();
                break;
            case "Döviz Sat":
                dovizSat();
                break;
            case "Altın Al":
                altinAl();
                break;
            case "Altın Sat":
                altinSat();
                break;
            case "Kurları Görüntüle":
                kurlariGoster();
                break;
            case "Ana Menü":
                ebeveyn.anaMenuGoster(hesap);
                break;
        }
    }
    
    private void dovizAl() {
        String[] secenekler = {"Dolar", "Euro"};
        String dovizTipi = (String) JOptionPane.showInputDialog(this,
            "Döviz tipini seçiniz:",
            "Döviz Al",
            JOptionPane.QUESTION_MESSAGE,
            null,
            secenekler,
            secenekler[0]);
            
        if (dovizTipi == null) return;
        
        String miktar = JOptionPane.showInputDialog(this,
            String.format("TL Bakiyeniz: %.2f TL\n%s almak istediğiniz miktarı giriniz:", 
                hesap.getBankaBakiye(), dovizTipi));
                
        if (miktar == null || miktar.trim().isEmpty()) return;
        
        try {
            double dovizMiktar = Double.parseDouble(miktar);
            double kur = dovizTipi.equals("Dolar") ? DOLAR_KURU : EURO_KURU;
            double tlTutar = dovizMiktar * kur;
            
            if (tlTutar > hesap.getBankaBakiye()) {
                JOptionPane.showMessageDialog(this,
                    "Yetersiz TL bakiye!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (BankaHesapDAO.dovizIslem(hesap.getHesapNo(), dovizTipi, dovizMiktar, true)) {
                if (dovizTipi.equals("Dolar")) {
                    hesap.setDolarBakiye(hesap.getDolarBakiye() + dovizMiktar);
                } else {
                    hesap.setEuroBakiye(hesap.getEuroBakiye() + dovizMiktar);
                }
                hesap.setBankaBakiye(hesap.getBankaBakiye() - tlTutar);
                
                JOptionPane.showMessageDialog(this,
                    String.format("%.2f %s alındı.\nÖdenen: %.2f TL\nKalan TL Bakiye: %.2f TL\n%s Bakiye: %.2f",
                        dovizMiktar, dovizTipi, tlTutar, hesap.getBankaBakiye(),
                        dovizTipi, dovizTipi.equals("Dolar") ? hesap.getDolarBakiye() : hesap.getEuroBakiye()),
                    "İşlem Başarılı",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Geçersiz miktar!",
                "Hata",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void dovizSat() {
        String[] secenekler = {"Dolar", "Euro"};
        String dovizTipi = (String) JOptionPane.showInputDialog(this,
            "Döviz tipini seçiniz:",
            "Döviz Sat",
            JOptionPane.QUESTION_MESSAGE,
            null,
            secenekler,
            secenekler[0]);
            
        if (dovizTipi == null) return;
        
        double mevcutDoviz = dovizTipi.equals("Dolar") ? hesap.getDolarBakiye() : hesap.getEuroBakiye();
        
        String miktar = JOptionPane.showInputDialog(this,
            String.format("Mevcut %s Bakiye: %.2f\nSatmak istediğiniz %s miktarını giriniz:", 
                dovizTipi, mevcutDoviz, dovizTipi));
                
        if (miktar == null || miktar.trim().isEmpty()) return;
        
        try {
            double dovizMiktar = Double.parseDouble(miktar);
            
            if (dovizMiktar > mevcutDoviz) {
                JOptionPane.showMessageDialog(this,
                    "Yetersiz döviz bakiye!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double kur = dovizTipi.equals("Dolar") ? DOLAR_KURU : EURO_KURU;
            double tlTutar = dovizMiktar * kur;
            
            if (BankaHesapDAO.dovizIslem(hesap.getHesapNo(), dovizTipi, dovizMiktar, false)) {
                if (dovizTipi.equals("Dolar")) {
                    hesap.setDolarBakiye(hesap.getDolarBakiye() - dovizMiktar);
                } else {
                    hesap.setEuroBakiye(hesap.getEuroBakiye() - dovizMiktar);
                }
                hesap.setBankaBakiye(hesap.getBankaBakiye() + tlTutar);
                
                JOptionPane.showMessageDialog(this,
                    String.format("%.2f %s satıldı.\nAlınan: %.2f TL\nGüncel TL Bakiye: %.2f TL\n%s Bakiye: %.2f",
                        dovizMiktar, dovizTipi, tlTutar, hesap.getBankaBakiye(),
                        dovizTipi, dovizTipi.equals("Dolar") ? hesap.getDolarBakiye() : hesap.getEuroBakiye()),
                    "İşlem Başarılı",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Geçersiz miktar!",
                "Hata",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void altinAl() {
        String[] secenekler = {"Gram Altın", "Çeyrek Altın", "Yarım Altın", "Cumhuriyet Altını"};
        String altinTipi = (String) JOptionPane.showInputDialog(this,
            "Altın tipini seçiniz:",
            "Altın Al",
            JOptionPane.QUESTION_MESSAGE,
            null,
            secenekler,
            secenekler[0]);
            
        if (altinTipi == null) return;
        
        String miktar = JOptionPane.showInputDialog(this,
            String.format("TL Bakiyeniz: %.2f TL\nKaç adet %s almak istiyorsunuz?", 
                hesap.getBankaBakiye(), altinTipi));
                
        if (miktar == null || miktar.trim().isEmpty()) return;
        
        try {
            double adet = Double.parseDouble(miktar);
            double birimFiyat = 0;
            
            switch (altinTipi) {
                case "Gram Altın": birimFiyat = GRAM_ALTIN; break;
                case "Çeyrek Altın": birimFiyat = CEYREK_ALTIN; break;
                case "Yarım Altın": birimFiyat = YARIM_ALTIN; break;
                case "Cumhuriyet Altını": birimFiyat = CUMHURIYET_ALTIN; break;
            }
            
            double tlTutar = adet * birimFiyat;
            
            if (tlTutar > hesap.getBankaBakiye()) {
                JOptionPane.showMessageDialog(this,
                    "Yetersiz TL bakiye!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (BankaHesapDAO.altinIslem(hesap.getHesapNo(), altinTipi, adet, tlTutar, true)) {
                switch (altinTipi) {
                    case "Gram Altın": 
                        hesap.setGramAltin(hesap.getGramAltin() + adet);
                        break;
                    case "Çeyrek Altın":
                        hesap.setCeyrekAltin(hesap.getCeyrekAltin() + adet);
                        break;
                    case "Yarım Altın":
                        hesap.setYarimAltin(hesap.getYarimAltin() + adet);
                        break;
                    case "Cumhuriyet Altını":
                        hesap.setCumhuriyetAltin(hesap.getCumhuriyetAltin() + adet);
                        break;
                }
                hesap.setBankaBakiye(hesap.getBankaBakiye() - tlTutar);
                
                double mevcutAltin = 0;
                switch (altinTipi) {
                    case "Gram Altın": mevcutAltin = hesap.getGramAltin(); break;
                    case "Çeyrek Altın": mevcutAltin = hesap.getCeyrekAltin(); break;
                    case "Yarım Altın": mevcutAltin = hesap.getYarimAltin(); break;
                    case "Cumhuriyet Altını": mevcutAltin = hesap.getCumhuriyetAltin(); break;
                }
                
                JOptionPane.showMessageDialog(this,
                    String.format("%.2f adet %s alındı.\nÖdenen: %.2f TL\nKalan TL Bakiye: %.2f TL\n%s Bakiye: %.2f",
                        adet, altinTipi, tlTutar, hesap.getBankaBakiye(), altinTipi, mevcutAltin),
                    "İşlem Başarılı",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Geçersiz miktar!",
                "Hata",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void altinSat() {
        String[] secenekler = {"Gram Altın", "Çeyrek Altın", "Yarım Altın", "Cumhuriyet Altını"};
        String altinTipi = (String) JOptionPane.showInputDialog(this,
            "Altın tipini seçiniz:",
            "Altın Sat",
            JOptionPane.QUESTION_MESSAGE,
            null,
            secenekler,
            secenekler[0]);
            
        if (altinTipi == null) return;
        
        double mevcutAltin = 0;
        switch (altinTipi) {
            case "Gram Altın": mevcutAltin = hesap.getGramAltin(); break;
            case "Çeyrek Altın": mevcutAltin = hesap.getCeyrekAltin(); break;
            case "Yarım Altın": mevcutAltin = hesap.getYarimAltin(); break;
            case "Cumhuriyet Altını": mevcutAltin = hesap.getCumhuriyetAltin(); break;
        }
        
        String miktar = JOptionPane.showInputDialog(this,
            String.format("Mevcut %s: %.2f\nKaç adet %s satmak istiyorsunuz?", 
                altinTipi, mevcutAltin, altinTipi));
                
        if (miktar == null || miktar.trim().isEmpty()) return;
        
        try {
            double adet = Double.parseDouble(miktar);
            
            if (adet > mevcutAltin) {
                JOptionPane.showMessageDialog(this,
                    "Yetersiz altın bakiye!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double birimFiyat = 0;
            switch (altinTipi) {
                case "Gram Altın": birimFiyat = GRAM_ALTIN; break;
                case "Çeyrek Altın": birimFiyat = CEYREK_ALTIN; break;
                case "Yarım Altın": birimFiyat = YARIM_ALTIN; break;
                case "Cumhuriyet Altını": birimFiyat = CUMHURIYET_ALTIN; break;
            }
            
            double tlTutar = adet * birimFiyat;
            
            if (BankaHesapDAO.altinIslem(hesap.getHesapNo(), altinTipi, adet, tlTutar, false)) {
                switch (altinTipi) {
                    case "Gram Altın":
                        hesap.setGramAltin(hesap.getGramAltin() - adet);
                        break;
                    case "Çeyrek Altın":
                        hesap.setCeyrekAltin(hesap.getCeyrekAltin() - adet);
                        break;
                    case "Yarım Altın":
                        hesap.setYarimAltin(hesap.getYarimAltin() - adet);
                        break;
                    case "Cumhuriyet Altını":
                        hesap.setCumhuriyetAltin(hesap.getCumhuriyetAltin() - adet);
                        break;
                }
                hesap.setBankaBakiye(hesap.getBankaBakiye() + tlTutar);
                
                mevcutAltin = 0;
                switch (altinTipi) {
                    case "Gram Altın": mevcutAltin = hesap.getGramAltin(); break;
                    case "Çeyrek Altın": mevcutAltin = hesap.getCeyrekAltin(); break;
                    case "Yarım Altın": mevcutAltin = hesap.getYarimAltin(); break;
                    case "Cumhuri yet Altını": mevcutAltin = hesap.getCumhuriyetAltin(); break;
                }
                
                JOptionPane.showMessageDialog(this,
                    String.format("%.2f adet %s satıldı.\nAlınan: %.2f TL\nGüncel TL Bakiye: %.2f TL\n%s Bakiye: %.2f",
                        adet, altinTipi, tlTutar, hesap.getBankaBakiye(), altinTipi, mevcutAltin),
                    "İşlem Başarılı",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Geçersiz miktar!",
                "Hata",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void kurlariGoster() {
        String mesaj = String.format("""
            Güncel Kurlar:
            
            1 USD = %.2f TL
            1 EUR = %.2f TL
            
            Altın Fiyatları:
            1 Gram Altın = %.2f TL
            1 Çeyrek Altın = %.2f TL
            1 Yarım Altın = %.2f TL
            1 Cumhuriyet Altını = %.2f TL
            
            Bakiyeleriniz:
            TL Bakiye: %.2f
            Dolar Bakiye: %.2f
            Euro Bakiye: %.2f
            Gram Altın: %.2f
            Çeyrek Altın: %.2f
            Yarım Altın: %.2f
            Cumhuriyet Altını: %.2f
            """,
            DOLAR_KURU,
            EURO_KURU,
            GRAM_ALTIN,
            CEYREK_ALTIN,
            YARIM_ALTIN,
            CUMHURIYET_ALTIN,
            hesap.getBankaBakiye(),
            hesap.getDolarBakiye(),
            hesap.getEuroBakiye(),
            hesap.getGramAltin(),
            hesap.getCeyrekAltin(),
            hesap.getYarimAltin(),
            hesap.getCumhuriyetAltin()
        );
        
        JOptionPane.showMessageDialog(this,
            mesaj,
            "Güncel Kurlar ve Bakiyeler",
            JOptionPane.INFORMATION_MESSAGE);
    }
}