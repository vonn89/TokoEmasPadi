/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View;

import com.excellentsystem.TokoEmasPadi.View.Dialog.*;
import com.excellentsystem.TokoEmasPadi.DAO.BungaGadaiDAO;
import com.excellentsystem.TokoEmasPadi.DAO.GadaiHeadDAO;
import com.excellentsystem.TokoEmasPadi.Function;
import static com.excellentsystem.TokoEmasPadi.Function.getTableCell;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import com.excellentsystem.TokoEmasPadi.Model.BungaGadai;
import com.excellentsystem.TokoEmasPadi.Model.GadaiDetail;
import com.excellentsystem.TokoEmasPadi.Model.GadaiHead;
import com.excellentsystem.TokoEmasPadi.Model.User;
import com.excellentsystem.TokoEmasPadi.PrintOut.PrintOut;
import com.excellentsystem.TokoEmasPadi.Service.Service;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
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
public class TerimaGadaiController  {

    
    @FXML private TableView<GadaiDetail> detailTable;
    @FXML private TableColumn<GadaiDetail, Number> jumlahColumn;
    @FXML private TableColumn<GadaiDetail, String> kodeKategoriColumn;
    @FXML private TableColumn<GadaiDetail, String> namaBarangColumn;
    @FXML private TableColumn<GadaiDetail, Number> beratColumn;
    @FXML private TableColumn<GadaiDetail, String> kondisiColumn;
    @FXML private TableColumn<GadaiDetail, String> statusSuratColumn;
    @FXML private TableColumn<GadaiDetail, Number> nilaiJualColumn;
    
    @FXML private TextField namaField;
    @FXML private TextField alamatField;
    
    @FXML private TextField totalpinjamanField;
    @FXML private TextField bungaPersenField;
    @FXML private TextField bungaRpField;
    
    @FXML private Button ubahButton;
    @FXML private Button hapusButton;
    
    @FXML private CheckBox printCheckBox;
    private ObservableList<GadaiDetail> allDetail = FXCollections.observableArrayList();
    private Main mainApp;
    private List<BungaGadai> bungaGadai;
    public void initialize() {
        jumlahColumn.setCellValueFactory(cellData -> cellData.getValue().qtyProperty());
        jumlahColumn.setCellFactory(col -> getTableCell(rp, ""));
        kodeKategoriColumn.setCellValueFactory(cellData -> cellData.getValue().kodeKategoriProperty());
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().namaBarangProperty());
        beratColumn.setCellValueFactory(cellData -> cellData.getValue().beratProperty());
        beratColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        kondisiColumn.setCellValueFactory(cellData -> cellData.getValue().kondisiProperty());
        statusSuratColumn.setCellValueFactory(cellData -> cellData.getValue().statusSuratProperty());
        nilaiJualColumn.setCellValueFactory(cellData -> cellData.getValue().nilaiJualProperty());
        nilaiJualColumn.setCellFactory(col -> getTableCell(rp, "Rp"));
        
        totalpinjamanField.setOnKeyReleased((event) -> {
            try{
                String string = totalpinjamanField.getText();
                if(string.contains("-"))
                    totalpinjamanField.undo();
                else{
                    if(string.indexOf(".")>0){
                        String string2 = string.substring(string.indexOf(".")+1, string.length());
                        if(string2.contains("."))
                            totalpinjamanField.undo();
                        else if(!string2.equals("") && Double.parseDouble(string2)!=0)
                            totalpinjamanField.setText(gr.format(Double.parseDouble(string.replaceAll(",", ""))));
                    }else
                        totalpinjamanField.setText(gr.format(Double.parseDouble(string.replaceAll(",", ""))));
                }
                totalpinjamanField.end();
            }catch(Exception e){
                totalpinjamanField.undo();
            }
            getBungaPersen();
        });
        bungaPersenField.setOnKeyReleased((event) -> {
            try{
                String string = bungaPersenField.getText();
                if(string.contains("-"))
                    bungaPersenField.undo();
                else{
                    if(string.indexOf(".")>0){
                        String string2 = string.substring(string.indexOf(".")+1, string.length());
                        if(string2.contains("."))
                            bungaPersenField.undo();
                        else if(!string2.equals("") && Double.parseDouble(string2)!=0)
                            bungaPersenField.setText(gr.format(Double.parseDouble(string.replaceAll(",", ""))));
                    }else
                        bungaPersenField.setText(gr.format(Double.parseDouble(string.replaceAll(",", ""))));
                }
                bungaPersenField.end();
            }catch(Exception e){
                bungaPersenField.undo();
            }
            getBungaRp();
        });
        namaField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                alamatField.selectAll();
                alamatField.requestFocus();
            }
        });
        totalpinjamanField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                bungaPersenField.selectAll();
                bungaPersenField.requestFocus();
            }
        });
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem tambah = new MenuItem("Tambah Barang");
        tambah.setOnAction((ActionEvent event) -> {
            tambahBarang();
        });
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            detailTable.refresh();
        });
        rowMenu.getItems().addAll(tambah, refresh);
        detailTable.setContextMenu(rowMenu);
        detailTable.setRowFactory(table -> {
            TableRow<GadaiDetail> row = new TableRow<GadaiDetail>(){
                @Override
                public void updateItem(GadaiDetail item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(null);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
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
                        rowMenu.getItems().addAll(tambah, ubah, hapus, refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)&&
                        mouseEvent.getClickCount() == 2){
                    if(row.getItem()!=null)
                        ubahBarang();
                }
            });
            return row;
        });
        detailTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                ubahButton.setVisible(true);
                hapusButton.setVisible(true);
            }else{
                ubahButton.setVisible(false);
                hapusButton.setVisible(false);
            }
        });
        detailTable.setItems(allDetail);
    }    
    public void setMainApp(Main mainApp) {
        try(Connection con = Koneksi.getConnection()){
            this.mainApp = mainApp;
            bungaGadai = BungaGadaiDAO.getAll(con);
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    private void getBungaPersen(){
        double pinjaman = Double.parseDouble(totalpinjamanField.getText().replaceAll(",", ""));
        double bunga = 0;
        for(BungaGadai b : bungaGadai){
            if(b.getMin()<=pinjaman&&pinjaman<=b.getMax())
                bunga = b.getBunga();
        }
        bungaPersenField.setText(gr.format(bunga));
        getBungaRp();
    }
    private void getBungaRp(){
        double pinjaman = Double.parseDouble(totalpinjamanField.getText().replaceAll(",", ""));
        double bunga = Double.parseDouble(bungaPersenField.getText().replaceAll(",", ""));
        double bungaRp = pinjaman*bunga/100;
        bungaRpField.setText(rp.format(Math.ceil(bungaRp/500)*500));
    }
    private void hitungTotal(){
        double totalNilaiJual = 0;
        for(GadaiDetail d : allDetail){
            totalNilaiJual = totalNilaiJual + d.getNilaiJual();
        }
        totalpinjamanField.setText(rp.format(totalNilaiJual*Main.sistem.getPersentasePinjaman()/100));
        getBungaPersen();
    }
    @FXML
    private void tambahBarang(){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/AddBarangGadai.fxml");
        AddBarangGadaiController controller = loader.getController();
        controller.setMainApp(mainApp, child);
        controller.saveButton.setOnAction((event) -> {
            if(controller.kodeKategoriCombo.getSelectionModel().getSelectedItem()==null)
                mainApp.showMessage(Modality.NONE, "Warning", "Kode kategori belum dipilih");
            else if("".equals(controller.namaBarangField.getText()))
                mainApp.showMessage(Modality.NONE, "Warning", "Nama barang masih kosong");
            else if(Double.parseDouble(controller.jumlahField.getText().replaceAll(",", ""))<=0)
                mainApp.showMessage(Modality.NONE, "Warning", "Jumlah masih kosong");
            else if(Double.parseDouble(controller.beratField.getText().replaceAll(",", ""))<=0)
                mainApp.showMessage(Modality.NONE, "Warning", "Berat masih kosong");
            else if(controller.kondisiCombo.getSelectionModel().getSelectedItem()==null)
                mainApp.showMessage(Modality.NONE, "Warning", "Kondisi barang belum dipilih");
            else if(controller.statusSuratCombo.getSelectionModel().getSelectedItem()==null)
                mainApp.showMessage(Modality.NONE, "Warning", "Status surat belum dipilih");
            else{
                child.close();
                GadaiDetail d  = new GadaiDetail();
                d.setKodeKategori(controller.kodeKategoriCombo.getSelectionModel().getSelectedItem());
                d.setNamaBarang(controller.namaBarangField.getText());
                d.setQty(Integer.parseInt(controller.jumlahField.getText().replaceAll(",", "")));
                d.setBerat(Double.parseDouble(controller.beratField.getText().replaceAll(",", "")));
                d.setKondisi(controller.kondisiCombo.getSelectionModel().getSelectedItem());
                d.setStatusSurat(controller.statusSuratCombo.getSelectionModel().getSelectedItem());
                d.setNilaiJual(Double.parseDouble(controller.nilaiJualField.getText().replaceAll(",", "")));
                allDetail.add(d);
                detailTable.refresh();
                hitungTotal();
            }
        });
    }
    @FXML
    private void ubahBarang(){
        if(detailTable.getSelectionModel().getSelectedItem()!=null){
            GadaiDetail d = detailTable.getSelectionModel().getSelectedItem();
            Stage child = new Stage();
            FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/AddBarangGadai.fxml");
            AddBarangGadaiController controller = loader.getController();
            controller.setMainApp(mainApp, child);
            controller.setDetailGadai(d);
            controller.saveButton.setOnAction((event) -> {
                if(controller.kodeKategoriCombo.getSelectionModel().getSelectedItem()==null)
                    mainApp.showMessage(Modality.NONE, "Warning", "Kode kategori belum dipilih");
                else if("".equals(controller.namaBarangField.getText()))
                    mainApp.showMessage(Modality.NONE, "Warning", "Nama barang masih kosong");
                else if(Double.parseDouble(controller.jumlahField.getText().replaceAll(",", ""))<=0)
                    mainApp.showMessage(Modality.NONE, "Warning", "Jumlah masih kosong");
                else if(Double.parseDouble(controller.beratField.getText().replaceAll(",", ""))<=0)
                    mainApp.showMessage(Modality.NONE, "Warning", "Berat masih kosong");
                else if(controller.kondisiCombo.getSelectionModel().getSelectedItem()==null)
                    mainApp.showMessage(Modality.NONE, "Warning", "Kondisi barang belum dipilih");
                else if(controller.statusSuratCombo.getSelectionModel().getSelectedItem()==null)
                    mainApp.showMessage(Modality.NONE, "Warning", "Status surat belum dipilih");
                else{
                    child.close();
                    d.setKodeKategori(controller.kodeKategoriCombo.getSelectionModel().getSelectedItem());
                    d.setNamaBarang(controller.namaBarangField.getText());
                    d.setQty(Integer.parseInt(controller.jumlahField.getText().replaceAll(",", "")));
                    d.setBerat(Double.parseDouble(controller.beratField.getText().replaceAll(",", "")));
                    d.setKondisi(controller.kondisiCombo.getSelectionModel().getSelectedItem());
                    d.setStatusSurat(controller.statusSuratCombo.getSelectionModel().getSelectedItem());
                    d.setNilaiJual(Double.parseDouble(controller.nilaiJualField.getText().replaceAll(",", "")));
                    detailTable.refresh();
                    hitungTotal();
                }
            });
        }
    }
    @FXML
    private void hapusBarang(){
        if(detailTable.getSelectionModel().getSelectedItem()!=null){
            allDetail.remove(detailTable.getSelectionModel().getSelectedItem());
            hitungTotal();
            detailTable.refresh();
        }
    }
    @FXML
    private void reset(){
        allDetail.clear();
        namaField.setText("");
        alamatField.setText("");
        detailTable.refresh();
        hitungTotal();
    }
    @FXML
    private void saveGadai(){
        if(allDetail.isEmpty())
            mainApp.showMessage(Modality.NONE, "Warning", "Detail gadai masih kosong");
        else if(namaField.getText().equals(""))
            mainApp.showMessage(Modality.NONE, "Warning", "Nama masih kosong");
        else if(alamatField.getText().equals(""))
            mainApp.showMessage(Modality.NONE, "Warning", "Alamat masih kosong");
        else if(totalpinjamanField.getText().equals(""))
            mainApp.showMessage(Modality.NONE, "Warning", "Total pinjaman masih kosong");
        else if(bungaPersenField.getText().equals(""))
            mainApp.showMessage(Modality.NONE, "Warning", "Bunga persen masih kosong");
        else{
            double maxPinjam = 0;
            for(GadaiDetail d : allDetail){
                maxPinjam = maxPinjam + d.getNilaiJual();
            }
            maxPinjam  = maxPinjam*Main.sistem.getPersentasePinjaman()/100;
            double pinjaman = Double.parseDouble(totalpinjamanField.getText().replaceAll(",", ""));
            double bunga = 0;
            for(BungaGadai b : bungaGadai){
                if(b.getMin()<=pinjaman&&pinjaman<=b.getMax())
                    bunga = b.getBunga();
            }
            if(pinjaman>maxPinjam){
                Stage child = new Stage();
                FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/Verifikasi.fxml");
                VerifikasiController controller = loader.getController();
                controller.setMainApp(mainApp, child);
                controller.keteranganLabel.setText(
                        "Total Pinjaman : Rp "+rp.format(pinjaman)+"\n"+
                        "Maks Pinjaman : Rp "+rp.format(maxPinjam)+"\n"+
                        "Verifikasi : ");
                controller.password.setOnKeyPressed((KeyEvent keyEvent) -> {
                    if (keyEvent.getCode() == KeyCode.ENTER)  {
                        child.close();
                        User user = Function.verifikasi(controller.password.getText(),"Terima Gadai");
                        if(user!=null){
                            save();
                        }else{
                            mainApp.showMessage(Modality.NONE, "Warning", "Verifikasi salah");
                        }
                    }
                });
            }else if(bunga!=Double.parseDouble(bungaPersenField.getText().replaceAll(",", ""))){
                Stage child = new Stage();
                FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/Verifikasi.fxml");
                VerifikasiController controller = loader.getController();
                controller.setMainApp(mainApp, child);
                controller.keteranganLabel.setText(
                        "Bunga persen : "+bungaPersenField.getText()+" %\n"+
                        "Bunga persen komputer : "+gr.format(bunga)+" %\n"+
                        "Verifikasi : ");
                controller.password.setOnKeyPressed((KeyEvent keyEvent) -> {
                    if (keyEvent.getCode() == KeyCode.ENTER)  {
                        child.close();
                        User user = Function.verifikasi(controller.password.getText(),"Terima Gadai");
                        if(user!=null){
                            save();
                        }else{
                            mainApp.showMessage(Modality.NONE, "Warning", "Verifikasi salah");
                        }
                    }
                });
            }else{
                save();
            }
        }
    }  
    private void save(){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/Verifikasi.fxml");
        VerifikasiController controller = loader.getController();
        controller.setMainApp(mainApp, child);
        controller.keteranganLabel.setText("Verifikasi Sales : ");
        controller.password.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                child.close();
                String sales = Function.cekSales(controller.password.getText());
                if(sales!=null){
                    try(Connection con = Koneksi.getConnection()){
                        int totalQty = 0;
                        double totalBerat = 0;
                        for(GadaiDetail d : allDetail){
                            totalQty = totalQty + d.getQty();
                            totalBerat = totalBerat + d.getBerat();
                        }

                        GadaiHead p = new GadaiHead();
                        p.setNoGadai(GadaiHeadDAO.getId(con));
                        p.setTglGadai(Function.getSystemDate());
                        p.setKodeSales(sales);
                        p.setNama(namaField.getText());
                        p.setAlamat(alamatField.getText());
                        p.setKeterangan("");
                        p.setTotalQty(totalQty);
                        p.setTotalBerat(totalBerat);
                        p.setTotalPinjaman(Double.parseDouble(totalpinjamanField.getText().replaceAll(",", "")));
                        p.setLamaPinjam(1);
                        p.setBungaPersen(Double.parseDouble(bungaPersenField.getText().replaceAll(",", "")));
                        p.setBungaKomp(Math.ceil(p.getTotalPinjaman()*p.getBungaPersen()/100/30*1/500)*500);
                        p.setBungaRp(Math.ceil(p.getTotalPinjaman()*p.getBungaPersen()/100/30*1/500)*500);
                        p.setJatuhTempo(LocalDate.parse(sistem.getTglSystem()).plusDays(Main.sistem.getJatuhTempoGadai()).toString());
                        p.setStatusLunas("false");
                        p.setTglLunas("2000-01-01 00:00:00");
                        p.setSalesLunas("");
                        p.setStatusBatal("false");
                        p.setTglBatal("2000-01-01 00:00:00");
                        p.setSalesBatal("");
                        for(GadaiDetail d : allDetail){
                            d.setGadaiHead(p);
                            d.setNoGadai(p.getNoGadai());
                        }
                        p.setListGadaiDetail(allDetail);
                        String status = Service.saveTerimaGadai(con, p);
                        if(status.equals("true")){
                            if(printCheckBox.isSelected()){
                                PrintOut printOut = new PrintOut();
                                printOut.printSuratHutang(allDetail);
                                printOut.printSuratHutangInternal(allDetail);
                            }
                            mainApp.showMessage(Modality.NONE, "Success", "Terima Gadai berhasil disimpan");
                            reset();
                        }else{
                            mainApp.showMessage(Modality.NONE, "Error", status);
                        }
                    }catch(Exception e){
                        mainApp.showMessage(Modality.NONE, "Error", e.toString());
                    }
                }else{
                    mainApp.showMessage(Modality.NONE, "Warning", "Verifikasi salah");
                }
            }
        });
    }
    
}
