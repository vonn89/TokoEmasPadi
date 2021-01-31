/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.excellentsystem.TokoEmasPadi.View.Dialog;

import com.excellentsystem.TokoEmasPadi.DAO.UserDAO;
import com.excellentsystem.TokoEmasPadi.Koneksi;
import com.excellentsystem.TokoEmasPadi.Main;
import static com.excellentsystem.TokoEmasPadi.Main.user;
import java.sql.Connection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Xtreme
 */
public class UbahPasswordController  {

    @FXML public Label username;
    @FXML public PasswordField passwordLama;
    @FXML public PasswordField passwordBaru;
    @FXML public PasswordField ulangiPasswordBaru;
    @FXML public Label warning;
    private Main mainApp;
    private Stage stage;
    public void setMainApp(Main mainApp, Stage stage) {
        this.mainApp = mainApp;
        this.stage = stage;
        username.setText(user.getUsername());
        warning.setText("");
    }
    public void save(){
        if(passwordLama.getText().equals(""))
            warning.setText("password lama masih kosong");
        else if(passwordBaru.getText().equals(""))
            warning.setText("password baru masih kosong");
        else if(ulangiPasswordBaru.getText().equals(""))
            warning.setText("ulangi password baru masih kosong");
        else if(!passwordLama.getText().equals(user.getPassword()))
            warning.setText("password lama salah");
        else if(!passwordBaru.getText().equals(ulangiPasswordBaru.getText()))
            warning.setText("password baru tidak sama");
        else{
            try(Connection con = Koneksi.getConnection()){
                user.setPassword(passwordBaru.getText());
                UserDAO.update(con, user);
                mainApp.showMessage(Modality.NONE, "Success", "Password baru berhasil di simpan");
                close();
            }catch(Exception e){
                mainApp.showMessage(Modality.NONE, "Error", e.toString());
            }
        }
    }
    public void close(){
        stage.close();
    }
    
}
