/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Dialog;

import com.excellentsystem.TokoEmasPadi.DAO.PenjualanDetailDAO;
import com.excellentsystem.TokoEmasPadi.DAO.PenjualanHeadDAO;
import com.excellentsystem.TokoEmasPadi.Function;
import static com.excellentsystem.TokoEmasPadi.Function.getTableCell;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import static com.excellentsystem.TokoEmasPadi.Main.tglLengkap;
import static com.excellentsystem.TokoEmasPadi.Main.tglSql;
import com.excellentsystem.TokoEmasPadi.Model.PenjualanDetail;
import com.excellentsystem.TokoEmasPadi.Model.PenjualanHead;
import java.sql.Connection;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
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
public class AddBarangTerjualController {

    @FXML public TableView<PenjualanDetail> penjualanTable;
    @FXML private TableColumn<PenjualanDetail, String> noPenjualanColumn;
    @FXML private TableColumn<PenjualanDetail, String> tglPenjualanColumn;
    @FXML private TableColumn<PenjualanDetail, String> kodeSalesColumn;
    @FXML private TableColumn<PenjualanDetail, String> namaColumn;
    @FXML private TableColumn<PenjualanDetail, String> alamatColumn;
    
    @FXML private TableColumn<PenjualanDetail, String> kodeBarcodeColumn;
    @FXML private TableColumn<PenjualanDetail, String> namaBarangColumn;
    @FXML private TableColumn<PenjualanDetail, String> kodeKategoriColumn;
    @FXML private TableColumn<PenjualanDetail, String> kodeJenisColumn;
    @FXML private TableColumn<PenjualanDetail, Number> beratColumn;
    @FXML private TableColumn<PenjualanDetail, Number> beratPembulatanColumn;
    @FXML private TableColumn<PenjualanDetail, Number> hargaJualColumn;
    
    @FXML private TextField searchField;
    @FXML private DatePicker tglMulaiPicker;
    @FXML private DatePicker tglAkhirPicker;
    private ObservableList<PenjualanDetail> allPenjualanDetail = FXCollections.observableArrayList();
    private ObservableList<PenjualanDetail> filterData = FXCollections.observableArrayList();
    private Main mainApp;  
    private Stage stage;
    public void initialize() {
        noPenjualanColumn.setCellValueFactory( param -> param.getValue().noPenjualanProperty());
        tglPenjualanColumn.setCellValueFactory(cellData -> { 
            try{
                return new SimpleStringProperty(tglLengkap.format(tglSql.parse(
                        cellData.getValue().getPenjualanHead().getTglPenjualan())));
            }catch(ParseException ex){
                return null;
            }
        });
        kodeSalesColumn.setCellValueFactory( param -> param.getValue().getPenjualanHead().kodeSalesProperty());
        namaColumn.setCellValueFactory( param -> param.getValue().getPenjualanHead().namaProperty());
        alamatColumn.setCellValueFactory( param -> param.getValue().getPenjualanHead().alamatProperty());
        
        kodeBarcodeColumn.setCellValueFactory( param -> param.getValue().kodeBarcodeProperty());
        namaBarangColumn.setCellValueFactory( param -> param.getValue().namaBarangProperty());
        kodeKategoriColumn.setCellValueFactory( param -> param.getValue().kodeKategoriProperty());
        kodeJenisColumn.setCellValueFactory( param -> param.getValue().kodeJenisProperty());
        beratColumn.setCellValueFactory( param -> param.getValue().beratProperty());
        beratColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        beratPembulatanColumn.setCellValueFactory( param -> param.getValue().beratPembulatanProperty());
        beratPembulatanColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        hargaJualColumn.setCellValueFactory( param -> param.getValue().hargaJualProperty());
        hargaJualColumn.setCellFactory(col -> getTableCell(rp, "Rp"));
        
        tglMulaiPicker.setConverter(Function.getTglConverter());
        tglMulaiPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglMulaiPicker.setDayCellFactory((final DatePicker datePicker) -> 
                Function.getDateCellMulai(tglAkhirPicker, LocalDate.parse(sistem.getTglSystem())));
        tglAkhirPicker.setConverter(Function.getTglConverter());
        tglAkhirPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglAkhirPicker.setDayCellFactory((final DatePicker datePicker) -> 
                Function.getDateCellAkhir(tglMulaiPicker, LocalDate.parse(sistem.getTglSystem())));
        
        allPenjualanDetail.addListener((ListChangeListener.Change<? extends PenjualanDetail> change) -> {
            searchBarang();
        });
        searchField.textProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            searchBarang();
        });
        filterData.addAll(allPenjualanDetail);
        penjualanTable.setItems(filterData);
    }
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        getPenjualanHeadDetail();
    }
    public void getPenjualanHeadDetail(){
        try(Connection con = Koneksi.getConnection()){
            allPenjualanDetail.clear();
            List<PenjualanDetail> listPenjualanDetail = PenjualanDetailDAO.getAllByTglPenjualanAndStatus(con, 
                    tglMulaiPicker.getValue().toString(), tglAkhirPicker.getValue().toString(), "true");
            List<PenjualanHead> listPenjualanHead = PenjualanHeadDAO.getAllByTglPenjualanAndStatus(con, 
                    tglMulaiPicker.getValue().toString(), tglAkhirPicker.getValue().toString(), "true");
            for(PenjualanDetail d : listPenjualanDetail){
                for(PenjualanHead h : listPenjualanHead){
                    if(d.getNoPenjualan().equals(h.getNoPenjualan()))
                        d.setPenjualanHead(h);
                }
                if(d.getNoPembelian().equals(""))
                    allPenjualanDetail.add(d);
            }
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
    private void searchBarang() {
        try{
            filterData.clear();
            for (PenjualanDetail temp : allPenjualanDetail) {
                if (searchField.getText() == null || searchField.getText().equals(""))
                    filterData.add(temp);
                else{
                    if(checkColumn(temp.getNoPenjualan())||
                        checkColumn(tglLengkap.format(tglSql.parse(temp.getPenjualanHead().getTglPenjualan())))||
                        checkColumn(temp.getPenjualanHead().getKodeSales())||
                        checkColumn(temp.getPenjualanHead().getNama())||
                        checkColumn(temp.getPenjualanHead().getAlamat())||
                        checkColumn(temp.getPenjualanHead().getCatatan())||
                        checkColumn(temp.getKodeBarcode())||
                        checkColumn(temp.getNamaBarang())||
                        checkColumn(temp.getKodeKategori())||
                        checkColumn(temp.getKodeJenis())||
                        checkColumn(gr.format(temp.getBerat()))||
                        checkColumn(gr.format(temp.getBeratPembulatan()))||
                        checkColumn(gr.format(temp.getNilaiPokok()))||
                        checkColumn(gr.format(temp.getHargaJual()))||
                        checkColumn(gr.format(temp.getHargaKomp())))
                            filterData.add(temp);
                }
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
