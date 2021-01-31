/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Report;

import com.excellentsystem.TokoEmasPadi.DAO.BarangDAO;
import com.excellentsystem.TokoEmasPadi.DAO.JenisDAO;
import com.excellentsystem.TokoEmasPadi.DAO.KategoriDAO;
import com.excellentsystem.TokoEmasPadi.Function;
import static com.excellentsystem.TokoEmasPadi.Function.getTableCell;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import static com.excellentsystem.TokoEmasPadi.Main.tglLengkap;
import static com.excellentsystem.TokoEmasPadi.Main.tglSql;
import com.excellentsystem.TokoEmasPadi.Model.Barang;
import com.excellentsystem.TokoEmasPadi.Model.BatalBarcode;
import com.excellentsystem.TokoEmasPadi.Model.HancurDetail;
import com.excellentsystem.TokoEmasPadi.Model.HancurHead;
import com.excellentsystem.TokoEmasPadi.Model.Jenis;
import com.excellentsystem.TokoEmasPadi.Model.Kategori;
import com.excellentsystem.TokoEmasPadi.Model.User;
import com.excellentsystem.TokoEmasPadi.PrintOut.PrintOut;
import com.excellentsystem.TokoEmasPadi.Service.Service;
import com.excellentsystem.TokoEmasPadi.View.Dialog.BatalBarcodeController;
import com.excellentsystem.TokoEmasPadi.View.Dialog.CetakBarcodeController;
import com.excellentsystem.TokoEmasPadi.View.Dialog.DetailBarangController;
import com.excellentsystem.TokoEmasPadi.View.Dialog.HancurBarangController;
import com.excellentsystem.TokoEmasPadi.View.Dialog.VerifikasiController;
import java.sql.Connection;
import java.text.ParseException;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Xtreme
 */
public class LaporanBarangController  {

    @FXML private TableView<Barang> barangTable;
    @FXML private TableColumn<Barang, Boolean> checkColumn;
    @FXML private TableColumn<Barang, String> kodeBarcodeColumn;
    @FXML private TableColumn<Barang, String> namaBarangColumn;
    @FXML private TableColumn<Barang, String> namaBarcodeColumn;
    @FXML private TableColumn<Barang, String> kodeKategoriColumn;
    @FXML private TableColumn<Barang, String> kodeJenisColumn;
    @FXML private TableColumn<Barang, Number> qtyColumn;
    @FXML private TableColumn<Barang, Number> beratColumn;
    @FXML private TableColumn<Barang, Number> beratPembulatanColumn;
    @FXML private TableColumn<Barang, String> keteranganColumn;
    @FXML private TableColumn<Barang, String> merkColumn;
    @FXML private TableColumn<Barang, Number> persentaseColumn;
    @FXML private TableColumn<Barang, Number> nilaiPokokColumn;
    @FXML private TableColumn<Barang, Number> hargaJualColumn;
    @FXML private TableColumn<Barang, String> tglBarcodeColumn;
    @FXML private TableColumn<Barang, String> userBarcodeColumn;
    
    @FXML private CheckBox checkAll;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> kodeKategoriCombo;
    @FXML private ComboBox<String> kodeJenisCombo;
    @FXML private Label totalQtyLabel;
    @FXML private Label totalBeratLabel;
    @FXML private Label totalBeratPembulatanLabel;
    @FXML private Label totalNilaiPokokLabel;
    @FXML private Label totalHargaJualLabel;
    private Main mainApp;   
    private final ObservableList<String> allKategori = FXCollections.observableArrayList();
    private final ObservableList<String> allJenis = FXCollections.observableArrayList();
    private final ObservableList<Barang> allBarang = FXCollections.observableArrayList();
    private final ObservableList<Barang> filterData = FXCollections.observableArrayList();
    public void initialize() {
        kodeBarcodeColumn.setCellValueFactory(cellData -> cellData.getValue().kodeBarcodeProperty());
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().namaBarangProperty());
        namaBarcodeColumn.setCellValueFactory(cellData -> cellData.getValue().namaBarcodeProperty());
        kodeKategoriColumn.setCellValueFactory(cellData -> cellData.getValue().kodeKategoriProperty());
        kodeJenisColumn.setCellValueFactory(cellData -> cellData.getValue().kodeJenisProperty());
        keteranganColumn.setCellValueFactory(cellData -> cellData.getValue().keteranganProperty());
        merkColumn.setCellValueFactory(cellData -> cellData.getValue().merkProperty());
        persentaseColumn.setCellValueFactory(cellData -> cellData.getValue().persentaseProperty());
        persentaseColumn.setCellFactory(col -> getTableCell(gr, ""));
        qtyColumn.setCellValueFactory(cellData -> cellData.getValue().qtyProperty());
        qtyColumn.setCellFactory(col -> getTableCell(rp, ""));
        beratColumn.setCellValueFactory(cellData -> cellData.getValue().beratProperty());
        beratColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        beratPembulatanColumn.setCellValueFactory(cellData -> cellData.getValue().beratPembulatanProperty());
        beratPembulatanColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        nilaiPokokColumn.setCellValueFactory(cellData -> cellData.getValue().nilaiPokokProperty());
        nilaiPokokColumn.setCellFactory(col -> getTableCell(rp, "Rp"));
        hargaJualColumn.setCellValueFactory(cellData -> cellData.getValue().hargaJualProperty());
        hargaJualColumn.setCellFactory(col -> getTableCell(rp, "Rp"));
        tglBarcodeColumn.setCellValueFactory(cellData -> { 
            try {
                return new SimpleStringProperty(tglLengkap.format(tglSql.parse(cellData.getValue().getBarcodeDate())));
            } catch (ParseException ex) {
                return null;
            }
        });
        tglBarcodeColumn.setComparator(Function.sortDate(tglLengkap));
        userBarcodeColumn.setCellValueFactory(cellData -> cellData.getValue().barcodeByProperty());
        checkColumn.setCellValueFactory(cellData -> cellData.getValue().checkedProperty());
        checkColumn.setCellFactory(CheckBoxTableCell.forTableColumn((Integer v) -> {
            return barangTable.getItems().get(v).checkedProperty();
        }));
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem cetak = new MenuItem("Cetak Barcode");
        cetak.setOnAction((ActionEvent e)->{
            cetakBarcode();
        });
        MenuItem hancur = new MenuItem("Hancur Barang");
        hancur.setOnAction((ActionEvent event) -> {
            hancurBarang();
        });
        MenuItem batal = new MenuItem("Batal Barcode");
        batal.setOnAction((ActionEvent event) -> {
            batalBarcode();
        });
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            getBarang();
        });
        rowMenu.getItems().addAll(cetak, batal, hancur, refresh);
        barangTable.setContextMenu(rowMenu);
        barangTable.setRowFactory(table -> {
            TableRow<Barang> row = new TableRow<Barang>() {
                @Override
                public void updateItem(Barang item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rowMenu);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem edit = new MenuItem("Edit Barang");
                        edit.setOnAction((ActionEvent e) -> {
                            editBarang(item);
                        });
                        MenuItem cetak = new MenuItem("Cetak Barcode");
                        cetak.setOnAction((ActionEvent e)->{
                            cetakBarcode();
                        });
                        MenuItem batal = new MenuItem("Batal Barcode");
                        batal.setOnAction((ActionEvent event) -> {
                            batalBarcode();
                        });
                        MenuItem hancur = new MenuItem("Hancur Barang");
                        hancur.setOnAction((ActionEvent event) -> {
                            hancurBarang();
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            getBarang();
                        });
                        rowMenu.getItems().addAll(edit, cetak, batal, hancur, refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)&&
                        mouseEvent.getClickCount() == 2){
                    if(row.getItem()!=null){
                        if(row.getItem().isChecked())
                            row.getItem().setChecked(false);
                        else
                            row.getItem().setChecked(true);
                    }
                }
            });
            return row;
        });
        allBarang.addListener((ListChangeListener.Change<? extends Barang> change) -> {
            searchBarang();
        });
        searchField.textProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            searchBarang();
        });
        filterData.addAll(allBarang);
    }    
    public void setMainApp(Main mainApp){
        try(Connection con = Koneksi.getConnection()){
            this.mainApp = mainApp;
            allKategori.add("Semua");
            List<Kategori> kategori = KategoriDAO.getAll(con);
            for(Kategori k : kategori){
                allKategori.add(k.getKodeKategori());
            }
            kodeKategoriCombo.setItems(allKategori);
            kodeKategoriCombo.getSelectionModel().select("Semua");

            allJenis.add("Semua");
            List<Jenis> jenis = JenisDAO.getAll(con);
            for(Jenis j : jenis){
                allJenis.add(j.getKodeJenis());
            }
            kodeJenisCombo.setItems(allJenis);
            kodeJenisCombo.getSelectionModel().select("Semua");

            getBarang();
            barangTable.setItems(filterData);
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    } 
    @FXML
    private void getBarang(){
        try(Connection con = Koneksi.getConnection()){
            allBarang.clear();
            String kategori = "%";
            String jenis = "%";
            if(!kodeKategoriCombo.getSelectionModel().getSelectedItem().equals("Semua"))
                kategori = kodeKategoriCombo.getSelectionModel().getSelectedItem();
            if(!kodeJenisCombo.getSelectionModel().getSelectedItem().equals("Semua"))
                jenis = kodeJenisCombo.getSelectionModel().getSelectedItem();
            List<Barang> listBarang = BarangDAO.getAllByKategoriAndJenisAndKeteranganAndMerkAndStokAda(
                    con, kategori, jenis, "%", "%");
            for(Barang b : listBarang){
                b.setChecked(checkAll.isSelected());
                allBarang.add(b);
            }
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    @FXML
    private void checkAllHandle(){
        for(Barang b : allBarang){
            b.setChecked(checkAll.isSelected());
        }
        barangTable.refresh();
    }
    private Boolean checkColumn(String column){
        if(column!=null)
            if(column.toLowerCase().contains(searchField.getText().toLowerCase()))
                return true;
        return false;
    }
    private void searchBarang() {
        try{
            filterData.clear();
            for (Barang temp : allBarang) {
                if (searchField.getText() == null || searchField.getText().equals(""))
                    filterData.add(temp);
                else{
                    if(checkColumn(temp.getKodeKategori())||
                        checkColumn(temp.getKodeBarcode())||
                        checkColumn(temp.getNamaBarang())||
                        checkColumn(temp.getKeterangan())||
                        checkColumn(temp.getKodeJenis())||
                        checkColumn(temp.getMerk())||
                        checkColumn(temp.getNamaBarcode())||
                        checkColumn(temp.getBarcodeBy())||
                        checkColumn(tglLengkap.format(tglSql.parse(temp.getBarcodeDate())))||
                        checkColumn(gr.format(temp.getBerat()))||
                        checkColumn(gr.format(temp.getBeratPembulatan()))||
                        checkColumn(rp.format(temp.getNilaiPokok()))||
                        checkColumn(rp.format(temp.getHargaJual())))
                        filterData.add(temp);
                }
            }
            for (Barang s : filterData) {
                s.checkedProperty().addListener((obs, oldValue, newValue) ->{
                    hitungTotal();
                });
            }
            hitungTotal();
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    private void hitungTotal(){
        int totalQty = 0;
        double totalBerat = 0;
        double totalBeratPembulatan = 0;
        double totalNilai = 0;
        double totalHarga = 0;
        for(Barang s : filterData){
            if(s.isChecked()){
                totalQty = totalQty + s.getQty();
                totalBerat = totalBerat + s.getQty()*s.getBerat();
                totalBeratPembulatan = totalBeratPembulatan + s.getQty()*s.getBeratPembulatan();
                totalNilai = totalNilai + s.getQty()*s.getNilaiPokok();
                totalHarga = totalHarga + s.getQty()*s.getHargaJual();
            }
        }
        totalQtyLabel.setText(rp.format(totalQty));
        totalBeratLabel.setText(gr.format(totalBerat) + " gr");
        totalBeratPembulatanLabel.setText(gr.format(totalBeratPembulatan)+ " gr");
        totalNilaiPokokLabel.setText("Rp "+rp.format(totalNilai));
        totalHargaJualLabel.setText("Rp "+rp.format(totalHarga));
    }
    private void editBarang(Barang b){
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailBarang.fxml");
        DetailBarangController controller = loader.getController();
        controller.setMainApp(mainApp, stage);
        controller.setBarang(b);
        controller.saveButton.setOnAction((event) -> {
            Stage stageVerifikasi = new Stage();
            FXMLLoader loaderVerifikasi = mainApp.showDialog(stage, stageVerifikasi, "View/Dialog/Verifikasi.fxml");
            VerifikasiController v = loaderVerifikasi.getController();
            v.setMainApp(mainApp, stageVerifikasi);
            v.keteranganLabel.setText("Verifikasi : ");
            v.password.setOnKeyPressed((KeyEvent) -> {
                if (KeyEvent.getCode() == KeyCode.ENTER)  {
                    stageVerifikasi.close();
                    if(Function.verifikasi(v.password.getText(), "Edit Barang")!=null){
                        try(Connection con = Koneksi.getConnection()){
                            b.setNamaBarang(controller.namaBarangField.getText());
                            b.setNamaBarcode(controller.namaBarcodeField.getText());
                            b.setKeterangan(controller.keteranganField.getText());
                            b.setMerk(controller.merkField.getText());
                            b.setPersentase(Double.parseDouble(controller.persentaseField.getText().replaceAll(",", "")));
                            b.setNilaiPokok(Double.parseDouble(controller.nilaiPokokField.getText().replaceAll(",", "")));
                            b.setHargaJual(Double.parseDouble(controller.hargaJualField.getText().replaceAll(",", "")));
                            BarangDAO.update(con, b);
                            stage.close();
                            mainApp.showMessage(Modality.NONE, "Success", "Edit barang berhasil disimpan");
                            getBarang();
                        }catch(Exception e){
                            getBarang();
                            mainApp.showMessage(Modality.NONE, "Error", e.toString());
                        }
                    }else{
                        mainApp.showMessage(Modality.NONE, "Warning", "Verifikasi salah");
                    }
                }
            });
        });
    }
    private void batalBarcode(){
        List<BatalBarcode> listBatalBarcode = new ArrayList<>();
        for(Barang b : filterData){
            if(b.isChecked()){
                BatalBarcode d = new BatalBarcode();
                d.setKodeBarcode(b.getKodeBarcode());
                d.setQty(b.getQty());
                d.setBarang(b);
                listBatalBarcode.add(d);
            }
        }
        if(listBatalBarcode.isEmpty())
            mainApp.showMessage(Modality.NONE, "Warning", "Barang belum dipilih");
        else{
            Stage stage = new Stage();
            FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/BatalBarcode.fxml");
            BatalBarcodeController controller = loader.getController();
            controller.setMainApp(mainApp, stage);
            controller.setBarang(listBatalBarcode);
            controller.saveButton.setOnAction((event) -> {
                if(controller.allBarang.isEmpty())
                    mainApp.showMessage(Modality.NONE, "Warning", "Barang yang akan dibatal tidak ada");
                else{
                    Stage stageVerifikasi = new Stage();
                    FXMLLoader loaderVerifikasi = mainApp.showDialog(stage, stageVerifikasi, "View/Dialog/Verifikasi.fxml");
                    VerifikasiController v = loaderVerifikasi.getController();
                    v.setMainApp(mainApp, stageVerifikasi);
                    v.keteranganLabel.setText("Verifikasi : ");
                    v.password.setOnKeyPressed((KeyEvent) -> {
                        if (KeyEvent.getCode() == KeyCode.ENTER)  {
                            stageVerifikasi.close();
                            User user = Function.verifikasi(v.password.getText(), "Batal Barcode");
                            if(user!=null){
                                try(Connection con = Koneksi.getConnection()){
                                    for(BatalBarcode bb : controller.allBarang){
                                        bb.setTglBatal(Function.getSystemDate());
                                        bb.setKodeUser(user.getUsername());
                                    }
                                    String status = Service.saveBatalBarcode(con, controller.allBarang);
                                    getBarang();
                                    if(status.equals("true")){
                                        stage.close();
                                        mainApp.showMessage(Modality.NONE, "Success", "Batal barcode berhasil disimpan");
                                    }else{
                                        mainApp.showMessage(Modality.NONE, "Error", status);
                                    }
                                }catch(Exception e){
                                    e.printStackTrace();
                                    mainApp.showMessage(Modality.NONE, "Error", e.toString());
                                }
                            }else{
                                mainApp.showMessage(Modality.NONE, "Warning", "Verifikasi salah");
                            }
                        }
                    });
                }
            });
        }
    }
    private void hancurBarang(){
        List<HancurDetail> listHancurDetail = new ArrayList<>();
        for(Barang b : filterData){
            if(b.isChecked()){
                HancurDetail d = new HancurDetail();
                d.setKodeBarcode(b.getKodeBarcode());
                d.setQty(b.getQty());
                d.setBarang(b);
                listHancurDetail.add(d);
            }
        }
        if(listHancurDetail.isEmpty())
            mainApp.showMessage(Modality.NONE, "Warning", "Barang belum dipilih");
        else{
            Stage stage = new Stage();
            FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/HancurBarang.fxml");
            HancurBarangController controller = loader.getController();
            controller.setMainApp(mainApp, stage);
            controller.setBarang(listHancurDetail);
            controller.saveButton.setOnAction((event) -> {
                if(controller.allBarang.isEmpty())
                    mainApp.showMessage(Modality.NONE, "Warning", "Barang yang akan dihancur tidak ada");
                else{
                    Stage stageVerifikasi = new Stage();
                    FXMLLoader loaderVerifikasi = mainApp.showDialog(stage, stageVerifikasi, "View/Dialog/Verifikasi.fxml");
                    VerifikasiController v = loaderVerifikasi.getController();
                    v.setMainApp(mainApp, stageVerifikasi);
                    v.keteranganLabel.setText("Verifikasi : ");
                    v.password.setOnKeyPressed((KeyEvent) -> {
                        if (KeyEvent.getCode() == KeyCode.ENTER)  {
                            stageVerifikasi.close();
                            User user = Function.verifikasi(v.password.getText(), "Hancur Barang");
                            if(user!=null){
                                try(Connection con = Koneksi.getConnection()){
                                    HancurHead head = new HancurHead();
                                    head.setTglHancur(Function.getSystemDate());
                                    head.setTotalQty(Integer.parseInt(controller.totalQtyLabel.getText().replaceAll(",", "")));
                                    head.setTotalBerat(Double.parseDouble(controller.totalBeratLabel.getText().replaceAll(",", "").replaceAll("gr", "")));
                                    head.setTotalBeratPembulatan(Double.parseDouble(controller.totalBeratPembulatanLabel.getText().replaceAll(",", "").replaceAll("gr", "")));
                                    head.setKodeUser(user.getUsername());
                                    head.setListHancurDetail(controller.allBarang);
                                    String status = Service.saveHancurBarang(con, head);
                                    getBarang();
                                    if(status.equals("true")){
                                        stage.close();
                                        mainApp.showMessage(Modality.NONE, "Success", "Hancur Barang berhasil disimpan");
                                    }else{
                                        mainApp.showMessage(Modality.NONE, "Error", status);
                                    }
                                }catch(Exception e){
                                    e.printStackTrace();
                                    mainApp.showMessage(Modality.NONE, "Error", e.toString());
                                }
                            }else{
                                mainApp.showMessage(Modality.NONE, "Warning", "Verifikasi salah");
                            }
                        }
                    });
                }
            });
        }
    }
    private void cetakBarcode(){
        List<Barang> barang = new ArrayList<>();
        for(Barang b : filterData){
            if(b.isChecked()){
                Barang brg = new Barang();
                brg.setKodeBarcode(b.getKodeBarcode());
                brg.setNamaBarang(b.getNamaBarang());
                brg.setNamaBarcode(b.getNamaBarcode());
                brg.setKodeKategori(b.getKodeKategori());
                brg.setKodeJenis(b.getKodeJenis());
                brg.setBerat(b.getBerat());
                brg.setBeratPembulatan(b.getBeratPembulatan());
                brg.setNilaiPokok(b.getNilaiPokok());
                brg.setHargaJual(b.getHargaJual());
                brg.setKeterangan(b.getKeterangan());
                brg.setMerk(b.getMerk());
                brg.setBarcodeDate(b.getBarcodeDate());
                brg.setBarcodeBy(b.getBarcodeBy());
                brg.setQty(b.getQty());
                barang.add(brg);
            }
        }
        if(barang.isEmpty())
            mainApp.showMessage(Modality.NONE, "Warning", "Barang belum dipilih");
        else{
            Stage stage = new Stage();
            FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/CetakBarcode.fxml");
            CetakBarcodeController x = loader.getController();
            x.setMainApp(mainApp, stage);
            x.setBarang(barang);
            x.saveButton.setOnAction((event) -> {
                if(x.allBarang.isEmpty())
                    mainApp.showMessage(Modality.NONE, "Warning", "Barang yang akan dicetak tidak ada");
                else{
                    try{
                        List<Barang> printBarang = new ArrayList<>();
                        for(Barang b : x.allBarang){
                            for(int i = 1 ; i <= b.getQty() ; i++){
                                printBarang.add(b);
                            }
                        }
                        PrintOut.printBarcode(printBarang);
                        stage.close();
                    }catch(Exception e){
                        mainApp.showMessage(Modality.NONE, "Error", e.toString());
                    }
                }
            });
        }
    }
}
