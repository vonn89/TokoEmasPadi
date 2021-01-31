/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View;

import com.excellentsystem.TokoEmasPadi.DAO.KategoriDAO;
import com.excellentsystem.TokoEmasPadi.DAO.KeuanganDAO;
import com.excellentsystem.TokoEmasPadi.DAO.PenjualanDetailDAO;
import static com.excellentsystem.TokoEmasPadi.Function.getTableCell;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import static com.excellentsystem.TokoEmasPadi.Main.user;
import com.excellentsystem.TokoEmasPadi.Model.Kategori;
import com.excellentsystem.TokoEmasPadi.Model.Keuangan;
import com.excellentsystem.TokoEmasPadi.Model.Otoritas;
import com.excellentsystem.TokoEmasPadi.Model.PenjualanDetail;
import static java.lang.Math.abs;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;

/**
 * FXML Controller class
 *
 * @author excellent
 */
public class DashboardController {

    @FXML
    private Label totalPenjualanLabel;
    @FXML
    private Label totalPembelianLabel;
    @FXML
    private Label totalTerimaGadaiLabel;
    @FXML
    private Label totalPelunasanGadaiLabel;
    @FXML
    private Label totalBungaGadaiLabel;

    @FXML
    private LineChart<String, Double> omzetPenjualanChart;
    @FXML
    private CategoryAxis periodeOmzetAxis;

    @FXML
    private PieChart bestSellingItemChart;

    @FXML
    private StackedBarChart<String, Double> salesPerformanceChart;
    @FXML
    private CategoryAxis salesAxis;

    @FXML
    private TableView<Kategori> kategoriTable;
    @FXML
    private TableColumn<Kategori, String> kodeKategoriColumn;
    @FXML
    private TableColumn<Kategori, Number> hargaBeliColumn;
    @FXML
    private TableColumn<Kategori, Number> hargaJualColumn;

    @FXML
    private ComboBox<String> periodeCombo;
    private ObservableList<String> periode = FXCollections.observableArrayList();
    private String tglAwal = sistem.getTglSystem();
    private String tglAkhir = sistem.getTglSystem();

    private ObservableList<Kategori> allKategori = FXCollections.observableArrayList();
    private Main mainApp;

    class HoveredThresholdNode extends StackPane {

        HoveredThresholdNode(double value) {
            setPrefSize(15, 15);
            final Label label = createDataThresholdLabel(value);
            setOnMouseEntered((MouseEvent mouseEvent) -> {
                getChildren().setAll(label);
                setCursor(Cursor.NONE);
                toFront();
            });
            setOnMouseExited((MouseEvent mouseEvent) -> {
                getChildren().clear();
                setCursor(Cursor.CROSSHAIR);
            });
        }

        private Label createDataThresholdLabel(double value) {
            final Label label = new Label(rp.format(value));
            label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
            label.setStyle("-fx-font-size: 14; -font-weight: bold;");
            label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
            return label;
        }
    }

    public void initialize() {
        kategoriTable.setItems(allKategori);
        kodeKategoriColumn.setCellValueFactory(cellData -> cellData.getValue().kodeKategoriProperty());
        hargaBeliColumn.setCellValueFactory(cellData -> cellData.getValue().hargaBeliProperty());
        hargaBeliColumn.setCellFactory(col -> getTableCell(rp, "Rp"));
        hargaJualColumn.setCellValueFactory(cellData -> cellData.getValue().hargaJualProperty());
        hargaJualColumn.setCellFactory(col -> getTableCell(rp, "Rp"));

        bestSellingItemChart.setLabelsVisible(true);
        
        ContextMenu menu = new ContextMenu();
        MenuItem update = new MenuItem("Update Harga");
        update.setOnAction((ActionEvent) -> {
            updateHarga();
        });
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent) -> {
            getKategori();
        });
        for (Otoritas o : user.getOtoritas()) {
            if (o.getJenis().equals("Pengaturan Umum")) {
                menu.getItems().add(update);
            }
        }
        menu.getItems().add(refresh);
        kategoriTable.setContextMenu(menu);
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        periode.clear();
        periode.add("This Day");
        periode.add("This Month");
        periode.add("Last 6 Months");
        periode.add("Last 12 Months");
        periode.add("This Year");
        periodeCombo.setItems(periode);
        periodeCombo.getSelectionModel().selectFirst();
        getData();
    }

    @FXML
    private void getData() {
        try (Connection con = Koneksi.getConnection()) {
            LocalDate localDate = LocalDate.parse(sistem.getTglSystem(), DateTimeFormatter.ISO_DATE);
            tglAwal = localDate.toString();
            tglAkhir = localDate.toString();
            DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyy-MM");
            DateTimeFormatter yyyy = DateTimeFormatter.ofPattern("yyyy");
            if (periodeCombo.getSelectionModel().getSelectedItem().equals("This Day")) {
                tglAwal = tglAkhir;
            } else if (periodeCombo.getSelectionModel().getSelectedItem().equals("This Month")) {
                tglAwal = localDate.format(yyyyMM) + "-01";
            } else if (periodeCombo.getSelectionModel().getSelectedItem().equals("Last 6 Months")) {
                tglAwal = localDate.minusMonths(5).format(yyyyMM) + "-01";
            } else if (periodeCombo.getSelectionModel().getSelectedItem().equals("Last 12 Months")) {
                tglAwal = localDate.minusMonths(11).format(yyyyMM) + "-01";
            } else if (periodeCombo.getSelectionModel().getSelectedItem().equals("This Year")) {
                tglAwal = localDate.format(yyyy) + "-01-01";
            }

            List<PenjualanDetail> listPenjualanDetail = PenjualanDetailDAO.getAllByTglPenjualanAndStatus(con, tglAwal, tglAkhir, "true");

            List<Keuangan> listKeuangan = KeuanganDAO.getAllByDateAndKategori(con, tglAwal, tglAkhir, "%");
            setLabelHeader(listKeuangan);
            setOmzetPenjualan(listKeuangan);
            setSalesPerformance(listKeuangan);
            setBestSellingItems(listPenjualanDetail);
            getKategori();
        } catch (Exception ex) {
            mainApp.showMessage(Modality.NONE, "Error", ex.toString());
        }
    }

    private void setLabelHeader(List<Keuangan> listKeuangan) {
        double totalPenjualan = 0;
        double totalPembelian = 0;
        double totalTerimaGadai = 0;
        double totalPelunasanGadai = 0;
        double totalBungaGadai = 0;
        for (Keuangan k : listKeuangan) {
            if (k.getKategori().equals("Penjualan") || k.getKategori().equals("Batal Penjualan")) {
                totalPenjualan = totalPenjualan + k.getJumlahRp();
            }
            if (k.getKategori().equals("Pembelian") || k.getKategori().equals("Batal Pembelian")) {
                totalPembelian = totalPembelian + k.getJumlahRp();
            }
            if (k.getKategori().equals("Terima Gadai") || k.getKategori().equals("Batal Terima Gadai")) {
                totalTerimaGadai = totalTerimaGadai + k.getJumlahRp();
            }
            if (k.getKategori().equals("Pelunasan Gadai") || k.getKategori().equals("Batal Pelunasan Gadai")) {
                totalPelunasanGadai = totalPelunasanGadai + k.getJumlahRp();
            }
            if (k.getKategori().equals("Bunga Gadai") || k.getKategori().equals("Batal Bunga Gadai")) {
                totalBungaGadai = totalBungaGadai + k.getJumlahRp();
            }
        }
        DecimalFormat df = new DecimalFormat("###,##0.##");
        String a = new DecimalFormat("###,##0").format(abs(totalPenjualan));
        String b = new DecimalFormat("###,##0").format(abs(totalPembelian));
        String c = new DecimalFormat("###,##0").format(abs(totalTerimaGadai));
        String d = new DecimalFormat("###,##0").format(abs(totalPelunasanGadai));
        String e = new DecimalFormat("###,##0").format(abs(totalBungaGadai));
        if (abs(totalPenjualan) / 1000000000 >= 1 || abs(totalPenjualan) / 1000000000 <= -1) {
            a = df.format(abs(totalPenjualan) / 1000000000) + " M";
        }
        if (abs(totalPembelian) / 1000000000 >= 1 || abs(totalPembelian) / 1000000000 <= -1) {
            b = df.format(abs(totalPembelian) / 1000000000) + " M";
        }
        if (abs(totalTerimaGadai) / 1000000000 >= 1 || abs(totalTerimaGadai) / 1000000000 <= -1) {
            c = df.format(abs(totalTerimaGadai) / 1000000000) + " M";
        }
        if (abs(totalPelunasanGadai) / 1000000000 >= 1 || abs(totalPelunasanGadai) / 1000000000 <= -1) {
            d = df.format(abs(totalPelunasanGadai) / 1000000000) + " M";
        }
        if (abs(totalBungaGadai) / 1000000000 >= 1 || abs(totalBungaGadai) / 1000000000 <= -1) {
            e = df.format(abs(totalBungaGadai) / 1000000000) + " M";
        }

        totalPenjualanLabel.setText(a);
        totalPembelianLabel.setText(b);
        totalTerimaGadaiLabel.setText(c);
        totalPelunasanGadaiLabel.setText(d);
        totalBungaGadaiLabel.setText(e);
    }

    private void setOmzetPenjualan(List<Keuangan> listKeuangan) throws Exception {
        omzetPenjualanChart.getData().clear();
        periodeOmzetAxis.getCategories().clear();
        for (Keuangan k : listKeuangan) {
            if (!periodeOmzetAxis.getCategories().contains(new SimpleDateFormat("MMM yyyy").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(k.getTglKeuangan())))) {
                periodeOmzetAxis.getCategories().add(new SimpleDateFormat("MMM yyyy").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(k.getTglKeuangan())));
            }
        }
        XYChart.Series series1 = new XYChart.Series<>();
        series1.setName("Penjualan");
        for (String s : periodeOmzetAxis.getCategories()) {
            double totalPenjualan = 0;
            for (Keuangan p : listKeuangan) {
                if (p.getKategori().equals("Penjualan") || p.getKategori().equals("Batal Penjualan")) {
                    if (s.equals(new SimpleDateFormat("MMM yyyy").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(p.getTglKeuangan())))) {
                        totalPenjualan = totalPenjualan + p.getJumlahRp();
                    }
                }
            }
            XYChart.Data<String, Double> data = new XYChart.Data<>(s, abs(totalPenjualan));
            data.setNode(
                    new HoveredThresholdNode(abs(totalPenjualan))
            );
            if (totalPenjualan != 0) {
                series1.getData().add(data);
            }
        }
        if (!series1.getData().isEmpty()) {
            omzetPenjualanChart.getData().add(series1);
        }

        XYChart.Series series2 = new XYChart.Series<>();
        series2.setName("Pembelian");
        for (String s : periodeOmzetAxis.getCategories()) {
            double totalPembelian = 0;
            for (Keuangan p : listKeuangan) {
                if (p.getKategori().equals("Pembelian") || p.getKategori().equals("Batal Pembelian")) {
                    if (s.equals(new SimpleDateFormat("MMM yyyy").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(p.getTglKeuangan())))) {
                        totalPembelian = totalPembelian + p.getJumlahRp();
                    }
                }
            }
            XYChart.Data<String, Double> data = new XYChart.Data<>(s, abs(totalPembelian));
            data.setNode(
                    new HoveredThresholdNode(abs(totalPembelian))
            );
            if (totalPembelian != 0) {
                series2.getData().add(data);
            }
        }
        if (!series2.getData().isEmpty()) {
            omzetPenjualanChart.getData().add(series2);
        }

        XYChart.Series series3 = new XYChart.Series<>();
        series3.setName("Terima Gadai");
        for (String s : periodeOmzetAxis.getCategories()) {
            double totalTerimaGadai = 0;
            for (Keuangan p : listKeuangan) {
                if (p.getKategori().equals("Terima Gadai") || p.getKategori().equals("Batal Terima Gadai")) {
                    if (s.equals(new SimpleDateFormat("MMM yyyy").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(p.getTglKeuangan())))) {
                        totalTerimaGadai = totalTerimaGadai + p.getJumlahRp();
                    }
                }
            }
            XYChart.Data<String, Double> data = new XYChart.Data<>(s, abs(totalTerimaGadai));
            data.setNode(
                    new HoveredThresholdNode(abs(totalTerimaGadai))
            );
            if (totalTerimaGadai != 0) {
                series3.getData().add(data);
            }
        }
        if (!series3.getData().isEmpty()) {
            omzetPenjualanChart.getData().add(series3);
        }

        XYChart.Series series4 = new XYChart.Series<>();
        series4.setName("Pelunasan Gadai");
        for (String s : periodeOmzetAxis.getCategories()) {
            double totalPelunasanGadai = 0;
            for (Keuangan p : listKeuangan) {
                if (p.getKategori().equals("Pelunasan Gadai") || p.getKategori().equals("Batal Pelunasan Gadai")) {
                    if (s.equals(new SimpleDateFormat("MMM yyyy").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(p.getTglKeuangan())))) {
                        totalPelunasanGadai = totalPelunasanGadai + p.getJumlahRp();
                    }
                }
            }
            XYChart.Data<String, Double> data = new XYChart.Data<>(s, abs(totalPelunasanGadai));
            data.setNode(
                    new HoveredThresholdNode(abs(totalPelunasanGadai))
            );
            if (totalPelunasanGadai != 0) {
                series4.getData().add(data);
            }
        }
        if (!series4.getData().isEmpty()) {
            omzetPenjualanChart.getData().add(series4);
        }

        XYChart.Series series5 = new XYChart.Series<>();
        series5.setName("Bunga Gadai");
        for (String s : periodeOmzetAxis.getCategories()) {
            double totalBungaGadai = 0;
            for (Keuangan p : listKeuangan) {
                if (p.getKategori().equals("Bunga Gadai") || p.getKategori().equals("Batal Bunga Gadai")) {
                    if (s.equals(new SimpleDateFormat("MMM yyyy").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(p.getTglKeuangan())))) {
                        totalBungaGadai = totalBungaGadai + p.getJumlahRp();
                    }
                }
            }
            XYChart.Data<String, Double> data = new XYChart.Data<>(s, abs(totalBungaGadai));
            data.setNode(
                    new HoveredThresholdNode(abs(totalBungaGadai))
            );
            if (totalBungaGadai != 0) {
                series5.getData().add(data);
            }
        }
        if (!series5.getData().isEmpty()) {
            omzetPenjualanChart.getData().add(series5);
        }
    }

    private void setSalesPerformance(List<Keuangan> listPenjualan) throws Exception {
        salesPerformanceChart.getData().clear();
        salesAxis.getCategories().clear();
        for (Keuangan p : listPenjualan) {
            if (!salesAxis.getCategories().contains(p.getKodeUser())) {
                salesAxis.getCategories().add(p.getKodeUser());
            }
        }
        XYChart.Series series1 = new XYChart.Series<>();
        series1.setName("Penjualan");
        for (String s : salesAxis.getCategories()) {
            double totalPenjualan = 0;
            for (Keuangan p : listPenjualan) {
                if (p.getKategori().equals("Penjualan") || p.getKategori().equals("Batal Penjualan")) {
                    if (s.equals(p.getKodeUser())) {
                        totalPenjualan = totalPenjualan + p.getJumlahRp();
                    }
                }
            }
            XYChart.Data<String, Double> data = new XYChart.Data<>(s, abs(totalPenjualan));
            data.setNode(
                    new HoveredThresholdNode(abs(totalPenjualan))
            );
            if (totalPenjualan != 0) {
                series1.getData().add(data);
            }
        }
        if (!series1.getData().isEmpty()) {
            salesPerformanceChart.getData().add(series1);
        }

        XYChart.Series series2 = new XYChart.Series<>();
        series2.setName("Pembelian");
        for (String s : salesAxis.getCategories()) {
            double totalPembelian = 0;
            for (Keuangan p : listPenjualan) {
                if (p.getKategori().equals("Pembelian") || p.getKategori().equals("Batal Pembelian")) {
                    if (s.equals(p.getKodeUser())) {
                        totalPembelian = totalPembelian + p.getJumlahRp();
                    }
                }
            }
            XYChart.Data<String, Double> data = new XYChart.Data<>(s, abs(totalPembelian));
            data.setNode(
                    new HoveredThresholdNode(abs(totalPembelian))
            );
            if (totalPembelian != 0) {
                series2.getData().add(data);
            }
        }
        if (!series2.getData().isEmpty()) {
            salesPerformanceChart.getData().add(series2);
        }

        XYChart.Series series3 = new XYChart.Series<>();
        series3.setName("Terima Gadai");
        for (String s : salesAxis.getCategories()) {
            double totalTerimaGadai = 0;
            for (Keuangan p : listPenjualan) {
                if (p.getKategori().equals("Terima Gadai") || p.getKategori().equals("Batal Terima Gadai")) {
                    if (s.equals(p.getKodeUser())) {
                        totalTerimaGadai = totalTerimaGadai + p.getJumlahRp();
                    }
                }
            }
            XYChart.Data<String, Double> data = new XYChart.Data<>(s, abs(totalTerimaGadai));
            data.setNode(
                    new HoveredThresholdNode(abs(totalTerimaGadai))
            );
            if (totalTerimaGadai != 0) {
                series3.getData().add(data);
            }
        }
        if (!series3.getData().isEmpty()) {
            salesPerformanceChart.getData().add(series3);
        }

        XYChart.Series series4 = new XYChart.Series<>();
        series4.setName("Pelunasan Gadai");
        for (String s : salesAxis.getCategories()) {
            double totalPelunasanGadai = 0;
            for (Keuangan p : listPenjualan) {
                if (p.getKategori().equals("Pelunasan Gadai") || p.getKategori().equals("Batal Pelunasan Gadai")) {
                    if (s.equals(p.getKodeUser())) {
                        totalPelunasanGadai = totalPelunasanGadai + p.getJumlahRp();
                    }
                }
            }
            XYChart.Data<String, Double> data = new XYChart.Data<>(s, abs(totalPelunasanGadai));
            data.setNode(
                    new HoveredThresholdNode(abs(totalPelunasanGadai))
            );
            if (totalPelunasanGadai != 0) {
                series4.getData().add(data);
            }
        }
        if (!series4.getData().isEmpty()) {
            salesPerformanceChart.getData().add(series4);
        }

        XYChart.Series series5 = new XYChart.Series<>();
        series5.setName("Bunga Gadai");
        for (String s : salesAxis.getCategories()) {
            double totalBungaGadai = 0;
            for (Keuangan p : listPenjualan) {
                if (p.getKategori().equals("Bunga Gadai") || p.getKategori().equals("Batal Bunga Gadai")) {
                    if (s.equals(p.getKodeUser())) {
                        totalBungaGadai = totalBungaGadai + p.getJumlahRp();
                    }
                }
            }
            XYChart.Data<String, Double> data = new XYChart.Data<>(s, abs(totalBungaGadai));
            data.setNode(
                    new HoveredThresholdNode(abs(totalBungaGadai))
            );
            if (totalBungaGadai != 0) {
                series5.getData().add(data);
            }
        }
        if (!series5.getData().isEmpty()) {
            salesPerformanceChart.getData().add(series5);
        }
    }

    private void setBestSellingItems(List<PenjualanDetail> listPenjualanDetail) throws Exception {
        bestSellingItemChart.getData().clear();
        HashMap hm = new HashMap();
        for (PenjualanDetail d : listPenjualanDetail) {
            if (hm.get(d.getKodeJenis()) == null) {
                hm.put(d.getKodeJenis(), d.getBerat());
            } else {
                hm.put(d.getKodeJenis(), (Double) hm.get(d.getKodeJenis()) + d.getBerat());
            }
        }
        Iterator i = hm.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            PieChart.Data data = new PieChart.Data(me.getKey().toString()+" - "+(Double) me.getValue()+" gr", (Double) me.getValue());
            bestSellingItemChart.getData().add(data);
        }
    }

    private void getKategori() {
        try (Connection con = Koneksi.getConnection()) {
            allKategori.clear();
            allKategori.addAll(KategoriDAO.getAll(con));
            kategoriTable.refresh();
        } catch (Exception e) {
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }

    private void updateHarga() {
        mainApp.showDataKategoriBarang();
    }
}
