package com.atm.dao;

import com.atm.config.VeritabaniAyar;
import com.atm.model.Hesap;
import java.sql.*;

public class HesapDAO {
    public static boolean hesapNoDogrula(String hesapNo) {
        if (hesapNo == null || hesapNo.trim().isEmpty()) {
            return false;
        }
        
        String sql = "SELECT hesap_no FROM hesaplar WHERE hesap_no = ?";
        
        try (Connection conn = VeritabaniAyar.baglantıAl();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, hesapNo.trim());
            ResultSet rs = stmt.executeQuery();
            return rs.next();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static Hesap sifreDogrula(String hesapNo, String sifre) {
        if (hesapNo == null || sifre == null || 
            hesapNo.trim().isEmpty() || sifre.trim().isEmpty()) {
            return null;
        }
        
        String sql = "SELECT * FROM hesaplar WHERE hesap_no = ? AND sifre = ?";
        
        try (Connection conn = VeritabaniAyar.baglantıAl();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, hesapNo.trim());
            stmt.setString(2, sifre.trim());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Hesap(
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
                );
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}