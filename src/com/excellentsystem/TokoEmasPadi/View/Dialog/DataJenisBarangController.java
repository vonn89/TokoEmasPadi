/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Dialog;

import com.excellentsystem.TokoEmasPadi.DAO.JenisDAO;
import com.excellentsystem.TokoEmasPadi.DAO.KategoriDAO;
import com.excellentsystem.TokoEmasPadi.DAO.StokBarangDAO;
import static com.excellentsystem.TokoEmasPadi.Function.getTableCell;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import com.excellentsystem.TokoEmasPadi.Model.Jenis;
import com.excellentsystem.TokoEmasPadi.Model.Kategori;
import com.excellentsystem.TokoEmasPadi.Model.StokBarang;
import com.excellentsystem.TokoEmasPadi.Service.Service;
import java.sql.Connection;
import java.util.List;
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
public class DataJenisBarangController  {

    @FXML private TableView<Kategori> kategoriTable;
    @FXML private TableColumn<Kategori, String> kodeKategoriColumn;
    @FXML private TableColumn<Kategori, Number> hargaBeliColumn;
    @FXML private TableColumn<Kategori, Number> hargaJualColumn;
    
    @FXML private TableView<Jenis> jenisTable;
    @FXML private TableColumn<Jenis, String> kodeJenisColumn;
    @FXML private TableColumn<Jenis, String> namaJenisColumn;
    
    @FXML private Button cancelButton;
    @FXML private TextField kodeJenisField;
    @FXML private TextField namaJenisField;
    @FXML private ComboBox<String> kategoriCombo;
    private String status = "";
    private ObservableList<Kategori> allKategori = FXCollections.observableArrayList();
    private ObservableList<Jenis> allJenis = FXCollections.observableArrayList();
    private ObservableList<String> temp = FXCollections.observableArrayList();
    private Main mainApp;
    private Stage stage;
    public void initialize() {
        kodeKategoriColumn.setCellValueFactory(cellData -> cellData.getValue().kodeKategoriProperty());
        hargaBeliColumn.setCellValueFactory(cellData -> cellData.getValue().hargaBeliProperty());
        hargaBeliColumn.setCellFactory(col -> getTableCell(rp, "Rp"));
        hargaJualColumn.setCellValueFactory(cellData -> cellData.getValue().hargaJualProperty());
        hargaJualColumn.setCellFactory(col -> getTableCell(rp, "Rp"));
        kodeJenisColumn.setCellValueFactory(cellData -> cellData.getValue().kodeJenisProperty());
        namaJenisColumn.setCellValueFactory(cellData -> cellData.getValue().namaJenisProperty());
        
        kodeJenisField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                namaJenisField.selectAll();
                namaJenisField.requestFocus();
            }
        });
        namaJenisField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                kategoriCombo.requestFocus();
            }
        });
        ContextMenu menu = new ContextMenu();
        MenuItem addNew = new MenuItem("Tambah Jenis Baru");
        addNew.setOnAction((ActionEvent event) -> {
            newJenis();
        });
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            getKategori();
        });
        menu.getItems().addAll(addNew,refresh);
        jenisTable.setContextMenu(menu);
        jenisTable.setRowFactory(table -> {
            TableRow<Jenis> row = new TableRow<Jenis>(){
                @Override
                public void updateItem(Jenis item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(menu);
                    }else{
                        MenuItem addNew = new MenuItem("Tambah Jenis Baru");
                        addNew.setOnAction((ActionEvent event) -> {
                            newJenis();
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            getKategori();
                        });
                        MenuItem hapus = new MenuItem("Hapus Jenis");
                        hapus.setOnAction((ActionEvent event) -> {
                            deleteJenis(item);
                        });
                        ContextMenu rowMenu = new ContextMenu();
                        rowMenu.getItems().addAll(addNew,hapus,refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            return row;
        });
        kategoriTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> selectKategori(newValue));
        jenisTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> selectJenis(newValue));
        kategoriTable.setItems(allKategori);
        kategoriCombo.setItems(temp);
        jenisTable.setItems(allJenis);
    }    
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        getKategori();
    }
    private void getKategori(){
        try(Connection con = Koneksi.getConnection()){
            allKategori.clear();
            temp.clear();
            selectKategori(null);
            allKategori.addAll(KategoriDAO.getAll(con));
            for(Kategori k : allKategori){
                temp.add(k.getKodeKategori());
            }
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    @FXML
    private void close(){
        stage.close();
    }
    private void newJenis(){
        kodeJenisField.setDisable(false);
        kategoriCombo.setDisable(false);
        kodeJenisField.setText("");
        namaJenisField.setText("");
        kategoriCombo.getSelectionModel().select("");
        kodeJenisField.requestFocus();
        status = "New";
    }
    @FXML
    private void cancel(){
        kodeJenisField.setDisable(true);
        kategoriCombo.setDisable(true);
        kodeJenisField.setText("");
        namaJenisField.setText("");
        kategoriCombo.getSelectionModel().select("");
        status = "";
    }
    private void deleteJenis(Jenis temp){
        try(Connection con = Koneksi.getConnection()){
            double stok = 0;
            List<StokBarang> listStokBarang = StokBarangDAO.getAllByDateAndJenisAndBarcode(con, 
                    sistem.getTglSystem(), sistem.getTglSystem(), temp.getKodeJenis(), "%");
            for(StokBarang s : listStokBarang){
                stok = stok + s.getStokAkhir();
            }
            if(stok>0){
                mainApp.showMessage(Modality.NONE, "Warning", "Jenis barang tidak dapat dihapus,"
                        + "\nkarena masih ada stok barang dengan jenis "+temp.getKodeJenis());
            }else{
                JenisDAO.delete(con, temp.getKodeJenis());
                selectKategori(null);
                mainApp.showMessage(Modality.NONE, "Success", "Jenis Barang berhasil dihapus");
                cancelButton.fire();
            }
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    private void selectKategori(Kategori temp){
        try(Connection con = Koneksi.getConnection()){
            allJenis.clear();
            if(temp!=null){
                List<Jenis> listJenis = JenisDAO.getAll(con);
                for(Jenis j : listJenis){
                    if(j.getKodeKategori().equals(temp.getKodeKategori()))
                        allJenis.add(j);
                }
            }
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    private void selectJenis(Jenis temp){
        if(temp!=null){
            kodeJenisField.setDisable(true);
            kategoriCombo.setDisable(true);
            kodeJenisField.setText(temp.getKodeJenis());
            namaJenisField.setText(temp.getNamaJenis());
            kategoriCombo.getSelectionModel().select(temp.getKodeKategori());
            namaJenisField.requestFocus();
            status = "Update";
        }
    }
    @FXML
    private void saveJenis(){
        if(kodeJenisField.getText().equals(""))
            mainApp.showMessage(Modality.NONE, "Warning", "Kode jenis masih kosong");
        else if(namaJenisField.getText().equals(""))
            mainApp.showMessage(Modality.NONE, "Warning", "Nama Jenis masih kosong");
        else if(kategoriCombo.getSelectionModel().getSelectedItem()==null||"".equals(kategoriCombo.getSelectionModel().getSelectedItem()))
            mainApp.showMessage(Modality.NONE, "Warning", "Kategori belum dipilih");
        else{
            try(Connection con = Koneksi.getConnection()){
                if(status.equals("New")){
                    Boolean s = true;
                    List<Jenis> listJenis = JenisDAO.getAll(con);
                    for(Jenis j : listJenis){
                        if(j.getKodeJenis().toUpperCase().equals(kodeJenisField.getText().toUpperCase()))
                            s = false;
                    }
                    if(s){
                        Jenis j = new Jenis();
                        j.setKodeJenis(kodeJenisField.getText().toUpperCase());
                        j.setNamaJenis(namaJenisField.getText());
                        j.setKodeKategori(kategoriCombo.getSelectionModel().getSelectedItem());
                        JenisDAO.insert(con, j);
                        for(Kategori k : allKategori){
                            if(k.getKodeKategori().equals(j.getKodeKategori()))
                                selectKategori(k);
                        }
                        mainApp.showMessage(Modality.NONE, "Success", "Jenis Barang berhasil disimpan");
                        cancelButton.fire();
                    }else
                        mainApp.showMessage(Modality.NONE, "Warning", "Kode Jenis sudah terdaftar");
                }else if(status.equals("Update")){
                    Jenis jenis = null;
                    List<Jenis> listJenis = JenisDAO.getAll(con);
                    for(Jenis j : listJenis){
                        if(j.getKodeJenis().toUpperCase().equals(kodeJenisField.getText().toUpperCase()))
                            jenis = j;
                    }
                    if(jenis==null){
                        mainApp.showMessage(Modality.NONE, "Warning", "Jenis barang tidak ditemukan");
                    }else{
                        jenis.setNamaJenis(namaJenisField.getText());
                        jenis.setKodeKategori(kategoriCombo.getSelectionModel().getSelectedItem());
                        for(Kategori kategori : allKategori){
                            if(kategori.getKodeKategori().equals(kategoriCombo.getSelectionModel().getSelectedItem()))
                               jenis.setKategori(kategori);
                        }
                        String x = Service.saveUpdateJenis(con, jenis);
                        if(x.equals("true")){
                            selectKategori(jenis.getKategori());
                            mainApp.showMessage(Modality.NONE, "Success", "Jenis Barang berhasil disimpan");
                            cancelButton.fire();
                        }else{
                            mainApp.showMessage(Modality.NONE, "Error", x);
                        }
                    }
                }
            }catch(Exception e){
                mainApp.showMessage(Modality.NONE, "Error", e.toString());
            }
        }
    }
}
