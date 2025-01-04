package com.atm.dao;

import com.atm.config.VeritabaniAyar;
import com.atm.model.Admin;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    public static Admin girisYap(String kullaniciAdi, String sifre) {
        String sql = "SELECT * FROM adminler WHERE kullanici_adi = ? AND sifre = ?";
        
        try (Connection conn = VeritabaniAyar.baglantıAl();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, kullaniciAdi);
            stmt.setString(2, sifre);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Admin(
                    rs.getInt("id"),
                    rs.getString("kullanici_adi"),
                    rs.getString("sifre"),
                    rs.getString("ad_soyad")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean sifreDegistir(int adminId, String yeniSifre) {
        String sql = "UPDATE adminler SET sifre = ? WHERE id = ?";
        
        try (Connection conn = VeritabaniAyar.baglantıAl();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, yeniSifre);
            stmt.setInt(2, adminId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean adminEkle(String kullaniciAdi, String sifre, String adSoyad) {
        String sql = "INSERT INTO adminler (kullanici_adi, sifre, ad_soyad) VALUES (?, ?, ?)";
        
        try (Connection conn = VeritabaniAyar.baglantıAl();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, kullaniciAdi);
            stmt.setString(2, sifre);
            stmt.setString(3, adSoyad);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static List<Admin> tumAdminleriGetir() {
        List<Admin> adminler = new ArrayList<>();
        String sql = "SELECT * FROM adminler";
        
        try (Connection conn = VeritabaniAyar.baglantıAl();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                adminler.add(new Admin(
                    rs.getInt("id"),
                    rs.getString("kullanici_adi"),
                    rs.getString("sifre"),
                    rs.getString("ad_soyad")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adminler;
    }
    
    public static boolean adminSil(int adminId) {
        String sql = "DELETE FROM adminler WHERE id = ?";
        
        try (Connection conn = VeritabaniAyar.baglantıAl();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, adminId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}