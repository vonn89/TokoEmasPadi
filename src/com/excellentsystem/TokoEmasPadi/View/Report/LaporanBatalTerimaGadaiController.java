/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Report;

import com.excellentsystem.TokoEmasPadi.Function;
import static com.excellentsystem.TokoEmasPadi.Function.getTreeTableCell;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import static com.excellentsystem.TokoEmasPadi.Main.tglLengkap;
import static com.excellentsystem.TokoEmasPadi.Main.tglSql;
import com.excellentsystem.TokoEmasPadi.Model.GadaiHead;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.stage.Modality;

/**
 * FXML Controller class
 *
 * @author Yunaz
 */
public class LaporanBatalTerimaGadaiController  {

    
    @FXML private TreeTableView<GadaiHead> gadaiTable;
    @FXML private TreeTableColumn<GadaiHead, String> noGadaiColumn;
    @FXML private TreeTableColumn<GadaiHead, String> tglGadaiColumn;
    @FXML private TreeTableColumn<GadaiHead, String> kodeSalesColumn;
    @FXML private TreeTableColumn<GadaiHead, String> tglBatalColumn;
    @FXML private TreeTableColumn<GadaiHead, String> userBatalColumn;
    @FXML private TreeTableColumn<GadaiHead, String> kodePelangganColumn;
    @FXML private TreeTableColumn<GadaiHead, String> namaColumn;
    @FXML private TreeTableColumn<GadaiHead, String> alamatColumn;
    @FXML private TreeTableColumn<GadaiHead, String> noTelpColumn;
    @FXML private TreeTableColumn<GadaiHead, String> keteranganColumn;
    @FXML private TreeTableColumn<GadaiHead, Number> totalBeratColumn;
    @FXML private TreeTableColumn<GadaiHead, Number> totalPinjamanColumn;
    @FXML private TreeTableColumn<GadaiHead, Number> lamaPinjamColumn;
    @FXML private TreeTableColumn<GadaiHead, Number> bungaPersenColumn;
    @FXML private TreeTableColumn<GadaiHead, Number> bungaKompColumn;
    @FXML private TreeTableColumn<GadaiHead, Number> bungaRpColumn;
    @FXML private TreeTableColumn<GadaiHead, String> jatuhTempoColumn;
    
    @FXML private TextField searchField;
    @FXML private Label totalBeratField;
    @FXML private Label totalPinjamanField;
    @FXML private DatePicker tglMulaiPicker;
    @FXML private DatePicker tglAkhirPicker;
    final TreeItem<GadaiHead> root = new TreeItem<>();
    private ObservableList<GadaiHead> allGadai = FXCollections.observableArrayList();
    private ObservableList<GadaiHead> filterData = FXCollections.observableArrayList();
    private Main mainApp;  
    public void initialize() {
        noGadaiColumn.setCellValueFactory( param -> param.getValue().getValue().noGadaiProperty());
        tglGadaiColumn.setCellValueFactory(cellData -> { 
            try {
                return new SimpleStringProperty(tglLengkap.format(tglSql.parse(
                        cellData.getValue().getValue().getTglGadai())));
            } catch (Exception ex) {
                return null;
            }
        });
        tglBatalColumn.setCellValueFactory(cellData -> { 
            try {
                return new SimpleStringProperty(tglLengkap.format(tglSql.parse(
                        cellData.getValue().getValue().getTglBatal())));
            } catch (Exception ex) {
                return null;
            }
        });
        userBatalColumn.setCellValueFactory( param -> param.getValue().getValue().salesBatalProperty());
        kodeSalesColumn.setCellValueFactory( param -> param.getValue().getValue().kodeSalesProperty());
//        kodePelangganColumn.setCellValueFactory( param -> param.getValue().getValue().kodePelangganProperty());
        namaColumn.setCellValueFactory( param -> param.getValue().getValue().namaProperty());
        alamatColumn.setCellValueFactory( param -> param.getValue().getValue().alamatProperty());
//        noTelpColumn.setCellValueFactory( param -> param.getValue().getValue().noTelpProperty());
        keteranganColumn.setCellValueFactory( param -> param.getValue().getValue().keteranganProperty());
        totalBeratColumn.setCellValueFactory(param -> param.getValue().getValue().totalBeratProperty());
        totalBeratColumn.setCellFactory(col -> getTreeTableCell(gr, false, "gr"));
        totalPinjamanColumn.setCellValueFactory(param -> param.getValue().getValue().totalPinjamanProperty());
        totalPinjamanColumn.setCellFactory(col -> getTreeTableCell(rp, false, "Rp"));
        lamaPinjamColumn.setCellValueFactory(param -> param.getValue().getValue().lamaPinjamProperty());
        lamaPinjamColumn.setCellFactory(col -> getTreeTableCell(rp, false, ""));
        bungaPersenColumn.setCellValueFactory(param -> param.getValue().getValue().bungaPersenProperty());
        bungaPersenColumn.setCellFactory(col -> getTreeTableCell(gr, false, ""));
        bungaKompColumn.setCellValueFactory(param -> param.getValue().getValue().bungaKompProperty());
        bungaKompColumn.setCellFactory(col -> getTreeTableCell(rp, false, "Rp"));
        bungaRpColumn.setCellValueFactory(param -> param.getValue().getValue().bungaRpProperty());
        bungaRpColumn.setCellFactory(col -> getTreeTableCell(rp, false, "Rp"));
        jatuhTempoColumn.setCellValueFactory( param -> param.getValue().getValue().jatuhTempoProperty());
        
        tglMulaiPicker.setConverter(Function.getTglConverter());
        tglMulaiPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglMulaiPicker.setDayCellFactory((final DatePicker datePicker) -> 
                Function.getDateCellMulai(tglAkhirPicker, LocalDate.parse(sistem.getTglSystem())));
        tglAkhirPicker.setConverter(Function.getTglConverter());
        tglAkhirPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglAkhirPicker.setDayCellFactory((final DatePicker datePicker) -> 
                Function.getDateCellAkhir(tglMulaiPicker, LocalDate.parse(sistem.getTglSystem())));
        
        allGadai.addListener((ListChangeListener.Change<? extends GadaiHead> change) -> {
            searchGadai();
        });
        searchField.textProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            searchGadai();
        });
        filterData.addAll(allGadai);
    }
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        getGadai();
    }  
    @FXML
    private void getGadai(){
        try(Connection con = Koneksi.getConnection()){
            allGadai.clear();
//            List<GadaiHead> gadai = GadaiHeadDAO.getAllByTglGadaiAndStatus(con, 
//                    tglMulaiPicker.getValue().toString(), tglAkhirPicker.getValue().toString(), "Batal Gadai");
//            allGadai.addAll(gadai);
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
    private void searchGadai() {
        try{
            filterData.clear();
            for (GadaiHead temp : allGadai) {
                if (searchField.getText() == null || searchField.getText().equals(""))
                    filterData.add(temp);
                else{
                    if(checkColumn(temp.getNoGadai())||
                        checkColumn(tglLengkap.format(tglSql.parse(temp.getTglGadai())))||
                        checkColumn(tglLengkap.format(tglSql.parse(temp.getTglBatal())))||
                        checkColumn(temp.getSalesBatal())||
                        checkColumn(temp.getKodeSales())||
//                        checkColumn(temp.getKodePelanggan())||
                        checkColumn(temp.getNama())||
                        checkColumn(temp.getAlamat())||
//                        checkColumn(temp.getNoTelp())||
                        checkColumn(gr.format(temp.getTotalBerat()))||
                        checkColumn(gr.format(temp.getTotalPinjaman()))||
                        checkColumn(gr.format(temp.getLamaPinjam()))||
                        checkColumn(gr.format(temp.getBungaPersen()))||
                        checkColumn(gr.format(temp.getBungaKomp()))||
                        checkColumn(gr.format(temp.getBungaRp()))||
                        checkColumn(temp.getKeterangan())||
//                        checkColumn(temp.getStatus())||
                        checkColumn(temp.getJatuhTempo()))
                            filterData.add(temp);
                }
            }
            setTable();
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    private void setTable()throws Exception{
        if(gadaiTable.getRoot()!=null)
            gadaiTable.getRoot().getChildren().clear();
        List<String> groupBy = new ArrayList<>();
        for(GadaiHead temp: filterData){
            if(!groupBy.contains(temp.getTglGadai().substring(0,10)))
                groupBy.add(temp.getTglGadai().substring(0,10));
        }
        double totalBerat = 0;
        double totalPinjaman = 0;
        for(String temp : groupBy){
            GadaiHead head = new GadaiHead();
            head.setNoGadai(temp);
            TreeItem<GadaiHead> parent = new TreeItem<>(head);
            double berat = 0;
            double pinjaman = 0;
            double bungaKomp = 0;
            for(GadaiHead detail: filterData){
                if(temp.equals(detail.getTglGadai().substring(0, 10))){
                    TreeItem<GadaiHead> child = new TreeItem<>(detail);
                    parent.getChildren().addAll(child);
                    berat = berat + detail.getTotalBerat();
                    pinjaman = pinjaman + detail.getTotalPinjaman();
                    bungaKomp = bungaKomp + detail.getBungaKomp();
                    totalBerat = totalBerat + detail.getTotalBerat();
                    totalPinjaman = totalPinjaman + detail.getTotalPinjaman();
                }
            }
            parent.getValue().setTotalBerat(berat);
            parent.getValue().setTotalPinjaman(totalPinjaman);
            parent.getValue().setBungaKomp(bungaKomp);
            root.getChildren().add(parent);
        }
        gadaiTable.setRoot(root);
        totalBeratField.setText(gr.format(totalBerat));
        totalPinjamanField.setText(rp.format(totalPinjaman));
    }   
    
}
