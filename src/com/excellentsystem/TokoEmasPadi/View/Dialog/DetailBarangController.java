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
import static com.excellentsystem.TokoEmasPadi.Main.tglLengkap;
import static com.excellentsystem.TokoEmasPadi.Main.tglSql;
import com.excellentsystem.TokoEmasPadi.Model.Barang;
import java.text.ParseException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Yunaz
 */
public class DetailBarangController  {

    @FXML private GridPane gridPane;
    @FXML private TextField kodeBarcodeField;
    @FXML public TextField namaBarangField;
    @FXML public TextField namaBarcodeField;
    @FXML private TextField kodeKategoriField;
    @FXML private TextField kodeJenisField;
    
    @FXML public TextField merkField;
    @FXML public TextField persentaseField;
    @FXML public TextField keteranganField;
    @FXML private TextField beratField;
    @FXML private TextField beratPembulatanField;
    
    @FXML private TextField tglBarcodeField;
    @FXML private TextField userBarcodeField;
    
    @FXML public TextField nilaiPokokField;
    @FXML public TextField hargaJualField;
    
    @FXML public Button saveButton;
    @FXML private Button cancelButton;
    
    private Main mainApp;
    private Stage stage;
    public void initialize() {
        Function.setNumberField(persentaseField, gr);
        Function.setNumberField(nilaiPokokField, rp);
        Function.setNumberField(hargaJualField, rp);
    }    
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
    }
    public void setBarang(Barang b){
        try {
            kodeBarcodeField.setText(b.getKodeBarcode());
            namaBarangField.setText(b.getNamaBarang());
            namaBarcodeField.setText(b.getNamaBarcode());
            kodeKategoriField.setText(b.getKodeKategori());
            kodeJenisField.setText(b.getKodeJenis());
            merkField.setText(b.getMerk());
            keteranganField.setText(b.getKeterangan());
            persentaseField.setText(gr.format(b.getPersentase()));
            beratField.setText(gr.format(b.getBerat()));
            beratPembulatanField.setText(gr.format(b.getBeratPembulatan()));
            tglBarcodeField.setText(tglLengkap.format(tglSql.parse(b.getBarcodeDate())));
            userBarcodeField.setText(b.getBarcodeBy());
            nilaiPokokField.setText(rp.format(b.getNilaiPokok()));
            hargaJualField.setText(rp.format(b.getHargaJual()));
        } catch (ParseException ex) {
            mainApp.showMessage(Modality.NONE, "Error", ex.toString());
        }
    }
    public void setViewOnly(){
        namaBarangField.setDisable(true);
        namaBarcodeField.setDisable(true);
        merkField.setDisable(true);
        persentaseField.setDisable(true);
        keteranganField.setDisable(true);
        nilaiPokokField.setDisable(true);
        hargaJualField.setDisable(true);
        saveButton.setVisible(false);
        cancelButton.setVisible(false);
        gridPane.getRowConstraints().get(11).setMinHeight(0);
        gridPane.getRowConstraints().get(11).setPrefHeight(0);
        gridPane.getRowConstraints().get(11).setMaxHeight(0);
    }
    @FXML
    private void close(){
        stage.close();
    }
    
}
