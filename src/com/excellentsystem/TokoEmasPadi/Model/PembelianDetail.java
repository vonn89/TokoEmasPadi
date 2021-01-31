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
public class PembelianDetail {
    private final StringProperty noPembelian = new SimpleStringProperty();
    private final IntegerProperty noUrut = new SimpleIntegerProperty();
    private final StringProperty kodeKategori = new SimpleStringProperty();
    private final StringProperty namaBarang = new SimpleStringProperty();
    private final IntegerProperty qty = new SimpleIntegerProperty();
    private final DoubleProperty berat = new SimpleDoubleProperty();
    private final StringProperty kondisi = new SimpleStringProperty();
    private final StringProperty statusSurat = new SimpleStringProperty();
    private final DoubleProperty hargaKomp = new SimpleDoubleProperty();
    private final DoubleProperty hargaBeli = new SimpleDoubleProperty();
    private PembelianHead pembelianHead;
    private PenjualanDetail penjualanDetail;

    public int getQty() {
        return qty.get();
    }

    public void setQty(int value) {
        qty.set(value);
    }

    public IntegerProperty qtyProperty() {
        return qty;
    }

    public PenjualanDetail getPenjualanDetail() {
        return penjualanDetail;
    }

    public void setPenjualanDetail(PenjualanDetail penjualanDetail) {
        this.penjualanDetail = penjualanDetail;
    }
    
    public PembelianHead getPembelianHead() {
        return pembelianHead;
    }

    public void setPembelianHead(PembelianHead pembelianHead) {
        this.pembelianHead = pembelianHead;
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

    public int getNoUrut() {
        return noUrut.get();
    }

    public void setNoUrut(int value) {
        noUrut.set(value);
    }

    public IntegerProperty noUrutProperty() {
        return noUrut;
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


    public String getKondisi() {
        return kondisi.get();
    }

    public void setKondisi(String value) {
        kondisi.set(value);
    }

    public StringProperty kondisiProperty() {
        return kondisi;
    }

    
    public double getHargaBeli() {
        return hargaBeli.get();
    }

    public void setHargaBeli(double value) {
        hargaBeli.set(value);
    }

    public DoubleProperty hargaBeliProperty() {
        return hargaBeli;
    }

    public double getHargaKomp() {
        return hargaKomp.get();
    }

    public void setHargaKomp(double value) {
        hargaKomp.set(value);
    }

    public DoubleProperty hargaKompProperty() {
        return hargaKomp;
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

    public String getNoPembelian() {
        return noPembelian.get();
    }

    public void setNoPembelian(String value) {
        noPembelian.set(value);
    }

    public StringProperty noPembelianProperty() {
        return noPembelian;
    }
    
}
