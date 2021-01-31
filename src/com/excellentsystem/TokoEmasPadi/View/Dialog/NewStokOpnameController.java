/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Dialog;

import com.excellentsystem.TokoEmasPadi.DAO.JenisDAO;
import com.excellentsystem.TokoEmasPadi.DAO.KategoriDAO;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import com.excellentsystem.TokoEmasPadi.Model.Jenis;
import com.excellentsystem.TokoEmasPadi.Model.Kategori;
import java.sql.Connection;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Yunaz
 */
public class NewStokOpnameController  {

    @FXML public ComboBox<String> kodeKategoriCombo;
    @FXML public ComboBox<String> kodeJenisCombo;
    public Button okButton;
    private Main mainApp;
    private Stage stage;
    private final ObservableList<String> allKategori = FXCollections.observableArrayList();
    private final ObservableList<String> allJenis = FXCollections.observableArrayList();
    private List<Jenis> jenis ;
    public void setMainApp(Main mainApp, Stage stage) {
        try(Connection con = Koneksi.getConnection()){
            this.mainApp = mainApp;
            this.stage = stage;
            allKategori.clear();
            allKategori.add("Semua");
            List<Kategori> kategori = KategoriDAO.getAll(con);
            for(Kategori k : kategori){
                allKategori.add(k.getKodeKategori());
            }
            kodeKategoriCombo.setItems(allKategori);
            kodeKategoriCombo.getSelectionModel().select("Semua");

            jenis = JenisDAO.getAll(con);
            getJenis();
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    @FXML
    private void getJenis(){
        if(jenis!=null){
            allJenis.clear();
            allJenis.add("Semua");
            for(Jenis j : jenis){
                if(j.getKodeKategori().equals(kodeKategoriCombo.getSelectionModel().getSelectedItem())||
                        kodeKategoriCombo.getSelectionModel().getSelectedItem().equals("Semua"))
                    allJenis.add(j.getKodeJenis());
            }
            kodeJenisCombo.setItems(allJenis);
            kodeJenisCombo.getSelectionModel().select("Semua");
        }
    }
    @FXML
    private void close(){
        stage.close();
    }
}
