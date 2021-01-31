/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.DAO;

import com.excellentsystem.TokoEmasPadi.Model.HancurDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yunaz
 */
public class HancurDetailDAO {

    public static List<HancurDetail> getAllByTglHancur(Connection con, 
            String tglMulai, String tglAkhir)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_hancur_detail where no_hancur in ("
                + " select no_hancur from tt_hancur_head where left(tgl_hancur,10) between ? and ? )");
        ps.setString(1, tglMulai);
        ps.setString(2, tglAkhir);
        ResultSet rs = ps.executeQuery();
        List<HancurDetail> allHancurDetail = new ArrayList<>();
        while(rs.next()){
            HancurDetail h = new HancurDetail();
            h.setNoHancur(rs.getString(1));
            h.setNoUrut(rs.getInt(2));
            h.setKodeBarcode(rs.getString(3));
            h.setQty(rs.getInt(4));
            allHancurDetail.add(h);
        }
        return allHancurDetail;
    }
    public static void insert(Connection con, HancurDetail h)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_hancur_detail values (?,?,?,?)");
        ps.setString(1, h.getNoHancur());
        ps.setInt(2, h.getNoUrut());
        ps.setString(3, h.getKodeBarcode());
        ps.setInt(4, h.getQty());
        ps.executeUpdate();
    }
}
