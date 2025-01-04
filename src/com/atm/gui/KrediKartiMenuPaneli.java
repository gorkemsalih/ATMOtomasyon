package com.atm.gui;

import com.atm.model.Hesap;
import com.atm.model.KrediKarti;
import com.atm.dao.KrediKartiDAO;
import com.atm.util.ArayuzUtil;
import javax.swing.*;
import java.awt.*;

public class KrediKartiMenuPaneli extends JPanel {
    private Hesap hesap;
    private ATMFrame ebeveyn;
    private KrediKarti krediKarti;
    
    public KrediKartiMenuPaneli(ATMFrame ebeveyn, Hesap hesap) {
        this.ebeveyn = ebeveyn;
        this.hesap = hesap;
        this.krediKarti = KrediKartiDAO.krediKartiBul(hesap.getId());
        
        setLayout(new GridLayout(4, 2, 15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        bilesenlerOlustur();
    }
    
    private void bilesenlerOlustur() {
        String[] secenekler = {
        	"Kredi Kartı Başvurusu",
        	"Borç Sorgula",
            "Borç Öde",
            "Kart Bilgileri",
            "Limit Arttırma",
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
            case "Kredi Kartı Başvurusu":
                krediKartiBasvurusu();
                break;
            case "Borç Sorgula":
                borcSorgula();
                break;
            case "Borç Öde":
                krediKartiBorcuOde();
                break;
            case "Kart Bilgileri":
                kartBilgileriGoster();
                break; // Burada break eklenmeli
            case "Limit Arttırma":
                limitArttir();
                break;
            case "Ana Menü":
                ebeveyn.anaMenuGoster(hesap);
                break;
        }
    }
    
    private void borcSorgula() {
        if (krediKarti == null) {
            JOptionPane.showMessageDialog(this,
                "Kredi kartınız bulunmamaktadır!",
                "Uyarı",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(this,
            String.format("Güncel Borç: %.2f TL\nKullanılabilir Limit: %.2f TL",
                krediKarti.getBakiye(),
                krediKarti.getLimit() - krediKarti.getBakiye()),
            "Borç Bilgisi",
            JOptionPane.INFORMATION_MESSAGE);
    }
    private void krediKartiBasvurusu() {
        if (krediKarti != null) {
            JOptionPane.showMessageDialog(this,
                "Zaten bir kredi kartınız bulunmaktadır!",
                "Uyarı",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        krediKarti = KrediKartiDAO.krediKartiOlustur(hesap.getId());
        
        if (krediKarti != null) {
            JOptionPane.showMessageDialog(this,
                "Kredi kartı başvurunuz onaylandı!\n\n" +
                "Kart Numarası: " + krediKarti.getKartNo() + "\n" +
                "Son Kullanım: " + krediKarti.getSonKullanim() + "\n" +
                "CVV: " + krediKarti.getCvv() + "\n" +
                "Limit: " + String.format("%.2f TL", krediKarti.getLimit()),
                "Başarılı",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Kredi kartı başvurusu başarısız!",
                "Hata",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    private void kartBilgileriGoster() {
        if (krediKarti == null) {
            JOptionPane.showMessageDialog(this,
                "Kredi kartınız bulunmamaktadır!",
                "Uyarı",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String mesaj = String.format("""
            Kart Numarası: %s
            Son Kullanım: %s
            CVV: %s
            Limit: %.2f TL
            Kullanılabilir Limit: %.2f TL
            Güncel Borç: %.2f TL
            """,
            krediKarti.getKartNo(),
            krediKarti.getSonKullanim(),
            krediKarti.getCvv(),
            krediKarti.getLimit(),
            krediKarti.getLimit() - krediKarti.getBakiye(),
            krediKarti.getBakiye()
        );
        
        JOptionPane.showMessageDialog(this,
            mesaj,
            "Kart Bilgileri",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void krediKartiBorcuOde() {
        if (krediKarti == null) {
            JOptionPane.showMessageDialog(this,
                "Kredi kartınız bulunmamaktadır!",
                "Uyarı",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (krediKarti.getBakiye() <= 0) {
            JOptionPane.showMessageDialog(this,
                "Kredi kartı borcunuz bulunmamaktadır!",
                "Bilgi",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] odemeSecenekleri = {"Banka Hesabından Öde", "Nakit Öde"};
        String odemeTipi = (String) JOptionPane.showInputDialog(this,
            "Ödeme tipini seçiniz:",
            "Borç Ödeme",
            JOptionPane.QUESTION_MESSAGE,
            null,
            odemeSecenekleri,
            odemeSecenekleri[0]);
            
        if (odemeTipi == null) return;
        
        String miktar = JOptionPane.showInputDialog(this,
            String.format("Mevcut Borç: %.2f TL\nÖdemek istediğiniz tutarı giriniz:", krediKarti.getBakiye()),
            "Borç Ödeme",
            JOptionPane.QUESTION_MESSAGE);
            
        if (miktar == null || miktar.trim().isEmpty()) return;
        
        try {
            double odenecekMiktar = Double.parseDouble(miktar);
            
            if (odenecekMiktar <= 0) {
                JOptionPane.showMessageDialog(this,
                    "Geçersiz tutar!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (odenecekMiktar > krediKarti.getBakiye()) {
                JOptionPane.showMessageDialog(this,
                    "Ödeme tutarı mevcut borçtan fazla olamaz!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean odemeBasarili = false;
            
            if (odemeTipi.equals("Banka Hesabından Öde")) {
                if (odenecekMiktar > hesap.getBankaBakiye()) {
                    JOptionPane.showMessageDialog(this,
                        "Banka hesabınızda yeterli bakiye bulunmamaktadır!",
                        "Hata",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                odemeBasarili = KrediKartiDAO.borcOdeBankaHesabindan(
                    krediKarti.getId(),
                    hesap.getHesapNo(),
                    odenecekMiktar
                );
                
                if (odemeBasarili) {
                    hesap.setBankaBakiye(hesap.getBankaBakiye() - odenecekMiktar);
                }
            } else {
                odemeBasarili = KrediKartiDAO.borcOde(krediKarti.getId(), odenecekMiktar);
            }
            
            if (odemeBasarili) {
                krediKarti = KrediKartiDAO.krediKartiBul(hesap.getId());
                JOptionPane.showMessageDialog(this,
                    String.format("%.2f TL ödeme yapıldı.\nKalan borç: %.2f TL",
                        odenecekMiktar, krediKarti.getBakiye()),
                    "İşlem Başarılı",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Ödeme işlemi başarısız!",
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
    private void limitArttir() {
        if (krediKarti == null) {
            JOptionPane.showMessageDialog(this,
                "Kredi kartınız bulunmamaktadır!",
                "Uyarı",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String yeniLimit = JOptionPane.showInputDialog(this,
            String.format("Mevcut Limit: %.2f TL\nYeni limit tutarını giriniz:", krediKarti.getLimit()),
            "Limit Arttırma",
            JOptionPane.QUESTION_MESSAGE);
            
        if (yeniLimit == null || yeniLimit.trim().isEmpty()) return;
        
        try {
            double yeniLimitTutar = Double.parseDouble(yeniLimit);
            
            if (yeniLimitTutar <= krediKarti.getLimit()) {
                JOptionPane.showMessageDialog(this,
                    "Yeni limit mevcut limitten büyük olmalıdır!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (KrediKartiDAO.limitGuncelle(krediKarti.getId(), yeniLimitTutar)) {
                krediKarti = KrediKartiDAO.krediKartiBul(hesap.getId());
                JOptionPane.showMessageDialog(this,
                    String.format("Limitiniz %.2f TL'ye yükseltildi!", yeniLimitTutar),
                    "Limit Güncellendi",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Limit güncelleme işlemi başarısız!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Geçersiz limit tutarı!",
                "Hata",
                JOptionPane.ERROR_MESSAGE);
        }
    }}
