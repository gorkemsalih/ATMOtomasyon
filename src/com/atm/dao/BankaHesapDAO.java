package com.atm.dao;

import com.atm.config.VeritabaniAyar;
import java.sql.*;

public class BankaHesapDAO {
    public static boolean paraYatir(String hesapNo, double miktar) {
        String sql = "UPDATE hesaplar SET banka_bakiye = banka_bakiye + ? WHERE hesap_no = ?";
        
        try (Connection conn = VeritabaniAyar.baglantıAl();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, miktar);
            stmt.setString(2, hesapNo);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean paraCek(String hesapNo, double miktar) {
        String sql = "UPDATE hesaplar SET banka_bakiye = banka_bakiye - ? WHERE hesap_no = ? AND banka_bakiye >= ?";
        
        try (Connection conn = VeritabaniAyar.baglantıAl();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, miktar);
            stmt.setString(2, hesapNo);
            stmt.setDouble(3, miktar);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean dovizIslem(String hesapNo, String dovizTipi, double miktar, boolean isAlim) {
        String sqlBanka = isAlim ? 
            "UPDATE hesaplar SET banka_bakiye = banka_bakiye - ? WHERE hesap_no = ? AND banka_bakiye >= ?" :
            "UPDATE hesaplar SET banka_bakiye = banka_bakiye + ? WHERE hesap_no = ?";
            
        String sqlDoviz = dovizTipi.equals("Dolar") ?
            "UPDATE hesaplar SET dolar_bakiye = dolar_bakiye " + (isAlim ? "+" : "-") + " ? WHERE hesap_no = ?" + (!isAlim ? " AND dolar_bakiye >= ?" : "") :
            "UPDATE hesaplar SET euro_bakiye = euro_bakiye " + (isAlim ? "+" : "-") + " ? WHERE hesap_no = ?" + (!isAlim ? " AND euro_bakiye >= ?" : "");
        
        Connection conn = null;
        try {
            conn = VeritabaniAyar.baglantıAl();
            conn.setAutoCommit(false);
            
            if (isAlim) {
                PreparedStatement stmtBanka = conn.prepareStatement(sqlBanka);
                stmtBanka.setDouble(1, miktar);
                stmtBanka.setString(2, hesapNo);
                stmtBanka.setDouble(3, miktar);
                
                PreparedStatement stmtDoviz = conn.prepareStatement(sqlDoviz);
                stmtDoviz.setDouble(1, miktar);
                stmtDoviz.setString(2, hesapNo);
                
                int bankaSonuc = stmtBanka.executeUpdate();
                int dovizSonuc = stmtDoviz.executeUpdate();
                
                if (bankaSonuc > 0 && dovizSonuc > 0) {
                    conn.commit();
                    return true;
                }
            } else {
                PreparedStatement stmtDoviz = conn.prepareStatement(sqlDoviz);
                stmtDoviz.setDouble(1, miktar);
                stmtDoviz.setString(2, hesapNo);
                stmtDoviz.setDouble(3, miktar);
                
                PreparedStatement stmtBanka = conn.prepareStatement(sqlBanka);
                stmtBanka.setDouble(1, miktar);
                stmtBanka.setString(2, hesapNo);
                
                int dovizSonuc = stmtDoviz.executeUpdate();
                int bankaSonuc = stmtBanka.executeUpdate();
                
                if (bankaSonuc > 0 && dovizSonuc > 0) {
                    conn.commit();
                    return true;
                }
            }
            
            conn.rollback();
            return false;
            
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
    
    public static boolean altinIslem(String hesapNo, String altinTipi, double miktar, double tlTutar, boolean isAlim) {
        String sqlBanka = isAlim ? 
            "UPDATE hesaplar SET banka_bakiye = banka_bakiye - ? WHERE hesap_no = ? AND banka_bakiye >= ?" :
            "UPDATE hesaplar SET banka_bakiye = banka_bakiye + ? WHERE hesap_no = ?";
            
        String altinKolon = "";
        switch (altinTipi) {
            case "Gram Altın": altinKolon = "gram_altin"; break;
            case "Çeyrek Altın": altinKolon = "ceyrek_altin"; break;
            case "Yarım Altın": altinKolon = "yarim_altin"; break;
            case "Cumhuriyet Altını": altinKolon = "cumhuriyet_altin"; break;
        }
        
        String sqlAltin = "UPDATE hesaplar SET " + altinKolon + " = " + altinKolon + 
                         (isAlim ? " + " : " - ") + "? WHERE hesap_no = ?" +
                         (!isAlim ? " AND " + altinKolon + " >= ?" : "");
        
        Connection conn = null;
        try {
            conn = VeritabaniAyar.baglantıAl();
            conn.setAutoCommit(false);
            
            if (isAlim) {
                PreparedStatement stmtBanka = conn.prepareStatement(sqlBanka);
                stmtBanka.setDouble(1, tlTutar);
                stmtBanka.setString(2, hesapNo);
                stmtBanka.setDouble(3, tlTutar);
                
                PreparedStatement stmtAltin = conn.prepareStatement(sqlAltin);
                stmtAltin.setDouble(1, miktar);
                stmtAltin.setString(2, hesapNo);
                
                int bankaSonuc = stmtBanka.executeUpdate();
                int altinSonuc = stmtAltin.executeUpdate();
                
                if (bankaSonuc > 0 && altinSonuc > 0) {
                    conn.commit();
                    return true;
                }
            } else {
                PreparedStatement stmtAltin = conn.prepareStatement(sqlAltin);
                stmtAltin.setDouble(1, miktar);
                stmtAltin.setString(2, hesapNo);
                stmtAltin.setDouble(3, miktar);
                
                PreparedStatement stmtBanka = conn.prepareStatement(sqlBanka);
                stmtBanka.setDouble(1, tlTutar);
                stmtBanka.setString(2, hesapNo);
                
                int altinSonuc = stmtAltin.executeUpdate();
                int bankaSonuc = stmtBanka.executeUpdate();
                
                if (bankaSonuc > 0 && altinSonuc > 0) {
                    conn.commit();
                    return true;
                }
            }
            
            conn.rollback();
            return false;
            
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
    public static boolean paraTransfer(String gonderenHesap, String aliciHesap, double miktar) {
        Connection conn = null;
        try {
            conn = VeritabaniAyar.baglantıAl();
            conn.setAutoCommit(false);
            
            String sqlGonderen = "UPDATE hesaplar SET banka_bakiye = banka_bakiye - ? WHERE hesap_no = ? AND banka_bakiye >= ?";
            PreparedStatement stmtGonderen = conn.prepareStatement(sqlGonderen);
            stmtGonderen.setDouble(1, miktar);
            stmtGonderen.setString(2, gonderenHesap);
            stmtGonderen.setDouble(3, miktar);
            
            String sqlAlici = "UPDATE hesaplar SET banka_bakiye = banka_bakiye + ? WHERE hesap_no = ?";
            PreparedStatement stmtAlici = conn.prepareStatement(sqlAlici);
            stmtAlici.setDouble(1, miktar);
            stmtAlici.setString(2, aliciHesap);
            
            int gonderenSonuc = stmtGonderen.executeUpdate();
            int aliciSonuc = stmtAlici.executeUpdate();
            
            if (gonderenSonuc > 0 && aliciSonuc > 0) {
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