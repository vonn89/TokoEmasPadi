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
public class GadaiDetail {
    private final StringProperty noGadai = new SimpleStringProperty();
    private final IntegerProperty noUrut = new SimpleIntegerProperty();
    private final StringProperty kodeKategori = new SimpleStringProperty();
    private final StringProperty namaBarang = new SimpleStringProperty();
    private final IntegerProperty qty = new SimpleIntegerProperty();
    private final DoubleProperty berat = new SimpleDoubleProperty();
    private final StringProperty kondisi = new SimpleStringProperty();
    private final StringProperty statusSurat = new SimpleStringProperty();
    private final DoubleProperty nilaiJual = new SimpleDoubleProperty();
    private GadaiHead gadaiHead;
    private Kategori kategori;
    private PembelianDetail pembelianDetail;

    public int getQty() {
        return qty.get();
    }

    public void setQty(int value) {
        qty.set(value);
    }

    public IntegerProperty qtyProperty() {
        return qty;
    }

    public String getStatusSurat() {
        return statusSurat.get();
    }

    public void setStatusSurat(String value) {
        statusSurat.set(value);
    }

    public StringProperty statusSuratProperty() {
        return statusSurat;
    }

    public String getKondisi() {
        return kondisi.get();
    }

    public void setKondisi(String value) {
        kondisi.set(value);
    }

    public StringProperty kondisiProperty() {
        return kondisi;
    }

    public PembelianDetail getPembelianDetail() {
        return pembelianDetail;
    }

    public void setPembelianDetail(PembelianDetail pembelianDetail) {
        this.pembelianDetail = pembelianDetail;
    }
    
    public Kategori getKategori() {
        return kategori;
    }

    public void setKategori(Kategori kategori) {
        this.kategori = kategori;
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

    public GadaiHead getGadaiHead() {
        return gadaiHead;
    }

    public void setGadaiHead(GadaiHead gadaiHead) {
        this.gadaiHead = gadaiHead;
    }
    

    public double getNilaiJual() {
        return nilaiJual.get();
    }

    public void setNilaiJual(double value) {
        nilaiJual.set(value);
    }

    public DoubleProperty nilaiJualProperty() {
        return nilaiJual;
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

    public String getNamaBarang() {
        return namaBarang.get();
    }

    public void setNamaBarang(String value) {
        namaBarang.set(value);
    }

    public StringProperty namaBarangProperty() {
        return namaBarang;
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

    public String getNoGadai() {
        return noGadai.get();
    }

    public void setNoGadai(String value) {
        noGadai.set(value);
    }

    public StringProperty noGadaiProperty() {
        return noGadai;
    }
    
}
