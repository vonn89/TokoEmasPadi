/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Xtreme
 */
public class Sistem {
    private final StringProperty nama = new SimpleStringProperty();
    private final StringProperty alamat = new SimpleStringProperty();
    private final StringProperty kota = new SimpleStringProperty();
    private final StringProperty noTelp = new SimpleStringProperty();
    private final StringProperty website = new SimpleStringProperty();
    private final StringProperty tglSystem = new SimpleStringProperty();
    private final DoubleProperty persentasePinjaman = new SimpleDoubleProperty();
    private final IntegerProperty jatuhTempoGadai = new SimpleIntegerProperty();
    private final StringProperty code = new SimpleStringProperty();

    public String getWebsite() {
        return website.get();
    }

    public void setWebsite(String value) {
        website.set(value);
    }

    public StringProperty websiteProperty() {
        return website;
    }
    

    public String getCode() {
        return code.get();
    }

    public void setCode(String value) {
        code.set(value);
    }

    public StringProperty codeProperty() {
        return code;
    }
    public int getJatuhTempoGadai() {
        return jatuhTempoGadai.get();
    }

    public void setJatuhTempoGadai(int value) {
        jatuhTempoGadai.set(value);
    }

    public IntegerProperty jatuhTempoGadaiProperty() {
        return jatuhTempoGadai;
    }

    public double getPersentasePinjaman() {
        return persentasePinjaman.get();
    }

    public void setPersentasePinjaman(double value) {
        persentasePinjaman.set(value);
    }

    public DoubleProperty persentasePinjamanProperty() {
        return persentasePinjaman;
    }
    
    
    public String getTglSystem() {
        return tglSystem.get();
    }

    public void setTglSystem(String value) {
        tglSystem.set(value);
    }

    public StringProperty tglSystemProperty() {
        return tglSystem;
    }


    public String getNoTelp() {
        return noTelp.get();
    }

    public void setNoTelp(String value) {
        noTelp.set(value);
    }

    public StringProperty noTelpProperty() {
        return noTelp;
    }

    public String getKota() {
        return kota.get();
    }

    public void setKota(String value) {
        kota.set(value);
    }

    public StringProperty kotaProperty() {
        return kota;
    }

    public String getAlamat() {
        return alamat.get();
    }

    public void setAlamat(String value) {
        alamat.set(value);
    }

    public StringProperty alamatProperty() {
        return alamat;
    }

    public String getNama() {
        return nama.get();
    }

    public void setNama(String value) {
        nama.set(value);
    }

    public StringProperty namaProperty() {
        return nama;
    }
    
}
