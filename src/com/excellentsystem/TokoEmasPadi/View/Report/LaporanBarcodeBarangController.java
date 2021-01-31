/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Report;

import com.excellentsystem.TokoEmasPadi.DAO.BarangDAO;
import com.excellentsystem.TokoEmasPadi.Function;
import static com.excellentsystem.TokoEmasPadi.Function.getTreeTableCell;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import static com.excellentsystem.TokoEmasPadi.Main.tglBarang;
import static com.excellentsystem.TokoEmasPadi.Main.tglLengkap;
import static com.excellentsystem.TokoEmasPadi.Main.tglNormal;
import static com.excellentsystem.TokoEmasPadi.Main.tglSql;
import com.excellentsystem.TokoEmasPadi.Model.Barang;
import com.excellentsystem.TokoEmasPadi.View.Dialog.DetailBarangController;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Yunaz
 */
public class LaporanBarcodeBarangController {


    @FXML private TreeTableView<Barang> barcodeBarangHeadTable;
    @FXML private TreeTableColumn<Barang, String> kodeBarcodeColumn;
    @FXML private TreeTableColumn<Barang, String> namaBarangColumn;
    @FXML private TreeTableColumn<Barang, String> namaBarcodeColumn;
    @FXML private TreeTableColumn<Barang, String> kodeKategoriColumn;
    @FXML private TreeTableColumn<Barang, String> kodeJenisColumn;
    @FXML private TreeTableColumn<Barang, Number> persentaseColumn;
    @FXML private TreeTableColumn<Barang, Number> beratColumn;
    @FXML private TreeTableColumn<Barang, Number> beratPembulatanColumn;
    @FXML private TreeTableColumn<Barang, Number> nilaiPokokColumn;
    @FXML private TreeTableColumn<Barang, Number> hargaJualColumn;
    @FXML private TreeTableColumn<Barang, String> keteranganColumn;
    @FXML private TreeTableColumn<Barang, String> merkColumn;
    @FXML private TreeTableColumn<Barang, String> userBarcodeColumn;
    @FXML private TreeTableColumn<Barang, String> tglBarcodeColumn;
    
    
    @FXML private ComboBox<String> groupByCombo;
    @FXML private DatePicker tglStartPicker;
    @FXML private DatePicker tglAkhirPicker;
    @FXML private TextField searchField;
    private Main mainApp;   
    final TreeItem<Barang> root = new TreeItem<>();
    private ObservableList<Barang> allBarcodeBarang = FXCollections.observableArrayList();
    private ObservableList<Barang> filterData = FXCollections.observableArrayList();
    public void initialize(){
        kodeBarcodeColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().kodeBarcodeProperty());
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().namaBarangProperty());
        namaBarcodeColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().namaBarcodeProperty());
        kodeKategoriColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().kodeKategoriProperty());
        kodeJenisColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().kodeJenisProperty());
        persentaseColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().persentaseProperty());
        persentaseColumn.setCellFactory(col -> getTreeTableCell(gr, true, "%"));
        beratColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().beratProperty());
        beratColumn.setCellFactory(col -> getTreeTableCell(gr, true, "gr"));
        beratPembulatanColumn.setCellValueFactory(cellData ->cellData.getValue().getValue().beratPembulatanProperty());
        beratPembulatanColumn.setCellFactory(col -> getTreeTableCell(gr, true, "gr"));
        nilaiPokokColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().nilaiPokokProperty());
        nilaiPokokColumn.setCellFactory(col -> getTreeTableCell(rp, true, "Rp"));
        hargaJualColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().hargaJualProperty());
        hargaJualColumn.setCellFactory(col -> getTreeTableCell(rp, true, "Rp"));
        keteranganColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().keteranganProperty());
        merkColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().merkProperty());
        userBarcodeColumn.setCellValueFactory(cellData -> cellData.getValue().getValue().barcodeByProperty());
        
        tglBarcodeColumn.setCellValueFactory(cellData -> { 
            try {
                return new SimpleStringProperty(tglLengkap.format(tglSql.parse(cellData.getValue().getValue().getBarcodeDate())));
            } catch (Exception ex) {
                return null;
            }
        });
        tglStartPicker.setConverter(Function.getTglConverter());
        tglStartPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglStartPicker.setDayCellFactory((final DatePicker datePicker) -> 
                Function.getDateCellMulai(tglAkhirPicker, LocalDate.parse(sistem.getTglSystem())));
        tglAkhirPicker.setConverter(Function.getTglConverter());
        tglAkhirPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglAkhirPicker.setDayCellFactory((final DatePicker datePicker) -> 
                Function.getDateCellAkhir(tglStartPicker, LocalDate.parse(sistem.getTglSystem())));
        
        allBarcodeBarang.addListener((ListChangeListener.Change<? extends Barang> change) -> {
            searchBarcodeBarang();
        });
        searchField.textProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            searchBarcodeBarang();
        });
        filterData.addAll(allBarcodeBarang);
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            getBarcodeBarang();
        });
        rowMenu.getItems().addAll(refresh);
        barcodeBarangHeadTable.setContextMenu(rowMenu);
        barcodeBarangHeadTable.setRowFactory(table -> {
            TreeTableRow<Barang> row = new TreeTableRow<Barang>() {
                @Override
                public void updateItem(Barang item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rowMenu);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem detail = new MenuItem("Detail Barang");
                        detail.setOnAction((ActionEvent e) -> {
                            detailBarang(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            getBarcodeBarang();
                        });
                        if(item.getNamaBarang()!=null)
                            rowMenu.getItems().addAll(detail);
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
            ObservableList<String> groupBy = FXCollections.observableArrayList();
            groupBy.add("Tanggal");
            groupBy.add("User");
            groupBy.add("Kategori Barang");
            groupBy.add("Jenis Barang");
            groupByCombo.setItems(groupBy);
            groupByCombo.getSelectionModel().select("Tanggal");
            getBarcodeBarang();
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }   
    @FXML
    private void getBarcodeBarang(){
        try(Connection con = Koneksi.getConnection()){
            allBarcodeBarang.clear();
            allBarcodeBarang.addAll(BarangDAO.getAllByTglBarcode(con, 
                    tglStartPicker.getValue().toString(), tglAkhirPicker.getValue().toString(), "%", "%", "%", "%", "%"));
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
    private void searchBarcodeBarang() {
        try{
            filterData.clear();
            for (Barang temp : allBarcodeBarang) {
                if (searchField.getText() == null || searchField.getText().equals(""))
                    filterData.add(temp);
                else{
                    if(checkColumn(temp.getKodeBarcode())||
                        checkColumn(tglLengkap.format(tglSql.parse(temp.getBarcodeDate())))||
                        checkColumn(temp.getNamaBarang())||
                        checkColumn(temp.getNamaBarcode())||
                        checkColumn(temp.getKeterangan())||
                        checkColumn(temp.getKodeKategori())||
                        checkColumn(temp.getKodeJenis())||
                        checkColumn(temp.getKeterangan())||
                        checkColumn(temp.getMerk())||
                        checkColumn(gr.format(temp.getBerat()))||
                        checkColumn(gr.format(temp.getBeratPembulatan()))||
                        checkColumn(rp.format(temp.getNilaiPokok()))||
                        checkColumn(rp.format(temp.getHargaJual()))||
                        checkColumn(temp.getBarcodeBy()))
                            filterData.add(temp);
                }
            }
            setTable();
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    private void setTable()throws Exception{
        if(barcodeBarangHeadTable.getRoot()!=null)
            barcodeBarangHeadTable.getRoot().getChildren().clear();
        List<String> groupBy = new ArrayList<>();
        for(Barang temp: filterData){
            if(groupByCombo.getSelectionModel().getSelectedItem().equals("Tanggal")){
                if(!groupBy.contains(temp.getBarcodeDate().substring(0,10)))
                    groupBy.add(temp.getBarcodeDate().substring(0,10));
            }else if(groupByCombo.getSelectionModel().getSelectedItem().equals("User")){
                if(!groupBy.contains(temp.getBarcodeBy()))
                    groupBy.add(temp.getBarcodeBy());
            }else if(groupByCombo.getSelectionModel().getSelectedItem().equals("Kategori Barang")){
                if(!groupBy.contains(temp.getKodeKategori()))
                    groupBy.add(temp.getKodeKategori());
            }else if(groupByCombo.getSelectionModel().getSelectedItem().equals("Jenis Barang")){
                if(!groupBy.contains(temp.getKodeJenis()))
                    groupBy.add(temp.getKodeJenis());
            }
        }
        for(String temp : groupBy){
            Barang head = new Barang();
            if(groupByCombo.getSelectionModel().getSelectedItem().equals("Tanggal"))
                head.setKodeBarcode(tglNormal.format(tglBarang.parse(temp)));
            else
                head.setKodeBarcode(temp);
            TreeItem<Barang> parent = new TreeItem<>(head);
            for(Barang detail: filterData){
                if(groupByCombo.getSelectionModel().getSelectedItem().equals("Tanggal")){
                    if(temp.equals(detail.getBarcodeDate().substring(0, 10))){
                        TreeItem<Barang> child = new TreeItem<>(detail);
                        parent.getChildren().addAll(child);
                    }
                }else if(groupByCombo.getSelectionModel().getSelectedItem().equals("User")){
                    if(temp.equals(detail.getBarcodeBy())){
                        TreeItem<Barang> child = new TreeItem<>(detail);
                        parent.getChildren().addAll(child);
                    }
                }else if(groupByCombo.getSelectionModel().getSelectedItem().equals("Kategori Barang")){
                    if(temp.equals(detail.getKodeKategori())){
                        TreeItem<Barang> child = new TreeItem<>(detail);
                        parent.getChildren().addAll(child);
                    }
                }else if(groupByCombo.getSelectionModel().getSelectedItem().equals("Jenis Barang")){
                    if(temp.equals(detail.getKodeJenis())){
                        TreeItem<Barang> child = new TreeItem<>(detail);
                        parent.getChildren().addAll(child);
                    }
                }
            }
            root.getChildren().add(parent);
        }
        barcodeBarangHeadTable.setRoot(root);
    }      
    private void detailBarang(Barang b){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/DetailBarang.fxml");
        DetailBarangController controller = loader.getController();
        controller.setMainApp(mainApp, child);
        controller.setBarang(b);
        controller.setViewOnly();
    }
}
