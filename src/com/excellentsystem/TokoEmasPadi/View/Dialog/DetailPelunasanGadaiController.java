/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Dialog;

import com.excellentsystem.TokoEmasPadi.DAO.GadaiDetailDAO;
import com.excellentsystem.TokoEmasPadi.DAO.GadaiHeadDAO;
import com.excellentsystem.TokoEmasPadi.DAO.KategoriDAO;
import static com.excellentsystem.TokoEmasPadi.Function.getTableCell;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import static com.excellentsystem.TokoEmasPadi.Main.tglLengkap;
import static com.excellentsystem.TokoEmasPadi.Main.tglSql;
import com.excellentsystem.TokoEmasPadi.Model.GadaiDetail;
import com.excellentsystem.TokoEmasPadi.Model.GadaiHead;
import com.excellentsystem.TokoEmasPadi.Model.Kategori;
import static java.lang.Math.ceil;
import java.sql.Connection;
import java.util.List;
import javafx.beans.property.SimpleDoubleProperty;
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
public class DetailPelunasanGadaiController  {

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
    @FXML private TextField salesField;
    
    @FXML private TextField totalpinjamanField;
    @FXML private TextField bungaPersenField;
    @FXML private TextField bungaRpField;
    
    @FXML private TextField pembayaranBungaField;
    @FXML private TextField totalJumlahDiterimaField;
    
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
        nilaiJualSekarangColumn.setCellValueFactory(cellData -> {
            return new SimpleDoubleProperty(
                    ceil(cellData.getValue().getBerat()*cellData.getValue().getKategori().getHargaJual()*500)/500);
        });
        nilaiJualSekarangColumn.setCellFactory(col -> getTableCell(rp, "Rp"));
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
    public void viewGadai(String noGadai){
        try(Connection con = Koneksi.getConnection()){
            GadaiHead p = GadaiHeadDAO.get(con, noGadai);
            p.setListGadaiDetail(GadaiDetailDAO.getAll(con, noGadai));
            List<Kategori> listKategori = KategoriDAO.getAll(con);
            for(GadaiDetail d : p.getListGadaiDetail()){
                for(Kategori k : listKategori){
                    if(d.getKodeKategori().equals(k.getKodeKategori()))
                        d.setKategori(k);
                }
            }
            allDetail.clear();
            noGadaiField.setText("");
            tglGadaiField.setText("");
            tglLunasField.setText("");
            namaField.setText("");
            alamatField.setText("");
            salesField.setText("");
            totalpinjamanField.setText("0");
            bungaPersenField.setText("0");
            bungaRpField.setText("0");
            pembayaranBungaField.setText("0");
            totalJumlahDiterimaField.setText("0");
            if(p!=null){
                noGadaiField.setText(p.getNoGadai());
                tglGadaiField.setText(tglLengkap.format(tglSql.parse(p.getTglGadai())));
                tglLunasField.setText(tglLengkap.format(tglSql.parse(p.getTglLunas())));
                namaField.setText(p.getNama());
                alamatField.setText(p.getAlamat());
                allDetail.addAll(p.getListGadaiDetail());
                totalpinjamanField.setText(rp.format(p.getTotalPinjaman()));
                bungaPersenField.setText(gr.format(p.getBungaPersen()));
                bungaRpField.setText(gr.format(p.getBungaKomp()));
                salesField.setText(p.getSalesLunas());
                pembayaranBungaField.setText(rp.format(p.getBungaRp()));
                totalJumlahDiterimaField.setText(rp.format(p.getTotalPinjaman()+p.getBungaRp()));
            }
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    
}
