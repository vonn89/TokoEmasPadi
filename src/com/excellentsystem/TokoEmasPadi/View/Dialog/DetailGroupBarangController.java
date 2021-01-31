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
import static com.excellentsystem.TokoEmasPadi.Main.tglBarang;
import static com.excellentsystem.TokoEmasPadi.Main.tglNormal;
import com.excellentsystem.TokoEmasPadi.Model.Barang;
import com.excellentsystem.TokoEmasPadi.Model.StokBarang;
import java.sql.Connection;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Yunaz
 */
public class DetailGroupBarangController  {

    @FXML private TableView<Barang> barangTable;
    @FXML private TableColumn<Barang, Number> qtyColumn;
    @FXML private TableColumn<Barang, String> kodeBarcodeColumn;
    @FXML private TableColumn<Barang, String> namaBarangColumn;
    @FXML private TableColumn<Barang, Number> beratColumn;
    @FXML private TableColumn<Barang, Number> beratPembulatanColumn;
    @FXML private TableColumn<Barang, String> keteranganColumn;
    @FXML private TableColumn<Barang, String> merkColumn;
    
    @FXML private TextField searchField;
    @FXML private Label kodeJenisLabel;
    @FXML private Label tanggalLabel;
    @FXML private Label totalQtyLabel;
    @FXML private Label totalBeratLabel;
    @FXML private Label totalBeratPembulatanLabel;
    
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
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent e) -> {
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
                        MenuItem detail = new MenuItem("Detail Barang");
                        detail.setOnAction((ActionEvent e) -> {
                            detailBarang(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            barangTable.refresh();
                        });
                        rowMenu.getItems().addAll(detail, refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)&&mouseEvent.getClickCount() == 2){
                    if(row.getItem()!=null){
                        detailBarang(row.getItem());
                    }
                }
            });
            return row;
        });
        barangTable.setItems(filterData);
    }    
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
    }
    public void setBarang(String kodeJenis, String tgl){
        try(Connection con = Koneksi.getConnection()){
            kodeJenisLabel.setText(kodeJenis);
            tanggalLabel.setText(tglNormal.format(tglBarang.parse(tgl)));
            allBarang.clear();
            List<StokBarang> listStokBarang = StokBarangDAO.getAllByDateAndJenisAndBarcode(con, tgl, tgl, kodeJenis, "%");
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
            hitungTotal();
        }catch(Exception e){
            e.printStackTrace();
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    private void hitungTotal(){
        double beratPembulatan = 0;
        double berat = 0;
        int qty = 0;
        for(Barang b : filterData){
            beratPembulatan = beratPembulatan + b.getStokBarang().getStokAkhir()*b.getBeratPembulatan();
            berat = berat + b.getStokBarang().getStokAkhir()*b.getBerat();
            qty = qty + b.getStokBarang().getStokAkhir();
        }
        totalQtyLabel.setText(gr.format(qty));
        totalBeratLabel.setText(gr.format(berat)+ " gr");
        totalBeratPembulatanLabel.setText(gr.format(beratPembulatan)+ " gr");
    }
    private void detailBarang(Barang b){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(stage, child, "View/Dialog/DetailBarang.fxml");
        DetailBarangController controller = loader.getController();
        controller.setMainApp(mainApp, child);
        controller.setBarang(b);
        controller.setViewOnly();
    }
    @FXML
    private void close(){
        stage.close();
    }  
    
}
