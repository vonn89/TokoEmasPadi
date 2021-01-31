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
import com.excellentsystem.TokoEmasPadi.Model.HancurDetail;
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
public class HancurBarangController  {

    @FXML private TableView<HancurDetail> barangTable;
    @FXML private TableColumn<HancurDetail, String> kodeBarcodeColumn;
    @FXML private TableColumn<HancurDetail, String> namaBarangColumn;
    @FXML private TableColumn<HancurDetail, String> kodeJenisColumn;
    @FXML private TableColumn<HancurDetail, String> merkColumn;
    @FXML private TableColumn<HancurDetail, String> keteranganColumn;
    @FXML private TableColumn<HancurDetail, Number> beratColumn;
    @FXML private TableColumn<HancurDetail, Number> beratPembulatanColumn;
    @FXML private TableColumn<HancurDetail, Number> qtyColumn;
    
    @FXML public Label totalQtyLabel;
    @FXML public Label totalBeratLabel;
    @FXML public Label totalBeratPembulatanLabel;
    @FXML public Button saveButton;
    private Main mainApp;   
    private Stage stage;
    public ObservableList<HancurDetail> allBarang = FXCollections.observableArrayList();
    public void initialize() {
        kodeBarcodeColumn.setCellValueFactory(cellData -> cellData.getValue().kodeBarcodeProperty());
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().namaBarangProperty());
        kodeJenisColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().kodeJenisProperty());
        merkColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().merkProperty());
        keteranganColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().keteranganProperty());
        beratColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().beratProperty());
        beratColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        beratPembulatanColumn.setCellValueFactory(cellData ->cellData.getValue().getBarang().beratPembulatanProperty());
        beratPembulatanColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        qtyColumn.setCellValueFactory(cellData ->cellData.getValue().qtyProperty());
        qtyColumn.setCellFactory(col -> getTableCell(rp, ""));
        
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            barangTable.refresh();
        });
        rowMenu.getItems().addAll(refresh);
        barangTable.setContextMenu(rowMenu);
        barangTable.setRowFactory(ttv -> {
            TableRow<HancurDetail> row = new TableRow<HancurDetail>() {
                @Override
                public void updateItem(HancurDetail item, boolean empty) {
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
                            allBarang.remove(item);
                            hitungTotal();
                            barangTable.refresh();
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            barangTable.refresh();
                        });
                        rowMenu.getItems().addAll(ubahQty);
                        rowMenu.getItems().addAll(delete);
                        rowMenu.getItems().addAll(refresh);
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
    }    
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        barangTable.setItems(allBarang);
    }
    public void setBarang(List<HancurDetail> x){
        allBarang.addAll(x);
        hitungTotal();
    }
    private void ubahQty(HancurDetail d){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/UbahQty.fxml");
        UbahQtyController controller = loader.getController();
        controller.setMainApp(mainApp, child);
        controller.qtyField.setText(rp.format(d.getQty()));
        controller.qtyField.selectAll();
        controller.okButton.setOnAction((event) -> {
            if(Integer.parseInt(controller.qtyField.getText().replaceAll(",", ""))<=0){
                allBarang.remove(d);
                hitungTotal();
                barangTable.refresh();
            }else if(Integer.parseInt(controller.qtyField.getText().replaceAll(",", ""))>d.getQty()){
                mainApp.showMessage(Modality.NONE, "Warning", "Jumlah melebihi jumlah stok barang");
            }else{
                d.setQty(Integer.parseInt(controller.qtyField.getText().replaceAll(",", "")));
                hitungTotal();
                barangTable.refresh();
                child.close();
            }
        });
    }
    private void hitungTotal(){
        int qty = 0;
        double berat = 0;
        double beratPembulatan = 0;
        for(HancurDetail d : allBarang){
            qty = qty + d.getQty();
            berat = berat + d.getBarang().getBerat()*d.getQty();
            beratPembulatan = beratPembulatan + d.getBarang().getBeratPembulatan()*d.getQty();
        }
        totalQtyLabel.setText(gr.format(qty));
        totalBeratLabel.setText(gr.format(berat)+" gr");
        totalBeratPembulatanLabel.setText(gr.format(beratPembulatan)+" gr");
    }
    @FXML
    private void close(){
        stage.close();
    }
}
