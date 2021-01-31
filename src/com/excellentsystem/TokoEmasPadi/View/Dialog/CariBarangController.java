/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Dialog;

import com.excellentsystem.TokoEmasPadi.DAO.BarangDAO;
import com.excellentsystem.TokoEmasPadi.DAO.StokBarangDAO;
import static com.excellentsystem.TokoEmasPadi.Function.getTableCell;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import com.excellentsystem.TokoEmasPadi.Model.Barang;
import com.excellentsystem.TokoEmasPadi.Model.StokBarang;
import java.sql.Connection;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Yunaz
 */
public class CariBarangController  {

    @FXML public TableView<Barang> barangTable;
    @FXML private TableColumn<Barang, Number> qtyColumn;
    @FXML private TableColumn<Barang, String> kodeBarcodeColumn;
    @FXML private TableColumn<Barang, String> namaBarangColumn;
    @FXML private TableColumn<Barang, Number> beratColumn;
    @FXML private TableColumn<Barang, Number> beratPembulatanColumn;
    @FXML private TableColumn<Barang, String> keteranganColumn;
    @FXML private TableColumn<Barang, String> merkColumn;
    
    @FXML private TextField searchField;
    
    private Main mainApp;   
    private Stage stage;
    private final ObservableList<Barang> allBarang = FXCollections.observableArrayList();
    private ObservableList<Barang> filterData = FXCollections.observableArrayList();
    public void initialize() {
        kodeBarcodeColumn.setCellValueFactory(cellData -> cellData.getValue().kodeBarcodeProperty());
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().namaBarangProperty());
        beratColumn.setCellValueFactory(cellData ->cellData.getValue().beratProperty());
        beratColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        beratPembulatanColumn.setCellValueFactory(cellData ->cellData.getValue().beratPembulatanProperty());
        beratPembulatanColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        qtyColumn.setCellValueFactory(cellData -> cellData.getValue().getStokBarang().stokAkhirProperty());
        qtyColumn.setCellFactory(col -> getTableCell(rp, ""));
        keteranganColumn.setCellValueFactory(cellData -> cellData.getValue().keteranganProperty());
        merkColumn.setCellValueFactory(cellData -> cellData.getValue().merkProperty());
        
        allBarang.addListener((ListChangeListener.Change<? extends Barang> change) -> {
            searchStokBarang();
        });
        searchField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            searchStokBarang();
        });
        filterData.addAll(allBarang);
        barangTable.setItems(filterData);
    }    
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        getBarang();
    }
    public void getBarang(){
        try(Connection con = Koneksi.getConnection()){
            allBarang.clear();
            List<StokBarang> listStokBarang = StokBarangDAO.getAllByTanggal(con, Main.sistem.getTglSystem());
            for(StokBarang s : listStokBarang){
                Barang b = BarangDAO.get(con, s.getKodeBarcode());
                b.setStokBarang(s);
                allBarang.add(b);
            }
        }catch(Exception e){
            e.printStackTrace();
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    private Boolean checkColumn(String column){
        if(column!=null){
            if(column.toLowerCase().contains(searchField.getText().toLowerCase()))
                return true;
        }
        return false;
    }
    private void searchStokBarang() {
        try{
            filterData.clear();
            for (Barang temp : allBarang) {
                if (searchField.getText() == null || searchField.getText().equals(""))
                    filterData.add(temp);
                else{
                    if(checkColumn(temp.getKodeKategori())||
                        checkColumn(temp.getKodeJenis())||
                        checkColumn(temp.getKodeBarcode())||
                        checkColumn(temp.getNamaBarang())||
                        checkColumn(temp.getKeterangan())||
                        checkColumn(temp.getMerk())||
                        checkColumn(temp.getNamaBarcode())||
                        checkColumn(gr.format(temp.getBerat()))||
                        checkColumn(gr.format(temp.getBeratPembulatan())))
                        filterData.add(temp);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    @FXML
    private void close(){
        stage.close();
    }  
    
}
