/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View.Dialog;

import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import com.excellentsystem.TokoEmasPadi.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Yunaz
 */
public class VerifikasiController  {

    public Label keteranganLabel;
    public PasswordField password;
    public void initialize() {
    }   
    private Main mainApp;
    private Stage stage;
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
    }
    @FXML
    private void close(){
        stage.close();
    }
}
