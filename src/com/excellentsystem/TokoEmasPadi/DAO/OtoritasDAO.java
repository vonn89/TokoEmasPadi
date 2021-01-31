/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.excellentsystem.TokoEmasPadi.DAO;

import com.excellentsystem.TokoEmasPadi.Model.Otoritas;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Xtreme
 */
public class OtoritasDAO {

    public static List<Otoritas> getAll(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_otoritas");
        ResultSet rs = ps.executeQuery();
        List<Otoritas> allOtoritas = new ArrayList<>();
        while(rs.next()){
            Otoritas o = new Otoritas();
            o.setUsername(rs.getString(1));
            o.setJenis(rs.getString(2));
            o.setStatus(Boolean.parseBoolean(rs.getString(3)));
            allOtoritas.add(o);
        }
        return allOtoritas;
    }
    public static void update(Connection con, Otoritas o)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tm_otoritas set status=? where username=? and jenis=?");
        ps.setString(1, String.valueOf(o.isStatus()));
        ps.setString(2, o.getUsername());
        ps.setString(3, o.getJenis());
        ps.executeUpdate();
    }
    public static void insert(Connection con, Otoritas o)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tm_otoritas values(?,?,?)");
        ps.setString(1, o.getUsername());
        ps.setString(2, o.getJenis());
        ps.setString(3, String.valueOf(o.isStatus()));
        ps.executeUpdate();
    }
    public static void deleteAllByUsername(Connection con, String username)throws Exception{
        PreparedStatement ps = con.prepareStatement("delete from tm_otoritas where username=?");
        ps.setString(1, username);
        ps.executeUpdate();
    }
}
