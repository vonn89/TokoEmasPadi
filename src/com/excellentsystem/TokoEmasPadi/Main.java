/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi;

import com.excellentsystem.TokoEmasPadi.DAO.GadaiHeadDAO;
import com.excellentsystem.TokoEmasPadi.DAO.OtoritasDAO;
import com.excellentsystem.TokoEmasPadi.DAO.SistemDAO;
import com.excellentsystem.TokoEmasPadi.DAO.StokBarangDAO;
import com.excellentsystem.TokoEmasPadi.DAO.UserDAO;
import com.excellentsystem.TokoEmasPadi.DAO.VerifikasiDAO;
import com.excellentsystem.TokoEmasPadi.Model.Otoritas;
import com.excellentsystem.TokoEmasPadi.Model.Sistem;
import com.excellentsystem.TokoEmasPadi.Model.User;
import com.excellentsystem.TokoEmasPadi.Model.Verifikasi;
import com.excellentsystem.TokoEmasPadi.View.CariGadaiController;
import com.excellentsystem.TokoEmasPadi.View.DashboardController;
import com.excellentsystem.TokoEmasPadi.View.Dialog.BackupAndRestoreController;
import com.excellentsystem.TokoEmasPadi.View.Dialog.BarcodeBarangController;
import com.excellentsystem.TokoEmasPadi.View.Dialog.DataJenisBarangController;
import com.excellentsystem.TokoEmasPadi.View.Dialog.DataKategoriBarangController;
import com.excellentsystem.TokoEmasPadi.View.Dialog.DataKategoriTransaksiController;
import com.excellentsystem.TokoEmasPadi.View.Dialog.DataUserController;
import com.excellentsystem.TokoEmasPadi.View.Dialog.MessageController;
import com.excellentsystem.TokoEmasPadi.View.Dialog.PengaturanGadaiController;
import com.excellentsystem.TokoEmasPadi.View.Dialog.PengaturanNilaiPokokController;
import com.excellentsystem.TokoEmasPadi.View.Dialog.PengaturanUmumController;
import com.excellentsystem.TokoEmasPadi.View.Dialog.UbahPasswordController;
import com.excellentsystem.TokoEmasPadi.View.LoginController;
import com.excellentsystem.TokoEmasPadi.View.MainController;
import com.excellentsystem.TokoEmasPadi.View.PembelianController;
import com.excellentsystem.TokoEmasPadi.View.PenjualanController;
import com.excellentsystem.TokoEmasPadi.View.Report.LaporanBarangController;
import com.excellentsystem.TokoEmasPadi.View.Report.LaporanBarcodeBarangController;
import com.excellentsystem.TokoEmasPadi.View.Report.LaporanBatalBarcodeController;
import com.excellentsystem.TokoEmasPadi.View.Report.LaporanBatalTerimaGadaiController;
import com.excellentsystem.TokoEmasPadi.View.Report.LaporanHancurBarangController;
import com.excellentsystem.TokoEmasPadi.View.Report.LaporanKeuanganController;
import com.excellentsystem.TokoEmasPadi.View.Report.LaporanPelunasanGadaiController;
import com.excellentsystem.TokoEmasPadi.View.Report.LaporanPembelianController;
import com.excellentsystem.TokoEmasPadi.View.Report.LaporanPenjualanController;
import com.excellentsystem.TokoEmasPadi.View.Report.LaporanStokBarangController;
import com.excellentsystem.TokoEmasPadi.View.Report.LaporanStokGadaiController;
import com.excellentsystem.TokoEmasPadi.View.Report.LaporanTerimaGadaiController;
import com.excellentsystem.TokoEmasPadi.View.Report.LaporanTransaksiSalesController;
import com.excellentsystem.TokoEmasPadi.View.Report.LaporanUbahHargaKategoriController;
import com.excellentsystem.TokoEmasPadi.View.StokOpnameController;
import com.excellentsystem.TokoEmasPadi.View.TerimaGadaiController;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author Xtreme
 */
public class Main extends Application {
    
    public Stage MainStage;
    public Stage message;
    
    public Dimension screenSize;
    public BorderPane mainLayout;
    public MainController mainController;
    
    private double x = 0;
    private double y = 0;
    
    public static DecimalFormat gr = new DecimalFormat("###,##0.##");
    public static DecimalFormat rp = new DecimalFormat("###,##0");
    public static DateFormat tglKode = new SimpleDateFormat("yyMMdd");
    public static DateFormat tglBarang = new SimpleDateFormat("yyyy-MM-dd");
    public static DateFormat tglNormal = new SimpleDateFormat("dd MMM yyyy");
    public static DateFormat tglSql = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public static DateFormat tglLengkap = new SimpleDateFormat("dd MMM yyyy hh:mm:ss");
    public static String printerPenjualan = "";
    public static String printerGadai = "";
    public static String printerBarcode = "";
    public static String ipServer = "localhost";
    
    public static Sistem sistem = null;
    public static User user;
    public static List<User> allUser;
    
    public static final String version = "1.0.0";
    
    public void setSistem()throws Exception{
        try(Connection con = Koneksi.getConnection()){
            sistem = SistemDAO.get(con);
            allUser = UserDAO.getAll(con);
            List<Otoritas> listOtoritas = OtoritasDAO.getAll(con);
            List<Verifikasi> listVerifikasi = VerifikasiDAO.getAll(con);
            for(User u : allUser){
                List<Otoritas> otoritas = new ArrayList<>();
                for(Otoritas o : listOtoritas){
                    if(u.getUsername().equals(o.getUsername()))
                        otoritas.add(o);
                }
                u.setOtoritas(otoritas);
                List<Verifikasi> verifikasi = new ArrayList<>();
                for(Verifikasi v : listVerifikasi){
                    if(u.getUsername().equals(v.getUsername()))
                        verifikasi.add(v);
                }
                u.setVerifikasi(verifikasi);
            }
        }
    }
    private void tutupToko()throws Exception{
        try(Connection con = Koneksi.getConnection()){
            while(tglBarang.parse(sistem.getTglSystem()).before(tglBarang.parse(tglBarang.format(new Date())))){
                StokBarangDAO.insertNewStokBarang(con);
                LocalDate yesterday = LocalDate.parse(sistem.getTglSystem(), DateTimeFormatter.ISO_DATE);
                LocalDate today = yesterday.plusDays(1);
                sistem.setTglSystem(today.toString());
                GadaiHeadDAO.updateBungaGadai(con);
                SistemDAO.update(con, sistem);
            }
            if(tglBarang.parse(sistem.getTglSystem()).after(tglBarang.parse(tglBarang.format(new Date())))){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Application error - Tanggal system tidak cocok dengan tanggal komputer");
                alert.showAndWait();
                System.exit(0);
            } 
        }
    }
    @Override
    public void start(Stage stage) {
        try{
            BufferedReader in = new BufferedReader(new FileReader("koneksi.txt"));
            ipServer = in.readLine();
            printerPenjualan = in.readLine();
            printerGadai = in.readLine();
            printerBarcode = in.readLine();
            
            setSistem();
            tutupToko();
            
            MainStage = stage;
            MainStage.setTitle(sistem.getNama());
            MainStage.setMaximized(true);
            MainStage.getIcons().add(new Image(Main.class.getResourceAsStream("Resource/logo.png")));
            screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            showLoginScene();
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Application error - \n" +e);
            alert.showAndWait();
        }
    }
    public void showLoginScene() {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("View/Login.fxml"));
            AnchorPane container = (AnchorPane) loader.load();
            Scene scene = new Scene(container);
            
            MainStage.hide();
            MainStage.setScene(scene);
            MainStage.show();
            
            LoginController controller = loader.getController();
            controller.setMainApp(this);
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(MainStage);
            alert.setTitle("Error");
            alert.setContentText("Application error - \n" +e);
            alert.showAndWait();
            e.printStackTrace();
        }
    }
    public void showMainScene(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("View/Main.fxml"));
            mainLayout = (BorderPane) loader.load();
            Scene scene = new Scene(mainLayout);
            
            MainStage.hide();
            MainStage.setScene(scene);
            MainStage.show();
            
            mainController = loader.getController();
            mainController.setMainApp(this);
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(MainStage);
            alert.setTitle("Error");
            alert.setContentText("Application error - \n" +e);
            alert.showAndWait();
        }
    }
    public void showDashboard(){
        FXMLLoader loader = changeStage("View/Dashboard.fxml");
        DashboardController controller = loader.getController();
        controller.setMainApp(this);
        setTitle("Dashboard");
    }
    public void showBarcodeBarang(){
        Stage stage = new Stage();
        FXMLLoader loader = showDialog(MainStage, stage, "View/Dialog/BarcodeBarang.fxml");
        BarcodeBarangController controller = loader.getController();
        controller.setMainApp(this, stage);
    }
    public void showStokOpname(){
        FXMLLoader loader = changeStage("View/StokOpname.fxml");
        StokOpnameController controller = loader.getController();
        controller.setMainApp(this);
        setTitle("Stok Opname Barang");
    }
    public void showPenjualan(){
        FXMLLoader loader = changeStage("View/Penjualan.fxml");
        PenjualanController controller = loader.getController();
        controller.setMainApp(this);
        setTitle("Penjualan");
    }
    public void showPembelian(){
        FXMLLoader loader = changeStage("View/Pembelian.fxml");
        PembelianController controller = loader.getController();
        controller.setMainApp(this);
        setTitle("Pembelian");
    }
    public void showTerimaGadai(){
        FXMLLoader loader = changeStage("View/TerimaGadai.fxml");
        TerimaGadaiController controller = loader.getController();
        controller.setMainApp(this);
        setTitle("Terima Gadai");
    }
    public void showPelunasanGadai(){
        FXMLLoader loader = changeStage("View/CariGadai.fxml");
        CariGadaiController controller = loader.getController();
        controller.setMainApp(this);
        controller.setStatus("pelunasan gadai");
        setTitle("Pelunasan Gadai");
    }
    public void showCicilGadai(){
        FXMLLoader loader = changeStage("View/CariGadai.fxml");
        CariGadaiController controller = loader.getController();
        controller.setMainApp(this);
        controller.setStatus("cicil gadai");
        setTitle("Cicil Gadai");
    }
    public void showPembelianGadai(){
        FXMLLoader loader = changeStage("View/CariGadai.fxml");
        CariGadaiController controller = loader.getController();
        controller.setMainApp(this);
        controller.setStatus("pembelian gadai");
        setTitle("Pembelian Gadai");
    }
    public void showLaporanBarang(){
        FXMLLoader loader = changeStage("View/Report/LaporanBarang.fxml");
        LaporanBarangController controller = loader.getController();
        controller.setMainApp(this);
        setTitle("Laporan Barang");
    }
    public void showLaporanStokBarang(){
        FXMLLoader loader = changeStage("View/Report/LaporanStokBarang.fxml");
        LaporanStokBarangController controller = loader.getController();
        controller.setMainApp(this);
        setTitle("Laporan Stok Barang");
    }
    public void showLaporanBarcodeBarang(){
        FXMLLoader loader = changeStage("View/Report/LaporanBarcodeBarang.fxml");
        LaporanBarcodeBarangController controller = loader.getController();
        controller.setMainApp(this);
        setTitle("Laporan Barcode Barang");
    }
    public void showLaporanBatalBarcode(){
        FXMLLoader loader = changeStage("View/Report/LaporanBatalBarcode.fxml");
        LaporanBatalBarcodeController controller = loader.getController();
        controller.setMainApp(this);
        setTitle("Laporan Batal Barcode");
    }
    public void showLaporanHancurBarang(){
        FXMLLoader loader = changeStage("View/Report/LaporanHancurBarang.fxml");
        LaporanHancurBarangController controller = loader.getController();
        controller.setMainApp(this);
        setTitle("Laporan Hancur Barang");
    }
    public void showLaporanUbahHargaKategori(){
        FXMLLoader loader = changeStage("View/Report/LaporanUbahHargaKategori.fxml");
        LaporanUbahHargaKategoriController controller = loader.getController();
        controller.setMainApp(this);
        setTitle("Laporan Ubah Harga Kategori");
    }
    public void showLaporanPenjualan(){
        FXMLLoader loader = changeStage("View/Report/LaporanPenjualan.fxml");
        LaporanPenjualanController controller = loader.getController();
        controller.setMainApp(this);
        setTitle("Laporan Penjualan");
    }
    public void showLaporanPembelian(){
        FXMLLoader loader = changeStage("View/Report/LaporanPembelian.fxml");
        LaporanPembelianController controller = loader.getController();
        controller.setMainApp(this);
        setTitle("Laporan Pembelian");
    }
    public void showLaporanTerimaGadai(){
        FXMLLoader loader = changeStage("View/Report/LaporanTerimaGadai.fxml");
        LaporanTerimaGadaiController controller = loader.getController();
        controller.setMainApp(this);
        setTitle("Laporan Terima Gadai");
    }
    public void showLaporanPelunasanGadai(){
        FXMLLoader loader = changeStage("View/Report/LaporanPelunasanGadai.fxml");
        LaporanPelunasanGadaiController controller = loader.getController();
        controller.setMainApp(this);
        setTitle("Laporan Pelunasan Gadai");
    }
    public void showLaporanStokGadai(){
        FXMLLoader loader = changeStage("View/Report/LaporanStokGadai.fxml");
        LaporanStokGadaiController controller = loader.getController();
        controller.setMainApp(this);
        setTitle("Laporan Stok Gadai");
    }
    public void showLaporanBatalTerimaGadai(){
        FXMLLoader loader = changeStage("View/Report/LaporanBatalTerimaGadai.fxml");
        LaporanBatalTerimaGadaiController controller = loader.getController();
        controller.setMainApp(this);
        setTitle("Laporan Batal Terima Gadai");
    }
    public void showLaporanKeuangan(){
        FXMLLoader loader = changeStage("View/Report/LaporanKeuangan.fxml");
        LaporanKeuanganController controller = loader.getController();
        controller.setMainApp(this);
        setTitle("Laporan Keuangan");
    }
    public void showLaporanTransaksiSales(){
        FXMLLoader loader = changeStage("View/Report/LaporanTransaksiSales.fxml");
        LaporanTransaksiSalesController controller = loader.getController();
        controller.setMainApp(this);
        setTitle("Laporan Transaksi Sales");
    }
    public void showDataUser(){
        Stage stage = new Stage();
        FXMLLoader loader = showDialog(MainStage, stage, "View/Dialog/DataUser.fxml");
        DataUserController controller = loader.getController();
        controller.setMainApp(this, stage);
    }
    public void showBackupAndRestore(){
        Stage stage = new Stage();
        FXMLLoader loader = showDialog(MainStage, stage, "View/Dialog/BackupAndRestore.fxml");
        BackupAndRestoreController controller = loader.getController();
        controller.setMainApp(this, stage);
    }
    public PengaturanUmumController showDataToko(){
        Stage stage = new Stage();
        FXMLLoader loader = showDialog(MainStage, stage, "View/Dialog/PengaturanUmum.fxml");
        PengaturanUmumController controller = loader.getController();
        controller.setMainApp(this, stage);
        return controller;
    }
    public DataKategoriBarangController showDataKategoriBarang(){
        Stage stage = new Stage();
        FXMLLoader loader = showDialog(MainStage, stage, "View/Dialog/DataKategoriBarang.fxml");
        DataKategoriBarangController controller = loader.getController();
        controller.setMainApp(this, stage);
        return controller;
    }
    public DataJenisBarangController showDataJenisBarang(){
        Stage stage = new Stage();
        FXMLLoader loader = showDialog(MainStage, stage, "View/Dialog/DataJenisBarang.fxml");
        DataJenisBarangController controller = loader.getController();
        controller.setMainApp(this, stage);
        return controller;
    }
    public PengaturanGadaiController showSettingGadai(){
        Stage stage = new Stage();
        FXMLLoader loader = showDialog(MainStage, stage, "View/Dialog/PengaturanGadai.fxml");
        PengaturanGadaiController controller = loader.getController();
        controller.setMainApp(this, stage);
        return controller;
    }
    public PengaturanNilaiPokokController showPengaturanNilaiPokok(){
        Stage stage = new Stage();
        FXMLLoader loader = showDialog(MainStage, stage, "View/Dialog/PengaturanNilaiPokok.fxml");
        PengaturanNilaiPokokController controller = loader.getController();
        controller.setMainApp(this, stage);
        return controller;
    }
    public DataKategoriTransaksiController showDataKategoriTransaksi(){
        Stage stage = new Stage();
        FXMLLoader loader = showDialog(MainStage, stage, "View/Dialog/DataKategoriTransaksi.fxml");
        DataKategoriTransaksiController controller = loader.getController();
        controller.setMainApp(this, stage);
        return controller;
    }
    public UbahPasswordController showUbahPassword(){
        Stage stage = new Stage();
        FXMLLoader loader = showDialog(MainStage, stage, "View/Dialog/UbahPassword.fxml");
        UbahPasswordController controller = loader.getController();
        controller.setMainApp(this, stage);
        return controller;
    }
    public void setTitle(String title){
        mainController.setTitle(title);
        if (mainController.vbox.getPrefWidth()!=0) 
            mainController.showMenu();
    }
    public FXMLLoader changeStage(String URL){
        try{
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(URL));
            Node container = loader.load();
            BorderPane border = (BorderPane) mainLayout.getCenter();
            border.setCenter(container);
            return loader;
        }catch(Exception e){
            showMessage(Modality.NONE, "Error", e.toString());
            return null;
        }
    }
    public FXMLLoader showDialog(Stage owner, Stage dialog, String URL){
        try{
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(owner);
            dialog.initStyle(StageStyle.TRANSPARENT);
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(URL));
            AnchorPane container = (AnchorPane) loader.load();

            Scene scene = new Scene(container);
            scene.setFill(Color.TRANSPARENT);
            
            dialog.hide();
            dialog.setScene(scene);
            dialog.show();
            
            dialog.setHeight(screenSize.getHeight());
            dialog.setWidth(screenSize.getWidth());
            dialog.setX((screenSize.getWidth() - dialog.getWidth()) / 2);
            dialog.setY((screenSize.getHeight() - dialog.getHeight()) / 2);
            return loader;
        }catch(IOException e){
            showMessage(Modality.NONE, "Error", e.toString());
            return null;
        }
    }
    public MessageController showMessage(Modality modal,String type,String content){
        try{
            if(message!=null)
                message.close();
            message = new Stage();
            message.initModality(modal);
            message.initOwner(MainStage);
            message.initStyle(StageStyle.TRANSPARENT);
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("View/Dialog/Message.fxml"));
            AnchorPane container = (AnchorPane) loader.load();

            Scene scene = new Scene(container);
            scene.setFill(Color.TRANSPARENT);
            
            message.setX(screenSize.getWidth()-430);
            message.setY(screenSize.getHeight()-180);
//            message.setHeight(screenSize.getHeight()/5);
//            message.setWidth(screenSize.getWidth()/3);
//            message.setX(screenSize.getWidth()-30-screenSize.getWidth()/3);
//            message.setY(screenSize.getHeight()-50-screenSize.getHeight()/5);
            
            final Animation popup = new Transition() {
                { setCycleDuration(Duration.millis(500)); }
                @Override
                protected void interpolate(double frac) {
                    final double curPos = (screenSize.getHeight()-50-screenSize.getHeight()/5) * (1-frac);
                    container.setTranslateY(curPos);
                }
            };
            popup.play();

            message.hide();
            message.setScene(scene);
            message.show();
              
            MessageController controller = loader.getController();
            controller.setMainApp(this,type,content);          
            message.addEventFilter(KeyEvent.KEY_PRESSED, event->{
                if (event.getCode() == KeyCode.ENTER) {
                    if(type.equals("Confirmation"))
                        controller.OK.fire();
                    else
                        closeMessage();
                }
                if (event.getCode() == KeyCode.ESCAPE) {
                    controller.cancel.fire();
                }
            });
            return controller;
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(MainStage);
            alert.setTitle("Error");
            alert.setContentText("Application error - \n" +e);
            alert.showAndWait();
            return null;
        }
    }
    public void closeMessage(){
        message.close();
    }
    public static void main(String[] args) {
        launch(args);
    }
    
}
