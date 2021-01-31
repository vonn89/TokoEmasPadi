/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 *
 * @author Xtreme
 */
public class BungaGadai {
    private final DoubleProperty min = new SimpleDoubleProperty();
    private final DoubleProperty max = new SimpleDoubleProperty();
    private final DoubleProperty bunga = new SimpleDoubleProperty();

    public double getBunga() {
        return bunga.get();
    }

    public void setBunga(double value) {
        bunga.set(value);
    }

    public DoubleProperty bungaProperty() {
        return bunga;
    }

    public double getMax() {
        return max.get();
    }

    public void setMax(double value) {
        max.set(value);
    }

    public DoubleProperty maxProperty() {
        return max;
    }

    public double getMin() {
        return min.get();
    }

    public void setMin(double value) {
        min.set(value);
    }

    public DoubleProperty minProperty() {
        return min;
    }
    
}
