/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View;

import com.excellentsystem.TokoEmasPadi.DAO.BarangDAO;
import com.excellentsystem.TokoEmasPadi.DAO.StokOpnameDetailDAO;
import com.excellentsystem.TokoEmasPadi.DAO.StokOpnameHeadDAO;
import com.excellentsystem.TokoEmasPadi.Function;
import static com.excellentsystem.TokoEmasPadi.Function.getTableCell;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import com.excellentsystem.TokoEmasPadi.Model.Barang;
import com.excellentsystem.TokoEmasPadi.Model.StokOpnameDetail;
import com.excellentsystem.TokoEmasPadi.Model.StokOpnameHead;
import com.excellentsystem.TokoEmasPadi.View.Dialog.DetailBarangController;
import com.excellentsystem.TokoEmasPadi.View.Dialog.LanjutStokOpnameController;
import com.excellentsystem.TokoEmasPadi.View.Dialog.NewStokOpnameController;
import java.sql.Connection;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Yunaz
 */
public class StokOpnameController  {

    @FXML private TableView<StokOpnameDetail> barangTable;
    @FXML private TableColumn<StokOpnameDetail, String> kodeBarcodeColumn;
    @FXML private TableColumn<StokOpnameDetail, String> namaBarangColumn;
    @FXML private TableColumn<StokOpnameDetail, String> kodeJenisColumn;
    @FXML private TableColumn<StokOpnameDetail, String> merkColumn;
    @FXML private TableColumn<StokOpnameDetail, String> keteranganColumn;
    @FXML private TableColumn<StokOpnameDetail, Number> beratColumn;
    @FXML private TableColumn<StokOpnameDetail, Number> beratPembulatanColumn;
    @FXML private TableColumn<StokOpnameDetail, Number> qtyColumn;
    @FXML private TableColumn<StokOpnameDetail, Number> qtyDistokColumn;
    
    @FXML private Label noStokOpnameLabel;
    @FXML private Button newStokOpnameButton;
    @FXML private Button lanjutStokOpnameButton;
    @FXML private TextField kodeBarcodeField;
    @FXML private Label totalQtyDistokLabel;
    @FXML private Label totalQtyLabel;
    @FXML private Label totalBeratLabel;
    @FXML private Label totalBeratPembulatanLabel;
    private Main mainApp;   
    private final ObservableList<StokOpnameDetail> allBarang = FXCollections.observableArrayList();
    public void initialize() {
        kodeBarcodeColumn.setCellValueFactory(cellData -> cellData.getValue().kodeBarcodeProperty());
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().namaBarangProperty());
        kodeJenisColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().kodeJenisProperty());
        merkColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().merkProperty());
        keteranganColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().keteranganProperty());
        beratColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().beratProperty());
        beratColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        beratPembulatanColumn.setCellValueFactory(cellData -> cellData.getValue().getBarang().beratPembulatanProperty());
        beratPembulatanColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        qtyColumn.setCellValueFactory(cellData -> cellData.getValue().qtyProperty());
        qtyColumn.setCellFactory(col -> getTableCell(rp, ""));
        qtyDistokColumn.setCellValueFactory(cellData -> cellData.getValue().qtyDiStokProperty());
        qtyDistokColumn.setCellFactory(col -> getTableCell(rp, ""));
        
        kodeBarcodeField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  
                stokBarcode();
        });
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            getStokOpname();
        });
        rowMenu.getItems().addAll(refresh);
        barangTable.setContextMenu(rowMenu);
        barangTable.setRowFactory(table -> {
            TableRow<StokOpnameDetail> row = new TableRow<StokOpnameDetail>() {
                @Override
                public void updateItem(StokOpnameDetail item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rowMenu);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem lihat = new MenuItem("Lihat Detail Barang");
                        lihat.setOnAction((ActionEvent e) -> {
                            lihatDetailBarang(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            getStokOpname();
                        });
                        rowMenu.getItems().addAll(lihat,refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
//            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
//                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)&&
//                        mouseEvent.getClickCount() == 2){
//                    if(row.getItem().isStatus()){
//                        try {
//                            if(con.isClosed()) setService();
//                            row.getItem().setStatus(false);
//                            row.getItem().setKodeSales("");
//                            stokOpnameDetailDao.updateStokOpnameDetail(row.getItem());
//                            getStokOpname();
//                        } catch (Exception ex) {
//                            mainApp.showMessage(Modality.NONE, "Warning", ex.toString());
//                        }
//                    }else{
//                        try {
//                            if(con.isClosed()) setService();
//                            row.getItem().setStatus(true);
//                            row.getItem().setKodeSales(salesCombo.getSelectionModel().getSelectedItem());
//                            stokOpnameDetailDao.updateStokOpnameDetail(row.getItem());
//                            getStokOpname();
//                        } catch (Exception ex) {
//                            mainApp.showMessage(Modality.NONE, "Warning", ex.toString());
//                        }
//                    }
//                }
//            });
            return row;
        });
    }    
    public void setMainApp(Main mainApp){
        this.mainApp = mainApp;
        barangTable.setItems(allBarang);
    } 
    private void getBarang(String kodeKategori, String kodeJenis){
        try(Connection con = Koneksi.getConnection()){
            allBarang.clear();
            StokOpnameHead s = new StokOpnameHead();
            s.setNoStokOpname(StokOpnameHeadDAO.getId(con));
            s.setTglStokOpname(Function.getSystemDate());
            s.setKodeKategori(kodeKategori);
            s.setKodeJenis(kodeJenis);
            
            int totalQty = 0;
            double totalBerat = 0;
            double totalBeratPembulatan = 0;
            int i = 1;
            
            String kategori = "%";
            if(!kodeKategori.equals("Semua"))
                kategori = kodeKategori;
            String jenis = "%";
            if(!kodeJenis.equals("Semua"))
                jenis = kodeJenis;
            List<Barang> x = BarangDAO.getAllByKategoriAndJenisAndKeteranganAndMerkAndStokAda(con, kategori, jenis, "%", "%");
            for(Barang b : x){
                StokOpnameDetail d = new StokOpnameDetail();
                d.setNoStokOpname(s.getNoStokOpname());
                d.setNoUrut(i);
                d.setKodeBarcode(b.getKodeBarcode());
                d.setQty(b.getQty());
                d.setQtyDiStok(0);
                d.setBarang(b);
                StokOpnameDetailDAO.insert(con, d);
                
                allBarang.add(d);
                
                totalQty = totalQty + b.getQty();
                totalBerat = totalBerat + (b.getBerat()*b.getQty());
                totalBeratPembulatan = totalBeratPembulatan + (b.getBeratPembulatan()*b.getQty());
                
                i++;
            }
            s.setTotalQty(totalQty);
            s.setTotalBerat(totalBerat);
            s.setTotalBeratPembulatan(totalBeratPembulatan);
            StokOpnameHeadDAO.insert(con, s);
            noStokOpnameLabel.setText(s.getNoStokOpname());
            noStokOpnameLabel.setVisible(true);
            newStokOpnameButton.setVisible(false);
            lanjutStokOpnameButton.setVisible(false);
            hitungTotal();
        }catch(Exception e){
            e.printStackTrace();
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    private void getStokOpname(){
        try(Connection con = Koneksi.getConnection()){
            allBarang.clear();
            if(noStokOpnameLabel.getText().equals(""))
                mainApp.showMessage(Modality.NONE, "Warning", "No Stok Opname belum dipilih");
            else{
                StokOpnameHead sHead = StokOpnameHeadDAO.get(con, noStokOpnameLabel.getText());
                if(sHead==null){
                    mainApp.showMessage(Modality.NONE, "Warning", "No Stok Opname tidak ditemukan");
                    noStokOpnameLabel.setText("");
                }else{
                    List<StokOpnameDetail> stok = StokOpnameDetailDAO.getAll(con,
                            noStokOpnameLabel.getText());
                    for(StokOpnameDetail s : stok){
                        s.setBarang(BarangDAO.get(con, s.getKodeBarcode()));
                    }
                    allBarang.addAll(stok);
                    noStokOpnameLabel.setVisible(true);
                    newStokOpnameButton.setVisible(false);
                    lanjutStokOpnameButton.setVisible(false);
                    hitungTotal();
                }
            }
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    @FXML
    private void newStokOpname(){
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/NewStokOpname.fxml");
        NewStokOpnameController c = loader.getController();
        c.setMainApp(mainApp, stage);
        c.okButton.setOnAction((event) -> {
            getBarang(c.kodeKategoriCombo.getSelectionModel().getSelectedItem(), c.kodeJenisCombo.getSelectionModel().getSelectedItem());
            stage.close();
        });
    }
    @FXML
    private void lanjutStokOpname(){
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/LanjutStokOpname.fxml");
        LanjutStokOpnameController c = loader.getController();
        c.setMainApp(mainApp, stage);
        c.okButton.setOnAction((event) -> {
            if(c.noStokOpnameField.getText().equals(""))
                mainApp.showMessage(Modality.NONE, "Warning", "No Stok Opname masih kosong");
            else{
                noStokOpnameLabel.setText(c.noStokOpnameField.getText());
                getStokOpname();
                stage.close();
            }
        });
    }
    private void hitungTotal(){
        int totalQtyDistok = 0;
        int totalQty = 0;
        double totalBerat = 0;
        double totalBeratPembulatan = 0;
        for(StokOpnameDetail b : allBarang){
            totalQtyDistok = totalQtyDistok + b.getQtyDiStok();
            totalQty = totalQty + b.getBarang().getQty();
            totalBerat = totalBerat + b.getBarang().getBerat() * b.getBarang().getQty();
            totalBeratPembulatan = totalBeratPembulatan + b.getBarang().getBeratPembulatan() * b.getBarang().getQty();
        }
        totalQtyDistokLabel.setText(gr.format(totalQtyDistok));
        totalQtyLabel.setText(gr.format(totalQty));
        totalBeratLabel.setText(gr.format(totalBerat)+" gr");
        totalBeratPembulatanLabel.setText(gr.format(totalBeratPembulatan)+" gr");
    }
    private void lihatDetailBarang(StokOpnameDetail b){
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailBarang.fxml");
        DetailBarangController controller = loader.getController();
        controller.setMainApp(mainApp, stage);
        controller.setBarang(b.getBarang());
        controller.setViewOnly();
    }
    @FXML
    private void stokBarcode(){
        if(kodeBarcodeField.getText().equals(""))
            mainApp.showMessage(Modality.NONE, "Warning","Kode Barcode masih kosong");
        else{
            Boolean status = false;
            for(StokOpnameDetail s : allBarang){
                if(s.getKodeBarcode().equals(kodeBarcodeField.getText())){
                    status = true;
                    try(Connection con = Koneksi.getConnection()){
                        s.setQtyDiStok(s.getQtyDiStok()+1);
                        StokOpnameDetailDAO.update(con, s);
                        getStokOpname();
                        kodeBarcodeField.setText("");
                        kodeBarcodeField.requestFocus();
                    }catch(Exception e){
                        mainApp.showMessage(Modality.NONE, "Error", e.toString());
                    }
                }
            }
            if(!status){
                mainApp.showMessage(Modality.NONE, "Warning", "Kode Barcode tidak ditemukan");
            }
        }
    }
    
}
