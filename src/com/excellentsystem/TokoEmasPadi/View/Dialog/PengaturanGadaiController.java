/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Dialog;

import com.excellentsystem.TokoEmasPadi.DAO.BungaGadaiDAO;
import com.excellentsystem.TokoEmasPadi.DAO.SistemDAO;
import com.excellentsystem.TokoEmasPadi.Function;
import static com.excellentsystem.TokoEmasPadi.Function.getTableCell;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.gr;
import static com.excellentsystem.TokoEmasPadi.Main.rp;
import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import com.excellentsystem.TokoEmasPadi.Model.BungaGadai;
import com.excellentsystem.TokoEmasPadi.Model.Sistem;
import java.sql.Connection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
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
public class PengaturanGadaiController  {

    @FXML private TableView<BungaGadai> bungaGadaiTable;
    @FXML private TableColumn<BungaGadai, Number> jumlahPinjamanMinColumn;
    @FXML private TableColumn<BungaGadai, Number> jumlahPinjamanMaxColumn;
    @FXML private TableColumn<BungaGadai, Number> bungaGadaiColumn;
    
    @FXML private TextField jumlahPinjamanMinField;
    @FXML private TextField jumlahPinjamanMaxField;
    @FXML private TextField bungaGadaiField;
    @FXML private TextField jatuhTempoField;
    @FXML private TextField persentasePinjamanField;
    private ObservableList<BungaGadai> allBungaGadai = FXCollections.observableArrayList();
    private Main mainApp;
    private Stage stage;
    public void initialize() {
        jumlahPinjamanMinColumn.setCellValueFactory(cellData -> cellData.getValue().minProperty());
        jumlahPinjamanMinColumn.setCellFactory(col -> getTableCell(rp, "Rp"));
        jumlahPinjamanMaxColumn.setCellValueFactory(cellData -> cellData.getValue().maxProperty());
        jumlahPinjamanMaxColumn.setCellFactory(col -> getTableCell(rp, "Rp"));
        bungaGadaiColumn.setCellValueFactory(cellData -> cellData.getValue().bungaProperty());
        bungaGadaiColumn.setCellFactory(col -> getTableCell(gr, ""));
        
        Function.setNumberField(jumlahPinjamanMinField, rp);
        Function.setNumberField(jumlahPinjamanMaxField, rp);
        Function.setNumberField(bungaGadaiField, gr);
        
        jumlahPinjamanMinField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                jumlahPinjamanMaxField.selectAll();
                jumlahPinjamanMaxField.requestFocus();
            }
        });
        jumlahPinjamanMaxField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                bungaGadaiField.selectAll();
                bungaGadaiField.requestFocus();
            }
        });
        final ContextMenu menu = new ContextMenu();
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction((ActionEvent event) -> {
            bungaGadaiTable.refresh();
        });

        menu.getItems().addAll(refresh);
        bungaGadaiTable.setContextMenu(menu);
        bungaGadaiTable.setRowFactory(table -> {
            TableRow<BungaGadai> row = new TableRow<BungaGadai>(){
                @Override
                public void updateItem(BungaGadai item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            bungaGadaiTable.refresh();
                        });
                        
                        rowMenu.getItems().addAll(refresh);
                        setContextMenu(rowMenu);
                    }else{
                        final ContextMenu rowMenu = new ContextMenu();
                        MenuItem hapus = new MenuItem("Hapus");
                        hapus.setOnAction((ActionEvent event) -> {
                            deleteBungaGadai(item);
                        });
                        
                        MenuItem refresh = new MenuItem("Refresh");
                        refresh.setOnAction((ActionEvent event) -> {
                            bungaGadaiTable.refresh();
                        });
                        rowMenu.getItems().addAll(hapus,refresh);
                        setContextMenu(rowMenu);
                    }
                }
            };
            return row;
        });
    }    
    public void setMainApp(Main mainApp, Stage stage) {
        try(Connection con = Koneksi.getConnection()){
            this.mainApp = mainApp;
            this.stage = stage;
            allBungaGadai.addAll(BungaGadaiDAO.getAll(con));
            bungaGadaiTable.setItems(allBungaGadai);
            persentasePinjamanField.setText(gr.format(sistem.getPersentasePinjaman()));
            jatuhTempoField.setText(gr.format(sistem.getJatuhTempoGadai()));
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    @FXML
    private void close(){
        try(Connection con = Koneksi.getConnection()){
            stage.close();
            Sistem sistem = Main.sistem;
            sistem.setPersentasePinjaman(Double.parseDouble(persentasePinjamanField.getText().replaceAll(",", "")));
            sistem.setJatuhTempoGadai(Integer.parseInt(jatuhTempoField.getText().replaceAll(",", "")));
            SistemDAO.update(con, sistem);
        } catch (Exception ex) {
            mainApp.showMessage(Modality.NONE, "Error", ex.toString());
        }
    }
    private void deleteBungaGadai(BungaGadai temp){
        try(Connection con = Koneksi.getConnection()){
            allBungaGadai.remove(temp);
            BungaGadaiDAO.delete(con, temp);
            bungaGadaiTable.refresh();
            jumlahPinjamanMinField.setText("0");
            jumlahPinjamanMaxField.setText("0");
            bungaGadaiField.setText("0");
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    @FXML
    private void saveBungaGadai(){
        if(jumlahPinjamanMinField.getText().equals(""))
            mainApp.showMessage(Modality.NONE, "Warning", "Jumlah pinjaman minimal masih kosong");
        else if(jumlahPinjamanMaxField.getText().equals(""))
            mainApp.showMessage(Modality.NONE, "Warning", "Jumlah pinjaman maksimal masih kosong");
        else if(bungaGadaiField.getText().equals(""))
            mainApp.showMessage(Modality.NONE, "Warning", "Bunga gadai masih kosong");
        else if(Double.parseDouble(jumlahPinjamanMinField.getText().replaceAll(",", ""))>
                Double.parseDouble(jumlahPinjamanMaxField.getText().replaceAll(",", "")))
            mainApp.showMessage(Modality.NONE, "Warning", "Min. pinjaman lebih besar dari max. pinjaman");
        else{
            try(Connection con = Koneksi.getConnection()){
                BungaGadai bunga = new BungaGadai();
                bunga.setMin(Double.parseDouble(jumlahPinjamanMinField.getText().replaceAll(",", "")));
                bunga.setMax(Double.parseDouble(jumlahPinjamanMaxField.getText().replaceAll(",", "")));
                bunga.setBunga(Double.parseDouble(bungaGadaiField.getText().replaceAll(",", "")));
                BungaGadaiDAO.insert(con, bunga);
                allBungaGadai.add(bunga);
                bungaGadaiTable.refresh();
                jumlahPinjamanMinField.setText("0");
                jumlahPinjamanMaxField.setText("0");
                bungaGadaiField.setText("0");
            }catch(Exception e){
                mainApp.showMessage(Modality.NONE, "Error", e.toString());
            }
        }
    }
}
