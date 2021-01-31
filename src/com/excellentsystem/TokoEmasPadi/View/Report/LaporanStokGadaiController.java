/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Report;

import com.excellentsystem.TokoEmasPadi.DAO.GadaiHeadDAO;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;

/**
 * FXML Controller class
 *
 * @author Yunaz
 */
public class LaporanStokGadaiController {

    @FXML
    private GridPane gridPane;
    @FXML 
    private HBox hbox;
    @FXML private TreeTableView<GadaiHead> gadaiTable;
    @FXML private TreeTableColumn<GadaiHead, String> noGadaiColumn;
    @FXML private TreeTableColumn<GadaiHead, String> tglGadaiColumn;
    @FXML private TreeTableColumn<GadaiHead, String> salesTerimaColumn;
    @FXML private TreeTableColumn<GadaiHead, String> namaColumn;
    @FXML private TreeTableColumn<GadaiHead, String> alamatColumn;
    @FXML private TreeTableColumn<GadaiHead, String> keteranganColumn;
    @FXML private TreeTableColumn<GadaiHead, Number> totalBeratColumn;
    @FXML private TreeTableColumn<GadaiHead, Number> totalPinjamanColumn;
    @FXML private TreeTableColumn<GadaiHead, Number> lamaPinjamColumn;
    @FXML private TreeTableColumn<GadaiHead, Number> bungaPersenColumn;
    @FXML private TreeTableColumn<GadaiHead, Number> bungaKompColumn;
    
    @FXML private TextField searchField;
    @FXML private Label totalBeratField;
    @FXML private Label totalPinjamanField;
    @FXML private Label totalBungaKompField;
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
        salesTerimaColumn.setCellValueFactory( param -> param.getValue().getValue().kodeSalesProperty());
        namaColumn.setCellValueFactory( param -> param.getValue().getValue().namaProperty());
        alamatColumn.setCellValueFactory( param -> param.getValue().getValue().alamatProperty());
        keteranganColumn.setCellValueFactory( param -> param.getValue().getValue().keteranganProperty());
        totalBeratColumn.setCellValueFactory(param -> param.getValue().getValue().totalBeratProperty());
        totalBeratColumn.setCellFactory(col -> getTreeTableCell(gr, false, "gr"));
        totalPinjamanColumn.setCellValueFactory(param -> param.getValue().getValue().totalPinjamanProperty());
        totalPinjamanColumn.setCellFactory(col -> getTreeTableCell(rp, false, "Rp"));
        lamaPinjamColumn.setCellValueFactory(param -> param.getValue().getValue().lamaPinjamProperty());
        lamaPinjamColumn.setCellFactory(col -> getTreeTableCell(rp, true, ""));
        bungaPersenColumn.setCellValueFactory(param -> param.getValue().getValue().bungaPersenProperty());
        bungaPersenColumn.setCellFactory(col -> getTreeTableCell(gr, true, ""));
        bungaKompColumn.setCellValueFactory(param -> param.getValue().getValue().bungaKompProperty());
        bungaKompColumn.setCellFactory(col -> getTreeTableCell(rp, false, "Rp"));
        
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
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent e) -> {
            getGadai();
        });
        rowMenu.getItems().addAll(refresh);
        gadaiTable.setContextMenu(rowMenu);
        gadaiTable.setRowFactory(table -> {
            TreeTableRow<GadaiHead> row = new TreeTableRow<GadaiHead>() {
                @Override
                public void updateItem(GadaiHead item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rowMenu);
                    } else {
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            getGadai();
                        });
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
        getGadai();
        if (Main.user == null) {
            hbox.setVisible(false);
            gridPane.getRowConstraints().get(2).setMinHeight(0);
            gridPane.getRowConstraints().get(2).setPrefHeight(0);
            gridPane.getRowConstraints().get(2).setMaxHeight(0);
        }
    }  
    @FXML
    private void getGadai(){
        try(Connection con = Koneksi.getConnection()){
            allGadai.clear();
            List<GadaiHead> gadai = GadaiHeadDAO.getAllByTglGadaiAndStatusLunasAndStatusBatal(con, 
                    tglMulaiPicker.getValue().toString(), tglAkhirPicker.getValue().toString(), "false", "false");
            allGadai.addAll(gadai);
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
                        checkColumn(temp.getKodeSales())||
                        checkColumn(temp.getNama())||
                        checkColumn(temp.getAlamat())||
                        checkColumn(gr.format(temp.getTotalBerat()))||
                        checkColumn(gr.format(temp.getTotalPinjaman()))||
                        checkColumn(gr.format(temp.getLamaPinjam()))||
                        checkColumn(gr.format(temp.getBungaPersen()))||
                        checkColumn(gr.format(temp.getBungaKomp()))||
                        checkColumn(temp.getKeterangan())||
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
        double totalBungaKomp = 0;
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
                    totalBungaKomp = totalBungaKomp + detail.getBungaKomp();
                }
            }
            parent.getValue().setTotalBerat(berat);
            parent.getValue().setTotalPinjaman(pinjaman);
            parent.getValue().setBungaKomp(bungaKomp);
            root.getChildren().add(parent);
        }
        gadaiTable.setRoot(root);
        totalBeratField.setText(gr.format(totalBerat));
        totalPinjamanField.setText(rp.format(totalPinjaman));
        totalBungaKompField.setText(rp.format(totalBungaKomp));
    }     
}
