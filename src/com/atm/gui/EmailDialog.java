package com.atm.gui;

import com.atm.util.EmailUtil;
import com.atm.util.EmailValidator;
import javax.swing.*;
import java.awt.*;

public class EmailDialog extends JDialog {
    private final JTextField emailField;
    
    public EmailDialog(Window owner) {
        super(owner, "E-posta Gönderimi", ModalityType.APPLICATION_MODAL);
        
        setLayout(new BorderLayout(10, 10));
        
        // Email giriş alanı
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(new JLabel("E-posta adresinizi giriniz:"), BorderLayout.NORTH);
        emailField = new JTextField(30);
        inputPanel.add(emailField, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.CENTER);
        
        // Butonlar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton gonderButton = new JButton("Gönder");
        JButton iptalButton = new JButton("İptal");
        
        gonderButton.addActionListener(e -> gonder());
        iptalButton.addActionListener(e -> dispose());
        
        buttonPanel.add(gonderButton);
        buttonPanel.add(iptalButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(owner);
    }
    
    public void showDialog(String content) {
        this.content = content;
        emailField.setText("");
        setVisible(true);
    }
    
    private String content;
    
    private void gonder() {
        String email = emailField.getText().trim();
        
        if (!EmailValidator.isValid(email)) {
            JOptionPane.showMessageDialog(this,
                "Geçersiz e-posta adresi!",
                "Hata",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (EmailUtil.sendEmail(email, "Hesap Bilgileri", content)) {
            JOptionPane.showMessageDialog(this,
                "Hesap bilgileri e-posta adresinize gönderildi.",
                "Başarılı",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "E-posta gönderimi başarısız oldu!",
                "Hata",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}