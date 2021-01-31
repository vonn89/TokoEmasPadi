/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Dialog;

import com.excellentsystem.TokoEmasPadi.Function;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import com.excellentsystem.TokoEmasPadi.Model.Barang;
import com.excellentsystem.TokoEmasPadi.Model.PenjualanDetail;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Excellent
 */
public class AddBarangController  {

    @FXML private Label kodeBarcodeLabel;
    @FXML private Label namaBarangLabel;
    @FXML private TextField beratField;
    @FXML public TextField jumlahField;
    @FXML public TextField hargaField;
    @FXML public Button saveButton;
    
    private Barang barang;
    private Main mainApp;  
    private Stage stage;
    public void initialize() {
        Function.setNumberField(jumlahField, rp);
        Function.setNumberField(hargaField, rp);
    }    
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
    }
    public void setDetailPenjualan(PenjualanDetail d){
        barang = d.getBarang();
        kodeBarcodeLabel.setText(d.getKodeBarcode());
        namaBarangLabel.setText(d.getNamaBarang());
        beratField.setText(gr.format(d.getBeratPembulatan())+" Gr");
        jumlahField.setText(rp.format(d.getQty()));
        hargaField.setText(rp.format(d.getHargaJual()));
    }
    public void setBarang(Barang b){
        barang = b;
        kodeBarcodeLabel.setText(b.getKodeBarcode());
        namaBarangLabel.setText(b.getNamaBarang());
        beratField.setText(gr.format(b.getBeratPembulatan())+" Gr");
        jumlahField.setText("1");
        hargaField.setText(rp.format(b.getHargaJual()));
    }
    @FXML
    private void minus(){
        if(Integer.parseInt(jumlahField.getText())>1){
            jumlahField.setText(rp.format(Integer.parseInt(jumlahField.getText())-1));
        }
    }
    @FXML
    private void plus(){
        if(Integer.parseInt(jumlahField.getText())<barang.getQty()){
            jumlahField.setText(rp.format(Integer.parseInt(jumlahField.getText())+1));
        }
    }
    @FXML
    private void close(){
        stage.close();
    }   
}
