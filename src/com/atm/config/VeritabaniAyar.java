package com.atm.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class VeritabaniAyar {
    private static final String URL = "jdbc:mysql://localhost:3306/atm_db";
    private static final String KULLANICI = "root";
    private static final String SIFRE = "";
    
    public static Connection baglantıAl() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, KULLANICI, SIFRE);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver bulunamadı.", e);
        }
    }
}