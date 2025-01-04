package com.atm.gui;

import com.atm.model.Hesap;
import com.atm.model.Admin;
import javax.swing.*;
import java.awt.*;

public class ATMFrame extends JFrame {
    private CardLayout kartDuzen;
    private JPanel anaPaneli;
    
    public ATMFrame() {
        setTitle("Gelişim Bankası ATM");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        kartDuzen = new CardLayout();
        anaPaneli = new JPanel(kartDuzen);
        
        anaPaneli.add(new HesapGirisPaneli(this), "HESAP");
        
        add(anaPaneli);
        kartDuzen.show(anaPaneli, "HESAP");
    }
    
    public void hesapGirisiGoster() {
        anaPaneli.removeAll();
        anaPaneli.add(new HesapGirisPaneli(this), "HESAP");
        kartDuzen.show(anaPaneli, "HESAP");
        validate();
    }
    
    public void adminGirisiGoster() {
        anaPaneli.removeAll();
        anaPaneli.add(new AdminGirisPaneli(this), "ADMIN_GIRIS");
        kartDuzen.show(anaPaneli, "ADMIN_GIRIS");
        validate();
    }
    
    public void adminMenuGoster(Admin admin) {
        anaPaneli.removeAll();
        anaPaneli.add(new AdminMenuPaneli(this, admin), "ADMIN_MENU");
        kartDuzen.show(anaPaneli, "ADMIN_MENU");
        validate();
    }
    
    public void sifreGirisiGoster(String hesapNo) {
        anaPaneli.removeAll();
        SifreGirisPaneli sifrePaneli = new SifreGirisPaneli(this);
        sifrePaneli.hesapNoAyarla(hesapNo);
        anaPaneli.add(sifrePaneli, "SIFRE");
        kartDuzen.show(anaPaneli, "SIFRE");
        validate();
    }
    
    public void anaMenuGoster(Hesap hesap) {
        anaPaneli.removeAll();
        anaPaneli.add(new AnaMenuPaneli(this, hesap), "MENU");
        kartDuzen.show(anaPaneli, "MENU");
        validate();
    }
    
    public void krediKartiMenuGoster(Hesap hesap) {
        anaPaneli.removeAll();
        anaPaneli.add(new KrediKartiMenuPaneli(this, hesap), "KREDI_KARTI");
        kartDuzen.show(anaPaneli, "KREDI_KARTI");
        validate();
    }
    
    public void bankaHesapMenuGoster(Hesap hesap) {
        anaPaneli.removeAll();
        anaPaneli.add(new BankaHesapMenuPaneli(this, hesap), "BANKA_HESAP");
        kartDuzen.show(anaPaneli, "BANKA_HESAP");
        validate();
    }
    
    public void dovizAltinMenuGoster(Hesap hesap) {
        anaPaneli.removeAll();
        anaPaneli.add(new DovizAltinMenuPaneli(this, hesap), "DOVIZ_ALTIN");
        kartDuzen.show(anaPaneli, "DOVIZ_ALTIN");
        validate();
    }
}