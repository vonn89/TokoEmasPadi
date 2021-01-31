/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Report;

import com.excellentsystem.TokoEmasPadi.DAO.StokBarangDAO;
import com.excellentsystem.TokoEmasPadi.Function;
import static com.excellentsystem.TokoEmasPadi.Function.getTableCell;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import com.excellentsystem.TokoEmasPadi.Model.StokBarang;
import com.excellentsystem.TokoEmasPadi.View.Dialog.DetailGroupBarangController;
import com.excellentsystem.TokoEmasPadi.View.Dialog.KartuStokController;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Yunaz
 */
public class LaporanStokBarangController  {
    @FXML private TableView<StokBarang> stokBarangTable;
    @FXML private TableColumn<StokBarang, String> kodeJenisColumn;
    @FXML private TableColumn<StokBarang, Number> stokAwalColumn;
    @FXML private TableColumn<StokBarang, Number> beratAwalColumn;
    @FXML private TableColumn<StokBarang, Number> stokMasukColumn;
    @FXML private TableColumn<StokBarang, Number> beratMasukColumn;
    @FXML private TableColumn<StokBarang, Number> stokKeluarColumn;
    @FXML private TableColumn<StokBarang, Number> beratKeluarColumn;
    @FXML private TableColumn<StokBarang, Number> stokAkhirColumn;
    @FXML private TableColumn<StokBarang, Number> beratAkhirColumn;
    
    @FXML private TextField searchField;
    @FXML
    private DatePicker tglMulaiPicker;
    @FXML
    private DatePicker tglAkhirPicker;
    @FXML private Label totalQtyMasukLabel;
    @FXML private Label totalBeratMasukLabel;
    @FXML private Label totalQtyKeluarLabel;
    @FXML private Label totalBeratKeluarLabel;
    @FXML private Label totalQtyAkhirLabel;
    @FXML private Label totalBeratAkhirLabel;
    
    private Main mainApp;   
    private final ObservableList<StokBarang> allStokBarang = FXCollections.observableArrayList();
    private final ObservableList<StokBarang> filterData = FXCollections.observableArrayList();
    public void initialize() {
        kodeJenisColumn.setCellValueFactory(cellData -> cellData.getValue().kodeJenisProperty());
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
        stokAkhirColumn.getStyleClass().add("color-column");
        
        beratAkhirColumn.setCellValueFactory(cellData -> cellData.getValue().beratAkhirProperty());
        beratAkhirColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        beratAkhirColumn.getStyleClass().add("color-column");
        
        tglMulaiPicker.setConverter(Function.getTglConverter());
        tglMulaiPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglMulaiPicker.setDayCellFactory((final DatePicker datePicker)
                -> Function.getDateCellMulai(tglAkhirPicker, LocalDate.parse(sistem.getTglSystem())));
        tglAkhirPicker.setConverter(Function.getTglConverter());
        tglAkhirPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglAkhirPicker.setDayCellFactory((final DatePicker datePicker)
                -> Function.getDateCellAkhir(tglMulaiPicker, LocalDate.parse(sistem.getTglSystem())));
        
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            getStokBarang();
        });
        rowMenu.getItems().addAll(refresh);

        stokBarangTable.setContextMenu(rowMenu);
        stokBarangTable.setRowFactory(ttv -> {
            TableRow<StokBarang> row = new TableRow<StokBarang>() {
                @Override
                public void updateItem(StokBarang item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rowMenu);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem cek = new MenuItem("Cek Detail");
                        cek.setOnAction((ActionEvent e)->{
                            showDetail(item);
                        });
                        MenuItem cekKartuStok = new MenuItem("Cek Kartu Stok");
                        cekKartuStok.setOnAction((ActionEvent e)->{
                            showKartuStok(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            getStokBarang();
                        });
                        rowMenu.getItems().addAll(cek, cekKartuStok, refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            return row;
        });
        allStokBarang.addListener((ListChangeListener.Change<? extends StokBarang> change) -> {
            searchStokBarang();
        });
        searchField.textProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            searchStokBarang();
        });
        filterData.addAll(allStokBarang);
        stokBarangTable.setItems(filterData);
    }    
    public void setMainApp(Main mainApp){
        this.mainApp = mainApp;
        getStokBarang();
    } 
    @FXML
    private void getStokBarang(){
        try(Connection con = Koneksi.getConnection()){
            allStokBarang.clear();
//            List<StokBarang> listStokAwal = StokBarangDAO.getAllByTanggal(con, tglMulaiPicker.getValue().toString());
            List<StokBarang> listStokBarang = StokBarangDAO.getAllByTanggalMulaiAndAkhir(con, tglMulaiPicker.getValue().toString(), tglAkhirPicker.getValue().toString());
//            List<StokBarang> listBarang = new ArrayList<>();
//            for(StokBarang s : listStokAwal){
//                boolean status = true;
//                for(StokBarang temp : listBarang){
//                    if(s.getKodeJenis().equals(temp.getKodeJenis())){
//                        status = false;
//                        temp.setStokAwal(temp.getStokAwal()+s.getStokAwal());
//                        temp.setBeratAwal(temp.getBeratAwal()+s.getBeratAwal());
//                        temp.setStokMasuk(temp.getStokMasuk()+s.getStokMasuk());
//                        temp.setBeratMasuk(temp.getBeratMasuk()+s.getBeratMasuk());
//                        temp.setStokKeluar(temp.getStokKeluar()+s.getStokKeluar());
//                        temp.setBeratKeluar(temp.getBeratKeluar()+s.getBeratKeluar());
//                        temp.setStokAkhir(temp.getStokAkhir()+s.getStokAkhir());
//                        temp.setBeratAkhir(temp.getBeratAkhir()+s.getBeratAkhir());
//                    }
//                }
//                if(status){
//                    StokBarang stokJenis = new StokBarang();
//                    stokJenis.setKodeJenis(s.getKodeJenis());
//                    stokJenis.setStokAwal(s.getStokAwal());
//                    stokJenis.setBeratAwal(s.getBeratAwal());
//                    stokJenis.setStokMasuk(s.getStokMasuk());
//                    stokJenis.setBeratMasuk(s.getBeratMasuk());
//                    stokJenis.setStokKeluar(s.getStokKeluar());
//                    stokJenis.setBeratKeluar(s.getBeratKeluar());
//                    stokJenis.setStokAkhir(s.getStokAkhir());
//                    stokJenis.setBeratAkhir(s.getBeratAkhir());
//                    listBarang.add(stokJenis);
//                }
//            }
//            listBarang.sort(Comparator.comparing(StokBarang::getKodeJenis));
//            allStokBarang.addAll(listBarang);
            allStokBarang.addAll(listStokBarang);
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
    private void searchStokBarang() {
        try{
            filterData.clear();
            for (StokBarang temp : allStokBarang) {
                if (searchField.getText() == null || searchField.getText().equals(""))
                    filterData.add(temp);
                else{
                    if(checkColumn(temp.getKodeJenis()))
                        filterData.add(temp);
                }
            }
            hitungTotal();
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    private void hitungTotal(){
        int totalQtyMasuk = 0;
        double totalBeratMasuk = 0;
        int totalQtyKeluar = 0;
        double totalBeratKeluar = 0;
        int totalQtyAkhir = 0;
        double totalBeratAkhir = 0;
        for(StokBarang b : filterData){
            totalQtyMasuk = totalQtyMasuk + b.getStokAkhir();
            totalBeratMasuk = totalBeratMasuk + b.getBeratAkhir();
            totalQtyKeluar = totalQtyKeluar + b.getStokAkhir();
            totalBeratKeluar = totalBeratKeluar + b.getBeratAkhir();
            totalQtyAkhir = totalQtyAkhir + b.getStokAkhir();
            totalBeratAkhir = totalBeratAkhir + b.getBeratAkhir();
        }
        totalQtyMasukLabel.setText(gr.format(totalQtyMasuk));
        totalBeratMasukLabel.setText(gr.format(totalBeratMasuk)+" gr");
        totalQtyKeluarLabel.setText(gr.format(totalQtyKeluar));
        totalBeratKeluarLabel.setText(gr.format(totalBeratKeluar)+" gr");
        totalQtyAkhirLabel.setText(gr.format(totalQtyAkhir));
        totalBeratAkhirLabel.setText(gr.format(totalBeratAkhir)+" gr");
    }  
    private void showDetail(StokBarang s){
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailGroupBarang.fxml");
        DetailGroupBarangController x = loader.getController();
        x.setMainApp(mainApp, stage);
        x.setBarang(s.getKodeJenis(), tglAkhirPicker.getValue().toString());
    }
    private void showKartuStok(StokBarang s){
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/KartuStok.fxml");
        KartuStokController x = loader.getController();
        x.setMainApp(mainApp, stage);
        x.setBarang("Kode Jenis", s.getKodeJenis());
    }
}
