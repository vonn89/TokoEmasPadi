/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Xtreme
 */
public class StokOpnameDetail {
    private final StringProperty noStokOpname = new SimpleStringProperty();
    private final IntegerProperty noUrut = new SimpleIntegerProperty();
    private final StringProperty kodeBarcode = new SimpleStringProperty();
    private final IntegerProperty qty = new SimpleIntegerProperty();
    private final IntegerProperty qtyDiStok = new SimpleIntegerProperty();
    private Barang barang;

    public int getQtyDiStok() {
        return qtyDiStok.get();
    }

    public void setQtyDiStok(int value) {
        qtyDiStok.set(value);
    }

    public IntegerProperty qtyDiStokProperty() {
        return qtyDiStok;
    }

    public int getQty() {
        return qty.get();
    }

    public void setQty(int value) {
        qty.set(value);
    }

    public IntegerProperty qtyProperty() {
        return qty;
    }

    public int getNoUrut() {
        return noUrut.get();
    }

    public void setNoUrut(int value) {
        noUrut.set(value);
    }

    public IntegerProperty noUrutProperty() {
        return noUrut;
    }


    public Barang getBarang() {
        return barang;
    }

    public void setBarang(Barang barang) {
        this.barang = barang;
    }


    public String getKodeBarcode() {
        return kodeBarcode.get();
    }

    public void setKodeBarcode(String value) {
        kodeBarcode.set(value);
    }

    public StringProperty kodeBarcodeProperty() {
        return kodeBarcode;
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
