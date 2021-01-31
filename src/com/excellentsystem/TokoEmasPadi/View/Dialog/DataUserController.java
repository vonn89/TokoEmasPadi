/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Dialog;

import com.excellentsystem.TokoEmasPadi.DAO.OtoritasDAO;
import com.excellentsystem.TokoEmasPadi.DAO.UserDAO;
import com.excellentsystem.TokoEmasPadi.DAO.VerifikasiDAO;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import com.excellentsystem.TokoEmasPadi.Model.Otoritas;
import com.excellentsystem.TokoEmasPadi.Model.User;
import com.excellentsystem.TokoEmasPadi.Model.Verifikasi;
import com.excellentsystem.TokoEmasPadi.Service.Service;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Xtreme
 */
public class DataUserController {

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> levelColumn;
    
    @FXML private TableView<Otoritas> otoritasTable;
    @FXML private TableColumn<Otoritas, String> jenisColumn;
    @FXML private TableColumn<Otoritas, Boolean> statusColumn;
    
    @FXML private TableView<Verifikasi> verifikasiTable;
    @FXML private TableColumn<Verifikasi, String> jenisVerifikasiColumn;
    @FXML private TableColumn<Verifikasi, Boolean> statusVerifikasiColumn;
    
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> levelCombo;
    @FXML private CheckBox checkOtoritas;
    @FXML private CheckBox checkVerifikasi;
    
    private Main mainApp;
    private Stage stage;
    private User user;
    private String status = "";
    private ObservableList<User> allUser = FXCollections.observableArrayList();
    private ObservableList<Otoritas> otoritas = FXCollections.observableArrayList();
    private ObservableList<Verifikasi> verifikasi = FXCollections.observableArrayList();
    public void initialize() {
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        levelColumn.setCellValueFactory(cellData -> cellData.getValue().levelProperty());
        jenisColumn.setCellValueFactory(cellData -> cellData.getValue().jenisProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        jenisVerifikasiColumn.setCellValueFactory(cellData -> cellData.getValue().jenisProperty());
        statusVerifikasiColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        
        userTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> selectUser(newValue));
        
        statusColumn.setCellFactory(CheckBoxTableCell.forTableColumn((Integer param) -> 
                otoritasTable.getItems().get(param).statusProperty()));
        statusVerifikasiColumn.setCellFactory(CheckBoxTableCell.forTableColumn((Integer param) -> 
                verifikasiTable.getItems().get(param).statusProperty()));
        
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem addNew = new MenuItem("Tambah User Baru");
        addNew.setOnAction((ActionEvent e)->{
            newUser();
        });
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent e) -> {
            reset();
        });
        rowMenu.getItems().addAll(addNew,refresh);
        userTable.setContextMenu(rowMenu);
        userTable.setRowFactory(table -> {
            TableRow<User> row = new TableRow<User>() {
                @Override
                public void updateItem(User item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(rowMenu);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem addNew = new MenuItem("Tambah User Baru");
                        addNew.setOnAction((ActionEvent e)->{
                            newUser();
                        });
                        MenuItem delete = new MenuItem("Hapus User");
                        delete.setOnAction((ActionEvent e) -> {
                            deleteUser(item);
                        });
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent e) -> {
                            reset();
                        });
                        rowMenu.getItems().addAll(addNew,delete,refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            return row;
        });
        userTable.setItems(allUser);
        otoritasTable.setItems(otoritas);
        verifikasiTable.setItems(verifikasi);
    }
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        ObservableList<String> allLevel = FXCollections.observableArrayList();
        allLevel.clear();
        allLevel.add("Admin");
        allLevel.add("Sales");
        allLevel.add("Manager");
        levelCombo.setItems(allLevel);
        reset();
    }
    @FXML
    private void close(){
        stage.close();
    }    
    @FXML
    private void checkOtoritas(){
        for(Otoritas o : otoritas){
            o.setStatus(checkOtoritas.isSelected());
        }
        otoritasTable.refresh();
    }
    @FXML
    private void checkVerifikasi(){
        for(Verifikasi o : verifikasi){
            o.setStatus(checkVerifikasi.isSelected());
        }
        verifikasiTable.refresh();
    }
    @FXML
    private void reset(){
        try(Connection con = Koneksi.getConnection()){
            allUser.clear();
            List<User> listUser = UserDAO.getAll(con);
            List<Otoritas> listOtoritas = OtoritasDAO.getAll(con);
            List<Verifikasi> listVerifikasi = VerifikasiDAO.getAll(con);
            for(User u : listUser){
                List<Otoritas> oto = new ArrayList<>();
                for(Otoritas o : listOtoritas){
                    if(u.getUsername().equals(o.getUsername()))
                        oto.add(o);
                }
                u.setOtoritas(oto);
                List<Verifikasi> ver = new ArrayList<>();
                for(Verifikasi v : listVerifikasi){
                    if(u.getUsername().equals(v.getUsername()))
                        ver.add(v);
                }
                u.setVerifikasi(ver);
            }
            allUser.addAll(listUser);
            verifikasi.clear();
            otoritas.clear();
            usernameField.setText("");
            passwordField.setText("");
            levelCombo.getSelectionModel().clearSelection();
            usernameField.setDisable(true);
            passwordField.setDisable(true);
            levelCombo.setDisable(true);
            user=null;
            status="";
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    private void selectUser(User value){
        status="";
        user=null;
        usernameField.setText("");
        passwordField.setText("");
        levelCombo.getSelectionModel().clearSelection();
        usernameField.setDisable(true);
        passwordField.setDisable(true);
        levelCombo.setDisable(true);
        otoritas.clear();
        verifikasi.clear();
        if(value!=null){
            status="update";
            user = value;
            usernameField.setText(user.getUsername());
            passwordField.setText(user.getPassword());
            levelCombo.getSelectionModel().select(user.getLevel());
            otoritas.addAll(user.getOtoritas());
            verifikasi.addAll(user.getVerifikasi());
            passwordField.setDisable(false);
            levelCombo.setDisable(false);
        }
    }    
    @FXML
    private void newUser(){
        status = "new";
        user = new User();
        usernameField.setDisable(false);
        passwordField.setDisable(false);
        levelCombo.setDisable(false);
        usernameField.setText("");
        passwordField.setText("");
        levelCombo.getSelectionModel().clearSelection();

        List<String> jenis = new ArrayList<>();
        jenis.add("Barcode Barang");
        jenis.add("Stok Opname Barang");

        jenis.add("Penjualan");
        jenis.add("Pembelian");

        jenis.add("Terima Gadai");
        jenis.add("Pelunasan Gadai");
        jenis.add("Cicil Gadai");
        jenis.add("Pembelian Gadai");

        jenis.add("Laporan Barang");
        jenis.add("Laporan Penjualan");
        jenis.add("Laporan Pembelian");
        jenis.add("Laporan Gadai");
        jenis.add("Laporan Keuangan");

        jenis.add("Pengaturan Gadai");
        jenis.add("Pengaturan Nilai Pokok");
        jenis.add("Data User");
        jenis.add("Kategori Barang");
        jenis.add("Jenis Barang");
        jenis.add("Kategori Transaksi");
        jenis.add("Backup Dan Restore");
        List<Otoritas> tempOtoritas = new ArrayList<>();
        for(String jns : jenis){
            Otoritas temp = new Otoritas();
            temp.setUsername(user.getUsername());
            temp.setJenis(jns);
            temp.setStatus(checkOtoritas.isSelected());
            tempOtoritas.add(temp);
        }
        List<String> ver = new ArrayList<>();
        ver.add("Edit Barang");
        ver.add("Hancur Barang");
        ver.add("Batal Barcode");
        ver.add("Penjualan");
        ver.add("Batal Penjualan");
        ver.add("Pembelian");
        ver.add("Batal Pembelian");
        ver.add("Terima Gadai");
        ver.add("Batal Terima Gadai");
        ver.add("Pelunasan Gadai");
        ver.add("Batal Pelunasan Gadai");
        ver.add("Batal Transaksi");
        List<Verifikasi> tempverifikasi = new ArrayList<>();
        for(String v : ver){
            Verifikasi temp = new Verifikasi();
            temp.setUsername(user.getUsername());
            temp.setJenis(v);
            temp.setStatus(checkVerifikasi.isSelected());
            tempverifikasi.add(temp);
        }
        otoritas.clear();
        otoritas.addAll(tempOtoritas);
        verifikasi.clear();
        verifikasi.addAll(tempverifikasi);
    }    
    @FXML
    private void saveUser(){
        if(status.equals("update")){
            if(user==null || usernameField.getText().equals("")){
                mainApp.showMessage(Modality.NONE, "Warning", "User belum dipilih");
            }else{
                user.setPassword(passwordField.getText());
                user.setLevel(levelCombo.getSelectionModel().getSelectedItem());
                for(Otoritas temp : otoritas){
                    temp.setUsername(usernameField.getText());
                }
                user.setOtoritas(otoritas);
                for(Verifikasi temp : verifikasi){
                    temp.setUsername(usernameField.getText());
                }
                user.setVerifikasi(verifikasi);
                MessageController controller = mainApp.showMessage(Modality.WINDOW_MODAL, "Confirmation",
                        "Update user "+user.getUsername()+" ?");
                controller.OK.setOnAction((ActionEvent ev) -> {
                    try(Connection con = Koneksi.getConnection()){
                        String message = Service.saveUpdateUser(con, user);
                        if(message.equals("true")){
                            mainApp.showMessage(Modality.NONE, "Success", "Data user berhasil disimpan");
                            reset();
                        }else{
                            mainApp.showMessage(Modality.NONE, "Error", message);
                        }
                    }catch(Exception e){
                        mainApp.showMessage(Modality.NONE, "Error", e.toString());
                    }
                });
                controller.cancel.setOnAction((ActionEvent e) -> {
                    mainApp.closeMessage();
                });
            }
        }else if(status.equals("new")){
            if(user==null || usernameField.getText().equals("")){
                mainApp.showMessage(Modality.NONE, "Warning", "username masih kosong");
            }else if ("".equals(levelCombo.getSelectionModel().getSelectedItem())){
                mainApp.showMessage(Modality.NONE, "Warning", "Level user belum dipilih");
            }else{
                user.setUsername(usernameField.getText());
                user.setPassword(passwordField.getText());
                user.setLevel(levelCombo.getSelectionModel().getSelectedItem());
                for(Otoritas temp:otoritas){
                    temp.setUsername(usernameField.getText());
                }
                user.setOtoritas(otoritas);
                for(Verifikasi temp : verifikasi){
                    temp.setUsername(usernameField.getText());
                }
                user.setVerifikasi(verifikasi);
                MessageController controller = mainApp.showMessage(Modality.WINDOW_MODAL, "Confirmation",
                        "Save new user "+user.getUsername()+" ?");
                controller.OK.setOnAction((ActionEvent ev) -> {
                    try(Connection con = Koneksi.getConnection()){
                        String message = Service.saveNewUser(con, user);
                        if(message.equals("true")){
                            mainApp.showMessage(Modality.NONE, "Success", "Data user berhasil disimpan");
                            reset();
                        }else{
                            mainApp.showMessage(Modality.NONE, "Error", message);
                        }
                    }catch(Exception e){
                        mainApp.showMessage(Modality.NONE, "Error", e.toString());
                    }
                });
            }
        }
    }    
    @FXML
    private void deleteUser(User user){
        MessageController controller = mainApp.showMessage(Modality.WINDOW_MODAL, "Confirmation",
                "Delete user "+user.getUsername()+" ?");
        controller.OK.setOnAction((ActionEvent ev) -> {
            try(Connection con = Koneksi.getConnection()){
                String message = Service.deleteUser(con, user);
                if(message.equals("true")){
                    mainApp.showMessage(Modality.NONE, "Success", "Data user berhasil dihapus");
                    reset();
                }else
                    mainApp.showMessage(Modality.NONE, "Error", message);
            }catch(Exception e){
                mainApp.showMessage(Modality.NONE, "Error", e.toString());
            }
        });
    }
    
}
