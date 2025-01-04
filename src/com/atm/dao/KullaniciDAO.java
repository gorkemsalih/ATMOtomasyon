package com.atm.dao;

import com.atm.config.VeritabaniAyar;
import com.atm.model.Hesap;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KullaniciDAO {
    
    public static String hesapNoUret() {
        Random random = new Random();
        StringBuilder hesapNo = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            hesapNo.append(random.nextInt(10));
        }
        return hesapNo.toString();
    }
    
    public static boolean hesapEkle(String adSoyad, String sifre, double baslangicBakiye) {
        if (adSoyad == null || sifre == null || 
            adSoyad.trim().isEmpty() || sifre.trim().isEmpty()) {
            return false;
        }
        
        String sql = "INSERT INTO hesaplar (hesap_no, ad_soyad, sifre, banka_bakiye) VALUES (?, ?, ?, ?)";
        String hesapNo = hesapNoUret();
        
        try (Connection conn = VeritabaniAyar.baglantıAl();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, hesapNo);
            stmt.setString(2, adSoyad.trim());
            stmt.setString(3, sifre.trim());
            stmt.setDouble(4, baslangicBakiye);
            
            int sonuc = stmt.executeUpdate();
            if (sonuc > 0) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null,
                        String.format("Hesap başarıyla oluşturuldu!\n\nHesap No: %s\nAd Soyad: %s\nBakiye: %.2f TL",
                            hesapNo, adSoyad, baslangicBakiye),
                        "Hesap Bilgileri",
                        JOptionPane.INFORMATION_MESSAGE);
                });
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null,
                    "Hesap oluşturulurken bir hata oluştu!",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
            });
            return false;
        }
    }
    
    public static List<Hesap> tumHesaplariGetir() {
        List<Hesap> hesaplar = new ArrayList<>();
        String sql = "SELECT * FROM hesaplar";
        
        try (Connection conn = VeritabaniAyar.baglantıAl();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                hesaplar.add(new Hesap(
                    rs.getInt("id"),
                    rs.getString("hesap_no"),
                    rs.getString("ad_soyad"),
                    rs.getString("sifre"),
                    rs.getDouble("banka_bakiye"),
                    rs.getDouble("dolar_bakiye"),
                    rs.getDouble("euro_bakiye"),
                    rs.getDouble("gram_altin"),
                    rs.getDouble("ceyrek_altin"),
                    rs.getDouble("yarim_altin"),
                    rs.getDouble("cumhuriyet_altin")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hesaplar;
    }
    
    public static boolean hesapSil(String hesapNo) {
        String sql = "DELETE FROM hesaplar WHERE hesap_no = ?";
        
        try (Connection conn = VeritabaniAyar.baglantıAl();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, hesapNo);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}