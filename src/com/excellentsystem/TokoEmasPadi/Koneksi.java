package com.excellentsystem.TokoEmasPadi;

import static com.excellentsystem.TokoEmasPadi.Main.ipServer;
import java.sql.Connection;
import java.sql.DriverManager;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Xtreme
 */
public class Koneksi {
    public static Connection getConnection()throws Exception{
        String DbName = "jdbc:mysql://"+ipServer+":3306/tokoemaspadi?"
                + "connectTimeout=5000&socketTimeout=5000";
        Class.forName("com.mysql.jdbc.Driver");
        DriverManager.setLoginTimeout(5);
        return DriverManager.getConnection(DbName,"admin","excellentsystem");
    }
}
