/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.excellentsystem.TokoEmasPadi.View.Dialog;

import com.excellentsystem.TokoEmasPadi.DAO.KategoriTransaksiDAO;
import com.excellentsystem.TokoEmasPadi.Function;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import com.excellentsystem.TokoEmasPadi.Model.KategoriTransaksi;
import java.sql.Connection;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Xtreme
 */
public class NewKeuanganController  {

    
    public ComboBox<String> kategoriCombo;
    public TextArea keteranganField;
    public TextField jumlahRpField;
    public Button saveButton;
    public Button cancelButton;
    private Main mainApp;   
    private Stage stage;
    public List<KategoriTransaksi> kategoriTransaksi;
    public void setMainApp(Main mainApp, Stage stage) {
        try(Connection con = Koneksi.getConnection()){
            this.mainApp = mainApp;
            this.stage = stage;
            ObservableList<String> allKategori = FXCollections.observableArrayList();
            kategoriTransaksi = KategoriTransaksiDAO.getAll(con);
            for(KategoriTransaksi k : kategoriTransaksi){
                allKategori.add(k.getKodeKategori());
            }
            kategoriCombo.setItems(allKategori);
            Function.setNumberField(jumlahRpField, rp);
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    public void closeDialog(){
        stage.close();
    }    
    
}
