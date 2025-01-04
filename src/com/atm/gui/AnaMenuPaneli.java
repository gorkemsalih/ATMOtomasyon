package com.atm.gui;

import com.atm.model.Hesap;
import com.atm.util.ArayuzUtil;
import com.atm.dao.KrediKartiDAO;
import com.atm.dao.BankaHesapDAO;
import com.atm.model.KrediKarti;
import com.atm.service.EmailService;
import javax.swing.*;
import java.awt.*;

public class AnaMenuPaneli extends JPanel {
    private Hesap hesap;
    private ATMFrame ebeveyn;
    private KrediKarti krediKarti;

    public AnaMenuPaneli(ATMFrame ebeveyn, Hesap hesap) {
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
            "Kredi Kartı İşlemleri",
            "Kredi Çekimi",
            "EFT/HAVALE",
            "Banka Hesabı",
            "Döviz/Altın İşlemleri",
            "Hesaplarım",
            "Mail Gönder",
            "Çıkış"
        };

        for (String secenek : secenekler) {
            JButton buton = ArayuzUtil.stilliButonOlustur(secenek);
            buton.addActionListener(e -> menuSecenekIsle(secenek));
            add(buton);
        }
    }

    private void menuSecenekIsle(String secenek) {
        switch (secenek) {
            case "Kredi Kartı İşlemleri":
                ebeveyn.krediKartiMenuGoster(hesap);
                break;
            case "Kredi Çekimi":
                krediCek();
                break;
            case "EFT/HAVALE":
                eftHavaleYap();
                break;
            case "Banka Hesabı":
                ebeveyn.bankaHesapMenuGoster(hesap);
                break;
            case "Döviz/Altın İşlemleri":
                ebeveyn.dovizAltinMenuGoster(hesap);
                break;
            case "Hesaplarım":
                hesapBilgileriniGoster();
                break;
            case "Mail Gönder":
                mailGonder();
                break;
            case "Çıkış":
                System.exit(0);
                break;
        }
    }

    private void mailGonder() {
        String email = JOptionPane.showInputDialog(this,
            "Mail adresinizi giriniz:",
            "Mail Gönderme",
            JOptionPane.QUESTION_MESSAGE);

        if (email == null || email.trim().isEmpty()) return;

        if (EmailService.sendHesapBilgileri(email, hesap)) {
            JOptionPane.showMessageDialog(this,
                "Hesap bilgileriniz mail adresinize gönderildi.",
                "Başarılı",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Mail gönderimi başarısız oldu!",
                "Hata",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void krediCek() {
        if (krediKarti == null) {
            JOptionPane.showMessageDialog(this,
                "Kredi çekebilmek için kredi kartınız olması gerekiyor!",
                "Uyarı",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String miktar = JOptionPane.showInputDialog(this,
            String.format("Kullanılabilir Limit: %.2f TL\nÇekmek istediğiniz kredi tutarını giriniz:",
                krediKarti.getLimit() - krediKarti.getBakiye()));

        if (miktar == null || miktar.trim().isEmpty()) return;

        try {
            double cekilecekMiktar = Double.parseDouble(miktar);
            double faizliTutar = krediKarti.faizliTutar(cekilecekMiktar);

            if (cekilecekMiktar <= 0) {
                JOptionPane.showMessageDialog(this,
                    "Geçersiz tutar!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (faizliTutar > (krediKarti.getLimit() - krediKarti.getBakiye())) {
                JOptionPane.showMessageDialog(this,
                    "Yetersiz limit!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (KrediKartiDAO.krediKullan(krediKarti.getId(), cekilecekMiktar, faizliTutar)) {
                hesap.setBankaBakiye(hesap.getBankaBakiye() + cekilecekMiktar);
                krediKarti = KrediKartiDAO.krediKartiBul(hesap.getId());

                JOptionPane.showMessageDialog(this,
                    String.format("%.2f TL kredi çekildi.\nFaizli Tutar: %.2f TL\nGüncel Borç: %.2f TL",
                        cekilecekMiktar, faizliTutar, krediKarti.getBakiye()),
                    "İşlem Başarılı",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Geçersiz tutar!",
                "Hata",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eftHavaleYap() {
        String aliciHesapNo = JOptionPane.showInputDialog(this,
            "Alıcı hesap numarasını giriniz:",
            "EFT/HAVALE",
            JOptionPane.QUESTION_MESSAGE);

        if (aliciHesapNo == null || aliciHesapNo.trim().isEmpty()) return;

        if (aliciHesapNo.equals(hesap.getHesapNo())) {
            JOptionPane.showMessageDialog(this,
                "Kendi hesabınıza transfer yapamazsınız!",
                "Hata",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        String miktar = JOptionPane.showInputDialog(this,
            String.format("Mevcut Bakiye: %.2f TL\nTransfer tutarını giriniz:", hesap.getBankaBakiye()));

        if (miktar == null || miktar.trim().isEmpty()) return;

        try {
            double transferMiktar = Double.parseDouble(miktar);

            if (transferMiktar <= 0) {
                JOptionPane.showMessageDialog(this,
                    "Geçersiz tutar!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (transferMiktar > hesap.getBankaBakiye()) {
                JOptionPane.showMessageDialog(this,
                    "Yetersiz bakiye!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (BankaHesapDAO.paraTransfer(hesap.getHesapNo(), aliciHesapNo, transferMiktar)) {
                hesap.setBankaBakiye(hesap.getBankaBakiye() - transferMiktar);
                JOptionPane.showMessageDialog(this,
                    String.format("%.2f TL transfer edildi.\nKalan bakiye: %.2f TL",
                        transferMiktar, hesap.getBankaBakiye()),
                    "İşlem Başarılı",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Transfer işlemi başarısız! Alıcı hesap numarasını kontrol ediniz.",
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

    private void hesapBilgileriniGoster() {
        String mesaj = String.format("""
            Hesap Bilgileriniz:
            
            Ad Soyad: %s
            Hesap No: %s
            
            Bakiyeler:
            Banka Hesabı: %.2f TL
            Dolar: %.2f USD
            Euro: %.2f EUR
            
            Altın Varlıkları:
            Gram Altın: %.2f gr
            Çeyrek Altın: %.2f adet
            Yarım Altın: %.2f adet
            Cumhuriyet Altını: %.2f adet
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

        JOptionPane.showMessageDialog(this,
            mesaj,
            "Hesap Bilgileri",
            JOptionPane.INFORMATION_MESSAGE);
    }
}