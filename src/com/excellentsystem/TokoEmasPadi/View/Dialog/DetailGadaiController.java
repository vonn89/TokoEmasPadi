/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Dialog;

import com.excellentsystem.TokoEmasPadi.DAO.GadaiDetailDAO;
import com.excellentsystem.TokoEmasPadi.DAO.GadaiHeadDAO;
import static com.excellentsystem.TokoEmasPadi.Function.getTableCell;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import static com.excellentsystem.TokoEmasPadi.Main.tglLengkap;
import static com.excellentsystem.TokoEmasPadi.Main.tglSql;
import com.excellentsystem.TokoEmasPadi.Model.GadaiDetail;
import com.excellentsystem.TokoEmasPadi.Model.GadaiHead;
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
public class DetailGadaiController  {

    
    @FXML private TableView<GadaiDetail> detailTable;
    @FXML private TableColumn<GadaiDetail, Number> jumlahColumn;
    @FXML private TableColumn<GadaiDetail, String> kodeKategoriColumn;
    @FXML private TableColumn<GadaiDetail, String> namaBarangColumn;
    @FXML private TableColumn<GadaiDetail, Number> beratColumn;
    @FXML private TableColumn<GadaiDetail, String> kondisiColumn;
    @FXML private TableColumn<GadaiDetail, String> statusSuratColumn;
    @FXML private TableColumn<GadaiDetail, Number> nilaiJualColumn;
    
    @FXML private TextField noGadaiField;
    @FXML private TextField tglGadaiField;
    @FXML private TextField namaField;
    @FXML private TextField alamatField;
    @FXML private TextField salesField;
    
    @FXML private TextField totalpinjamanField;
    @FXML private TextField bungaPersenField;
    @FXML private TextField bungaRpField;
    
    private ObservableList<GadaiDetail> allDetail = FXCollections.observableArrayList();
    private Main mainApp;
    private Stage stage;
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
    public void setGadai(String noGadai){
        try(Connection con = Koneksi.getConnection()){
            GadaiHead g = GadaiHeadDAO.get(con, noGadai);
            g.setListGadaiDetail(GadaiDetailDAO.getAll(con, noGadai));
            noGadaiField.setText("");
            tglGadaiField.setText("");
            namaField.setText("");
            alamatField.setText("");
            salesField.setText("");
            bungaPersenField.setText("0");
            bungaRpField.setText("0");
            allDetail.clear();
            if(g!=null){
                noGadaiField.setText(g.getNoGadai());
                tglGadaiField.setText(tglLengkap.format(tglSql.parse(g.getTglGadai())));
                namaField.setText(g.getNama());
                alamatField.setText(g.getAlamat());
                salesField.setText(g.getKodeSales());
                
                allDetail.addAll(GadaiDetailDAO.getAll(con, g.getNoGadai()));
                totalpinjamanField.setText(rp.format(g.getTotalPinjaman()));
                bungaPersenField.setText(gr.format(g.getBungaPersen()));
                bungaRpField.setText(gr.format(g.getBungaRp()));
            }
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    
}
