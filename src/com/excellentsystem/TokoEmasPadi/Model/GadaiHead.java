/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.Model;

import java.util.List;
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
public class GadaiHead {
    private final StringProperty noGadai = new SimpleStringProperty();
    private final StringProperty tglGadai = new SimpleStringProperty();
    private final StringProperty kodeSales = new SimpleStringProperty();
    private final StringProperty nama = new SimpleStringProperty();
    private final StringProperty alamat = new SimpleStringProperty();
    private final StringProperty keterangan = new SimpleStringProperty();
    private final IntegerProperty totalQty = new SimpleIntegerProperty();
    private final DoubleProperty totalBerat = new SimpleDoubleProperty();
    private final DoubleProperty totalPinjaman = new SimpleDoubleProperty();
    private final IntegerProperty lamaPinjam = new SimpleIntegerProperty();
    private final DoubleProperty bungaPersen = new SimpleDoubleProperty();
    private final DoubleProperty bungaKomp = new SimpleDoubleProperty();
    private final DoubleProperty bungaRp = new SimpleDoubleProperty();
    private final StringProperty jatuhTempo = new SimpleStringProperty();
    private final StringProperty statusLunas = new SimpleStringProperty();
    private final StringProperty tglLunas = new SimpleStringProperty();
    private final StringProperty salesLunas = new SimpleStringProperty();
    private final StringProperty statusBatal = new SimpleStringProperty();
    private final StringProperty tglBatal = new SimpleStringProperty();
    private final StringProperty salesBatal = new SimpleStringProperty();
    private List<GadaiDetail> listGadaiDetail;

    public int getTotalQty() {
        return totalQty.get();
    }

    public void setTotalQty(int value) {
        totalQty.set(value);
    }

    public IntegerProperty totalQtyProperty() {
        return totalQty;
    }

    public List<GadaiDetail> getListGadaiDetail() {
        return listGadaiDetail;
    }

    public void setListGadaiDetail(List<GadaiDetail> listGadaiDetail) {
        this.listGadaiDetail = listGadaiDetail;
    }
    
    
    public String getStatusBatal() {
        return statusBatal.get();
    }

    public void setStatusBatal(String value) {
        statusBatal.set(value);
    }

    public StringProperty statusBatalProperty() {
        return statusBatal;
    }

    public String getStatusLunas() {
        return statusLunas.get();
    }

    public void setStatusLunas(String value) {
        statusLunas.set(value);
    }

    public StringProperty statusLunasProperty() {
        return statusLunas;
    }

    public String getJatuhTempo() {
        return jatuhTempo.get();
    }

    public void setJatuhTempo(String value) {
        jatuhTempo.set(value);
    }

    public StringProperty jatuhTempoProperty() {
        return jatuhTempo;
    }

    
    public String getSalesBatal() {
        return salesBatal.get();
    }

    public void setSalesBatal(String value) {
        salesBatal.set(value);
    }

    public StringProperty salesBatalProperty() {
        return salesBatal;
    }

    public String getTglBatal() {
        return tglBatal.get();
    }

    public void setTglBatal(String value) {
        tglBatal.set(value);
    }

    public StringProperty tglBatalProperty() {
        return tglBatal;
    }

    public String getSalesLunas() {
        return salesLunas.get();
    }

    public void setSalesLunas(String value) {
        salesLunas.set(value);
    }

    public StringProperty salesLunasProperty() {
        return salesLunas;
    }

    public String getTglLunas() {
        return tglLunas.get();
    }

    public void setTglLunas(String value) {
        tglLunas.set(value);
    }

    public StringProperty tglLunasProperty() {
        return tglLunas;
    }


    public double getBungaKomp() {
        return bungaKomp.get();
    }

    public void setBungaKomp(double value) {
        bungaKomp.set(value);
    }

    public DoubleProperty bungaKompProperty() {
        return bungaKomp;
    }

    public double getBungaRp() {
        return bungaRp.get();
    }

    public void setBungaRp(double value) {
        bungaRp.set(value);
    }

    public DoubleProperty bungaRpProperty() {
        return bungaRp;
    }

    public double getBungaPersen() {
        return bungaPersen.get();
    }

    public void setBungaPersen(double value) {
        bungaPersen.set(value);
    }

    public DoubleProperty bungaPersenProperty() {
        return bungaPersen;
    }

    public int getLamaPinjam() {
        return lamaPinjam.get();
    }

    public void setLamaPinjam(int value) {
        lamaPinjam.set(value);
    }

    public IntegerProperty lamaPinjamProperty() {
        return lamaPinjam;
    }

    public double getTotalPinjaman() {
        return totalPinjaman.get();
    }

    public void setTotalPinjaman(double value) {
        totalPinjaman.set(value);
    }

    public DoubleProperty totalPinjamanProperty() {
        return totalPinjaman;
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

    public String getKeterangan() {
        return keterangan.get();
    }

    public void setKeterangan(String value) {
        keterangan.set(value);
    }

    public StringProperty keteranganProperty() {
        return keterangan;
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


    public String getKodeSales() {
        return kodeSales.get();
    }

    public void setKodeSales(String value) {
        kodeSales.set(value);
    }

    public StringProperty kodeSalesProperty() {
        return kodeSales;
    }

    public String getTglGadai() {
        return tglGadai.get();
    }

    public void setTglGadai(String value) {
        tglGadai.set(value);
    }

    public StringProperty tglGadaiProperty() {
        return tglGadai;
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
