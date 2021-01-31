/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View;

import com.excellentsystem.TokoEmasPadi.DAO.BarangDAO;
import com.excellentsystem.TokoEmasPadi.Function;
import static com.excellentsystem.TokoEmasPadi.Function.getTableCell;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import com.excellentsystem.TokoEmasPadi.Model.Barang;
import com.excellentsystem.TokoEmasPadi.Model.PenjualanDetail;
import com.excellentsystem.TokoEmasPadi.Model.PenjualanHead;
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
public class PenjualanController {

    @FXML
    private TableView<PenjualanDetail> detailTable;
    @FXML
    private TableColumn<PenjualanDetail, String> kodeBarcodeColumn;
    @FXML
    private TableColumn<PenjualanDetail, Number> jumlahColumn;
    @FXML
    private TableColumn<PenjualanDetail, String> namaBarangColumn;
    @FXML
    private TableColumn<PenjualanDetail, Number> beratColumn;
    @FXML
    private TableColumn<PenjualanDetail, Number> hargaColumn;
    @FXML
    private TableColumn<PenjualanDetail, Number> totalColumn;

    @FXML
    private Label totalHargaLabel;

    @FXML
    private TextField namaField;
    @FXML
    private TextField alamatField;

    @FXML
    private TextField kodeBarcodeField;
    @FXML
    private Button ubahButton;
    @FXML
    private Button hapusButton;

    @FXML
    private CheckBox printCheckBox;
    private ObservableList<PenjualanDetail> allDetail = FXCollections.observableArrayList();
    private Main mainApp;

    public void initialize() {
        kodeBarcodeColumn.setCellValueFactory(cellData -> cellData.getValue().kodeBarcodeProperty());
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().namaBarangProperty());
        jumlahColumn.setCellValueFactory(cellData -> cellData.getValue().qtyProperty());
        jumlahColumn.setCellFactory(col -> getTableCell(rp, ""));
        beratColumn.setCellValueFactory(cellData -> cellData.getValue().beratPembulatanProperty());
        beratColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        hargaColumn.setCellValueFactory(cellData -> cellData.getValue().hargaJualProperty());
        hargaColumn.setCellFactory(col -> getTableCell(rp, "Rp"));
        totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty());
        totalColumn.setCellFactory(col -> getTableCell(rp, "Rp"));
        namaField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                alamatField.selectAll();
                alamatField.requestFocus();
            }
        });
        alamatField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                kodeBarcodeField.selectAll();
                kodeBarcodeField.requestFocus();
            }
        });
        kodeBarcodeField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                getBarang();
            }
        });
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem cari = new MenuItem("Cari Barang");
        cari.setOnAction((ActionEvent event) -> {
            cariBarang();
        });
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            detailTable.refresh();
        });
        rowMenu.getItems().addAll(cari, refresh);
        detailTable.setContextMenu(rowMenu);
        detailTable.setRowFactory(table -> {
            TableRow<PenjualanDetail> row = new TableRow<PenjualanDetail>() {
                @Override
                public void updateItem(PenjualanDetail item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rowMenu);
                    } else {
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem cari = new MenuItem("Cari Barang");
                        cari.setOnAction((ActionEvent event) -> {
                            cariBarang();
                        });
                        MenuItem lihat = new MenuItem("Ubah Barang");
                        lihat.setOnAction((ActionEvent event) -> {
                            lihatBarang();
                        });
                        MenuItem hapus = new MenuItem("Hapus Barang");
                        hapus.setOnAction((ActionEvent event) -> {
                            hapusBarang();
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            detailTable.refresh();
                        });
                        rowMenu.getItems().addAll(cari, lihat, hapus, refresh);
                        setContextMenu(rowMenu);

                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)
                        && mouseEvent.getClickCount() == 2) {
                    if (row.getItem() != null) {
                        lihatBarang();
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
        for (PenjualanDetail d : allDetail) {
            totalHarga = totalHarga + d.getHargaJual() * d.getQty();
        }
        totalHargaLabel.setText(rp.format(totalHarga));
    }

    @FXML
    private void cariBarang() {
        Stage cariBarangStage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, cariBarangStage, "View/Dialog/CariBarang.fxml");
        CariBarangController controller = loader.getController();
        controller.setMainApp(mainApp, cariBarangStage);

        final ContextMenu rowMenu = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent e) -> {
            controller.getBarang();
        });
        rowMenu.getItems().addAll(refresh);
        controller.barangTable.setContextMenu(rowMenu);
        controller.barangTable.setRowFactory(table -> {
            TableRow<Barang> row = new TableRow<Barang>() {
                @Override
                public void updateItem(Barang item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rowMenu);
                    } else {
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem detail = new MenuItem("Detail Barang");
                        detail.setOnAction((ActionEvent e) -> {
                            Stage child = new Stage();
                            FXMLLoader loader = mainApp.showDialog(cariBarangStage, child, "View/Dialog/DetailBarang.fxml");
                            DetailBarangController controller = loader.getController();
                            controller.setMainApp(mainApp, child);
                            controller.setBarang(item);
                            controller.setViewOnly();
                        });
                        MenuItem pilih = new MenuItem("Pilih Barang");
                        pilih.setOnAction((ActionEvent e) -> {
                            cariBarangStage.close();
                            kodeBarcodeField.setText(item.getKodeBarcode());
                            getBarang();
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            controller.getBarang();
                        });
                        rowMenu.getItems().addAll(pilih, detail, refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)
                        && mouseEvent.getClickCount() == 2) {
                    if (row.getItem() != null) {
                        cariBarangStage.close();
                        kodeBarcodeField.setText(row.getItem().getKodeBarcode());
                        getBarang();
                    }
                }
            });
            return row;
        });
    }

    @FXML
    private void getBarang() {
        if (allDetail.size() >= 3) {
            mainApp.showMessage(Modality.NONE, "Warning", "satu surat penjualan maksimal 3 macam barang");
        } else if ("".equals(kodeBarcodeField.getText())) {
            mainApp.showMessage(Modality.NONE, "Warning", "Kode Barcode masih kosong");
        } else {
            try (Connection con = Koneksi.getConnection()) {
                Barang b = BarangDAO.get(con, kodeBarcodeField.getText());
                if (b == null) {
                    mainApp.showMessage(Modality.NONE, "Warning", "Kode Barcode tidak ditemukan");
                } else if (b.getQty() <= 0) {
                    mainApp.showMessage(Modality.NONE, "Warning", "Stok barang sudah tidak ada");
                } else {
                    Stage child = new Stage();
                    FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/AddBarang.fxml");
                    AddBarangController controller = loader.getController();
                    controller.setMainApp(mainApp, child);
                    controller.setBarang(b);
                    controller.saveButton.setOnAction((event) -> {
                        int qty = 0;
                        for (PenjualanDetail d : allDetail) {
                            if (d.getKodeBarcode().equals(b.getKodeBarcode())) {
                                qty = qty + d.getQty();
                            }
                        }
                        if ("".equals(controller.jumlahField.getText()) || Integer.parseInt(controller.jumlahField.getText().replaceAll(",", "")) <= 0) {
                            mainApp.showMessage(Modality.NONE, "Warning", "Jumlah masih kosong");
                        } else if (b.getQty() < qty + Integer.parseInt(controller.jumlahField.getText().replaceAll(",", ""))) {
                            mainApp.showMessage(Modality.NONE, "Warning", "Stok barang tidak mencukupi");
                        } else if ("".equals(controller.hargaField.getText()) || Double.parseDouble(controller.hargaField.getText().replaceAll(",", "")) <= 0) {
                            mainApp.showMessage(Modality.NONE, "Warning", "Harga masih kosong");
                        } else {
                            child.close();
                            if (Double.parseDouble(controller.hargaField.getText().replaceAll(",", "")) < b.getHargaJual()) {
                                Stage verifikasi = new Stage();
                                FXMLLoader verifikasiLoader = mainApp.showDialog(mainApp.MainStage, verifikasi, "View/Dialog/Verifikasi.fxml");
                                VerifikasiController verifikasiController = verifikasiLoader.getController();
                                verifikasiController.setMainApp(mainApp, verifikasi);
                                verifikasiController.keteranganLabel.setText(
                                        "Harga jual : Rp " + controller.hargaField.getText() + "\n"
                                        + "Harga komp : Rp " + rp.format(b.getHargaJual()) + "\n"
                                        + "Verifikasi : ");
                                verifikasiController.password.setOnKeyPressed((KeyEvent keyEvent) -> {
                                    if (keyEvent.getCode() == KeyCode.ENTER) {
                                        verifikasi.close();
                                        User user = Function.verifikasi(verifikasiController.password.getText(), "Penjualan");
                                        if (user != null) {
                                            addDetail(b, Integer.parseInt(controller.jumlahField.getText().replaceAll(",", "")),
                                                    Double.parseDouble(controller.hargaField.getText().replaceAll(",", "")));
                                        } else {
                                            mainApp.showMessage(Modality.NONE, "Warning", "Verifikasi salah");
                                        }
                                    }
                                });
                            } else {
                                addDetail(b, Integer.parseInt(controller.jumlahField.getText().replaceAll(",", "")),
                                        Double.parseDouble(controller.hargaField.getText().replaceAll(",", "")));
                            }
                        }
                    });
                }
            } catch (Exception e) {
                mainApp.showMessage(Modality.NONE, "Error", e.toString());
            }
        }
    }

    private void addDetail(Barang b, int jumlah, double harga) {
        boolean input = true;
        for (PenjualanDetail d : allDetail) {
            if (d.getKodeBarcode().equals(b.getKodeBarcode()) && d.getHargaJual() == harga) {
                input = false;
                d.setQty(d.getQty() + jumlah);
                d.setTotal(harga * d.getQty());
            }
        }
        if (input) {
            PenjualanDetail d = new PenjualanDetail();
            d.setKodeBarcode(b.getKodeBarcode());
            d.setNamaBarang(b.getNamaBarang());
            d.setKodeKategori(b.getKodeKategori());
            d.setKodeJenis(b.getKodeJenis());
            d.setQty(jumlah);
            d.setBerat(b.getBerat());
            d.setBeratPembulatan(b.getBeratPembulatan());
            d.setNilaiPokok(b.getNilaiPokok());
            d.setHargaKomp(b.getHargaJual());
            d.setHargaJual(harga);
            d.setTotal(harga * jumlah);
            d.setNoPembelian("");
            d.setBarang(b);
            allDetail.add(d);
        }
        detailTable.refresh();
        hitungTotal();
        kodeBarcodeField.setText("");
        kodeBarcodeField.requestFocus();
    }

    @FXML
    private void lihatBarang() {
        if (detailTable.getSelectionModel().getSelectedItem() != null) {
            PenjualanDetail d = detailTable.getSelectionModel().getSelectedItem();
            Stage child = new Stage();
            FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/AddBarang.fxml");
            AddBarangController controller = loader.getController();
            controller.setMainApp(mainApp, child);
            controller.setDetailPenjualan(d);
            controller.saveButton.setOnAction((event) -> {
                if ("".equals(controller.jumlahField.getText()) || Integer.parseInt(controller.jumlahField.getText().replaceAll(",", "")) <= 0) {
                    mainApp.showMessage(Modality.NONE, "Warning", "Jumlah masih kosong");
                } else if (d.getBarang().getQty() < Integer.parseInt(controller.jumlahField.getText().replaceAll(",", ""))) {
                    mainApp.showMessage(Modality.NONE, "Warning", "Stok barang tidak mencukupi");
                } else if ("".equals(controller.hargaField.getText()) || Double.parseDouble(controller.hargaField.getText().replaceAll(",", "")) <= 0) {
                    mainApp.showMessage(Modality.NONE, "Warning", "Harga masih kosong");
                } else {
                    child.close();
                    if (Double.parseDouble(controller.hargaField.getText().replaceAll(",", "")) < d.getBarang().getHargaJual()) {
                        Stage verifikasi = new Stage();
                        FXMLLoader verifikasiLoader = mainApp.showDialog(mainApp.MainStage, verifikasi, "View/Dialog/Verifikasi.fxml");
                        VerifikasiController verifikasiController = verifikasiLoader.getController();
                        verifikasiController.setMainApp(mainApp, verifikasi);
                        verifikasiController.keteranganLabel.setText(
                                "Harga jual : Rp " + controller.hargaField.getText() + "\n"
                                + "Harga komp : Rp " + rp.format(d.getBarang().getHargaJual()) + "\n"
                                + "Verifikasi : ");
                        verifikasiController.password.setOnKeyPressed((KeyEvent keyEvent) -> {
                            if (keyEvent.getCode() == KeyCode.ENTER) {
                                verifikasi.close();
                                User user = Function.verifikasi(verifikasiController.password.getText(), "Penjualan");
                                if (user != null) {
                                    d.setQty(Integer.parseInt(controller.jumlahField.getText().replaceAll(",", "")));
                                    d.setHargaJual(Double.parseDouble(controller.hargaField.getText().replaceAll(",", "")));
                                    d.setTotal(d.getHargaJual() * d.getQty());
                                    detailTable.refresh();
                                    hitungTotal();
                                    kodeBarcodeField.setText("");
                                    kodeBarcodeField.requestFocus();
                                } else {
                                    mainApp.showMessage(Modality.NONE, "Warning", "Verifikasi salah");
                                }
                            }
                        });
                    } else {
                        d.setQty(Integer.parseInt(controller.jumlahField.getText().replaceAll(",", "")));
                        d.setHargaJual(Double.parseDouble(controller.hargaField.getText().replaceAll(",", "")));
                        d.setTotal(d.getHargaJual() * d.getQty());
                        detailTable.refresh();
                        hitungTotal();
                        kodeBarcodeField.setText("");
                        kodeBarcodeField.requestFocus();
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
        alamatField.setText("");
        detailTable.refresh();
        hitungTotal();
        kodeBarcodeField.setText("");
        kodeBarcodeField.requestFocus();
    }

    @FXML
    private void savePenjualan() {
        if (allDetail.isEmpty()) {
            mainApp.showMessage(Modality.NONE, "Warning", "Detail penjualan masih kosong");
        } else {
            Stage diskonStage = new Stage();
            FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, diskonStage, "View/Dialog/Diskon.fxml");
            DiskonController diskonController = loader.getController();
            diskonController.setMainApp(mainApp, diskonStage);
            diskonController.sepuluhRibuButton.setOnAction((event) -> {
                diskonStage.close();
                save(10000);
            });
            diskonController.limaRibuButton.setOnAction((event) -> {
                diskonStage.close();
                save(5000);
            });
            diskonController.nolButton.setOnAction((event) -> {
                diskonStage.close();
                save(0);
            });
            
        }
    }
    private void save(double diskon){
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
                        PenjualanHead p = new PenjualanHead();
                        p.setTglPenjualan(Function.getSystemDate());
                        p.setKodeSales(sales);
                        p.setNama(namaField.getText());
                        p.setAlamat(alamatField.getText());
                        int totalQty = 0;
                        double totalBerat = 0;
                        double totalBeratPembulatan = 0;
                        double totalHarga = 0;
                        for (PenjualanDetail d : allDetail) {
                            totalQty = totalQty + d.getQty();
                            totalBerat = totalBerat + d.getBerat();
                            totalBeratPembulatan = totalBeratPembulatan + d.getBeratPembulatan();
                            totalHarga = totalHarga + d.getTotal();
                        }
                        p.setTotalQty(totalQty);
                        p.setTotalBerat(totalBerat);
                        p.setTotalBeratPembulatan(totalBeratPembulatan);
                        p.setGrandtotal(totalHarga);
                        p.setDiskon(diskon);
                        p.setCatatan("");
                        p.setStatus("true");
                        p.setTglBatal("2000-01-01 00:00:00");
                        p.setUserBatal("");
                        p.setListPenjualanDetail(allDetail);
                        String status = Service.savePenjualan(con, p);
                        if (status.equals("true")) {
                            if (printCheckBox.isSelected()) {
                                int i = 0;
                                for (PenjualanDetail d : allDetail) {
                                    d.setPenjualanHead(p);
                                    i++;
                                }
                                while(i<5){
                                    PenjualanDetail detail = new PenjualanDetail();
                                    detail.setPenjualanHead(p);
                                    detail.setNamaBarang("");
                                    detail.setQty(0);
                                    detail.setKodeBarcode("");
                                    detail.setBeratPembulatan(0);
                                    detail.setHargaJual(0);
                                    allDetail.add(detail);
                                    i++;
                                }
                                PrintOut printOut = new PrintOut();
                                printOut.printSuratPenjualan(allDetail);
                            }
                            mainApp.showMessage(Modality.NONE, "Success", "Penjualan berhasil disimpan");
                            reset();
                        } else {
                            mainApp.showMessage(Modality.NONE, "Error", status);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mainApp.showMessage(Modality.NONE, "Error", e.toString());
                    }
                } else {
                    mainApp.showMessage(Modality.NONE, "Warning", "Verifikasi salah");
                }
            }
        });
    }
}
