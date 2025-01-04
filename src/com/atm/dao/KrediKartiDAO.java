package com.atm.dao;

import com.atm.config.VeritabaniAyar;
import com.atm.model.KrediKarti;
import com.atm.model.KrediKartiLimit;
import com.atm.util.KrediKartiUtil;

import java.sql.*;

public class KrediKartiDAO {
	 public static KrediKarti krediKartiOlustur(int hesapId) {
	        String sql = "INSERT INTO kredi_kartlari (hesap_id, kart_no, son_kullanim, cvv, limit_miktar, faiz_orani) " +
	                     "VALUES (?, ?, ?, ?, ?, ?)";
	        
	        try (Connection conn = VeritabaniAyar.baglantıAl();
	             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	            
	            String kartNo = KrediKartiUtil.kartNoUret();
	            String sonKullanim = KrediKartiUtil.sonKullanimUret();
	            String cvv = KrediKartiUtil.cvvUret();
	            
	            KrediKartiLimit limit = KrediKartiLimit.varsayilan();

	            stmt.setInt(1, hesapId);
	            stmt.setString(2, kartNo);
	            stmt.setString(3, sonKullanim);
	            stmt.setString(4, cvv);
	            stmt.setDouble(5, limit.getLimit());
	            stmt.setDouble(6, limit.getFaizOrani());

	            int etkilenenSatir = stmt.executeUpdate();
	            if (etkilenenSatir > 0) {
	                try (ResultSet rs = stmt.getGeneratedKeys()) {
	                    if (rs.next()) {
	                        int kartId = rs.getInt(1);
	                        return new KrediKarti(kartId, hesapId, kartNo, sonKullanim, 
	                                            cvv, 0, limit.getLimit(), limit.getFaizOrani());
	                    }
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	    
    public static KrediKarti krediKartiBul(int hesapId) {
        String sql = "SELECT * FROM kredi_kartlari WHERE hesap_id = ?";
        
        try (Connection conn = VeritabaniAyar.baglantıAl();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, hesapId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new KrediKarti(
                    rs.getInt("id"),
                    rs.getInt("hesap_id"),
                    rs.getString("kart_no"),
                    rs.getString("son_kullanim"),
                    rs.getString("cvv"),
                    rs.getDouble("bakiye"),
                    rs.getDouble("limit_miktar"),
                    rs.getDouble("faiz_orani")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean krediKullan(int kartId, double miktar, double faizliTutar) {
        String sql = "UPDATE kredi_kartlari SET bakiye = bakiye + ? WHERE id = ? AND (bakiye + ?) <= limit_miktar";
        
        try (Connection conn = VeritabaniAyar.baglantıAl();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, faizliTutar);
            stmt.setInt(2, kartId);
            stmt.setDouble(3, faizliTutar);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean limitGuncelle(int kartId, double yeniLimit) {
        String sql = "UPDATE kredi_kartlari SET limit_miktar = ? WHERE id = ?";
        
        try (Connection conn = VeritabaniAyar.baglantıAl();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Debugging: SQL sorgusunu ve parametreleri yazdırma
            System.out.println("SQL Sorgusu: " + sql);
            System.out.println("Kart ID: " + kartId);
            System.out.println("Yeni Limit: " + yeniLimit);
            
            stmt.setDouble(1, yeniLimit);
            stmt.setInt(2, kartId);
            
            int etkilenensatir = stmt.executeUpdate();
            
            // Debugging: Etkilenen satır sayısını kontrol etme
            System.out.println("Etkilenen satır sayısı: " + etkilenensatir);
            
            if (etkilenensatir > 0) {
                return true;
            } else {
                System.out.println("Güncellenen satır yok: Kart ID: " + kartId); // Debugging
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Hata mesajlarını yazdırma
            return false;
        }
    }

    public static boolean borcOde(int kartId, double miktar) {
        String sql = "UPDATE kredi_kartlari SET bakiye = bakiye - ? WHERE id = ? AND bakiye >= ?";
        
        try (Connection conn = VeritabaniAyar.baglantıAl();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, miktar);
            stmt.setInt(2, kartId);
            stmt.setDouble(3, miktar);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean borcOdeBankaHesabindan(int kartId, String hesapNo, double miktar) {
        String sqlKart = "UPDATE kredi_kartlari SET bakiye = bakiye - ? WHERE id = ? AND bakiye >= ?";
        String sqlHesap = "UPDATE hesaplar SET banka_bakiye = banka_bakiye - ? WHERE hesap_no = ? AND banka_bakiye >= ?";
        
        Connection conn = null;
        try {
            conn = VeritabaniAyar.baglantıAl();
            conn.setAutoCommit(false);
            
            PreparedStatement kartStmt = conn.prepareStatement(sqlKart);
            kartStmt.setDouble(1, miktar);
            kartStmt.setInt(2, kartId);
            kartStmt.setDouble(3, miktar);
            
            PreparedStatement hesapStmt = conn.prepareStatement(sqlHesap);
            hesapStmt.setDouble(1, miktar);
            hesapStmt.setString(2, hesapNo);
            hesapStmt.setDouble(3, miktar);
            
            int kartSonuc = kartStmt.executeUpdate();
            int hesapSonuc = hesapStmt.executeUpdate();
            
            if (kartSonuc > 0 && hesapSonuc > 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }   
   

}