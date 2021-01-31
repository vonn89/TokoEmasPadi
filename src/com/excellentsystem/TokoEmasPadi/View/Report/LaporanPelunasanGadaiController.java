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
import static com.excellentsystem.TokoEmasPadi.Main.tglBarang;
import static com.excellentsystem.TokoEmasPadi.Main.tglLengkap;
import static com.excellentsystem.TokoEmasPadi.Main.tglNormal;
import static com.excellentsystem.TokoEmasPadi.Main.tglSql;
import com.excellentsystem.TokoEmasPadi.Model.GadaiHead;
import com.excellentsystem.TokoEmasPadi.Model.User;
import com.excellentsystem.TokoEmasPadi.PrintOut.PrintOut;
import com.excellentsystem.TokoEmasPadi.Service.Service;
import com.excellentsystem.TokoEmasPadi.View.Dialog.DetailPelunasanGadaiController;
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
public class LaporanPelunasanGadaiController {

    @FXML
    private GridPane gridPane;
    @FXML 
    private HBox hbox;
    @FXML
    private TreeTableView<GadaiHead> gadaiTable;
    @FXML
    private TreeTableColumn<GadaiHead, String> noGadaiColumn;
    @FXML
    private TreeTableColumn<GadaiHead, String> tglGadaiColumn;
    @FXML
    private TreeTableColumn<GadaiHead, String> tglLunasColumn;
    @FXML
    private TreeTableColumn<GadaiHead, String> salesTerimaColumn;
    @FXML
    private TreeTableColumn<GadaiHead, String> salesLunasColumn;
    @FXML
    private TreeTableColumn<GadaiHead, String> namaColumn;
    @FXML
    private TreeTableColumn<GadaiHead, String> alamatColumn;
    @FXML
    private TreeTableColumn<GadaiHead, String> keteranganColumn;
    @FXML
    private TreeTableColumn<GadaiHead, Number> totalBeratColumn;
    @FXML
    private TreeTableColumn<GadaiHead, Number> totalPinjamanColumn;
    @FXML
    private TreeTableColumn<GadaiHead, Number> lamaPinjamColumn;
    @FXML
    private TreeTableColumn<GadaiHead, Number> bungaPersenColumn;
    @FXML
    private TreeTableColumn<GadaiHead, Number> bungaKompColumn;
    @FXML
    private TreeTableColumn<GadaiHead, Number> bungaRpColumn;

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> groupByCombo;
    @FXML
    private Label totalBeratField;
    @FXML
    private Label totalPinjamanField;
    @FXML
    private Label totalBungaKompField;
    @FXML
    private Label totalBungaRpField;
    @FXML
    private DatePicker tglMulaiPicker;
    @FXML
    private DatePicker tglAkhirPicker;
    final TreeItem<GadaiHead> root = new TreeItem<>();
    private ObservableList<GadaiHead> allGadai = FXCollections.observableArrayList();
    private ObservableList<GadaiHead> filterData = FXCollections.observableArrayList();
    private Main mainApp;

    public void initialize() {
        noGadaiColumn.setCellValueFactory(param -> param.getValue().getValue().noGadaiProperty());
        tglGadaiColumn.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(
                        tglLengkap.format(
                                tglSql.parse(cellData.getValue().getValue().getTglGadai())));
            } catch (Exception ex) {
                return null;
            }
        });
        tglLunasColumn.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(
                        tglLengkap.format(
                                tglSql.parse(cellData.getValue().getValue().getTglLunas())));
            } catch (Exception ex) {
                return null;
            }
        });
        salesTerimaColumn.setCellValueFactory(param -> param.getValue().getValue().kodeSalesProperty());
        salesLunasColumn.setCellValueFactory(param -> param.getValue().getValue().salesLunasProperty());
        namaColumn.setCellValueFactory(param -> param.getValue().getValue().namaProperty());
        alamatColumn.setCellValueFactory(param -> param.getValue().getValue().alamatProperty());
        keteranganColumn.setCellValueFactory(param -> param.getValue().getValue().keteranganProperty());
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
        bungaRpColumn.setCellValueFactory(param -> param.getValue().getValue().bungaRpProperty());
        bungaRpColumn.setCellFactory(col -> getTreeTableCell(rp, false, "Rp"));

        tglMulaiPicker.setConverter(Function.getTglConverter());
        tglMulaiPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglMulaiPicker.setDayCellFactory((final DatePicker datePicker)
                -> Function.getDateCellMulai(tglAkhirPicker, LocalDate.parse(sistem.getTglSystem())));
        tglAkhirPicker.setConverter(Function.getTglConverter());
        tglAkhirPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglAkhirPicker.setDayCellFactory((final DatePicker datePicker)
                -> Function.getDateCellAkhir(tglMulaiPicker, LocalDate.parse(sistem.getTglSystem())));

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
                        MenuItem print = new MenuItem("Print Surat Pelunasan Gadai");
                        print.setOnAction((ActionEvent e) -> {
                            printPelunasanGadai(item);
                        });
                        MenuItem batal = new MenuItem("Batal Pelunasan Gadai");
                        batal.setOnAction((ActionEvent event) -> {
                            deletePelunasanGadai(item);
                        });
                        MenuItem view = new MenuItem("Detail Pelunasan Gadai");
                        view.setOnAction((ActionEvent event) -> {
                            viewPelunasanGadai(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            getGadai();
                        });
                        if(item.getTglGadai()!=null)
                            rowMenu.getItems().addAll(view, batal);
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
        groupBy.add("Tanggal Terima");
        groupBy.add("Tanggal Lunas");
        groupBy.add("Sales Terima");
        groupBy.add("Sales Lunas");
        groupByCombo.setItems(groupBy);
        groupByCombo.getSelectionModel().select("Tanggal Lunas");
        getGadai();
        if (Main.user == null) {
            hbox.setVisible(false);
            gridPane.getRowConstraints().get(2).setMinHeight(0);
            gridPane.getRowConstraints().get(2).setPrefHeight(0);
            gridPane.getRowConstraints().get(2).setMaxHeight(0);
        }
    }

    @FXML
    private void getGadai() {
        try (Connection con = Koneksi.getConnection()) {
            allGadai.clear();
            List<GadaiHead> gadai = GadaiHeadDAO.getAllByTglLunas(con,
                    tglMulaiPicker.getValue().toString(), tglAkhirPicker.getValue().toString());
            allGadai.addAll(gadai);
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

    private void searchGadai() {
        try {
            filterData.clear();
            for (GadaiHead temp : allGadai) {
                if (searchField.getText() == null || searchField.getText().equals("")) {
                    filterData.add(temp);
                } else {
                    if (checkColumn(temp.getNoGadai())
                            || checkColumn(tglLengkap.format(tglSql.parse(temp.getTglGadai())))
                            || checkColumn(tglLengkap.format(tglSql.parse(temp.getTglLunas())))
                            || checkColumn(temp.getKodeSales())
                            || checkColumn(temp.getSalesLunas())
                            || checkColumn(temp.getNama())
                            || checkColumn(temp.getAlamat())
                            || checkColumn(gr.format(temp.getTotalBerat()))
                            || checkColumn(gr.format(temp.getTotalPinjaman()))
                            || checkColumn(gr.format(temp.getLamaPinjam()))
                            || checkColumn(gr.format(temp.getBungaPersen()))
                            || checkColumn(gr.format(temp.getBungaKomp()))
                            || checkColumn(gr.format(temp.getBungaRp()))
                            || checkColumn(temp.getKeterangan())
                            || checkColumn(temp.getJatuhTempo())) {
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
        if (gadaiTable.getRoot() != null) {
            gadaiTable.getRoot().getChildren().clear();
        }
        List<String> groupBy = new ArrayList<>();
        for (GadaiHead temp : filterData) {
            if (groupByCombo.getSelectionModel().getSelectedItem().equals("Tanggal Terima")) {
                if (!groupBy.contains(temp.getTglGadai().substring(0, 10))) {
                    groupBy.add(temp.getTglGadai().substring(0, 10));
                }
            } else if (groupByCombo.getSelectionModel().getSelectedItem().equals("Tanggal Lunas")) {
                if (!groupBy.contains(temp.getTglLunas().substring(0, 10))) {
                    groupBy.add(temp.getTglLunas().substring(0, 10));
                }
            } else if (groupByCombo.getSelectionModel().getSelectedItem().equals("Sales Terima")) {
                if (!groupBy.contains(temp.getKodeSales())) {
                    groupBy.add(temp.getKodeSales());
                }
            } else if (groupByCombo.getSelectionModel().getSelectedItem().equals("Sales Lunas")) {
                if (!groupBy.contains(temp.getSalesLunas())) {
                    groupBy.add(temp.getSalesLunas());
                }
            }
        }
        double totalBerat = 0;
        double totalPinjaman = 0;
        double totalBungaKomp = 0;
        double totalBungaRp = 0;
        for (String temp : groupBy) {
            GadaiHead head = new GadaiHead();
            if (groupByCombo.getSelectionModel().getSelectedItem().equals("Tanggal Terima") || groupByCombo.getSelectionModel().getSelectedItem().equals("Tanggal Lunas")) {
                head.setNoGadai(tglNormal.format(tglBarang.parse(temp)));
            } else {
                head.setNoGadai(temp);
            }
            TreeItem<GadaiHead> parent = new TreeItem<>(head);
            double berat = 0;
            double pinjaman = 0;
            double bungaKomp = 0;
            double bungaRp = 0;
            for (GadaiHead detail : filterData) {
                if (groupByCombo.getSelectionModel().getSelectedItem().equals("Tanggal Terima")) {
                    if (temp.equals(detail.getTglGadai().substring(0, 10))) {
                        TreeItem<GadaiHead> child = new TreeItem<>(detail);
                        parent.getChildren().addAll(child);
                        berat = berat + detail.getTotalBerat();
                        pinjaman = pinjaman + detail.getTotalPinjaman();
                        bungaKomp = bungaKomp + detail.getBungaKomp();
                        bungaRp = bungaRp + detail.getBungaRp();
                        totalBerat = totalBerat + detail.getTotalBerat();
                        totalPinjaman = totalPinjaman + detail.getTotalPinjaman();
                        totalBungaKomp = totalBungaKomp + detail.getBungaKomp();
                        totalBungaRp = totalBungaRp + detail.getBungaRp();
                    }
                } else if (groupByCombo.getSelectionModel().getSelectedItem().equals("Tanggal Lunas")) {
                    if (temp.equals(detail.getTglLunas().substring(0, 10))) {
                        TreeItem<GadaiHead> child = new TreeItem<>(detail);
                        parent.getChildren().addAll(child);
                        berat = berat + detail.getTotalBerat();
                        pinjaman = pinjaman + detail.getTotalPinjaman();
                        bungaKomp = bungaKomp + detail.getBungaKomp();
                        bungaRp = bungaRp + detail.getBungaRp();
                        totalBerat = totalBerat + detail.getTotalBerat();
                        totalPinjaman = totalPinjaman + detail.getTotalPinjaman();
                        totalBungaKomp = totalBungaKomp + detail.getBungaKomp();
                        totalBungaRp = totalBungaRp + detail.getBungaRp();
                    }
                } else if (groupByCombo.getSelectionModel().getSelectedItem().equals("Sales Terima")) {
                    if (temp.equals(detail.getKodeSales())) {
                        TreeItem<GadaiHead> child = new TreeItem<>(detail);
                        parent.getChildren().addAll(child);
                        berat = berat + detail.getTotalBerat();
                        pinjaman = pinjaman + detail.getTotalPinjaman();
                        bungaKomp = bungaKomp + detail.getBungaKomp();
                        bungaRp = bungaRp + detail.getBungaRp();
                        totalBerat = totalBerat + detail.getTotalBerat();
                        totalPinjaman = totalPinjaman + detail.getTotalPinjaman();
                        totalBungaKomp = totalBungaKomp + detail.getBungaKomp();
                        totalBungaRp = totalBungaRp + detail.getBungaRp();
                    }
                } else if (groupByCombo.getSelectionModel().getSelectedItem().equals("Sales Lunas")) {
                    if (temp.equals(detail.getSalesLunas())) {
                        TreeItem<GadaiHead> child = new TreeItem<>(detail);
                        parent.getChildren().addAll(child);
                        berat = berat + detail.getTotalBerat();
                        pinjaman = pinjaman + detail.getTotalPinjaman();
                        bungaKomp = bungaKomp + detail.getBungaKomp();
                        bungaRp = bungaRp + detail.getBungaRp();
                        totalBerat = totalBerat + detail.getTotalBerat();
                        totalPinjaman = totalPinjaman + detail.getTotalPinjaman();
                        totalBungaKomp = totalBungaKomp + detail.getBungaKomp();
                        totalBungaRp = totalBungaRp + detail.getBungaRp();
                    }
                }
            }
            parent.getValue().setTotalBerat(berat);
            parent.getValue().setTotalPinjaman(pinjaman);
            parent.getValue().setBungaKomp(bungaKomp);
            parent.getValue().setBungaRp(bungaRp);
            root.getChildren().add(parent);
        }
        gadaiTable.setRoot(root);
        totalBeratField.setText(gr.format(totalBerat));
        totalPinjamanField.setText(rp.format(totalPinjaman));
        totalBungaKompField.setText(rp.format(totalBungaKomp));
        totalBungaRpField.setText(rp.format(totalBungaRp));
    }

    private void viewPelunasanGadai(GadaiHead p) {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailPelunasanGadai.fxml");
        DetailPelunasanGadaiController controller = loader.getController();
        controller.setMainApp(mainApp, stage);
        controller.viewGadai(p.getNoGadai());
    }

    private void printPelunasanGadai(GadaiHead p) {
        try {
            PrintOut printOut = new PrintOut();
            printOut.printBuktiPelunasanGadai(p.getListGadaiDetail());
        } catch (Exception e) {
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }

    private void deletePelunasanGadai(GadaiHead p) {
        if (!p.getTglLunas().substring(0, 10).equals(sistem.getTglSystem())) {
            mainApp.showMessage(Modality.NONE, "Warning", "Tanggal pelunasan gadai sudah berbeda dengan tanggal sekarang");
        } else {
            Stage stage = new Stage();
            FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/Verifikasi.fxml");
            VerifikasiController controller = loader.getController();
            controller.setMainApp(mainApp, stage);
            controller.keteranganLabel.setText("Batal Pelunasan Gadai : " + p.getNoGadai() + "\nVerifikasi : ");
            controller.password.setOnKeyPressed((KeyEvent keyEvent) -> {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    stage.close();
                    User user = Function.verifikasi(controller.password.getText(), "Batal Pelunasan Gadai");
                    if (user != null) {
                        try (Connection con = Koneksi.getConnection()) {
                            String status = Service.batalPelunasanGadai(con, p, user.getUsername());
                            if (status.equals("true")) {
                                mainApp.showMessage(Modality.NONE, "Success", "Pelunasan gadai berhasil dibatal");
                                getGadai();
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
    }
}
