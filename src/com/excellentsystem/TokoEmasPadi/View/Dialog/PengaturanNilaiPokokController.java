/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Dialog;

import com.excellentsystem.TokoEmasPadi.DAO.BarangDAO;
import com.excellentsystem.TokoEmasPadi.DAO.JenisDAO;
import com.excellentsystem.TokoEmasPadi.DAO.KategoriDAO;
import com.excellentsystem.TokoEmasPadi.Function;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import com.excellentsystem.TokoEmasPadi.Model.Barang;
import com.excellentsystem.TokoEmasPadi.Model.Jenis;
import com.excellentsystem.TokoEmasPadi.Model.Kategori;
import com.excellentsystem.TokoEmasPadi.Model.User;
import com.excellentsystem.TokoEmasPadi.Service.Service;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Excellent
 */
public class PengaturanNilaiPokokController  {

    @FXML private DatePicker tglAwalPicker;
    @FXML private DatePicker tglAkhirPicker;
    @FXML private ComboBox<String> kategoriCombo;
    @FXML private ComboBox<String> subKategoriCombo;
    @FXML private ComboBox<String> keteranganCombo;
    @FXML private TextField persentaseField;
    
    private Main mainApp;  
    private Stage stage;
    private final ObservableList<String> allKategori = FXCollections.observableArrayList();
    private final ObservableList<String> allJenis = FXCollections.observableArrayList();
    private final ObservableList<String> allKeterangan = FXCollections.observableArrayList();
    public void initialize() {
        Function.setNumberField(persentaseField, gr);
        tglAwalPicker.setConverter(Function.getTglConverter());
        tglAwalPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglAwalPicker.setDayCellFactory((final DatePicker datePicker) -> 
                Function.getDateCellMulai(tglAkhirPicker, LocalDate.parse(sistem.getTglSystem())));
        tglAkhirPicker.setConverter(Function.getTglConverter());
        tglAkhirPicker.setValue(LocalDate.parse(sistem.getTglSystem()));
        tglAkhirPicker.setDayCellFactory((final DatePicker datePicker) -> 
                Function.getDateCellAkhir(tglAwalPicker, LocalDate.parse(sistem.getTglSystem())));
    }    
    public void setMainApp(Main mainApp, Stage stage) {
        try(Connection con = Koneksi.getConnection()){
            this.mainApp = mainApp;
            this.stage = stage;
            List<Kategori> kategori = KategoriDAO.getAll(con);
            for(Kategori k : kategori){
                allKategori.add(k.getKodeKategori());
            }
            kategoriCombo.setItems(allKategori);
            kategoriCombo.getSelectionModel().selectFirst();

            allJenis.add("Semua");
            List<Jenis> jenis = JenisDAO.getAll(con);
            for(Jenis j : jenis){
                allJenis.add(j.getKodeJenis());
            }
            subKategoriCombo.setItems(allJenis);
            subKategoriCombo.getSelectionModel().select("Semua");
            
            allKeterangan.clear();
            allKeterangan.add("Baru");
            allKeterangan.add("Setengah Baru");
            allKeterangan.add("Lama");
            keteranganCombo.setItems(allKeterangan);
            keteranganCombo.getSelectionModel().selectFirst();
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    @FXML
    private void cariBarang(){
        List<Barang> listBarang = new ArrayList<>();
        try(Connection con = Koneksi.getConnection()){
            String jenis = "%";
            if(!subKategoriCombo.getSelectionModel().getSelectedItem().equals("Semua"))
                jenis = subKategoriCombo.getSelectionModel().getSelectedItem();
            
            listBarang = BarangDAO.getAllByTglBarcode(
                    con, tglAwalPicker.getValue().toString(), tglAkhirPicker.getValue().toString(), 
                    kategoriCombo.getSelectionModel().getSelectedItem(), jenis , 
                    keteranganCombo.getSelectionModel().getSelectedItem(), persentaseField.getText(), "%");
            stage.close();
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/UbahNilaiPokok.fxml");
        UbahNilaiPokokController controller = loader.getController();
        controller.setMainApp(mainApp, child);
        controller.setBarang(listBarang);
        controller.saveButton.setOnAction((event) -> {
            if(controller.allBarang.isEmpty()){
                mainApp.showMessage(Modality.NONE, "Warning", "Data barang masih kosong");
            }else if(controller.nilaiPokokPerGramField.getText().equals("0")){
                mainApp.showMessage(Modality.NONE, "Warning", "Nilai pokok per gram masih kosong");
            }else{
                Stage stageVerifikasi = new Stage();
                FXMLLoader loaderVerifikasi = mainApp.showDialog(stage, stageVerifikasi, "View/Dialog/Verifikasi.fxml");
                VerifikasiController v = loaderVerifikasi.getController();
                v.setMainApp(mainApp, stageVerifikasi);
                v.keteranganLabel.setText("Verifikasi : ");
                v.password.setOnKeyPressed((KeyEvent) -> {
                    if (KeyEvent.getCode() == KeyCode.ENTER)  {
                        stageVerifikasi.close();
                        User user = Function.verifikasi(v.password.getText(), "Ubah Nilai Pokok");
                        if(user!=null){
                            try(Connection con = Koneksi.getConnection()){
                                String status = Service.saveUpdateNilaiPokok(con, controller.allBarang, 
                                        Double.parseDouble(controller.nilaiPokokPerGramField.getText().replaceAll(",", "")));
                                if(status.equals("true")){
                                    child.close();
                                    mainApp.showMessage(Modality.NONE, "Success", "Ubah Nilai Pokok berhasil disimpan");
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
        }); 
    }
    
    @FXML
    private void close(){
        stage.close();
    }  
}
