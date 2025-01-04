package com.atm.gui;

import com.atm.dao.KullaniciDAO;
import com.atm.model.Hesap;
import com.atm.util.ArayuzUtil;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class KullaniciIslemleriPaneli extends JPanel {
    private ATMFrame ebeveyn;
    
    public KullaniciIslemleriPaneli(ATMFrame ebeveyn) {
        this.ebeveyn = ebeveyn;
        
        setLayout(new GridLayout(4, 2, 15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        bilesenlerOlustur();
    }
    
    private void bilesenlerOlustur() {
        String[] secenekler = {
            "Kullanıcı Ekle",
            "Kullanıcı Listele/Sil",
            "Geri Dön",
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
            case "Kullanıcı Ekle":
                kullaniciEkle();
                break;
            case "Kullanıcı Listele/Sil":
                kullaniciListeleSil();
                break;
            case "Geri Dön":
                ebeveyn.adminMenuGoster(null);
                break;
        }
    }
    
    private void kullaniciEkle() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        
        JTextField adSoyadAlani = new JTextField();
        JPasswordField sifreAlani = new JPasswordField();
        JTextField bakiyeAlani = new JTextField("0");
        
        panel.add(new JLabel("Ad Soyad:"));
        panel.add(adSoyadAlani);
        panel.add(new JLabel("Şifre (4 haneli):"));
        panel.add(sifreAlani);
        panel.add(new JLabel("Başlangıç Bakiyesi:"));
        panel.add(bakiyeAlani);
        
        int sonuc = JOptionPane.showConfirmDialog(this, panel,
            "Yeni Kullanıcı Ekle", JOptionPane.OK_CANCEL_OPTION);
            
        if (sonuc == JOptionPane.OK_OPTION) {
            String adSoyad = adSoyadAlani.getText().trim();
            String sifre = new String(sifreAlani.getPassword());
            
            if (adSoyad.isEmpty() || sifre.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Ad soyad ve şifre boş olamaz!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (sifre.length() != 4 || !sifre.matches("\\d+")) {
                JOptionPane.showMessageDialog(this,
                    "Şifre 4 haneli sayısal değer olmalıdır!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                double bakiye = Double.parseDouble(bakiyeAlani.getText());
                
                // Kullanıcı ekleme işlemi, KullaniciDAO'yu kullanıyoruz
                boolean hesapEklendi = KullaniciDAO.hesapEkle(adSoyad, sifre, bakiye);
                if (!hesapEklendi) {
                    JOptionPane.showMessageDialog(this,
                        "Kullanıcı eklenirken bir hata oluştu.",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "Geçersiz bakiye değeri!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void kullaniciListeleSil() {
        List<Hesap> hesaplar = KullaniciDAO.tumHesaplariGetir();
        
        if (hesaplar.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Kayıtlı kullanıcı bulunmamaktadır!",
                "Bilgi",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] hesapDizisi = hesaplar.stream()
            .map(h -> String.format("%s - %s - Bakiye: %.2f TL", 
                h.getHesapNo(), h.getAdSoyad(), h.getBankaBakiye()))
            .toArray(String[]::new);
            
        String secim = (String) JOptionPane.showInputDialog(this,
            "Silmek istediğiniz hesabı seçiniz:",
            "Kullanıcı Listesi",
            JOptionPane.QUESTION_MESSAGE,
            null,
            hesapDizisi,
            hesapDizisi[0]);
            
        if (secim == null) return;
        
        String hesapNo = secim.split(" - ")[0];
        
        int onay = JOptionPane.showConfirmDialog(this,
            "Seçili hesabı silmek istediğinize emin misiniz?",
            "Onay",
            JOptionPane.YES_NO_OPTION);
            
        if (onay == JOptionPane.YES_OPTION) {
            boolean sonucHesapSilme = KullaniciDAO.hesapSil(hesapNo);
            if (sonucHesapSilme) {
                JOptionPane.showMessageDialog(this,
                    "Hesap başarıyla silindi!",
                    "Başarılı",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Hesap silinirken bir hata oluştu.",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
