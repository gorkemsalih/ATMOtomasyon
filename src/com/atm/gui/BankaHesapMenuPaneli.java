package com.atm.gui;

import com.atm.dao.BankaHesapDAO;
import com.atm.model.Hesap;
import com.atm.util.ArayuzUtil;
import javax.swing.*;
import java.awt.*;

public class BankaHesapMenuPaneli extends JPanel {
    private Hesap hesap;
    private ATMFrame ebeveyn;
    
    public BankaHesapMenuPaneli(ATMFrame ebeveyn, Hesap hesap) {
        this.ebeveyn = ebeveyn;
        this.hesap = hesap;
        
        setLayout(new GridLayout(4, 2, 15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        bilesenlerOlustur();
    }
    
    private void bilesenlerOlustur() {
        String[] secenekler = {
            "Para Yatır",
            "Para Çek",
            "Bakiye Görüntüle",
            "Ana Menü",
            "",
            ""
        };
        
        for (String secenek : secenekler) {
            if (!secenek.isEmpty()) {
                JButton buton = ArayuzUtil.stilliButonOlustur(secenek);
                buton.addActionListener(e -> menuSecenekIsle(secenek));
                add(buton);
            } else {
                add(new JPanel());
            }
        }
    }
    
    private void menuSecenekIsle(String secenek) {
        switch (secenek) {
            case "Para Yatır":
                paraYatir();
                break;
            case "Para Çek":
                paraCek();
                break;
            case "Bakiye Görüntüle":
                bakiyeGoruntule();
                break;
            case "Ana Menü":
                ebeveyn.anaMenuGoster(hesap);
                break;
        }
    }
    
    private void paraYatir() {
        String miktar = JOptionPane.showInputDialog(this,
            "Yatırmak istediğiniz tutarı giriniz:",
            "Para Yatırma",
            JOptionPane.QUESTION_MESSAGE);
            
        if (miktar == null || miktar.trim().isEmpty()) return;
        
        try {
            double yatirilacakMiktar = Double.parseDouble(miktar);
            
            if (yatirilacakMiktar <= 0) {
                JOptionPane.showMessageDialog(this,
                    "Geçersiz tutar!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (BankaHesapDAO.paraYatir(hesap.getHesapNo(), yatirilacakMiktar)) {
                hesap.setBankaBakiye(hesap.getBankaBakiye() + yatirilacakMiktar);
                JOptionPane.showMessageDialog(this,
                    String.format("%.2f TL yatırıldı.\nGüncel bakiye: %.2f TL",
                        yatirilacakMiktar, hesap.getBankaBakiye()),
                    "İşlem Başarılı",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Para yatırma işlemi başarısız!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Geçersiz tutar!",
                "Hata",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void paraCek() {
        String miktar = JOptionPane.showInputDialog(this,
            String.format("Mevcut Bakiye: %.2f TL\nÇekmek istediğiniz tutarı giriniz:", hesap.getBankaBakiye()),
            "Para Çekme",
            JOptionPane.QUESTION_MESSAGE);
            
        if (miktar == null || miktar.trim().isEmpty()) return;
        
        try {
            double cekilecekMiktar = Double.parseDouble(miktar);
            
            if (cekilecekMiktar <= 0) {
                JOptionPane.showMessageDialog(this,
                    "Geçersiz tutar!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (cekilecekMiktar > hesap.getBankaBakiye()) {
                JOptionPane.showMessageDialog(this,
                    "Yetersiz bakiye!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (BankaHesapDAO.paraCek(hesap.getHesapNo(), cekilecekMiktar)) {
                hesap.setBankaBakiye(hesap.getBankaBakiye() - cekilecekMiktar);
                JOptionPane.showMessageDialog(this,
                    String.format("%.2f TL çekildi.\nKalan bakiye: %.2f TL",
                        cekilecekMiktar, hesap.getBankaBakiye()),
                    "İşlem Başarılı",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Para çekme işlemi başarısız!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Geçersiz tutar!",
                "Hata",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void bakiyeGoruntule() {
        JOptionPane.showMessageDialog(this,
            String.format("Banka Hesabı Bakiyesi: %.2f TL", hesap.getBankaBakiye()),
            "Bakiye Bilgisi",
            JOptionPane.INFORMATION_MESSAGE);
    }
}