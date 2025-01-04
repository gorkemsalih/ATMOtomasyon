package com.atm.email;

public class EmailConfig {
    private static final String FROM_EMAIL = "gelisimbankasi00@gmail.com";
    private static final String PASSWORD = "iysp ltnq cmek zwiv";
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    
    public static String getFromEmail() { return FROM_EMAIL; }
    public static String getPassword() { return PASSWORD; }
    public static String getSmtpHost() { return SMTP_HOST; }
    public static String getSmtpPort() { return SMTP_PORT; }
}