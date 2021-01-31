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
 * @author Yunaz
 */
public class HancurHead {
    
    private final StringProperty noHancur = new SimpleStringProperty();
    private final StringProperty tglHancur = new SimpleStringProperty();
    private final IntegerProperty totalQty = new SimpleIntegerProperty();
    private final DoubleProperty totalBerat = new SimpleDoubleProperty();
    private final DoubleProperty totalBeratPembulatan = new SimpleDoubleProperty();
    private final StringProperty kodeUser = new SimpleStringProperty();
    private List<HancurDetail> listHancurDetail;

    public List<HancurDetail> getListHancurDetail() {
        return listHancurDetail;
    }

    public void setListHancurDetail(List<HancurDetail> listHancurDetail) {
        this.listHancurDetail = listHancurDetail;
    }
    
    public double getTotalBeratPembulatan() {
        return totalBeratPembulatan.get();
    }

    public void setTotalBeratPembulatan(double value) {
        totalBeratPembulatan.set(value);
    }

    public DoubleProperty totalBeratPembulatanProperty() {
        return totalBeratPembulatan;
    }
    
    public String getKodeUser() {
        return kodeUser.get();
    }

    public void setKodeUser(String value) {
        kodeUser.set(value);
    }

    public StringProperty kodeUserProperty() {
        return kodeUser;
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

    public String getTglHancur() {
        return tglHancur.get();
    }

    public void setTglHancur(String value) {
        tglHancur.set(value);
    }

    public StringProperty tglHancurProperty() {
        return tglHancur;
    }

    public String getNoHancur() {
        return noHancur.get();
    }

    public void setNoHancur(String value) {
        noHancur.set(value);
    }

    public StringProperty noHancurProperty() {
        return noHancur;
    }
}
