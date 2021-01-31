/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.Model;

import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Xtreme
 */
public class User {
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty level = new SimpleStringProperty();
    private List<Otoritas> otoritas;
    private List<Verifikasi> verifikasi;

    public List<Verifikasi> getVerifikasi() {
        return verifikasi;
    }

    public void setVerifikasi(List<Verifikasi> verifikasi) {
        this.verifikasi = verifikasi;
    }
    
    public List<Otoritas> getOtoritas() {
        return otoritas;
    }

    public void setOtoritas(List<Otoritas> otoritas) {
        this.otoritas = otoritas;
    }
    
    public String getLevel() {
        return level.get();
    }

    public void setLevel(String value) {
        level.set(value);
    }

    public StringProperty levelProperty() {
        return level;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String value) {
        password.set(value);
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String value) {
        username.set(value);
    }

    public StringProperty usernameProperty() {
        return username;
    }
    
}
