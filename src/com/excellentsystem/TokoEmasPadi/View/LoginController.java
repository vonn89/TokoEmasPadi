/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.View;

import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.allUser;
import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import com.excellentsystem.TokoEmasPadi.Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author Xtreme
 */
public class LoginController {

    @FXML private Label versionLabel;
    @FXML private Label namaPerusahaanLabel;
    @FXML private Label warningLabel;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    private Main mainApp;
    private int attempt = 0;
    public void initialize() {
        usernameField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  
                passwordField.requestFocus();
        });
        passwordField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  
                handleLoginButton();
        });
    }    
    public void setMainApp(Main mainApp){
        this.mainApp = mainApp;
        versionLabel.setText("Ver. "+Main.version);
        namaPerusahaanLabel.setText(sistem.getNama().toUpperCase());
        warningLabel.setText("");
    }
    @FXML 
    private void handleLoginButton(){
        if("".equals(usernameField.getText())){
            warningLabel.setText("Username belum dipilih");
        }else if(passwordField.getText().equals("")){
            warningLabel.setText("Password masih kosong");
        }else if(attempt>=3){
            System.exit(0);
        }else{
            User temp = null;
            for(User u : allUser){
                if(u.getUsername().equals(usernameField.getText()))
                    temp = u;
            }
            if(temp!=null){
                if(!temp.getPassword().equals(passwordField.getText())){
                    warningLabel.setText("Password masih salah");
                    attempt = attempt +1;
                }else{
                    Main.user = temp;
                    mainApp.showMainScene();
                    mainApp.mainController.setUser();
                }
            }else{
                warningLabel.setText("Username masih salah");
            }
        }
    }
    @FXML 
    private void frontDeskApp(){
        Main.user = null;
        mainApp.showMainScene();
        mainApp.mainController.setUser();
    }
    @FXML
    private void close(){
        System.exit(3);
    }
   
    
}
