/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Dialog;

import com.excellentsystem.TokoEmasPadi.DAO.KategoriDAO;
import com.excellentsystem.TokoEmasPadi.Function;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import com.excellentsystem.TokoEmasPadi.Model.GadaiDetail;
import com.excellentsystem.TokoEmasPadi.Model.Kategori;
import com.excellentsystem.TokoEmasPadi.Model.PembelianDetail;
import java.sql.Connection;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Excellent
 */
public class AddBarangGadaiController  {

    @FXML public ComboBox<String> kodeKategoriCombo;
    @FXML public TextField namaBarangField;
    @FXML public TextField jumlahField;
    @FXML public TextField beratField;
    @FXML public ComboBox<String> statusSuratCombo;
    @FXML public ComboBox<String> kondisiCombo;
    @FXML public TextField nilaiJualField;
    @FXML public Button saveButton;
    
    private List<Kategori> allKategori;
    private ObservableList<String> listKategori = FXCollections.observableArrayList();
    private ObservableList<String> listStatusSurat = FXCollections.observableArrayList();
    private ObservableList<String> listKondisi = FXCollections.observableArrayList();
    
    private Main mainApp;  
    private Stage stage;
    public void initialize() {
        Function.setNumberField(jumlahField, rp);
        beratField.setOnKeyReleased((event) -> {
            try{
                String string = beratField.getText();
                if(string.contains("-"))
                    beratField.undo();
                else{
                    if(string.indexOf(".")>0){
                        String string2 = string.substring(string.indexOf(".")+1, string.length());
                        if(string2.contains("."))
                            beratField.undo();
                        else if(!string2.equals("") && Double.parseDouble(string2)!=0)
                            beratField.setText(gr.format(Double.parseDouble(string.replaceAll(",", ""))));
                    }else
                        beratField.setText(gr.format(Double.parseDouble(string.replaceAll(",", ""))));
                }
                beratField.end();
            }catch(Exception e){
                beratField.undo();
            }
            getNilaiJual();
        });
        kodeKategoriCombo.setItems(listKategori);
        statusSuratCombo.setItems(listStatusSurat);
        kondisiCombo.setItems(listKondisi);
    }    
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        setComboBox();
    }
    private void setComboBox(){
        try(Connection con = Koneksi.getConnection()){
            allKategori = KategoriDAO.getAll(con);
            listKategori.clear();
            for(Kategori k : allKategori){
                listKategori.add(k.getKodeKategori());
            }
            kodeKategoriCombo.getSelectionModel().clearSelection();
            listStatusSurat.clear();
            listStatusSurat.add("Ada");
            listStatusSurat.add("Tidak ada");
            statusSuratCombo.getSelectionModel().selectFirst();
            listKondisi.clear();
            listKondisi.add("Baik");
            listKondisi.add("Rusak");
            kondisiCombo.getSelectionModel().selectFirst();
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    public void setDetailGadai(GadaiDetail d){
        kodeKategoriCombo.getSelectionModel().select(d.getKodeKategori());
        namaBarangField.setText(d.getNamaBarang());
        jumlahField.setText(rp.format(d.getQty()));
        beratField.setText(gr.format(d.getBerat()));
        nilaiJualField.setText(rp.format(d.getNilaiJual()));
        statusSuratCombo.getSelectionModel().select(d.getStatusSurat());
        kondisiCombo.getSelectionModel().select(d.getKondisi());
    }
    @FXML
    public void getNilaiJual(){
        if(kodeKategoriCombo.getSelectionModel().getSelectedItem()!=null){
            for(Kategori k : allKategori){
                if(kodeKategoriCombo.getSelectionModel().getSelectedItem().equals(k.getKodeKategori())){
                    nilaiJualField.setText(rp.format(Double.parseDouble(beratField.getText().replaceAll(",", ""))*k.getHargaJual()));
                }
            }
        }
    }
    @FXML
    private void close(){
        stage.close();
    }  
}
