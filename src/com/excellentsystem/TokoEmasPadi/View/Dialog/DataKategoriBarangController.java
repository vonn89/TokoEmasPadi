/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Dialog;

import com.excellentsystem.TokoEmasPadi.DAO.JenisDAO;
import com.excellentsystem.TokoEmasPadi.DAO.KategoriDAO;
import com.excellentsystem.TokoEmasPadi.Function;
import static com.excellentsystem.TokoEmasPadi.Function.getTableCell;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import com.excellentsystem.TokoEmasPadi.Model.Jenis;
import com.excellentsystem.TokoEmasPadi.Model.Kategori;
import com.excellentsystem.TokoEmasPadi.Service.Service;
import java.sql.Connection;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
public class DataKategoriBarangController  {
    
    @FXML private TableView<Kategori> kategoriTable;
    @FXML private TableColumn<Kategori, String> kodeKategoriColumn;
    @FXML private TableColumn<Kategori, String> namaKategoriColumn;
    @FXML private TableColumn<Kategori, Number> hargaBeliColumn;
    @FXML private TableColumn<Kategori, Number> hargaJualColumn;
    
    @FXML private Button cancelButton;
    @FXML private TextField kodeKategoriField;
    @FXML private TextField namaKategoriField;
    @FXML private TextField hargaBeliField;
    @FXML private TextField hargaJualField;
    private String status = "";
    private ObservableList<Kategori> allKategori = FXCollections.observableArrayList();
    private Main mainApp;
    private Stage stage;
    public void initialize() {
        kodeKategoriColumn.setCellValueFactory(cellData -> cellData.getValue().kodeKategoriProperty());
        namaKategoriColumn.setCellValueFactory(cellData -> cellData.getValue().namaKategoriProperty());
        hargaBeliColumn.setCellValueFactory(cellData -> cellData.getValue().hargaBeliProperty());
        hargaBeliColumn.setCellFactory(col -> getTableCell(rp, "Rp"));
        hargaJualColumn.setCellValueFactory(cellData -> cellData.getValue().hargaJualProperty());
        hargaJualColumn.setCellFactory(col -> getTableCell(rp, "Rp"));
        Function.setNumberField(hargaBeliField, rp);
        Function.setNumberField(hargaJualField, rp);
        kodeKategoriField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                namaKategoriField.selectAll();
                namaKategoriField.requestFocus();
            }
        });
        namaKategoriField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                hargaBeliField.selectAll();
                hargaBeliField.requestFocus();
            }
        });
        hargaBeliField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                hargaJualField.selectAll();
                hargaJualField.requestFocus();
            }
        });
        ContextMenu menu = new ContextMenu();
        MenuItem addNew = new MenuItem("Tambah Kategori Baru");
        addNew.setOnAction((ActionEvent) -> {
            newKategori();
        });
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent) -> {
            getKategori();
        });
        menu.getItems().addAll(addNew,refresh);
        kategoriTable.setContextMenu(menu);
        kategoriTable.setRowFactory(t -> {
            TableRow<Kategori> row = new TableRow<Kategori>(){
                @Override
                public void updateItem(Kategori item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(menu);
                    }else{
                        MenuItem addNew = new MenuItem("Tambah Kategori Baru");
                        addNew.setOnAction((ActionEvent) -> {
                            newKategori();
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent) -> {
                            getKategori();
                        });
                        MenuItem hapus = new MenuItem("Hapus Kategori");
                        hapus.setOnAction((ActionEvent) -> {
                            deleteKategori(item);
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
        kategoriTable.setItems(allKategori);
    }    
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        getKategori();
    }
    private void getKategori(){
        try(Connection con = Koneksi.getConnection()){
            allKategori.clear();
            allKategori.addAll(KategoriDAO.getAll(con));
            kategoriTable.refresh();
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    @FXML
    private void close(){
        stage.close();
    }
    private void newKategori(){
        kodeKategoriField.setDisable(false);
        kodeKategoriField.setText("");
        namaKategoriField.setText("");
        hargaBeliField.setText("");
        hargaJualField.setText("");
        kodeKategoriField.requestFocus();
        status = "New";
    }
    @FXML
    private void cancel(){
        kodeKategoriField.setDisable(true);
        kodeKategoriField.setText("");
        namaKategoriField.setText("");
        hargaBeliField.setText("");
        hargaJualField.setText("");
        status = "";
    }
    private void deleteKategori(Kategori temp){
        try(Connection con = Koneksi.getConnection()){
            Boolean s = true;
            List<Jenis> jenis = JenisDAO.getAll(con);
            for(Jenis j : jenis){
                if(j.getKodeKategori().toUpperCase().equals(temp.getKodeKategori().toUpperCase()))
                    s = false;
            }
            if(s){
                KategoriDAO.delete(con, temp.getKodeKategori());
                getKategori();
                mainApp.showMessage(Modality.NONE, "Success", "Kategori Barang berhasil dihapus");
                cancelButton.fire();
            }else{
                mainApp.showMessage(Modality.NONE, "Warning", "Kategori barang tidak dapat dihapus,"
                        + "\nkarena masih ada jenis barang dengan kategori "+temp.getKodeKategori());
            }
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    private void selectKategori(Kategori temp){
        if(temp!=null){
            kodeKategoriField.setDisable(true);
            kodeKategoriField.setText(temp.getKodeKategori());
            namaKategoriField.setText(temp.getNamaKategori());
            hargaBeliField.setText(rp.format(temp.getHargaBeli()));
            hargaJualField.setText(rp.format(temp.getHargaJual()));
            namaKategoriField.requestFocus();
            status = "Update";
        }
    }
    @FXML
    private void saveKategori(){
        try(Connection con = Koneksi.getConnection()){
            if(kodeKategoriField.getText().equals(""))
                mainApp.showMessage(Modality.NONE, "Warning", "Kode kategori masih kosong");
            else if(namaKategoriField.getText().equals(""))
                mainApp.showMessage(Modality.NONE, "Warning", "Nama Kategori masih kosong");
            else if(hargaBeliField.getText().equals(""))
                mainApp.showMessage(Modality.NONE, "Warning", "Harga beli masih kosong");
            else if(hargaJualField.getText().equals(""))
                mainApp.showMessage(Modality.NONE, "Warning", "Harga jual masih kosong");
            else{
                if(status.equals("New")){
                    Boolean s = true;
                    for(Kategori k : allKategori){
                        if(k.getKodeKategori().toUpperCase().equals(kodeKategoriField.getText().toUpperCase()))
                            s = false;
                    }
                    if(s){
                        Kategori k = new Kategori();
                        k.setKodeKategori(kodeKategoriField.getText().toUpperCase());
                        k.setNamaKategori(namaKategoriField.getText());
                        k.setHargaBeli(Double.parseDouble(hargaBeliField.getText().replaceAll(",", "")));
                        k.setHargaJual(Double.parseDouble(hargaJualField.getText().replaceAll(",", "")));
                        KategoriDAO.insert(con, k);
                        getKategori();
                        mainApp.showMessage(Modality.NONE, "Success", "Kategori Barang berhasil disimpan");
                        cancelButton.fire();
                    }else
                        mainApp.showMessage(Modality.NONE, "Warning", "Kode Kategori sudah terdaftar");
                }else if(status.equals("Update")){
                    Kategori kategori = null;
                    for(Kategori k : allKategori){
                        if(k.getKodeKategori().toUpperCase().equals(kodeKategoriField.getText().toUpperCase()))
                            kategori = k;
                    }
                    if(kategori==null){
                        mainApp.showMessage(Modality.NONE, "Warning", "Kategori barang tidak ditemukan");
                    }else{
                        kategori.setNamaKategori(namaKategoriField.getText());
                        kategori.setHargaBeli(Double.parseDouble(hargaBeliField.getText().replaceAll(",", "")));
                        kategori.setHargaJual(Double.parseDouble(hargaJualField.getText().replaceAll(",", "")));
                        String x = Service.saveUpdateKategori(con, kategori);
                        if(x.equals("true")){
                            getKategori();
                            mainApp.showMessage(Modality.NONE, "Success", "Kategori Barang berhasil disimpan");
                            cancelButton.fire();
                        }else
                            mainApp.showMessage(Modality.NONE, "Error", x);
                    }
                }
            }
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
}
