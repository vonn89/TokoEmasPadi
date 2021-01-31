/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View;

import com.excellentsystem.TokoEmasPadi.DAO.KategoriDAO;
import com.excellentsystem.TokoEmasPadi.Function;
import static com.excellentsystem.TokoEmasPadi.Function.getTableCell;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import com.excellentsystem.TokoEmasPadi.Model.PembelianDetail;
import com.excellentsystem.TokoEmasPadi.Model.PembelianHead;
import com.excellentsystem.TokoEmasPadi.Model.PenjualanDetail;
import com.excellentsystem.TokoEmasPadi.Model.User;
import com.excellentsystem.TokoEmasPadi.PrintOut.PrintOut;
import com.excellentsystem.TokoEmasPadi.Service.Service;
import com.excellentsystem.TokoEmasPadi.View.Dialog.*;
import java.sql.Connection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Yunaz
 */
public class PembelianController {

    @FXML
    private TableView<PembelianDetail> detailTable;
    @FXML
    private TableColumn<PembelianDetail, Number> jumlahColumn;
    @FXML
    private TableColumn<PembelianDetail, String> kodeKategoriColumn;
    @FXML
    private TableColumn<PembelianDetail, String> namaBarangColumn;
    @FXML
    private TableColumn<PembelianDetail, Number> beratColumn;
    @FXML
    private TableColumn<PembelianDetail, String> kondisiColumn;
    @FXML
    private TableColumn<PembelianDetail, String> statusSuratColumn;
    @FXML
    private TableColumn<PembelianDetail, Number> hargaBeliColumn;

    @FXML
    private Label totalPembelianLabel;

    @FXML
    private TextField namaField;
    @FXML
    private TextField alamatDesaField;

    @FXML
    private Button ubahButton;
    @FXML
    private Button hapusButton;

    @FXML
    private CheckBox printCheckBox;

    private ObservableList<PembelianDetail> allDetail = FXCollections.observableArrayList();
    private Main mainApp;

    public void initialize() {
        jumlahColumn.setCellValueFactory(cellData -> cellData.getValue().qtyProperty());
        jumlahColumn.setCellFactory(col -> getTableCell(rp, ""));
        kodeKategoriColumn.setCellValueFactory(cellData -> cellData.getValue().kodeKategoriProperty());
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().namaBarangProperty());
        kondisiColumn.setCellValueFactory(cellData -> cellData.getValue().kondisiProperty());
        statusSuratColumn.setCellValueFactory(cellData -> cellData.getValue().statusSuratProperty());
        beratColumn.setCellValueFactory(cellData -> cellData.getValue().beratProperty());
        beratColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        hargaBeliColumn.setCellValueFactory(cellData -> cellData.getValue().hargaBeliProperty());
        hargaBeliColumn.setCellFactory(col -> getTableCell(rp, "Rp"));

        namaField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                alamatDesaField.selectAll();
                alamatDesaField.requestFocus();
            }
        });
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem cari = new MenuItem("Cari Barang Terjual");
        cari.setOnAction((ActionEvent event) -> {
            cariBarangTerjual();
        });
        MenuItem tambah = new MenuItem("Tambah Barang");
        tambah.setOnAction((ActionEvent event) -> {
            tambahBarang();
        });
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            detailTable.refresh();
        });
        rowMenu.getItems().addAll(cari, tambah, refresh);
        detailTable.setContextMenu(rowMenu);
        detailTable.setRowFactory(table -> {
            TableRow<PembelianDetail> row = new TableRow<PembelianDetail>() {
                @Override
                public void updateItem(PembelianDetail item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rowMenu);
                    } else {
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem cari = new MenuItem("Cari Barang Terjual");
                        cari.setOnAction((ActionEvent event) -> {
                            cariBarangTerjual();
                        });
                        MenuItem tambah = new MenuItem("Tambah Barang");
                        tambah.setOnAction((ActionEvent event) -> {
                            tambahBarang();
                        });
                        MenuItem ubah = new MenuItem("Ubah Barang");
                        ubah.setOnAction((ActionEvent event) -> {
                            ubahBarang();
                        });
                        MenuItem hapus = new MenuItem("Hapus Barang");
                        hapus.setOnAction((ActionEvent event) -> {
                            hapusBarang();
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            detailTable.refresh();
                        });
                        rowMenu.getItems().addAll(cari, tambah, ubah, hapus, refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)
                        && mouseEvent.getClickCount() == 2) {
                    if (row.getItem() != null) {
                        ubahBarang();
                    }
                }
            });
            return row;
        });
        detailTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                ubahButton.setVisible(true);
                hapusButton.setVisible(true);
            } else {
                ubahButton.setVisible(false);
                hapusButton.setVisible(false);
            }
        });
        detailTable.setItems(allDetail);
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    private void hitungTotal() {
        double totalHarga = 0;
        for (PembelianDetail d : allDetail) {
            totalHarga = totalHarga + d.getHargaBeli();
        }
        totalPembelianLabel.setText(rp.format(totalHarga));
    }

    @FXML
    private void cariBarangTerjual() {
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/AddBarangTerjual.fxml");
        AddBarangTerjualController c = loader.getController();
        c.setMainApp(mainApp, child);
        c.penjualanTable.setRowFactory(table -> {
            TableRow<PenjualanDetail> row = new TableRow<PenjualanDetail>() {
                @Override
                public void updateItem(PenjualanDetail item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(null);
                    } else {
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem pilih = new MenuItem("Pilih Barang");
                        pilih.setOnAction((ActionEvent e) -> {
                            addBarangTerjual(item);
                            child.close();
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            c.getPenjualanHeadDetail();
                        });
                        rowMenu.getItems().addAll(pilih, refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
                    if (row.getItem() != null) {
                        addBarangTerjual(row.getItem());
                        child.close();
                    }
                }
            });
            return row;
        });
    }

    private void addBarangTerjual(PenjualanDetail pj) {
        PembelianDetail d = new PembelianDetail();
        d.setKodeKategori(pj.getKodeKategori());
        d.setNamaBarang(pj.getNamaBarang());
        d.setQty(pj.getQty());
        d.setBerat(pj.getBerat());
        d.setKondisi("Baik");
        d.setStatusSurat("Ada");
        d.setHargaKomp(0);
        d.setHargaBeli(0);

        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/AddBarangPembelian.fxml");
        AddBarangPembelianController controller = loader.getController();
        controller.setMainApp(mainApp, child);
        controller.setDetailPembelian(d);
        controller.getHargaBeli();
        controller.saveButton.setOnAction((event) -> {
            if (controller.kodeKategoriCombo.getSelectionModel().getSelectedItem() == null) {
                mainApp.showMessage(Modality.NONE, "Warning", "Kode kategori belum dipilih");
            } else if ("".equals(controller.namaBarangField.getText())) {
                mainApp.showMessage(Modality.NONE, "Warning", "Nama barang masih kosong");
            } else if (Double.parseDouble(controller.jumlahField.getText().replaceAll(",", "")) <= 0) {
                mainApp.showMessage(Modality.NONE, "Warning", "Jumlah masih kosong");
            } else if (Double.parseDouble(controller.beratField.getText().replaceAll(",", "")) <= 0) {
                mainApp.showMessage(Modality.NONE, "Warning", "Berat masih kosong");
            } else if (controller.kondisiCombo.getSelectionModel().getSelectedItem() == null) {
                mainApp.showMessage(Modality.NONE, "Warning", "Kondisi barang belum dipilih");
            } else if (controller.statusSuratCombo.getSelectionModel().getSelectedItem() == null) {
                mainApp.showMessage(Modality.NONE, "Warning", "Status surat belum dipilih");
            } else if (Double.parseDouble(controller.hargaField.getText().replaceAll(",", "")) <= 0) {
                mainApp.showMessage(Modality.NONE, "Warning", "Harga beli masih kosong");
            } else {
                child.close();
                try (Connection con = Koneksi.getConnection()) {
                    if (controller.kondisiCombo.getSelectionModel().getSelectedItem().equals("Baik")
                            && controller.statusSuratCombo.getSelectionModel().getSelectedItem().equals("Ada")) {
                        hargaKomp = KategoriDAO.get(con, controller.kodeKategoriCombo.getSelectionModel().getSelectedItem()).getHargaJual()
                                * Double.parseDouble(controller.beratField.getText().replaceAll(",", "")) * 95 / 100;
                    } else {
                        hargaKomp = KategoriDAO.get(con, controller.kodeKategoriCombo.getSelectionModel().getSelectedItem()).getHargaBeli()
                                * Double.parseDouble(controller.beratField.getText().replaceAll(",", ""));
                    }
                    if (Double.parseDouble(controller.hargaField.getText().replaceAll(",", "")) > hargaKomp) {
                        Stage verifikasi = new Stage();
                        FXMLLoader verifikasiLoader = mainApp.showDialog(mainApp.MainStage, verifikasi, "View/Dialog/Verifikasi.fxml");
                        VerifikasiController verifikasiController = verifikasiLoader.getController();
                        verifikasiController.setMainApp(mainApp, verifikasi);
                        verifikasiController.keteranganLabel.setText(
                                "Harga beli : Rp " + controller.hargaField.getText() + "\n"
                                + "Harga komp : Rp " + rp.format(hargaKomp) + "\n"
                                + "Verifikasi : ");
                        verifikasiController.password.setOnKeyPressed((KeyEvent keyEvent) -> {
                            if (keyEvent.getCode() == KeyCode.ENTER) {
                                verifikasi.close();
                                User user = Function.verifikasi(verifikasiController.password.getText(), "Pembelian");
                                if (user != null) {
                                    d.setKodeKategori(controller.kodeKategoriCombo.getSelectionModel().getSelectedItem());
                                    d.setNamaBarang(controller.namaBarangField.getText());
                                    d.setQty(Integer.parseInt(controller.jumlahField.getText().replaceAll(",", "")));
                                    d.setBerat(Double.parseDouble(controller.beratField.getText().replaceAll(",", "")));
                                    d.setKondisi(controller.kondisiCombo.getSelectionModel().getSelectedItem());
                                    d.setStatusSurat(controller.statusSuratCombo.getSelectionModel().getSelectedItem());
                                    d.setHargaKomp(hargaKomp);
                                    d.setHargaBeli(Double.parseDouble(controller.hargaField.getText().replaceAll(",", "")));
                                    allDetail.add(d);
                                    detailTable.refresh();
                                    hitungTotal();
                                } else {
                                    mainApp.showMessage(Modality.NONE, "Warning", "Verifikasi salah");
                                }
                            }
                        });
                    } else {
                        d.setKodeKategori(controller.kodeKategoriCombo.getSelectionModel().getSelectedItem());
                        d.setNamaBarang(controller.namaBarangField.getText());
                        d.setQty(Integer.parseInt(controller.jumlahField.getText().replaceAll(",", "")));
                        d.setBerat(Double.parseDouble(controller.beratField.getText().replaceAll(",", "")));
                        d.setKondisi(controller.kondisiCombo.getSelectionModel().getSelectedItem());
                        d.setStatusSurat(controller.statusSuratCombo.getSelectionModel().getSelectedItem());
                        d.setHargaKomp(hargaKomp);
                        d.setHargaBeli(Double.parseDouble(controller.hargaField.getText().replaceAll(",", "")));
                        allDetail.add(d);
                        detailTable.refresh();
                        hitungTotal();
                    }
                } catch (Exception e) {
                    mainApp.showMessage(Modality.NONE, "Error", e.toString());
                }
            }
        });
    }
    double hargaKomp = 0;

    @FXML
    private void tambahBarang() {
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/AddBarangPembelian.fxml");
        AddBarangPembelianController controller = loader.getController();
        controller.setMainApp(mainApp, child);
        controller.saveButton.setOnAction((event) -> {
            if (controller.kodeKategoriCombo.getSelectionModel().getSelectedItem() == null) {
                mainApp.showMessage(Modality.NONE, "Warning", "Kode kategori belum dipilih");
            } else if ("".equals(controller.namaBarangField.getText())) {
                mainApp.showMessage(Modality.NONE, "Warning", "Nama barang masih kosong");
            } else if (Double.parseDouble(controller.jumlahField.getText().replaceAll(",", "")) <= 0) {
                mainApp.showMessage(Modality.NONE, "Warning", "Jumlah masih kosong");
            } else if (Double.parseDouble(controller.beratField.getText().replaceAll(",", "")) <= 0) {
                mainApp.showMessage(Modality.NONE, "Warning", "Berat masih kosong");
            } else if (controller.kondisiCombo.getSelectionModel().getSelectedItem() == null) {
                mainApp.showMessage(Modality.NONE, "Warning", "Kondisi barang belum dipilih");
            } else if (controller.statusSuratCombo.getSelectionModel().getSelectedItem() == null) {
                mainApp.showMessage(Modality.NONE, "Warning", "Status surat belum dipilih");
            } else if (Double.parseDouble(controller.hargaField.getText().replaceAll(",", "")) <= 0) {
                mainApp.showMessage(Modality.NONE, "Warning", "Harga beli masih kosong");
            } else {
                child.close();
                try (Connection con = Koneksi.getConnection()) {

                    if (controller.kondisiCombo.getSelectionModel().getSelectedItem().equals("Baik")
                            && controller.statusSuratCombo.getSelectionModel().getSelectedItem().equals("Ada")) {
                        hargaKomp = KategoriDAO.get(con, controller.kodeKategoriCombo.getSelectionModel().getSelectedItem()).getHargaJual()
                                * Double.parseDouble(controller.beratField.getText().replaceAll(",", "")) * 95 / 100;
                    } else {
                        hargaKomp = KategoriDAO.get(con, controller.kodeKategoriCombo.getSelectionModel().getSelectedItem()).getHargaBeli()
                                * Double.parseDouble(controller.beratField.getText().replaceAll(",", ""));
                    }

                    if (Double.parseDouble(controller.hargaField.getText().replaceAll(",", "")) > hargaKomp) {
                        Stage verifikasiStage = new Stage();
                        FXMLLoader verifikasiLoader = mainApp.showDialog(mainApp.MainStage, verifikasiStage, "View/Dialog/Verifikasi.fxml");
                        VerifikasiController verifikasiController = verifikasiLoader.getController();
                        verifikasiController.setMainApp(mainApp, verifikasiStage);
                        verifikasiController.keteranganLabel.setText(
                                "Harga beli : Rp " + controller.hargaField.getText() + "\n"
                                + "Harga komp : Rp " + rp.format(hargaKomp) + "\n"
                                + "Verifikasi : ");
                        verifikasiController.password.setOnKeyPressed((KeyEvent keyEvent) -> {
                            if (keyEvent.getCode() == KeyCode.ENTER) {
                                verifikasiStage.close();
                                User user = Function.verifikasi(verifikasiController.password.getText(), "Pembelian");
                                if (user != null) {
                                    PembelianDetail d = new PembelianDetail();
                                    d.setKodeKategori(controller.kodeKategoriCombo.getSelectionModel().getSelectedItem());
                                    d.setNamaBarang(controller.namaBarangField.getText());
                                    d.setQty(Integer.parseInt(controller.jumlahField.getText().replaceAll(",", "")));
                                    d.setBerat(Double.parseDouble(controller.beratField.getText().replaceAll(",", "")));
                                    d.setKondisi(controller.kondisiCombo.getSelectionModel().getSelectedItem());
                                    d.setStatusSurat(controller.statusSuratCombo.getSelectionModel().getSelectedItem());
                                    d.setHargaKomp(hargaKomp);
                                    d.setHargaBeli(Double.parseDouble(controller.hargaField.getText().replaceAll(",", "")));
                                    allDetail.add(d);
                                    detailTable.refresh();
                                    hitungTotal();
                                } else {
                                    mainApp.showMessage(Modality.NONE, "Warning", "Verifikasi salah");
                                }
                            }
                        });
                    } else {
                        PembelianDetail d = new PembelianDetail();
                        d.setKodeKategori(controller.kodeKategoriCombo.getSelectionModel().getSelectedItem());
                        d.setNamaBarang(controller.namaBarangField.getText());
                        d.setQty(Integer.parseInt(controller.jumlahField.getText().replaceAll(",", "")));
                        d.setBerat(Double.parseDouble(controller.beratField.getText().replaceAll(",", "")));
                        d.setKondisi(controller.kondisiCombo.getSelectionModel().getSelectedItem());
                        d.setStatusSurat(controller.statusSuratCombo.getSelectionModel().getSelectedItem());
                        d.setHargaKomp(hargaKomp);
                        d.setHargaBeli(Double.parseDouble(controller.hargaField.getText().replaceAll(",", "")));
                        allDetail.add(d);
                        detailTable.refresh();
                        hitungTotal();
                    }
                } catch (Exception e) {
                    mainApp.showMessage(Modality.NONE, "Error", e.toString());
                }
            }
        });
    }

    @FXML
    private void ubahBarang() {
        if (detailTable.getSelectionModel().getSelectedItem() != null) {
            PembelianDetail d = detailTable.getSelectionModel().getSelectedItem();
            Stage child = new Stage();
            FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/AddBarangPembelian.fxml");
            AddBarangPembelianController controller = loader.getController();
            controller.setMainApp(mainApp, child);
            controller.setDetailPembelian(d);
            controller.saveButton.setOnAction((event) -> {
                if (controller.kodeKategoriCombo.getSelectionModel().getSelectedItem() == null) {
                    mainApp.showMessage(Modality.NONE, "Warning", "Kode kategori belum dipilih");
                } else if ("".equals(controller.namaBarangField.getText())) {
                    mainApp.showMessage(Modality.NONE, "Warning", "Nama barang masih kosong");
                } else if (Double.parseDouble(controller.jumlahField.getText().replaceAll(",", "")) <= 0) {
                    mainApp.showMessage(Modality.NONE, "Warning", "Jumlah masih kosong");
                } else if (Double.parseDouble(controller.beratField.getText().replaceAll(",", "")) <= 0) {
                    mainApp.showMessage(Modality.NONE, "Warning", "Berat masih kosong");
                } else if (controller.kondisiCombo.getSelectionModel().getSelectedItem() == null) {
                    mainApp.showMessage(Modality.NONE, "Warning", "Kondisi barang belum dipilih");
                } else if (controller.statusSuratCombo.getSelectionModel().getSelectedItem() == null) {
                    mainApp.showMessage(Modality.NONE, "Warning", "Status surat belum dipilih");
                } else if (Double.parseDouble(controller.hargaField.getText().replaceAll(",", "")) <= 0) {
                    mainApp.showMessage(Modality.NONE, "Warning", "Harga beli masih kosong");
                } else {
                    child.close();
                    try (Connection con = Koneksi.getConnection()) {
                        if (controller.kondisiCombo.getSelectionModel().getSelectedItem().equals("Baik")
                                && controller.statusSuratCombo.getSelectionModel().getSelectedItem().equals("Ada")) {
                            hargaKomp = KategoriDAO.get(con, controller.kodeKategoriCombo.getSelectionModel().getSelectedItem()).getHargaJual()
                                    * Double.parseDouble(controller.beratField.getText().replaceAll(",", "")) * 95 / 100;
                        } else {
                            hargaKomp = KategoriDAO.get(con, controller.kodeKategoriCombo.getSelectionModel().getSelectedItem()).getHargaBeli()
                                    * Double.parseDouble(controller.beratField.getText().replaceAll(",", ""));
                        }
                        if (Double.parseDouble(controller.hargaField.getText().replaceAll(",", "")) > hargaKomp) {
                            Stage verifikasi = new Stage();
                            FXMLLoader verifikasiLoader = mainApp.showDialog(mainApp.MainStage, verifikasi, "View/Dialog/Verifikasi.fxml");
                            VerifikasiController verifikasiController = verifikasiLoader.getController();
                            verifikasiController.setMainApp(mainApp, verifikasi);
                            verifikasiController.keteranganLabel.setText(
                                    "Harga beli : Rp " + controller.hargaField.getText() + "\n"
                                    + "Harga komp : Rp " + rp.format(hargaKomp) + "\n"
                                    + "Verifikasi : ");
                            verifikasiController.password.setOnKeyPressed((KeyEvent keyEvent) -> {
                                if (keyEvent.getCode() == KeyCode.ENTER) {
                                    verifikasi.close();
                                    User user = Function.verifikasi(verifikasiController.password.getText(), "Pembelian");
                                    if (user != null) {
                                        d.setKodeKategori(controller.kodeKategoriCombo.getSelectionModel().getSelectedItem());
                                        d.setNamaBarang(controller.namaBarangField.getText());
                                        d.setQty(Integer.parseInt(controller.jumlahField.getText().replaceAll(",", "")));
                                        d.setBerat(Double.parseDouble(controller.beratField.getText().replaceAll(",", "")));
                                        d.setKondisi(controller.kondisiCombo.getSelectionModel().getSelectedItem());
                                        d.setStatusSurat(controller.statusSuratCombo.getSelectionModel().getSelectedItem());
                                        d.setHargaKomp(hargaKomp);
                                        d.setHargaBeli(Double.parseDouble(controller.hargaField.getText().replaceAll(",", "")));
                                        detailTable.refresh();
                                        hitungTotal();
                                    } else {
                                        mainApp.showMessage(Modality.NONE, "Warning", "Verifikasi salah");
                                    }
                                }
                            });
                        } else {
                            d.setKodeKategori(controller.kodeKategoriCombo.getSelectionModel().getSelectedItem());
                            d.setNamaBarang(controller.namaBarangField.getText());
                            d.setQty(Integer.parseInt(controller.jumlahField.getText().replaceAll(",", "")));
                            d.setBerat(Double.parseDouble(controller.beratField.getText().replaceAll(",", "")));
                            d.setKondisi(controller.kondisiCombo.getSelectionModel().getSelectedItem());
                            d.setStatusSurat(controller.statusSuratCombo.getSelectionModel().getSelectedItem());
                            d.setHargaKomp(hargaKomp);
                            d.setHargaBeli(Double.parseDouble(controller.hargaField.getText().replaceAll(",", "")));
                            detailTable.refresh();
                            hitungTotal();
                        }
                    } catch (Exception e) {
                        mainApp.showMessage(Modality.NONE, "Error", e.toString());
                    }
                }
            });
        }
    }

    @FXML
    private void hapusBarang() {
        if (detailTable.getSelectionModel().getSelectedItem() != null) {
            allDetail.remove(detailTable.getSelectionModel().getSelectedItem());
            hitungTotal();
            detailTable.refresh();
        }
    }

    @FXML
    private void reset() {
        allDetail.clear();
        namaField.setText("");
        alamatDesaField.setText("");
        detailTable.refresh();
        hitungTotal();
    }

    @FXML
    private void savePembelian() {
        if (allDetail.isEmpty()) {
            mainApp.showMessage(Modality.NONE, "Warning", "Detail pembelian masih kosong");
        } else {
            Stage child = new Stage();
            FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/Verifikasi.fxml");
            VerifikasiController controller = loader.getController();
            controller.setMainApp(mainApp, child);
            controller.keteranganLabel.setText("Verifikasi Sales : ");
            controller.password.setOnKeyPressed((KeyEvent keyEvent) -> {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    child.close();
                    String sales = Function.cekSales(controller.password.getText());
                    if (sales != null) {
                        try (Connection con = Koneksi.getConnection()) {
                            PembelianHead p = new PembelianHead();
                            p.setTglPembelian(Function.getSystemDate());
                            p.setKodeSales(sales);
                            p.setNama(namaField.getText());
                            p.setAlamat(alamatDesaField.getText());
                            int totalQty = 0;
                            double totalBerat = 0;
                            double totalHarga = 0;
                            for (PembelianDetail d : allDetail) {
                                totalQty = totalQty + d.getQty();
                                totalBerat = totalBerat + d.getBerat();
                                totalHarga = totalHarga + d.getHargaBeli();
                            }
                            p.setTotalQty(totalQty);
                            p.setTotalBerat(totalBerat);
                            p.setTotalPembelian(totalHarga);
                            p.setCatatan("");
                            p.setStatus("true");
                            p.setTglBatal("2000-01-01 00:00:00");
                            p.setUserBatal("");
                            for (PembelianDetail d : allDetail) {
                                d.setPembelianHead(p);
                            }
                            p.setListPembelianDetail(allDetail);
                            String status = Service.savePembelian(con, p);
                            if (status.equals("true")) {
                                if (printCheckBox.isSelected()) {
                                    PrintOut printOut = new PrintOut();
                                    printOut.printBuktiPembelian(allDetail);
                                }
                                mainApp.showMessage(Modality.NONE, "Success", "Pembelian berhasil disimpan");
                                reset();
                            } else {
                                mainApp.showMessage(Modality.NONE, "Error", status);
                            }
                        } catch (Exception e) {
                            mainApp.showMessage(Modality.NONE, "Warning", e.toString());
                        }
                    } else {
                        mainApp.showMessage(Modality.NONE, "Warning", "Verifikasi salah");
                    }
                }
            });
        }
    }

}
