/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Dialog;

import com.excellentsystem.TokoEmasPadi.DAO.PembelianDetailDAO;
import com.excellentsystem.TokoEmasPadi.DAO.PembelianHeadDAO;
import com.excellentsystem.TokoEmasPadi.DAO.PenjualanDetailDAO;
import com.excellentsystem.TokoEmasPadi.DAO.PenjualanHeadDAO;
import static com.excellentsystem.TokoEmasPadi.Function.getTableCell;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import static com.excellentsystem.TokoEmasPadi.Main.tglLengkap;
import static com.excellentsystem.TokoEmasPadi.Main.tglSql;
import com.excellentsystem.TokoEmasPadi.Model.PembelianDetail;
import com.excellentsystem.TokoEmasPadi.Model.PembelianHead;
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
public class DetailPembelianController  {
    
    @FXML private TableView<PembelianDetail> detailTable;
    @FXML private TableColumn<PembelianDetail, Number> jumlahColumn;
    @FXML private TableColumn<PembelianDetail, String> kodeKategoriColumn;
    @FXML private TableColumn<PembelianDetail, String> namaBarangColumn;
    @FXML private TableColumn<PembelianDetail, Number> beratColumn;
    @FXML private TableColumn<PembelianDetail, String> kondisiColumn;
    @FXML private TableColumn<PembelianDetail, String> statusSuratColumn;
    @FXML private TableColumn<PembelianDetail, Number> hargaBeliColumn;
    
    @FXML private TextField noPembelianField;
    @FXML private TextField tglPembelianField;
    
    @FXML private TextField namaField;
    @FXML private TextField alamatDesaField;
    @FXML private TextField salesField;
    
    @FXML private TextField totalPembelianField;
    
    private ObservableList<PembelianDetail> allDetail = FXCollections.observableArrayList();
    private Main mainApp;
    private Stage stage;
    public void initialize() {
        jumlahColumn.setCellValueFactory(cellData -> cellData.getValue().qtyProperty());
        jumlahColumn.setCellFactory(col -> getTableCell(rp, ""));
        kodeKategoriColumn.setCellValueFactory(cellData -> cellData.getValue().kodeKategoriProperty());
        namaBarangColumn.setCellValueFactory(cellData -> cellData.getValue().namaBarangProperty());
        kondisiColumn.setCellValueFactory(cellData -> cellData.getValue().kondisiProperty());
        statusSuratColumn.setCellValueFactory(cellData -> cellData.getValue().statusSuratProperty());
        beratColumn.setCellValueFactory(cellData -> cellData.getValue().beratProperty());
        beratColumn.setCellFactory(col -> getTableCell(gr, "gr"));
        hargaBeliColumn.setCellValueFactory(cellData -> cellData.getValue().hargaBeliProperty());
        hargaBeliColumn.setCellFactory(col -> getTableCell(rp, "Rp"));
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
    public void setPembelian(String noPembelian){
        try(Connection con = Koneksi.getConnection()){
            PembelianHead p = PembelianHeadDAO.get(con, noPembelian);
            p.setListPembelianDetail(PembelianDetailDAO.getAll(con, noPembelian));
            
            noPembelianField.setText("");
            tglPembelianField.setText("");
            namaField.setText("");
            alamatDesaField.setText("");
            salesField.setText("");
            totalPembelianField.setText("0");
            allDetail.clear();
            if(p!=null){
                noPembelianField.setText(p.getNoPembelian());
                tglPembelianField.setText(tglLengkap.format(tglSql.parse(p.getTglPembelian())));
                namaField.setText(p.getNama());
                alamatDesaField.setText(p.getAlamat());
                salesField.setText(p.getKodeSales());
                totalPembelianField.setText(rp.format(p.getTotalPembelian()));
                allDetail.addAll(p.getListPembelianDetail());
            }
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    
}
