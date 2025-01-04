package com.atm.gui;

import com.atm.dao.AdminDAO;
import com.atm.dao.KurlarDAO;
import com.atm.model.Admin;
import com.atm.model.Kurlar;
import com.atm.util.ArayuzUtil;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminMenuPaneli extends JPanel {
    private Admin admin;
    private ATMFrame ebeveyn;
    
    public AdminMenuPaneli(ATMFrame ebeveyn, Admin admin) {
        this.ebeveyn = ebeveyn;
        this.admin = admin;
        
        setLayout(new GridLayout(4, 2, 15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        bilesenlerOlustur();
    }
   
    private void bilesenlerOlustur() {
        String[] secenekler = {
            "Kurları Düzenle",
            "Kullanıcı İşlemleri",
            "Admin İşlemleri",
            "Şifre Değiştir",
            "Çıkış",
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
            case "Kurları Düzenle":
                kurDuzenle();
                break;
            case "Kullanıcı İşlemleri":
                kullaniciIslemleri();  // Kullanıcı işlemleri paneline yönlendir
                break;
            case "Admin İşlemleri":
                adminIslemleri();
                break;
            case "Şifre Değiştir":
                sifreDegistir();
                break;
            case "Çıkış":
                ebeveyn.hesapGirisiGoster();
                break;
        }
    }
    
    private void kurDuzenle() {
        Kurlar mevcutKurlar = KurlarDAO.kurlariGetir();
        if (mevcutKurlar == null) return;
        
        JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));
        
        JTextField dolarAlani = new JTextField(String.valueOf(mevcutKurlar.getDolarKuru()));
        JTextField euroAlani = new JTextField(String.valueOf(mevcutKurlar.getEuroKuru()));
        JTextField gramAltinAlani = new JTextField(String.valueOf(mevcutKurlar.getGramAltin()));
        JTextField ceyrekAltinAlani = new JTextField(String.valueOf(mevcutKurlar.getCeyrekAltin()));
        JTextField yarimAltinAlani = new JTextField(String.valueOf(mevcutKurlar.getYarimAltin()));
        JTextField cumhuriyetAltinAlani = new JTextField(String.valueOf(mevcutKurlar.getCumhuriyetAltin()));
        JTextField faizOraniAlani = new JTextField(String.valueOf(mevcutKurlar.getFaizOrani()));
        
        panel.add(new JLabel("Dolar Kuru:"));
        panel.add(dolarAlani);
        panel.add(new JLabel("Euro Kuru:"));
        panel.add(euroAlani);
        panel.add(new JLabel("Gram Altın:"));
        panel.add(gramAltinAlani);
        panel.add(new JLabel("Çeyrek Altın:"));
        panel.add(ceyrekAltinAlani);
        panel.add(new JLabel("Yarım Altın:"));
        panel.add(yarimAltinAlani);
        panel.add(new JLabel("Cumhuriyet Altını:"));
        panel.add(cumhuriyetAltinAlani);
        panel.add(new JLabel("Faiz Oranı (%):"));
        panel.add(faizOraniAlani);
        
        int sonuc = JOptionPane.showConfirmDialog(this, panel,
            "Kurları Düzenle", JOptionPane.OK_CANCEL_OPTION);
            
        if (sonuc == JOptionPane.OK_OPTION) {
            try {
                double dolar = Double.parseDouble(dolarAlani.getText());
                double euro = Double.parseDouble(euroAlani.getText());
                double gramAltin = Double.parseDouble(gramAltinAlani.getText());
                double ceyrekAltin = Double.parseDouble(ceyrekAltinAlani.getText());
                double yarimAltin = Double.parseDouble(yarimAltinAlani.getText());
                double cumhuriyetAltin = Double.parseDouble(cumhuriyetAltinAlani.getText());
                double faizOrani = Double.parseDouble(faizOraniAlani.getText());
                
                if (KurlarDAO.kurGuncelle(dolar, euro, gramAltin, ceyrekAltin,
                                        yarimAltin, cumhuriyetAltin, faizOrani)) {
                    JOptionPane.showMessageDialog(this,
                        "Kurlar başarıyla güncellendi!",
                        "Başarılı",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    "Geçersiz değer girişi!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void kullaniciIslemleri() {
        // Kullanıcı işlemleri paneli gösterilecek
        ebeveyn.setContentPane(new KullaniciIslemleriPaneli(ebeveyn));
        ebeveyn.validate();
    }
    
    private void adminIslemleri() {
        String[] secenekler = {"Admin Ekle", "Admin Listele/Sil"};
        
        String secim = (String) JOptionPane.showInputDialog(this,
            "İşlem seçiniz:",
            "Admin İşlemleri",
            JOptionPane.QUESTION_MESSAGE,
            null,
            secenekler,
            secenekler[0]);
            
        if (secim == null) return;
        
        if (secim.equals("Admin Ekle")) {
            adminEkle();
        } else {
            adminListeleSil();
        }
    }
    
    private void adminEkle() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        
        JTextField kullaniciAdiAlani = new JTextField();
        JPasswordField sifreAlani = new JPasswordField();
        JTextField adSoyadAlani = new JTextField();
        
        panel.add(new JLabel("Kullanıcı Adı:"));
        panel.add(kullaniciAdiAlani);
        panel.add(new JLabel("Şifre:"));
        panel.add(sifreAlani);
        panel.add(new JLabel("Ad Soyad:"));
        panel.add(adSoyadAlani);
        
        int sonuc = JOptionPane.showConfirmDialog(this, panel,
            "Yeni Admin Ekle", JOptionPane.OK_CANCEL_OPTION);
            
        if (sonuc == JOptionPane.OK_OPTION) {
            String kullaniciAdi = kullaniciAdiAlani.getText().trim();
            String sifre = new String(sifreAlani.getPassword());
            String adSoyad = adSoyadAlani.getText().trim();
            
            if (kullaniciAdi.isEmpty() || sifre.isEmpty() || adSoyad.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Tüm alanları doldurunuz!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (AdminDAO.adminEkle(kullaniciAdi, sifre, adSoyad)) {
                JOptionPane.showMessageDialog(this,
                    "Admin başarıyla eklendi!",
                    "Başarılı",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void adminListeleSil() {
        List<Admin> adminler = AdminDAO.tumAdminleriGetir();
        
        if (adminler.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Kayıtlı admin bulunmamaktadır!",
                "Bilgi",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] adminDizisi = adminler.stream()
            .map(a -> a.getId() + " - " + a.getKullaniciAdi() + " (" + a.getAdSoyad() + ")")
            .toArray(String[]::new);
            
        String secim = (String) JOptionPane.showInputDialog(this,
            "Silmek istediğiniz admini seçiniz:",
            "Admin Listesi",
            JOptionPane.QUESTION_MESSAGE,
            null,
            adminDizisi,
            adminDizisi[0]);
            
        if (secim == null) return;
        
        int adminId = Integer.parseInt(secim.split(" - ")[0]);
        
        if (adminId == admin.getId()) {
            JOptionPane.showMessageDialog(this,
                "Kendinizi silemezsiniz!",
                "Hata",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int onay = JOptionPane.showConfirmDialog(this,
            "Seçili admini silmek istediğinize emin misiniz?",
            "Onay",
            JOptionPane.YES_NO_OPTION);
            
        if (onay == JOptionPane.YES_OPTION) {
            if (AdminDAO.adminSil(adminId)) {
                JOptionPane.showMessageDialog(this,
                    "Admin başarıyla silindi!",
                    "Başarılı",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void sifreDegistir() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        
        JPasswordField yeniSifreAlani = new JPasswordField();
        JPasswordField yeniSifreTekrarAlani = new JPasswordField();
        
        panel.add(new JLabel("Yeni Şifre:"));
        panel.add(yeniSifreAlani);
        panel.add(new JLabel("Yeni Şifre (Tekrar):"));
        panel.add(yeniSifreTekrarAlani);
        
        int sonuc = JOptionPane.showConfirmDialog(this, panel,
            "Şifre Değiştir", JOptionPane.OK_CANCEL_OPTION);
            
        if (sonuc == JOptionPane.OK_OPTION) {
            String yeniSifre = new String(yeniSifreAlani.getPassword());
            String yeniSifreTekrar = new String(yeniSifreTekrarAlani.getPassword());
            
            if (yeniSifre.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Şifre boş olamaz!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!yeniSifre.equals(yeniSifreTekrar)) {
                JOptionPane.showMessageDialog(this,
                    "Şifreler eşleşmiyor!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (AdminDAO.sifreDegistir(admin.getId(), yeniSifre)) {
                JOptionPane.showMessageDialog(this,
                    "Şifreniz başarıyla değiştirildi!",
                    "Başarılı",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
