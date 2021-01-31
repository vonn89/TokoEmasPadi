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
public class StokOpnameHead {
    private final StringProperty noStokOpname = new SimpleStringProperty();
    private final StringProperty tglStokOpname = new SimpleStringProperty();
    private final StringProperty kodeKategori = new SimpleStringProperty();
    private final StringProperty kodeJenis = new SimpleStringProperty();
    private final IntegerProperty totalQty = new SimpleIntegerProperty();
    private final DoubleProperty totalBerat = new SimpleDoubleProperty();
    private final DoubleProperty totalBeratPembulatan = new SimpleDoubleProperty();

    public double getTotalBeratPembulatan() {
        return totalBeratPembulatan.get();
    }

    public void setTotalBeratPembulatan(double value) {
        totalBeratPembulatan.set(value);
    }

    public DoubleProperty totalBeratPembulatanProperty() {
        return totalBeratPembulatan;
    }

    public double getTotalBerat() {
        return totalBerat.get();
    }

    public void setTotalBerat(double value) {
        totalBerat.set(value);
    }

    public DoubleProperty totalBeratProperty() {
        return totalBerat;
    }

    public int getTotalQty() {
        return totalQty.get();
    }

    public void setTotalQty(int value) {
        totalQty.set(value);
    }

    public IntegerProperty totalQtyProperty() {
        return totalQty;
    }

    public String getKodeJenis() {
        return kodeJenis.get();
    }

    public void setKodeJenis(String value) {
        kodeJenis.set(value);
    }

    public StringProperty kodeJenisProperty() {
        return kodeJenis;
    }

    public String getKodeKategori() {
        return kodeKategori.get();
    }

    public void setKodeKategori(String value) {
        kodeKategori.set(value);
    }

    public StringProperty kodeKategoriProperty() {
        return kodeKategori;
    }

    public String getTglStokOpname() {
        return tglStokOpname.get();
    }

    public void setTglStokOpname(String value) {
        tglStokOpname.set(value);
    }

    public StringProperty tglStokOpnameProperty() {
        return tglStokOpname;
    }

    public String getNoStokOpname() {
        return noStokOpname.get();
    }

    public void setNoStokOpname(String value) {
        noStokOpname.set(value);
    }

    public StringProperty noStokOpnameProperty() {
        return noStokOpname;
    }
    
}
