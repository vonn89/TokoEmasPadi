/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Report;

import com.excellentsystem.TokoEmasPadi.DAO.BarangDAO;
import com.excellentsystem.TokoEmasPadi.DAO.HancurDetailDAO;
import com.excellentsystem.TokoEmasPadi.DAO.HancurHeadDAO;
import com.excellentsystem.TokoEmasPadi.Function;
import static com.excellentsystem.TokoEmasPadi.Function.getTableCell;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import static com.excellentsystem.TokoEmasPadi.Main.tglLengkap;
import static com.excellentsystem.TokoEmasPadi.Main.tglSql;
import com.excellentsystem.TokoEmasPadi.Model.Barang;
import com.excellentsystem.TokoEmasPadi.Model.HancurDetail;
import com.excellentsystem.TokoEmasPadi.Model.HancurHead;
import com.excellentsystem.TokoEmasPadi.View.Dialog.DetailBarangController;
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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Yunaz
 */
public class LaporanHancurBarangController {

    @FXML
    private TableView<HancurDetail> hancurDetailTable;
    @FXML
    private TableColumn<HancurDetail, String> tglHancurColumn;
    @FXML
    private TableColumn<HancurDetail, String> kodeUserColumn;
    @FXML
    private TableColumn<HancurDetail, Number> qtyColumn;
    @FXML
    private TableColumn<HancurDetail, String> kodeBarcodeColumn;
    @FXML
    private TableColumn<HancurDetail, String> namaBarangColumn;
    @FXML
    private TableColumn<HancurDetail, String> namaBarcodeColumn;
    @FXML
    private TableColumn<HancurDetail, String> kodeKategoriColumn;
    @FXML
    private TableColumn<HancurDetail, String> kodeJenisColumn;
    @FXML
    private TableColumn<HancurDetail, String> keteranganColumn;
    @FXML
    private TableColumn<HancurDetail, String> merkColumn;
    @FXML
    private TableColumn<HancurDetail, Number> beratColumn;
    @FXML
    private TableColumn<HancurDetail, Number> beratPembulatanColumn;
    @FXML
    private TableColumn<HancurDetail, Number> nilaiPokokColumn;
    @FXML
    private TableColumn<HancurDetail, Number> hargaJualColumn;
    @FXML
    private TableColumn<HancurDetail, String> userBarcodeColumn;
    @FXML
    private TableColumn<HancurDetail, String> tglBarcodeColumn;

    @FXML
    private Label totalQtyLabel;
    @FXML
    private Label totalBeratLabel;
    @FXML
    private Label totalBeratPembulatanLabel;
    @FXML
    private DatePicker tglStartPicker;
    @FXML
    private DatePicker tglAkhirPicker;
    @FXML
    private TextField searchField;
    private Main mainApp;
    private ObservableList<HancurDetail> allHancur = FXCollections.observableArrayList();
    private ObservableList<HancurDetail> filterData = FXCollections.observableArrayList();

    public void initialize() {
        tglHancurColumn.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(tglLengkap.format(
                        tglSql.parse(cellData.getValue().getHancurHead().getTglHancur())));
            } catch (ParseException ex) {
                return null;
            }
        });
        kodeUserColumn.setCellValueFactory(cellData -> cellData.getValue().getHancurHead().kodeUserProperty());

        qtyColumn.setCellValueFactory(cellData -> cellData.getValue().qtyProperty());
        qtyColumn.setCellFactory(col -> getTableCell(rp, ""));
        kodeBarcodeColumn.setCellValueFactory(cellData -> cellData.getValue().kodeBarcodeProperty());
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().namaBarangProperty());
        namaBarcodeColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().namaBarcodeProperty());
        kodeKategoriColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().kodeKategoriProperty());
        kodeJenisColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().kodeJenisProperty());
        keteranganColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().keteranganProperty());
        merkColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().merkProperty());
        beratColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().beratProperty());
        beratColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        beratPembulatanColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().beratPembulatanProperty());
        beratPembulatanColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        nilaiPokokColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().nilaiPokokProperty());
        nilaiPokokColumn.setCellFactory(col -> getTableCell(rp, "Rp"));
        hargaJualColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().hargaJualProperty());
        hargaJualColumn.setCellFactory(col -> getTableCell(rp, "Rp"));
        userBarcodeColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().barcodeByProperty());
        tglBarcodeColumn.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(tglLengkap.format(
                        tglSql.parse(cellData.getValue().getBarang().getBarcodeDate())));
            } catch (ParseException ex) {
                return null;
            }
        });

        tglStartPicker.setConverter(Function.getTglConverter());
        tglStartPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglStartPicker.setDayCellFactory((final DatePicker datePicker)
                -> Function.getDateCellMulai(tglAkhirPicker, LocalDate.parse(sistem.getTglSystem())));
        tglAkhirPicker.setConverter(Function.getTglConverter());
        tglAkhirPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglAkhirPicker.setDayCellFactory((final DatePicker datePicker)
                -> Function.getDateCellAkhir(tglStartPicker, LocalDate.parse(sistem.getTglSystem())));

        allHancur.addListener((ListChangeListener.Change<? extends HancurDetail> change) -> {
            searchHancur();
        });
        searchField.textProperty().addListener(
                (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    searchHancur();
                });
        filterData.addAll(allHancur);
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            getHancurBarang();
        });
        rowMenu.getItems().addAll(refresh);
        hancurDetailTable.setContextMenu(rowMenu);
        hancurDetailTable.setRowFactory(table -> {
            TableRow<HancurDetail> row = new TableRow<HancurDetail>() {
                @Override
                public void updateItem(HancurDetail item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rowMenu);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem detail = new MenuItem("Detail Barang");
                        detail.setOnAction((ActionEvent e) -> {
                            detailBarang(item.getBarang());
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            getHancurBarang();
                        });
                        rowMenu.getItems().addAll(detail);
                        rowMenu.getItems().addAll(refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            return row;
        });
    }

    public void setMainApp(Main mainApp) {
        try {
            this.mainApp = mainApp;
            hancurDetailTable.setItems(filterData);
            getHancurBarang();
        } catch (Exception e) {
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }

    @FXML
    private void getHancurBarang() {
        try (Connection con = Koneksi.getConnection()) {
            allHancur.clear();
            List<HancurHead> listHancurHead = HancurHeadDAO.getAllByTglHancur(con,
                    tglStartPicker.getValue().toString(), tglAkhirPicker.getValue().toString());
            List<HancurDetail> listHancurDetail = HancurDetailDAO.getAllByTglHancur(con,
                    tglStartPicker.getValue().toString(), tglAkhirPicker.getValue().toString());
            for (HancurDetail d : listHancurDetail) {
                for (HancurHead h : listHancurHead) {
                    if (h.getNoHancur().equals(d.getNoHancur())) {
                        d.setHancurHead(h);
                    }
                }
                Barang b = BarangDAO.get(con, d.getKodeBarcode());
                d.setBarang(b);
            }
            allHancur.addAll(listHancurDetail);
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

    private void searchHancur() {
        try {
            filterData.clear();
            for (HancurDetail temp : allHancur) {
                if (searchField.getText() == null || searchField.getText().equals("")) {
                    filterData.add(temp);
                } else {
                    if (checkColumn(temp.getNoHancur())
                            || checkColumn(tglLengkap.format(tglSql.parse(temp.getHancurHead().getTglHancur())))
                            || checkColumn(rp.format(temp.getQty()))
                            || checkColumn(temp.getKodeBarcode())
                            || checkColumn(tglLengkap.format(tglSql.parse(temp.getBarang().getBarcodeDate())))
                            || checkColumn(temp.getBarang().getNamaBarang())
                            || checkColumn(temp.getBarang().getNamaBarcode())
                            || checkColumn(temp.getBarang().getKeterangan())
                            || checkColumn(temp.getBarang().getKodeKategori())
                            || checkColumn(temp.getBarang().getKodeJenis())
                            || checkColumn(temp.getBarang().getKeterangan())
                            || checkColumn(temp.getBarang().getMerk())
                            || checkColumn(gr.format(temp.getBarang().getBerat()))
                            || checkColumn(gr.format(temp.getBarang().getBeratPembulatan()))
                            || checkColumn(rp.format(temp.getBarang().getNilaiPokok()))
                            || checkColumn(rp.format(temp.getBarang().getHargaJual()))
                            || checkColumn(temp.getBarang().getBarcodeBy())
                            || checkColumn(temp.getHancurHead().getKodeUser())) {
                        filterData.add(temp);
                    }

                }
            }
            hitungTotal();
        } catch (Exception e) {
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }

    private void hitungTotal() {
        int qty = 0;
        double berat = 0;
        double beratPembulatan = 0;
        for (HancurDetail h : filterData) {
            qty = qty + h.getQty();
            berat = berat + h.getBarang().getBerat();
            beratPembulatan = beratPembulatan + h.getBarang().getBeratPembulatan();
        }
        totalQtyLabel.setText(gr.format(qty));
        totalBeratLabel.setText(gr.format(berat) + " gr");
        totalBeratPembulatanLabel.setText(gr.format(beratPembulatan) + " gr");
    }
    private void detailBarang(Barang b){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/DetailBarang.fxml");
        DetailBarangController controller = loader.getController();
        controller.setMainApp(mainApp, child);
        controller.setBarang(b);
        controller.setViewOnly();
    }
}
