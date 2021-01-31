/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.DAO;

import com.excellentsystem.TokoEmasPadi.Model.BungaGadai;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yunaz
 */
public class BungaGadaiDAO {
    
    public static List<BungaGadai> getAll(Connection con)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tm_bunga_gadai ");
        ResultSet rs = ps.executeQuery();
        List<BungaGadai> allBungaGadai = new ArrayList<>();
        while(rs.next()){
            BungaGadai b = new BungaGadai();
            b.setMin(rs.getDouble(1));
            b.setMax(rs.getDouble(2));
            b.setBunga(rs.getDouble(3));
            allBungaGadai.add(b);
        }
        return allBungaGadai;
    }
    public static void insert(Connection con, BungaGadai b)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tm_bunga_gadai values(?,?,?)");
        ps.setDouble(1, b.getMin());
        ps.setDouble(2, b.getMax());
        ps.setDouble(3, b.getBunga());
        ps.executeUpdate();
    }
    public static void delete(Connection con, BungaGadai b)throws Exception{
        PreparedStatement ps = con.prepareStatement("delete from tm_bunga_gadai where min=? and max=? and bunga=? ");
        ps.setDouble(1, b.getMin());
        ps.setDouble(2, b.getMax());
        ps.setDouble(3, b.getBunga());
        ps.executeUpdate();
    }
}
