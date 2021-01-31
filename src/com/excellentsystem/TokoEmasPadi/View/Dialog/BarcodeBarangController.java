/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Dialog;

import com.excellentsystem.TokoEmasPadi.DAO.JenisDAO;
import com.excellentsystem.TokoEmasPadi.DAO.KategoriDAO;
import com.excellentsystem.TokoEmasPadi.Function;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import static com.excellentsystem.TokoEmasPadi.Main.user;
import com.excellentsystem.TokoEmasPadi.Model.Barang;
import com.excellentsystem.TokoEmasPadi.Model.Jenis;
import com.excellentsystem.TokoEmasPadi.Model.Kategori;
import com.excellentsystem.TokoEmasPadi.Model.PembelianDetail;
import com.excellentsystem.TokoEmasPadi.Service.Service;
import java.sql.Connection;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Xtreme
 */
public class BarcodeBarangController  {
  
    @FXML private ComboBox<Jenis> jenisCombo;
    @FXML private TextField namaBarangField;
    @FXML private TextField namaBarcodeField;
    @FXML private TextField beratField;
    @FXML private TextField beratPembulatanField;
    @FXML private TextField hargaPokokField;
    @FXML private ComboBox<String> keteranganCombo;
    @FXML private TextField merkField;
    @FXML private TextField persentaseField;
    @FXML private TextField jumlahField;
    @FXML private Button saveButton;
    private Main mainApp;  
    private Stage stage;
    private final ObservableList<Jenis> allJenis = FXCollections.observableArrayList(); 
    public void initialize() {
        jenisCombo.setConverter(new StringConverter<Jenis>(){
            @Override
            public String toString(Jenis p){
                return p == null? null : p.getKodeJenis()+ "-" + p.getNamaJenis();
            }
            @Override
            public Jenis fromString(String string){
		Jenis p = null;
                if (string == null){
                    return p;
		}
		int commaIndex = string.indexOf("-");
		if (commaIndex == -1){
                    for(Jenis j : jenisCombo.getItems()){
                        if(j.getKodeJenis().toUpperCase().equals(string.toUpperCase()))
                            p=j;
                    }
		}else{
                    String kodeJenis = string.substring(0, commaIndex);
                    for(Jenis j : jenisCombo.getItems()){
                        if(j.getKodeJenis().toUpperCase().equals(kodeJenis.toUpperCase()))
                            p=j;
                    }
		}
		return p;
            }
        });
        Function.setNumberField(hargaPokokField, rp);
        Function.setNumberField(jumlahField, rp);
        Function.setNumberField(beratPembulatanField, gr);
        Function.setNumberField(persentaseField, gr);
        beratField.setOnKeyReleased((event) -> {
            try{
                String string = beratField.getText();
                if(string.contains("-"))
                    beratField.undo();
                else{
                    if(string.indexOf(".")>0){
                        String string2 = string.substring(string.indexOf(".")+1, string.length());
                        if(string2.contains("."))
                            beratField.undo();
                        else if(!string2.equals("") && Double.parseDouble(string2)!=0)
                            beratField.setText(gr.format(Double.parseDouble(string.replaceAll(",", ""))));
                    }else
                        beratField.setText(gr.format(Double.parseDouble(string.replaceAll(",", ""))));
                }
                beratField.end();
            }catch(Exception e){
                beratField.undo();
            }
//            double hargaBeli = 0;
//            if(jenisCombo.getSelectionModel().getSelectedItem()!=null && keteranganCombo.getSelectionModel().getSelectedItem()!=null){
//                if(keteranganCombo.getSelectionModel().getSelectedItem().equals("Baru"))
//                    hargaBeli = jenisCombo.getSelectionModel().getSelectedItem().getKategori().getHargaBeli();
//                else if(keteranganCombo.getSelectionModel().getSelectedItem().equals("Setengah Baru"))
//                    hargaBeli = jenisCombo.getSelectionModel().getSelectedItem().getKategori().getHargaBeli()-10000;
//                else if(keteranganCombo.getSelectionModel().getSelectedItem().equals("Lama"))
//                    hargaBeli = jenisCombo.getSelectionModel().getSelectedItem().getKategori().getHargaBeli()-20000;
//            }
//            hargaPokokField.setText(rp.format(hargaBeli*Double.parseDouble(beratField.getText().replaceAll(",", ""))));
        });
        jenisCombo.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                namaBarangField.selectAll();
                namaBarangField.requestFocus();
            }
        });
        namaBarangField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                namaBarcodeField.selectAll();
                namaBarcodeField.requestFocus();
            }
        });
        namaBarcodeField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                beratField.selectAll();
                beratField.requestFocus();
            }
        });
        beratField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                beratPembulatanField.selectAll();
                beratPembulatanField.requestFocus();
            }
        });
        beratPembulatanField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                hargaPokokField.selectAll();
                hargaPokokField.requestFocus();
            }
        });
        hargaPokokField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  
                keteranganCombo.requestFocus();
        });
        keteranganCombo.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                merkField.selectAll();
                merkField.requestFocus();
            }
        });
        merkField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                persentaseField.selectAll();
                persentaseField.requestFocus();
            }
        });
        persentaseField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                jumlahField.selectAll();
                jumlahField.requestFocus();
            }
        });
        jumlahField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  
                saveButton.fire();
        });
        saveButton.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  
                saveButton.fire();
        });
    }  
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        jenisCombo.setItems(allJenis);
        setBarcodeBarang();
    }
    private void setBarcodeBarang(){
        try(Connection con = Koneksi.getConnection()){
            allJenis.clear();
            List<Jenis> listJenis = JenisDAO.getAll(con);
            List<Kategori> listKategori = KategoriDAO.getAll(con);
            for(Jenis j : listJenis){
                for(Kategori k : listKategori){
                    if(j.getKodeKategori().equals(k.getKodeKategori()))
                        j.setKategori(k);
                }
                allJenis.add(j);
            }
            ObservableList<String> keterangan = FXCollections.observableArrayList();
            keterangan.add("Baru");
            keterangan.add("Setengah Baru");
            keterangan.add("Lama");
            keteranganCombo.setItems(keterangan);
            keteranganCombo.getSelectionModel().selectFirst();
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    @FXML
    private void cancel(){
        namaBarangField.setText("");
        namaBarcodeField.setText("");
        beratField.setText("0");
        beratPembulatanField.setText("0");
        hargaPokokField.setText("0");
        merkField.setText("");
        persentaseField.setText("0");
        jumlahField.setText("0");
    }
    @FXML
    private void cariBarangDibeli(){
        Stage child = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, child, "View/Dialog/AddBarangDibeli.fxml");
        AddBarangDibeliController c = loader.getController();
        c.setMainApp(mainApp, child);
        c.pembelianTable.setRowFactory(table -> {
            TableRow<PembelianDetail> row = new TableRow<PembelianDetail>() {
                @Override
                public void updateItem(PembelianDetail item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(null);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem pilih = new MenuItem("Pilih Barang");
                        pilih.setOnAction((ActionEvent e) -> {
                            addBarangDibeli(item);
                            child.close();
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            c.getPembelianHeadDetail();
                        });
                        rowMenu.getItems().addAll(pilih, refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)&&mouseEvent.getClickCount() == 2){
                    if(row.getItem()!=null){
                        addBarangDibeli(row.getItem());
                        child.close();
                    }
                }
            });
            return row;
        });
    }
    private void addBarangDibeli(PembelianDetail pb){
        namaBarangField.setText(pb.getNamaBarang());
        namaBarcodeField.setText(pb.getNamaBarang());
        beratField.setText(gr.format(pb.getBerat()));
        beratPembulatanField.setText(gr.format(pb.getBerat()));
        hargaPokokField.setText(rp.format(pb.getHargaBeli()));
        keteranganCombo.getSelectionModel().clearSelection();
        merkField.setText("");
        persentaseField.setText("0");
        jumlahField.setText(rp.format(pb.getQty()));
    }
    @FXML
    private void save(){
        if(jenisCombo.getSelectionModel().getSelectedItem()==null)
            mainApp.showMessage(Modality.NONE, "Warning", "Jenis belum dipilih");
        else if(keteranganCombo.getSelectionModel().getSelectedItem()==null)
            mainApp.showMessage(Modality.NONE, "Warning", "Keterangan belum dipilih");
        else if(namaBarangField.getText().equals(""))
            mainApp.showMessage(Modality.NONE, "Warning", "Nama barang masih kosong");
        else if(namaBarcodeField.getText().equals(""))
            mainApp.showMessage(Modality.NONE, "Warning", "Nama barcode masih kosong");
        else if(jumlahField.getText().equals("")||jumlahField.getText().equals("0"))
            mainApp.showMessage(Modality.NONE, "Warning", "Jumlah masih kosong");
        else if(beratField.getText().equals("")||beratField.getText().equals("0"))
            mainApp.showMessage(Modality.NONE, "Warning", "Berat masih kosong");
        else if(beratPembulatanField.getText().equals("")||beratPembulatanField.getText().equals("0"))
            mainApp.showMessage(Modality.NONE, "Warning", "Berat pembulatan masih kosong");
        else if(persentaseField.getText().equals("")||persentaseField.getText().equals("0"))
            mainApp.showMessage(Modality.NONE, "Warning", "Persentase masih kosong");
        else if(Double.parseDouble(beratPembulatanField.getText().replaceAll(",", ""))<
                Double.parseDouble(beratField.getText().replaceAll(",", "")))
            mainApp.showMessage(Modality.NONE, "Warning", "Berat pembulatan tidak boleh kurang dari berat");
        else{
            try(Connection con = Koneksi.getConnection()){
                Barang b = new Barang();
                b.setNamaBarang(namaBarangField.getText());
                b.setNamaBarcode(namaBarcodeField.getText());
                b.setKodeKategori(jenisCombo.getSelectionModel().getSelectedItem().getKodeKategori());
                b.setKodeJenis(jenisCombo.getSelectionModel().getSelectedItem().getKodeJenis());
                b.setBerat(Double.parseDouble(beratField.getText().replaceAll(",", "")));
                b.setBeratPembulatan(Double.parseDouble(beratPembulatanField.getText().replaceAll(",", "")));
                b.setNilaiPokok(Double.parseDouble(hargaPokokField.getText().replaceAll(",", "")));
                b.setHargaJual(Math.round(Math.ceil(jenisCombo.getSelectionModel().getSelectedItem().getKategori().getHargaJual()*b.getBeratPembulatan()*500)/500));
                b.setKeterangan(keteranganCombo.getSelectionModel().getSelectedItem());
                b.setMerk(merkField.getText());
                b.setPersentase(Double.parseDouble(persentaseField.getText().replaceAll(",", "")));
                b.setBarcodeBy(user.getUsername());
                b.setBarcodeDate(Function.getSystemDate());
                b.setQty(Integer.parseInt(jumlahField.getText().replaceAll(",", "")));
                String status = Service.saveNewBarang(con, b);
                if(status.equals("true")){
                    mainApp.showMessage(Modality.NONE, "Success", "Barcode barang berhasil disimpan");
                    cancel();
                    namaBarangField.selectAll();
                    namaBarangField.requestFocus();
                }else{
                    mainApp.showMessage(Modality.NONE, "Error", status);
                }
            }catch(Exception e){
                mainApp.showMessage(Modality.NONE, "Error", e.toString());
            }
        }
    }
    @FXML
    private void close(){
        stage.close();
    }   
}
