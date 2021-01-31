/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Report;

import com.excellentsystem.TokoEmasPadi.DAO.PembelianDetailDAO;
import com.excellentsystem.TokoEmasPadi.DAO.PembelianHeadDAO;
import com.excellentsystem.TokoEmasPadi.Function;
import static com.excellentsystem.TokoEmasPadi.Function.getTreeTableCell;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import static com.excellentsystem.TokoEmasPadi.Main.tglBarang;
import static com.excellentsystem.TokoEmasPadi.Main.tglKode;
import static com.excellentsystem.TokoEmasPadi.Main.tglLengkap;
import static com.excellentsystem.TokoEmasPadi.Main.tglNormal;
import static com.excellentsystem.TokoEmasPadi.Main.tglSql;
import com.excellentsystem.TokoEmasPadi.Model.PembelianDetail;
import com.excellentsystem.TokoEmasPadi.Model.PembelianHead;
import com.excellentsystem.TokoEmasPadi.Model.User;
import com.excellentsystem.TokoEmasPadi.PrintOut.PrintOut;
import com.excellentsystem.TokoEmasPadi.Service.Service;
import com.excellentsystem.TokoEmasPadi.View.Dialog.DetailPembelianController;
import com.excellentsystem.TokoEmasPadi.View.Dialog.VerifikasiController;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Yunaz
 */
public class LaporanPembelianController {
    
    @FXML
    private GridPane gridPane;
    @FXML 
    private HBox hbox;
    @FXML
    private TreeTableView<PembelianDetail> pembelianTable;
    @FXML
    private TreeTableColumn<PembelianDetail, String> noPembelianColumn;
    @FXML
    private TreeTableColumn<PembelianDetail, String> tglPembelianColumn;
    @FXML
    private TreeTableColumn<PembelianDetail, String> kodeSalesColumn;
    @FXML
    private TreeTableColumn<PembelianDetail, String> namaColumn;
    @FXML
    private TreeTableColumn<PembelianDetail, String> alamatColumn;
    @FXML
    private TreeTableColumn<PembelianDetail, Number> totalBeratColumn;
    @FXML
    private TreeTableColumn<PembelianDetail, Number> totalPembelianColumn;
    @FXML
    private TreeTableColumn<PembelianDetail, String> catatanColumn;
    
    @FXML
    private TreeTableColumn<PembelianDetail, String> kodeKategoriColumn;
    @FXML
    private TreeTableColumn<PembelianDetail, String> namaBarangColumn;
    @FXML
    private TreeTableColumn<PembelianDetail, Number> beratColumn;
    @FXML
    private TreeTableColumn<PembelianDetail, String> kondisiColumn;
    @FXML
    private TreeTableColumn<PembelianDetail, String> statusSuratColumn;
    @FXML
    private TreeTableColumn<PembelianDetail, Number> hargaKompColumn;
    @FXML
    private TreeTableColumn<PembelianDetail, Number> hargaBeliColumn;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> groupByCombo;
    @FXML
    private Label totalBeratField;
    @FXML
    private Label totalPembelianField;
    @FXML
    private DatePicker tglMulaiPicker;
    @FXML
    private DatePicker tglAkhirPicker;
    final TreeItem<PembelianDetail> root = new TreeItem<>();
    private ObservableList<PembelianDetail> allPembelian = FXCollections.observableArrayList();
    private ObservableList<PembelianDetail> filterData = FXCollections.observableArrayList();
    private Main mainApp;
    
    public void initialize() {
        noPembelianColumn.setCellValueFactory(param -> param.getValue().getValue().noPembelianProperty());
        tglPembelianColumn.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(
                        tglLengkap.format(
                                tglSql.parse(cellData.getValue().getValue().getPembelianHead().getTglPembelian())));
            } catch (Exception ex) {
                return null;
            }
        });
        kodeSalesColumn.setCellValueFactory(param -> param.getValue().getValue().getPembelianHead().kodeSalesProperty());
        namaColumn.setCellValueFactory(param -> param.getValue().getValue().getPembelianHead().namaProperty());
        alamatColumn.setCellValueFactory(param -> param.getValue().getValue().getPembelianHead().alamatProperty());
        totalBeratColumn.setCellValueFactory(param -> param.getValue().getValue().getPembelianHead().totalBeratProperty());
        totalBeratColumn.setCellFactory(col -> getTreeTableCell(gr, false, "gr"));
        totalPembelianColumn.setCellValueFactory(param -> param.getValue().getValue().getPembelianHead().totalPembelianProperty());
        totalPembelianColumn.setCellFactory(col -> getTreeTableCell(rp, false, "Rp"));
        catatanColumn.setCellValueFactory(param -> param.getValue().getValue().getPembelianHead().catatanProperty());
        
        namaBarangColumn.setCellValueFactory(param -> param.getValue().getValue().namaBarangProperty());
        kodeKategoriColumn.setCellValueFactory(param -> param.getValue().getValue().kodeKategoriProperty());
        kondisiColumn.setCellValueFactory(param -> param.getValue().getValue().kondisiProperty());
        statusSuratColumn.setCellValueFactory(param -> param.getValue().getValue().statusSuratProperty());
        beratColumn.setCellValueFactory(param -> param.getValue().getValue().beratProperty());
        beratColumn.setCellFactory(col -> getTreeTableCell(gr, false, "gr"));
        hargaKompColumn.setCellValueFactory(param -> param.getValue().getValue().hargaKompProperty());
        hargaKompColumn.setCellFactory(col -> getTreeTableCell(rp, false, "Rp"));
        hargaBeliColumn.setCellValueFactory(param -> param.getValue().getValue().hargaBeliProperty());
        hargaBeliColumn.setCellFactory(col -> getTreeTableCell(rp, false, "Rp"));
        
        tglMulaiPicker.setConverter(Function.getTglConverter());
        tglMulaiPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglMulaiPicker.setDayCellFactory((final DatePicker datePicker)
                -> Function.getDateCellMulai(tglAkhirPicker, LocalDate.parse(sistem.getTglSystem())));
        tglAkhirPicker.setConverter(Function.getTglConverter());
        tglAkhirPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglAkhirPicker.setDayCellFactory((final DatePicker datePicker)
                -> Function.getDateCellAkhir(tglMulaiPicker, LocalDate.parse(sistem.getTglSystem())));
        
        allPembelian.addListener((ListChangeListener.Change<? extends PembelianDetail> change) -> {
            searchPembelianDetail();
        });
        searchField.textProperty().addListener(
                (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    searchPembelianDetail();
                });
        filterData.addAll(allPembelian);
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent e) -> {
            getPembelianHead();
        });
        rowMenu.getItems().addAll(refresh);
        pembelianTable.setContextMenu(rowMenu);
        pembelianTable.setRowFactory(table -> {
            TreeTableRow<PembelianDetail> row = new TreeTableRow<PembelianDetail>() {
                @Override
                public void updateItem(PembelianDetail item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rowMenu);
                    } else {
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem view = new MenuItem("Detail Pembelian");
                        view.setOnAction((ActionEvent event) -> {
                            viewPembelian(item.getPembelianHead());
                        });
                        MenuItem batal = new MenuItem("Batal Pembelian");
                        batal.setOnAction((ActionEvent event) -> {
                            deletePembelian(item.getPembelianHead());
                        });
                        MenuItem print = new MenuItem("Print Bukti Pembelian");
                        print.setOnAction((ActionEvent e) -> {
                            printPembelian(item.getPembelianHead());
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            getPembelianHead();
                        });
                        if (item.getPembelianHead().getTglPembelian() != null) {
                            rowMenu.getItems().addAll(view, batal);
                        }
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
        ObservableList<String> groupBy = FXCollections.observableArrayList();
        groupBy.add("No Pembelian");
        groupBy.add("Tanggal");
        groupBy.add("Sales");
        groupBy.add("Kategori Barang");
        groupBy.add("Kondisi");
        groupByCombo.setItems(groupBy);
        groupByCombo.getSelectionModel().select("No Pembelian");
        getPembelianHead();
        if (Main.user == null) {
            hbox.setVisible(false);
            gridPane.getRowConstraints().get(2).setMinHeight(0);
            gridPane.getRowConstraints().get(2).setPrefHeight(0);
            gridPane.getRowConstraints().get(2).setMaxHeight(0);
        }
    }
    
    @FXML
    private void getPembelianHead() {
        try (Connection con = Koneksi.getConnection()) {
            allPembelian.clear();
            List<PembelianDetail> listPembelianDetail = PembelianDetailDAO.getAllByTglBeliAndStatus(con,
                    tglMulaiPicker.getValue().toString(), tglAkhirPicker.getValue().toString(), "true");
            List<PembelianHead> listPembelianHead = PembelianHeadDAO.getAllByTglBeliAndStatus(con,
                    tglMulaiPicker.getValue().toString(), tglAkhirPicker.getValue().toString(), "true");
            for (PembelianDetail d : listPembelianDetail) {
                for (PembelianHead h : listPembelianHead) {
                    if (d.getNoPembelian().equals(h.getNoPembelian())) {
                        d.setPembelianHead(h);
                    }
                }
            }
            allPembelian.addAll(listPembelianDetail);
        } catch (Exception e) {
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    
    private Boolean checkColumn(String column) {
        if (column != null) {
            if (column.toLowerCase().contains(searchField.getText().toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    
    private void searchPembelianDetail() {
        try {
            filterData.clear();
            for (PembelianDetail temp : allPembelian) {
                if (searchField.getText() == null || searchField.getText().equals("")) {
                    filterData.add(temp);
                } else {
                    if (checkColumn(temp.getNoPembelian())
                            || checkColumn(tglLengkap.format(tglSql.parse(temp.getPembelianHead().getTglPembelian())))
                            || checkColumn(temp.getPembelianHead().getKodeSales())
                            || checkColumn(temp.getPembelianHead().getNama())
                            || checkColumn(temp.getPembelianHead().getAlamat())
                            || checkColumn(gr.format(temp.getPembelianHead().getTotalBerat()))
                            || checkColumn(gr.format(temp.getPembelianHead().getTotalPembelian()))
                            || checkColumn(temp.getPembelianHead().getCatatan())
                            || checkColumn(temp.getNamaBarang())
                            || checkColumn(temp.getKodeKategori())
                            || checkColumn(gr.format(temp.getBerat()))
                            || checkColumn(temp.getKondisi())
                            || checkColumn(gr.format(temp.getHargaBeli()))
                            || checkColumn(gr.format(temp.getHargaKomp()))) {
                        filterData.add(temp);
                    }
                }
            }
            setTable();
        } catch (Exception e) {
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    
    private void setTable() throws Exception {
        if (pembelianTable.getRoot() != null) {
            pembelianTable.getRoot().getChildren().clear();
        }
        List<String> groupBy = new ArrayList<>();
        for (PembelianDetail temp : filterData) {
            if (groupByCombo.getSelectionModel().getSelectedItem().equals("No Pembelian")) {
                if (!groupBy.contains(temp.getNoPembelian())) {
                    groupBy.add(temp.getNoPembelian());
                }
            } else if (groupByCombo.getSelectionModel().getSelectedItem().equals("Tanggal")) {
                if (!groupBy.contains(temp.getPembelianHead().getTglPembelian().substring(0, 10))) {
                    groupBy.add(temp.getPembelianHead().getTglPembelian().substring(0, 10));
                }
            } else if (groupByCombo.getSelectionModel().getSelectedItem().equals("Sales")) {
                if (!groupBy.contains(temp.getPembelianHead().getKodeSales())) {
                    groupBy.add(temp.getPembelianHead().getKodeSales());
                }
            } else if (groupByCombo.getSelectionModel().getSelectedItem().equals("Kategori Barang")) {
                if (!groupBy.contains(temp.getKodeKategori())) {
                    groupBy.add(temp.getKodeKategori());
                }
            }else if (groupByCombo.getSelectionModel().getSelectedItem().equals("Kondisi")) {
                if (!groupBy.contains(temp.getKondisi())) {
                    groupBy.add(temp.getKondisi());
                }
            }
        }
        double totalBeratKotor = 0;
        double totalBeli = 0;
        for (String temp : groupBy) {
            PembelianDetail head = new PembelianDetail();
            if (groupByCombo.getSelectionModel().getSelectedItem().equals("Tanggal")) {
                head.setNoPembelian(tglNormal.format(tglBarang.parse(temp)));
            } else {
                head.setNoPembelian(temp);
            }
            PembelianHead p = new PembelianHead();
            p.setKodeSales("");
            head.setPembelianHead(p);
            TreeItem<PembelianDetail> parent = new TreeItem<>(head);
            double beratKotor = 0;
            double hargaKomp = 0;
            double hargaBeli = 0;
            for (PembelianDetail detail : filterData) {
                if (groupByCombo.getSelectionModel().getSelectedItem().equals("No Pembelian")) {
                    if (temp.equals(detail.getNoPembelian())) {
                        TreeItem<PembelianDetail> child = new TreeItem<>(detail);
                        parent.getChildren().addAll(child);
                        beratKotor = beratKotor + detail.getBerat();
                        hargaKomp = hargaKomp + detail.getHargaKomp();
                        hargaBeli = hargaBeli + detail.getHargaBeli();
                        totalBeratKotor = totalBeratKotor + detail.getBerat();
                        totalBeli = totalBeli + detail.getHargaBeli();
                    }
                } else if (groupByCombo.getSelectionModel().getSelectedItem().equals("Tanggal")) {
                    if (temp.equals(detail.getPembelianHead().getTglPembelian().substring(0, 10))) {
                        TreeItem<PembelianDetail> child = new TreeItem<>(detail);
                        parent.getChildren().addAll(child);
                        beratKotor = beratKotor + detail.getBerat();
                        hargaKomp = hargaKomp + detail.getHargaKomp();
                        hargaBeli = hargaBeli + detail.getHargaBeli();
                        totalBeratKotor = totalBeratKotor + detail.getBerat();
                        totalBeli = totalBeli + detail.getHargaBeli();
                    }
                } else if (groupByCombo.getSelectionModel().getSelectedItem().equals("Sales")) {
                    if (temp.equals(detail.getPembelianHead().getKodeSales())) {
                        TreeItem<PembelianDetail> child = new TreeItem<>(detail);
                        parent.getChildren().addAll(child);
                        beratKotor = beratKotor + detail.getBerat();
                        hargaKomp = hargaKomp + detail.getHargaKomp();
                        hargaBeli = hargaBeli + detail.getHargaBeli();
                        totalBeratKotor = totalBeratKotor + detail.getBerat();
                        totalBeli = totalBeli + detail.getHargaBeli();
                    }
                } else if (groupByCombo.getSelectionModel().getSelectedItem().equals("Kategori Barang")) {
                    if (temp.equals(detail.getKodeKategori())) {
                        TreeItem<PembelianDetail> child = new TreeItem<>(detail);
                        parent.getChildren().addAll(child);
                        beratKotor = beratKotor + detail.getBerat();
                        hargaKomp = hargaKomp + detail.getHargaKomp();
                        hargaBeli = hargaBeli + detail.getHargaBeli();
                        totalBeratKotor = totalBeratKotor + detail.getBerat();
                        totalBeli = totalBeli + detail.getHargaBeli();
                    }
                }else if (groupByCombo.getSelectionModel().getSelectedItem().equals("Kondisi")) {
                    if (temp.equals(detail.getKondisi())) {
                        TreeItem<PembelianDetail> child = new TreeItem<>(detail);
                        parent.getChildren().addAll(child);
                        beratKotor = beratKotor + detail.getBerat();
                        hargaKomp = hargaKomp + detail.getHargaKomp();
                        hargaBeli = hargaBeli + detail.getHargaBeli();
                        totalBeratKotor = totalBeratKotor + detail.getBerat();
                        totalBeli = totalBeli + detail.getHargaBeli();
                    }
                }
            }
            parent.getValue().setBerat(beratKotor);
            parent.getValue().setHargaBeli(hargaBeli);
            parent.getValue().setHargaKomp(hargaKomp);
            root.getChildren().add(parent);
        }
        pembelianTable.setRoot(root);
        totalBeratField.setText(gr.format(totalBeratKotor));
        totalPembelianField.setText(rp.format(totalBeli));
    }
    
    private void viewPembelian(PembelianHead p) {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailPembelian.fxml");
        DetailPembelianController controller = loader.getController();
        controller.setMainApp(mainApp, stage);
        controller.setPembelian(p.getNoPembelian());
    }
    
    private void printPembelian(PembelianHead p) {
        try(Connection con = Koneksi.getConnection()){
            for (PembelianDetail d : p.getListPembelianDetail()) {
                d.setPembelianHead(p);
            }
            PrintOut printOut = new PrintOut();
            printOut.printBuktiPembelian(p.getListPembelianDetail());
        } catch (Exception e) {
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    
    private void deletePembelian(PembelianHead p) {
        try {
            if (!p.getNoPembelian().substring(3, 9).equals(tglKode.format(tglBarang.parse(sistem.getTglSystem())))) {
                mainApp.showMessage(Modality.NONE, "Warning", "Pembelian sudah berbeda tanggal");
            } else {
                Stage stage = new Stage();
                FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/Verifikasi.fxml");
                VerifikasiController controller = loader.getController();
                controller.setMainApp(mainApp, stage);
                controller.keteranganLabel.setText("Batal Pembelian : " + p.getNoPembelian() + "\nVerifikasi : ");
                controller.password.setOnKeyPressed((KeyEvent keyEvent) -> {
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        stage.close();
                        User user = Function.verifikasi(controller.password.getText(), "Batal Pembelian");
                        if (user != null) {
                            try (Connection con = Koneksi.getConnection()) {
                                p.setTglBatal(Function.getSystemDate());
                                p.setUserBatal(user.getUsername());
                                p.setStatus("false");
                                String status = Service.batalPembelian(con, p);
                                getPembelianHead();
                                if (status.equals("true")) {
                                    mainApp.showMessage(Modality.NONE, "Success", "Data pembelian berhasil dibatal");
                                } else {
                                    mainApp.showMessage(Modality.NONE, "Error", status);
                                }
                            } catch (Exception ex) {
                                mainApp.showMessage(Modality.NONE, "Error", ex.toString());
                            }
                        } else {
                            mainApp.showMessage(Modality.NONE, "Warning", "Verifikasi Salah");
                        }
                    }
                });
            }
        } catch (Exception ex) {
            mainApp.showMessage(Modality.NONE, "Error", ex.toString());
        }
    }
}
