package com.atm.gui;

import com.atm.dao.HesapDAO;
import com.atm.util.ArayuzUtil;
import javax.swing.*;
import java.awt.*;

public class HesapGirisPaneli extends JPanel {
    private JTextField hesapNoAlani;
    private ATMFrame ebeveyn;
    
    public HesapGirisPaneli(ATMFrame ebeveyn) {
        this.ebeveyn = ebeveyn;
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        bilesenlerOlustur();
    }
    
    private void bilesenlerOlustur() {
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel baslikEtiketi = new JLabel("Gelişim Bankası");
        baslikEtiketi.setFont(ArayuzUtil.BASLIKYAZI);
        baslikEtiketi.setForeground(ArayuzUtil.ANARENK);
        
        JLabel hesapEtiketi = new JLabel("Hesap Numaranızı Giriniz");
        hesapEtiketi.setFont(ArayuzUtil.NORMALYAZI);
        
        hesapNoAlani = new JTextField(12);
        hesapNoAlani.setFont(ArayuzUtil.NORMALYAZI);
        
        JPanel tuslarPaneli = ArayuzUtil.tuslarOlustur(hesapNoAlani);
        
        JButton devamButonu = ArayuzUtil.stilliButonOlustur("Devam");
        devamButonu.addActionListener(e -> hesapDogrula());
        
        JButton adminGirisButonu = ArayuzUtil.stilliButonOlustur("Admin Girişi");
        adminGirisButonu.addActionListener(e -> ebeveyn.adminGirisiGoster());
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 20, 10);
        
        add(baslikEtiketi, gbc);
        gbc.gridy++;
        add(hesapEtiketi, gbc);
        gbc.gridy++;
        add(hesapNoAlani, gbc);
        gbc.gridy++;
        add(tuslarPaneli, gbc);
        gbc.gridy++;
        add(devamButonu, gbc);
        gbc.gridy++;
        add(adminGirisButonu, gbc);
    }
    
    private void hesapDogrula() {
        String hesapNo = hesapNoAlani.getText().trim();
        
        if (HesapDAO.hesapNoDogrula(hesapNo)) {
            ebeveyn.sifreGirisiGoster(hesapNo);
        } else {
            JOptionPane.showMessageDialog(this,
                "Geçersiz hesap numarası!",
                "Hata",
                JOptionPane.ERROR_MESSAGE);
            hesapNoAlani.setText("");
        }
    }
}