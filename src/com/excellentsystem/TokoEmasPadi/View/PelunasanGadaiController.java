/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View;

import com.excellentsystem.TokoEmasPadi.View.Dialog.*;
import com.excellentsystem.TokoEmasPadi.Function;
import static com.excellentsystem.TokoEmasPadi.Function.getTableCell;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import static com.excellentsystem.TokoEmasPadi.Main.tglSql;
import com.excellentsystem.TokoEmasPadi.Model.GadaiDetail;
import com.excellentsystem.TokoEmasPadi.Model.GadaiHead;
import com.excellentsystem.TokoEmasPadi.Model.User;
import com.excellentsystem.TokoEmasPadi.Service.Service;
import static java.lang.Math.ceil;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
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
public class PelunasanGadaiController  {

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
    
    @FXML private TextField pembayaranBungaField;
    @FXML private TextField totalJumlahDiterimaField;
        
    private GadaiHead g =null;
    private ObservableList<GadaiDetail> allDetail = FXCollections.observableArrayList();
    private Main mainApp;
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
        detailTable.setItems(allDetail);
    }    
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
    @FXML
    private void back(){
        mainApp.showPelunasanGadai();
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
            tglGadaiField.setText(new SimpleDateFormat("dd MMMMM yyyy  hh:mm:ss").format(tglSql.parse(g.getTglGadai())));
            bindToTime();
            namaField.setText(g.getNama());
            alamatField.setText(g.getAlamat());
            totalpinjamanField.setText(rp.format(g.getTotalPinjaman()));
            bungaPersenField.setText(rp.format(g.getBungaPersen()));
            bungaRpField.setText(rp.format(g.getBungaKomp()));

            pembayaranBungaField.setText(rp.format(g.getBungaKomp()));
            totalJumlahDiterimaField.setText(rp.format(g.getTotalPinjaman()+g.getBungaKomp()));
        }catch(Exception e){
            e.printStackTrace();
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    @FXML
    private void hitung(){
        double totalPinjaman = Double.parseDouble(totalpinjamanField.getText().replaceAll(",", ""));
        double bayarBunga = Double.parseDouble(pembayaranBungaField.getText().replaceAll(",", ""));
        totalJumlahDiterimaField.setText(rp.format(totalPinjaman+bayarBunga));
    }
    @FXML
    private void saveGadai(){
        if(g==null)
            mainApp.showMessage(Modality.NONE, "Warning", "Gadai belum dipilih");
        else if(pembayaranBungaField.getText().equals(""))
            mainApp.showMessage(Modality.NONE, "Warning", "Pembayaran bunga masih kosong");
        else{
            double bungaKomp = Double.parseDouble(bungaRpField.getText().replaceAll(",", ""));
            double bayarBunga = Double.parseDouble(pembayaranBungaField.getText().replaceAll(",", ""));
            if(bayarBunga<bungaKomp ){
                Stage child = new Stage();
                FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/Verifikasi.fxml");
                VerifikasiController controller = loader.getController();
                controller.setMainApp(mainApp, child);
                controller.keteranganLabel.setText(
                        "Bunga di bayar : Rp "+rp.format(bayarBunga)+"\n"+
                        "Bunga komputer : Rp "+rp.format(bungaKomp)+"\n"+
                        "Verifikasi : ");
                controller.password.setOnKeyPressed((KeyEvent keyEvent) -> {
                    if (keyEvent.getCode() == KeyCode.ENTER)  {
                        child.close();
                        User user = Function.verifikasi(controller.password.getText(),"Pelunasan Gadai");
                        if(user!=null){
                            save(bayarBunga);
                        }else{
                            mainApp.showMessage(Modality.NONE, "Warning", "Verifikasi salah");
                        }
                    }
                });
            }else{
                save(bayarBunga);
            }
        }
    }  
    private void save(double bayarBunga){
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

                        String status = Service.savePelunasanGadai(con, g, null, 0);
                        if(status.equals("true")){
                            mainApp.showPelunasanGadai();
                            mainApp.showMessage(Modality.NONE, "Success", "Pelunasan gadai berhasil disimpan");
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
