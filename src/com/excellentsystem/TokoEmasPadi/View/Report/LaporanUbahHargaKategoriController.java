/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Report;

import com.excellentsystem.TokoEmasPadi.DAO.LogHargaDAO;
import com.excellentsystem.TokoEmasPadi.Function;
import static com.excellentsystem.TokoEmasPadi.Function.getTableCell;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import static com.excellentsystem.TokoEmasPadi.Main.tglBarang;
import static com.excellentsystem.TokoEmasPadi.Main.tglNormal;
import com.excellentsystem.TokoEmasPadi.Model.HancurDetail;
import com.excellentsystem.TokoEmasPadi.Model.LogHarga;
import java.sql.Connection;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

/**
 * FXML Controller class
 *
 * @author Yunaz
 */
public class LaporanUbahHargaKategoriController  {

    @FXML private TableView<LogHarga> logHargaHeadTable;
    @FXML private TableColumn<LogHarga, String> tanggalColumn;
    @FXML private TableColumn<LogHarga, String> kodeKategoriColumn;
    @FXML private TableColumn<LogHarga, Number> hargaBeliColumn;
    @FXML private TableColumn<LogHarga, Number> hargaJualColumn;
    @FXML private DatePicker tglStartPicker;
    @FXML private DatePicker tglAkhirPicker;
    @FXML private TextField searchField;
    private Main mainApp;   
    private ObservableList<LogHarga> allLogHarga = FXCollections.observableArrayList();
    private ObservableList<LogHarga> filterData = FXCollections.observableArrayList();
    public void initialize() {
        tanggalColumn.setCellValueFactory(cellData -> { 
            try {
                return new SimpleStringProperty(tglNormal.format(
                        tglBarang.parse(cellData.getValue().getTanggal())));
            } catch (ParseException ex) {
                return null;
            }
        });
        kodeKategoriColumn.setCellValueFactory(cellData -> cellData.getValue().kodeKategoriProperty());
        hargaBeliColumn.setCellValueFactory(cellData -> cellData.getValue().hargaBeliProperty());
        hargaBeliColumn.setCellFactory(col -> getTableCell(rp, "Rp"));
        hargaJualColumn.setCellValueFactory(cellData -> cellData.getValue().hargaJualProperty());
        hargaJualColumn.setCellFactory(col -> getTableCell(rp, "Rp"));
        
        tglStartPicker.setConverter(Function.getTglConverter());
        tglStartPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglStartPicker.setDayCellFactory((final DatePicker datePicker) -> 
                Function.getDateCellMulai(tglAkhirPicker, LocalDate.parse(sistem.getTglSystem())));
        tglAkhirPicker.setConverter(Function.getTglConverter());
        tglAkhirPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglAkhirPicker.setDayCellFactory((final DatePicker datePicker) -> 
                Function.getDateCellAkhir(tglStartPicker, LocalDate.parse(sistem.getTglSystem())));
        
        
        allLogHarga.addListener((ListChangeListener.Change<? extends LogHarga> change) -> {
            searchLogHarga();
        });
        searchField.textProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            searchLogHarga();
        });
        filterData.addAll(allLogHarga);
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            getLogHarga();
        });
        rowMenu.getItems().addAll(refresh);
        logHargaHeadTable.setContextMenu(rowMenu);
        logHargaHeadTable.setRowFactory(table -> {
            TableRow<LogHarga> row = new TableRow<LogHarga>() {
                @Override
                public void updateItem(LogHarga item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rowMenu);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            getLogHarga();
                        });
                        rowMenu.getItems().addAll(refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            return row;
        });
    } 
    public void setMainApp(Main mainApp) {
        try{
            this.mainApp = mainApp;
            logHargaHeadTable.setItems(filterData);
            getLogHarga();
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }   
    @FXML
    private void getLogHarga(){
        try(Connection con = Koneksi.getConnection()){
            allLogHarga.clear();
            List<LogHarga> x = LogHargaDAO.getAllByDate(con, 
                    tglStartPicker.getValue().toString(), tglAkhirPicker.getValue().toString());
            allLogHarga.addAll(x);
        }catch(Exception e){
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
    private void searchLogHarga() {
        try{
            filterData.clear();
            for (LogHarga temp : allLogHarga) {
                if (searchField.getText() == null || searchField.getText().equals(""))
                    filterData.add(temp);
                else{
                    if(checkColumn(tglNormal.format(tglBarang.parse(temp.getTanggal())))||
                        checkColumn(temp.getKodeKategori())||
                        checkColumn(gr.format(temp.getHargaBeli()))||
                        checkColumn(gr.format(temp.getHargaJual())))
                            filterData.add(temp);
                }
            }
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }   
    
}
