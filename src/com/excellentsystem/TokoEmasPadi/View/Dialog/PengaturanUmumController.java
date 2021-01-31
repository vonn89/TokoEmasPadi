/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Dialog;

import com.excellentsystem.TokoEmasPadi.DAO.SistemDAO;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.ipServer;
import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import com.excellentsystem.TokoEmasPadi.Model.Sistem;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Xtreme
 */
public class PengaturanUmumController {

    @FXML private TextField namaField;
    @FXML private TextField alamatField;
    @FXML private TextField kotaField;
    @FXML private TextField noTelpField;
    @FXML private TextField emailField;
    @FXML private TextField prefixBarcodeField;
    @FXML private TextField serverField;
    @FXML private TextField printerPenjualanField;
    @FXML private TextField printerGadaiField;
    @FXML private TextField printerBarcodeField;
    private Main mainApp;
    private Stage stage;
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        setDataPerusahaan();
    }
    @FXML
    private void setDataPerusahaan(){
        try{
            namaField.setText(sistem.getNama());
            alamatField.setText(sistem.getAlamat());
            kotaField.setText(sistem.getKota());
            noTelpField.setText(sistem.getNoTelp());
            emailField.setText(sistem.getWebsite());
            prefixBarcodeField.setText(sistem.getCode());
            BufferedReader in = new BufferedReader(new FileReader("koneksi.txt"));
            String ipServer = in.readLine();
            String printerPenjualan = in.readLine();
            String printerGadai = in.readLine();
            String printerBarcode = in.readLine();
            serverField.setText(ipServer);
            printerPenjualanField.setText(printerPenjualan);
            printerGadaiField.setText(printerGadai);
            printerBarcodeField.setText(printerBarcode);
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
    @FXML 
    private void cancel(){
        stage.close();
    }
    @FXML
    private void backup(){
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select location to backup");
            fileChooser.setInitialFileName("Backup database - "+sistem.getTglSystem());
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("zip file", "*.zip"));
            File backupfile = fileChooser.showSaveDialog(stage);
            
            File file = new File("temp.sql");
            String executeCmd = "mysqldump --host "+ipServer+" -P 3306 "
                    + " -u admin -pexcellentsystem tokoemaspadi -r \"" + file.getPath()+"\"";
            System.out.println(executeCmd);
            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();
            if (processComplete == 0) {
                FileOutputStream fos = new FileOutputStream(backupfile.getPath());
                ZipOutputStream zipOut = new ZipOutputStream(fos);
                FileInputStream fis = new FileInputStream(file);
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zipOut.putNextEntry(zipEntry);
                final byte[] bytes = new byte[1024];
                int length;
                while((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                zipOut.close();
                fis.close();
                fos.close();
                file.delete();
                mainApp.showMessage(Modality.NONE, "Success", "Backup database berhasil");
            } else {
                mainApp.showMessage(Modality.NONE, "Success", "Backup database gagal");
            }
        } catch (Exception ex) {
                mainApp.showMessage(Modality.NONE, "Error", ex.toString());
        }
    }
    @FXML
    private void restore(){
    
    }
    @FXML
    private void saveDataPerusahaan(){
        try(Connection con = Koneksi.getConnection()){
            Sistem sistem = Main.sistem;
            sistem.setNama(namaField.getText());
            sistem.setAlamat(alamatField.getText());
            sistem.setKota(kotaField.getText());
            sistem.setNoTelp(noTelpField.getText());
            sistem.setWebsite(emailField.getText());
            sistem.setCode(prefixBarcodeField.getText());
            try (BufferedWriter out = new BufferedWriter(new FileWriter("koneksi.txt"))) {
                out.write(serverField.getText());
                out.newLine();
                out.write(printerPenjualanField.getText());
                out.newLine();
                out.write(printerGadaiField.getText());
                out.newLine();
                out.write(printerBarcodeField.getText());
            }
            SistemDAO.update(con, sistem);
            MessageController controller = mainApp.showMessage(Modality.APPLICATION_MODAL, "Confirmation",
                    "Setting baru berhasil disimpan,\nto take effect please restart program");
            controller.OK.setOnAction((ActionEvent event) -> {
                try{
                    mainApp.MainStage.close();
                    mainApp.start(new Stage());
                }catch(Exception e){
                    System.exit(0);
                }
            });
        }catch(Exception e){
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }
}
