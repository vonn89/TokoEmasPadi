/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.Model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Xtreme
 */
public class Barang {
    private final StringProperty kodeBarcode = new SimpleStringProperty();
    private final StringProperty namaBarang = new SimpleStringProperty();
    private final StringProperty namaBarcode = new SimpleStringProperty();
    private final StringProperty kodeKategori = new SimpleStringProperty();
    private final StringProperty kodeJenis = new SimpleStringProperty();
    private final DoubleProperty berat = new SimpleDoubleProperty();
    private final DoubleProperty beratPembulatan = new SimpleDoubleProperty();
    private final DoubleProperty nilaiPokok = new SimpleDoubleProperty();
    private final DoubleProperty hargaJual = new SimpleDoubleProperty();
    private final StringProperty keterangan = new SimpleStringProperty();
    private final StringProperty merk = new SimpleStringProperty();
    private final DoubleProperty persentase = new SimpleDoubleProperty();
    private final StringProperty barcodeDate = new SimpleStringProperty();
    private final StringProperty barcodeBy = new SimpleStringProperty();
    private final IntegerProperty qty = new SimpleIntegerProperty();
    private Kategori kategori;
    private StokBarang stokBarang;
    private final BooleanProperty checked = new SimpleBooleanProperty();

    public double getPersentase() {
        return persentase.get();
    }

    public void setPersentase(double value) {
        persentase.set(value);
    }

    public DoubleProperty persentaseProperty() {
        return persentase;
    }

    public StokBarang getStokBarang() {
        return stokBarang;
    }

    public void setStokBarang(StokBarang stokBarang) {
        this.stokBarang = stokBarang;
    }

    public boolean isChecked() {
        return checked.get();
    }

    public void setChecked(boolean value) {
        checked.set(value);
    }

    public BooleanProperty checkedProperty() {
        return checked;
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

    public String getMerk() {
        return merk.get();
    }

    public void setMerk(String value) {
        merk.set(value);
    }

    public StringProperty merkProperty() {
        return merk;
    }

    public String getNamaBarcode() {
        return namaBarcode.get();
    }

    public void setNamaBarcode(String value) {
        namaBarcode.set(value);
    }

    public StringProperty namaBarcodeProperty() {
        return namaBarcode;
    }

    public String getBarcodeBy() {
        return barcodeBy.get();
    }

    public void setBarcodeBy(String value) {
        barcodeBy.set(value);
    }

    public StringProperty barcodeByProperty() {
        return barcodeBy;
    }

    public String getBarcodeDate() {
        return barcodeDate.get();
    }

    public void setBarcodeDate(String value) {
        barcodeDate.set(value);
    }

    public StringProperty barcodeDateProperty() {
        return barcodeDate;
    }


    public double getHargaJual() {
        return hargaJual.get();
    }

    public void setHargaJual(double value) {
        hargaJual.set(value);
    }

    public DoubleProperty hargaJualProperty() {
        return hargaJual;
    }


    public Kategori getKategori() {
        return kategori;
    }

    public void setKategori(Kategori kategori) {
        this.kategori = kategori;
    }

    public double getNilaiPokok() {
        return nilaiPokok.get();
    }

    public void setNilaiPokok(double value) {
        nilaiPokok.set(value);
    }

    public DoubleProperty nilaiPokokProperty() {
        return nilaiPokok;
    }

    public double getBeratPembulatan() {
        return beratPembulatan.get();
    }

    public void setBeratPembulatan(double value) {
        beratPembulatan.set(value);
    }

    public DoubleProperty beratPembulatanProperty() {
        return beratPembulatan;
    }
   
    public double getBerat() {
        return berat.get();
    }

    public void setBerat(double value) {
        berat.set(value);
    }

    public DoubleProperty beratProperty() {
        return berat;
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

    public String getKeterangan() {
        return keterangan.get();
    }

    public void setKeterangan(String value) {
        keterangan.set(value);
    }

    public StringProperty keteranganProperty() {
        return keterangan;
    }

    public String getNamaBarang() {
        return namaBarang.get();
    }

    public void setNamaBarang(String value) {
        namaBarang.set(value);
    }

    public StringProperty namaBarangProperty() {
        return namaBarang;
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
    
}
