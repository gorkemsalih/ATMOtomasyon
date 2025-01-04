package com.atm.gui;

import com.atm.dao.HesapDAO;
import com.atm.model.Hesap;
import com.atm.util.ArayuzUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SifreGirisPaneli extends JPanel {
    private JPasswordField sifreAlani;
    private String hesapNo;
    private ATMFrame ebeveyn;
    
    public SifreGirisPaneli(ATMFrame ebeveyn) {
        this.ebeveyn = ebeveyn;
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        bilesenlerOlustur();
    }
    
    public void hesapNoAyarla(String hesapNo) {
        this.hesapNo = hesapNo;
        sifreAlani.setText("");
    }
    
    private void bilesenlerOlustur() {
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel sifreEtiketi = new JLabel("Şifrenizi Giriniz");
        sifreEtiketi.setFont(ArayuzUtil.NORMALYAZI);
        
        sifreAlani = new JPasswordField(4);
        sifreAlani.setFont(ArayuzUtil.NORMALYAZI);
        sifreAlani.setHorizontalAlignment(JTextField.CENTER);
        sifreAlani.setDocument(new SifreDocument());
        
        // Enter tuşu ile giriş yapma
        sifreAlani.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sifreDogrula();
                }
            }
        });
        
        JPanel tuslarPaneli = ArayuzUtil.tuslarOlustur(sifreAlani);
        
        JButton girisButonu = ArayuzUtil.stilliButonOlustur("Giriş");
        girisButonu.addActionListener(e -> sifreDogrula());
        
        JButton geriButonu = ArayuzUtil.stilliButonOlustur("Geri");
        geriButonu.addActionListener(e -> ebeveyn.hesapGirisiGoster());
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 20, 10);
        
        add(sifreEtiketi, gbc);
        
        gbc.gridy++;
        add(sifreAlani, gbc);
        
        gbc.gridy++;
        add(tuslarPaneli, gbc);
        
        gbc.gridy++;
        gbc.gridwidth = 1;
        add(girisButonu, gbc);
        
        gbc.gridx = 1;
        add(geriButonu, gbc);
    }
    
    private void sifreDogrula() {
        String sifre = new String(sifreAlani.getPassword()).trim();
        
        if (sifre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Lütfen şifre giriniz!",
                "Hata",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (sifre.length() != 4) {
            JOptionPane.showMessageDialog(this,
                "Şifre 4 haneli olmalıdır!",
                "Hata",
                JOptionPane.ERROR_MESSAGE);
            sifreAlani.setText("");
            return;
        }
        
        Hesap hesap = HesapDAO.sifreDogrula(hesapNo, sifre);
        
        if (hesap != null) {
            ebeveyn.anaMenuGoster(hesap);
        } else {
            JOptionPane.showMessageDialog(this,
                "Geçersiz şifre!",
                "Hata",
                JOptionPane.ERROR_MESSAGE);
            sifreAlani.setText("");
        }
    }
}

// Şifre alanı için özel document class
class SifreDocument extends javax.swing.text.PlainDocument {
    @Override
    public void insertString(int offs, String str, javax.swing.text.AttributeSet a)
            throws javax.swing.text.BadLocationException {
        if (str == null) return;
        
        // Sadece rakam girişine izin ver
        String yeniStr = str.replaceAll("[^0-9]", "");
        
        // Maksimum 4 karakter
        if ((getLength() + yeniStr.length()) <= 4) {
            super.insertString(offs, yeniStr, a);
        }
    }
}