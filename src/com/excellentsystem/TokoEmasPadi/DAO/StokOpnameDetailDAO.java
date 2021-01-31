/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.TokoEmasPadi.DAO;

import com.excellentsystem.TokoEmasPadi.Model.StokOpnameDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yunaz
 */
public class StokOpnameDetailDAO {
    
    public static List<StokOpnameDetail> getAll(Connection con, String noStokOpname)throws Exception{
        PreparedStatement ps = con.prepareStatement("select * from tt_stok_opname_detail where no_stok_opname = ?");
        ps.setString(1, noStokOpname);
        ResultSet rs = ps.executeQuery();
        List<StokOpnameDetail> allStokOpnameDetail = new ArrayList<>();
        while(rs.next()){
            StokOpnameDetail s = new StokOpnameDetail();
            s.setNoStokOpname(rs.getString(1));
            s.setNoUrut(rs.getInt(2));
            s.setKodeBarcode(rs.getString(3));
            s.setQty(rs.getInt(4));
            s.setQtyDiStok(rs.getInt(5));
            allStokOpnameDetail.add(s);
        }
        return allStokOpnameDetail;
    }
    public static void insert(Connection con, StokOpnameDetail s)throws Exception{
        PreparedStatement ps = con.prepareStatement("insert into tt_stok_opname_detail values (?,?,?,?,?)");
        ps.setString(1, s.getNoStokOpname());
        ps.setInt(2, s.getNoUrut());
        ps.setString(3, s.getKodeBarcode());
        ps.setInt(4, s.getQty());
        ps.setInt(5, s.getQtyDiStok());
        ps.executeUpdate();
    }
    public static void update(Connection con, StokOpnameDetail s)throws Exception{
        PreparedStatement ps = con.prepareStatement("update tt_stok_opname_detail set kode_barcode=?, qty=?, qty_distok=? "
                + " where no_stok_opname=? and no_urut=? ");
        ps.setString(1, s.getKodeBarcode());
        ps.setInt(2, s.getQty());
        ps.setInt(3, s.getQtyDiStok());
        ps.setString(4, s.getNoStokOpname());
        ps.setInt(5, s.getNoUrut());
        ps.executeUpdate();
    }
}
