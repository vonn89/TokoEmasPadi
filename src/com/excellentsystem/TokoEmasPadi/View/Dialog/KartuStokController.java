/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Dialog;

import com.excellentsystem.TokoEmasPadi.DAO.StokBarangDAO;
import com.excellentsystem.TokoEmasPadi.Function;
import static com.excellentsystem.TokoEmasPadi.Function.getTableCell;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import static com.excellentsystem.TokoEmasPadi.Main.tglBarang;
import static com.excellentsystem.TokoEmasPadi.Main.tglNormal;
import com.excellentsystem.TokoEmasPadi.Model.StokBarang;
import java.sql.Connection;
import java.time.LocalDate;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Yunaz
 */
public class KartuStokController  {

    @FXML private TableView<StokBarang> barangTable;
    @FXML private TableColumn<StokBarang, String> tanggalColumn;
    @FXML private TableColumn<StokBarang, Number> stokAwalColumn;
    @FXML private TableColumn<StokBarang, Number> beratAwalColumn;
    @FXML private TableColumn<StokBarang, Number> stokMasukColumn;
    @FXML private TableColumn<StokBarang, Number> beratMasukColumn;
    @FXML private TableColumn<StokBarang, Number> stokKeluarColumn;
    @FXML private TableColumn<StokBarang, Number> beratKeluarColumn;
    @FXML private TableColumn<StokBarang, Number> stokAkhirColumn;
    @FXML private TableColumn<StokBarang, Number> beratAkhirColumn;
    @FXML private Label kodeJenisLabel;
    @FXML private Label jenisLabel;
    @FXML private DatePicker tglAwalPicker;
    @FXML private DatePicker tglAkhirPicker;
    private Main mainApp;   
    private Stage stage;
    private final ObservableList<StokBarang> allBarang = FXCollections.observableArrayList();
    public void initialize() {
        tanggalColumn.setCellValueFactory(cellData -> { 
            try {
                return new SimpleStringProperty(tglNormal.format(tglBarang.parse(cellData.getValue().getTanggal())));
            } catch (Exception ex) {
                return null;
            }
        });
        tanggalColumn.setComparator(Function.sortDate(tglNormal));
        stokAwalColumn.setCellValueFactory(cellData -> cellData.getValue().stokAwalProperty());
        stokAwalColumn.setCellFactory(col -> getTableCell(rp, ""));
        beratAwalColumn.setCellValueFactory(cellData -> cellData.getValue().beratAwalProperty());
        beratAwalColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        stokMasukColumn.setCellValueFactory(cellData -> cellData.getValue().stokMasukProperty());
        stokMasukColumn.setCellFactory(col -> getTableCell(rp, ""));
        beratMasukColumn.setCellValueFactory(cellData -> cellData.getValue().beratMasukProperty());
        beratMasukColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        stokKeluarColumn.setCellValueFactory(cellData -> cellData.getValue().stokKeluarProperty());
        stokKeluarColumn.setCellFactory(col -> getTableCell(rp, ""));
        beratKeluarColumn.setCellValueFactory(cellData -> cellData.getValue().beratKeluarProperty());
        beratKeluarColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        stokAkhirColumn.setCellValueFactory(cellData -> cellData.getValue().stokAkhirProperty());
        stokAkhirColumn.setCellFactory(col -> getTableCell(rp, ""));
        beratAkhirColumn.setCellValueFactory(cellData -> cellData.getValue().beratAkhirProperty());
        beratAkhirColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        
        tglAwalPicker.setConverter(Function.getTglConverter());
        tglAwalPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglAwalPicker.setDayCellFactory((final DatePicker datePicker) -> 
                Function.getDateCellMulai(tglAkhirPicker, LocalDate.parse(sistem.getTglSystem())));
        tglAkhirPicker.setConverter(Function.getTglConverter());
        tglAkhirPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglAkhirPicker.setDayCellFactory((final DatePicker datePicker) -> 
                Function.getDateCellAkhir(tglAwalPicker, LocalDate.parse(sistem.getTglSystem())));
        
    }    
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        barangTable.setItems(allBarang);
    }
    public void setBarang(String jenisataubarcode, String kode){
        kodeJenisLabel.setText(jenisataubarcode);
        jenisLabel.setText(kode);
        getStokBarang();
    }
    @FXML
    private void getStokBarang(){
        try(Connection con = Koneksi.getConnection()){
            allBarang.clear();
            if(kodeJenisLabel.getText().equals("Kode Barcode")){
                allBarang.addAll(StokBarangDAO.getAllByDateAndJenisAndBarcode(con, 
                        tglAwalPicker.getValue().toString(), tglAkhirPicker.getValue().toString(),"%", jenisLabel.getText()));
            }else if(kodeJenisLabel.getText().equals("Kode Jenis")){
                allBarang.addAll(StokBarangDAO.getAllByDateAndJenisAndBarcode(con, 
                        tglAwalPicker.getValue().toString(), tglAkhirPicker.getValue().toString(), jenisLabel.getText(), "%"));
            }
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    @FXML
    private void close(){
        stage.close();
    }
}
