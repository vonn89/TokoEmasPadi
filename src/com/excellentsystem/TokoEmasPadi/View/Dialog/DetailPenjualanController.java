/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Dialog;

import com.excellentsystem.TokoEmasPadi.DAO.PenjualanDetailDAO;
import com.excellentsystem.TokoEmasPadi.DAO.PenjualanHeadDAO;
import static com.excellentsystem.TokoEmasPadi.Function.getTableCell;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import static com.excellentsystem.TokoEmasPadi.Main.tglLengkap;
import static com.excellentsystem.TokoEmasPadi.Main.tglSql;
import com.excellentsystem.TokoEmasPadi.Model.PenjualanDetail;
import com.excellentsystem.TokoEmasPadi.Model.PenjualanHead;
import java.sql.Connection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Yunaz
 */
public class DetailPenjualanController  {
    @FXML private TableView<PenjualanDetail> detailTable;
    @FXML private TableColumn<PenjualanDetail, Number> jumlahColumn;
    @FXML private TableColumn<PenjualanDetail, String> kodeBarcodeColumn;
    @FXML private TableColumn<PenjualanDetail, String> namaBarangColumn;
    @FXML private TableColumn<PenjualanDetail, Number> beratColumn;
    @FXML private TableColumn<PenjualanDetail, Number> hargaColumn;
    @FXML private TableColumn<PenjualanDetail, Number> totalColumn;
    
    @FXML private TextField noPenjualanField;
    @FXML private TextField tglPenjualanField;
    @FXML private TextField namaField;
    @FXML private TextField alamatField;
    @FXML private TextField salesField;
    @FXML private TextField totalPenjualanField;
    
    private ObservableList<PenjualanDetail> allDetail = FXCollections.observableArrayList();
    private Main mainApp;
    private Stage stage;
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
        detailTable.setItems(allDetail);
    }    
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
    }
    @FXML
    private void close(){
        stage.close();
    }
    public void setPenjualan(String noPenjualan){
        try(Connection con = Koneksi.getConnection()){
            PenjualanHead p = PenjualanHeadDAO.get(con, noPenjualan);
            p.setListPenjualanDetail(PenjualanDetailDAO.getAllByNoPenjualan(con, noPenjualan));
            
            noPenjualanField.setText("");
            tglPenjualanField.setText("");
            namaField.setText("");
            alamatField.setText("");
            salesField.setText("");
            totalPenjualanField.setText("0");
            allDetail.clear();
            if(p!=null){
                noPenjualanField.setText(p.getNoPenjualan());
                tglPenjualanField.setText(tglLengkap.format(tglSql.parse(p.getTglPenjualan())));
                namaField.setText(p.getNama());
                alamatField.setText(p.getAlamat());
                salesField.setText(p.getKodeSales());
                totalPenjualanField.setText(rp.format(p.getGrandtotal()));
                allDetail.addAll(p.getListPenjualanDetail());
            }
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
}
