/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View;

import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import static com.excellentsystem.TokoEmasPadi.Main.user;
import com.excellentsystem.TokoEmasPadi.Model.Otoritas;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Xtreme
 */
public class MainController {

    @FXML public VBox vbox;
    @FXML private Label title;
    
    @FXML private Accordion accordion;
    
    @FXML private TitledPane dashboardPane;
    
    @FXML private TitledPane barangPane;
    @FXML private VBox barangVbox;
    @FXML private MenuButton barcodeBarang;
    @FXML private MenuButton stokOpnameBarang;
    
    @FXML private TitledPane penjualanPane;
    @FXML private TitledPane pembelianPane;
    
    @FXML private TitledPane gadaiPane;
    @FXML private VBox gadaiVbox;
    @FXML private MenuButton terimaGadai;
    @FXML private MenuButton pelunasanGadai;
    @FXML private MenuButton cicilGadai;
    @FXML private MenuButton pembelianGadai;
    
    @FXML private TitledPane laporanPane;
    @FXML private VBox laporanVbox;
    @FXML private MenuButton laporanBarang;
    @FXML private MenuButton laporanPenjualan;
    @FXML private MenuButton laporanPembelian;
    @FXML private MenuButton laporanGadai;
    @FXML private MenuButton laporanKeuangan;
    
    @FXML private TitledPane pengaturanPane;
    @FXML private VBox pengaturanVbox;
    @FXML private MenuButton pengaturanGadai;
    @FXML private MenuButton pengaturanNilaiPokok;
    @FXML private MenuButton kategoriBarang;
    @FXML private MenuButton jenisBarang;
    @FXML private MenuButton kategoriTransaksi;
    @FXML private MenuButton dataUser;
    @FXML private MenuButton backupAndRestore;
    
    @FXML private TitledPane loginButton;
    @FXML private VBox userVbox;
    @FXML private MenuButton ubahPasswordButton;
    
    private Main mainApp;
    public void setMainApp(Main mainApp) {
        try{
            this.mainApp = mainApp;
            vbox.setPrefWidth(0);
            vbox.setVisible(false);
            for(Node n : accordion.getPanes()){
                n.managedProperty().bind(n.visibleProperty());
            }
            for(Node n : barangVbox.getChildren()){
                n.managedProperty().bind(n.visibleProperty());
            }
            for(Node n : gadaiVbox.getChildren()){
                n.managedProperty().bind(n.visibleProperty());
            }
            for(Node n : laporanVbox.getChildren()){
                n.managedProperty().bind(n.visibleProperty());
            }
            for(Node n : pengaturanVbox.getChildren()){
                n.managedProperty().bind(n.visibleProperty());
            }
            for(Node n : userVbox.getChildren()){
                n.managedProperty().bind(n.visibleProperty());
            }
            title.setText(sistem.getNama());
//            mainApp.showQuickMenu();
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }    
    public void setUser() {
        BorderPane border = (BorderPane) mainApp.mainLayout.getCenter();
        ImageView img = new ImageView(new Image(Main.class.getResourceAsStream("Resource/ESystemslogoonlywhite.png")));
        img.setFitHeight(150);
        img.setFitWidth(150);
        border.setCenter(img);
        loginButton.setText("Login");
        ubahPasswordButton.setVisible(false);

        barcodeBarang.setVisible(false);
        stokOpnameBarang.setVisible(false);

        dataUser.setVisible(false);
        backupAndRestore.setVisible(false);
        kategoriBarang.setVisible(false);
        jenisBarang.setVisible(false);
        kategoriTransaksi.setVisible(false);
        pengaturanGadai.setVisible(false);
        pengaturanNilaiPokok.setVisible(false);

        laporanBarang.setVisible(false);
        laporanPenjualan.setVisible(false);
        laporanPembelian.setVisible(false);
        laporanGadai.setVisible(false);
        laporanKeuangan.setVisible(false);
        
        if(user!=null){
            ubahPasswordButton.setVisible(true);
            loginButton.setText(user.getUsername());
            if(user.getLevel().equals("Manager")){
                mainApp.showDashboard();
            }
            for(Otoritas o : user.getOtoritas()){
                if(o.getJenis().equals("Dashboard")){
                    if(!o.isStatus())
                        accordion.getPanes().remove(dashboardPane);
                }else if(o.getJenis().equals("Barcode Barang")){
                    barcodeBarang.setVisible(o.isStatus());
                }else if(o.getJenis().equals("Stok Opname Barang")){
                    stokOpnameBarang.setVisible(o.isStatus());
                }else if(o.getJenis().equals("Penjualan")){
                    if(!o.isStatus())
                        accordion.getPanes().remove(penjualanPane);
                }else if(o.getJenis().equals("Pembelian")){
                    if(!o.isStatus())
                        accordion.getPanes().remove(pembelianPane);
                    
                }else if(o.getJenis().equals("Terima Gadai")){
                    terimaGadai.setVisible(o.isStatus());
                }else if(o.getJenis().equals("Pelunasan Gadai")){
                    pelunasanGadai.setVisible(o.isStatus());
                }else if(o.getJenis().equals("Cicil Gadai")){
                    cicilGadai.setVisible(o.isStatus());
                }else if(o.getJenis().equals("Pembelian Gadai")){
                    pembelianGadai.setVisible(o.isStatus());
                    
                }else if(o.getJenis().equals("Laporan Barang")){
                    laporanBarang.setVisible(o.isStatus());
                }else if(o.getJenis().equals("Laporan Penjualan")){
                    laporanPenjualan.setVisible(o.isStatus());
                }else if(o.getJenis().equals("Laporan Pembelian")){
                    laporanPembelian.setVisible(o.isStatus());
                }else if(o.getJenis().equals("Laporan Gadai")){
                    laporanGadai.setVisible(o.isStatus());
                }else if(o.getJenis().equals("Laporan Keuangan")){
                    laporanKeuangan.setVisible(o.isStatus());
                
                }else if(o.getJenis().equals("Pengaturan Gadai")){
                    pengaturanGadai.setVisible(o.isStatus());
                }else if(o.getJenis().equals("Pengaturan Nilai Pokok")){
                    pengaturanNilaiPokok.setVisible(o.isStatus());
                }else if(o.getJenis().equals("Kategori Barang")){
                    kategoriBarang.setVisible(o.isStatus());
                }else if(o.getJenis().equals("Jenis Barang")){
                    jenisBarang.setVisible(o.isStatus());
                }else if(o.getJenis().equals("Kategori Transaksi")){
                    kategoriTransaksi.setVisible(o.isStatus());
                }else if(o.getJenis().equals("Data User")){
                    dataUser.setVisible(o.isStatus());
                }else if(o.getJenis().equals("Backup Dan Restore")){
                    backupAndRestore.setVisible(o.isStatus());
                }
            }
        }else{
            loginButton.setText("Front Desk App");
            accordion.getPanes().remove(dashboardPane);
            stokOpnameBarang.setVisible(true);
            laporanPenjualan.setVisible(true);
            laporanPembelian.setVisible(true);
            laporanGadai.setVisible(true);
        }
        if(barcodeBarang.isVisible()==false &&
                stokOpnameBarang.isVisible()==false){
            accordion.getPanes().remove(barangPane);
        }
        if(terimaGadai.isVisible()==false &&
                pelunasanGadai.isVisible()==false &&
                cicilGadai.isVisible()==false &&
                pembelianGadai.isVisible()==false ){
            accordion.getPanes().remove(gadaiPane);
        }
        if(laporanBarang.isVisible()==false &&
                laporanPenjualan.isVisible()==false &&
                laporanPembelian.isVisible()==false &&
                laporanGadai.isVisible()==false &&
                laporanKeuangan.isVisible()==false){
            accordion.getPanes().remove(laporanPane);
        }

        if(pengaturanGadai.isVisible()==false &&
                pengaturanNilaiPokok.isVisible()==false &&
                kategoriBarang.isVisible()==false &&
                jenisBarang.isVisible()==false &&
                kategoriTransaksi.isVisible()==false &&
                dataUser.isVisible()==false &&
                backupAndRestore.isVisible()==false){
            accordion.getPanes().remove(pengaturanPane);
        }
    }
    @FXML
    public void showMenu() {
          final Animation hideSidebar = new Transition() {
            { setCycleDuration(Duration.millis(180)); }
            @Override
            protected void interpolate(double frac) {
              final double curWidth = 220 * (1.0 - frac);
                vbox.setPrefWidth(curWidth);
                vbox.setTranslateX(-220 + curWidth);
                dashboardPane.setExpanded(false);
                barangPane.setExpanded(false);
                penjualanPane.setExpanded(false);
                pembelianPane.setExpanded(false);
                gadaiPane.setExpanded(false);
                laporanPane.setExpanded(false);
                pengaturanPane.setExpanded(false);
                loginButton.setExpanded(false);
            }
          };
          hideSidebar.onFinishedProperty().set((EventHandler<ActionEvent>) (ActionEvent actionEvent) -> {
              vbox.setVisible(false);
          });
          final Animation showSidebar = new Transition() {
            { setCycleDuration(Duration.millis(180)); }
            @Override
            protected void interpolate(double frac) {
              final double curWidth = 220 * frac;
              vbox.setPrefWidth(curWidth);
              vbox.setTranslateX(-220 + curWidth);
            }
          };
          showSidebar.onFinishedProperty().set((EventHandler<ActionEvent>) (ActionEvent actionEvent) -> {
              vbox.setVisible(true);
          });
          if (showSidebar.statusProperty().get() == Animation.Status.STOPPED && hideSidebar.statusProperty().get() == Animation.Status.STOPPED) {
            if (vbox.isVisible()) {
              hideSidebar.play();
            } else {
              vbox.setVisible(true);
              showSidebar.play();
            }
          }
    }
    public void setTitle(String x){
        title.setText(x);
    }
    @FXML
    private void minimize(){
        mainApp.MainStage.setIconified(true);
    }
    @FXML
    private void logout(){
        mainApp.showLoginScene();
    }
    @FXML
    private void exit(){
        System.exit(0);
    }
    @FXML
    private void showUbahPassword(){
        mainApp.showUbahPassword();
    }  
    @FXML
    private void showDashboard(){
        mainApp.showDashboard();
    }
    @FXML
    private void showBarcodeBarang(){
        mainApp.showBarcodeBarang();
    }
    @FXML
    private void showStokOpname(){
        mainApp.showStokOpname();
    }
    @FXML
    private void showPenjualan(){
        mainApp.showPenjualan();
    }
    @FXML
    private void showPembelian(){
        mainApp.showPembelian();
    }
    @FXML
    private void showTerimaGadai(){
        mainApp.showTerimaGadai();
    }
    @FXML
    private void showPelunasanGadai(){
        mainApp.showPelunasanGadai();
    }
    @FXML
    private void showCicilGadai(){
        mainApp.showCicilGadai();
    }
    @FXML
    private void showPembelianGadai(){
        mainApp.showPembelianGadai();
    }
    
    @FXML
    private void showDataUser(){
        mainApp.showDataUser();
    }
    @FXML
    private void showBackupAndRestore(){
        mainApp.showBackupAndRestore();
    }
    @FXML
    private void showDataToko(){
        mainApp.showDataToko();
    }
    @FXML
    private void showDataKategoriBarang(){
        mainApp.showDataKategoriBarang();
    }
    @FXML
    private void showDataJenisBarang(){
        mainApp.showDataJenisBarang();
    }
    @FXML
    private void showSettingGadai(){
        mainApp.showSettingGadai();
    }
    @FXML
    private void showDataKategoriTransaksi(){
        mainApp.showDataKategoriTransaksi();
    }
    @FXML
    private void showPengaturanNilaiPokok(){
        mainApp.showPengaturanNilaiPokok();
    }
    
    @FXML
    private void showLaporanBarang(){
        mainApp.showLaporanBarang();
    }
    @FXML
    private void showLaporanStokBarang(){
        mainApp.showLaporanStokBarang();
    }
    @FXML
    private void showLaporanBarcodeBarang(){
        mainApp.showLaporanBarcodeBarang();
    }
    @FXML
    private void showLaporanBatalBarcode(){
        mainApp.showLaporanBatalBarcode();
    }
    @FXML
    private void showLaporanHancurBarang(){
        mainApp.showLaporanHancurBarang();
    }
    @FXML
    private void showLaporanUbahHargaKategori(){
        mainApp.showLaporanUbahHargaKategori();
    }
    @FXML
    private void showLaporanPenjualan(){
        mainApp.showLaporanPenjualan();
    }
    @FXML
    private void showLaporanPembelian(){
        mainApp.showLaporanPembelian();
    }
    @FXML
    private void showLaporanTerimaGadai(){
        mainApp.showLaporanTerimaGadai();
    }
    @FXML
    private void showLaporanPelunasanGadai(){
        mainApp.showLaporanPelunasanGadai();
    }
    @FXML
    private void showLaporanStokGadai(){
        mainApp.showLaporanStokGadai();
    }
    @FXML
    private void showLaporanBatalTerimaGadai(){
        mainApp.showLaporanBatalTerimaGadai();
    }
    @FXML
    private void showLaporanKeuangan(){
        mainApp.showLaporanKeuangan();
    }
    @FXML
    private void showLaporanTransaksiSales(){
        mainApp.showLaporanTransaksiSales();
    }
}
