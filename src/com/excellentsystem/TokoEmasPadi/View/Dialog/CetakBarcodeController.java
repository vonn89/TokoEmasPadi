/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Dialog;

import static com.excellentsystem.TokoEmasPadi.Function.getTableCell;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import com.excellentsystem.TokoEmasPadi.Model.Barang;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Yunaz
 */
public class CetakBarcodeController  {

    
    @FXML private TableView<Barang> barangTable;
    @FXML private TableColumn<Barang, Number> jumlahColumn;
    @FXML private TableColumn<Barang, String> kodeBarcodeColumn;
    @FXML private TableColumn<Barang, String> namaBarangColumn;
    @FXML private TableColumn<Barang, String> namaBarcodeColumn;
    @FXML private TableColumn<Barang, String> kodeJenisColumn;
    @FXML private TableColumn<Barang, Number> beratColumn;
    @FXML private TableColumn<Barang, Number> beratPembulatanColumn;
    @FXML private TableColumn<Barang, String> merkColumn;
    @FXML private TableColumn<Barang, String> keteranganColumn;
    
    @FXML private Label totalQtyLabel;
    @FXML private Label totalBeratLabel;
    @FXML private Label totalBeratPembulatanLabel;
    @FXML public Button saveButton;
    private Main mainApp;   
    private Stage stage;
    public ObservableList<Barang> allBarang = FXCollections.observableArrayList();
    public void initialize() {
        jumlahColumn.setCellValueFactory(cellData -> cellData.getValue().qtyProperty());
        jumlahColumn.setCellFactory(col -> getTableCell(rp, ""));
        kodeBarcodeColumn.setCellValueFactory(cellData -> cellData.getValue().kodeBarcodeProperty());
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().namaBarangProperty());
        namaBarcodeColumn.setCellValueFactory(cellData -> cellData.getValue().namaBarcodeProperty());
        kodeJenisColumn.setCellValueFactory(cellData -> cellData.getValue().kodeJenisProperty());
        keteranganColumn.setCellValueFactory(cellData -> cellData.getValue().keteranganProperty());
        merkColumn.setCellValueFactory(cellData -> cellData.getValue().merkProperty());
        beratColumn.setCellValueFactory(cellData -> cellData.getValue().beratProperty());
        beratColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        beratPembulatanColumn.setCellValueFactory(cellData ->cellData.getValue().beratPembulatanProperty());
        beratPembulatanColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            barangTable.refresh();
        });
        rowMenu.getItems().addAll(refresh);
        barangTable.setContextMenu(rowMenu);
        barangTable.setRowFactory(table -> {
            TableRow<Barang> row = new TableRow<Barang>() {
                @Override
                public void updateItem(Barang item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rowMenu);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem ubahQty = new MenuItem("Ubah Jumlah Barang");
                        ubahQty.setOnAction((ActionEvent event) -> {
                            ubahQty(item);
                        });
                        MenuItem delete = new MenuItem("Hapus Barang");
                        delete.setOnAction((ActionEvent event) -> {
                            allBarang.remove(getItem());
                            hitungTotal();
                            barangTable.refresh();
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            barangTable.refresh();
                        });
                        rowMenu.getItems().addAll(ubahQty, delete, refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)&&
                        mouseEvent.getClickCount() == 2){
                    if(row.getItem()!=null)
                        ubahQty(row.getItem());
                }
            });
            return row;
        });
        barangTable.setItems(allBarang);
    }    
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
    }
    public void setBarang(List<Barang> x){
        allBarang.addAll(x);
        hitungTotal();
    }
    private void ubahQty(Barang b){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/UbahQty.fxml");
        UbahQtyController controller = loader.getController();
        controller.setMainApp(mainApp, child);
        controller.qtyField.setText(rp.format(b.getQty()));
        controller.qtyField.selectAll();
        controller.okButton.setOnAction((event) -> {
            if(Integer.parseInt(controller.qtyField.getText().replaceAll(",", ""))<=0){
                allBarang.remove(b);
                hitungTotal();
                barangTable.refresh();
            }else if(Integer.parseInt(controller.qtyField.getText().replaceAll(",", ""))>b.getQty()){
                mainApp.showMessage(Modality.NONE, "Warning", "Jumlah melebihi jumlah stok barang");
            }else{
                b.setQty(Integer.parseInt(controller.qtyField.getText().replaceAll(",", "")));
                hitungTotal();
                barangTable.refresh();
                child.close();
            }
        });
    }
    private void hitungTotal(){
        double beratPembulatan = 0;
        double berat = 0;
        int qty = 0;
        for(Barang b : allBarang){
            beratPembulatan = beratPembulatan + b.getBeratPembulatan() *b.getQty();
            berat = berat + b.getBerat() * b.getQty();
            qty = qty + b.getQty();
        }
        totalQtyLabel.setText(rp.format(qty));
        totalBeratLabel.setText(gr.format(berat)+" gr");
        totalBeratPembulatanLabel.setText(gr.format(beratPembulatan)+" gr");
    }
    @FXML
    private void close(){
        stage.close();
    }  
    
}
