package com.atm.util;

import javax.swing.*;
import java.awt.*;

public class ArayuzUtil {
    public static final Color ANARENK = new Color(70, 130, 180);
    public static final Color BUTONRENK = new Color(100, 149, 237);
    public static final Font BASLIKYAZI = new Font("Arial", Font.BOLD, 24);
    public static final Font NORMALYAZI = new Font("Arial", Font.BOLD, 16);
    
    public static JPanel tuslarOlustur(JTextField alan) {
        JPanel tuslar = new JPanel(new GridLayout(4, 3, 5, 5));
        
        for (int i = 1; i <= 9; i++) {
            final String sayi = String.valueOf(i);
            JButton buton = stilliButonOlustur(sayi);
            buton.addActionListener(e -> alan.setText(alan.getText() + sayi));
            tuslar.add(buton);
        }
        
        JButton temizleButon = stilliButonOlustur("Sil");
        temizleButon.addActionListener(e -> alan.setText(""));
        tuslar.add(temizleButon);
        
        JButton sifirButon = stilliButonOlustur("0");
        sifirButon.addActionListener(e -> alan.setText(alan.getText() + "0"));
        tuslar.add(sifirButon);
        
        return tuslar;
    }
    
    public static JButton stilliButonOlustur(String yazi) {
        JButton buton = new JButton(yazi);
        buton.setFont(NORMALYAZI);
        buton.setBackground(BUTONRENK);
        buton.setForeground(Color.WHITE);
        buton.setFocusPainted(false);
        return buton;
    }
}