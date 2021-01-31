/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View;

import com.excellentsystem.TokoEmasPadi.DAO.GadaiDetailDAO;
import com.excellentsystem.TokoEmasPadi.DAO.GadaiHeadDAO;
import com.excellentsystem.TokoEmasPadi.DAO.KategoriDAO;
import com.excellentsystem.TokoEmasPadi.Function;
import static com.excellentsystem.TokoEmasPadi.Function.getTableCell;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import static com.excellentsystem.TokoEmasPadi.Main.tglLengkap;
import static com.excellentsystem.TokoEmasPadi.Main.tglSql;
import com.excellentsystem.TokoEmasPadi.Model.GadaiDetail;
import com.excellentsystem.TokoEmasPadi.Model.GadaiHead;
import com.excellentsystem.TokoEmasPadi.View.Dialog.DetailGadaiController;
import java.sql.Connection;
import java.text.ParseException;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
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
 * @author excellent
 */
public class CariGadaiController  {

    @FXML private TableView<GadaiDetail> terimaGadaiTable;
    @FXML private TableColumn<GadaiDetail, String> noGadaiColumn;
    @FXML private TableColumn<GadaiDetail, String> tglGadaiColumn;
    @FXML private TableColumn<GadaiDetail, String> kodeSalesColumn;
    @FXML private TableColumn<GadaiDetail, String> namaColumn;
    @FXML private TableColumn<GadaiDetail, String> alamatColumn;
    @FXML private TableColumn<GadaiDetail, String> namaBarangColumn;
    @FXML private TableColumn<GadaiDetail, Number> beratColumn;
    @FXML private TableColumn<GadaiDetail, Number> totalBeratColumn;
    @FXML private TableColumn<GadaiDetail, Number> totalGadaiColumn;
    @FXML private TableColumn<GadaiDetail, Number> bungaPersenColumn;
    
    @FXML private TextField noGadaiField;
    @FXML private DatePicker tglAwalPicker;
    @FXML private DatePicker tglAkhirPicker;
    @FXML private ComboBox<String> totalGadaiCombo;
    @FXML private TextField totalGadaiField;
    @FXML private TextField namaPelangganField;
    @FXML private TextField alamatPelangganField;
    @FXML private TextField namaBarangField;
    
    private Main mainApp;   
    private final ObservableList<GadaiDetail> allGadai = FXCollections.observableArrayList();
    private final ObservableList<GadaiDetail> filterData = FXCollections.observableArrayList();
    public void initialize() {
        noGadaiColumn.setCellValueFactory(cellData -> cellData.getValue().noGadaiProperty());
        tglGadaiColumn.setCellValueFactory(cellData -> { 
            try {
                return new SimpleStringProperty(tglLengkap.format(tglSql.parse(cellData.getValue().getGadaiHead().getTglGadai())));
            } catch (ParseException ex) {
                return null;
            }
        });
        tglGadaiColumn.setComparator(Function.sortDate(tglLengkap));
        kodeSalesColumn.setCellValueFactory(cellData -> cellData.getValue().getGadaiHead().kodeSalesProperty());
        namaColumn.setCellValueFactory(cellData -> cellData.getValue().getGadaiHead().namaProperty());
        alamatColumn.setCellValueFactory(cellData -> cellData.getValue().getGadaiHead().alamatProperty());
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().namaBarangProperty());
        beratColumn.setCellValueFactory(cellData -> cellData.getValue().beratProperty());
        beratColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        totalBeratColumn.setCellValueFactory(cellData -> cellData.getValue().getGadaiHead().totalBeratProperty());
        totalBeratColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        totalGadaiColumn.setCellValueFactory(cellData -> cellData.getValue().getGadaiHead().totalPinjamanProperty());
        totalGadaiColumn.setCellFactory(col -> getTableCell(rp, "Rp"));
        bungaPersenColumn.setCellValueFactory(cellData -> cellData.getValue().getGadaiHead().bungaPersenProperty());
        bungaPersenColumn.setCellFactory(col -> getTableCell(gr, ""));
        
        tglAwalPicker.setConverter(Function.getTglConverter());
        tglAwalPicker.setValue(LocalDate.parse("2000-01-01"));
        tglAwalPicker.setDayCellFactory((final DatePicker datePicker) -> 
                Function.getDateCellMulai(tglAkhirPicker, LocalDate.parse(sistem.getTglSystem())));
        tglAkhirPicker.setConverter(Function.getTglConverter());
        tglAkhirPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglAkhirPicker.setDayCellFactory((final DatePicker datePicker) -> 
                Function.getDateCellAkhir(tglAwalPicker, LocalDate.parse(sistem.getTglSystem())));
        
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            getGadai();
        });
        rowMenu.getItems().addAll(refresh);
        terimaGadaiTable.setContextMenu(rowMenu);
        terimaGadaiTable.setRowFactory(table -> {
            TableRow<GadaiDetail> row = new TableRow<GadaiDetail>() {
                @Override
                public void updateItem(GadaiDetail item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rowMenu);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem detailTerima = new MenuItem("Detail Terima Gadai");
                        detailTerima.setOnAction((ActionEvent e) -> {
                            detailGadai(item.getGadaiHead());
                        });
                        MenuItem bayar = new MenuItem("Pelunasan Gadai");
                        bayar.setOnAction((ActionEvent e) -> {
                            pelunasanGadai(item.getGadaiHead());
                        });
                        MenuItem cicil = new MenuItem("Cicil Gadai");
                        cicil.setOnAction((ActionEvent e) -> {
                            cicilGadai(item.getGadaiHead());
                        });
                        MenuItem beli = new MenuItem("Pembelian Gadai");
                        beli.setOnAction((ActionEvent e) -> {
                            pembelianGadai(item.getGadaiHead());
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            getGadai();
                        });
                        rowMenu.getItems().add(detailTerima);
                        if(status.equals("pelunasan gadai"))
                            rowMenu.getItems().add(bayar);
                        if(status.equals("cicil gadai"))
                            rowMenu.getItems().add(cicil);
                        if(status.equals("pembelian gadai"))
                            rowMenu.getItems().add(beli);
                        rowMenu.getItems().add(refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)&&mouseEvent.getClickCount() == 2){
                    if(row.getItem()!=null){
                        if(status.equals("pelunasan gadai"))
                            pelunasanGadai(row.getItem().getGadaiHead());
                        if(status.equals("cicil gadai"))
                            cicilGadai(row.getItem().getGadaiHead());
                        if(status.equals("pembelian gadai"))
                            pembelianGadai(row.getItem().getGadaiHead());
                    }
                }
            });
            return row;
        });
        totalGadaiField.setOnKeyReleased((event) -> {
            try{
                String string = totalGadaiField.getText();
                if(string.contains("-"))
                    totalGadaiField.undo();
                else{
                    if(string.indexOf(".")>0){
                        String string2 = string.substring(string.indexOf(".")+1, string.length());
                        if(string2.contains("."))
                            totalGadaiField.undo();
                        else if(!string2.equals("") && Double.parseDouble(string2)!=0)
                            totalGadaiField.setText(rp.format(Double.parseDouble(string.replaceAll(",", ""))));
                    }else{
                        if(!string.equals(""))
                            totalGadaiField.setText(rp.format(Double.parseDouble(string.replaceAll(",", ""))));
                    }
                }
                totalGadaiField.end();
            }catch(Exception e){
                totalGadaiField.undo();
            }
        });
        allGadai.addListener((ListChangeListener.Change<? extends GadaiDetail> change) -> {
            searchGadai();
        });
        noGadaiField.textProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            searchGadai();
        });
        namaPelangganField.textProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            searchGadai();
        });
        alamatPelangganField.textProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            searchGadai();
        });
        totalGadaiField.textProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            searchGadai();
        });
        namaBarangField.textProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            searchGadai();
        });
        filterData.addAll(allGadai);
        terimaGadaiTable.setItems(filterData);
    }    
    public void setMainApp(Main main){
        this.mainApp = main;
        ObservableList<String> allTotalGadai = FXCollections.observableArrayList();
        allTotalGadai.add("Sama Dengan");
        allTotalGadai.add("Kurang Dari");
        allTotalGadai.add("Lebih Dari");
        totalGadaiCombo.setItems(allTotalGadai);
        totalGadaiCombo.getSelectionModel().select("Sama Dengan");
        getGadai();
    }
    private String status  = "pelunasan gadai";
    @FXML
    private void getGadai(){
        try(Connection con = Koneksi.getConnection()){
            List<GadaiDetail> listGadaiDetail = GadaiDetailDAO.getAllByTglGadaiAndStatusLunasAndStatusBatal(
                con, tglAwalPicker.getValue().toString(), tglAkhirPicker.getValue().toString(), "false", "false");
            for(GadaiDetail h : listGadaiDetail){
                h.setGadaiHead(GadaiHeadDAO.get(con, h.getNoGadai()));
            }
            allGadai.clear();
            allGadai.addAll(listGadaiDetail);
            terimaGadaiTable.refresh();
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    public void setStatus(String status){
        this.status = status;
    }
    @FXML
    private void searchGadai() {
        try{
            filterData.clear();
            for (GadaiDetail d : allGadai) {
                if (noGadaiField.getText() == null || noGadaiField.getText().equals("") || 
                        (d.getNoGadai()!=null && d.getNoGadai().toLowerCase().contains(noGadaiField.getText().toLowerCase()))){
                    if (namaPelangganField.getText() == null || namaPelangganField.getText().equals("") || 
                            (d.getGadaiHead().getNama()!=null && d.getGadaiHead().getNama().toLowerCase().contains(namaPelangganField.getText().toLowerCase()))){
                        if (alamatPelangganField.getText() == null || alamatPelangganField.getText().equals("") || 
                                (d.getGadaiHead().getAlamat()!=null && d.getGadaiHead().getAlamat().toLowerCase().contains(alamatPelangganField.getText().toLowerCase()))){
                            if (namaBarangField.getText() == null || namaBarangField.getText().equals("") || 
                                    (d.getNamaBarang()!=null && d.getNamaBarang().toLowerCase().contains(namaBarangField.getText().toLowerCase()))){
                                if (totalGadaiField.getText() == null || totalGadaiField.getText().equals("")){
                                    filterData.add(d);
                                }else{
                                    if(totalGadaiCombo.getSelectionModel().getSelectedItem().equals("Sama Dengan") && 
                                            d.getGadaiHead().getTotalPinjaman()==Double.parseDouble(totalGadaiField.getText().replaceAll(",", "")))
                                        filterData.add(d);
                                    else if(totalGadaiCombo.getSelectionModel().getSelectedItem().equals("Lebih Dari") && 
                                            d.getGadaiHead().getTotalPinjaman()>=Double.parseDouble(totalGadaiField.getText().replaceAll(",", "")))
                                        filterData.add(d);
                                    else if(totalGadaiCombo.getSelectionModel().getSelectedItem().equals("Kurang Dari") && 
                                            d.getGadaiHead().getTotalPinjaman()<=Double.parseDouble(totalGadaiField.getText().replaceAll(",", "")))
                                        filterData.add(d);
                                }
                            }
                        }
                    }
                }
            }
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    private void detailGadai(GadaiHead g){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage ,child, "View/Dialog/DetailGadai.fxml");
        DetailGadaiController controller = loader.getController();
        controller.setMainApp(mainApp, child);
        controller.setGadai(g.getNoGadai());
    }
    private void pelunasanGadai(GadaiHead g){
        try(Connection con = Koneksi.getConnection()){
            g = GadaiHeadDAO.get(con, g.getNoGadai());
            if(g==null)
                mainApp.showMessage(Modality.NONE, "Warning", "No Gadai tidak ditemukan");
            else if(g.getStatusLunas().equals("true"))
                mainApp.showMessage(Modality.NONE, "Warning", "Gadai sudah pernah dilunasi");
            else if(g.getStatusBatal().equals("true"))
                mainApp.showMessage(Modality.NONE, "Warning", "Gadai sudah pernah dibatalkan");
            else{
                List<GadaiDetail> listGadai = GadaiDetailDAO.getAll(con, g.getNoGadai());
                for(GadaiDetail d : listGadai){
                    d.setKategori(KategoriDAO.get(con, d.getKodeKategori()));
                }
                g.setListGadaiDetail(listGadai);
                FXMLLoader loader = mainApp.changeStage("View/PelunasanGadai.fxml");
                PelunasanGadaiController controller = loader.getController();
                controller.setMainApp(mainApp);
                mainApp.setTitle("Pelunasan Gadai");
                controller.setGadai(g);
            }
        }catch(Exception e){
            e.printStackTrace();
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    private void cicilGadai(GadaiHead g){
        try(Connection con = Koneksi.getConnection()){
            g = GadaiHeadDAO.get(con, g.getNoGadai());
            if(g==null)
                mainApp.showMessage(Modality.NONE, "Warning", "No Gadai tidak ditemukan");
            else if(g.getStatusLunas().equals("true"))
                mainApp.showMessage(Modality.NONE, "Warning", "Gadai sudah pernah dilunasi");
            else if(g.getStatusBatal().equals("true"))
                mainApp.showMessage(Modality.NONE, "Warning", "Gadai sudah pernah dibatalkan");
            else{
                List<GadaiDetail> listGadai = GadaiDetailDAO.getAll(con, g.getNoGadai());
                for(GadaiDetail d : listGadai){
                    d.setKategori(KategoriDAO.get(con, d.getKodeKategori()));
                }
                g.setListGadaiDetail(listGadai);
                FXMLLoader loader = mainApp.changeStage("View/CicilGadai.fxml");
                CicilGadaiController controller = loader.getController();
                controller.setMainApp(mainApp);
                mainApp.setTitle("Cicil Gadai");
                controller.setGadai(g);
            }
        }catch(Exception e){
            e.printStackTrace();
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    private void pembelianGadai(GadaiHead g){
        try(Connection con = Koneksi.getConnection()){
            g = GadaiHeadDAO.get(con, g.getNoGadai());
            if(g==null)
                mainApp.showMessage(Modality.NONE, "Warning", "No Gadai tidak ditemukan");
            else if(g.getStatusLunas().equals("true"))
                mainApp.showMessage(Modality.NONE, "Warning", "Gadai sudah pernah dilunasi");
            else if(g.getStatusBatal().equals("true"))
                mainApp.showMessage(Modality.NONE, "Warning", "Gadai sudah pernah dibatalkan");
            else{
                List<GadaiDetail> listGadai = GadaiDetailDAO.getAll(con, g.getNoGadai());
                for(GadaiDetail d : listGadai){
                    d.setKategori(KategoriDAO.get(con, d.getKodeKategori()));
                }
                g.setListGadaiDetail(listGadai);
                FXMLLoader loader = mainApp.changeStage("View/PembelianGadai.fxml");
                PembelianGadaiController controller = loader.getController();
                controller.setMainApp(mainApp);
                mainApp.setTitle("Pembelian Gadai");
                controller.setGadai(g);
            }
        }catch(Exception e){
            e.printStackTrace();
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
}
