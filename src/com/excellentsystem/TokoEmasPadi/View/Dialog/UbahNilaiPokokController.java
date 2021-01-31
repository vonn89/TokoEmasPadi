/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Dialog;

import com.excellentsystem.TokoEmasPadi.Function;
import static com.excellentsystem.TokoEmasPadi.Function.getTableCell;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import com.excellentsystem.TokoEmasPadi.Model.Barang;
import java.util.List;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Yunaz
 */
public class UbahNilaiPokokController  {

    @FXML private TableView<Barang> barangTable;
    @FXML private TableColumn<Barang, String> kodeBarcodeColumn;
    @FXML private TableColumn<Barang, String> namaBarangColumn;
    @FXML private TableColumn<Barang, String> kodeJenisColumn;
    @FXML private TableColumn<Barang, String> merkColumn;
    @FXML private TableColumn<Barang, String> keteranganColumn;
    @FXML private TableColumn<Barang, Number> beratColumn;
    @FXML private TableColumn<Barang, Number> beratPembulatanColumn;
    @FXML private TableColumn<Barang, Number> nilaiPokokColumn;
    @FXML private TableColumn<Barang, Number> nilaiPokokPerGramColumn;
    
    @FXML public Label totalBeratLabel;
    @FXML public Label totalBeratPembulatanLabel;
    @FXML public TextField nilaiPokokPerGramField;
    @FXML public Button saveButton;
    private Main mainApp;   
    private Stage stage;
    public ObservableList<Barang> allBarang = FXCollections.observableArrayList();
    public void initialize() {
        kodeBarcodeColumn.setCellValueFactory(cellData -> cellData.getValue().kodeBarcodeProperty());
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().namaBarangProperty());
        kodeJenisColumn.setCellValueFactory(cellData -> cellData.getValue().kodeJenisProperty());
        merkColumn.setCellValueFactory(cellData -> cellData.getValue().merkProperty());
        keteranganColumn.setCellValueFactory(cellData -> cellData.getValue().keteranganProperty());
        beratColumn.setCellValueFactory(cellData -> cellData.getValue().beratProperty());
        beratColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        beratPembulatanColumn.setCellValueFactory(cellData ->cellData.getValue().beratPembulatanProperty());
        beratPembulatanColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        nilaiPokokColumn.setCellValueFactory(cellData ->cellData.getValue().nilaiPokokProperty());
        nilaiPokokColumn.setCellFactory(col -> getTableCell(rp, ""));
        nilaiPokokPerGramColumn.setCellValueFactory(cellData ->{
            return new SimpleDoubleProperty(cellData.getValue().getNilaiPokok()/cellData.getValue().getBerat());
        });
        nilaiPokokPerGramColumn.setCellFactory(col -> getTableCell(rp, ""));
        
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            barangTable.refresh();
        });
        rowMenu.getItems().addAll(refresh);
        barangTable.setContextMenu(rowMenu);
        barangTable.setRowFactory(ttv -> {
            TableRow<Barang> row = new TableRow<Barang>() {
                @Override
                public void updateItem(Barang item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rowMenu);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem delete = new MenuItem("Hapus Barang");
                        delete.setOnAction((ActionEvent event) -> {
                            allBarang.remove(item);
                            hitungTotal();
                            barangTable.refresh();
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            barangTable.refresh();
                        });
                        rowMenu.getItems().addAll(delete);
                        rowMenu.getItems().addAll(refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            return row;
        });
        Function.setNumberField(nilaiPokokPerGramField, rp);
    }    
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        barangTable.setItems(allBarang);
    }
    public void setBarang(List<Barang> x){
        allBarang.addAll(x);
        hitungTotal();
    }
    
    private void hitungTotal(){
        double berat = 0;
        double beratPembulatan = 0;
        for(Barang d : allBarang){
            berat = berat + d.getBerat()*d.getQty();
            beratPembulatan = beratPembulatan + d.getBeratPembulatan()*d.getQty();
        }
        totalBeratLabel.setText(gr.format(berat)+" gr");
        totalBeratPembulatanLabel.setText(gr.format(beratPembulatan)+" gr");
    }
    @FXML
    private void close(){
        stage.close();
    }
}
