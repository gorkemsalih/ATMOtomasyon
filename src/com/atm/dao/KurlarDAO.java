package com.atm.dao;

import com.atm.config.VeritabaniAyar;
import com.atm.model.Kurlar;
import java.sql.*;

public class KurlarDAO {
    public static boolean kurGuncelle(double dolarKuru, double euroKuru, 
                                    double gramAltin, double ceyrekAltin, 
                                    double yarimAltin, double cumhuriyetAltin,
                                    double faizOrani) {
        String sql = "UPDATE kurlar SET dolar_kuru = ?, euro_kuru = ?, " +
                    "gram_altin = ?, ceyrek_altin = ?, yarim_altin = ?, " +
                    "cumhuriyet_altin = ? WHERE id = 1";
                    
        String faizSql = "UPDATE faiz_oranlari SET oran = ? WHERE id = 1";
        
        Connection conn = null;
        try {
            conn = VeritabaniAyar.baglantıAl();
            conn.setAutoCommit(false);
            
            PreparedStatement kurStmt = conn.prepareStatement(sql);
            kurStmt.setDouble(1, dolarKuru);
            kurStmt.setDouble(2, euroKuru);
            kurStmt.setDouble(3, gramAltin);
            kurStmt.setDouble(4, ceyrekAltin);
            kurStmt.setDouble(5, yarimAltin);
            kurStmt.setDouble(6, cumhuriyetAltin);
            
            PreparedStatement faizStmt = conn.prepareStatement(faizSql);
            faizStmt.setDouble(1, faizOrani);
            
            int kurSonuc = kurStmt.executeUpdate();
            int faizSonuc = faizStmt.executeUpdate();
            
            if (kurSonuc > 0 && faizSonuc > 0) {
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
    
    public static Kurlar kurlariGetir() {
        String sql = "SELECT k.*, f.oran FROM kurlar k, faiz_oranlari f WHERE k.id = 1 AND f.id = 1";
        
        try (Connection conn = VeritabaniAyar.baglantıAl();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return new Kurlar(
                    rs.getDouble("dolar_kuru"),
                    rs.getDouble("euro_kuru"),
                    rs.getDouble("gram_altin"),
                    rs.getDouble("ceyrek_altin"),
                    rs.getDouble("yarim_altin"),
                    rs.getDouble("cumhuriyet_altin"),
                    rs.getDouble("oran")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}