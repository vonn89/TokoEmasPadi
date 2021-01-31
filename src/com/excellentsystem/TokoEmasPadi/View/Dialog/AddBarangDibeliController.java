/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Dialog;

import com.excellentsystem.TokoEmasPadi.DAO.PembelianDetailDAO;
import com.excellentsystem.TokoEmasPadi.DAO.PembelianHeadDAO;
import com.excellentsystem.TokoEmasPadi.Function;
import static com.excellentsystem.TokoEmasPadi.Function.getTableCell;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import static com.excellentsystem.TokoEmasPadi.Main.tglLengkap;
import static com.excellentsystem.TokoEmasPadi.Main.tglSql;
import com.excellentsystem.TokoEmasPadi.Model.PembelianDetail;
import com.excellentsystem.TokoEmasPadi.Model.PembelianHead;
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
public class AddBarangDibeliController {

    @FXML public TableView<PembelianDetail> pembelianTable;
    @FXML private TableColumn<PembelianDetail, String> noPembelianColumn;
    @FXML private TableColumn<PembelianDetail, String> tglPembelianColumn;
    @FXML private TableColumn<PembelianDetail, String> kodeSalesColumn;
    @FXML private TableColumn<PembelianDetail, String> namaColumn;
    @FXML private TableColumn<PembelianDetail, String> alamatColumn;
    
    @FXML private TableColumn<PembelianDetail, String> namaBarangColumn;
    @FXML private TableColumn<PembelianDetail, String> kodeKategoriColumn;
    @FXML private TableColumn<PembelianDetail, String> kondisiColumn;
    @FXML private TableColumn<PembelianDetail, String> suratColumn;
    @FXML private TableColumn<PembelianDetail, Number> beratColumn;
    @FXML private TableColumn<PembelianDetail, Number> hargaBeliColumn;
    
    @FXML private TextField searchField;
    @FXML private DatePicker tglMulaiPicker;
    @FXML private DatePicker tglAkhirPicker;
    private ObservableList<PembelianDetail> allPembelianDetail = FXCollections.observableArrayList();
    private ObservableList<PembelianDetail> filterData = FXCollections.observableArrayList();
    private Main mainApp;  
    private Stage stage;
    public void initialize() {
        noPembelianColumn.setCellValueFactory( param -> param.getValue().noPembelianProperty());
        tglPembelianColumn.setCellValueFactory(cellData -> { 
            try{
                return new SimpleStringProperty(tglLengkap.format(tglSql.parse(
                        cellData.getValue().getPembelianHead().getTglPembelian())));
            }catch(ParseException ex){
                return null;
            }
        });
        kodeSalesColumn.setCellValueFactory( param -> param.getValue().getPembelianHead().kodeSalesProperty());
        namaColumn.setCellValueFactory( param -> param.getValue().getPembelianHead().namaProperty());
        alamatColumn.setCellValueFactory( param -> param.getValue().getPembelianHead().alamatProperty());
        
        kondisiColumn.setCellValueFactory( param -> param.getValue().kondisiProperty());
        namaBarangColumn.setCellValueFactory( param -> param.getValue().namaBarangProperty());
        kodeKategoriColumn.setCellValueFactory( param -> param.getValue().kodeKategoriProperty());
        suratColumn.setCellValueFactory( param -> param.getValue().statusSuratProperty());
        beratColumn.setCellValueFactory( param -> param.getValue().beratProperty());
        beratColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        hargaBeliColumn.setCellValueFactory( param -> param.getValue().hargaBeliProperty());
        hargaBeliColumn.setCellFactory(col -> getTableCell(rp, "Rp"));
        
        tglMulaiPicker.setConverter(Function.getTglConverter());
        tglMulaiPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglMulaiPicker.setDayCellFactory((final DatePicker datePicker) -> 
                Function.getDateCellMulai(tglAkhirPicker, LocalDate.parse(sistem.getTglSystem())));
        tglAkhirPicker.setConverter(Function.getTglConverter());
        tglAkhirPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglAkhirPicker.setDayCellFactory((final DatePicker datePicker) -> 
                Function.getDateCellAkhir(tglMulaiPicker, LocalDate.parse(sistem.getTglSystem())));
        
        allPembelianDetail.addListener((ListChangeListener.Change<? extends PembelianDetail> change) -> {
            searchBarang();
        });
        searchField.textProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            searchBarang();
        });
        filterData.addAll(allPembelianDetail);
        pembelianTable.setItems(filterData);
    }
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        getPembelianHeadDetail();
    }
    public void getPembelianHeadDetail(){
        try(Connection con = Koneksi.getConnection()){
            allPembelianDetail.clear();
            List<PembelianDetail> listPembelianDetail = PembelianDetailDAO.getAllByTglBeliAndStatus(con, 
                    tglMulaiPicker.getValue().toString(), tglAkhirPicker.getValue().toString(), "true");
            List<PembelianHead> listPembelianHead = PembelianHeadDAO.getAllByTglBeliAndStatus(con, 
                    tglMulaiPicker.getValue().toString(), tglAkhirPicker.getValue().toString(), "true");
            for(PembelianDetail d : listPembelianDetail){
                for(PembelianHead h : listPembelianHead){
                    if(d.getNoPembelian().equals(h.getNoPembelian()))
                        d.setPembelianHead(h);
                }
//                if(d.getNoPembelian().equals(""))
                    allPembelianDetail.add(d);
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
            for (PembelianDetail temp : allPembelianDetail) {
                if (searchField.getText() == null || searchField.getText().equals(""))
                    filterData.add(temp);
                else{
                    if(checkColumn(temp.getNoPembelian())||
                        checkColumn(tglLengkap.format(tglSql.parse(temp.getPembelianHead().getTglPembelian())))||
                        checkColumn(temp.getPembelianHead().getKodeSales())||
                        checkColumn(temp.getPembelianHead().getNama())||
                        checkColumn(temp.getPembelianHead().getAlamat())||
                        checkColumn(temp.getPembelianHead().getCatatan())||
                        checkColumn(temp.getKondisi())||
                        checkColumn(temp.getNamaBarang())||
                        checkColumn(temp.getKodeKategori())||
                        checkColumn(temp.getStatusSurat())||
                        checkColumn(gr.format(temp.getBerat()))||
                        checkColumn(gr.format(temp.getHargaBeli()))||
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
