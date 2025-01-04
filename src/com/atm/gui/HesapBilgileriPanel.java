package com.atm.gui;

import com.atm.model.Hesap;
import javax.swing.*;
import java.awt.*;

public class HesapBilgileriPanel extends JPanel {
    private final Hesap hesap;
    private final JTextArea bilgiAlani;
    
    public HesapBilgileriPanel(Hesap hesap) {
        this.hesap = hesap;
        setLayout(new BorderLayout(10, 10));
        
        // Bilgi alanı oluştur
        bilgiAlani = new JTextArea(hesapBilgileriniGetir());
        bilgiAlani.setEditable(false);
        bilgiAlani.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(bilgiAlani), BorderLayout.CENTER);
        
        // Buton paneli
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton gonderButton = new JButton("Gönder");
        gonderButton.addActionListener(e -> emailGonder());
        buttonPanel.add(gonderButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private String hesapBilgileriniGetir() {
        return String.format("""
            Hesap Bilgileriniz:
            
            Banka Hesabı Bakiyesi: %.2f TL
            Dolar Bakiyesi: %.2f USD
            Euro Bakiyesi: %.2f EUR
            Gram Altın: %.2f gr
            Çeyrek Altın: %.2f adet
            Yarım Altın: %.2f adet
            Cumhuriyet Altını: %.2f adet
            """,
            hesap.getBankaBakiye(),
            hesap.getDolarBakiye(),
            hesap.getEuroBakiye(),
            hesap.getGramAltin(),
            hesap.getCeyrekAltin(),
            hesap.getYarimAltin(),
            hesap.getCumhuriyetAltin()
        );
    }
    
    private void emailGonder() {
        EmailDialog dialog = new EmailDialog(SwingUtilities.getWindowAncestor(this));
        dialog.showDialog(hesapBilgileriniGetir());
    }
}