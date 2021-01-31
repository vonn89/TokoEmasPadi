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
public class StokBarang {
    private final StringProperty tanggal = new SimpleStringProperty();
    private final StringProperty kodeBarcode = new SimpleStringProperty();
    private final StringProperty kodeJenis = new SimpleStringProperty();
    private final IntegerProperty stokAwal = new SimpleIntegerProperty();
    private final DoubleProperty beratAwal = new SimpleDoubleProperty();
    private final IntegerProperty stokMasuk = new SimpleIntegerProperty();
    private final DoubleProperty beratMasuk = new SimpleDoubleProperty();
    private final IntegerProperty stokKeluar = new SimpleIntegerProperty();
    private final DoubleProperty beratKeluar = new SimpleDoubleProperty();
    private final IntegerProperty stokAkhir = new SimpleIntegerProperty();
    private final DoubleProperty beratAkhir = new SimpleDoubleProperty();
    private Barang barang;

    public double getBeratAkhir() {
        return beratAkhir.get();
    }

    public void setBeratAkhir(double value) {
        beratAkhir.set(value);
    }

    public DoubleProperty beratAkhirProperty() {
        return beratAkhir;
    }

    public double getBeratKeluar() {
        return beratKeluar.get();
    }

    public void setBeratKeluar(double value) {
        beratKeluar.set(value);
    }

    public DoubleProperty beratKeluarProperty() {
        return beratKeluar;
    }

    public double getBeratMasuk() {
        return beratMasuk.get();
    }

    public void setBeratMasuk(double value) {
        beratMasuk.set(value);
    }

    public DoubleProperty beratMasukProperty() {
        return beratMasuk;
    }

    public double getBeratAwal() {
        return beratAwal.get();
    }

    public void setBeratAwal(double value) {
        beratAwal.set(value);
    }

    public DoubleProperty beratAwalProperty() {
        return beratAwal;
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

    public Barang getBarang() {
        return barang;
    }

    public void setBarang(Barang barang) {
        this.barang = barang;
    }
    
    public int getStokKeluar() {
        return stokKeluar.get();
    }

    public void setStokKeluar(int value) {
        stokKeluar.set(value);
    }

    public IntegerProperty stokKeluarProperty() {
        return stokKeluar;
    }

    public int getStokAkhir() {
        return stokAkhir.get();
    }

    public void setStokAkhir(int value) {
        stokAkhir.set(value);
    }

    public IntegerProperty stokAkhirProperty() {
        return stokAkhir;
    }


    public int getStokMasuk() {
        return stokMasuk.get();
    }

    public void setStokMasuk(int value) {
        stokMasuk.set(value);
    }

    public IntegerProperty stokMasukProperty() {
        return stokMasuk;
    }

    public int getStokAwal() {
        return stokAwal.get();
    }

    public void setStokAwal(int value) {
        stokAwal.set(value);
    }

    public IntegerProperty stokAwalProperty() {
        return stokAwal;
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

    public String getTanggal() {
        return tanggal.get();
    }

    public void setTanggal(String value) {
        tanggal.set(value);
    }

    public StringProperty tanggalProperty() {
        return tanggal;
    }
    
}
