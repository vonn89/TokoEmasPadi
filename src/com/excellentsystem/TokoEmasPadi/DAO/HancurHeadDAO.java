/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.DAO;

import static com.excellentsystem.TokoEmasPadi.Main.sistem;
import static com.excellentsystem.TokoEmasPadi.Main.tglBarang;
import static com.excellentsystem.TokoEmasPadi.Main.tglKode;
import com.excellentsystem.TokoEmasPadi.Model.HancurHead;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yunaz
 */
public class HancurHeadDAO {

    
    public static List<HancurHead> getAllByTglHancur(Connection con, String tglMulai, String tglAkhir)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_hancur_head "
                + " where left(tgl_hancur,10) between ? and ? ");
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<HancurHead> allHancurHead = new ArrayList<>();
        while(rs.next()){
            HancurHead h = new HancurHead();
            h.setNoHancur(rs.getString(1));
            h.setTglHancur(rs.getString(2));
            h.setTotalQty(rs.getInt(3));
            h.setTotalBerat(rs.getDouble(4));
            h.setTotalBeratPembulatan(rs.getDouble(5));
            h.setKodeUser(rs.getString(6));
            allHancurHead.add(h);
        }
        return allHancurHead;
    }
    public static void insert(Connection con, HancurHead h)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_hancur_head values (?,?,?,?,?,?)");
        ps.setString(1, h.getNoHancur());
        ps.setString(2, h.getTglHancur());
        ps.setInt(3, h.getTotalQty());
        ps.setDouble(4, h.getTotalBerat());
        ps.setDouble(5, h.getTotalBeratPembulatan());
        ps.setString(6, h.getKodeUser());
        ps.executeUpdate();
    }
    public static String getId(Connection con)throws Exception{
        PreparedStatement ps =  con.prepareStatement("select max(right(no_hancur,4)) from tt_hancur_head "
                + " where mid(no_hancur,4,6)=? ");
        DecimalFormat df = new DecimalFormat("0000");
        ps.setString(1, tglKode.format(tglBarang.parse(sistem.getTglSystem())));
        ResultSet rs = ps.executeQuery();
        if(rs.next())
            return "HB-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+"-"+df.format(rs.getInt(1)+1);
        else
            return "HB-"+tglKode.format(tglBarang.parse(sistem.getTglSystem()))+"-"+df.format(1);
    }
}
