/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Dialog;

import com.excellentsystem.TokoEmasPadi.DAO.KategoriTransaksiDAO;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import com.excellentsystem.TokoEmasPadi.Model.KategoriTransaksi;
import java.sql.Connection;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Yunaz
 */
public class DataKategoriTransaksiController  {

    @FXML private TableView<KategoriTransaksi> kategoriTable;
    @FXML private TableColumn<KategoriTransaksi, String> kodeKategoriTransaksiColumn;
    @FXML private TableColumn<KategoriTransaksi, String> jenisKategoriTransaksiColumn;
    
    @FXML private Button cancelButton;
    @FXML private TextField kodeKategoriTransaksiField;
    @FXML private ComboBox<String> jenisKategoriTransaksiCombo;
    private String status = "";
    private ObservableList<KategoriTransaksi> allKategoriTransaksi = FXCollections.observableArrayList();
    private Main mainApp;
    private Stage stage;
    public void initialize() {
        kodeKategoriTransaksiColumn.setCellValueFactory(cellData -> cellData.getValue().kodeKategoriProperty());
        jenisKategoriTransaksiColumn.setCellValueFactory(cellData -> {
            if(cellData.getValue().getJenisTransaksi().equals("D"))
                return new SimpleStringProperty("Debit");
            else
                return new SimpleStringProperty("Kredit");
        });
        kodeKategoriTransaksiField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                jenisKategoriTransaksiCombo.requestFocus();
            }
        });
        final ContextMenu menu = new ContextMenu();
        MenuItem addNew = new MenuItem("Tambah Kategori Transaksi Baru");
        addNew.setOnAction((ActionEvent event) -> {
            newKategoriTransaksi();
        });
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            getKategoriTransaksi();
        });
        menu.getItems().addAll(addNew,refresh);
        kategoriTable.setContextMenu(menu);
        kategoriTable.setRowFactory(table -> {
            TableRow<KategoriTransaksi> row = new TableRow<KategoriTransaksi>(){
                @Override
                public void updateItem(KategoriTransaksi item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(menu);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem addNew = new MenuItem("Tambah Kategori Transaksi Baru");
                        addNew.setOnAction((ActionEvent event) -> {
                            newKategoriTransaksi();
                        });
                        MenuItem hapus = new MenuItem("Hapus Kategori Transaksi");
                        hapus.setOnAction((ActionEvent event) -> {
                            deleteKategoriTransaksi(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            getKategoriTransaksi();
                        });
                        rowMenu.getItems().addAll(addNew,hapus,refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            return row;
        });
        kategoriTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> selectKategoriTransaksi(newValue));
        kategoriTable.setItems(allKategoriTransaksi);
    }    
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        getKategoriTransaksi();
        ObservableList<String> allJenis = FXCollections.observableArrayList();
        allJenis.add("Debit");
        allJenis.add("Kredit");
        jenisKategoriTransaksiCombo.setItems(allJenis);
    }
    private void getKategoriTransaksi(){
        try(Connection con = Koneksi.getConnection()){
            allKategoriTransaksi.clear();
            allKategoriTransaksi.addAll(KategoriTransaksiDAO.getAll(con));
            kategoriTable.refresh();
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    @FXML
    private void close(){
        stage.close();
    }
    private void newKategoriTransaksi(){
        kodeKategoriTransaksiField.setDisable(false);
        kodeKategoriTransaksiField.setText("");
        jenisKategoriTransaksiCombo.getSelectionModel().select("");
        kodeKategoriTransaksiField.requestFocus();
        status = "New";
    }
    @FXML
    private void cancel(){
        kodeKategoriTransaksiField.setDisable(true);
        kodeKategoriTransaksiField.setText("");
        jenisKategoriTransaksiCombo.getSelectionModel().select("");
        status = "";
    }
    private void deleteKategoriTransaksi(KategoriTransaksi temp){
        try(Connection con = Koneksi.getConnection()){
            KategoriTransaksiDAO.delete(con, temp.getKodeKategori());
            getKategoriTransaksi();
            mainApp.showMessage(Modality.NONE, "Success", "Kategori Transaksi berhasil dihapus");
            cancelButton.fire();
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    private void selectKategoriTransaksi(KategoriTransaksi temp){
        if(temp!=null){
            kodeKategoriTransaksiField.setDisable(true);
            kodeKategoriTransaksiField.setText(temp.getKodeKategori());
            jenisKategoriTransaksiCombo.getSelectionModel().select("Debit");
            if(temp.getJenisTransaksi().equals("K"))
                jenisKategoriTransaksiCombo.getSelectionModel().select("Kredit");
            jenisKategoriTransaksiCombo.requestFocus();
            status = "Update";
        }
    }
    @FXML
    private void saveKategoriTransaksi(){
        try(Connection con = Koneksi.getConnection()){
            if(kodeKategoriTransaksiField.getText().equals(""))
                mainApp.showMessage(Modality.NONE, "Warning", "Kode kategori masih kosong");
            else if(jenisKategoriTransaksiCombo.getSelectionModel().getSelectedItem()==null)
                mainApp.showMessage(Modality.NONE, "Warning", "Jenis Transaksi belum dipilih");
            else{
                if(status.equals("New")){
                    Boolean s = true;
                    for(KategoriTransaksi k : allKategoriTransaksi){
                        if(k.getKodeKategori().toUpperCase().equals(kodeKategoriTransaksiField.getText().toUpperCase()))
                            s = false;
                    }
                    if(s){
                        KategoriTransaksi k = new KategoriTransaksi();
                        k.setKodeKategori(kodeKategoriTransaksiField.getText().toUpperCase());
                        k.setJenisTransaksi("D");
                        if(jenisKategoriTransaksiCombo.getSelectionModel().getSelectedItem().equals("Kredit"))
                            k.setJenisTransaksi("K");
                        KategoriTransaksiDAO.insert(con, k);
                        getKategoriTransaksi();
                        mainApp.showMessage(Modality.NONE, "Success", "Kategori Transaksi berhasil disimpan");
                        cancelButton.fire();
                    }else
                        mainApp.showMessage(Modality.NONE, "Warning", "Kode Kategori Transaksi sudah terdaftar");
                }else if(status.equals("Update")){
                    KategoriTransaksi kategori = null;
                    for(KategoriTransaksi k : allKategoriTransaksi){
                        if(k.getKodeKategori().toUpperCase().equals(kodeKategoriTransaksiField.getText().toUpperCase()))
                            kategori = k;
                    }
                    if(kategori==null)
                        mainApp.showMessage(Modality.NONE, "Warning", "Kategori Transaksi tidak ditemukan");
                    else{
                        kategori.setJenisTransaksi("D");
                        if(jenisKategoriTransaksiCombo.getSelectionModel().getSelectedItem().equals("Kredit"))
                            kategori.setJenisTransaksi("K");
                        KategoriTransaksiDAO.update(con, kategori);
                        getKategoriTransaksi();
                        mainApp.showMessage(Modality.NONE, "Success", "Kategori Transaksi berhasil disimpan");
                        cancelButton.fire();
                    }
                }
            }
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
}
