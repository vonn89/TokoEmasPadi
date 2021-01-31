/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Report;

import com.excellentsystem.TokoEmasPadi.DAO.BarangDAO;
import com.excellentsystem.TokoEmasPadi.DAO.PenjualanDetailDAO;
import com.excellentsystem.TokoEmasPadi.DAO.PenjualanHeadDAO;
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
import com.excellentsystem.TokoEmasPadi.Model.Barang;
import com.excellentsystem.TokoEmasPadi.Model.PenjualanDetail;
import com.excellentsystem.TokoEmasPadi.Model.PenjualanHead;
import com.excellentsystem.TokoEmasPadi.Model.User;
import com.excellentsystem.TokoEmasPadi.PrintOut.PrintOut;
import com.excellentsystem.TokoEmasPadi.Service.Service;
import com.excellentsystem.TokoEmasPadi.View.Dialog.DetailPenjualanController;
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
public class LaporanPenjualanController {

    @FXML
    private TreeTableView<PenjualanDetail> penjualanTable;
    @FXML
    private TreeTableColumn<PenjualanDetail, String> noPenjualanColumn;
    @FXML
    private TreeTableColumn<PenjualanDetail, String> tglPenjualanColumn;
    @FXML
    private TreeTableColumn<PenjualanDetail, String> kodeSalesColumn;
    @FXML
    private TreeTableColumn<PenjualanDetail, String> namaColumn;
    @FXML
    private TreeTableColumn<PenjualanDetail, String> alamatColumn;
    @FXML
    private TreeTableColumn<PenjualanDetail, String> catatanColumn;

    @FXML
    private TreeTableColumn<PenjualanDetail, String> kodeBarcodeColumn;
    @FXML
    private TreeTableColumn<PenjualanDetail, String> namaBarangColumn;
    @FXML
    private TreeTableColumn<PenjualanDetail, String> namaBarcodeColumn;
    @FXML
    private TreeTableColumn<PenjualanDetail, String> kodeKategoriColumn;
    @FXML
    private TreeTableColumn<PenjualanDetail, String> kodeJenisColumn;
    @FXML
    private TreeTableColumn<PenjualanDetail, String> keteranganColumn;
    @FXML
    private TreeTableColumn<PenjualanDetail, String> merkColumn;

    @FXML
    private TreeTableColumn<PenjualanDetail, Number> qtyColumn;
    @FXML
    private TreeTableColumn<PenjualanDetail, Number> beratColumn;
    @FXML
    private TreeTableColumn<PenjualanDetail, Number> beratPembulatanColumn;
    @FXML
    private TreeTableColumn<PenjualanDetail, Number> nilaiPokokColumn;
    @FXML
    private TreeTableColumn<PenjualanDetail, Number> hargaKompColumn;
    @FXML
    private TreeTableColumn<PenjualanDetail, Number> hargaJualColumn;
    @FXML
    private TreeTableColumn<PenjualanDetail, String> userBarcodeColumn;
    @FXML
    private TreeTableColumn<PenjualanDetail, String> tglBarcodeColumn;

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> groupByCombo;

    @FXML
    private GridPane gridPane;
    @FXML
    private HBox hbox;
    @FXML
    private Label totalQtyLabel;
    @FXML
    private Label totalBeratLabel;
    @FXML
    private Label totalBeratPembulatanLabel;
    @FXML
    private Label totalNilaiPokokLabel;
    @FXML
    private Label totalPenjualanLabel;

    @FXML
    private DatePicker tglMulaiPicker;
    @FXML
    private DatePicker tglAkhirPicker;
    final TreeItem<PenjualanDetail> root = new TreeItem<>();
    private ObservableList<PenjualanDetail> allPenjualan = FXCollections.observableArrayList();
    private ObservableList<PenjualanDetail> filterData = FXCollections.observableArrayList();
    private Main mainApp;

    public void initialize() {
        noPenjualanColumn.setCellValueFactory(param -> param.getValue().getValue().noPenjualanProperty());
        tglPenjualanColumn.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(
                        tglLengkap.format(
                                tglSql.parse(cellData.getValue().getValue().getPenjualanHead().getTglPenjualan())));
            } catch (Exception ex) {
                return null;
            }
        });
        kodeSalesColumn.setCellValueFactory(param -> param.getValue().getValue().getPenjualanHead().kodeSalesProperty());
        namaColumn.setCellValueFactory(param -> param.getValue().getValue().getPenjualanHead().namaProperty());
        alamatColumn.setCellValueFactory(param -> param.getValue().getValue().getPenjualanHead().alamatProperty());
        catatanColumn.setCellValueFactory(param -> param.getValue().getValue().getPenjualanHead().catatanProperty());

        kodeBarcodeColumn.setCellValueFactory(param -> param.getValue().getValue().kodeBarcodeProperty());
        namaBarangColumn.setCellValueFactory(param -> param.getValue().getValue().getBarang().namaBarangProperty());
        namaBarcodeColumn.setCellValueFactory(param -> param.getValue().getValue().getBarang().namaBarcodeProperty());
        kodeKategoriColumn.setCellValueFactory(param -> param.getValue().getValue().kodeKategoriProperty());
        kodeJenisColumn.setCellValueFactory(param -> param.getValue().getValue().kodeJenisProperty());
        keteranganColumn.setCellValueFactory(param -> param.getValue().getValue().getBarang().keteranganProperty());
        merkColumn.setCellValueFactory(param -> param.getValue().getValue().getBarang().merkProperty());

        qtyColumn.setCellValueFactory(param -> param.getValue().getValue().qtyProperty());
        qtyColumn.setCellFactory(col -> getTreeTableCell(rp, false, ""));
        beratColumn.setCellValueFactory(param -> param.getValue().getValue().beratProperty());
        beratColumn.setCellFactory(col -> getTreeTableCell(gr, false, "gr"));
        beratPembulatanColumn.setCellValueFactory(param -> param.getValue().getValue().beratPembulatanProperty());
        beratPembulatanColumn.setCellFactory(col -> getTreeTableCell(gr, false, "gr"));
        nilaiPokokColumn.setCellValueFactory(param -> param.getValue().getValue().getBarang().nilaiPokokProperty());
        nilaiPokokColumn.setCellFactory(col -> getTreeTableCell(rp, false, "Rp"));
        hargaKompColumn.setCellValueFactory(param -> param.getValue().getValue().hargaKompProperty());
        hargaKompColumn.setCellFactory(col -> getTreeTableCell(rp, false, "Rp"));
        hargaJualColumn.setCellValueFactory(param -> param.getValue().getValue().hargaJualProperty());
        hargaJualColumn.setCellFactory(col -> getTreeTableCell(rp, false, "Rp"));
        userBarcodeColumn.setCellValueFactory(param -> param.getValue().getValue().getBarang().barcodeByProperty());
        tglBarcodeColumn.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(tglLengkap.format(tglSql.parse(cellData.getValue().getValue().getBarang().getBarcodeDate())));
            } catch (Exception ex) {
                return null;
            }
        });

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
        refresh.setOnAction((ActionEvent e) -> {
            getPenjualanHead();
        });
        rowMenu.getItems().addAll(refresh);
        penjualanTable.setContextMenu(rowMenu);
        penjualanTable.setRowFactory(table -> {
            TreeTableRow<PenjualanDetail> row = new TreeTableRow<PenjualanDetail>() {
                @Override
                public void updateItem(PenjualanDetail item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rowMenu);
                    } else {
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem view = new MenuItem("Detail Penjualan");
                        view.setOnAction((ActionEvent event) -> {
                            viewPenjualan(item);
                        });
                        MenuItem batal = new MenuItem("Batal Penjualan");
                        batal.setOnAction((ActionEvent event) -> {
                            deletePenjualan(item);
                        });
                        MenuItem print = new MenuItem("Print Faktur Penjualan");
                        print.setOnAction((ActionEvent e) -> {
                            printPenjualan(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            getPenjualanHead();
                        });
                        if (item.getPenjualanHead().getNama() != null) {
                            rowMenu.getItems().addAll(view, batal, print);
                        }
                        rowMenu.getItems().addAll(refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            return row;
        });
        allPenjualan.addListener((ListChangeListener.Change<? extends PenjualanDetail> change) -> {
            searchPenjualanDetail();
        });
        searchField.textProperty().addListener(
                (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    searchPenjualanDetail();
                });
        filterData.addAll(allPenjualan);
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        ObservableList<String> groupBy = FXCollections.observableArrayList();
        groupBy.add("No Penjualan");
        groupBy.add("Tanggal");
        groupBy.add("Sales");
        groupBy.add("Kategori Barang");
        groupBy.add("Jenis Barang");
        groupByCombo.setItems(groupBy);
        groupByCombo.getSelectionModel().select("No Penjualan");
        getPenjualanHead();
        if (Main.user == null) {
            nilaiPokokColumn.setVisible(false);
            penjualanTable.setTableMenuButtonVisible(false);
            hbox.setVisible(false);
            gridPane.getRowConstraints().get(2).setMinHeight(0);
            gridPane.getRowConstraints().get(2).setPrefHeight(0);
            gridPane.getRowConstraints().get(2).setMaxHeight(0);
        }
    }

    @FXML
    private void getPenjualanHead() {
        try (Connection con = Koneksi.getConnection()) {
            allPenjualan.clear();
            List<PenjualanDetail> allDetail = PenjualanDetailDAO.getAllByTglPenjualanAndStatus(con,
                    tglMulaiPicker.getValue().toString(), tglAkhirPicker.getValue().toString(), "true");
            List<PenjualanHead> listPenjualanHead = PenjualanHeadDAO.getAllByTglPenjualanAndStatus(con,
                    tglMulaiPicker.getValue().toString(), tglAkhirPicker.getValue().toString(), "true");
            for (PenjualanDetail d : allDetail) {
                for (PenjualanHead h : listPenjualanHead) {
                    if (d.getNoPenjualan().equals(h.getNoPenjualan())) {
                        d.setPenjualanHead(h);
                    }
                }
                Barang b = BarangDAO.get(con, d.getKodeBarcode());
                d.setBarang(b);
            }
            allPenjualan.addAll(allDetail);
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

    private void searchPenjualanDetail() {
        try {
            filterData.clear();
            for (PenjualanDetail temp : allPenjualan) {
                if (searchField.getText() == null || searchField.getText().equals("")) {
                    filterData.add(temp);
                } else {
                    if (checkColumn(temp.getNoPenjualan())
                            || checkColumn(tglLengkap.format(tglSql.parse(temp.getPenjualanHead().getTglPenjualan())))
                            || checkColumn(temp.getPenjualanHead().getKodeSales())
                            || checkColumn(temp.getPenjualanHead().getNama())
                            || checkColumn(temp.getPenjualanHead().getAlamat())
                            || checkColumn(rp.format(temp.getPenjualanHead().getTotalQty()))
                            || checkColumn(gr.format(temp.getPenjualanHead().getTotalBerat()))
                            || checkColumn(gr.format(temp.getPenjualanHead().getTotalBeratPembulatan()))
                            || checkColumn(gr.format(temp.getPenjualanHead().getGrandtotal()))
                            || checkColumn(temp.getPenjualanHead().getCatatan())
                            || checkColumn(temp.getKodeBarcode())
                            || checkColumn(temp.getBarang().getNamaBarang())
                            || checkColumn(temp.getBarang().getNamaBarcode())
                            || checkColumn(temp.getBarang().getKeterangan())
                            || checkColumn(temp.getBarang().getKodeKategori())
                            || checkColumn(temp.getBarang().getKodeJenis())
                            || checkColumn(temp.getBarang().getKeterangan())
                            || checkColumn(temp.getBarang().getMerk())
                            || checkColumn(temp.getBarang().getBarcodeBy())
                            || checkColumn(rp.format(temp.getQty()))
                            || checkColumn(gr.format(temp.getBerat()))
                            || checkColumn(gr.format(temp.getBeratPembulatan()))
                            || checkColumn(gr.format(temp.getBarang().getNilaiPokok()))
                            || checkColumn(gr.format(temp.getHargaJual()))
                            || checkColumn(gr.format(temp.getHargaKomp()))
                            || checkColumn(gr.format(temp.getTotal()))
                            || checkColumn(tglLengkap.format(tglSql.parse(
                                    temp.getBarang().getBarcodeDate())))) {
                        filterData.add(temp);
                    }
                }
            }
            setTable();
        } catch (Exception e) {
            e.printStackTrace();
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }

    private void setTable() throws Exception {
        if (penjualanTable.getRoot() != null) {
            penjualanTable.getRoot().getChildren().clear();
        }
        List<String> groupBy = new ArrayList<>();
        for (PenjualanDetail temp : filterData) {
            if (groupByCombo.getSelectionModel().getSelectedItem().equals("No Penjualan")) {
                if (!groupBy.contains(temp.getNoPenjualan())) {
                    groupBy.add(temp.getNoPenjualan());
                }
            } else if (groupByCombo.getSelectionModel().getSelectedItem().equals("Tanggal")) {
                if (!groupBy.contains(temp.getPenjualanHead().getTglPenjualan().substring(0, 10))) {
                    groupBy.add(temp.getPenjualanHead().getTglPenjualan().substring(0, 10));
                }
            } else if (groupByCombo.getSelectionModel().getSelectedItem().equals("Sales")) {
                if (!groupBy.contains(temp.getPenjualanHead().getKodeSales())) {
                    groupBy.add(temp.getPenjualanHead().getKodeSales());
                }
            } else if (groupByCombo.getSelectionModel().getSelectedItem().equals("Kategori Barang")) {
                if (!groupBy.contains(temp.getKodeKategori())) {
                    groupBy.add(temp.getKodeKategori());
                }
            } else if (groupByCombo.getSelectionModel().getSelectedItem().equals("Jenis Barang")) {
                if (!groupBy.contains(temp.getKodeJenis())) {
                    groupBy.add(temp.getKodeJenis());
                }
            }
        }
        int totalQty = 0;
        double totalBerat = 0;
        double totalBeratPembulatan = 0;
        double totalNilai = 0;
        double totalJual = 0;
        for (String temp : groupBy) {
            PenjualanDetail head = new PenjualanDetail();
            if (groupByCombo.getSelectionModel().getSelectedItem().equals("Tanggal")) {
                head.setNoPenjualan(tglNormal.format(tglBarang.parse(temp)));
            } else {
                head.setNoPenjualan(temp);
            }
            PenjualanHead p = new PenjualanHead();
            p.setKodeSales("");
            head.setPenjualanHead(p);
            TreeItem<PenjualanDetail> parent = new TreeItem<>(head);

            int qty = 0;
            double berat = 0;
            double beratPembulatan = 0;
            double nilaiPokok = 0;
            double hargaKomp = 0;
            double hargaJual = 0;

            for (PenjualanDetail detail : filterData) {
                if (groupByCombo.getSelectionModel().getSelectedItem().equals("No Penjualan")) {
                    if (temp.equals(detail.getNoPenjualan())) {
                        TreeItem<PenjualanDetail> child = new TreeItem<>(detail);
                        parent.getChildren().addAll(child);

                        qty = qty + detail.getQty();
                        berat = berat + detail.getQty() * detail.getBerat();
                        beratPembulatan = beratPembulatan + detail.getQty() * detail.getBeratPembulatan();
                        nilaiPokok = nilaiPokok + detail.getQty() * detail.getBarang().getNilaiPokok();
                        hargaKomp = hargaKomp + detail.getQty() * detail.getHargaKomp();
                        hargaJual = hargaJual + detail.getQty() * detail.getHargaJual();

                        totalQty = totalQty + detail.getQty();
                        totalBerat = totalBerat + detail.getQty() * detail.getBerat();
                        totalBeratPembulatan = totalBeratPembulatan + detail.getQty() * detail.getBeratPembulatan();
                        totalNilai = totalNilai + detail.getQty() * detail.getBarang().getNilaiPokok();
                        totalJual = totalJual + detail.getQty() * detail.getHargaJual();
                    }
                } else if (groupByCombo.getSelectionModel().getSelectedItem().equals("Tanggal")) {
                    if (temp.equals(detail.getPenjualanHead().getTglPenjualan().substring(0, 10))) {
                        TreeItem<PenjualanDetail> child = new TreeItem<>(detail);
                        parent.getChildren().addAll(child);

                        qty = qty + detail.getQty();
                        berat = berat + detail.getQty() * detail.getBerat();
                        beratPembulatan = beratPembulatan + detail.getQty() * detail.getBeratPembulatan();
                        nilaiPokok = nilaiPokok + detail.getQty() * detail.getBarang().getNilaiPokok();
                        hargaKomp = hargaKomp + detail.getQty() * detail.getHargaKomp();
                        hargaJual = hargaJual + detail.getQty() * detail.getHargaJual();

                        totalQty = totalQty + detail.getQty();
                        totalBerat = totalBerat + detail.getQty() * detail.getBerat();
                        totalBeratPembulatan = totalBeratPembulatan + detail.getQty() * detail.getBeratPembulatan();
                        totalNilai = totalNilai + detail.getQty() * detail.getBarang().getNilaiPokok();
                        totalJual = totalJual + detail.getQty() * detail.getHargaJual();
                    }
                } else if (groupByCombo.getSelectionModel().getSelectedItem().equals("Sales")) {
                    if (temp.equals(detail.getPenjualanHead().getKodeSales())) {
                        TreeItem<PenjualanDetail> child = new TreeItem<>(detail);
                        parent.getChildren().addAll(child);

                        qty = qty + detail.getQty();
                        berat = berat + detail.getQty() * detail.getBerat();
                        beratPembulatan = beratPembulatan + detail.getQty() * detail.getBeratPembulatan();
                        nilaiPokok = nilaiPokok + detail.getQty() * detail.getBarang().getNilaiPokok();
                        hargaKomp = hargaKomp + detail.getQty() * detail.getHargaKomp();
                        hargaJual = hargaJual + detail.getQty() * detail.getHargaJual();

                        totalQty = totalQty + detail.getQty();
                        totalBerat = totalBerat + detail.getQty() * detail.getBerat();
                        totalBeratPembulatan = totalBeratPembulatan + detail.getQty() * detail.getBeratPembulatan();
                        totalNilai = totalNilai + detail.getQty() * detail.getBarang().getNilaiPokok();
                        totalJual = totalJual + detail.getQty() * detail.getHargaJual();
                    }
                } else if (groupByCombo.getSelectionModel().getSelectedItem().equals("Kategori Barang")) {
                    if (temp.equals(detail.getKodeKategori())) {
                        TreeItem<PenjualanDetail> child = new TreeItem<>(detail);
                        parent.getChildren().addAll(child);

                        qty = qty + detail.getQty();
                        berat = berat + detail.getQty() * detail.getBerat();
                        beratPembulatan = beratPembulatan + detail.getQty() * detail.getBeratPembulatan();
                        nilaiPokok = nilaiPokok + detail.getQty() * detail.getBarang().getNilaiPokok();
                        hargaKomp = hargaKomp + detail.getQty() * detail.getHargaKomp();
                        hargaJual = hargaJual + detail.getQty() * detail.getHargaJual();

                        totalQty = totalQty + detail.getQty();
                        totalBerat = totalBerat + detail.getQty() * detail.getBerat();
                        totalBeratPembulatan = totalBeratPembulatan + detail.getQty() * detail.getBeratPembulatan();
                        totalNilai = totalNilai + detail.getQty() * detail.getBarang().getNilaiPokok();
                        totalJual = totalJual + detail.getQty() * detail.getHargaJual();
                    }
                } else if (groupByCombo.getSelectionModel().getSelectedItem().equals("Jenis Barang")) {
                    if (temp.equals(detail.getKodeJenis())) {
                        TreeItem<PenjualanDetail> child = new TreeItem<>(detail);
                        parent.getChildren().addAll(child);

                        qty = qty + detail.getQty();
                        berat = berat + detail.getQty() * detail.getBerat();
                        beratPembulatan = beratPembulatan + detail.getQty() * detail.getBeratPembulatan();
                        nilaiPokok = nilaiPokok + detail.getQty() * detail.getBarang().getNilaiPokok();
                        hargaKomp = hargaKomp + detail.getQty() * detail.getHargaKomp();
                        hargaJual = hargaJual + detail.getQty() * detail.getHargaJual();

                        totalQty = totalQty + detail.getQty();
                        totalBerat = totalBerat + detail.getQty() * detail.getBerat();
                        totalBeratPembulatan = totalBeratPembulatan + detail.getQty() * detail.getBeratPembulatan();
                        totalNilai = totalNilai + detail.getQty() * detail.getBarang().getNilaiPokok();
                        totalJual = totalJual + detail.getQty() * detail.getHargaJual();
                    }
                }
            }
            Barang b = new Barang();
            b.setNilaiPokok(nilaiPokok);
            parent.getValue().setBarang(b);

            parent.getValue().setQty(qty);
            parent.getValue().setBerat(berat);
            parent.getValue().setBeratPembulatan(beratPembulatan);

            parent.getValue().setHargaKomp(hargaKomp);
            parent.getValue().setHargaJual(hargaJual);
            root.getChildren().add(parent);
        }
        penjualanTable.setRoot(root);
        totalQtyLabel.setText(gr.format(totalQty));
        totalBeratLabel.setText(gr.format(totalBerat) + " gr");
        totalBeratPembulatanLabel.setText(gr.format(totalBeratPembulatan) + " gr");
        totalNilaiPokokLabel.setText("Rp " + rp.format(totalNilai));
        totalPenjualanLabel.setText("Rp " + rp.format(totalJual));
    }

    private void viewPenjualan(PenjualanDetail d) {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailPenjualan.fxml");
        DetailPenjualanController controller = loader.getController();
        controller.setMainApp(mainApp, stage);
        controller.setPenjualan(d.getNoPenjualan());
    }

    private void printPenjualan(PenjualanDetail pj) {
        try (Connection con = Koneksi.getConnection()) {
            List<PenjualanDetail> listDetail = PenjualanDetailDAO.getAllByNoPenjualan(con, pj.getNoPenjualan());
            PenjualanHead head = PenjualanHeadDAO.get(con, pj.getNoPenjualan());
            System.out.println(head.getGrandtotal());
            int i = 0;
            for (PenjualanDetail d : listDetail) {
                d.setPenjualanHead(head);
                i++;
            }
            while(i<5){
                PenjualanDetail detail = new PenjualanDetail();
                detail.setPenjualanHead(head);
                detail.setNamaBarang("");
                detail.setQty(0);
                detail.setKodeBarcode("");
                detail.setBeratPembulatan(0);
                detail.setHargaJual(0);
                listDetail.add(detail);
                i++;
            }
            PrintOut printout = new PrintOut();
            printout.printSuratPenjualan(listDetail);
        } catch (Exception e) {
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }

    private void deletePenjualan(PenjualanDetail pj) {
        try (Connection con = Koneksi.getConnection()) {
            PenjualanHead p = PenjualanHeadDAO.get(con, pj.getNoPenjualan());
            p.setListPenjualanDetail(PenjualanDetailDAO.getAllByNoPenjualan(con, pj.getNoPenjualan()));
            for (PenjualanDetail d : p.getListPenjualanDetail()) {
                d.setBarang(BarangDAO.get(con, d.getKodeBarcode()));
            }
            if (!p.getNoPenjualan().substring(3, 9).equals(tglKode.format(tglBarang.parse(sistem.getTglSystem())))) {
                mainApp.showMessage(Modality.NONE, "Warning", "Penjualan sudah berbeda tanggal");
            } else {
                Stage stage = new Stage();
                FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/Verifikasi.fxml");
                VerifikasiController controller = loader.getController();
                controller.setMainApp(mainApp, stage);
                controller.keteranganLabel.setText("Batal Penjualan : " + p.getNoPenjualan() + "\nVerifikasi : ");
                controller.password.setOnKeyPressed((KeyEvent keyEvent) -> {
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        stage.close();
                        User user = Function.verifikasi(controller.password.getText(), "Batal Penjualan");
                        if (user != null) {
                            try(Connection con1 = Koneksi.getConnection()){
                                p.setTglBatal(Function.getSystemDate());
                                p.setUserBatal(user.getUsername());
                                p.setStatus("false");
                                String status = Service.batalPenjualan(con1, p);
                                getPenjualanHead();
                                if (status.equals("true")) {
                                    mainApp.showMessage(Modality.NONE, "Success", "Data penjualan berhasil dibatal");
                                } else {
                                    mainApp.showMessage(Modality.NONE, "Error", status);
                                }
                            }catch(Exception e){
                            
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
