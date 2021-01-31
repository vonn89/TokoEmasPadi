/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Report;

import com.excellentsystem.TokoEmasPadi.DAO.BarangDAO;
import com.excellentsystem.TokoEmasPadi.DAO.BatalBarcodeDAO;
import com.excellentsystem.TokoEmasPadi.Function;
import static com.excellentsystem.TokoEmasPadi.Function.getTableCell;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import static com.excellentsystem.TokoEmasPadi.Main.tglLengkap;
import static com.excellentsystem.TokoEmasPadi.Main.tglSql;
import com.excellentsystem.TokoEmasPadi.Model.Barang;
import com.excellentsystem.TokoEmasPadi.Model.BatalBarcode;
import com.excellentsystem.TokoEmasPadi.View.Dialog.DetailBarangController;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Yunaz
 */
public class LaporanBatalBarcodeController {


    @FXML private TableView<BatalBarcode> barcodeBarangHeadTable;
    @FXML private TableColumn<BatalBarcode, Number> jumlahColumn;
    @FXML private TableColumn<BatalBarcode, String> kodeBarcodeColumn;
    @FXML private TableColumn<BatalBarcode, String> namaBarangColumn;
    @FXML private TableColumn<BatalBarcode, String> namaBarcodeColumn;
    @FXML private TableColumn<BatalBarcode, String> kodeKategoriColumn;
    @FXML private TableColumn<BatalBarcode, String> kodeJenisColumn;
    @FXML private TableColumn<BatalBarcode, Number> beratColumn;
    @FXML private TableColumn<BatalBarcode, Number> beratPembulatanColumn;
    @FXML private TableColumn<BatalBarcode, Number> nilaiPokokColumn;
    @FXML private TableColumn<BatalBarcode, Number> hargaJualColumn;
    @FXML private TableColumn<BatalBarcode, String> keteranganColumn;
    @FXML private TableColumn<BatalBarcode, String> merkColumn;
    @FXML private TableColumn<BatalBarcode, String> userBarcodeColumn;
    @FXML private TableColumn<BatalBarcode, String> tglBarcodeColumn;
    
    
    @FXML private DatePicker tglStartPicker;
    @FXML private DatePicker tglAkhirPicker;
    @FXML private TextField searchField;
    private Main mainApp;   
    final TreeItem<Barang> root = new TreeItem<>();
    private ObservableList<BatalBarcode> allBarcodeBarang = FXCollections.observableArrayList();
    private ObservableList<BatalBarcode> filterData = FXCollections.observableArrayList();
    public void initialize(){
        jumlahColumn.setCellValueFactory(cellData -> cellData.getValue().qtyProperty());
        jumlahColumn.setCellFactory(col -> getTableCell(rp, ""));
        kodeBarcodeColumn.setCellValueFactory(cellData -> cellData.getValue().kodeBarcodeProperty());
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().namaBarangProperty());
        namaBarcodeColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().namaBarcodeProperty());
        kodeKategoriColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().kodeKategoriProperty());
        kodeJenisColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().kodeJenisProperty());
        beratColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().beratProperty());
        beratColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        beratPembulatanColumn.setCellValueFactory(cellData ->cellData.getValue().getBarang().beratPembulatanProperty());
        beratPembulatanColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        nilaiPokokColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().nilaiPokokProperty());
        nilaiPokokColumn.setCellFactory(col -> getTableCell(rp, "Rp"));
        hargaJualColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().hargaJualProperty());
        hargaJualColumn.setCellFactory(col -> getTableCell(rp, "Rp"));
        keteranganColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().keteranganProperty());
        merkColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().merkProperty());
        userBarcodeColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().barcodeByProperty());
        
        tglBarcodeColumn.setCellValueFactory(cellData -> { 
            try {
                return new SimpleStringProperty(tglLengkap.format(tglSql.parse(cellData.getValue().getBarang().getBarcodeDate())));
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
        
        allBarcodeBarang.addListener((ListChangeListener.Change<? extends BatalBarcode> change) -> {
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
            TableRow<BatalBarcode> row = new TableRow<BatalBarcode>() {
                @Override
                public void updateItem(BatalBarcode item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rowMenu);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem detail = new MenuItem("Detail Barang");
                        detail.setOnAction((ActionEvent e) -> {
                            detailBarang(item.getBarang());
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            getBarcodeBarang();
                        });
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
        this.mainApp = mainApp;
        getBarcodeBarang();
        barcodeBarangHeadTable.setItems(filterData);
    }   
    @FXML
    private void getBarcodeBarang(){
        try(Connection con = Koneksi.getConnection()){
            allBarcodeBarang.clear();
            List<BatalBarcode> listBatal = BatalBarcodeDAO.getAllByTglBatal(con, 
                    tglStartPicker.getValue().toString(), tglAkhirPicker.getValue().toString());
            for(BatalBarcode bb : listBatal){
                bb.setBarang(BarangDAO.get(con, bb.getKodeBarcode()));
            }
            allBarcodeBarang.addAll(listBatal);
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
            for (BatalBarcode temp : allBarcodeBarang) {
                if (searchField.getText() == null || searchField.getText().equals(""))
                    filterData.add(temp);
                else{
                    if(checkColumn(temp.getKodeBarcode())||
                        checkColumn(tglLengkap.format(tglSql.parse(temp.getBarang().getBarcodeDate())))||
                        checkColumn(temp.getBarang().getNamaBarang())||
                        checkColumn(temp.getBarang().getNamaBarcode())||
                        checkColumn(temp.getBarang().getKeterangan())||
                        checkColumn(temp.getBarang().getKodeKategori())||
                        checkColumn(temp.getBarang().getKodeJenis())||
                        checkColumn(temp.getBarang().getKeterangan())||
                        checkColumn(temp.getBarang().getMerk())||
                        checkColumn(gr.format(temp.getBarang().getBerat()))||
                        checkColumn(gr.format(temp.getBarang().getBeratPembulatan()))||
                        checkColumn(rp.format(temp.getBarang().getNilaiPokok()))||
                        checkColumn(rp.format(temp.getBarang().getHargaJual()))||
                        checkColumn(temp.getBarang().getBarcodeBy()))
                            filterData.add(temp);
                }
            }
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
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
