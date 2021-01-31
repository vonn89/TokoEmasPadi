/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Dialog;

import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.ipServer;
import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Xtreme
 */
public class BackupAndRestoreController {

    private Main mainApp;
    private Stage stage;

    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
    }

    @FXML
    private void cancel() {
        stage.close();
    }

    @FXML
    private void backup() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select location to backup");
            fileChooser.setInitialFileName("Backup database - " + sistem.getTglSystem());
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("zip file", "*.zip"));
            File backupfile = fileChooser.showSaveDialog(stage);

            File file = new File("temp.sql");
            String executeCmd = "mysqldump --host " + ipServer + " -P 3306 "
                    + " -uadmin -pexcellentsystem --database tokoemaspadi -r \"" + file.getPath() + "\"";
            System.out.println(executeCmd);
            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
                System.out.println("0");
            int processComplete = runtimeProcess.waitFor();
            if (processComplete == 0) {
                System.out.println("1");
                FileOutputStream fos = new FileOutputStream(backupfile.getPath());
                ZipOutputStream zipOut = new ZipOutputStream(fos);
                FileInputStream fis = new FileInputStream(file);
                ZipEntry zipEntry = new ZipEntry(file.getName());
                System.out.println("2");
                zipOut.putNextEntry(zipEntry);
                final byte[] bytes = new byte[1024];
                System.out.println("3");
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                System.out.println("4");
                zipOut.close();
                fis.close();
                fos.close();
                file.delete();
                mainApp.showMessage(Modality.NONE, "Success", "Backup database berhasil");
            } else {
                mainApp.showMessage(Modality.NONE, "Failed", "Backup database gagal");
            }
        } catch (Exception ex) {
            mainApp.showMessage(Modality.NONE, "Error", ex.toString());
        }
    }

    @FXML
    private void restore() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select file to restore");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("sql file", "*.sql"));
            File sqlFile = fileChooser.showOpenDialog(stage);

            String[] executeCmd = new String[]{"mysql", "tokoemaspadi", "-u" + "admin", "-p" + "excellentsystem", "-e", " source " + sqlFile};

            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();

            if (processComplete == 0) {
                mainApp.showMessage(Modality.NONE, "Success", "Restore database berhasil");
                mainApp.MainStage.close();
                mainApp.start(new Stage());
            } else {
                mainApp.showMessage(Modality.NONE, "Failed", "Restore database gagal");
            }
        } catch (Exception ex) {
            mainApp.showMessage(Modality.NONE, "Error", ex.toString());
        }
    }
}
