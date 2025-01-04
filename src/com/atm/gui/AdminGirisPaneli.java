package com.atm.gui;

import com.atm.dao.AdminDAO;
import com.atm.model.Admin;
import com.atm.util.ArayuzUtil;
import javax.swing.*;
import java.awt.*;

public class AdminGirisPaneli extends JPanel {
    private JTextField kullaniciAdiAlani;
    private JPasswordField sifreAlani;
    private ATMFrame ebeveyn;
    
    public AdminGirisPaneli(ATMFrame ebeveyn) {
        this.ebeveyn = ebeveyn;
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        bilesenlerOlustur();
    }
    
    private void bilesenlerOlustur() {
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel baslikEtiketi = new JLabel("Admin Girişi");
        baslikEtiketi.setFont(ArayuzUtil.BASLIKYAZI);
        baslikEtiketi.setForeground(ArayuzUtil.ANARENK);
        
        JLabel kullaniciAdiEtiketi = new JLabel("Kullanıcı Adı:");
        kullaniciAdiEtiketi.setFont(ArayuzUtil.NORMALYAZI);
        
        kullaniciAdiAlani = new JTextField(15);
        kullaniciAdiAlani.setFont(ArayuzUtil.NORMALYAZI);
        
        JLabel sifreEtiketi = new JLabel("Şifre:");
        sifreEtiketi.setFont(ArayuzUtil.NORMALYAZI);
        
        sifreAlani = new JPasswordField(15);
        sifreAlani.setFont(ArayuzUtil.NORMALYAZI);
        
        JButton girisButonu = ArayuzUtil.stilliButonOlustur("Giriş");
        girisButonu.addActionListener(e -> girisYap());
        
        JButton iptalButonu = ArayuzUtil.stilliButonOlustur("İptal");
        iptalButonu.addActionListener(e -> ebeveyn.hesapGirisiGoster());
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 20, 10);
        add(baslikEtiketi, gbc);
        
        gbc.gridy++;
        gbc.gridwidth = 1;
        add(kullaniciAdiEtiketi, gbc);
        
        gbc.gridx = 1;
        add(kullaniciAdiAlani, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        add(sifreEtiketi, gbc);
        
        gbc.gridx = 1;
        add(sifreAlani, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        add(girisButonu, gbc);
        
        gbc.gridx = 1;
        add(iptalButonu, gbc);
    }
    
    private void girisYap() {
        String kullaniciAdi = kullaniciAdiAlani.getText().trim();
        String sifre = new String(sifreAlani.getPassword());
        
        if (kullaniciAdi.isEmpty() || sifre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Kullanıcı adı ve şifre boş olamaz!",
                "Hata",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Admin admin = AdminDAO.girisYap(kullaniciAdi, sifre);
        
        if (admin != null) {
            ebeveyn.adminMenuGoster(admin);
        } else {
            JOptionPane.showMessageDialog(this,
                "Geçersiz kullanıcı adı veya şifre!",
                "Hata",
                JOptionPane.ERROR_MESSAGE);
            sifreAlani.setText("");
        }
    }
}