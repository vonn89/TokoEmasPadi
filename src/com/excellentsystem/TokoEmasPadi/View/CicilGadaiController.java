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
import static com.excellentsystem.TokoEmasPadi.Main.tglLengkap;
import static com.excellentsystem.TokoEmasPadi.Main.tglSql;
import com.excellentsystem.TokoEmasPadi.Model.BungaGadai;
import com.excellentsystem.TokoEmasPadi.Model.GadaiDetail;
import com.excellentsystem.TokoEmasPadi.Model.GadaiHead;
import com.excellentsystem.TokoEmasPadi.Model.User;
import com.excellentsystem.TokoEmasPadi.PrintOut.PrintOut;
import com.excellentsystem.TokoEmasPadi.Service.Service;
import static java.lang.Math.ceil;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Yunaz
 */
public class CicilGadaiController  {

    @FXML private TableView<GadaiDetail> detailTable;
    @FXML private TableColumn<GadaiDetail, Number> jumlahColumn;
    @FXML private TableColumn<GadaiDetail, String> kodeKategoriColumn;
    @FXML private TableColumn<GadaiDetail, String> namaBarangColumn;
    @FXML private TableColumn<GadaiDetail, Number> beratColumn;
    @FXML private TableColumn<GadaiDetail, String> kondisiColumn;
    @FXML private TableColumn<GadaiDetail, String> statusSuratColumn;
    @FXML private TableColumn<GadaiDetail, Number> nilaiJualColumn;
    @FXML private TableColumn<GadaiDetail, Number> nilaiJualSekarangColumn;
    
    @FXML public TextField noGadaiField;
    @FXML private TextField tglGadaiField;
    @FXML private TextField tglLunasField;
    @FXML private TextField namaField;
    @FXML private TextField alamatField;
    
    @FXML private TextField totalpinjamanField;
    @FXML private TextField bungaPersenField;
    @FXML private TextField bungaRpField;
    @FXML private CheckBox printCheckBox;
    
    @FXML private TextField pembayaranPinjamanField;
    @FXML private TextField pembayaranBungaField;
    @FXML private TextField sisaPinjamanField;
    @FXML private TextField bungaPersenBaruField;
    @FXML private TextField bungaRpBaruField;
    @FXML private TextField totalJumlahDiterimaField;
    
    private GadaiHead g =null;
    private ObservableList<GadaiDetail> allDetail = FXCollections.observableArrayList();
    private Main mainApp;
    private List<BungaGadai> bungaGadai ;
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
        nilaiJualSekarangColumn.setCellValueFactory(cellData -> {
            return new SimpleDoubleProperty(
                    ceil(cellData.getValue().getBerat()*cellData.getValue().getKategori().getHargaJual()*500)/500);
        });
        nilaiJualSekarangColumn.setCellFactory(col -> getTableCell(rp, "Rp"));
        pembayaranPinjamanField.setOnKeyReleased((event) -> {
            try{
                String string = pembayaranPinjamanField.getText();
                if(string.contains("-"))
                    pembayaranPinjamanField.undo();
                else{
                    if(string.indexOf(".")>0){
                        String string2 = string.substring(string.indexOf(".")+1, string.length());
                        if(string2.contains("."))
                            pembayaranPinjamanField.undo();
                        else if(!string2.equals("") && Double.parseDouble(string2)!=0)
                            pembayaranPinjamanField.setText(rp.format(Double.parseDouble(string.replaceAll(",", ""))));
                    }else
                        pembayaranPinjamanField.setText(rp.format(Double.parseDouble(string.replaceAll(",", ""))));
                }
                pembayaranPinjamanField.end();
            }catch(Exception e){
                pembayaranPinjamanField.undo();
            }
            hitung();
        });
        pembayaranBungaField.setOnKeyReleased((event) -> {
            try{
                String string = pembayaranBungaField.getText();
                if(string.contains("-"))
                    pembayaranBungaField.undo();
                else{
                    if(string.indexOf(".")>0){
                        String string2 = string.substring(string.indexOf(".")+1, string.length());
                        if(string2.contains("."))
                            pembayaranBungaField.undo();
                        else if(!string2.equals("") && Double.parseDouble(string2)!=0)
                            pembayaranBungaField.setText(rp.format(Double.parseDouble(string.replaceAll(",", ""))));
                    }else
                        pembayaranBungaField.setText(rp.format(Double.parseDouble(string.replaceAll(",", ""))));
                }
                pembayaranBungaField.end();
            }catch(Exception e){
                pembayaranBungaField.undo();
            }
            hitung();
        });
        bungaPersenBaruField.setOnKeyReleased((event) -> {
            try{
                String string = bungaPersenBaruField.getText();
                if(string.contains("-"))
                    bungaPersenBaruField.undo();
                else{
                    if(string.indexOf(".")>0){
                        String string2 = string.substring(string.indexOf(".")+1, string.length());
                        if(string2.contains("."))
                            bungaPersenBaruField.undo();
                        else if(!string2.equals("") && Double.parseDouble(string2)!=0)
                            bungaPersenBaruField.setText(gr.format(Double.parseDouble(string.replaceAll(",", ""))));
                    }else
                        bungaPersenBaruField.setText(gr.format(Double.parseDouble(string.replaceAll(",", ""))));
                }
                bungaPersenBaruField.end();
            }catch(Exception e){
                bungaPersenBaruField.undo();
            }
            getBungaRp();
        });
        pembayaranPinjamanField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                pembayaranBungaField.selectAll();
                pembayaranBungaField.requestFocus();
            }
        });
        pembayaranBungaField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                bungaPersenBaruField.selectAll();
                bungaPersenBaruField.requestFocus();
            }
        });
        
        detailTable.setRowFactory(table -> {
            TableRow<GadaiDetail> row = new TableRow<GadaiDetail>(){
                @Override
                public void updateItem(GadaiDetail item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(null);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem hapus = new MenuItem("Hapus Barang");
                        hapus.setOnAction((ActionEvent event) -> {
                            if(allDetail.size()<=1){
                                mainApp.showMessage(Modality.NONE, "Warning", "Gadai minimal harus ada 1 barang");
                            }else{
                                allDetail.remove(item);
                                detailTable.refresh();
                            }
                        });
                        rowMenu.getItems().addAll(hapus);
                        setContextMenu(rowMenu);
                            
                    }
                }
            };
            return row;
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
    private void bindToTime() {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), (ActionEvent e) -> {
                tglLunasField.setText(new SimpleDateFormat("dd MMMMM yyyy  hh:mm:ss").format(new Date()));
            })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    public void setGadai(GadaiHead gadai){
        try{
            g = gadai;
            allDetail.addAll(g.getListGadaiDetail());
            noGadaiField.setText(g.getNoGadai());
            tglGadaiField.setText(tglLengkap.format(tglSql.parse(g.getTglGadai())));
            bindToTime();
            namaField.setText(g.getNama());
            alamatField.setText(g.getAlamat());
            totalpinjamanField.setText(rp.format(g.getTotalPinjaman()));
            bungaPersenField.setText(rp.format(g.getBungaPersen()));
            bungaRpField.setText(rp.format(g.getBungaKomp()));

            sisaPinjamanField.setText("0");
            bungaPersenBaruField.setText("0");
            bungaRpBaruField.setText("0");
            pembayaranPinjamanField.setText(rp.format(g.getTotalPinjaman()));
            pembayaranBungaField.setText(rp.format(g.getBungaKomp()));
            totalJumlahDiterimaField.setText(rp.format(g.getTotalPinjaman()+g.getBungaKomp()));
        }catch(Exception e){
            e.printStackTrace();
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    @FXML
    private void back(){
        mainApp.showCicilGadai();
    }
    private void getBungaPersen(){
        double pinjaman = Double.parseDouble(sisaPinjamanField.getText().replaceAll(",", ""));
        double bunga = 0;
        for(BungaGadai b : bungaGadai){
            if(b.getMin()<=pinjaman&&pinjaman<=b.getMax())
                bunga = b.getBunga();
        }
        bungaPersenBaruField.setText(gr.format(bunga));
        getBungaRp();
    }
    @FXML
    private void getBungaRp(){
        double pinjaman = Double.parseDouble(sisaPinjamanField.getText().replaceAll(",", ""));
        double bunga = Double.parseDouble(bungaPersenBaruField.getText().replaceAll(",", ""));
        double bungaRp = pinjaman*bunga/100;
        bungaRpBaruField.setText(rp.format(Math.ceil(bungaRp/500)*500));
    }
    @FXML
    private void hitung(){
        double totalPinjaman = Double.parseDouble(totalpinjamanField.getText().replaceAll(",", ""));
        double bayarPinjaman = Double.parseDouble(pembayaranPinjamanField.getText().replaceAll(",", ""));
        double bayarBunga = Double.parseDouble(pembayaranBungaField.getText().replaceAll(",", ""));
        sisaPinjamanField.setText(rp.format(totalPinjaman-bayarPinjaman));
        totalJumlahDiterimaField.setText(rp.format(bayarPinjaman+bayarBunga));
        getBungaPersen();
    }
    @FXML
    private void saveGadai(){
        if(g==null)
            mainApp.showMessage(Modality.NONE, "Warning", "Gadai belum dipilih");
        else if(pembayaranPinjamanField.getText().equals(""))
            mainApp.showMessage(Modality.NONE, "Warning", "Pembayaran pinjaman masih kosong");
        else if(pembayaranBungaField.getText().equals(""))
            mainApp.showMessage(Modality.NONE, "Warning", "Pembayaran bunga masih kosong");
        else if(bungaPersenBaruField.getText().equals(""))
            mainApp.showMessage(Modality.NONE, "Warning", "Bunga persen masih kosong");
        else if(Double.parseDouble(pembayaranPinjamanField.getText().replaceAll(",", ""))>
                Double.parseDouble(totalpinjamanField.getText().replaceAll(",", "")))
            mainApp.showMessage(Modality.NONE, "Warning", "Pembayaran pinjaman melebihi total pinjaman");
        else{
            double bungaKomp = Double.parseDouble(bungaRpField.getText().replaceAll(",", ""));
            double bayarBunga = Double.parseDouble(pembayaranBungaField.getText().replaceAll(",", ""));
            double bayarPinjaman = Double.parseDouble(pembayaranPinjamanField.getText().replaceAll(",", ""));
            double sisaPinjaman = Double.parseDouble(sisaPinjamanField.getText().replaceAll(",", ""));
            double bungaBaru = Double.parseDouble(bungaPersenBaruField.getText().replaceAll(",", ""));
            double bunga = 0;
            for(BungaGadai b : bungaGadai){
                if(b.getMin()<=sisaPinjaman&&sisaPinjaman<=b.getMax())
                    bunga = b.getBunga();
            }
            if(bayarBunga<bungaKomp){
                Stage child = new Stage();
                FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/Verifikasi.fxml");
                VerifikasiController controller = loader.getController();
                controller.setMainApp(mainApp, child);
                controller.keteranganLabel.setText(
                        "Bunga di bayar : Rp "+bayarBunga+"\n"+
                        "Bunga komputer : Rp "+bungaKomp+"\n"+
                        "Verifikasi : ");
                controller.password.setOnKeyPressed((KeyEvent keyEvent) -> {
                    if (keyEvent.getCode() == KeyCode.ENTER)  {
                        child.close();
                        User user = Function.verifikasi(controller.password.getText(),"Pelunasan Gadai");
                        if(user!=null){
                            save(bayarPinjaman, bayarBunga, sisaPinjaman, bungaBaru);
                        }else{
                            mainApp.showMessage(Modality.NONE, "Warning", "Verifikasi salah");
                        }
                    }
                });
            }else if(bunga!=bungaBaru){
                Stage child = new Stage();
                FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/Verifikasi.fxml");
                VerifikasiController controller = loader.getController();
                controller.setMainApp(mainApp, child);
                controller.keteranganLabel.setText(
                        "Bunga persen komputer : "+bunga+"%\n"+
                        "Bunga persen baru : "+bungaBaru+"%\n"+
                        "Verifikasi : ");
                controller.password.setOnKeyPressed((KeyEvent keyEvent) -> {
                    if (keyEvent.getCode() == KeyCode.ENTER)  {
                        child.close();
                        User user = Function.verifikasi(controller.password.getText(),"Pelunasan Gadai");
                        if(user!=null){
                            save(bayarPinjaman, bayarBunga, sisaPinjaman, bungaBaru);
                        }else{
                            mainApp.showMessage(Modality.NONE, "Warning", "Verifikasi salah");
                        }
                    }
                });
            }else{
                save(bayarPinjaman, bayarBunga, sisaPinjaman, bungaBaru);
            }
        }
    }  
    private void save(double bayarPinjaman, double bayarBunga, double sisaPinjaman, double bungaBaru){
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
                        g.setTglLunas(Function.getSystemDate());
                        g.setSalesLunas(sales);
                        g.setBungaRp(bayarBunga);
                        g.setStatusLunas("true");
                        g.setListGadaiDetail(allDetail);

                        GadaiHead gadaiBaru = null;
                        if(sisaPinjaman>0){
                            gadaiBaru = new GadaiHead();
                            gadaiBaru.setNoGadai(GadaiHeadDAO.getId(con));
                            gadaiBaru.setTglGadai(g.getTglLunas());
                            gadaiBaru.setKodeSales(g.getSalesLunas());
                            gadaiBaru.setNama(g.getNama());
                            gadaiBaru.setAlamat(g.getAlamat());
                            gadaiBaru.setKeterangan("No Hutang Lama : "+g.getNoGadai()+"\n"
                                    + "Pinjaman : "+rp.format(g.getTotalPinjaman())+"\n"
                                    + "Cicil Pinjaman : "+rp.format(bayarPinjaman)+"\n"
                                    + "Bunga Dibayar : "+rp.format(bayarBunga));
                            gadaiBaru.setTotalBerat(g.getTotalBerat());
                            gadaiBaru.setTotalPinjaman(sisaPinjaman);
                            gadaiBaru.setLamaPinjam(1);
                            gadaiBaru.setBungaPersen(bungaBaru);
                            gadaiBaru.setBungaKomp(Math.ceil(gadaiBaru.getTotalPinjaman()*gadaiBaru.getBungaPersen()/100/30*1/500)*500);
                            gadaiBaru.setBungaRp(Math.ceil(gadaiBaru.getTotalPinjaman()*gadaiBaru.getBungaPersen()/100/30*1/500)*500);
                            LocalDate jatuhTempo = LocalDate.parse(sistem.getTglSystem()).plusDays(sistem.getJatuhTempoGadai());
                            gadaiBaru.setJatuhTempo(jatuhTempo.toString());
                            gadaiBaru.setStatusLunas("false");
                            gadaiBaru.setTglLunas("2000-01-01 00:00:00");
                            gadaiBaru.setSalesLunas("");
                            gadaiBaru.setStatusBatal("false");
                            gadaiBaru.setTglBatal("2000-01-01 00:00:00");
                            gadaiBaru.setSalesBatal("");
                            gadaiBaru.setListGadaiDetail(allDetail);
                        }

                        String status = Service.savePelunasanGadai(con, g, gadaiBaru, 0);
                        if(status.equals("true")){
                            if(gadaiBaru!=null){
                                if(printCheckBox.isSelected()){
                                    try{
                                        for(GadaiDetail d : allDetail){
                                            d.setNoGadai(gadaiBaru.getNoGadai());
                                            d.setGadaiHead(gadaiBaru);
                                        }
                                        PrintOut printOut = new PrintOut();
                                        printOut.printSuratHutang(allDetail);
                                        printOut.printSuratHutangInternal(allDetail);
                                    }catch(Exception e){
                                        e.printStackTrace();
                                        mainApp.showMessage(Modality.NONE, "Error", e.toString());
                                    }
                                }
                                mainApp.showCicilGadai();
                                mainApp.showMessage(Modality.NONE, "Success", "Bayar bunga/cicil gadai berhasil disimpan");
                            }else{
                                mainApp.showCicilGadai();
                                mainApp.showMessage(Modality.NONE, "Success", "Pelunasan gadai berhasil disimpan");
                            }
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
    
}
