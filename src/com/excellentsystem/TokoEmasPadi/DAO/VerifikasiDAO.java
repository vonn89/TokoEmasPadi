/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.DAO;

import com.excellentsystem.TokoEmasPadi.Model.Verifikasi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Xtreme
 */
public class VerifikasiDAO {
    
    public static List<Verifikasi> getAll(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_verifikasi");
        ResultSet rs = ps.executeQuery();
        List<Verifikasi> allVerifikasi = new ArrayList<>();
        while(rs.next()){
            Verifikasi v = new Verifikasi();
            v.setUsername(rs.getString(1));
            v.setJenis(rs.getString(2));
            v.setStatus(Boolean.parseBoolean(rs.getString(3)));
            allVerifikasi.add(v);
        }
        return allVerifikasi;
    }
    public static void update(Connection con, Verifikasi v)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tm_verifikasi set status=? where username=? and jenis=?");
        ps.setString(1, String.valueOf(v.isStatus()));
        ps.setString(2, v.getUsername());
        ps.setString(3, v.getJenis());
        ps.executeUpdate();
    }
    public static void insert(Connection con, Verifikasi v)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tm_verifikasi values(?,?,?)");
        ps.setString(1, v.getUsername());
        ps.setString(2, v.getJenis());
        ps.setString(3, String.valueOf(v.isStatus()));
        ps.executeUpdate();
    }
    public static void deleteAllByUsername(Connection con, String username)throws Exception{
        PreparedStatement ps = con.prepareStatement("delete from tm_verifikasi where username=?");
        ps.setString(1, username);
        ps.executeUpdate();
    }
}
